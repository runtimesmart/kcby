package com.ctb_open_car.view.adapter.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.navi.model.NaviLatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.ctb_open_car.CTBApplication;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.community.response.user.UserCardDto;
import com.ctb_open_car.presenter.UserFollowPresenter;
import com.ctb_open_car.utils.Device;
import com.ctb_open_car.utils.PreferenceUtils;
import com.ctb_open_car.view.activity.map.NaviActivity;
import com.ctb_open_car.view.activity.person.PersonHomeActivity;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class SearchUserAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<UserCardDto> mUserList;
    private View mClickFocusView;
    private int mClickPosition;

    public SearchUserAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<UserCardDto> userList) {
        this.mUserList = userList;
        notifyDataSetChanged();
    }

    public UserCardDto getItem(int p) {
        return mUserList.get(p);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.ugc_search_user_item, null);
        return new SearchUserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SearchUserViewHolder userHolder = (SearchUserViewHolder) holder;
        UserCardDto userCardDto = getItem(position);
        userHolder.mUserName.setText(userCardDto.nickName);
        userHolder.mUserSign.setText(userCardDto.getUserSign());
        userHolder.mFanCount.setText("粉丝数：" + userCardDto.getUserStat().getFansCnt());
        userHolder.mFanCount.setTextColor(userHolder.mFanCount.getContext().getResources().getColor(R.color.color_3240DB));
        Glide.with(mContext).load(userCardDto.userIcon.getResourceUrl())
                .placeholder(R.drawable.avatar)
                .transform(new RoundedCorners(Device.dip2px(15)))
                .into(userHolder.mUserAvatar);
        if (0 == userCardDto.userAuthStatus) {
            userHolder.mUserAuthState.setVisibility(View.INVISIBLE);
        } else {
            userHolder.mUserAuthState.setVisibility(View.VISIBLE);
        }
        //隐藏自己的关注按钮
        if (userCardDto.getUserId() == PreferenceUtils.getLong(CTBApplication.getInstance(), "userId")) {
            userHolder.mUserFocusState.setVisibility(View.GONE);
        } else {
            userHolder.mUserFocusState.setVisibility(View.VISIBLE);

            if (0 == userCardDto.getRelationStatus()) {
                userHolder.mUserFocusState.setImageDrawable(mContext
                        .getResources().getDrawable(R.drawable.follow));
            } else {
                userHolder.mUserFocusState.setImageDrawable(mContext
                        .getResources().getDrawable(R.drawable.followed));
            }
        }
    }

    @Override
    public int getItemCount() {
        if (null == mUserList) {
            return Collections.EMPTY_LIST.size();
        }
        return mUserList.size();
    }

    public class SearchUserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.expert_fans_attentions)
        LinearLayout mFanAtten;

        @BindView(R.id.attention_count)
        TextView mAttention;

        @BindView(R.id.fans_count)
        TextView mFanCount;

        //        用户头像
        @BindView(R.id.ugc_user_avatar)
        public ImageView mUserAvatar;

        //        用户名字
        @BindView(R.id.ugc_news_user)
        public TextView mUserName;

        @BindView(R.id.ugc_pub_date)
        public TextView mUserPubDate;

        //        用户认证状态
        @BindView(R.id.ugc_user_auth_status)
        public ImageView mUserAuthState;

        //        用户签名
        @BindView(R.id.ugc_news_signature)
        public TextView mUserSign;

        //        关注状态
        @BindView(R.id.ugc_follow_status)
        public ImageView mUserFocusState;

        //        认证状态
        @BindView(R.id.ugc_news_verify)
        public TextView mUserVerifyState;

        public SearchUserViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            initView();
        }

        private void initView() {
            mUserVerifyState.setVisibility(View.GONE);
            mFanAtten.setVisibility(View.VISIBLE);
            mAttention.setVisibility(View.GONE);
            mUserPubDate.setVisibility(View.GONE);

        }

        @OnClick(R.id.user_state_layout)
        void onFollowClick(View v) {
            mClickFocusView = v;
            mClickPosition = getAdapterPosition();
            UserCardDto userCardDto = mUserList.get(mClickPosition);
            String targetUserId = userCardDto.getUserId() + "";
            UserFollowPresenter userFollowPresenter = new UserFollowPresenter(mContext, new UserFollowPresenter.FollowCallback() {
                @Override
                public void actionCancelSuccess() {
                    setFollowStatus(0);
                    mUserList.get(mClickPosition).setRelationStatus(0);
                }

                @Override
                public void actionFollowSuccess() {
                    setFollowStatus(1);
                    mUserList.get(mClickPosition).setRelationStatus(1);
                }
            });
            if (0 == userCardDto.getRelationStatus()) {
                userFollowPresenter.userFollow(targetUserId);
            } else {
                userFollowPresenter.userCancelFollow(targetUserId);
            }
        }

        /**
         * 设置用户关注状态
         */
        public void setFollowStatus(int type) {
            ImageView focusState = mClickFocusView.findViewById(R.id.ugc_follow_status);
            if (0 == type) {
                focusState.setImageDrawable(mContext.getResources().getDrawable(R.drawable.follow));
                Toasty.info(mContext, "已取消关注").show();
            } else {
                focusState.setImageDrawable(mContext.getResources().getDrawable(R.drawable.followed));
                Toasty.info(mContext, "已关注").show();
            }
        }

        @OnClick(R.id.feed_user_layout)
        void onItemClick(View v) {
            int p = getAdapterPosition();
            UserCardDto userCardDto = mUserList.get(p);
            Intent i = new Intent(mContext, PersonHomeActivity.class);
            i.putExtra("user_id", userCardDto.getUserId());
            mContext.startActivity(i);
            ((Activity) mContext).finish();
        }
    }
}
