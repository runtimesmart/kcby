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
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.InfoViewHolder> {

    private List<ReleaseDynamics> mCommentList;
    private Context mContext;

    private AdapterClickListener onClickListener;

    public AlbumAdapter(Context context, List<ReleaseDynamics> list) {
        mContext = context;
        mCommentList = list;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
    @Override
    public InfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InfoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.album_selecte_item, parent, false));

    }

    @Override
    public void onBindViewHolder(InfoViewHolder holder, int position) {

        Glide.with(mContext).asBitmap().load(mCommentList.get(position).getAlbumImgUrl()).error(R.drawable.album_add).into(holder.albumImage);

        if (mCommentList.get(position).getImageStatus()) {
            holder.albumUnselected.setVisibility(View.GONE);
            holder.ablumSelected.setVisibility(View.VISIBLE);
            holder.ablumSelected.setText(String.valueOf(mCommentList.get(position).getAlbumNum() +1));
        } else {
            holder.albumUnselected.setVisibility(View.VISIBLE);
            holder.ablumSelected.setVisibility(View.GONE);
        }

        holder.ablumSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClickListenerSelected(position);
                }
            }
        });

        holder.albumUnselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClickListenerSelected(position);
                }
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
        /*得到各个控件的对象*/
        public ImageView albumImage;
        public ImageView albumUnselected;
        public TextView ablumSelected;

        public InfoViewHolder(View itemView) {
            super(itemView);

            albumImage = (ImageView) itemView.findViewById(R.id.album_image);
            albumUnselected = (ImageView) itemView.findViewById(R.id.album_unselected);
            ablumSelected = (TextView) itemView.findViewById(R.id.tv_ablum_selected);
        }
    }

    public interface AdapterClickListener{
        void onClickListenerSelected(int position);

    }

}