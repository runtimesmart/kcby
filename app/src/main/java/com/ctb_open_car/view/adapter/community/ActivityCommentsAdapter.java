package com.ctb_open_car.view.adapter.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.ctb_open_car.bean.community.ActivityDetailData;
import com.ctb_open_car.bean.community.ResourceFileDto;
import com.ctb_open_car.bean.community.response.CommentData;
import com.ctb_open_car.bean.community.response.activity.ActivityDetailDto;
import com.ctb_open_car.bean.community.response.activity.ActivityDto;
import com.ctb_open_car.bean.community.response.comment.CommentDto;
import com.ctb_open_car.constraints.AppContraint;
import com.ctb_open_car.presenter.LikedPresenter;
import com.ctb_open_car.presenter.TransmitPresenter;
import com.ctb_open_car.presenter.UserFollowPresenter;
import com.ctb_open_car.utils.DateUtils;
import com.ctb_open_car.utils.Device;
import com.ctb_open_car.utils.PreferenceUtils;
import com.ctb_open_car.view.activity.community.ActivityDetailActivity;
import com.ctb_open_car.view.activity.person.PersonHomeActivity;
import com.ctb_open_car.view.adapter.viewholder.ActivityCommentViewHolder;
import com.ctb_open_car.view.adapter.viewholder.BaseTransmitEventViewHolder;
import com.ctb_open_car.view.adapter.viewholder.BaseViewHolder;
import com.ctb_open_car.view.fragment.dialog.EnrollEventDialog;
import com.library.InputTextMsgDialog;
import com.qmuiteam.qmui.span.QMUITouchableSpan;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.shinichi.library.ImagePreview;
import cc.shinichi.library.bean.ImageInfo;
import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class ActivityCommentsAdapter extends RecyclerView.Adapter {

    public List<CommentDto> mCommonDtoList;
    private static final int DETAIL_COMMENT_TYPE = 1;
    private static final int DETAIL_EVENT_TYPE = 2;
    private SoftReference<ActivityDetailActivity> mActivity;
    private ActivityDetailData mDetailData;
    private CommentData mCommentData;

    private int highlightTextNormalColor;
    private int highlightTextPressedColor;
    private int highlightBgNormalColor;
    private int highlightBgPressedColor;

    public ActivityCommentsAdapter(ActivityDetailActivity context) {
        this.mActivity = new SoftReference<>(context);
        initColor(mActivity.get());
        mCommonDtoList = new ArrayList<>();
    }

    public void getCurrentPosition() {
    }

    /**
     * 设置详情数据并刷新
     */
    public void setDetailData(ActivityDetailData detailData) {
        mDetailData = detailData;
        notifyItemChanged(0, mDetailData);
    }

    /**
     * 设置评论列表数据并刷新
     */
    public void setCommentData(CommentData commentData) {
        mCommentData = commentData;
        if (null != commentData && null != commentData.getPageData() && null != commentData.getPageData().getData()) {
            mCommonDtoList.addAll(commentData.getPageData().getData());
            if (commentData.getPageData().getData().size() > 0) {
                notifyItemRangeChanged(1, mCommonDtoList.size());
            }
        }
    }

    /**
     * 设置评论列表数据并刷新
     */
    public CommentDto getCommentData(int position) {
        return mCommonDtoList.get(position - 1);
    }


    /**
     * 获取动态详情数据
     */
    public ActivityDetailDto getActivitys() {
        if (null == mDetailData) {
            return null;
        }
        return mDetailData.getActivityDetail();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (DETAIL_EVENT_TYPE == viewType) {
            //活动布局
            return new EventDetailHolder(LayoutInflater.from(mActivity.get()).inflate(R.layout.ugc_topic_comment_event_header_view, null));
        } else {
            return new ActivityCommentViewHolder(LayoutInflater.from(mActivity.get()).inflate(R.layout.ugc_topic_comment_item, null),
                    mActivity.get(), this);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ActivityCommentViewHolder) {
            ActivityCommentViewHolder commentViewHolder = (ActivityCommentViewHolder) holder;
            CommentDto commentDto = getCommentData(position);
            if (null == commentDto) {
                return;
            }
            //设置用户信息
            commentViewHolder.mUserName.setText(commentDto.getCommentUser().nickName);

            if (1 == commentDto.getCommentUser().userAuthStatus) {
                commentViewHolder.mUserAuthState.setVisibility(View.VISIBLE);
            } else {
                commentViewHolder.mUserAuthState.setVisibility(View.INVISIBLE);
            }
            Glide.with(mActivity.get()).load(commentDto.getCommentUser().userIcon.getResourceUrl())
                    .placeholder(R.drawable.avatar)
                    .transform(new RoundedCorners(Device.dip2px(15)))
                    .into(commentViewHolder.mUserAvatar);
            Glide.with(mActivity.get()).load(commentDto.getCommentUser().userMoodIcon).into(commentViewHolder.mUserMood);
            if (null == commentDto.getReplyComment()) {
                commentViewHolder.mSecondaryCmtLayout.setVisibility(View.GONE);

            } else {
                String cmtString = commentDto.getReplyComment().getCommentUser().nickName + "：";
                commentViewHolder.mSecondaryCmtContent.setText(generateSp(cmtString + commentDto.getReplyComment().getCommentContent()));
                commentViewHolder.mSecondaryCmtLayout.setVisibility(View.VISIBLE);
            }

            //点赞、发布日期
            commentViewHolder.mCmtPubDate.setText(DateUtils.convertTimeToFormat(commentDto.getCommentTime()) + "");
            commentViewHolder.mCmtLikeCount.setText(commentDto.getCommentPraiseCnt() + "");
            commentViewHolder.mNewsOrigin.setText(commentDto.getCommentContent());

        } else {
            ActivityDetailDto activityDto = getActivitys();
            if (null == activityDto) {
                return;
            }
            BaseViewHolder currentViewHolder = null;
            if (holder instanceof EventDetailHolder) {
                currentViewHolder = (BaseViewHolder) holder;
                EventDetailHolder mDetailEventHolder = (EventDetailHolder) holder;
                //活动信息
                mDetailEventHolder.mEventTitle.setText(activityDto.getActivityTitle());
                mDetailEventHolder.mEventTitle.setMaxLines(10);
                mDetailEventHolder.mEventTitle.setMaxEms(12);
                mDetailEventHolder.mEventCurrentFeedText.setText(activityDto.getActivityDesc());
                mDetailEventHolder.mEventCurrentFeedText.setMaxLines(100);
                mDetailEventHolder.mStartDate.setText("出发日期:" + DateUtils.formatDate(activityDto.getActivityBegintime()));
                mDetailEventHolder.mEventDeadline.setText("报名截止:" + DateUtils.formatDate(activityDto.getActivityEnrollEndtime()));
                mDetailEventHolder.mEventMaxMembers.setText("人数上限:" + activityDto.getActivityEnrollLimit());

                //背景颜色
                mDetailEventHolder.mEventCurrentFeedText.setBackgroundColor(mActivity.get().getResources().getColor(R.color.transmit_content_color));
                mDetailEventHolder.mEventTransmittedText.setBackgroundColor(mActivity.get().getResources().getColor(R.color.transmit_content_color));
                mDetailEventHolder.mEventInfoLayout.setBackgroundColor(mActivity.get().getResources().getColor(R.color.transmit_content_color));
                mDetailEventHolder.mEventTitleInfoLayout.setBackgroundColor(mActivity.get().getResources().getColor(R.color.transmit_content_color));
                mDetailEventHolder.mEventCoverLayout.setBackgroundColor(mActivity.get().getResources().getColor(R.color.transmit_content_color));

                mDetailEventHolder.mEventOriginView.setVisibility(View.VISIBLE);
                mDetailEventHolder.mEventTransmitView.setVisibility(View.VISIBLE);
                mDetailEventHolder.mLocationLayout.setVisibility(View.VISIBLE);

                // 活动封面

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, Device.dip2px(167));
                params.setMargins(0, Device.dip2px(10), 0, Device.dip2px(10));

                mDetailEventHolder.mEventCoverLayout.removeAllViews();
                if (0 == activityDto.getActivityImageList().size()) {
                    mDetailEventHolder.addInviteButton(activityDto);
                }
                int index = 0;
                for (ResourceFileDto imgDto : activityDto.getActivityImageList()) {
                    ImageView eventCover = new ImageView(mActivity.get());
                    eventCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    eventCover.setLayoutParams(params);
                    Glide.with(mActivity.get()).load(imgDto.getResourceUrl()).into(eventCover);
                    //添加imageview
                    mDetailEventHolder.mEventCoverLayout.addView(eventCover);
                    if (++index == 1 || (activityDto.getActivityImageList().size() > 1 && index == activityDto.getActivityImageList().size())) {
                        mDetailEventHolder.addInviteButton(activityDto);
                    }
                    eventCover.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ImagePreview
                                    .getInstance()
                                    .setEnableDragClose(true)
                                    // 上下文，必须是activity，不需要担心内存泄漏，本框架已经处理好；
                                    .setContext(mActivity.get())
                                    .setImage(imgDto.getResourceUrl())
                                    // 设置从第几张开始看（索引从0开始）
                                    .setIndex(0)

                                    .start();
                        }
                    });
                }

                //活动状态
                if (mDetailEventHolder.verifyStatus(activityDto.getActivityEnrollEndtime())) {
                    mDetailEventHolder.mEventStatus.setText("报名中");
                    mDetailEventHolder.mEventStatus.setTextColor(mActivity.get().getResources().getColor(R.color.community_event_begin_status));
                    mDetailEventHolder.mEventStatus.setBackground(mActivity.get().getResources().getDrawable(R.drawable.event_state_ongoing_border));
                } else {
                    //结束
                    mDetailEventHolder.mEventStatus.setText("报名截止");
                    mDetailEventHolder.mEventStatus.setTextColor(mActivity.get().getResources().getColor(R.color.community_tab_txt_normal_color));
                    mDetailEventHolder.mEventStatus.setBackground(mActivity.get().getResources().getDrawable(R.drawable.event_state_finished_border));
                }


                //转发活动位置
                mDetailEventHolder.mLocationText.setText(activityDto.getActivityDeparturePlace());
                mDetailEventHolder.mEventTitle.setText(activityDto.getActivityTitle());

                //转评赞阅
                currentViewHolder.mUserTransCount.setText(activityDto.getActivityStat().getForwardCnt() + "");
                currentViewHolder.mUserCmtCount.setText(activityDto.getActivityStat().getCommentCnt() + "");
                currentViewHolder.mUserReadCount.setText(activityDto.getActivityStat().getReadCnt() + "");
                currentViewHolder.mUserLikeCount.setText(activityDto.getActivityStat().getPraiseCnt() + "");

                //设置用户信息
                currentViewHolder.mUserName.setText(activityDto.getActivityUser().nickName);
                currentViewHolder.mUserPubDate.setText(DateUtils.convertTimeToFormat(activityDto.getPublishTime()));
                currentViewHolder.mUserSign.setText(activityDto.getActivityUser().getUserSign());

                /**设置用户vip状态*/
                currentViewHolder.setUserVipStatus(activityDto.getActivityUser().getUserAuthStatus());

                if (null != activityDto.getActivityUser().getUserIcon()) {
                    Glide.with(mActivity.get()).load(activityDto.getActivityUser().getUserIcon().getResourceUrl())
                            .placeholder(R.drawable.avatar)
                            .transform(new RoundedCorners(Device.dip2px(15)))
                            .into(currentViewHolder.mUserAvatar);
                }
                Glide.with(mActivity.get()).load(activityDto.getActivityUser().userMoodIcon).into(currentViewHolder.mUserMood);

                //隐藏自己的关注按钮
                if (activityDto.getActivityUser().getUserId() == PreferenceUtils.getLong(CTBApplication.getInstance(), "userId")) {
                    currentViewHolder.mUserFollowStatus.setVisibility(View.GONE);
                } else {
                    /**设置关注状态*/
                    currentViewHolder.getFollowStatus(activityDto.getActivityUser().getRelationStatus());
                }
            }


        }

    }

    @Override
    public int getItemCount() {
        if (null == mCommentData || null == mCommentData.getPageData()) {
            if (null == mDetailData) {
                return 0;
            }
            return Collections.emptyList().size() + 1;
        }
        return mCommonDtoList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (0 == position) {
            return DETAIL_EVENT_TYPE;
        } else {
            return DETAIL_COMMENT_TYPE;
        }
    }


    public class EventDetailHolder extends BaseTransmitEventViewHolder {

        @BindView(R.id.ugc_feed_location)
        public TextView mLocationText;

        @BindView(R.id.feeds_extra_info)
        public RelativeLayout mLocationLayout;

        private UserFollowPresenter mUserFollowPresenter;

        public EventDetailHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mUserFollowPresenter = new UserFollowPresenter(itemView.getContext(), this);
            mTransmitPresenter = new TransmitPresenter(mActivity.get());
            mUserFollowStatus.setVisibility(View.VISIBLE);
        }

        public void addInviteButton(ActivityDto activityDto) {
            Button voteButton = new Button(mActivity.get());
            voteButton.setText("立即报名");
            voteButton.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP
                    , 7, mActivity.get().getResources().getDisplayMetrics()));
            voteButton.setTextColor(mActivity.get().getResources().getColor(android.R.color.white));
            voteButton.setBackground(mActivity.get().getDrawable(R.drawable.event_selected_bg));
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP
                    , 200, mActivity.get().getResources().getDisplayMetrics()), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP
                    , 44, mActivity.get().getResources().getDisplayMetrics()));
            ll.gravity = Gravity.CENTER_HORIZONTAL;
            voteButton.setLayoutParams(ll);
            ll.setMargins(0, Device.dip2px(20), 0, Device.dip2px(20));

            if (!verifyStatus(activityDto.getActivityEnrollEndtime())) {
                voteButton.setBackground(mActivity.get().getDrawable(R.drawable.event_status_end_bg));
                voteButton.setEnabled(false);
                voteButton.setText("报名截止");

            } else {
                voteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null == activityDto.getActivityInviteIcon()) {
                            Toasty.info(mActivity.get(), "暂无邀请图片").show();
                            return;
                        }

                        EnrollEventDialog dialog = new EnrollEventDialog(mActivity.get());
                        Bundle b = new Bundle();
                        b.putString("title", activityDto.getActivityTitle());
                        b.putString("image", activityDto.getActivityInviteIcon().getResourceUrl());
                        dialog.setArguments(b);
                        dialog.show(mActivity.get().getSupportFragmentManager(), "enroll");
                    }
                });
            }

            mEventCoverLayout.addView(voteButton);
        }

        @OnClick(R.id.share_weixin)
        void shareWeiXin(View v) {
            ActivityDetailDto detailDto = getActivitys();
            setmShareWx(v.getContext(), detailDto.getActivityTitle(), detailDto.getActivityDesc(),
                    AppContraint.WeiXin.sEventUrl + detailDto.getActivityId(), R.mipmap.app_launcher);
        }

        @OnClick(R.id.share_pengyouquan)
        void sharePengyou(View v) {
            ActivityDetailDto detailDto = getActivitys();
            setmShareWxPengyou(v.getContext(), detailDto.getActivityTitle(), detailDto.getActivityDesc(),
                    AppContraint.WeiXin.sEventUrl + detailDto.getActivityId(), R.mipmap.app_launcher);
        }

        @OnClick(R.id.share_weibo)
        void shareWeiBo(View v) {
            ActivityDetailDto detailDto = getActivitys();

            UMImage image = new UMImage(mActivity.get(), R.mipmap.app_launcher);
            UMWeb umWeb = new UMWeb(AppContraint.WeiXin.sFeedUrl + detailDto.getActivityId(),
                    detailDto.getActivityUser().nickName + "的动态"
                    , detailDto.getActivityDesc(), image); //URL 标题 描述 封面图
            new ShareAction(mActivity.get())
                    .setPlatform(SHARE_MEDIA.SINA)//传入平台
                    .withText(detailDto.getActivityUser().nickName + "的动态")//标题
                    .withMedia(umWeb)
                    .setCallback(shareListener)//回调监听器
                    .share();
        }

        private UMShareListener shareListener = new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {

            }

            @Override
            public void onResult(SHARE_MEDIA platform) {
                Timber.i("分享成功");
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                Timber.i("分享失败");
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                Timber.i("分享取消");
            }
        };

        /***
         * 用户转发
         * **/
        @Override
        protected void onTransmitClick(View v) {
            super.onTransmitClick(v);

            showDialog(v.getContext(), new InputTextMsgDialog.OnTextSendListener() {
                @Override
                public void onTextSend(String msg) {
                    String lng = CTBApplication.getInstance().getRxApp().mHeadBean.getLongitude() + "";
                    String lat = CTBApplication.getInstance().getRxApp().mHeadBean.getLatitude() + "";
                    String feedId = getActivitys().getActivityId() + "";

                    mTransmitPresenter.transmitFeed(lng, lat, msg, "2", feedId);
                }
            });
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
            String targetUserId = getActivitys().getActivityUser().getUserId() + "";

            if (0 == getActivitys().getActivityUser().getRelationStatus()) {
                mUserFollowPresenter.userFollow(targetUserId);
            } else {
                mUserFollowPresenter.userCancelFollow(targetUserId);
            }
        }

        @Override
        public void actionCancelSuccess() {
            super.actionCancelSuccess();
            getActivitys().getActivityUser().setRelationStatus(0);
        }

        @Override
        public void actionFollowSuccess() {
            super.actionFollowSuccess();
            getActivitys().getActivityUser().setRelationStatus(1);
        }

        /**
         * 点击跳转到个人主页
         */
        @Override
        protected void onAvatarClick(View v) {
            super.onAvatarClick(v);
            Intent i = new Intent(mActivity.get(), PersonHomeActivity.class);

            i.putExtra("user_id", getActivitys().getActivityUser().getUserId());
            mActivity.get().startActivity(i);
        }

        @OnClick(R.id.feed_like_layout)
        public void onActivityLike(View v) {
            LikedPresenter presenter = new LikedPresenter(mActivity.get());
//            if (!getActivitys().isPraised()) {
            presenter.activityLike(getActivitys().getActivityId() + "", getActivitys().getActivityUser().userId + "");
            mUserLikeCount.setText(Integer.parseInt(mUserLikeCount.getText().toString()) + 1 + "");
            mDetailData.getActivityDetail().setPraised(true);
//                presenter.activityUnLike(getActivitys().getActivityId() + "");
//                mUserLikeCount.setText(Integer.parseInt(mUserLikeCount.getText().toString())-1+"");
//            } else {

//            }
        }

        /**
         * 点击弹框评论
         */
        @OnClick(R.id.feed_cmt_layout)
        public void onCommentClick(View v) {
            InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(mActivity.get(), R.style.dialog_center);
            inputTextMsgDialog.setHint("写评论...");
            inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                @Override
                public void onTextSend(String msg) {
                    //点击发送按钮后，回调此方法，msg为输入的值
                    mActivity.get().mDetailPresenter.pubFeedComment(getActivitys().getActivityId() + "", msg, "");
                }
            });
            inputTextMsgDialog.show();
        }
    }


    protected void initColor(Context context) {
        highlightTextNormalColor = ContextCompat.getColor(context, R.color.color_text_highlight);
        highlightTextPressedColor = ContextCompat.getColor(context, R.color.color_text_highlight);
        highlightBgNormalColor = ContextCompat.getColor(context, R.color.transmit_content_color);
        highlightBgPressedColor = QMUIResHelper.getAttrColor(context, R.attr.qmui_config_color_gray_6);
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
//                    Toasty.info(CTBBaseApplication.getInstance(), "click ").show();
                }
            }, index, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            start = end;
        }
        return sp;
    }
}
