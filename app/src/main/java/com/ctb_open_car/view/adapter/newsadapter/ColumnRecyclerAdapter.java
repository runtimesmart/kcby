package com.ctb_open_car.view.adapter.newsadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.newsbean.ColumnFollowBean;
import com.ctb_open_car.bean.newsbean.NewsBean;

import java.util.List;

/**
 * project name RecyclerText
 * package name com.text.recyclertext
 */
public class ColumnRecyclerAdapter extends RecyclerView.Adapter<ColumnRecyclerAdapter.DaRenViewHolder> {

    private Context context;
    private List<ColumnFollowBean.FollowerBean> list_nam_child;
    private BloggerClickListener onClickListener;

    public ColumnRecyclerAdapter(Context context,
                                 List<ColumnFollowBean.FollowerBean> list_nam_child) {
        this.context = context;
        this.list_nam_child = list_nam_child;
    }

    @Override
    public ColumnRecyclerAdapter.DaRenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ColumnRecyclerAdapter.DaRenViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.news_column_item, parent, false));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(DaRenViewHolder holder, int position) {
        Glide.with(context).asBitmap().transform(new RoundedCorners( 10)).load(list_nam_child.get(position).getImg()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                holder.avarIcon.setImageBitmap(resource);
            }
        });

        Glide.with(context).asBitmap().load(list_nam_child.get(position).getIcon()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                holder.friendStatus.setImageBitmap(resource);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_nam_child.size();
    }

    public void setOnClickListener(BloggerClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public interface BloggerClickListener{
        void onClickListenerBlogger(NewsBean.BloggerInfo bloggerInfo);
    }

    public class DaRenViewHolder extends RecyclerView.ViewHolder {
        public ImageView avarIcon;
        public ImageView friendStatus;

        public DaRenViewHolder(View itemView) {
            super(itemView);
            avarIcon = (ImageView) itemView.findViewById(R.id.avar_image);
            friendStatus = (ImageView) itemView.findViewById(R.id.avar_status);
        }
    }
}