package com.ctb_open_car.view.adapter.community;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.community.RecommendUser;
import com.ctb_open_car.bean.community.response.feed.FeedDto;
import com.ctb_open_car.bean.community.response.user.UserCardDto;
import com.ctb_open_car.utils.Device;
import com.ctb_open_car.view.activity.community.FeedsDetailActivity;
import com.ctb_open_car.view.activity.person.PersonHomeActivity;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HotExpertAdapter extends RecyclerView.Adapter<HotExpertAdapter.HotExpertViewHolder> {
    private Context mContext;
    private List<UserCardDto> mRecommendUsers;

    public HotExpertAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<UserCardDto> recommendList) {
        this.mRecommendUsers = recommendList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HotExpertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HotExpertViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.ugc_hotlist_header_expert_item, null));
    }

    private UserCardDto getRecommendUser(int position) {
        return mRecommendUsers.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull HotExpertViewHolder holder, int position) {

        Glide.with(mContext).load(getRecommendUser(position).userIcon.getResourceUrl())
                .placeholder(R.drawable.avatar)
                .transform(new RoundedCorners(Device.dip2px(15)))
                .into(holder.mExpertAvatar);
        holder.mExpertTitle.setText(getRecommendUser(position).nickName);
        if (0 == getRecommendUser(position).userAuthStatus) {
            holder.mExpertVip.setVisibility(View.INVISIBLE);
        } else {
            holder.mExpertVip.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (null == mRecommendUsers) {
            return Collections.emptyList().size();
        }
        return mRecommendUsers.size();
    }

    public class HotExpertViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.hot_recommend_avatar)
        public ImageView mExpertAvatar;

        @BindView(R.id.hot_user_name)
        public TextView mExpertTitle;

        @BindView(R.id.hot_user_vip)
        public ImageView mExpertVip;

        public HotExpertViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent();
                    int position = getAdapterPosition();
                    UserCardDto userCardDto = getRecommendUser(position);
                    i.setClass(mContext, PersonHomeActivity.class);
                    i.putExtra("user_id", userCardDto.getUserId());
                    mContext.startActivity(i);
                }
            });
        }


    }

}
