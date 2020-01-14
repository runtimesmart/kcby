package com.ctb_open_car.view.adapter.newsadapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.newsbean.NewContentBean;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.NewshomeApi;
import com.ctb_open_car.view.activity.news.BloggerInfoActivity;
import com.google.gson.JsonObject;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;

import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class BloggerListAdpater extends BaseAdapter {

    private List<NewContentBean.ContentInfo> mCommentList;
    private LayoutInflater mInflater; //得到一个LayoutInfalter对象用来导入布局
    private Context mContext;
    private int mInfomationId;
    public BloggerListAdpater(Context context, List<NewContentBean.ContentInfo> list, int infomationId) {
        mContext = context;
        mCommentList = list;
        mInfomationId = infomationId;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mCommentList.size(); //返回数组的长度
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.news_blogger_item, null);
            holder = new ViewHolder();
            /*得到各个控件的对象*/
            holder.likeNumTv = (TextView) convertView.findViewById(R.id.like_num_tv);
            holder.bloggerName = (TextView) convertView.findViewById(R.id.blogger_name);
            holder.commentDes = (TextView) convertView.findViewById(R.id.comment_des);
            holder.bloggerDes = (ImageView) convertView.findViewById(R.id.blogger_des);
            holder.avarImage = (ImageView) convertView.findViewById(R.id.avar_image); // to ItemButton
            holder.vipImage = (ImageView) convertView.findViewById(R.id.vip_image);
            holder.likeImage = (ImageView) convertView.findViewById(R.id.like_image);
            convertView.setTag(holder); //绑定ViewHolder对象
        } else {
            holder = (ViewHolder) convertView.getTag(); //取出ViewHolder对象
        }

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
                    setFollow(position, mCommentList.get(position).getCommentId());
                }
            });

        } else {
            holder.likeImage.setImageResource(R.drawable.liked_clicked);
        }
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mCommentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return Long.parseLong(mCommentList.get(position).getCommentId());
    }

    private void setFollow(int position, String commentId) {
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("informationId",String.valueOf(mInfomationId));
        queryMap.put("commentId",commentId);
        NewshomeApi hostFeedsApi = new NewshomeApi(new HttpListener() {
            @Override
            public void onNext(Object object) {
                super.onNext(object);
                JsonObject jsonObject = (JsonObject)object;
                String code = jsonObject.get("code").toString();
                if (code.equals("0")) {
                    Toasty.info(mContext, "点赞成功").show();
                    mCommentList.get(position).setAlreadyCommentPraise(1);
                    notifyDataSetChanged();
                }
            }

            @Override
            public  void onError(Throwable e){
                Timber.e("getColumnFollow %s", e.toString());
            }
        }, (BloggerInfoActivity)mContext, 6);
        hostFeedsApi.setRequestBody(queryMap);
        HttpManager.getInstance().doHttpDeal(hostFeedsApi);
    }
    /*存放控件 的ViewHolder*/
    public final class ViewHolder {
        public TextView likeNumTv;
        public TextView bloggerName;
        public TextView commentDes;
        public ImageView bloggerDes;
        public ImageView avarImage;
        public ImageView vipImage;
        public ImageView likeImage;
    }

}
