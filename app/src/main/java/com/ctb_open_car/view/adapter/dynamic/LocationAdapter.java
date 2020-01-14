package com.ctb_open_car.view.adapter.dynamic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.releasedynamics.ReleaseDynamics;

import java.util.List;

/**
 * project name RecyclerText
 * package name com.text.recyclertext
 * 相册
 */
public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.InfoViewHolder> {

    private List<String> mCommentList;
    private Context mContext;

    private AdapterClickListener onClickListener;

    public LocationAdapter(Context context, List<String> list) {
        mContext = context;
        mCommentList = list;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
    @Override
    public InfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InfoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nearb_location_item, parent, false));

    }

    @Override
    public void onBindViewHolder(InfoViewHolder holder, int position) {
        holder.locationTV.setText(mCommentList.get(position));
        holder.locationTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClickListenerSelected(mCommentList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

    public void setOnClickListener(AdapterClickListener adapterClickListener) {
        onClickListener = adapterClickListener;
    }

    public class InfoViewHolder extends RecyclerView.ViewHolder {

        public TextView locationTV;

        public InfoViewHolder(View itemView) {
            super(itemView);

            locationTV = (TextView) itemView.findViewById(R.id.location_tv);
        }
    }

    public interface AdapterClickListener{
        void onClickListenerSelected(String address);
    }

}