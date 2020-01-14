package com.ctb_open_car.presenter;

import android.content.Context;
import android.util.Log;

import com.ctb_open_car.bean.community.response.HotFeedData;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.ActivityLikedApi;
import com.ctb_open_car.engine.net.api.BaseLikeApi;
import com.ctb_open_car.engine.net.api.FeedLikedApi;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class LikedPresenter {

    private SoftReference<Context> mContext;

    public LikedPresenter(Context context) {
        this.mContext = new SoftReference<>(context);
    }

    /**
     * 动态点赞
     */
    public void feedLike(String feedId, String targetOnwerId) {

        FeedLikedApi feedLikedApi = new FeedLikedApi(listener, (RxAppCompatActivity) mContext.get());
        HashMap map = new HashMap();
        map.put(BaseLikeApi.ACTION_LIKE, feedId);

        map.put("feedId", feedId);
        map.put("targetOnwerId", targetOnwerId);

        feedLikedApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(feedLikedApi);
    }

    /**
     * 取消动态点赞
     */
    public void feedUnLike(String feedId) {

        FeedLikedApi feedLikedApi = new FeedLikedApi(listener, (RxAppCompatActivity) mContext.get());
        HashMap map = new HashMap();
        map.put(BaseLikeApi.ACTION_UNLIKE, feedId);
        map.put("feedId", feedId);

        feedLikedApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(feedLikedApi);
    }

    /**
     * 动态点赞
     */
    public void feedCommentLike(String feedId, String commentId, String targetOnwerId) {

        FeedLikedApi feedLikedApi = new FeedLikedApi(listener, (RxAppCompatActivity) mContext.get());
        HashMap map = new HashMap();
        map.put(BaseLikeApi.ACTION_CMT_LIKE, feedId);

        map.put("feedId", feedId);
        map.put("commentId", commentId);
        map.put("targetOnwerId", targetOnwerId);

        feedLikedApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(feedLikedApi);
    }

    /**
     * 动态评论取消点赞
     */
    public void feedCommentUnLike(String commentId) {

        FeedLikedApi feedLikedApi = new FeedLikedApi(listener, (RxAppCompatActivity) mContext.get());
        HashMap map = new HashMap();
        map.put(BaseLikeApi.ACTION_CMT_UNLIKE, commentId);
        map.put("commentId", commentId);

        feedLikedApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(feedLikedApi);
    }

    /**
     * 活动点赞
     */
    public void activityLike(String activityId, String targetOnwerId) {

        ActivityLikedApi activityLikedApi = new ActivityLikedApi(listener, (RxAppCompatActivity) mContext.get());
        HashMap map = new HashMap();
        map.put(BaseLikeApi.ACTION_LIKE, activityId);
        map.put("activityId", activityId);
        map.put("targetOnwerId", targetOnwerId);

        activityLikedApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(activityLikedApi);
    }

    /**
     * 活动取消点赞
     */
    public void activityUnLike(String activityId) {

        ActivityLikedApi activityLikedApi = new ActivityLikedApi(listener, (RxAppCompatActivity) mContext.get());
        HashMap map = new HashMap();

        map.put(BaseLikeApi.ACTION_UNLIKE, activityId);
        map.put("activityId", activityId);

        activityLikedApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(activityLikedApi);
    }

    /**
     * 活动评论点赞
     */
    public void activityCommentLike(String activityId, String commentId, String targetOnwerId) {

        ActivityLikedApi activityLikedApi = new ActivityLikedApi(listener, (RxAppCompatActivity) mContext.get());
        HashMap map = new HashMap();
        map.put(BaseLikeApi.ACTION_CMT_LIKE, activityId);

        map.put("activityId", activityId);
        map.put("commentId", commentId);
        map.put("targetOnwerId", targetOnwerId);

        activityLikedApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(activityLikedApi);
    }

    /**
     * 活动评论取消点赞
     */
    public void activityCommentUnLike(String commentId) {

        ActivityLikedApi activityLikedApi = new ActivityLikedApi(listener, (RxAppCompatActivity) mContext.get());
        HashMap map = new HashMap();
        map.put(BaseLikeApi.ACTION_CMT_UNLIKE, commentId);
        map.put("commentId", commentId);

        activityLikedApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(activityLikedApi);
    }


    private HttpListener listener = new HttpListener() {
        @Override
        public void onNext(Object o) {
            super.onNext(o);
            BaseResultEntity<Object> objDao = (BaseResultEntity<Object>) o;
            if ("0".equals(objDao.getRet())) {
                Toasty.info(mContext.get(), "点赞成功").show();
            } else {
                Toasty.error(mContext.get(), "点赞失败").show();
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(Log.getStackTraceString(e));
            Toasty.error(mContext.get(), "点赞失败").show();
        }
    };
}
