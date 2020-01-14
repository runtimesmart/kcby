package com.ctb_open_car.view.adapter.newsadapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.newsbean.ColumnInfoBean;
import com.ctb_open_car.bean.newsbean.NewContentBean;
import com.ctb_open_car.engine.net.api.NewshomeApi;
import com.ctb_open_car.utils.DateUtils;
import com.ctb_open_car.view.activity.news.BloggerInfoActivity;
import com.google.gson.JsonObject;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;

import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

/**
 * project name RecyclerText
 * package name com.text.recyclertext
 */
public class BloggerRecyclerAdapter extends RecyclerView.Adapter<BloggerRecyclerAdapter.InfoViewHolder> {

    private List<NewContentBean.ContentInfo> mCommentList;
    private LayoutInflater mInflater; //得到一个LayoutInfalter对象用来导入布局
    private Context mContext;
    private int mInfomationId;
    private AdapterClickListener onClickListener;

    public BloggerRecyclerAdapter(Context context, List<NewContentBean.ContentInfo> list, int infomationId) {
        mContext = context;
        mCommentList = list;
        mInfomationId = infomationId;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
    @Override
    public InfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InfoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.news_blogger_item, parent, false));
    }

    @Override
    public void onBindViewHolder(InfoViewHolder holder, int position) {
        if (mCommentList.get(position).getCommentUser().getUserAuthStatus().equals("1")) {
            holder.vipImage.setVisibility(View.VISIBLE);
        } else {
            holder.vipImage.setVisibility(View.GONE);
        }
        holder.likeNumTv.setText(String.valueOf(mCommentList.get(position).getCommentPraiseCnt()));
        holder.bloggerName.setText(mCommentList.get(position).getCommentUser().getNickName());
        holder.commentDes.setText(mCommentList.get(position).getCommentContent());
        Glide.with(mContext).asBitmap().load(mCommentList.get(position).getCommentUser().getUserMoodIcon()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                holder.bloggerDes.setImageBitmap(resource);
            }
        });

        Glide.with(mContext).asBitmap().load(mCommentList.get(position).getCommentUser().getUserIcon()/*.getResourceUrl()*/).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                holder.avarImage.setImageBitmap(resource);
            }
        });

        if (mCommentList.get(position).getAlreadyCommentPraise() == 0) {
            holder.likeImage.setImageResource(R.drawable.liked);
            holder.likeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClickListenerLink(position, mCommentList.get(position).getCommentId());
                }
            });

        } else {
            holder.likeImage.setImageResource(R.drawable.liked_clicked);
        }
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
        public TextView likeNumTv;
        public TextView bloggerName;
        public TextView commentDes;
        public ImageView bloggerDes;
        public ImageView avarImage;
        public ImageView vipImage;
        public ImageView likeImage;

        public InfoViewHolder(View itemView) {
            super(itemView);
            likeNumTv = (TextView) itemView.findViewById(R.id.like_num_tv);
            bloggerName = (TextView) itemView.findViewById(R.id.blogger_name);
            commentDes = (TextView) itemView.findViewById(R.id.comment_des);
            bloggerDes = (ImageView) itemView.findViewById(R.id.blogger_des);
            avarImage = (ImageView) itemView.findViewById(R.id.avar_image); // to ItemButton
            vipImage = (ImageView) itemView.findViewById(R.id.vip_image);
            likeImage = (ImageView) itemView.findViewById(R.id.like_image);
        }
    }

    public interface AdapterClickListener{
        void onClickListenerLink(int position, String commentId);
    }

}