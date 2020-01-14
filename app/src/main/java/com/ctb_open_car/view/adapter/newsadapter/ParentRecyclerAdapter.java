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
 * Create by lxg
 * on 2017/8/21
 * at 13:36
 */
public class ParentRecyclerAdapter extends RecyclerView.Adapter<ParentViewHolder> {

    private Context mContext;
    private OnLayoutListenter mListenter;
    private List<NewsInfoBean.Category> mListNewBean;
    private NewsBeanClickListener mOnClickListener;

    public ParentRecyclerAdapter(Context context,
                                 List<NewsInfoBean.Category> list_name_parent) {
        this.mContext = context;
        this.mListNewBean = list_name_parent;
    }

    @Override
    public ParentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ParentViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.layout_recycelr_news_parent, parent, false));
    }

    @Override
    public void onBindViewHolder(ParentViewHolder holder, int position) {
        holder.columnName.setText(mListNewBean.get(position).getColumnTheme().getThemeName());
        holder.columnDes.setText(mListNewBean.get(position).getColumnTheme().getTips());
        getListenter().layoutChild(holder, position);
        Glide.with(mContext).asBitmap().circleCrop().load(mListNewBean.get(position).getColumnTheme().getThemeImg()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                holder.columnUrl.setImageBitmap(resource);
            }
        });

        holder.groupHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.onClickListenerNewsBean(mListNewBean.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListNewBean.size();
    }

    public OnLayoutListenter getListenter() {
        return mListenter;
    }

    public void setListenter(OnLayoutListenter listenter) {
        this.mListenter = listenter;
    }

    public void setOnClickListener(NewsBeanClickListener onClickListener){
        this.mOnClickListener = onClickListener;
    }

    public interface NewsBeanClickListener{
        void onClickListenerNewsBean(NewsInfoBean.Category newsBean);
    }

}
