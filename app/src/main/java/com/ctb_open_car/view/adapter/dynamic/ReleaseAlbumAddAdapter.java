package com.ctb_open_car.view.adapter.dynamic;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.releasedynamics.ReleaseDynamics;

import java.util.List;

/**
 * project name RecyclerText
 * package name com.text.recyclertext
 * 发布动态 展示已经选择的图片
 */
public class ReleaseAlbumAddAdapter extends RecyclerView.Adapter<ReleaseAlbumAddAdapter.InfoViewHolder> {

    private List<ReleaseDynamics> mCommentList;
    private Context mContext;

    private AdapterClickListener onClickListener;

    public ReleaseAlbumAddAdapter(Context context, List<ReleaseDynamics> list) {
        mContext = context;
        mCommentList = list;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
    @Override
    public InfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InfoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.release_album_item, parent, false));

    }

    @Override
    public void onBindViewHolder(InfoViewHolder holder, int position) {

        if (!TextUtils.isEmpty(mCommentList.get(position).getAlbumImgUrl())) {
            Glide.with(mContext).asBitmap().load(mCommentList.get(position).getAlbumImgUrl()).into( holder.albumImage);

            holder.albumDelete.setVisibility(View.VISIBLE);
        } else {
            holder.albumDelete.setVisibility(View.GONE);
            holder.albumImage.setImageResource(R.drawable.album_add);
        }

        holder.albumDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClickListenerDelete(position);
            }
        });

        holder.albumImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCommentList.get(position).getType() == 1) {
                    onClickListener.onClickListenerSelectAlbum();
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
        public ImageView albumDelete;

        public InfoViewHolder(View itemView) {
            super(itemView);

            albumImage = (ImageView) itemView.findViewById(R.id.album_image);
            albumDelete = (ImageView) itemView.findViewById(R.id.album_delete);
        }
    }

    public interface AdapterClickListener{
        void onClickListenerDelete(int position);
        void onClickListenerSelectAlbum();
    }

}