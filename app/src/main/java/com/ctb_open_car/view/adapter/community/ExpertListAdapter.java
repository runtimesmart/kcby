package com.ctb_open_car.view.adapter.community;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.ctb_open_car.CTBApplication;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.community.response.user.UserCardDto;
import com.ctb_open_car.presenter.UserFollowPresenter;
import com.ctb_open_car.utils.Device;
import com.ctb_open_car.utils.PreferenceUtils;
import com.ctb_open_car.view.activity.person.PersonHomeActivity;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class ExpertListAdapter extends RecyclerView.Adapter {

    public List<UserCardDto> mFocusUserList = new ArrayList<>();
    private SoftReference<Context> mContext;
    private View mClickFocusView;
    private int mClickPosition;

    private boolean mPersonPage;
    private UserFollowPresenter mUserFollowPresenter;

    public ExpertListAdapter(Context activity, boolean personPage) {
        this.mPersonPage = personPage;
        this.mContext = new SoftReference<>(activity);
    }

    public void setData(List<UserCardDto> focusUserList) {
        mFocusUserList.addAll(focusUserList);
        if (focusUserList.size() > 0) {
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext.get()).inflate(R.layout.ugc_focus_list_item, null);
        return new ExpertHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ExpertHolder expertHolder = (ExpertHolder) holder;
        UserCardDto userCardDto = getItem(position);
        expertHolder.mUserName.setText(userCardDto.nickName);
        expertHolder.mUserSignInfo.setText(userCardDto.getUserSign());

        Glide.with(mContext.get()).load(userCardDto.userIcon.getResourceUrl())
                .placeholder(R.drawable.avatar)
                .transform(new RoundedCorners(Device.dip2px(15)))
                .into(expertHolder.mUserAvatar);
        if (0 == userCardDto.userAuthStatus) {
            expertHolder.mUserAuthState.setVisibility(View.INVISIBLE);
        } else {
            expertHolder.mUserAuthState.setVisibility(View.VISIBLE);
        }

        if (mPersonPage) {
            if (TextUtils.isEmpty(userCardDto.userAuthTitle)) {
                expertHolder.mUserVerifyState.setVisibility(View.GONE);
            } else {
                expertHolder.mUserVerifyState.setVisibility(View.VISIBLE);
                expertHolder.mUserVerifyState.setText("认证：" + userCardDto.userAuthTitle);
            }
        } else {
            expertHolder.mAttention.setText("粉丝：" + userCardDto.getUserStat().getAttentionCnt());
            expertHolder.mFanCount.setText("关注：" + userCardDto.getUserStat().getFansCnt());
        }

        //隐藏自己的关注按钮
        if (userCardDto.getUserId() == PreferenceUtils.getLong(CTBApplication.getInstance(), "userId")) {
            expertHolder.mUserFocusState.setVisibility(View.GONE);
        } else {
            expertHolder.mUserFocusState.setVisibility(View.VISIBLE);

            if (0 == userCardDto.getRelationStatus()) {
                expertHolder.mUserFocusState.setImageDrawable(mContext.get()
                        .getResources().getDrawable(R.drawable.follow));
            } else {
                expertHolder.mUserFocusState.setImageDrawable(mContext.get()
                        .getResources().getDrawable(R.drawable.followed));
            }
        }
    }

    public UserCardDto getItem(int position) {
        return mFocusUserList.get(position);
    }

    @Override
    public int getItemCount() {
        if (null == mFocusUserList) {
            return Collections.EMPTY_LIST.size();
        }
        return mFocusUserList.size();
    }

    public class ExpertHolder extends RecyclerView.ViewHolder implements UserFollowPresenter.FollowCallback {
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

        //        关注状态
        @BindView(R.id.ugc_follow_status)
        public ImageView mUserFocusState;

        //        认证状态
        @BindView(R.id.ugc_news_verify)
        public TextView mUserVerifyState;

        //        签名信息
        @BindView(R.id.ugc_news_signature)
        public TextView mUserSignInfo;
        //        达人榜页面 粉丝和关注数布局
        @BindView(R.id.expert_fans_attentions)
        public LinearLayout mFanAttentionsLayout;
        @BindView(R.id.fans_count)
        public TextView mFanCount;
        @BindView(R.id.attention_count)
        public TextView mAttention;

        public ExpertHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            initView();
            mUserFollowPresenter = new UserFollowPresenter(itemView.getContext(), this);
            if (mPersonPage) {
                mFanAttentionsLayout.setVisibility(View.GONE);
                mUserVerifyState.setVisibility(View.VISIBLE);

            } else {
                mUserVerifyState.setVisibility(View.GONE);
                mFanAttentionsLayout.setVisibility(View.VISIBLE);
            }
        }


        @OnClick({R.id.user_name_state_layout, R.id.ugc_user_avatar, R.id.ugc_user_auth_status})
        void toPersonDetail(View v) {
            Intent i = new Intent(mContext.get(), PersonHomeActivity.class);
            UserCardDto userCardDto = getItem(getAdapterPosition());

            i.putExtra("user_id", userCardDto.getUserId());
            mContext.get().startActivity(i);
        }


        @OnClick(R.id.user_state_layout)
        void userFollow(View v) {
            mClickFocusView = v;
            mClickPosition=getAdapterPosition();
            UserCardDto userCardDto = getItem(mClickPosition);
            String targetUserId = userCardDto.getUserId() + "";
            if (0 == userCardDto.getRelationStatus()) {
                mUserFollowPresenter.userFollow(targetUserId);
            } else {
                mUserFollowPresenter.userCancelFollow(targetUserId);
            }
        }


        private void initView() {
            mUserPubDate.setVisibility(View.GONE);
        }

        @Override
        public void actionCancelSuccess() {
            setFocusStatus(0);
            getItem(mClickPosition).setRelationStatus(0);
        }

        @Override
        public void actionFollowSuccess() {
            setFocusStatus(1);
            getItem(mClickPosition).setRelationStatus(1);
        }

        private void setFocusStatus(int type) {
            Timber.e(Thread.currentThread()+"");
            ImageView focusState = mClickFocusView.findViewById(R.id.ugc_follow_status);
            if (0 == type) {
                focusState.setImageDrawable(mContext.get()
                        .getResources().getDrawable(R.drawable.follow));
                Toasty.info(mContext.get(), "已取消关注").show();
            } else {
                focusState.setImageDrawable(mContext.get()
                        .getResources().getDrawable(R.drawable.followed));
                Toasty.info(mContext.get(), "已关注").show();
            }

        }
    }
}
