package com.ctb_open_car.view.adapter.community;

import android.app.Activity;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.ctb_open_car.CTBApplication;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.community.ResourceFileDto;
import com.ctb_open_car.bean.community.response.FocusFeedData;
import com.ctb_open_car.bean.community.response.UserData;
import com.ctb_open_car.bean.community.response.feed.FeedContentDto;
import com.ctb_open_car.bean.community.response.feed.FeedDto;
import com.ctb_open_car.presenter.FeedDetailPresenter;
import com.ctb_open_car.presenter.LikedPresenter;
import com.ctb_open_car.presenter.TransmitPresenter;
import com.ctb_open_car.presenter.UserFollowPresenter;
import com.ctb_open_car.utils.DateUtils;
import com.ctb_open_car.utils.Device;
import com.ctb_open_car.utils.PreferenceUtils;
import com.ctb_open_car.view.ImageGridLayout;
import com.ctb_open_car.view.SearchView;
import com.ctb_open_car.view.activity.community.FeedsDetailActivity;
import com.ctb_open_car.view.activity.person.PersonHomeActivity;
import com.ctb_open_car.view.activity.search.SearchUserActivity;
import com.ctb_open_car.view.adapter.viewholder.BaseTransmitEventViewHolder;
import com.ctb_open_car.view.adapter.viewholder.BaseNewsViewHolder;
import com.ctb_open_car.view.adapter.viewholder.BaseViewHolder;
import com.library.InputTextMsgDialog;
import com.qmuiteam.qmui.span.QMUITouchableSpan;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FocusFeedsAdapter extends RecyclerView.Adapter {
    public List<FeedDto> mFocusFeedList;

    private Activity mActivity;
    private FocusFeedData mFocusList;

    private int highlightTextNormalColor;
    private int highlightTextPressedColor;
    private int highlightBgNormalColor;
    private int highlightBgPressedColor;

    private static final int FEEDS_TYPE_TRANSMIT_EVENTS = 1;
    private static final int FEEDS_TYPE_ORIGIN_NEWS = 2;
    private static final int FEEDS_TYPE_TRANSMIT_NEWS = 3;
    private static final int FEEDS_TYPE_HEADER = 0;

    private UserData mUserData;

    public FocusFeedsAdapter(Activity context) {
        this.mActivity = context;
        mFocusFeedList = new ArrayList<>();
        initColor();
    }


    /**
     * 设置关注动态列表
     */
    public void setData(FocusFeedData focusData) {
        this.mFocusList = focusData;
        if (null != focusData && null != focusData.getPageData() && null != focusData.getPageData().getData()) {
            mFocusFeedList.addAll(focusData.getPageData().getData());
            if (focusData.getPageData().getData().size() > 0) {
                notifyDataSetChanged();
            }
        }
    }


    /**
     * 设置本人信息
     */
    public void setHostData(UserData mUserData) {
        this.mUserData = mUserData;
        if (null != mUserData && null != mUserData.getUserHome()) {
            notifyDataSetChanged();
        }
    }

    private void initColor() {
        highlightTextNormalColor = ContextCompat.getColor(mActivity, R.color.color_text_highlight);
        highlightTextPressedColor = ContextCompat.getColor(mActivity, R.color.color_text_highlight);
        highlightBgNormalColor = ContextCompat.getColor(mActivity, R.color.transmit_content_color);
        highlightBgPressedColor = QMUIResHelper.getAttrColor(mActivity, R.attr.qmui_config_color_gray_6);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder focusFeedsHolder = null;
        if (viewType == FEEDS_TYPE_HEADER) {
            return new FocusHostHolder(LayoutInflater.from(mActivity).inflate(R.layout.ugc_focus_header_item, null));

        } else if (FEEDS_TYPE_TRANSMIT_EVENTS == viewType) {
            return new FocusEventsHolder(LayoutInflater.from(mActivity).inflate(R.layout.ugc_focus_events_item, null));

        } else if (viewType == FEEDS_TYPE_ORIGIN_NEWS || viewType == FEEDS_TYPE_TRANSMIT_NEWS) {
            return new FocusNewsViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.ugc_focus_news_item, null));
        }
        return new FocusNewsViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.ugc_focus_news_item, null));
    }

    private FeedDto getFocusData(int p) {
        return mFocusFeedList.get(p - 1);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FocusHostHolder) {
            FocusHostHolder hostHolder = (FocusHostHolder) holder;
            if (null == mUserData || null == mUserData.getUserHome()) {
                return;
            }
            hostHolder.mUserName.setText(mUserData.getUserHome().getNickName());
            Glide.with(mActivity).load(mUserData.getUserHome().getUserIcon().getResourceUrl())
                    .placeholder(R.drawable.avatar)
                    .transform(new RoundedCorners(Device.dip2px(15)))
                    .into(hostHolder.mUserAvatar);
            if (0 == mUserData.getUserHome().userAuthStatus) {
                hostHolder.mUserAuthState.setVisibility(View.INVISIBLE);
            } else {
                hostHolder.mUserAuthState.setVisibility(View.VISIBLE);
            }
            hostHolder.mUserFans.setText(mUserData.getUserHome().getUserStat().getFansCnt() + "");
            hostHolder.mUserFocus.setText(mUserData.getUserHome().getUserStat().getAttentionCnt() + "");
        } else {
            FeedDto feedDto = getFocusData(position);
            BaseViewHolder currentViewHolder = null;

            currentViewHolder = (BaseViewHolder) holder;

            if (holder instanceof FocusNewsViewHolder) {
                FocusNewsViewHolder mNewsHolder = (FocusNewsViewHolder) holder;

                //动态
                if (1 == feedDto.getFeedType()) {
                    //原发动态
                    mNewsHolder.mOriginView.setVisibility(View.VISIBLE);
                    mNewsHolder.mTransmitView.setVisibility(View.GONE);
                    mNewsHolder.mUserLocation.setText(feedDto.getFeedPlaceName());

                    //动态图片
                    List<String> imageList = new ArrayList<>();
                    if (null != feedDto.getFeedImageList()) {
                        for (ResourceFileDto imageFile : feedDto.getFeedImageList()) {
                            imageList.add(imageFile.getResourceUrl());
                        }
                        mNewsHolder.mImageGridLayout.setUrlList(imageList);
                        mNewsHolder.mImageGridLayout.setIsShowAll(true);
                        mNewsHolder.mImageGridLayout.setSpacing(5);
                    }

                    //消息动态内容
                    if (null != feedDto.getFeedContents() && feedDto.getFeedContents().size() > 0) {
                        List<FeedContentDto> feedContentDtos = feedDto.getFeedContents();
                        for (FeedContentDto contentDto : feedContentDtos) {
                            if (null != contentDto) {
                                mNewsHolder.mOriginFeedText.setText(contentDto.getFeedContent());
                                /**设置省略*/
                                mNewsHolder.ellipsizeOriginDescText(mActivity, R.string.feed_ellipsize);
                            }
                        }
                    } else {
                        mNewsHolder.mOriginFeedText.setText("");
                    }
                } else if (2 == feedDto.getFeedType()) {
                    //转发动态
                    mNewsHolder.mTransmitView.setVisibility(View.VISIBLE);
                    mNewsHolder.mOriginView.setVisibility(View.VISIBLE);
                    mNewsHolder.mBottomExtraLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.transmit_content_color));
                    mNewsHolder.mImageGridLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.transmit_content_color));
                    mNewsHolder.mUserLocation.setText(feedDto.getFeedPlaceName());
                    mNewsHolder.mBottomDivider.setVisibility(View.GONE);
                    //动态图片
                    List<String> imageList = new ArrayList<>();
                    if (null != feedDto.getForwardFeedCard() && null != feedDto.getForwardFeedCard().getFeedImageList()) {
                        for (ResourceFileDto imageFile : feedDto.getForwardFeedCard().getFeedImageList()) {
                            imageList.add(imageFile.getResourceUrl());
                        }
                        mNewsHolder.mImageGridLayout.setUrlList(imageList);
                        mNewsHolder.mImageGridLayout.setIsShowAll(true);
                        mNewsHolder.mImageGridLayout.setSpacing(5);
                    }

                    if (null != feedDto.getFeedContents()) {
                        List<FeedContentDto> feedContentDtos = feedDto.getFeedContents();
                        StringBuilder sb = new StringBuilder();
                        for (FeedContentDto contentDto : feedContentDtos) {
                            if (null != contentDto) {
                                if (null == contentDto.getUserId()) {
                                    //当前转发消息
                                    sb.append(contentDto.getFeedContent());
                                } else {
                                    //拼接历史转发/原发消息
                                    sb.append("// @").append(contentDto.getNickName()).append(contentDto.getFeedContent());
                                    sb.append(mNewsHolder.generateSp(sb.toString()));
                                }
                            }
                        }
                        //设置 当前消息
                        mNewsHolder.mOriginFeedText.setText(sb.toString());
                        /**设置原消息省略*/
                        mNewsHolder.ellipsizeOriginDescText(mActivity, R.string.feed_ellipsize);

                        //转发消息
                        String transmitOrigin = "@" + feedDto.getForwardFeedCard().getFeedUser().nickName
                                + "：" + feedDto.getForwardFeedCard().getFeedContents().get(0).getFeedContent();

                        mNewsHolder.mTransmitOriginFeedText.setText(mNewsHolder.generateSp(transmitOrigin));

                        /**设置转发消息省略*/
//                        mNewsHolder.ellipsizeTransmitDescText(mActivity,R.string.feed_ellipsize);
                        mNewsHolder.mOriginFeedText.setMovementMethodDefault();
                        mNewsHolder.mOriginFeedText.setNeedForceEventToParent(true);

                        mNewsHolder.mTransmitOriginFeedText.setMovementMethodDefault();
                        mNewsHolder.mTransmitOriginFeedText.setNeedForceEventToParent(true);
                    }
                }
            } else if (holder instanceof FocusEventsHolder) {

                FocusEventsHolder mEventsHolder = (FocusEventsHolder) holder;
                //活动信息
                mEventsHolder.mEventTitle.setText(feedDto.getForwardFeedCard().getActivityTitle());
                mEventsHolder.mEventCurrentFeedText.setText(feedDto.getForwardFeedCard().getActivityDesc());
                /**设置省略内容*/
                mEventsHolder.ellipsizeOriginEventText(mActivity, R.string.event_ellipsize);
                mEventsHolder.mStartDate.setText("出发日期:" + DateUtils.formatDate(feedDto.getForwardFeedCard().getActivityBegintime()));
                mEventsHolder.mEventDeadline.setText("报名截止:" + DateUtils.formatDate(feedDto.getForwardFeedCard().getActivityEnrollEndtime()));
                mEventsHolder.mEventMaxMembers.setText("人数上限:" + feedDto.getForwardFeedCard().getActivityEnrollLimit());

                //背景颜色
                mEventsHolder.mEventCurrentFeedText.setBackgroundColor(mActivity.getResources().getColor(R.color.transmit_content_color));
                mEventsHolder.mEventTransmittedText.setBackgroundColor(mActivity.getResources().getColor(R.color.transmit_content_color));
                mEventsHolder.mEventInfoLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.transmit_content_color));
                mEventsHolder.mEventTitleInfoLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.transmit_content_color));
                mEventsHolder.mEventCoverLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.transmit_content_color));

                mEventsHolder.mEventOriginView.setVisibility(View.VISIBLE);
                mEventsHolder.mEventTransmitView.setVisibility(View.VISIBLE);
                mEventsHolder.mLocationLayout.setVisibility(View.VISIBLE);

                // 活动封面
                if (null != feedDto.getForwardFeedCard().getActivityCoverImage()) {
                    ImageView eventCover = new ImageView(mActivity);
                    eventCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, Device.dip2px(167));
                    eventCover.setLayoutParams(params);
                    Glide.with(mActivity).load(feedDto.getForwardFeedCard().getActivityCoverImage().getResourceUrl()).into(eventCover);
                    mEventsHolder.mEventCoverLayout.removeAllViews();
                    mEventsHolder.mEventCoverLayout.addView(eventCover);
                }


                //转发活动位置
                mEventsHolder.mLocationText.setText(feedDto.getForwardFeedCard().getActivityDeparturePlace());
                mEventsHolder.mEventTitle.setText(feedDto.getForwardFeedCard().getActivityTitle());

                if (null != feedDto.getFeedContents()) {
                    List<FeedContentDto> feedContentDtos = feedDto.getFeedContents();
                    StringBuilder sb = new StringBuilder();
                    for (FeedContentDto contentDto : feedContentDtos) {
                        if (null != contentDto) {
                            if (null == contentDto.getUserId()) {
                                //当前转发消息
                                sb.append(contentDto.getFeedContent());
                            } else {
                                //拼接历史转发/原发消息
                                sb.append("// @").append(contentDto.getNickName() + "：").append(contentDto.getFeedContent());
                            }
                        }
                    }
                    //设置 当前消息
                    mEventsHolder.mEventCurrentFeedText.setText(mEventsHolder.generateSp(sb.toString()));
                    mEventsHolder.mEventTransmittedText.setVisibility(View.VISIBLE);
                    //转发消息
                    String transmitOrigin = "@" + feedDto.getForwardFeedCard().getActivityUser().nickName
                            + "：" + DateUtils.formatDate(feedDto.getPublishTime()) + " 发布";

                    //活动状态
                    if (mEventsHolder.verifyStatus(feedDto.getForwardFeedCard().getActivityEnrollEndtime())) {
                        mEventsHolder.mEventStatus.setText("报名中");
                        mEventsHolder.mEventStatus.setTextColor(mActivity.getResources().getColor(R.color.community_event_begin_status));
                        mEventsHolder.mEventStatus.setBackground(mActivity.getResources().getDrawable(R.drawable.event_state_ongoing_border));
                    } else {
                        //结束
                        mEventsHolder.mEventStatus.setText("报名截止");
                        mEventsHolder.mEventStatus.setTextColor(mActivity.getResources().getColor(R.color.community_tab_txt_normal_color));
                        mEventsHolder.mEventStatus.setBackground(mActivity.getResources().getDrawable(R.drawable.event_state_finished_border));
                    }
                    mEventsHolder.mEventTransmittedText.setText(mEventsHolder.generateSp(transmitOrigin));
                    mEventsHolder.mEventCurrentFeedText.setMovementMethodDefault();
                    mEventsHolder.mEventCurrentFeedText.setNeedForceEventToParent(true);

                    mEventsHolder.mEventTransmittedText.setMovementMethodDefault();
                    mEventsHolder.mEventTransmittedText.setNeedForceEventToParent(true);
                }
            }

            //转评赞阅
            currentViewHolder.mUserTransCount.setText(feedDto.getFeedStat().getForwardCnt() + "");
            currentViewHolder.mUserCmtCount.setText(feedDto.getFeedStat().getCommentCnt() + "");
            currentViewHolder.mUserReadCount.setText(feedDto.getFeedStat().getReadCnt() + "");
            currentViewHolder.mUserLikeCount.setText(feedDto.getFeedStat().getPraiseCnt() + "");

            //设置用户信息
            currentViewHolder.mUserName.setText(feedDto.getFeedUser().nickName);
            currentViewHolder.mUserPubDate.setText(DateUtils.convertTimeToFormat(feedDto.getPublishTime()));
            currentViewHolder.mUserSign.setText(feedDto.getFeedUser().getUserSign());

            /**设置用户vip状态*/
            currentViewHolder.setUserVipStatus(feedDto.getFeedUser().userAuthStatus);

            Glide.with(mActivity).load(feedDto.getFeedUser().userIcon.getResourceUrl())
                    .placeholder(R.drawable.avatar)
                    .transform(new RoundedCorners(Device.dip2px(15)))
                    .into(currentViewHolder.mUserAvatar);
            Glide.with(mActivity).load(feedDto.getFeedUser().userMoodIcon).into(currentViewHolder.mUserMood);


            //隐藏自己的关注按钮
            if (feedDto.getFeedUser().getUserId() == PreferenceUtils.getLong(CTBApplication.getInstance(), "userId")) {
                currentViewHolder.mUserFollowStatus.setVisibility(View.GONE);
            } else {
                currentViewHolder.mUserFollowStatus.setVisibility(View.VISIBLE);
                /**设置关注状态*/
                currentViewHolder.getFollowStatus(feedDto.getFeedUser().getRelationStatus());
            }
        }
    }

    @Override
    public int getItemCount() {
        if (null == mFocusList || null == mFocusList.getPageData() || null == mFocusList.getPageData().getData()) {
            return Collections.emptyList().size() + 1;
        }
        return mFocusFeedList.size() + 1;
    }


    @Override
    public int getItemViewType(int position) {
        if (0 == position) {
            return FEEDS_TYPE_HEADER;
        } else {
            if (1 == getFocusData(position).getFeedUgcType()) {
                //动态消息
                if (1 == getFocusData(position).getFeedType()) {
                    //  原发动态消息
                    return FEEDS_TYPE_ORIGIN_NEWS;
                } else if (2 == getFocusData(position).getFeedType()) {
                    //  转发动态消息
                    return FEEDS_TYPE_TRANSMIT_NEWS;
                }
            } else if (2 == getFocusData(position).getFeedUgcType()) {
                //转发活动消息
                return FEEDS_TYPE_TRANSMIT_EVENTS;
            }
        }
        return 0;
    }

    /**
     * 关注活动消息Holder
     */
    public class FocusHostHolder extends RecyclerView.ViewHolder {

        //        用户头像
        @BindView(R.id.ugc_user_avatar)
        public ImageView mUserAvatar;

        //        用户名字
        @BindView(R.id.ugc_news_user)
        public TextView mUserName;

        //        用户粉丝数

        @BindView(R.id.ugc_user_fans_num)
        public TextView mUserFans;
        //        用户关注

        @BindView(R.id.ugc_user_follow_num)
        public TextView mUserFocus;

        //        用户认证状态
        @BindView(R.id.ugc_user_auth_status)
        public ImageView mUserAuthState;      //        用户认证状态

        @BindView(R.id.search)
        public SearchView mUserSearch;

        public FocusHostHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null == mUserData || null == mUserData.getUserHome()) {
                        return;
                    }
                    Intent i = new Intent(itemView.getContext(), PersonHomeActivity.class);
                    i.putExtra("user_id", mUserData.getUserHome().userId);
                    itemView.getContext().startActivity(i);
                }
            });

            mUserSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    Intent i = new Intent(mActivity, SearchUserActivity.class);
                    i.putExtra("search_key", v.getText());
                    mActivity.startActivity(i);
                    return true;
                }
            });
        }

    }


    /**
     * 关注动态消息Holder
     */
    public class FocusNewsViewHolder extends BaseNewsViewHolder {

        @BindView(R.id.ugc_feeds_normal_layout)
        public LinearLayout mNewsOrigin;
        @BindView(R.id.ugc_feeds_transmit_layout)
        public LinearLayout mNewsTransmit;

        @BindView(R.id.feed_content_origin)
        public View mOriginView;

        @BindView(R.id.feed_news_bottom)
        public View mFeedsBottomLayout;

        @BindView(R.id.feeds_extra_info)
        RelativeLayout mBottomExtraLayout;

        @BindView(R.id.feed_content_transmit)
        public View mTransmitView;


        @BindView(R.id.feeds_pics)
        ImageGridLayout mImageGridLayout;

        private UserFollowPresenter mUserFollowPresenter;

        public FocusNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mTransmitOriginFeedText.setMovementMethodDefault();
            mTransmitOriginFeedText.setNeedForceEventToParent(true);
            mUserFollowPresenter = new UserFollowPresenter(itemView.getContext(), this);
            mTransmitPresenter = new TransmitPresenter((RxAppCompatActivity) mActivity);
        }


        /***
         * 用户转发
         * **/
        @Override
        protected void onTransmitClick(View v) {
            super.onTransmitClick(v);
            FeedDto feedDto = getFocusData(getAdapterPosition());

            showDialog(v.getContext(), new InputTextMsgDialog.OnTextSendListener() {
                @Override
                public void onTextSend(String msg) {
                    String lng = CTBApplication.getInstance().getRxApp().mHeadBean.getLongitude() + "";
                    String lat = CTBApplication.getInstance().getRxApp().mHeadBean.getLatitude() + "";
                    String feedId = feedDto.getFeedId() + "";

                    mTransmitPresenter.transmitFeed(lng, lat, msg, "1", feedId);
                }
            });
        }

        /**
         * 关注点击
         */
        @Override
        protected void onFocusClick(View v) {
            super.onFocusClick(v);
            FeedDto feedDto = getFocusData(getAdapterPosition());
            String targetUserId = feedDto.getFeedUser().getUserId() + "";
            if (0 == feedDto.getFeedUser().getRelationStatus()) {
                mUserFollowPresenter.userFollow(targetUserId);
            } else {
                mUserFollowPresenter.userCancelFollow(targetUserId);
            }
        }

        /**
         * 关注回调设置
         */
        @Override
        public void actionCancelSuccess() {
            super.actionCancelSuccess();
            getFocusData(getAdapterPosition()).getFeedUser().setRelationStatus(0);
        }

        @Override
        public void actionFollowSuccess() {
            super.actionFollowSuccess();
            getFocusData(getAdapterPosition()).getFeedUser().setRelationStatus(1);
        }

        /**
         * 点击跳转到个人主页
         */
        @Override
        protected void onAvatarClick(View v) {
            super.onAvatarClick(v);
            Intent i = new Intent(mActivity, PersonHomeActivity.class);
            FeedDto feedDto = getFocusData(getAdapterPosition());

            i.putExtra("user_id", feedDto.getFeedUser().userId);
            mActivity.startActivity(i);
        }


        @OnClick(R.id.feed_like_layout)
        public void onFeedLike(View v) {
            LikedPresenter presenter = new LikedPresenter(mActivity);
//            if (!getActivitys().isPraised()) {
            FeedDto feedDto = getFocusData(getAdapterPosition());
            presenter.feedLike(feedDto.getFeedId() + "",
                    feedDto.getFeedUser().userId + "");
            mUserLikeCount.setText(Integer.parseInt(mUserLikeCount.getText().toString()) + 1 + "");
            feedDto.setPraised(true);
//                presenter.activityUnLike(getActivitys().getActivityId() + "");
//                mUserLikeCount.setText(Integer.parseInt(mUserLikeCount.getText().toString())-1+"");
//            } else {

//            }
        }

        @OnClick({R.id.feed_content_origin, R.id.feeds_pics, R.id.feed_content_transmit})
        void onMsgInfoClick(View v) {
            Intent i = new Intent();
            int position = getAdapterPosition();
            FeedDto feedDto = getFocusData(position);
            i.setClass(mActivity, FeedsDetailActivity.class);
            i.putExtra("feedId", feedDto.getFeedId());
            mActivity.startActivity(i);
        }

        /**
         * 点击弹框评论
         */
        @OnClick(R.id.feed_cmt_layout)
        public void onCommentClick(View v) {
            InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(mActivity, R.style.dialog_center);
            inputTextMsgDialog.setHint("写评论...");
            inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                @Override
                public void onTextSend(String msg) {
                    //点击发送按钮后，回调此方法，msg为输入的值
                    FeedDetailPresenter presenter = new FeedDetailPresenter(mActivity);
                    int position = getAdapterPosition();
                    presenter.pubFeedComment(getFocusData(position).getFeedId() + "", msg, "");
                }
            });
            inputTextMsgDialog.show();
        }

        public SpannableString generateSp(String text) {
            String highlightStart = "@";
            String highlightEnd = "：";
            SpannableString sp = new SpannableString(text);
            int start = 0, end;
            int index;
            while ((index = text.indexOf(highlightStart, start)) > -1) {
                end = text.indexOf(highlightEnd, start);
                sp.setSpan(new QMUITouchableSpan(highlightTextNormalColor, highlightTextPressedColor,
                        highlightBgNormalColor, highlightBgPressedColor) {
                    @Override
                    public void onSpanClick(View widget) {
                        Intent i = new Intent(mActivity, PersonHomeActivity.class);

                        i.putExtra("user_id", getFocusData(getAdapterPosition()).getFeedUser().getUserId());
                        mActivity.startActivity(i);
                    }
                }, index, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                start = end;
            }
            return sp;
        }

    }

    /**
     * 附近活动消息Holder
     */
    public class FocusEventsHolder extends BaseTransmitEventViewHolder {

        private UserFollowPresenter mUserFollowPresenter;

        public FocusEventsHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //活动消息类型在 关注栏目下 以转发消息方式呈现
            mEventTransmitView.setVisibility(View.VISIBLE);
            mEventOriginView.setVisibility(View.GONE);

            mUserFollowPresenter = new UserFollowPresenter(itemView.getContext(), this);
            mTransmitPresenter = new TransmitPresenter((RxAppCompatActivity) mActivity);
        }

        /***
         * 用户转发
         * **/
        @Override
        protected void onTransmitClick(View v) {
            super.onTransmitClick(v);
            FeedDto feedDto = getFocusData(getAdapterPosition());

            showDialog(v.getContext(), new InputTextMsgDialog.OnTextSendListener() {
                @Override
                public void onTextSend(String msg) {
                    String lng = CTBApplication.getInstance().getRxApp().mHeadBean.getLongitude() + "";
                    String lat = CTBApplication.getInstance().getRxApp().mHeadBean.getLatitude() + "";
                    String feedId = feedDto.getFeedId() + "";

                    mTransmitPresenter.transmitFeed(lng, lat, msg, "1", feedId);
                }
            });
        }


        @OnClick({R.id.ugc_feeds_normal_layout,
                R.id.ugc_feeds_transmit_layout,
                R.id.feed_event_info,
                R.id.feed_event_covers,
                R.id.feed_event_titleinfo})
        void onMsgInfoClick(View v) {
            Intent i = new Intent();
            int position = getAdapterPosition();
            FeedDto feedDto = getFocusData(position);
            i.setClass(mActivity, FeedsDetailActivity.class);
            i.putExtra("feedId", feedDto.getFeedId());
            mActivity.startActivity(i);
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
            FeedDto feedDto = getFocusData(getAdapterPosition());
            String targetUserId = feedDto.getFeedUser().getUserId() + "";
            if (0 == feedDto.getFeedUser().getRelationStatus()) {
                mUserFollowPresenter.userFollow(targetUserId);
            } else {
                mUserFollowPresenter.userCancelFollow(targetUserId);

            }
        }

        @Override
        public void actionCancelSuccess() {
            super.actionCancelSuccess();
            getFocusData(getAdapterPosition()).getFeedUser().setRelationStatus(0);
        }

        @Override
        public void actionFollowSuccess() {
            super.actionFollowSuccess();
            getFocusData(getAdapterPosition()).getFeedUser().setRelationStatus(1);
        }


        @OnClick(R.id.feed_like_layout)
        public void onFeedLike(View v) {
            LikedPresenter presenter = new LikedPresenter(mActivity);
//            if (!getActivitys().isPraised()) {
            FeedDto feedDto = getFocusData(getAdapterPosition());
            presenter.feedLike(feedDto.getFeedId() + "",
                    feedDto.getFeedUser().userId + "");
            mUserLikeCount.setText(Integer.parseInt(mUserLikeCount.getText().toString()) + 1 + "");
            feedDto.setPraised(true);
//                presenter.activityUnLike(getActivitys().getActivityId() + "");
//                mUserLikeCount.setText(Integer.parseInt(mUserLikeCount.getText().toString())-1+"");
//            } else {

//            }
        }


        /**
         * 点击跳转到个人主页
         */
        @Override
        protected void onAvatarClick(View v) {
            super.onAvatarClick(v);
            Intent i = new Intent(mActivity, PersonHomeActivity.class);
            FeedDto feedDto = getFocusData(getAdapterPosition());

            i.putExtra("user_id", feedDto.getFeedUser().userId);
            mActivity.startActivity(i);
        }


        /**
         * 点击弹框评论
         */
        @OnClick(R.id.feed_cmt_layout)
        public void onCommentClick(View v) {
            InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(mActivity, R.style.dialog_center);
            inputTextMsgDialog.setHint("写评论...");
            inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                @Override
                public void onTextSend(String msg) {
                    //点击发送按钮后，回调此方法，msg为输入的值
                    FeedDetailPresenter presenter = new FeedDetailPresenter(mActivity);
                    int position = getAdapterPosition();
                    presenter.pubFeedComment(getFocusData(position).getFeedId() + "", msg, "");

                    //评论数+1
                    int increaseCount = Integer.parseInt(mUserCmtCount.getText().toString()) + 1;
                    mUserCmtCount.setText(+increaseCount + "");
                }
            });
            inputTextMsgDialog.show();
        }


        public SpannableString generateSp(String text) {
            String highlightStart = "@";
            String highlightEnd = "：";
            SpannableString sp = new SpannableString(text);
            int start = 0, end;
            int index;
            while ((index = text.indexOf(highlightStart, start)) > -1) {
                end = text.indexOf(highlightEnd, start);
                sp.setSpan(new QMUITouchableSpan(highlightTextNormalColor, highlightTextPressedColor,
                        highlightBgNormalColor, highlightBgPressedColor) {
                    @Override
                    public void onSpanClick(View widget) {
                        Intent i = new Intent(mActivity, PersonHomeActivity.class);

                        i.putExtra("user_id", getFocusData(getAdapterPosition()).getFeedUser().getUserId());
                        mActivity.startActivity(i);
                    }
                }, index, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                start = end;
            }
            return sp;
        }
    }


}
