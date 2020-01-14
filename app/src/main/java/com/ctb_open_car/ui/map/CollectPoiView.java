package com.ctb_open_car.ui.map;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.PoiItem;
import com.ctb_open_car.R;
import com.ctb_open_car.constraints.AppContraint;
import com.ctb_open_car.engine.manager.GsonManager;
import com.ctb_open_car.presenter.CollectPoiPresenter;
import com.ctb_open_car.ui.BaseView;
import com.ctb_open_car.ui.RecycleViewDivider;
import com.ctb_open_car.utils.PreferenceUtils;
import com.ctb_open_car.view.activity.map.FindAddressActivity;
import com.ctb_open_car.view.activity.map.NaviActivity;
import com.ctb_open_car.view.adapter.map.PoiCollectAdapter;
import com.ctb_open_car.view.fragment.dialog.PoiEditDialog;

import java.lang.ref.SoftReference;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectPoiView implements BaseView, PoiEditDialog.ActionCallback {

    @BindView(R.id.collect_home)
    View mCollectHome;
    @BindView(R.id.collect_company)
    View mCollectCompany;
    @BindView(R.id.collect_poi_list)
    RecyclerView mCollectPoiList;


    public CollectPoiPresenter mCollectPoiPresenter;

    public SoftReference<AppCompatActivity> mCollectActivity;
    private TextView mCompanyAddress;
    private TextView mHomeAddress;
    private String mCompany;
    private String mCompanyPoi;
    private String mHome;
    private String mHomePoi;

    private PoiCollectAdapter mAdapter;

    @Inject
    public CollectPoiView(AppCompatActivity collectPoiActivity) {
        this.mCollectActivity = new SoftReference<>(collectPoiActivity);
        ButterKnife.bind(this, mCollectActivity.get());

        initView();
        initData();
        initClick();
    }
    private void initData() {
        mCompany = PreferenceUtils.getString(mCollectActivity.get(), AppContraint.POICollect.COMPANY_ADDRESS);
        mCompanyPoi = PreferenceUtils.getString(mCollectActivity.get(), AppContraint.POICollect.HOME_POI);

        mHome = PreferenceUtils.getString(mCollectActivity.get(), AppContraint.POICollect.HOME_ADDRESS);
        mHomePoi = PreferenceUtils.getString(mCollectActivity.get(), AppContraint.POICollect.HOME_POI);

        if (!TextUtils.isEmpty(mCompany)) {
            setCompanyName(mCompany);
        }

        if (!TextUtils.isEmpty(mHome)) {
            setHomeName(mHome);
        }
    }

    public void initClick() {
        PoiEditDialog editDialog = new PoiEditDialog(mCollectActivity.get(), this);
        /**公司编辑按钮点击*/
        mCollectCompany.findViewById(R.id.collect_address_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog.setType(mCollectActivity.get().getResources().getString(R.string.poi_type_company));
                editDialog.show(mCollectActivity.get().getSupportFragmentManager(), "poi_edit");

            }
        });
        /**家编辑按钮点击*/
        mCollectHome.findViewById(R.id.collect_address_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog.setType(mCollectActivity.get().getResources().getString(R.string.poi_type_home));

                editDialog.show(mCollectActivity.get().getSupportFragmentManager(), "poi_edit");

            }
        });

        /**公司item点击*/
        mCollectCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();

                if (TextUtils.isEmpty(mCompany)) {
                    i.setClass(mCollectActivity.get(), FindAddressActivity.class);
                    i.putExtra("type", AppContraint.POICollect.LOCATION_COMPANY_RESULT_CODE);

                    mCollectActivity.get().startActivityForResult(i, AppContraint.POICollect.LOCATION_COMPANY_RESULT_CODE);
                } else {
                    LatLng latLng = GsonManager.getInstance().GsonToBean(mCompanyPoi, LatLng.class);
                    NaviLatLng naviLatLng = new NaviLatLng();
                    naviLatLng.setLatitude(latLng.latitude);
                    naviLatLng.setLongitude(latLng.longitude);
                    i.setClass(mCollectActivity.get(), NaviActivity.class);
                    i.putExtra("lat_lng", naviLatLng);
                    mCollectActivity.get().startActivity(i);
//                    mCollectActivity.get().startActivityForResult(i, AppContraint.POICollect.LOCATION_COMPANY_RESULT_CODE);


                }
            }
        });

        /**家item点击*/
        mCollectHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();

                if (TextUtils.isEmpty(mHome)) {
                    i.setClass(mCollectActivity.get(), FindAddressActivity.class);
                    i.putExtra("type", AppContraint.POICollect.LOCATION_HOME_RESULT_CODE);
                    mCollectActivity.get().startActivityForResult(i, AppContraint.POICollect.LOCATION_HOME_RESULT_CODE);

                } else {
                    LatLng latLng = GsonManager.getInstance().GsonToBean(mHomePoi, LatLng.class);
                    NaviLatLng naviLatLng = new NaviLatLng();
                    naviLatLng.setLatitude(latLng.latitude);
                    naviLatLng.setLongitude(latLng.longitude);
                    i.setClass(mCollectActivity.get(), NaviActivity.class);
                    i.putExtra("lat_lng", naviLatLng);
                    mCollectActivity.get().startActivityForResult(i, AppContraint.POICollect.LOCATION_HOME_RESULT_CODE);
                }
            }
        });

    }


    /**
     * 选择公司地址后，设置列表页公司地址
     */
    public void setCompanyName(String address) {
        mCompanyAddress.setText(address);
    }

    /**
     * 选择家的地址后，设置列表页家地址
     */
    public void setHomeName(String address) {
        mHomeAddress.setText(address);
    }

    private void initView() {
        TextView companyTip = mCollectCompany.findViewById(R.id.collect_address);
        TextView companyName = mCollectCompany.findViewById(R.id.collect_name);
        ImageView companyIcon = mCollectCompany.findViewById(R.id.icon_collect);
        mCompanyAddress = mCollectCompany.findViewById(R.id.collect_address);
        mHomeAddress = mCollectHome.findViewById(R.id.collect_address);
        companyIcon.setImageDrawable(mCollectActivity.get().getResources().getDrawable(R.drawable.icon_company));
        companyName.setText("公司");
        companyTip.setText("点击设置公司地址");


        /**获取收藏地址列表*/
        String normarList = PreferenceUtils.getString(mCollectActivity.get(), AppContraint.POICollect.NORMAL_ADDRESS);

        LinearLayoutManager ll = new LinearLayoutManager(mCollectActivity.get());
        ll.setOrientation(RecyclerView.VERTICAL);
        mCollectPoiList.addItemDecoration(new RecycleViewDivider(mCollectActivity.get(), DividerItemDecoration.VERTICAL, Color.parseColor("#F5F5F5"), 2));
        mCollectPoiList.setLayoutManager(ll);
        mAdapter = new PoiCollectAdapter(mCollectActivity.get());
        mCollectPoiList.setAdapter(mAdapter);
        if (!TextUtils.isEmpty(normarList)) {
            List<PoiItem> poiItemList = GsonManager.getInstance().GsonToList(normarList, PoiItem.class);
            mAdapter.setData(poiItemList);
        }
    }

    public void updateData(List<PoiItem> poiItemList) {
        mAdapter.setData(poiItemList);

    }

    /**
     * 选择弹框编辑回调
     *
     * @Param type 点击选择类型：公司或者家
     */
    @Override
    public void onEditListener(int type) {
        if (AppContraint.POICollect.LOCATION_COMPANY_RESULT_CODE == type) {
            Intent i = new Intent();
            i.setClass(mCollectActivity.get(), FindAddressActivity.class);
            mCollectActivity.get().startActivityForResult(i, AppContraint.POICollect.LOCATION_COMPANY_RESULT_CODE);
        } else {
            Intent i = new Intent();
            i.setClass(mCollectActivity.get(), FindAddressActivity.class);
            mCollectActivity.get().startActivityForResult(i, AppContraint.POICollect.LOCATION_HOME_RESULT_CODE);
        }
    }

    /**
     * 选择弹框删除回调
     *
     * @Param type 点击选择类型：公司或者家
     */
    @Override
    public void onDeleteListener(int type) {
        if (AppContraint.POICollect.LOCATION_COMPANY_RESULT_CODE == type) {
            setCompanyName("点击设置公司地址");
            PreferenceUtils.putString(mCollectActivity.get(), AppContraint.POICollect.COMPANY_ADDRESS, "");
            PreferenceUtils.putString(mCollectActivity.get(), AppContraint.POICollect.COMPANY_POI, "");

        } else {
            setHomeName("点击设置家的地址");
            PreferenceUtils.putString(mCollectActivity.get(), AppContraint.POICollect.HOME_ADDRESS, "");
            PreferenceUtils.putString(mCollectActivity.get(), AppContraint.POICollect.HOME_POI, "");
        }
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    public void drawTitleBar() {

    }

    @Override
    public void unbind() {

    }


}
