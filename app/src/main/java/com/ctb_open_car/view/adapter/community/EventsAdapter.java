package com.ctb_open_car.view.adapter.community;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.ctb_open_car.CTBApplication;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.community.response.EventData;
import com.ctb_open_car.bean.community.response.activity.ActivityCardDto;
import com.ctb_open_car.presenter.ActivityDeletePresenter;
import com.ctb_open_car.presenter.ActivityDetailPresenter;
import com.ctb_open_car.presenter.FeedDeletePresenter;
import com.ctb_open_car.presenter.LikedPresenter;
import com.ctb_open_car.presenter.TransmitPresenter;
import com.ctb_open_car.presenter.UserFollowPresenter;
import com.ctb_open_car.utils.DateUtils;
import com.ctb_open_car.utils.Device;
import com.ctb_open_car.utils.PreferenceUtils;
import com.ctb_open_car.view.activity.activities.ReleaseActivitiesActivity;
import com.ctb_open_car.view.activity.community.ActivityDetailActivity;
import com.ctb_open_car.view.activity.login.LoginActivity;
import com.ctb_open_car.view.activity.person.PersonHomeActivity;
import com.ctb_open_car.view.adapter.viewholder.BaseEventViewHolder;
import com.ctb_open_car.view.fragment.comminity.UGCEventFragment;
import com.library.InputTextMsgDialog;
import com.rxretrofitlibrary.RxRetrofitApp;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventsAdapter extends RecyclerView.Adapter {
    private SoftReference<UGCEventFragment> mFragment;
    private EventData mActivityData;
    public List<ActivityCardDto> mActivityCardList;

    private static final int FEEDS_TYPE_EVENTS = 1;
    private static final int FEEDS_TYPE_NEW_HEADER_EVENT = 0;
    private RxRetrofitApp sRxInstance;

    private boolean mHideHeader;
    private String mUserId;

    public EventsAdapter(UGCEventFragment fragment, String loadId) {
        mUserId = loadId;
        mHideHeader = TextUtils.isEmpty(loadId) ? false : true;
        this.mFragment = new SoftReference<>(fragment);
        mActivityCardList = new ArrayList<>();
        sRxInstance = RxRetrofitApp.Singleton.INSTANCE.get();
    }


    public void setData(EventData activityList) {
        this.mActivityData = activityList;
        if (null != activityList.getPageData() && null != activityList.getPageData().getData()) {
            mActivityCardList.addAll(activityList.getPageData().getData());
            if (activityList.getPageData().getData().size() > 0) {
                notifyDataSetChanged();
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder focusFeedsHolder = null;
        if (viewType == FEEDS_TYPE_NEW_HEADER_EVENT) {
            return new NewHostHolder(LayoutInflater.from(mFragment.get().getContext()).inflate(R.layout.ugc_events_header_item, null));
        } else if (FEEDS_TYPE_EVENTS == viewType) {
            return new EventsHolder(LayoutInflater.from(mFragment.get().getContext()).inflate(R.layout.ugc_event_item, null));
        }
        return new EventsHolder(LayoutInflater.from(mFragment.get().getContext()).inflate(R.layout.ugc_event_item, null));
    }

    private ActivityCardDto getEventData(int position) {
        if (mHideHeader) {
            return mActivityCardList.get(position);
        } else {
            return mActivityCardList.get(position - 1);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (FEEDS_TYPE_EVENTS == getItemViewType(position)) {
            EventsHolder mEventsHolder = (EventsHolder) holder;
            ActivityCardDto activityCardDto = getEventData(position);
            mEventsHolder.mEventTitle.setText(activityCardDto.getActivityTitle());
            mEventsHolder.mEventCurrentFeedText.setText(activityCardDto.getActivityDesc());
            mEventsHolder.mEventCurrentFeedText.setMaxLines(3);
            /**设置省略内容*/
            mEventsHolder.ellipsizeOriginEventText(mFragment.get().getContext(), R.string.event_ellipsize);
            mEventsHolder.mStartDate.setText("出发日期:" + DateUtils.formatDate(activityCardDto.getActivityBegintime()));
            mEventsHolder.mEventDeadline.setText("报名截止:" + DateUtils.formatDate(activityCardDto.getActivityEnrollEndtime()));
            mEventsHolder.mEventMaxMembers.setText("人数上限:" + activityCardDto.getActivityEnrollLimit());
            //转评赞阅
            mEventsHolder.mUserTransCount.setText(activityCardDto.getActivityStat().getForwardCnt() + "");
            mEventsHolder.mUserCmtCount.setText(activityCardDto.getActivityStat().getCommentCnt() + "");
            mEventsHolder.mUserReadCount.setText(activityCardDto.getActivityStat().getReadCnt() + "");
            mEventsHolder.mUserLikeCount.setText(activityCardDto.getActivityStat().getPraiseCnt() + "");

//            活动封面

            if (null != activityCardDto.getActivityCoverImage()) {
                ImageView eventCover = new ImageView(mFragment.get().getContext());
                eventCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, Device.dip2px(167));
                eventCover.setLayoutParams(params);

                Glide.with(mFragment.get().getContext()).load(activityCardDto.getActivityCoverImage().getResourceUrl()).into(eventCover);
                mEventsHolder.mEventCoverLayout.removeAllViews();
                mEventsHolder.mEventCoverLayout.addView(eventCover);
            }


//            用户信息
            mEventsHolder.mUserName.setText(activityCardDto.getActivityUser().nickName);
            mEventsHolder.mUserPubDate.setText(DateUtils.convertTimeToFormat(activityCardDto.getPublishTime()));
            mEventsHolder.mUserSign.setText(activityCardDto.getActivityUser().getUserSign());

            /**设置用户vip状态*/
            mEventsHolder.setUserVipStatus(activityCardDto.getActivityUser().getUserAuthStatus());

            if (null != activityCardDto.getActivityUser().getUserIcon()) {
                Glide.with(mFragment.get().getContext()).load(activityCardDto.getActivityUser().getUserIcon().getResourceUrl())
                        .placeholder(R.drawable.avatar)
                        .transform(new RoundedCorners(Device.dip2px(15)))
                        .into(mEventsHolder.mUserAvatar);
            }
            Glide.with(mFragment.get().getContext()).load(activityCardDto.getActivityUser().userMoodIcon).into(mEventsHolder.mUserMood);

            //进行中
            if (mEventsHolder.verifyStatus(activityCardDto.getActivityEnrollEndtime())) {
                mEventsHolder.mEventStatus.setText("报名中");
                mEventsHolder.mEventStatus.setTextColor(mFragment.get().getContext().getResources().getColor(R.color.community_event_begin_status));
                mEventsHolder.mEventStatus.setBackground(mFragment.get().getContext().getResources().getDrawable(R.drawable.event_state_ongoing_border));
            } else {
                //结束
                mEventsHolder.mEventStatus.setText("报名截止");
                mEventsHolder.mEventStatus.setTextColor(mFragment.get().getContext().getResources().getColor(R.color.community_tab_txt_normal_color));
                mEventsHolder.mEventStatus.setBackground(mFragment.get().getContext().getResources().getDrawable(R.drawable.event_state_finished_border));
            }

            if (!mHideHeader) {

                //隐藏自己的关注按钮
                if (activityCardDto.getActivityUser().getUserId() == PreferenceUtils.getLong(CTBApplication.getInstance(), "userId")) {
                    mEventsHolder.mUserFollowStatus.setVisibility(View.GONE);
                } else {
                    mEventsHolder.mUserFollowStatus.setVisibility(View.VISIBLE);
                    /**设置关注状态*/
                    mEventsHolder.getFollowStatus(activityCardDto.getActivityUser().getRelationStatus());
                }
            }
        }

    }

    @Override
    public int getItemCount() {

        if (mHideHeader) {
            return mActivityCardList.size();
        } else {
            if (null == mActivityData || null == mActivityData.getPageData() || null == mActivityData.getPageData().getData()) {
                return Collections.emptyList().size() + 1;
            }

            return mActivityCardList.size() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mHideHeader) {
            return FEEDS_TYPE_EVENTS;
        } else {
            if (0 == position) {
                return FEEDS_TYPE_NEW_HEADER_EVENT;
            } else {
                return FEEDS_TYPE_EVENTS;
            }
        }
    }

    /**
     * 最新活动消息Holder
     */
    public class NewHostHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.event_newest)
        Button mBtnNew;

        @BindView(R.id.event_local)
        Button mBtnLocal;

        public NewHostHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }


        @OnClick({R.id.event_newest, R.id.event_local, R.id.creat_event_new})
        public void onBtnSwitchClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.event_newest:
                    mBtnNew.setBackground(mFragment.get().getContext().getDrawable(R.drawable.event_selected_bg));
                    mBtnLocal.setBackground(mFragment.get().getContext().getDrawable(R.drawable.event_unselected_bg));
                    mFragment.get().requesetNewEvent();
                    break;
                case R.id.event_local:
                    mBtnNew.setBackground(mFragment.get().getContext().getDrawable(R.drawable.event_unselected_bg));
                    mBtnLocal.setBackground(mFragment.get().getContext().getDrawable(R.drawable.event_selected_bg));

                    mFragment.get().requesetLocalEvent();
                    break;
                case R.id.creat_event_new:
                    if (sRxInstance.mHeadBean.getUserId() > 0) {
                        Intent intent = new Intent(mFragment.get().getContext(), ReleaseActivitiesActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mFragment.get().startActivity(intent);
                    } else {
                        Intent intent = new Intent(mFragment.get().getContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        LatLng mCurrentLoc = new LatLng(sRxInstance.mHeadBean.getLatitude(), sRxInstance.mHeadBean.getLongitude());
                        intent.putExtra("LatLng", mCurrentLoc);
                        mFragment.get().getContext().startActivity(intent);
                    }

                    break;

            }
        }
    }


    /**
     * 附近活动消息Holder
     */
    public class EventsHolder extends BaseEventViewHolder {

        private UserFollowPresenter mUserFollowPresenter;

        public EventsHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mUserFollowPresenter = new UserFollowPresenter(itemView.getContext(), this);
            mTransmitPresenter = new TransmitPresenter((RxAppCompatActivity) mFragment.get().getActivity());
            if (mHideHeader) {
                mUserFollowStatus.setVisibility(View.GONE);
                if (mUserId.equals(CTBApplication.getInstance().getRxApp().mHeadBean.getUserId().toString())) {
                    mUserDelete.setVisibility(View.VISIBLE);
                }
            }
        }

        @OnClick({R.id.ugc_feeds_normal_layout,
                R.id.feed_event_info,
                R.id.feed_event_covers,
                R.id.feed_event_titleinfo})
        void onMsgInfoClick(View v) {
            Intent i = new Intent();
            int position = getAdapterPosition();
            ActivityCardDto activityDto = getEventData(position);
            i.setClass(mFragment.get().getContext(), ActivityDetailActivity.class);
            i.putExtra("activityId", activityDto.getActivityId());
            mFragment.get().getContext().startActivity(i);
        }


        /**
         * 点击跳转到个人主页
         */
        @Override
        protected void onAvatarClick(View v) {
            super.onAvatarClick(v);
            if (mHideHeader) {
                return;
            }
            ActivityCardDto activityDto = getEventData(getAdapterPosition());

            Intent i = new Intent(mFragment.get().getActivity(), PersonHomeActivity.class);
            i.putExtra("user_id", activityDto.getActivityUser().getUserId());
            mFragment.get().getContext().startActivity(i);
        }

        /***
         * 用户转发
         * **/
        @Override
        protected void onTransmitClick(View v) {
            super.onTransmitClick(v);
            ActivityCardDto activityDto = getEventData(getAdapterPosition());

            showDialog(v.getContext(), new InputTextMsgDialog.OnTextSendListener() {
                @Override
                public void onTextSend(String msg) {
                    String lng = CTBApplication.getInstance().getRxApp().mHeadBean.getLongitude() + "";
                    String lat = CTBApplication.getInstance().getRxApp().mHeadBean.getLatitude() + "";
                    String feedId = activityDto.getActivityId() + "";

                    mTransmitPresenter.transmitFeed(lng, lat, msg, "2", feedId);
                }
            });
        }

        @OnClick(R.id.feed_like_layout)
        public void onFeedLike(View v) {
            LikedPresenter presenter = new LikedPresenter(mFragment.get().getContext());
//            if (!getActivitys().isPraised()) {
            ActivityCardDto activityDto = getEventData(getAdapterPosition());
            presenter.activityLike(activityDto.getActivityId() + "",
                    activityDto.getActivityUser().userId + "");
            mUserLikeCount.setText(Integer.parseInt(mUserLikeCount.getText().toString()) + 1 + "");
            activityDto.setPraised(true);
//                presenter.activityUnLike(getActivitys().getActivityId() + "");
//                mUserLikeCount.setText(Integer.parseInt(mUserLikeCount.getText().toString())-1+"");
//            } else {

//            }
        }

        /**
         * 关注点击
         */
        @Override
        protected void onFocusClick(View v) {
            super.onFocusClick(v);
            if (mUserFollowStatus.getVisibility() == View.GONE) {
                return;
            }
            ActivityCardDto activityDto = getEventData(getAdapterPosition());
            String targetUserId = activityDto.getActivityUser().getUserId() + "";
            if (0 == activityDto.getActivityUser().getRelationStatus()) {
                mUserFollowPresenter.userFollow(targetUserId);
            } else {
                mUserFollowPresenter.userCancelFollow(targetUserId);
            }
        }

        @Override
        public void actionCancelSuccess() {
            super.actionCancelSuccess();
            getEventData(getAdapterPosition()).getActivityUser().setRelationStatus(0);
        }

        @Override
        public void actionFollowSuccess() {
            super.actionFollowSuccess();
            getEventData(getAdapterPosition()).getActivityUser().setRelationStatus(1);
        }

        /**
         * 点击弹框评论
         */
        @OnClick({R.id.feed_cmt_layout})
        public void onCommentClick(View v) {
            InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(mFragment.get().getContext(), R.style.dialog_center);
            inputTextMsgDialog.setHint("写评论...");
            inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                @Override
                public void onTextSend(String msg) {
                    //点击发送按钮后，回调此方法，msg为输入的值
                    ActivityDetailPresenter presenter = new ActivityDetailPresenter(mFragment.get().getContext());
                    int position = getAdapterPosition();
                    presenter.pubFeedComment(getEventData(position).getActivityId() + "", msg, "");

                    int increaseCount = Integer.parseInt(mUserCmtCount.getText().toString()) + 1;
                    mUserCmtCount.setText(+increaseCount + "");
                }
            });
            inputTextMsgDialog.show();
        }

        @Override
        protected void onDeleteItemClick(View v) {
            super.onDeleteItemClick(v);
            AlertDialog dialog = new AlertDialog.Builder(mFragment.get().getContext())
                    .setTitle("删除活动")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityDeletePresenter presenter = new ActivityDeletePresenter(v.getContext());
                            ActivityCardDto activityCardDto = getEventData(getAdapterPosition());
                            presenter.deleteActivity(String.valueOf(activityCardDto.getActivityId()));
                            mActivityCardList.remove(activityCardDto);
                            notifyDataSetChanged();

                            dialog.dismiss();
                        }
                    }).show();

        }


        public boolean verifyStatus(long endTime) {
            if (endTime <= System.currentTimeMillis()) {
                return false;
            } else {
                return true;
            }
        }
    }


}
