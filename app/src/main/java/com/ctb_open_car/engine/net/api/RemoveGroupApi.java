package com.ctb_open_car.engine.net.api;

import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.HttpRequestInterface;
import com.rxretrofitlibrary.Api.BaseApi;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Observable;

public class RemoveGroupApi extends BaseApi {
    private HashMap mBody;

    public RemoveGroupApi(HttpListener listener, RxAppCompatActivity rxAppCompatActivity) {
        super(listener, rxAppCompatActivity);
        setCache(false);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpRequestInterface mRequestInterface = retrofit.create(HttpRequestInterface.class);
        return mRequestInterface.removeMyGroup(mBody);
    }


    public void setRequestBody(HashMap mapBody) {
        this.mBody = mapBody;
    }


    @Override
    public Object call(Object o) {
        return o;
    }
}