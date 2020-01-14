package com.ctb_open_car.view.activity.map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseActivity;
import com.ctb_open_car.constraints.AppContraint;
import com.ctb_open_car.engine.manager.GsonManager;
import com.ctb_open_car.ui.map.CollectPoiView;
import com.ctb_open_car.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class CollectPoiActivity extends BaseActivity {

    @Inject
    CollectPoiView mCollectPoiView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colloct_poi);
        initLayout();
        setTitletName("收藏地址");
        getActivityComponent().inject(this);
    }

    /**
     * 地址收藏显示页面----回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == data) {
            return;
        }
        LatLng latLng = data.getParcelableExtra("lat_lng");
        String address = data.getStringExtra("address");
        String poiName = data.getStringExtra("poi_name");

        if (AppContraint.POICollect.LOCATION_COMPANY_RESULT_CODE == requestCode) {
            /**选择公司地址回调*/
            mCollectPoiView.setCompanyName(address);
            PreferenceUtils.putString(this, AppContraint.POICollect.COMPANY_ADDRESS, address);
            PreferenceUtils.putString(this, AppContraint.POICollect.COMPANY_POI, GsonManager.getInstance().GsonString(latLng));
        } else if (AppContraint.POICollect.LOCATION_HOME_RESULT_CODE == requestCode) {
            /**选择家的地址回调*/
            mCollectPoiView.setHomeName(address);
            PreferenceUtils.putString(this, AppContraint.POICollect.HOME_ADDRESS, address);
            PreferenceUtils.putString(this, AppContraint.POICollect.HOME_POI, GsonManager.getInstance().GsonString(latLng));
        } else if (AppContraint.POICollect.LOCATION_NORMAL_RESULT_CODE == requestCode) {
            /**选择普通的地址回调*/
            String normarList = PreferenceUtils.getString(this, AppContraint.POICollect.NORMAL_ADDRESS);
            List<PoiItem> poiItems;
            if (TextUtils.isEmpty(normarList)) {
                poiItems = new ArrayList<>();
            } else {
                poiItems = GsonManager.getInstance().GsonToList(normarList, PoiItem.class);
            }
            LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
            /**
             * 参数:
             * id - POI 的标识。
             * point - 该POI的位置。
             * title - 该POI的名称。
             * snippet - POI的地址。
             * */
            PoiItem poiItem = new PoiItem("1", latLonPoint, poiName, address);
            int updateIndex = data.getIntExtra("collect_index", -1);
            if (-1 < updateIndex) {
                poiItems.set(updateIndex, poiItem);
            } else {
                poiItems.add(poiItem);
            }

            //再次更新收藏地址列表
            String updateAddress = GsonManager.getInstance().GsonString(poiItems);
            PreferenceUtils.putString(this, AppContraint.POICollect.NORMAL_ADDRESS, updateAddress);

            mCollectPoiView.updateData(poiItems);
        }
    }

    @Override
    public Object getTag() {
        return null;
    }
}
