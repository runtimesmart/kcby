package com.ctb_open_car.view.adapter.im;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctb_open_car.R;
import com.ctb_open_car.bean.areacode.AreaCodeDtoBean;
import com.ctb_open_car.view.activity.im.AddGroupInfoActivity;
import com.ctb_open_car.view.adapter.newsadapter.ChildViewHolder;

import java.util.List;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.CityListAdapterViewHolder> {

    private List<AreaCodeDtoBean> mCityList;
    private Context mContext;
    public CityListAdapter(Context context, List<AreaCodeDtoBean> cityList){
        mContext = context;
        mCityList = cityList;
    }
    public void setData(){
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CityListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CityListAdapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.im_city_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CityListAdapterViewHolder holder, int position) {
        holder.textView.setText(mCityList.get(position).getAreaName());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AddGroupInfoActivity)mContext).setCityName(mCityList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCityList.size();
    }

    public class CityListAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        View view;
        public CityListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.city_name);
            view = itemView.findViewById(R.id.list_item);
        }
    }
}
