package com.ctb_open_car.engine.net.api;

import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.HttpRequestInterface;
import com.rxretrofitlibrary.Api.BaseApi;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Observable;

public class FeedLikedApi extends BaseLikeApi {
    private HashMap mBody;

    public FeedLikedApi(HttpListener listener, RxAppCompatActivity rxAppCompatActivity) {
        super(listener, rxAppCompatActivity);
        setCache(false);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpRequestInterface mRequestInterface = retrofit.create(HttpRequestInterface.class);
        if (null != mBody.get(ACTION_LIKE)) {
            mBody.remove(ACTION_LIKE);
            return mRequestInterface.feedLiked(mBody);
        } else if (null != mBody.get(ACTION_UNLIKE)) {
            mBody.remove(ACTION_UNLIKE);
            return mRequestInterface.feedCancelLiked(mBody);
        } else if (null != mBody.get(ACTION_CMT_LIKE)) {
            mBody.remove(ACTION_CMT_LIKE);
            return mRequestInterface.feedCommentLiked(mBody);
        } else if (null != mBody.get(ACTION_CMT_UNLIKE)) {
            mBody.remove(ACTION_CMT_UNLIKE);
            return mRequestInterface.feedCancelCommentLiked(mBody);
        }

        return mRequestInterface.feedLiked(mBody);
    }


    public void setRequestBody(HashMap mapBody) {
        this.mBody = mapBody;
    }


    @Override
    public Object call(Object o) {
        return o;
    }
}
