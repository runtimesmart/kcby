package com.ctb_open_car.view.adapter.newsadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.community.response.feed.FeedDto;
import com.ctb_open_car.bean.newsbean.ColumnFollowBean;
import com.ctb_open_car.bean.newsbean.ColumnInfoBean;
import com.ctb_open_car.bean.newsbean.NewsBean;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.NewshomeApi;
import com.ctb_open_car.view.activity.login.LoginActivity;
import com.ctb_open_car.view.activity.news.ColumnActivity;
import com.ctb_open_car.view.activity.person.PersonHomeActivity;
import com.google.gson.JsonObject;
import com.rxretrofitlibrary.RxRetrofitApp;
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
public class ColumnDaRenRecyclerAdapter extends RecyclerView.Adapter<ColumnDaRenRecyclerAdapter.DaRenViewHolder> {
    private RxRetrofitApp mRxInstance;
    private Context context;
    private List<ColumnFollowBean.FollowerBean> list_nam_child;
    private BloggerClickListener onClickListener;
    public ColumnDaRenRecyclerAdapter(Context context,
                                      List<ColumnFollowBean.FollowerBean> list_nam_child) {
        this.context = context;
        this.list_nam_child = list_nam_child;
        mRxInstance = RxRetrofitApp.Singleton.INSTANCE.get();
    }

    @Override
    public ColumnDaRenRecyclerAdapter.DaRenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ColumnDaRenRecyclerAdapter.DaRenViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.news_column_friend_item, parent, false));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(ColumnDaRenRecyclerAdapter.DaRenViewHolder holder, int position) {
        holder.friendName.setText(list_nam_child.get(position).getNickname());
        holder.friendDes.setText(list_nam_child.get(position).getFeed());

        Glide.with(context).asBitmap().circleCrop().load(list_nam_child.get(position).getImg()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                holder.avarIcon.setImageBitmap(resource);
            }
        });

        if (list_nam_child.get(position).getFollowStatus() == 1) {
            holder.subscription.setBackgroundResource(R.drawable.follow_news);
        } else {
            holder.subscription.setBackgroundResource(R.drawable.follow_yes);
        }

        /**** 个人中心****/
        holder.avarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PersonHomeActivity.class);
                i.putExtra("user_id",list_nam_child.get(position).getUserId());
                context.startActivity(i);
            }
        });

        /**** 关注 ****/
        holder.subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRxInstance.mHeadBean.getUserId() > 0) {
                    getColumnFollow(list_nam_child.get(position), position);
                } else {
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    LatLng mCurrentLoc = new LatLng(mRxInstance.mHeadBean.getLatitude(), mRxInstance.mHeadBean.getLongitude());
                    intent.putExtra("LatLng", mCurrentLoc);
                    context.startActivity(intent);
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

    public void getColumnFollow(ColumnFollowBean.FollowerBean followerBean, int position) {
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("themeId",followerBean.getUserId()+"");
        NewshomeApi hostFeedsApi = new NewshomeApi(new HttpListener() {
            @Override
            public void onNext(Object object) {
                super.onNext(object);
                JsonObject jsonObject = (JsonObject)object;
                String code = jsonObject.get("code").toString();
                int codeIndex = Integer.parseInt(code =code .replace("\"", ""));
                if (codeIndex == 0){
                    list_nam_child.get(position).setFollowStatus(1);
                    notifyItemChanged(position);
                    Toasty.info(context, "关注成功").show();
                } else {
                    Toasty.info(context, "关注失敗").show();
                }
            }

            @Override
            public  void onError(Throwable e){
                Timber.e("getColumnFollow %s", e.toString());
            }
        }, (ColumnActivity)context, 1);
        hostFeedsApi.setRequestBody(queryMap);
        HttpManager.getInstance().doHttpDeal(hostFeedsApi);
    }

    public interface BloggerClickListener{
        void onClickListenerBlogger(ColumnFollowBean.FollowerBean bloggerInfo);
    }

    public class DaRenViewHolder extends RecyclerView.ViewHolder {
        public TextView friendName;
        public TextView friendDes;
        public ImageView avarIcon;
        public ImageView friendStatus;
        public Button subscription;

        public DaRenViewHolder(View itemView) {
            super(itemView);
            friendName = (TextView) itemView.findViewById(R.id.friend_name);
            friendDes = (TextView) itemView.findViewById(R.id.friend_des);
            avarIcon = (ImageView) itemView.findViewById(R.id.avar_icon);
            friendStatus = (ImageView) itemView.findViewById(R.id.friend_status);
            subscription = (Button) itemView.findViewById(R.id.subscription);
        }
    }
}