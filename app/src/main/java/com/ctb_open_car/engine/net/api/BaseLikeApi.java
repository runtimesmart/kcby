package com.ctb_open_car.engine.net.api;

import com.ctb_open_car.engine.HttpListener;
import com.rxretrofitlibrary.Api.BaseApi;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import retrofit2.Retrofit;
import rx.Observable;

public class BaseLikeApi extends BaseApi {
    public static final int ACTION_LIKE = 1;
    public static final int ACTION_UNLIKE = 2;
    public static final int ACTION_CMT_LIKE = 3;
    public static final int ACTION_CMT_UNLIKE = 4;

    public BaseLikeApi(HttpListener listener, RxAppCompatActivity rxAppCompatActivity) {
        super(listener, rxAppCompatActivity);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        return null;
    }

    @Override
    public Object call(Object o) {
        return null;
    }
}
