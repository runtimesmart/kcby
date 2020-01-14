package com.ctb_open_car.view.adapter.vehicletoolsadpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctb_open_car.R;
import com.ctb_open_car.bean.vehicletools.VehicleToolsBean;
import com.ctb_open_car.view.dialog.ProvinceCodeDialog;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProvinceCodeAdapter extends RecyclerView.Adapter {
    private Context mContext;

    private List<String> mCarViolationBeanList = Arrays.asList("京", "津", "冀", "晋", "蒙", "辽", "黑", "沪",
            "苏", "浙", "皖", "闵", "赣", "鲁", "豫", "鄂",
            "湘", "粤", "桂", "琼", "渝", "川", "贵", "云",
            "藏", "陕", "甘", "青", "宁", "吉", "新", "台");

    public ProvinceCodeAdapter(Context mContext, SelectAdpateOnListener selectListener) {
        this.mContext = mContext;
        this.mSelectListener = selectListener;
    }

    public String getItem(int p) {
        return mCarViolationBeanList.get(p);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.province_code_adpate_item, null);
        return new ViolationDetailsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViolationDetailsHolder poiHolder = (ViolationDetailsHolder) holder;
        poiHolder.mProvinceName.setText(getItem(position));
        poiHolder.mProvinceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectListener.selectListener(getItem(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        if(null == mCarViolationBeanList){
            return Collections.EMPTY_LIST.size();
        }
        return mCarViolationBeanList.size();
    }

    public class ViolationDetailsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.province)
        TextView mProvinceName;


        public ViolationDetailsHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public SelectAdpateOnListener mSelectListener;
    public interface  SelectAdpateOnListener{
        void selectListener(String select);
    }

}
