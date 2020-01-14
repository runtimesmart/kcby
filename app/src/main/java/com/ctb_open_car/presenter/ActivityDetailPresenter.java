package com.ctb_open_car.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ctb_open_car.bean.community.ActivityDetailData;
import com.ctb_open_car.bean.community.response.CommentData;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.ActivityDetailApi;
import com.ctb_open_car.engine.net.api.ActivityDetailCommentApi;
import com.ctb_open_car.engine.net.api.PubActivityCommentApi;
import com.ctb_open_car.ui.community.ActivityDetailView;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;


import java.lang.ref.SoftReference;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class ActivityDetailPresenter {

    private SoftReference<Context> mContext;

    private ActivityDetailView mActivityDetailView;

    private String mActivityId;

    public ActivityDetailPresenter(Context context, ActivityDetailView activityDetailView) {
        this.mContext = new SoftReference<>(context);
        this.mActivityDetailView = activityDetailView;
    }

    public ActivityDetailPresenter(Context context) {
        this.mContext = new SoftReference<>(context);
    }

    /**
     * 请求活动详情接口
     */
    public void requestActivityDetail(String activityId) {
        ActivityDetailApi activityDetailApi = new ActivityDetailApi(listener, (RxAppCompatActivity) mContext.get());
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("activityId", activityId);

        activityDetailApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(activityDetailApi);
    }

    /**
     * 请求评论接口
     */
    public void requestComment(String activityId, int pageNum) {
        if (1 == pageNum) {
            mActivityDetailView.mCommentsAdapter.mCommonDtoList.clear();
        }
        ActivityDetailCommentApi activityCommentApi = new ActivityDetailCommentApi(listener, (RxAppCompatActivity) mContext.get());
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("activityId", activityId);
        map.put("currentPage", pageNum + "");

        activityCommentApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(activityCommentApi);
    }

    /**
     * 发布评论接口
     */
    public void pubFeedComment(String activityId, String comment, String subCmtId) {
        mActivityId = activityId;
        PubActivityCommentApi activityCommentApi = new PubActivityCommentApi(pubCommentListener, (RxAppCompatActivity) mContext.get());
        HashMap<String,String> map = new HashMap<String,String>();

        map.put("activityId", activityId);
        map.put("commentContent", comment);
        if (!TextUtils.isEmpty(subCmtId)) {
            map.put("replyCommentId", subCmtId);
        }

        activityCommentApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(activityCommentApi);
    }

    /**
     * 请求发布评论的回调
     */
    private HttpListener pubCommentListener = new HttpListener() {
        @Override
        public void onNext(Object o) {
            super.onNext(o);
            @SuppressWarnings("unchecked")
            BaseResultEntity<Object> objDao = (BaseResultEntity<Object>) o;
            //评论发布成功，刷新评论列表
            if (objDao.getRet().equals("0")) {
                if (null != mActivityDetailView) {
                    requestComment(mActivityId, 1);
                    requestActivityDetail(mActivityId);
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
     * 请求活动详情回调
     */
    private HttpListener listener = new HttpListener() {
        @Override
        public void onNext(Object o) {
            @SuppressWarnings("unchecked")
            BaseResultEntity<Object> objDao = (BaseResultEntity<Object>) o;
            if (!objDao.getRet().equals("0")) {
                Toasty.warning(mContext.get(), objDao.getMsg()).show();
            }
            if (null != objDao && null != objDao.getData()) {
                Object obj = objDao.getData();
                if (obj instanceof ActivityDetailData) {
                    ActivityDetailData activityDao = (ActivityDetailData) obj;
                    if (null != activityDao) {
                        mActivityDetailView.updateDetailInfo(activityDao);
                    }
                } else if (obj instanceof CommentData) {
                    CommentData commentData = (CommentData) obj;
                    if (null == commentData.getPageData() || commentData.getPageData().getData().size() == 0) {
                        mActivityDetailView.loadCommentFailed();
                    } else {
                        mActivityDetailView.updateCommentInfo(commentData);
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
