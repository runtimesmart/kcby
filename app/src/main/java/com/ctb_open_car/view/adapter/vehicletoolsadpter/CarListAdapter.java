package com.ctb_open_car.view.adapter.vehicletoolsadpter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctb_open_car.R;
import com.ctb_open_car.bean.userInfo.CarListBean;
import com.ctb_open_car.bean.vehicletools.VehicleToolsBean;
import com.ctb_open_car.contract.AccountLoginContract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.CarListHolder> {
    private Context mContext;

    private List<CarListBean.PlateDto> mCarList;
    public CarListAdapter(Context mContext,  List<CarListBean.PlateDto> carList) {
        this.mContext = mContext;
        mCarList = carList;
    }

    public CarListBean.PlateDto getItem(int p) {
        return mCarList.get(p);
    }

    @Override
    public CarListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.car_list_item, parent, false);
        return new CarListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CarListHolder holder, int position) {
        holder.mNumberPlate.setText(getItem(position).getPlate());
        if (!TextUtils.isEmpty(getItem(position).getBrand())) {
            holder.mCarType.setVisibility(View.VISIBLE);
            holder.mCarType.setText(getItem(position).getBrand());
        } else {
            holder.mCarType.setVisibility(View.GONE);
        }
    }

    public void setList(List<CarListBean.PlateDto> datas) {
        this.mCarList = datas;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if(null == mCarList){
            return Collections.EMPTY_LIST.size();
        }
        return mCarList.size();
    }

    public class CarListHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.number_plate)
        TextView mNumberPlate;
        @BindView(R.id.car_type)
        TextView mCarType;
        @BindView(R.id.car_icon)
        ImageView mCarIcon;

        public CarListHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
