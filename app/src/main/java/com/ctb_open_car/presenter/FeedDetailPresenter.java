package com.ctb_open_car.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ctb_open_car.bean.community.ActivityDetailData;
import com.ctb_open_car.bean.community.FeedDetailData;
import com.ctb_open_car.bean.community.response.CommentData;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.FeedDetailApi;
import com.ctb_open_car.engine.net.api.FeedDetailCommentApi;
import com.ctb_open_car.engine.net.api.PubFeedCommentApi;
import com.ctb_open_car.ui.community.FeedsDetailView;
import com.ctb_open_car.view.activity.community.FeedsDetailActivity;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class FeedDetailPresenter {

    private SoftReference<Context> mContext;
    private String mFeedId;
    private FeedsDetailView mFeedsDetailView;

    public FeedDetailPresenter(Context detailActivity, FeedsDetailView feedsDetailView) {
        this.mContext = new SoftReference<>(detailActivity);
        this.mFeedsDetailView = feedsDetailView;
    }

    public FeedDetailPresenter(Context context) {
        mContext = new SoftReference<>(context);
    }

    /**
     * 请求动态详情接口
     */
    public void requestNewsDetail(String feedId) {
        FeedDetailApi newsDetailApi = new FeedDetailApi(listener, (RxAppCompatActivity) mContext.get());
        HashMap map = new HashMap();
        map.put("feedId", feedId);

        newsDetailApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(newsDetailApi);
    }

    /**
     * 发布评论接口
     */
    public void pubFeedComment(String feedId, String comment, String subCmtId) {
        mFeedId = feedId;
        PubFeedCommentApi feedCommentApi = new PubFeedCommentApi(pubCommentListener, (RxAppCompatActivity) mContext.get());
        HashMap map = new HashMap();
        map.put("feedId", feedId);
        map.put("commentContent", comment);
        if (!TextUtils.isEmpty(subCmtId)) {
            map.put("replyCommentId", subCmtId);
        }

        feedCommentApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(feedCommentApi);
    }

    /**
     * 请求动评论情接口
     */
    public void requestComment(String feedId, int pageNum) {
        if (1 == pageNum) {
            mFeedsDetailView.mCommentsAdapter.mCommentDtoList.clear();
        }
        FeedDetailCommentApi newsCommentApi = new FeedDetailCommentApi(listener, (RxAppCompatActivity) mContext.get());
        HashMap map = new HashMap();
        map.put("feedId", feedId);
        map.put("currentPage", pageNum + "");

        newsCommentApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(newsCommentApi);
    }

    /**
     * 评论成功请求回调
     */
    private HttpListener pubCommentListener = new HttpListener() {
        @Override
        public void onNext(Object o) {
            super.onNext(o);
            BaseResultEntity<Object> objDao = (BaseResultEntity<Object>) o;
            if (objDao.getRet().equals("0")) {
                if (null != mFeedsDetailView) {
                    requestComment(mFeedId, 1);
                    requestNewsDetail(mFeedId);
                }
                Toasty.info(mContext.get(), "评论成功").show();
            } else {
                Toasty.error(mContext.get(), "评论失败").show();

            }

        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(Log.getStackTraceString(e));
        }
    };

    /**
     * 请求回调
     */
    private HttpListener listener = new HttpListener() {
        @Override
        public void onNext(Object o) {
            super.onNext(o);
            BaseResultEntity<Object> objDao = (BaseResultEntity<Object>) o;

            if (null != objDao && null != objDao.getData()) {
                Object obj = objDao.getData();
                if (obj instanceof ActivityDetailData) {
                    //活动详情

                } else if (obj instanceof FeedDetailData) {
                    //动态详情
                    FeedDetailData feedDao = (FeedDetailData) obj;
                    mFeedsDetailView.updateDetailInfo(feedDao);
                } else if (obj instanceof CommentData) {
                    //评论列表
                    BaseResultEntity<CommentData> commentData = (BaseResultEntity<CommentData>) o;

                    if (null == commentData.getData() || null == commentData.getData().getPageData() || commentData.getData().getPageData().getData().size() == 0) {
                        mFeedsDetailView.loadCommentFailed();
                    } else {
                        mFeedsDetailView.updateCommentInfo(commentData.getData());
                    }

                }
            }

        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(Log.getStackTraceString(e));
        }
    };
}
