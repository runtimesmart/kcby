package com.ctb_open_car.engine.net.api;


import com.ctb_open_car.engine.HttpListener;
import com.rxretrofitlibrary.Api.BaseApi;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import retrofit2.Retrofit;
import rx.Observable;

public class HotNewsApi extends BaseApi {

    public HotNewsApi(HttpListener listener, RxAppCompatActivity rxAppCompatActivity) {
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
