package com.ctb_open_car.view.adapter.newsadapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.newsbean.NewsInfoBean;

import java.util.List;

/**
 * project name RecyclerText
 * package name com.text.recyclertext
 */
public class ChildRecyclerAdapter extends RecyclerView.Adapter<ChildViewHolder> {

    private Context context;
    private List<NewsInfoBean.Information> list_nam_child;
    private BloggerClickListener onClickListener;

    public ChildRecyclerAdapter(Context context,
                                 List<NewsInfoBean.Information> list_nam_child) {
        this.context = context;
        this.list_nam_child = list_nam_child;
    }

    @Override
    public ChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChildViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_recycelr_news_child, parent, false));
    }

    @Override
    public void onBindViewHolder(ChildViewHolder holder, int position) {
        holder.bloggerName.setText(list_nam_child.get(position).getTitle());
        holder.bloggerDes.setText(list_nam_child.get(position).getTitle());
        Glide.with(context).asBitmap().load(list_nam_child.get(position).getCoverUrl()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                holder.bloggerAvatar.setImageBitmap(resource);
            }
        });

        Glide.with(context).asBitmap().load(list_nam_child.get(position).getCoverUrl()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                holder.bloggerDesIcon.setImageBitmap(resource);
            }
        });

        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClickListenerBlogger(list_nam_child.get(position));
                }
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
        void onClickListenerBlogger(NewsInfoBean.Information bloggerInfo);
    }
}