package com.ctb_open_car.view.adapter.im;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctb_open_car.R;
import com.ctb_open_car.bean.im.CarModelBean;
import com.ctb_open_car.view.activity.im.AddGroupInfoActivity;

import java.util.List;

public class CarModelAdapter extends RecyclerView.Adapter<CarModelAdapter.CarModelViewHolder> {

    private List<CarModelBean> mCarModelList;
    private Context mContext;
    public CarModelAdapter(Context context, List<CarModelBean> carModelList){
        mContext = context;
        Log.e("xxx","xxxx carModelList = " +carModelList.size());
        mCarModelList = carModelList;
    }
    public void setData(){
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CarModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CarModelViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.im_city_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CarModelViewHolder holder, int position) {
        holder.textView.setText(mCarModelList.get(position).getShowName());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AddGroupInfoActivity)mContext).setCarModelBean(mCarModelList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCarModelList.size();
    }

    public class CarModelViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        View view;
        public CarModelViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.city_name);
            view = itemView.findViewById(R.id.list_item);
        }
    }
}
