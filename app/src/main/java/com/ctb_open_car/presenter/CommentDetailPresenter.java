package com.ctb_open_car.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.ctb_open_car.bean.community.ActivityDetailData;
import com.ctb_open_car.bean.community.FeedDetailData;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.ActivityDetailCommentApi;
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

import timber.log.Timber;

public class CommentDetailPresenter {

    private SoftReference<FeedsDetailActivity> mDetailActivity;

    private FeedsDetailView mFeedsDetailView;

    public CommentDetailPresenter(FeedsDetailActivity detailActivity, FeedsDetailView feedsDetailView) {
        this.mDetailActivity = new SoftReference<>(detailActivity);
        this.mFeedsDetailView = feedsDetailView;
    }


    /**
     * 请求动态详情接口
     */
    public void requestNewsComment(String feedId) {
        FeedDetailCommentApi newsCommentApi = new FeedDetailCommentApi(listener, (RxAppCompatActivity) mDetailActivity.get());
        HashMap map = new HashMap();
        map.put("feedId", feedId);

        newsCommentApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(newsCommentApi);
    }


    /**
     * 请求活动详情接口
     */
    public void requestActivityComment(String activityId) {
        ActivityDetailCommentApi activityCommentApi = new ActivityDetailCommentApi(listener, (RxAppCompatActivity) mDetailActivity.get());
        HashMap map = new HashMap();
        map.put("activityId", activityId);

        activityCommentApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(activityCommentApi);
    }

    /**
     * 发布评论接口
     */
    public void pubFeedComment(String feedId, String comment, String subCmtId) {
        PubFeedCommentApi feedCommentApi = new PubFeedCommentApi(listener, (RxAppCompatActivity) mDetailActivity.get());
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
                }
            }
            //评论列表

        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(Log.getStackTraceString(e));
        }
    };

}
