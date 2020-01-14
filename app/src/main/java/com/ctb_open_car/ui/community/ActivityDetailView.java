package com.ctb_open_car.ui.community;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctb_open_car.CTBApplication;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.community.ActivityDetailData;
import com.ctb_open_car.bean.community.response.CommentData;
import com.ctb_open_car.presenter.ActivityDetailPresenter;
import com.ctb_open_car.presenter.LikedPresenter;
import com.ctb_open_car.presenter.TransmitPresenter;
import com.ctb_open_car.ui.BaseView;
import com.ctb_open_car.ui.RecycleViewDivider;
import com.ctb_open_car.view.activity.community.ActivityDetailActivity;
import com.ctb_open_car.view.adapter.community.ActivityCommentsAdapter;
import com.library.BottomDialog;
import com.library.InputTextMsgDialog;
import com.library.Item;
import com.library.OnItemClickListener;
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

public class ActivityDetailView implements BaseView {

    public ActivityCommentsAdapter mCommentsAdapter;

    @BindView(R.id.ugc_content_refresh)
    SmartRefreshLayout mRefreshLayout;

    @BindView(R.id.comment_list)
    RecyclerView mCommentList;

    @BindView(R.id.topic_detail_share)
    TextView mTopicShare;

    @BindView(R.id.topic_detail_like)
    TextView mActivityDetailLikeCount;

    private ActivityDetailPresenter mActivityDetailPresenter;
    private final SoftReference<ActivityDetailActivity> mActivity;

    private int commentPageNum = 1;
    private String mActivityId;

    public ActivityDetailView(ActivityDetailActivity activity, String activityId) {
        this.mActivityId = activityId;
        mActivity = new SoftReference(activity);
        ButterKnife.bind(this, activity);

        initView();
    }


    private void initView() {
        setSmartRefreshLayout();
        buildHotContentView();
    }


    /**
     * 请求活动详情接口
     */
    private void initRequestDetail(String activityId) {
        mActivityDetailPresenter.requestActivityDetail(activityId);
    }


    /**
     * 请求活动评论列表
     */
    private void initRequestComment(String activityId, int commentPageNum) {
        mActivityDetailPresenter.requestComment(activityId, commentPageNum);
    }

    /**
     * 设置详情数据
     */
    public void updateDetailInfo(ActivityDetailData details) {
        mCommentsAdapter.setDetailData(details);
    }

    /**
     * 设置评论数据
     */
    public void updateCommentInfo(CommentData commentData) {
        mCommentsAdapter.setCommentData(commentData);
    }

    @Override
    public void setPresenter(Object presenter) {
        this.mActivityDetailPresenter = (ActivityDetailPresenter) presenter;

        //请求详情和评论数据
        initRequestDetail(mActivityId);
        initRequestComment(mActivityId, commentPageNum = 1);
    }

    @Override
    public void drawTitleBar() {

    }


    @OnClick({R.id.topic_detail_share, R.id.topic_detail_cmt, R.id.topic_detail_like})
    public void onShareClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.topic_detail_share:
//                new BottomDialog(mActivity.get())
//                        .orientation(BottomDialog.HORIZONTAL)
//                        .inflateMenu(R.menu.menu_share, new OnItemClickListener() {
//                            @Override
//                            public void click(Item item) {
//
//                            }
//                        })
//                        .inflateMenu(R.menu.menu_other, new OnItemClickListener() {
//                            @Override
//                            public void click(Item item) {
//
//                            }
//                        }).show();

                showDialog(v.getContext(), new InputTextMsgDialog.OnTextSendListener() {
                    @Override
                    public void onTextSend(String msg) {
                        String lng = CTBApplication.getInstance().getRxApp().mHeadBean.getLongitude() + "";
                        String lat = CTBApplication.getInstance().getRxApp().mHeadBean.getLatitude() + "";
                        TransmitPresenter mTransmitPresenter = new TransmitPresenter(mActivity.get());

                        mTransmitPresenter.transmitFeed(lng, lat, msg, "2", mActivityId);
                    }
                });

                break;
            case R.id.topic_detail_cmt:
                InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(mActivity.get(), R.style.dialog_center);
                inputTextMsgDialog.setHint("写评论...");
                inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                    @Override
                    public void onTextSend(String msg) {
                        //点击发送按钮后，回调此方法，msg为输入的值
                        mActivityDetailPresenter.pubFeedComment(mActivityId, msg, "");
                    }
                });
                inputTextMsgDialog.show();
                break;
            case R.id.topic_detail_like:
                LikedPresenter presenter = new LikedPresenter(mActivity.get());
                //未点赞的可以点赞
//                if (!mCommentsAdapter.getActivitys().isPraised()) {
                presenter.activityLike(mActivityId, mCommentsAdapter.getActivitys().getActivityUser().userId + "");
                mActivityDetailPresenter.requestActivityDetail(mActivityId);
//                }
                break;


        }
    }

    protected void showDialog(Context context, InputTextMsgDialog.OnTextSendListener listener) {
        InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(context, R.style.dialog_center);
        inputTextMsgDialog.setHint("写分享...");

        inputTextMsgDialog.setmOnTextSendListener(listener);
        inputTextMsgDialog.show();
    }

    public void loadCommentFailed() {
        commentPageNum--;
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
                //刷新
                initRequestDetail(mActivityId);
                initRequestComment(mActivityId, commentPageNum = 1);

                refreshlayout.finishRefresh(20/*,false*/);//传入false表示刷新失败
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(20/*,false*/);//传入false表示加载失败

                //刷新
                initRequestComment(mActivityId, ++commentPageNum);
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

        mCommentsAdapter = new ActivityCommentsAdapter(mActivity.get());
        mCommentList.setAdapter(mCommentsAdapter);

    }

    @Override
    public void unbind() {

    }
}
