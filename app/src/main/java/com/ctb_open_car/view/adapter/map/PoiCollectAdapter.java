package com.ctb_open_car.view.adapter.map;

import android.content.Intent;
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
import com.amap.api.services.core.PoiItem;
import com.ctb_open_car.R;
import com.ctb_open_car.constraints.AppContraint;
import com.ctb_open_car.engine.manager.GsonManager;
import com.ctb_open_car.utils.PreferenceUtils;
import com.ctb_open_car.view.activity.map.FindAddressActivity;
import com.ctb_open_car.view.activity.map.NaviActivity;
import com.ctb_open_car.view.fragment.dialog.PoiEditDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PoiCollectAdapter extends RecyclerView.Adapter {
    private AppCompatActivity mActivity;
    private List<PoiItem> mPoiList;
    private final int ADD_BUTTON_LAYOUT = 0x1;
    private final int LIST_LAYOUT = 0x2;

    private String mCurrentCity;
    private LatLng mCurrentLoc;

    public PoiCollectAdapter(AppCompatActivity mContext) {
        this.mActivity = mContext;
    }

    public void setData(List<PoiItem> poiList) {
        this.mPoiList = poiList;
        notifyDataSetChanged();
    }

    public PoiItem getItem(int p) {
        if (mPoiList.size() == p) {
            return null;
        }
        return mPoiList.get(p);
    }

    public void setCurrentLocation(String currentCity, LatLng currentLoc) {
        this.mCurrentCity = currentCity;
        this.mCurrentLoc = currentLoc;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ADD_BUTTON_LAYOUT) {
            View v = LayoutInflater.from(mActivity).inflate(R.layout.layout_collect_button, parent, false);
            return new PoiCollectAdapter.PoiViewAddHolder(v);
        } else {
            View v = LayoutInflater.from(mActivity).inflate(R.layout.activity_poi_item, parent, false);
            return new PoiCollectAdapter.PoiViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (ADD_BUTTON_LAYOUT == getItemViewType(position)) {

        } else {
            if (null == getItem(position)) {
                return;
            }
            PoiCollectAdapter.PoiViewHolder poiHolder = (PoiCollectAdapter.PoiViewHolder) holder;
            poiHolder.mPoiName.setText(getItem(position).getTitle());
            poiHolder.mPoiLocTxt.setText(getItem(position).getSnippet());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mPoiList.size()) {
            return ADD_BUTTON_LAYOUT;
        } else {
            return LIST_LAYOUT;
        }
    }

    @Override
    public int getItemCount() {
        if (null == mPoiList) {
            mPoiList = new ArrayList<>();
            return mPoiList.size() + 1;
        }
        return mPoiList.size() + 1;
    }

    public class PoiViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.poi_name)
        TextView mPoiName;

        @BindView(R.id.poi_loc_txt)
        TextView mPoiLocTxt;

        @BindView(R.id.navi_icon)
        ImageView mNaviIcon;
        @BindView(R.id.navi_edit)
        ImageView mNaviEdit;

        public PoiViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mNaviIcon.setVisibility(View.GONE);
            mNaviEdit.setVisibility(View.VISIBLE);
        }

        @OnClick({R.id.poi_panel,R.id.poi_icon})
        void onItemClick(View v) {
            int p = getAdapterPosition();
            double lat = getItem(p).getLatLonPoint().getLatitude();
            double lng = getItem(p).getLatLonPoint().getLongitude();
            Intent i = new Intent();

            NaviLatLng naviLatLng = new NaviLatLng();
            naviLatLng.setLatitude(lat);
            naviLatLng.setLongitude(lng);
            i.setClass(mActivity, NaviActivity.class);
            i.putExtra("lat_lng", naviLatLng);
            mActivity.startActivity(i);
            mActivity.finish();
        }

        @OnClick(R.id.navi_edit)
        void toEdit(View v) {
            PoiEditDialog editDialog = new PoiEditDialog(mActivity, new PoiEditDialog.ActionCallback() {
                @Override
                public void onEditListener(int type) {
                    Intent i = new Intent();
                    i.setClass(mActivity, FindAddressActivity.class);
                    i.putExtra("current_city", mCurrentCity);
                    i.putExtra("lat_lng", mCurrentLoc);
                    i.putExtra("collect_index",type);
                    mActivity.startActivityForResult(i, AppContraint.POICollect.LOCATION_NORMAL_RESULT_CODE);
                }

                @Override
                public void onDeleteListener(int type) {
                    mPoiList.remove(type);
                    PreferenceUtils.putString(mActivity, AppContraint.POICollect.NORMAL_ADDRESS, GsonManager.getInstance().GsonString(mPoiList));
                    notifyDataSetChanged();
                }
            });
            editDialog.setType(getAdapterPosition());
            editDialog.setType(getItem(getAdapterPosition()).getTitle());

            editDialog.show(mActivity.getSupportFragmentManager(), "poi_normal_edit");
        }
    }

    public class PoiViewAddHolder extends RecyclerView.ViewHolder {

        public PoiViewAddHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.collect_add_new)
        void toAdd(View v) {
            Intent i = new Intent();
            i.setClass(mActivity, FindAddressActivity.class);
            i.putExtra("current_city", mCurrentCity);
            i.putExtra("lat_lng", mCurrentLoc);
            i.putExtra("type", AppContraint.POICollect.LOCATION_HOME_RESULT_CODE);
            i.putExtra("collect_index", -1);
            mActivity.startActivityForResult(i, AppContraint.POICollect.LOCATION_NORMAL_RESULT_CODE);
        }
    }
}
