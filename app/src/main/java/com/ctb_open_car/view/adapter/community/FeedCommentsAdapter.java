package com.ctb_open_car.view.adapter.community;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
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
import com.ctb_open_car.bean.community.FeedDetailData;
import com.ctb_open_car.bean.community.ResourceFileDto;
import com.ctb_open_car.bean.community.response.CommentData;
import com.ctb_open_car.bean.community.response.activity.ActivityCardDto;
import com.ctb_open_car.bean.community.response.comment.CommentDto;
import com.ctb_open_car.bean.community.response.feed.FeedContentDto;
import com.ctb_open_car.bean.community.response.feed.FeedDto;
import com.ctb_open_car.constraints.AppContraint;
import com.ctb_open_car.presenter.FeedDetailPresenter;
import com.ctb_open_car.presenter.LikedPresenter;
import com.ctb_open_car.presenter.TransmitPresenter;
import com.ctb_open_car.presenter.UserFollowPresenter;
import com.ctb_open_car.utils.DateUtils;
import com.ctb_open_car.utils.Device;
import com.ctb_open_car.utils.PreferenceUtils;
import com.ctb_open_car.view.ImageGridLayout;
import com.ctb_open_car.view.activity.community.ActivityDetailActivity;
import com.ctb_open_car.view.activity.community.FeedsDetailActivity;
import com.ctb_open_car.view.activity.person.PersonHomeActivity;
import com.ctb_open_car.view.adapter.viewholder.BaseTransmitEventViewHolder;
import com.ctb_open_car.view.adapter.viewholder.BaseNewsViewHolder;
import com.ctb_open_car.view.adapter.viewholder.BaseViewHolder;
import com.ctb_open_car.view.adapter.viewholder.FeedsCommentViewHolder;
import com.ctb_open_car.wbapi.ShareBean;
import com.ctb_open_car.wbapi.WeiBoShareManager;
import com.library.InputTextMsgDialog;
import com.qmuiteam.qmui.span.QMUITouchableSpan;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView;
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
import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class FeedCommentsAdapter extends RecyclerView.Adapter {

    public List<CommentDto> mCommentDtoList;
    private static final int DETAIL_COMMENTS_TYPE = 1;
    private static final int DETAIL_NEWS_TYPE = 2;
    private static final int DETAIL_EVENT_TYPE = 3;
    private SoftReference<FeedsDetailActivity> mActivity;
    private FeedDetailData mDetailData;
    private CommentData mCommentData;

    private int highlightTextNormalColor;
    private int highlightTextPressedColor;
    private int highlightBgNormalColor;
    private int highlightBgPressedColor;

    public FeedCommentsAdapter(FeedsDetailActivity context) {
        this.mActivity = new SoftReference<>(context);
        this.mCommentDtoList = new ArrayList<>();
        initColor();
    }

    private void initColor() {
        highlightTextNormalColor = ContextCompat.getColor(mActivity.get(), R.color.color_text_highlight);
        highlightTextPressedColor = ContextCompat.getColor(mActivity.get(), R.color.color_text_highlight);
        highlightBgNormalColor = ContextCompat.getColor(mActivity.get(), R.color.transmit_content_color);
        highlightBgPressedColor = QMUIResHelper.getAttrColor(mActivity.get(), R.attr.qmui_config_color_gray_6);
    }

    /**
     * 设置详情数据并刷新
     */
    public void setDetailData(FeedDetailData detailData) {
        mDetailData = detailData;
        notifyItemChanged(0, mDetailData);
    }

    /**
     * 设置评论列表数据并刷新
     */
    public void setCommentData(CommentData commentData) {
        mCommentData = commentData;
        if (null != commentData && null != commentData.getPageData() && null != commentData.getPageData().getData()) {
            mCommentDtoList.addAll(commentData.getPageData().getData());
            if (commentData.getPageData().getData().size() > 0) {
                notifyItemRangeChanged(1, mCommentData.getPageData().getData().size());
            }
        }
    }

    /**
     * 设置评论列表数据并刷新
     */
    public CommentDto getCommentData(int position) {
        return mCommentDtoList.get(position - 1);
    }


    /**
     * 获取动态详情数据
     */
    public FeedDto getFeeds() {
        if (null == mDetailData) {
            return null;
        }
        return mDetailData.getFeed();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (DETAIL_NEWS_TYPE == viewType) {
            return new FeedDetailNewsHolder(LayoutInflater.from(mActivity.get()).inflate(R.layout.ugc_topic_comment_news_header_view, null));
        } else if (DETAIL_EVENT_TYPE == viewType) {
            return new FeedDetailEventHolder(LayoutInflater.from(mActivity.get()).inflate(R.layout.ugc_topic_comment_event_header_view, null));
        } else {
            return new FeedsCommentViewHolder(LayoutInflater.from(mActivity.get()).inflate(R.layout.ugc_topic_comment_item, null),
                    mActivity.get(), this);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FeedsCommentViewHolder) {
            //评论
            FeedsCommentViewHolder commentViewHolder = (FeedsCommentViewHolder) holder;
            CommentDto commentDto = getCommentData(position);
            if (null == commentDto || null == commentDto.getCommentUser()) {
                return;
            }
            //设置用户信息
            commentViewHolder.mUserName.setText(commentDto.getCommentUser().nickName);

            if (1 == commentDto.getCommentUser().userAuthStatus) {
                commentViewHolder.mUserAuthState.setVisibility(View.VISIBLE);
            } else {
                commentViewHolder.mUserAuthState.setVisibility(View.INVISIBLE);
            }
            if (null != commentDto.getCommentUser().getUserIcon()) {
                Glide.with(mActivity.get()).load(commentDto.getCommentUser().getUserIcon().getResourceUrl())
                        .placeholder(R.drawable.avatar)
                        .transform(new RoundedCorners(Device.dip2px(15)))
                        .into(commentViewHolder.mUserAvatar);
                Glide.with(mActivity.get()).load(commentDto.getCommentUser().getUserMoodIcon()).into(commentViewHolder.mUserMood);
            }
            if (null == commentDto.getReplyComment()) {
                commentViewHolder.mSecondaryCmtLayout.setVisibility(View.GONE);
            } else {
                String cmtString = commentDto.getReplyComment().getCommentUser().nickName + "：";
                commentViewHolder.mSecondaryCmtContent.setText(commentViewHolder.generateSp(cmtString + commentDto.getReplyComment().getCommentContent()));
                commentViewHolder.mSecondaryCmtLayout.setVisibility(View.VISIBLE);
            }
            //点赞、发布日期
            commentViewHolder.mCmtPubDate.setText(DateUtils.convertTimeToFormat(commentDto.getCommentTime()) + "");
            commentViewHolder.mCmtLikeCount.setText(commentDto.getCommentPraiseCnt() + "");
            commentViewHolder.mNewsOrigin.setText(commentDto.getCommentContent());

        } else {
            FeedDto feedDto = getFeeds();
            if (null == feedDto) return;
            BaseViewHolder currentViewHolder = null;
            if (holder instanceof FeedDetailNewsHolder) {
                //动态
                FeedDetailNewsHolder detailNewsHolder = (FeedDetailNewsHolder) holder;
                if (1 == feedDto.getFeedType()) {
                    //原发动态
                    currentViewHolder = detailNewsHolder;
                    detailNewsHolder.mOriginView.setVisibility(View.VISIBLE);
                    detailNewsHolder.mTransmitView.setVisibility(View.GONE);
                    detailNewsHolder.mUserLocation.setText(feedDto.getFeedPlaceName());

                    //动态图片
                    List<String> imageList = new ArrayList<>();
                    if (null != feedDto.getFeedImageList()) {
                        for (ResourceFileDto imageFile : feedDto.getFeedImageList()) {
                            imageList.add(imageFile.getResourceUrl());
                        }
                        detailNewsHolder.mImageGridLayout.setUrlList(imageList);
                        detailNewsHolder.mImageGridLayout.setIsShowAll(true);
                        detailNewsHolder.mImageGridLayout.setSpacing(5);
                    }
                    /**设置原消息消息不限制*/
                    detailNewsHolder.mOriginFeedText.setMaxLines(100);
                    //消息动态内容
                    if (null != feedDto.getFeedContents()) {
                        List<FeedContentDto> feedContentDtos = feedDto.getFeedContents();
                        for (FeedContentDto contentDto : feedContentDtos) {
                            if (null != contentDto) {
                                detailNewsHolder.mOriginFeedText.setText(contentDto.getFeedContent());
                            }
                        }
                    }

                } else if (2 == feedDto.getFeedType()) {
                    //转发动态
                    currentViewHolder = detailNewsHolder;

                    detailNewsHolder.mTransmitView.setVisibility(View.VISIBLE);
                    detailNewsHolder.mOriginView.setVisibility(View.VISIBLE);
                    detailNewsHolder.mBottomExtraLayout.setBackgroundColor(mActivity.get().getResources().getColor(R.color.transmit_content_color));
                    detailNewsHolder.mImageGridLayout.setBackgroundColor(mActivity.get().getResources().getColor(R.color.transmit_content_color));
                    detailNewsHolder.mUserLocation.setText(feedDto.getFeedPlaceName());
                    detailNewsHolder.mBottomDivider.setVisibility(View.GONE);
                    //动态图片
                    List<String> imageList = new ArrayList<>();
                    if (null != feedDto.getForwardFeedCard() && null != feedDto.getForwardFeedCard().getFeedImageList()) {
                        for (ResourceFileDto imageFile : feedDto.getForwardFeedCard().getFeedImageList()) {
                            imageList.add(imageFile.getResourceUrl());
                        }
                        detailNewsHolder.mImageGridLayout.setUrlList(imageList);
                        detailNewsHolder.mImageGridLayout.setIsShowAll(true);
                        detailNewsHolder.mImageGridLayout.setSpacing(5);
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
                                    sb.append(detailNewsHolder.generateSp(sb.toString()));
                                }
                            }
                        }
                        //设置 当前消息
                        detailNewsHolder.mOriginFeedText.setText(sb.toString());
                        /**设置原消息消息不限制*/
                        detailNewsHolder.mOriginFeedText.setMaxLines(100);


                        detailNewsHolder.mTransmitOriginFeedText.setVisibility(View.VISIBLE);
                        //转发消息
                        String transmitOrigin = "@" + feedDto.getForwardFeedCard().getFeedUser().nickName
                                + "：" + feedDto.getForwardFeedCard().getFeedContents().get(0).getFeedContent();

                        detailNewsHolder.mTransmitOriginFeedText.setText(detailNewsHolder.generateSp(transmitOrigin));
                        /**设置转发消息省略*/
//                        detailNewsHolder.ellipsizeTransmitDescText(mActivity.get(),R.string.feed_ellipsize);

                        detailNewsHolder.mOriginFeedText.setMovementMethodDefault();
                        detailNewsHolder.mOriginFeedText.setNeedForceEventToParent(true);

                        detailNewsHolder.mTransmitOriginFeedText.setMovementMethodDefault();
                        detailNewsHolder.mTransmitOriginFeedText.setNeedForceEventToParent(true);
                    }
                }
            } else if (holder instanceof FeedDetailEventHolder) {
//            动态中转发活动详情
                FeedDetailEventHolder detailEventHolder = (FeedDetailEventHolder) holder;
                currentViewHolder = detailEventHolder;

                //活动信息
                detailEventHolder.mEventTitle.setText(feedDto.getForwardFeedCard().getActivityTitle());
                detailEventHolder.mEventCurrentFeedText.setText(feedDto.getForwardFeedCard().getActivityDesc());
                detailEventHolder.mStartDate.setText("出发日期:" + DateUtils.formatDate(feedDto.getForwardFeedCard().getActivityBegintime()));
                detailEventHolder.mEventDeadline.setText("报名截止:" + DateUtils.formatDate(feedDto.getForwardFeedCard().getActivityEnrollEndtime()));
                detailEventHolder.mEventMaxMembers.setText("人数上限:" + feedDto.getForwardFeedCard().getActivityEnrollLimit());

                //进行中
                if (detailEventHolder.verifyStatus(feedDto.getForwardFeedCard().getActivityEnrollEndtime())) {
                    detailEventHolder.mEventStatus.setText("报名中");
                    detailEventHolder.mEventStatus.setTextColor(mActivity.get().getResources().getColor(R.color.community_event_begin_status));
                    detailEventHolder.mEventStatus.setBackground(mActivity.get().getResources().getDrawable(R.drawable.event_state_ongoing_border));
                } else {
                    //结束
                    detailEventHolder.mEventStatus.setText("报名截止");
                    detailEventHolder.mEventStatus.setTextColor(mActivity.get().getResources().getColor(R.color.community_tab_txt_normal_color));
                    detailEventHolder.mEventStatus.setBackground(mActivity.get().getResources().getDrawable(R.drawable.event_state_finished_border));
                }

                //背景颜色
                detailEventHolder.mEventCurrentFeedText.setBackgroundColor(mActivity.get().getResources().getColor(R.color.transmit_content_color));
                detailEventHolder.mEventTransmittedText.setBackgroundColor(mActivity.get().getResources().getColor(R.color.transmit_content_color));
                detailEventHolder.mEventInfoLayout.setBackgroundColor(mActivity.get().getResources().getColor(R.color.transmit_content_color));
                detailEventHolder.mEventTitleInfoLayout.setBackgroundColor(mActivity.get().getResources().getColor(R.color.transmit_content_color));
                detailEventHolder.mEventCoverLayout.setBackgroundColor(mActivity.get().getResources().getColor(R.color.transmit_content_color));

                detailEventHolder.mEventOriginView.setVisibility(View.VISIBLE);
                detailEventHolder.mEventTransmitView.setVisibility(View.VISIBLE);
                detailEventHolder.mLocationLayout.setVisibility(View.VISIBLE);

                // 活动封面

                if (null != feedDto.getForwardFeedCard().getActivityCoverImage()) {
                    ImageView eventCover = new ImageView(mActivity.get());
                    eventCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, Device.dip2px(167));
                    eventCover.setLayoutParams(params);
                    Glide.with(mActivity.get()).load(feedDto.getForwardFeedCard().getActivityCoverImage().getResourceUrl()).into(eventCover);
                    detailEventHolder.mEventCoverLayout.removeAllViews();
                    detailEventHolder.mEventCoverLayout.addView(eventCover);
                }

                //转发活动位置
                detailEventHolder.mLocationText.setText(feedDto.getForwardFeedCard().getActivityDeparturePlace());
                detailEventHolder.mEventTitle.setText(feedDto.getForwardFeedCard().getActivityTitle());

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
                    detailEventHolder.mEventCurrentFeedText.setText(detailEventHolder.generateSp(sb.toString()));
                    detailEventHolder.mEventTransmittedText.setVisibility(View.VISIBLE);
                    //转发消息
                    String transmitOrigin = "@" + feedDto.getForwardFeedCard().getActivityUser().getNickName()
                            + "：" + DateUtils.formatDate(feedDto.getPublishTime()) + " 发布";

                    detailEventHolder.mEventTransmittedText.setText(detailEventHolder.generateSp(transmitOrigin));

                    detailEventHolder.mEventCurrentFeedText.setMovementMethodDefault();
                    detailEventHolder.mEventCurrentFeedText.setNeedForceEventToParent(true);

                    detailEventHolder.mEventTransmittedText.setMovementMethodDefault();
                    detailEventHolder.mEventTransmittedText.setNeedForceEventToParent(true);
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

            if (1 == feedDto.getFeedUser().userAuthStatus) {
                /**设置用户vip状态*/
                currentViewHolder.setUserVipStatus(feedDto.getFeedUser().userAuthStatus);
            }
            Glide.with(mActivity.get()).load(feedDto.getFeedUser().userMoodIcon).into(currentViewHolder.mUserMood);
            Glide.with(mActivity.get()).load(feedDto.getFeedUser().userIcon.getResourceUrl())
                    .placeholder(R.drawable.avatar)
                    .transform(new RoundedCorners(Device.dip2px(15)))
                    .into(currentViewHolder.mUserAvatar);

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
        if (null == mCommentData || null == mCommentData.getPageData()) {
            if (null == mDetailData) {
                return 0;
            }
            return Collections.emptyList().size() + 1;
        }
        return mCommentDtoList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (0 == position) {
            if (null == getFeeds()) {
                return DETAIL_NEWS_TYPE;
            }
            if (1 == getFeeds().getFeedUgcType()) {
                return DETAIL_NEWS_TYPE;
            } else {
                return DETAIL_EVENT_TYPE;
            }
        } else {
            return DETAIL_COMMENTS_TYPE;
        }
    }


    public class FeedDetailEventHolder extends BaseTransmitEventViewHolder {

        @BindView(R.id.ugc_feed_location)
        public TextView mLocationText;

        @BindView(R.id.feeds_extra_info)
        public RelativeLayout mLocationLayout;

        private UserFollowPresenter mUserFollowPresenter;

        public FeedDetailEventHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mUserFollowPresenter = new UserFollowPresenter(itemView.getContext(), this);
        }

        /**
         * 点击活动信息布局跳转活动详情
         */
        @OnClick({R.id.feed_event_info,
                R.id.feed_event_covers,
                R.id.feed_event_titleinfo})
        void toEventDetailClick(View v) {
            Intent i = new Intent(mActivity.get(), ActivityDetailActivity.class);
            i.putExtra("activityId", getFeeds().getForwardFeedCard().getActivityId());
            mActivity.get().startActivity(i);
        }

        @OnClick(R.id.share_weixin)
        void shareWeiXin(View v) {
            FeedDto dto = getFeeds();
            setmShareWx(v.getContext(), dto.getFeedContents().get(0).getFeedContent(), dto.getFeedContents().get(0).getFeedContent()
                    , AppContraint.WeiXin.sFeedUrl + dto.getFeedId(), R.mipmap.app_launcher);
        }


        @OnClick(R.id.share_pengyouquan)
        void sharePengyou(View v) {
            FeedDto dto = getFeeds();
            setmShareWxPengyou(v.getContext(), dto.getFeedContents().get(0).getFeedContent(), dto.getFeedContents().get(0).getFeedContent()
                    , AppContraint.WeiXin.sFeedUrl + dto.getFeedId(), R.mipmap.app_launcher);
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
            String targetUserId = getFeeds().getFeedUser().getUserId() + "";
            if (0 == getFeeds().getFeedUser().getRelationStatus()) {
                mUserFollowPresenter.userFollow(targetUserId);
            } else {
                mUserFollowPresenter.userCancelFollow(targetUserId);
            }
        }


        @Override
        public void actionCancelSuccess() {
            super.actionCancelSuccess();
            getFeeds().getFeedUser().setRelationStatus(0);
        }

        @Override
        public void actionFollowSuccess() {
            super.actionFollowSuccess();
            getFeeds().getFeedUser().setRelationStatus(1);
        }

        /**
         * 点击跳转到个人主页
         */
        @Override
        protected void onAvatarClick(View v) {
            super.onAvatarClick(v);
            Intent i = new Intent(mActivity.get(), PersonHomeActivity.class);

            i.putExtra("user_id", getFeeds().getFeedUser().getUserId());
            mActivity.get().startActivity(i);
        }

        @OnClick(R.id.feed_like_layout)
        public void onFeedLike(View v) {
            LikedPresenter presenter = new LikedPresenter(mActivity.get());
//            if (!getActivitys().isPraised()) {
            presenter.feedLike(getFeeds().getFeedId() + "", getFeeds().getFeedUser().userId + "");
            mUserLikeCount.setText(Integer.parseInt(mUserLikeCount.getText().toString()) + 1 + "");
            mDetailData.getFeed().setPraised(true);
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
                    FeedDetailPresenter presenter = new FeedDetailPresenter(mActivity.get());
                    presenter.pubFeedComment(getFeeds().getFeedId() + "", msg, "");
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
                        Intent i = new Intent(mActivity.get(), PersonHomeActivity.class);

                        i.putExtra("user_id", getFeeds().getFeedUser().getUserId());
                        mActivity.get().startActivity(i);
                    }
                }, index, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                start = end;
            }
            return sp;
        }

    }


    /**
     * 头部话题信息布局
     */
    public class FeedDetailNewsHolder extends BaseNewsViewHolder {

        @BindView(R.id.ugc_feeds_normal_layout)
        public LinearLayout mNewsOrigin;
        @BindView(R.id.ugc_feeds_transmit_layout)
        public LinearLayout mNewsTransmit;

        @BindView(R.id.feed_content_origin)
        public View mOriginView;

        @BindView(R.id.feed_content_transmit)
        public View mTransmitView;

        @BindView(R.id.feeds_extra_info)
        RelativeLayout mBottomExtraLayout;

        @BindView(R.id.feeds_pics)
        ImageGridLayout mImageGridLayout;

        @BindView(R.id.ugc_bottom_layout)
        public View mFeedsBottomLayout;

        @BindView(R.id.share_to_third)
        public View mShareLayout;

        @BindView(R.id.bottom_divider)
        public View mBottomDivider;
        private UserFollowPresenter mUserFollowPresenter;

        public FeedDetailNewsHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            initColor(mActivity.get());
            mUserFollowPresenter = new UserFollowPresenter(itemView.getContext(), this);
            mTransmitPresenter = new TransmitPresenter(mActivity.get());
            mShareLayout.setVisibility(View.VISIBLE);
        }

        @OnClick(R.id.share_weixin)
        void shareWeiXin(View v) {
            FeedDto feedDto = getFeeds();
            setmShareWx(v.getContext(), feedDto.getFeedContents().get(0).getFeedContent(),
                    feedDto.getFeedContents().get(0).getFeedContent(),
                    AppContraint.WeiXin.sFeedUrl + feedDto.getFeedId(), R.mipmap.app_launcher);
        }

        @OnClick(R.id.share_pengyouquan)
        void sharePengyou(View v) {
            FeedDto feedDto = getFeeds();
            setmShareWxPengyou(v.getContext(), feedDto.getFeedContents().get(0).getFeedContent(),
                    feedDto.getFeedContents().get(0).getFeedContent(),
                    AppContraint.WeiXin.sFeedUrl + feedDto.getFeedId(), R.mipmap.app_launcher);

//            WbShareHandler shareHandler = new WbShareHandler();
//            shareHandler.registerApp();
//            shareHandler.setProgressColor(0xff33b5e5);
//            FeedDto feedDto = getFeeds();
//            ShareBean bean = new ShareBean();
//            bean.setTitle(feedDto.getFeedUser().nickName + "的动态");
//            bean.setDescription(feedDto.getFeedContents().get(0).getFeedContent());
//            bean.setDefaultText(feedDto.getFeedContents().get(0).getFeedContent());
//            bean.setActionUrl(AppContraint.WeiXin.sFeedUrl + feedDto.getFeedId());
//            WeiBoShareManager.getWebpageObj(bean, mActivity.get());
        }

        @OnClick(R.id.share_weibo)
        void shareWeiBo(View v) {
            FeedDto feedDto = getFeeds();
            UMImage image = new UMImage(mActivity.get(), R.mipmap.app_launcher);
            UMWeb umWeb = new UMWeb(AppContraint.WeiXin.sFeedUrl + feedDto.getFeedId(),
                    feedDto.getFeedUser().nickName + "的动态"
                    , feedDto.getFeedContents().get(0).getFeedContent(), image); //URL 标题 描述 封面图
            new ShareAction(mActivity.get())
                    .setPlatform(SHARE_MEDIA.SINA)//传入平台
                    .withText(feedDto.getFeedUser().nickName + "的动态")//标题
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
                    String feedId = getFeeds().getFeedId() + "";

                    mTransmitPresenter.transmitFeed(lng, lat, msg, "1", feedId);
                }
            });
        }

        /**
         * 点击活动信息布局跳转活动详情
         */
        @OnClick({R.id.feed_content_origin,
                R.id.feeds_pics,
                R.id.feed_content_transmit})
        void toTransmitDetailClick(View v) {
            if (2 == getFeeds().getFeedType()) {
                Intent i = new Intent(mActivity.get(), FeedsDetailActivity.class);
                i.putExtra("feedId", getFeeds().getForwardFeedCard().getFeedId());
                mActivity.get().startActivity(i);
            }
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
            String targetUserId = getFeeds().getFeedUser().getUserId() + "";
            if (0 == getFeeds().getFeedUser().getRelationStatus()) {
                mUserFollowPresenter.userFollow(targetUserId);
            } else {
                mUserFollowPresenter.userCancelFollow(targetUserId);
            }
        }

        @Override
        public void actionCancelSuccess() {
            super.actionCancelSuccess();
            getFeeds().getFeedUser().setRelationStatus(0);
        }

        @Override
        public void actionFollowSuccess() {
            super.actionFollowSuccess();
            getFeeds().getFeedUser().setRelationStatus(1);
        }

        @OnClick(R.id.feed_like_layout)
        public void onFeedLike(View v) {
            LikedPresenter presenter = new LikedPresenter(mActivity.get());
//            if (!getActivitys().isPraised()) {
            presenter.feedLike(getFeeds().getFeedId() + "", getFeeds().getFeedUser().userId + "");
            mUserLikeCount.setText(String.format("%d", Integer.parseInt(mUserLikeCount.getText().toString()) + 1));
            mDetailData.getFeed().setPraised(true);
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
            Intent i = new Intent(mActivity.get(), PersonHomeActivity.class);

            i.putExtra("user_id", getFeeds().getFeedUser().getUserId());
            mActivity.get().startActivity(i);
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
                    FeedDetailPresenter presenter = new FeedDetailPresenter(mActivity.get());
                    mActivity.get().mDetailPresenter.pubFeedComment(getFeeds().getFeedId() + "", msg, "");

                    //评论数+1
                    int increaseCount = Integer.parseInt(mUserCmtCount.getText().toString()) + 1;
                    mUserCmtCount.setText(+increaseCount + "");
                }
            });
            inputTextMsgDialog.show();
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
                        Intent i = new Intent(mActivity.get(), PersonHomeActivity.class);

                        i.putExtra("user_id", getFeeds().getFeedUser().getUserId());
                        mActivity.get().startActivity(i);
                    }
                }, index, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                start = end;
            }
            return sp;
        }
    }


}
