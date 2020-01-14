package com.ctb_open_car.view.adapter.map;

import android.content.Intent;
import android.text.TextUtils;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.ctb_open_car.R;
import com.ctb_open_car.constraints.AppContraint;
import com.ctb_open_car.engine.manager.GsonManager;
import com.ctb_open_car.utils.PreferenceUtils;
import com.ctb_open_car.view.activity.map.NaviActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PoiResultAdapter extends RecyclerView.Adapter {
    private AppCompatActivity mActivity;
    private List<PoiItem> mPoiList;
    private int mCollectIndex = -2;

    public PoiResultAdapter(AppCompatActivity mContext) {
        this.mActivity = mContext;
    }

    public PoiResultAdapter(AppCompatActivity mContext, int collect) {
        this.mActivity = mContext;
        this.mCollectIndex = collect;
    }

    public void setData(List<PoiItem> poiList) {
        this.mPoiList = poiList;
        notifyDataSetChanged();
    }

    public PoiItem getItem(int p) {
        return mPoiList.get(p);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mActivity).inflate(R.layout.activity_poi_item, null);
        return new PoiViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PoiViewHolder poiHolder = (PoiViewHolder) holder;
        poiHolder.mPoiName.setText(getItem(position).getTitle());
        poiHolder.mPoiLocTxt.setText(getItem(position).getSnippet());
    }

    @Override
    public int getItemCount() {
        if (null == mPoiList) {
            return Collections.EMPTY_LIST.size();
        }
        return mPoiList.size();
    }

    public class PoiViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.poi_name)
        TextView mPoiName;

        @BindView(R.id.poi_loc_txt)
        TextView mPoiLocTxt;

        @BindView(R.id.navi_icon)
        ImageView mNaviIcon;

        public PoiViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (mCollectIndex > -1) {
                mNaviIcon.setVisibility(View.INVISIBLE);
            }
        }

        @OnClick(R.id.poi_item)
        void onItemClick(View v) {
            int p = getAdapterPosition();
            double lat = getItem(p).getLatLonPoint().getLatitude();
            double lng = getItem(p).getLatLonPoint().getLongitude();
            Intent i = new Intent();

            if (mCollectIndex > -2) {
                LatLng latLng = new LatLng(lat, lng);
                i.putExtra("lat_lng", latLng);
                i.putExtra("address", getItem(p).getSnippet());
                i.putExtra("poi_name", getItem(p).getTitle());
                i.putExtra("collect_index", mCollectIndex);

                mActivity.setResult(AppContraint.POICollect.LOCATION_COMPANY_RESULT_CODE, i);
                mActivity.finish();
            } else {
                /**存储导航记录*/
                LinkedList<PoiItem> poiItems;
                String history = PreferenceUtils.getString(mActivity, AppContraint.SearchHistory.NAVI_HISTORY_KEY);
                if (TextUtils.isEmpty(history)) {
                    poiItems = new LinkedList<>();
                } else {
                    poiItems = GsonManager.getInstance().GsonToLinkedList(history, PoiItem.class);
                }
                LatLonPoint latLonPoint = new LatLonPoint(lat, lng);
                PoiItem poiItem = new PoiItem("2", latLonPoint, getItem(p).getTitle(), getItem(p).getSnippet());
                if (poiItems.size() >= AppContraint.SearchHistory.MAX_CACHE_SIZE) {
                    poiItems.remove(AppContraint.SearchHistory.MAX_CACHE_SIZE - 1);
                }
                if(poiItems.indexOf(poiItem)!=-1){
                    poiItems.remove(poiItems.indexOf(poiItem));
                }
                poiItems.addFirst(poiItem);

                PreferenceUtils.putString(mActivity, AppContraint.SearchHistory.NAVI_HISTORY_KEY
                        , GsonManager.getInstance().GsonString(poiItems));
                /**存储导航记录*/
                NaviLatLng naviLatLng = new NaviLatLng();
                naviLatLng.setLatitude(lat);
                naviLatLng.setLongitude(lng);
                i.setClass(mActivity, NaviActivity.class);
                i.putExtra("lat_lng", naviLatLng);
                mActivity.startActivity(i);
                mActivity.finish();
            }
        }
    }
}
