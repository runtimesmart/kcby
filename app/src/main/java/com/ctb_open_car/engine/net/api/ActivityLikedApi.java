package com.ctb_open_car.engine.net.api;

import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.HttpRequestInterface;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Observable;

public class ActivityLikedApi extends BaseLikeApi {
    private HashMap mBody;

    public ActivityLikedApi(HttpListener listener, RxAppCompatActivity rxAppCompatActivity) {
        super(listener, rxAppCompatActivity);
        setCache(false);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpRequestInterface mRequestInterface = retrofit.create(HttpRequestInterface.class);
        if (null != mBody.get(ACTION_LIKE)) {
            mBody.remove(ACTION_LIKE);
            return mRequestInterface.activitytLiked(mBody);
        } else if (null != mBody.get(ACTION_UNLIKE)) {
            mBody.remove(ACTION_UNLIKE);
            return mRequestInterface.activitytCancelLiked(mBody);
        } else if (null != mBody.get(ACTION_CMT_LIKE)) {
            mBody.remove(ACTION_CMT_LIKE);
            return mRequestInterface.activitytCommentLiked(mBody);
        } else if (null != mBody.get(ACTION_CMT_UNLIKE)) {
            mBody.remove(ACTION_CMT_UNLIKE);
            return mRequestInterface.activitytCancelCommentLiked(mBody);
        }

        return null;
    }


    public void setRequestBody(HashMap mapBody) {
        this.mBody = mapBody;
    }


    @Override
    public Object call(Object o) {
        return o;
    }
}
