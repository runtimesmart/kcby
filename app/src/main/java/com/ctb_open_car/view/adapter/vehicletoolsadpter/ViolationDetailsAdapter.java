package com.ctb_open_car.view.adapter.vehicletoolsadpter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.PoiItem;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.vehicletools.VehicleToolsBean;
import com.ctb_open_car.view.activity.map.NaviActivity;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViolationDetailsAdapter extends RecyclerView.Adapter {
    private Context mContext;

    private List<VehicleToolsBean.CarViolationBean> mCarViolationBeanList;

    public ViolationDetailsAdapter(Context mContext, List<VehicleToolsBean.CarViolationBean> poiList) {
        this.mContext = mContext;
        this.mCarViolationBeanList = poiList;
    }

    public VehicleToolsBean.CarViolationBean getItem(int p) {
        return mCarViolationBeanList.get(p);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.handling_violations_item, null);
        return new ViolationDetailsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViolationDetailsHolder poiHolder = (ViolationDetailsHolder) holder;
        poiHolder.mVehicleTime.setText(getItem(position).getViolateTime());
        poiHolder.mWhereforeTv.setText(getItem(position).getReason());
        poiHolder.mAddressTv.setText(getItem(position).getLocation());
        poiHolder.mBuckleTv.setText(mContext.getString(R.string.buckle, getItem(position).getPoint()));
        poiHolder.mHMPenaltyTv.setText(mContext.getString(R.string.penalty,getItem(position).getPunishAmount()));

    }

    @Override
    public int getItemCount() {
        if(null == mCarViolationBeanList){
            return Collections.EMPTY_LIST.size();
        }
        return mCarViolationBeanList.size();
    }

    public class ViolationDetailsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.vehicle_time)
        TextView mVehicleTime;
        @BindView(R.id.wherefore_tv)
        TextView mWhereforeTv;
        @BindView(R.id.address_tv)
        TextView mAddressTv;
        @BindView(R.id.buckle_tv)
        TextView mBuckleTv;
        @BindView(R.id.hm_penalty_tv)
        TextView mHMPenaltyTv;

        public ViolationDetailsHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
