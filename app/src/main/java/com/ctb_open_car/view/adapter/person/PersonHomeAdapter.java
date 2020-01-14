package com.ctb_open_car.view.adapter.person;

import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.community.ResourceFileDto;
import com.ctb_open_car.bean.community.response.HotFeedData;
import com.ctb_open_car.bean.community.response.feed.FeedContentDto;
import com.ctb_open_car.bean.community.response.feed.FeedDto;
import com.ctb_open_car.bean.community.response.user.UserCardDto;
import com.ctb_open_car.presenter.FeedDetailPresenter;
import com.ctb_open_car.presenter.LikedPresenter;
import com.ctb_open_car.utils.DateUtils;
import com.ctb_open_car.utils.Device;
import com.ctb_open_car.view.ImageGridLayout;
import com.ctb_open_car.view.activity.community.FeedsDetailActivity;
import com.ctb_open_car.view.activity.person.PersonHomeActivity;
import com.ctb_open_car.view.adapter.community.HotExpertAdapter;
import com.ctb_open_car.view.adapter.viewholder.BaseNewsViewHolder;
import com.ctb_open_car.view.adapter.viewholder.BaseTransmitEventViewHolder;
import com.ctb_open_car.view.adapter.viewholder.BaseViewHolder;
import com.ctb_open_car.view.fragment.comminity.UGCHotFragment;
import com.library.InputTextMsgDialog;
import com.qmuiteam.qmui.span.QMUITouchableSpan;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class PersonHomeAdapter {

//        extends RecyclerView.Adapter {

//    public List<FeedDto> mHotFeedDtoList;
//    private List<UserCardDto> mRecommendList;
//
//    private static final int PERSON_TYPE_HEADER = 1;
//    private static final int PERSON_TYPE_SUMMARY = 2;
//
//
//    private SoftReference<UGCHotFragment> mFragment;
//    private HotFeedData mHotFeedData;
//    private int highlightTextNormalColor;
//    private int highlightTextPressedColor;
//    private int highlightBgNormalColor;
//    private int highlightBgPressedColor;
//
//    public PersonHomeAdapter(UGCHotFragment mContext) {
//        this.mFragment = new SoftReference<>(mContext);
//        mHotFeedDtoList = new ArrayList<>();
//        mRecommendList = new ArrayList<>();
//        initColor();
//    }
//
//    public void setData(HotFeedData hotFeedData) {
//        this.mHotFeedData = hotFeedData;
//        if (null != hotFeedData && null != hotFeedData.getPageData() && null != hotFeedData.getPageData().getData()) {
//            mHotFeedDtoList.addAll(hotFeedData.getPageData().getData());
//            if (hotFeedData.getPageData().getData().size() > 0) {
//                notifyDataSetChanged();
//            }
//        }
//    }
//
//
//    private void initColor() {
//        highlightTextNormalColor = ContextCompat.getColor(mFragment.get().getContext(), R.color.color_text_highlight);
//        highlightTextPressedColor = ContextCompat.getColor(mFragment.get().getContext(), R.color.color_text_highlight);
//        highlightBgNormalColor = ContextCompat.getColor(mFragment.get().getContext(), R.color.transmit_content_color);
//        highlightBgPressedColor = QMUIResHelper.getAttrColor(mFragment.get().getContext(), R.attr.qmui_config_color_gray_6);
//    }
//
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (FEEDS_TYPE__RECOMMEND_HEADER == viewType) {
//            return new HotRecommendHolder(LayoutInflater.from(mFragment.get().getContext()).inflate(R.layout.ugc_hotlist_header_expert_layout, null));
//
//        } else if (FEEDS_TYPE_TRANSMIT_EVENTS == viewType) {
//            return new HotEventsHolder(LayoutInflater.from(mFragment.get().getContext()).inflate(R.layout.ugc_hotlist_event_item, null));
//        } else if (viewType == FEEDS_TYPE_ORIGIN_NEWS || viewType == FEEDS_TYPE_TRANSMIT_NEWS) {
//            return new HotNewsHolder(LayoutInflater.from(mFragment.get().getContext()).inflate(R.layout.ugc_hotlist_news_item, null));
//        }
//        return new HotNewsHolder(LayoutInflater.from(mFragment.get().getContext()).inflate(R.layout.ugc_hotlist_news_item, null));
//
//    }
//
//    private FeedDto getHotData(int p) {
//        return mHotFeedDtoList.get(p - 1);
//    }
//
//    @Override
//    public int getItemCount() {
//        if (null == mHotFeedData || null == mHotFeedData.getPageData() || null == mHotFeedData.getPageData().getData()) {
//            return Collections.emptyList().size();
//        }
//        return mHotFeedDtoList.size() + 1;
//    }
//
//
//    @Override
//    public int getItemViewType(int position) {
//        if (0 == position) {
//            return PERSON_TYPE_HEADER;
//        } else {
//            if (1 == getHotData(position).getFeedUgcType()) {
//                //动态消息
//                if (1 == getHotData(position).getFeedType()) {
//                    //  原发动态消息
//                    return FEEDS_TYPE_ORIGIN_NEWS;
//                } else if (2 == getHotData(position).getFeedType()) {
//                    //  转发动态消息
//                    return FEEDS_TYPE_TRANSMIT_NEWS;
//                }
//            } else if (2 == getHotData(position).getFeedUgcType()) {
//                //转发活动消息
//                return FEEDS_TYPE_TRANSMIT_EVENTS;
//            }
//        }
//        return 0;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        if (holder instanceof HotRecommendHolder) {
//            HotRecommendHolder mRecommendHolder = (HotRecommendHolder) holder;
//            if (null != mHotFeedData && null != mHotFeedData.getRecommendUser()) {
//                mRecommendList.clear();
//                mRecommendList.addAll(mHotFeedData.getRecommendUser());
//                mRecommendHolder.setExpertData(mHotFeedData.getRecommendUser());
//            } else {
//                mRecommendHolder.setExpertData(mRecommendList);
//            }
//        } else {
//            FeedDto feedDto = getHotData(position);
//            // 用户信息
//            BaseViewHolder currentViewHolder = null;
//
//            if (holder instanceof HotEventsHolder) {
//                //活动
//                currentViewHolder = (BaseViewHolder) holder;
//                HotEventsHolder mEventsHolder = (HotEventsHolder) holder;
//                //活动信息
//                mEventsHolder.mEventTitle.setText(feedDto.getForwardFeedCard().getActivityTitle());
//                mEventsHolder.mEventCurrentFeedText.setText(feedDto.getForwardFeedCard().getActivityDesc());
//                mEventsHolder.mStartDate.setText("出发日期:" + DateUtils.formatDate(feedDto.getForwardFeedCard().getActivityBegintime()));
//                mEventsHolder.mEventDeadline.setText("报名截止:" + DateUtils.formatDate(feedDto.getForwardFeedCard().getActivityEnrollEndtime()));
//                mEventsHolder.mEventMaxMembers.setText("人数上限:" + feedDto.getForwardFeedCard().getActivityEnrollLimit());
//
//                //背景颜色
//                mEventsHolder.mEventCurrentFeedText.setBackgroundColor(mFragment.get().getContext().getResources().getColor(R.color.transmit_content_color));
//                mEventsHolder.mEventTransmittedText.setBackgroundColor(mFragment.get().getContext().getResources().getColor(R.color.transmit_content_color));
//                mEventsHolder.mEventInfoLayout.setBackgroundColor(mFragment.get().getContext().getResources().getColor(R.color.transmit_content_color));
//                mEventsHolder.mEventTitleInfoLayout.setBackgroundColor(mFragment.get().getContext().getResources().getColor(R.color.transmit_content_color));
//                mEventsHolder.mEventCoverLayout.setBackgroundColor(mFragment.get().getContext().getResources().getColor(R.color.transmit_content_color));
//
//                mEventsHolder.mEventOriginView.setVisibility(View.VISIBLE);
//                mEventsHolder.mEventTransmitView.setVisibility(View.VISIBLE);
//                mEventsHolder.mLocationLayout.setVisibility(View.VISIBLE);
//
//                // 活动封面
//                ImageView eventCover = new ImageView(mFragment.get().getContext());
//                eventCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT, Device.dip2px(167));
//                eventCover.setLayoutParams(params);
//                if (null != feedDto.getForwardFeedCard().getActivityCoverImage()) {
//                    Glide.with(mFragment.get().getContext()).load(feedDto.getForwardFeedCard().getActivityCoverImage().getResourceUrl()).into(eventCover);
//                }
//                mEventsHolder.mEventCoverLayout.removeAllViews();
//                mEventsHolder.mEventCoverLayout.addView(eventCover);
//
//                //转发活动位置
//                mEventsHolder.mLocationText.setText(feedDto.getForwardFeedCard().getActivityDeparturePlace());
//                mEventsHolder.mEventTitle.setText(feedDto.getForwardFeedCard().getActivityTitle());
//
//                if (null != feedDto.getFeedContents()) {
//                    List<FeedContentDto> feedContentDtos = feedDto.getFeedContents();
//                    StringBuilder sb = new StringBuilder();
//                    for (FeedContentDto contentDto : feedContentDtos) {
//                        if (null != contentDto) {
//                            if (null == contentDto.getUserId()) {
//                                //当前转发消息
//                                sb.append(contentDto.getFeedContent());
//                            } else {
//                                //拼接历史转发/原发消息
//                                sb.append("// @").append(contentDto.getNickName() + "：").append(contentDto.getFeedContent());
//                            }
//                        }
//                    }
//                    //设置 当前消息
//                    mEventsHolder.mEventCurrentFeedText.setText(generateSp(sb.toString()));
//                    mEventsHolder.mEventTransmittedText.setVisibility(View.VISIBLE);
//                    //转发消息
//                    String transmitOrigin = "@" + feedDto.getForwardFeedCard().getActivityUser().nickName
//                            + "：" + DateUtils.formatDate(feedDto.getPublishTime()) + " 发布";
//
//
//                    //活动状态
//                    if (mEventsHolder.verifyStatus(feedDto.getForwardFeedCard().getActivityEndtime())) {
//                        mEventsHolder.mEventStatus.setText("报名中");
//                        mEventsHolder.mEventStatus.setTextColor(mFragment.get().getContext().getResources().getColor(R.color.community_event_begin_status));
//                        mEventsHolder.mEventStatus.setBackground(mFragment.get().getContext().getResources().getDrawable(R.drawable.event_state_ongoing_border));
//                    } else {
//                        //结束
//                        mEventsHolder.mEventStatus.setText("报名截止");
//                        mEventsHolder.mEventStatus.setTextColor(mFragment.get().getContext().getResources().getColor(R.color.community_tab_txt_normal_color));
//                        mEventsHolder.mEventStatus.setBackground(mFragment.get().getContext().getResources().getDrawable(R.drawable.event_state_finished_border));
//                    }
//
//                    mEventsHolder.mEventTransmittedText.setText(generateSp(transmitOrigin));
//                    mEventsHolder.mEventCurrentFeedText.setMovementMethodDefault();
//                    mEventsHolder.mEventCurrentFeedText.setNeedForceEventToParent(true);
//
//                    mEventsHolder.mEventTransmittedText.setMovementMethodDefault();
//                    mEventsHolder.mEventTransmittedText.setNeedForceEventToParent(true);
//                }
//            } else if (holder instanceof HotNewsHolder) {
//                HotNewsHolder mNewsHolder = (HotNewsHolder) holder;
//
//                //动态
//                if (1 == feedDto.getFeedType()) {
//                    //原发动态
//                    currentViewHolder = mNewsHolder;
//                    mNewsHolder.mOriginView.setVisibility(View.VISIBLE);
//                    mNewsHolder.mTransmitView.setVisibility(View.GONE);
//                    mNewsHolder.mUserLocation.setText(feedDto.getFeedPlaceName());
//
//                    //动态图片
//                    List<String> imageList = new ArrayList<>();
//                    if (null != feedDto.getFeedImageList()) {
//                        for (ResourceFileDto imageFile : feedDto.getFeedImageList()) {
//                            imageList.add(imageFile.getResourceUrl());
//                        }
//                        mNewsHolder.mImageGridLayout.setUrlList(imageList);
//                        mNewsHolder.mImageGridLayout.setIsShowAll(true);
//                        mNewsHolder.mImageGridLayout.setSpacing(5);
//                    }
//
//                    //消息动态内容
//                    if (null != feedDto.getFeedContents() && feedDto.getFeedContents().size() > 0) {
//                        List<FeedContentDto> feedContentDtos = feedDto.getFeedContents();
//                        for (FeedContentDto contentDto : feedContentDtos) {
//                            if (null != contentDto) {
//                                mNewsHolder.mOriginFeedText.setText(contentDto.getFeedContent());
//                            }
//                        }
//                    } else {
//                        mNewsHolder.mOriginFeedText.setText("");
//
//                    }
//                } else if (2 == feedDto.getFeedType()) {
//                    //转发动态
//                    currentViewHolder = mNewsHolder;
//
//                    mNewsHolder.mTransmitView.setVisibility(View.VISIBLE);
//                    mNewsHolder.mOriginView.setVisibility(View.VISIBLE);
//                    mNewsHolder.mBottomExtraLayout.setBackgroundColor(mFragment.get().getContext().getResources().getColor(R.color.transmit_content_color));
//                    mNewsHolder.mImageGridLayout.setBackgroundColor(mFragment.get().getContext().getResources().getColor(R.color.transmit_content_color));
//                    mNewsHolder.mUserLocation.setText(feedDto.getFeedPlaceName());
//                    mNewsHolder.mBottomDivider.setVisibility(View.GONE);
//                    //动态图片
//                    List<String> imageList = new ArrayList<>();
//                    if (null != feedDto.getFeedImageList()) {
//                        for (ResourceFileDto imageFile : feedDto.getFeedImageList()) {
//                            imageList.add(imageFile.getResourceUrl());
//                        }
//                        mNewsHolder.mImageGridLayout.setUrlList(imageList);
//                        mNewsHolder.mImageGridLayout.setIsShowAll(true);
//                        mNewsHolder.mImageGridLayout.setSpacing(5);
//                    }
//
//
//                    if (null != feedDto.getFeedContents()) {
//                        List<FeedContentDto> feedContentDtos = feedDto.getFeedContents();
//                        StringBuilder sb = new StringBuilder();
//                        for (FeedContentDto contentDto : feedContentDtos) {
//                            if (null != contentDto) {
//                                if (null == contentDto.getUserId()) {
//                                    //当前转发消息
//                                    sb.append(contentDto.getFeedContent());
//                                } else {
//                                    //拼接历史转发/原发消息
//                                    sb.append("// @").append(contentDto.getNickName()).append(contentDto.getFeedContent());
//                                    sb.append(generateSp(sb.toString()));
//                                }
//                            }
//                        }
//                        //设置 当前消息
//                        mNewsHolder.mOriginFeedText.setText(sb.toString());
//                        mNewsHolder.mTransmitOriginFeedText.setVisibility(View.VISIBLE);
//                        //转发消息
//                        String transmitOrigin = "@" + feedDto.getForwardFeedCard().getFeedUser().nickName
//                                + "：" + feedDto.getForwardFeedCard().getFeedContents().get(0).getFeedContent();
//
//                        mNewsHolder.mTransmitOriginFeedText.setText(generateSp(transmitOrigin));
//                        mNewsHolder.mOriginFeedText.setMovementMethodDefault();
//                        mNewsHolder.mOriginFeedText.setNeedForceEventToParent(true);
//
//                        mNewsHolder.mTransmitOriginFeedText.setMovementMethodDefault();
//                        mNewsHolder.mTransmitOriginFeedText.setNeedForceEventToParent(true);
//                    }
//                }
//            }
//
//
//            //转评赞阅
//            currentViewHolder.mUserTransCount.setText(feedDto.getFeedStat().getForwardCnt() + "");
//            currentViewHolder.mUserCmtCount.setText(feedDto.getFeedStat().getCommentCnt() + "");
//            currentViewHolder.mUserReadCount.setText(feedDto.getFeedStat().getReadCnt() + "");
//            currentViewHolder.mUserLikeCount.setText(feedDto.getFeedStat().getPraiseCnt() + "");
//
//            //设置用户信息
//            currentViewHolder.mUserName.setText(feedDto.getFeedUser().nickName);
//            currentViewHolder.mUserPubDate.setText(DateUtils.convertTimeToFormat(feedDto.getPublishTime()));
//            currentViewHolder.mUserSign.setText(feedDto.getFeedUser().getUserSign());
////            if(0==feedDto.get){
////
////                mEventsHolder.mUserFollowStatus.setImageResource(R.drawable.follow);
////            }else{
////                mEventsHolder.mUserFollowStatus.setImageResource(R.drawable.followed);
////
////            }
//            if (1 == feedDto.getFeedUser().userAuthStatus) {
//                currentViewHolder.mUserAuthState.setVisibility(View.VISIBLE);
//            } else {
//                currentViewHolder.mUserAuthState.setVisibility(View.INVISIBLE);
//            }
//            Glide.with(mFragment.get().getContext()).load(feedDto.getFeedUser().userIcon.getResourceUrl()).into(currentViewHolder.mUserAvatar);
//            Glide.with(mFragment.get().getContext()).load(feedDto.getFeedUser().userMoodIcon).into(currentViewHolder.mUserMood);
//
//        }
//    }
//
//
//    public class HotRecommendHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.ugc_hot_title_recommend)
//        RecyclerView mRecommendRecycleView;
//
//        private HotExpertAdapter recommendExpertAdapter;
//
//
//        public HotRecommendHolder(@NonNull View itemView) {
//            super(itemView);
//
//            ButterKnife.bind(this, itemView);
//            buildRecommendLayout();
//        }
//
//        /**
//         * 推荐用户列表
//         */
//        private void buildRecommendLayout() {
//            recommendExpertAdapter = new HotExpertAdapter(mFragment.get().getContext());
//            mRecommendRecycleView.setAdapter(recommendExpertAdapter);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(mFragment.get().getContext());
//            layoutManager.setOrientation(RecyclerView.HORIZONTAL);
//            mRecommendRecycleView.setLayoutManager(layoutManager);
//        }
//
//        private void setExpertData(List<UserCardDto> recommendList) {
//            recommendExpertAdapter.setData(recommendList);
//        }
//    }
//
//
//    //    热门动态数据类型
//    public class HotNewsHolder extends BaseNewsViewHolder {
//
//        @BindView(R.id.ugc_feeds_current_txt)
//        public QMUISpanTouchFixTextView mOriginFeedText;
//
//        @BindView(R.id.ugc_feeds_transmitted_txt)
//        public QMUISpanTouchFixTextView mTransmitOriginFeedText;
//
//        @BindView(R.id.ugc_feeds_normal_layout)
//        public LinearLayout mNewsOrigin;
//        @BindView(R.id.ugc_feeds_transmit_layout)
//        public LinearLayout mNewsTransmit;
//
//        @BindView(R.id.feed_content_origin)
//        public View mOriginView;
//
//        @BindView(R.id.feed_content_transmit)
//        public View mTransmitView;
//
//        @BindView(R.id.feeds_pics)
//        ImageGridLayout mImageGridLayout;
//
//        @BindView(R.id.feeds_extra_info)
//        RelativeLayout mBottomExtraLayout;
//
//
//        @BindView(R.id.ugc_bottom_layout)
//        public View mFeedsBottomLayout;
//
//        public HotNewsHolder(@NonNull View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//            mTransmitOriginFeedText.setMovementMethodDefault();
//            mTransmitOriginFeedText.setNeedForceEventToParent(true);
//
//        }
//
//        @OnClick(R.id.feed_like_layout)
//        public void onFeedLike(View v) {
//            LikedPresenter presenter = new LikedPresenter(mFragment.get().getContext());
////            if (!getActivitys().isPraised()) {
//            FeedDto feedDto = getHotData(getAdapterPosition());
//            presenter.feedLike(feedDto.getFeedId() + "",
//                    feedDto.getFeedUser().userId + "");
//            mUserLikeCount.setText(Integer.parseInt(mUserLikeCount.getText().toString()) + 1 + "");
//            feedDto.setPraised(true);
////                presenter.activityUnLike(getActivitys().getActivityId() + "");
////                mUserLikeCount.setText(Integer.parseInt(mUserLikeCount.getText().toString())-1+"");
////            } else {
//
////            }
//        }
//
//        /**
//         * 点击跳转到动态详情
//         */
//        @OnClick({R.id.feed_content_origin, R.id.feed_content_transmit})
//        void onMsgInfoClick(View v) {
//            Intent i = new Intent();
//            int position = getAdapterPosition();
//            FeedDto feedDto = getHotData(position);
//            i.setClass(mFragment.get().getContext(), FeedsDetailActivity.class);
//            i.putExtra("feedId", feedDto.getFeedId());
//
//            mFragment.get().getContext().startActivity(i);
//        }
//
//
//        @Override
//        protected void onAvatarClick(View v) {
//            super.onAvatarClick(v);
//            Intent i = new Intent(mFragment.get().getActivity(), PersonHomeActivity.class);
//            FeedDto feedDto = getHotData(getAdapterPosition());
//
//            i.putExtra("user_id", feedDto.getFeedUser().userId);
//            mFragment.get().startActivity(i);
//        }
//
//        /**
//         * 点击弹框评论
//         */
//        @OnClick(R.id.feed_cmt_layout)
//        public void onCommentClick(View v) {
//            InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(mFragment.get().getContext(), R.style.dialog_center);
//            inputTextMsgDialog.setHint("写评论...");
//            inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
//                @Override
//                public void onTextSend(String msg) {
//                    //点击发送按钮后，回调此方法，msg为输入的值
//                    FeedDetailPresenter presenter = new FeedDetailPresenter(mFragment.get().getActivity());
//                    int position = getAdapterPosition();
//                    presenter.pubFeedComment(getHotData(position).getFeedId() + "", msg, "");
//
//                    int increaseCount = Integer.parseInt(mUserCmtCount.getText().toString()) + 1;
//                    mUserCmtCount.setText(+increaseCount + "");
//                }
//            });
//            inputTextMsgDialog.show();
//        }
//    }
//
//    //    热门活动数据类型
//    public class HotEventsHolder extends BaseTransmitEventViewHolder {
//
//        public HotEventsHolder(@NonNull View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//        }
//
//        @OnClick({R.id.ugc_feeds_normal_layout,
//                R.id.ugc_feeds_transmit_layout,
//                R.id.feed_event_info,
//                R.id.feed_event_covers,
//                R.id.feed_event_titleinfo})
//        void onMsgInfoClick(View v) {
//            Intent i = new Intent();
//            int position = getAdapterPosition();
//            FeedDto feedDto = getHotData(position);
//            i.setClass(mFragment.get().getContext(), FeedsDetailActivity.class);
//            i.putExtra("feedId", feedDto.getFeedId());
//            mFragment.get().getContext().startActivity(i);
//        }
//
//        @Override
//        protected void onAvatarClick(View v) {
//            super.onAvatarClick(v);
//            Intent i = new Intent(mFragment.get().getActivity(), PersonHomeActivity.class);
//            FeedDto feedDto = getHotData(getAdapterPosition());
//
//            i.putExtra("user_id", feedDto.getFeedUser().userId);
//            mFragment.get().startActivity(i);
//        }
//
//        @OnClick(R.id.feed_like_layout)
//        public void onFeedLike(View v) {
//            LikedPresenter presenter = new LikedPresenter(mFragment.get().getContext());
////            if (!getActivitys().isPraised()) {
//            FeedDto feedDto = getHotData(getAdapterPosition());
//            presenter.feedLike(feedDto.getFeedId() + "",
//                    feedDto.getFeedUser().userId + "");
//            mUserLikeCount.setText(Integer.parseInt(mUserLikeCount.getText().toString()) + 1 + "");
//            feedDto.setPraised(true);
////                presenter.activityUnLike(getActivitys().getActivityId() + "");
////                mUserLikeCount.setText(Integer.parseInt(mUserLikeCount.getText().toString())-1+"");
////            } else {
//
////            }
//        }
//
//        /**
//         * 点击弹框评论
//         */
//        @OnClick(R.id.feed_cmt_layout)
//        public void onCommentClick(View v) {
//            InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(mFragment.get().getContext(), R.style.dialog_center);
//            inputTextMsgDialog.setHint("写评论...");
//            inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
//                @Override
//                public void onTextSend(String msg) {
//                    //点击发送按钮后，回调此方法，msg为输入的值
//                    FeedDetailPresenter presenter = new FeedDetailPresenter(mFragment.get().getActivity());
//                    int position = getAdapterPosition();
//                    presenter.pubFeedComment(getHotData(position).getFeedId() + "", msg, "");
//
//                }
//            });
//            inputTextMsgDialog.show();
//        }
//
//    }
//
//    private SpannableString generateSp(String text) {
//        String highlightStart = "@";
//        String highlightEnd = "：";
//        SpannableString sp = new SpannableString(text);
//        int start = 0, end;
//        int index;
//        while ((index = text.indexOf(highlightStart, start)) > -1) {
//            end = text.indexOf(highlightEnd, start);
//            sp.setSpan(new QMUITouchableSpan(highlightTextNormalColor, highlightTextPressedColor,
//                    highlightBgNormalColor, highlightBgPressedColor) {
//                @Override
//                public void onSpanClick(View widget) {
//                    Toasty.info(mFragment.get().getContext(), "click ").show();
//                }
//            }, index, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//            start = end;
//        }
//        return sp;
//    }
}

