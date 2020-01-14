package com.ctb_open_car.ui.community;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctb_open_car.CTBApplication;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.community.FeedDetailData;
import com.ctb_open_car.bean.community.response.CommentData;
import com.ctb_open_car.presenter.CommentDetailPresenter;
import com.ctb_open_car.presenter.FeedDetailPresenter;
import com.ctb_open_car.presenter.LikedPresenter;
import com.ctb_open_car.presenter.TransmitPresenter;
import com.ctb_open_car.ui.BaseView;
import com.ctb_open_car.ui.RecycleViewDivider;
import com.ctb_open_car.view.activity.community.FeedsDetailActivity;
import com.ctb_open_car.view.adapter.community.FeedCommentsAdapter;
import com.library.InputTextMsgDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.lang.ref.SoftReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedsDetailView implements BaseView {

    public FeedCommentsAdapter mCommentsAdapter;

    @BindView(R.id.ugc_content_refresh)
    SmartRefreshLayout mRefreshLayout;

    @BindView(R.id.comment_list)
    RecyclerView mCommentList;

    @BindView(R.id.topic_detail_share)
    TextView mTopicShare;

    //    private FeedDto mFeedDto;
    //    private ActivityCardDto mActivityfeedDto;
    private FeedDetailPresenter mFeedDetailPresenter;
    private final SoftReference<FeedsDetailActivity> mActivity;

    private int commentPageNum = 1;
    private String mFeedId;

    public FeedsDetailView(FeedsDetailActivity activity, String feedId) {
        mActivity = new SoftReference(activity);
        ButterKnife.bind(this, activity);
        mFeedId = feedId;
        initView();
    }

    private void initView() {
        setSmartRefreshLayout();
        buildHotContentView();
    }

    /**
     * 根据feed类型，请求详情接口
     */
    private void initRequestDetail(String feedId) {
        mFeedDetailPresenter.requestNewsDetail(feedId);
    }


    /**
     * 根据feed类型，请求详情接口
     */
    private void initRequestComment(String feedId, int commentPageNum) {
        mFeedDetailPresenter.requestComment(feedId, commentPageNum);
    }

    /**
     * 设置详情数据
     */
    public void updateDetailInfo(FeedDetailData details) {
        mCommentsAdapter.setDetailData(details);
    }

    /**
     * 设置评论数据
     */
    public void updateCommentInfo(CommentData commentData) {
        mCommentsAdapter.setCommentData(commentData);
    }

    public void loadCommentFailed() {
        commentPageNum--;
    }

    @Override
    public void setPresenter(Object presenter) {
        this.mFeedDetailPresenter = (FeedDetailPresenter) presenter;

        //初始化数据
        initRequestDetail(mFeedId);
        //初始化评论数据
        initRequestComment(mFeedId, commentPageNum = 1);
    }

    @Override
    public void drawTitleBar() {

    }

    @OnClick({R.id.topic_detail_share, R.id.topic_detail_cmt, R.id.topic_detail_like})
    public void onShareClick(View v) {
        int id = v.getId();
        InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(mActivity.get(), R.style.dialog_center);
        switch (id) {
            case R.id.topic_detail_share:
                inputTextMsgDialog.setHint("写分享...");

                inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                    @Override
                    public void onTextSend(String msg) {
                        //点击发送按钮后，回调此方法，msg为输入的值
//                        mFeedDetailPresenter.pubFeedComment(mFeedId,msg,"");
                        TransmitPresenter presenter = new TransmitPresenter(mActivity.get());
                        presenter.transmitFeed(CTBApplication.getInstance().getRxApp().mHeadBean.getLongitude() + ""
                                , CTBApplication.getInstance().getRxApp().mHeadBean.getLatitude() + "", msg, "1", mFeedId);
                    }
                });
                inputTextMsgDialog.show();
                break;
            case R.id.topic_detail_cmt:
                inputTextMsgDialog.setHint("写评论...");

                inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                    @Override
                    public void onTextSend(String msg) {
                        //点击发送按钮后，回调此方法，msg为输入的值
                        mFeedDetailPresenter.pubFeedComment(mFeedId, msg, "");
                    }
                });
                inputTextMsgDialog.show();
                break;
            case R.id.topic_detail_like:
                LikedPresenter presenter = new LikedPresenter(mActivity.get());
                //未点赞的可以点赞
//                if (!mCommentsAdapter.getActivitys().isPraised()) {
                presenter.feedLike(mFeedId, mCommentsAdapter.getFeeds().getFeedUser().userId + "");
                mFeedDetailPresenter.requestNewsDetail(mFeedId);
//                }
                break;
        }
    }


    /**
     * 设置上下拉样式
     */
    private void setSmartRefreshLayout() {

        mRefreshLayout.setRefreshHeader(new ClassicsHeader(mActivity.get()));
        //设置 Footer 为 球脉冲 样式
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(mActivity.get()).setSpinnerStyle(SpinnerStyle.Translate));
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(20/*,false*/);//传入false表示刷新失败

                initRequestDetail(mFeedId);
                initRequestComment(mFeedId, commentPageNum = 1);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                initRequestDetail(mFeedId);
                initRequestComment(mFeedId, ++commentPageNum);

                refreshlayout.finishLoadMore(20/*,false*/);//传入false表示加载失败
            }
        });
    }

    /**
     * 构造详情页面，详情+评论放在 comment的recyclerView中
     */
    private void buildHotContentView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity.get());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mCommentList.setLayoutManager(layoutManager);
//        mHotContentRecycleView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        mCommentList.addItemDecoration(new RecycleViewDivider(mActivity.get(), DividerItemDecoration.VERTICAL, Color.parseColor("#F5F5F5"), 50));

        mCommentsAdapter = new FeedCommentsAdapter(mActivity.get());
        mCommentList.setAdapter(mCommentsAdapter);

    }

    @Override
    public void unbind() {

    }
}
