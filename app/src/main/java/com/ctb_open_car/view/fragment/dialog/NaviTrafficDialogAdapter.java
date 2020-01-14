package com.ctb_open_car.view.fragment.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ctb_open_car.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NaviTrafficDialogAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<String> mImageList;
    public NaviTrafficDialogAdapter(Context context){
        this.mContext=context;
    }
    public void setData(List<String> imageList){
        mImageList=imageList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.layout_pic_item,parent,false);

        return new DialogHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DialogHolder dHolder= (DialogHolder) holder;
        Glide.with(mContext).load(mImageList.get(position)).into(dHolder.mImg);

    }

    @Override
    public int getItemCount() {
        if(null==mImageList){
            return 0;
        }
        return mImageList.size();
    }

    public class DialogHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.pics)
        ImageView mImg;

        public DialogHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
