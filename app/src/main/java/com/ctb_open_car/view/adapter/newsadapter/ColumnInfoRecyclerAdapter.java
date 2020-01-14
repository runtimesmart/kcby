package com.ctb_open_car.view.adapter.newsadapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.ctb_open_car.bean.newsbean.NewsBean;
import com.ctb_open_car.utils.DateUtils;
import com.ctb_open_car.view.activity.news.BloggerInfoActivity;

import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * project name RecyclerText
 * package name com.text.recyclertext
 */
public class ColumnInfoRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int HORIZONTAL_VIEW = 1;
    private final int VERTICAL_VIEW = 2;
    private Context context;
    private List<ColumnInfoBean.Information> listColumnInfo;
    private ColumnInfoAdapterClickListener onClickListener;
    private ColumnDaRenRecyclerAdapter mDaRenAdpater;
    private ColumnInfoBean mColumnInfoBean;
    public ColumnInfoRecyclerAdapter(Context context,
                                     List<ColumnInfoBean.Information> list_nam_child, ColumnInfoBean columnInfoBean) {
        this.context = context;
        this.listColumnInfo = list_nam_child;
        this.mColumnInfoBean = columnInfoBean;
    }

    @Override
    public int getItemViewType(int position) {
        if(listColumnInfo.get(position).getType() == 1){
            return HORIZONTAL_VIEW;
        } else if(listColumnInfo.get(position).getType() == 0){
            return VERTICAL_VIEW;
        } else{
              return super.getItemViewType(position);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == HORIZONTAL_VIEW){
            return new DaRenViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.news_columnn_info_header, parent, false));
        } else {
            return new InfoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.news_column_info_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof InfoViewHolder) {
            InfoViewHolder viewHolder = (InfoViewHolder)holder;
            viewHolder.infoTip.setText(listColumnInfo.get(position).getTitle());
            viewHolder.pushTime.setText(DateUtils.convertTimeToFormat(listColumnInfo.get(position).getPublishTime()));

            viewHolder.likeNum.setText(String.valueOf(listColumnInfo.get(position).getLikes()));
            viewHolder.commentNum.setText(String.valueOf(listColumnInfo.get(position).getComments()));
            Glide.with(context).asBitmap().load(listColumnInfo.get(position).getCoverUrl()).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    viewHolder.infoImage.setImageBitmap(resource);
                }
            });

            viewHolder.infoImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toasty.info(context, "需要跳转到 资讯内容 详情").show();
                    Intent intent = new Intent(context, BloggerInfoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("columnName", mColumnInfoBean.getThemeName());
                    intent.putExtra("ColumnInfo",listColumnInfo.get(position));
                    context.startActivity(intent);
                }
            });
            /***点赞***/
            viewHolder.likeLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.onClickListenerColumnInfo(listColumnInfo.get(position),position , "like");
                        Toasty.info(context, "点击了点赞");
                    }
                }
            });
            /***评论***/
            viewHolder.commentLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.onClickListenerColumnInfo(listColumnInfo.get(position),position, "comment");
                    }
                }
            });
            /****分享****/
            viewHolder.shareLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.onClickListenerColumnInfo(listColumnInfo.get(position), position, "share");
                    }
                }
            });
        } else if (holder instanceof DaRenViewHolder) {
            DaRenViewHolder viewHolder = (DaRenViewHolder)holder;
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

            viewHolder.columnDarenList.setLayoutManager(linearLayoutManager);
//            viewHolder.columnDarenList.addItemDecoration(
//                    new DividerItemDecoration(context,
//                            LinearLayoutManager.HORIZONTAL));
            mDaRenAdpater = new ColumnDaRenRecyclerAdapter(context, listColumnInfo.get(position).getBloggerInfoList());
            viewHolder.columnDarenList.setAdapter(mDaRenAdpater);
        }
    }

    @Override
    public int getItemCount() {
        return listColumnInfo.size();
    }

    public void setOnClickListener(ColumnInfoAdapterClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public interface ColumnInfoAdapterClickListener{
        void onClickListenerColumnInfo(ColumnInfoBean.Information bloggerInfo,int position, String type);
    }

    public class DaRenViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView columnDarenList;
        public TextView columnDaren;
        public View view;
        public DaRenViewHolder(View itemView) {
            super(itemView);
            view = (View) itemView.findViewById(R.id.line);
            columnDaren = (TextView) itemView.findViewById(R.id.column_daren);
            columnDarenList = (RecyclerView) itemView.findViewById(R.id.ugc_hot_title_recommend);
        }
    }

    public class InfoViewHolder extends RecyclerView.ViewHolder {
        public TextView infoTip;
        public TextView pushTime;
        public ImageView infoImage;
        public LinearLayout likeLay;
        public TextView likeNum;
        public LinearLayout commentLay;
        public TextView commentNum;
        public LinearLayout shareLay;


        public InfoViewHolder(View itemView) {
            super(itemView);
            infoTip = (TextView) itemView.findViewById(R.id.info_tip);
            pushTime = (TextView) itemView.findViewById(R.id.info_tiem);
            infoImage = (ImageView) itemView.findViewById(R.id.info_image);

            likeLay = (LinearLayout) itemView.findViewById(R.id.like_lay);
            likeNum = (TextView) itemView.findViewById(R.id.like_num);
            commentLay = (LinearLayout) itemView.findViewById(R.id.comment_lay);
            commentNum = (TextView) itemView.findViewById(R.id.comment_mun);
            shareLay = (LinearLayout) itemView.findViewById(R.id.share_lay);
        }
    }
}