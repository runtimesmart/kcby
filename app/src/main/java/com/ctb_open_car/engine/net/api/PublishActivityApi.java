package com.ctb_open_car.engine.net.api;

import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.HttpRequestInterface;
import com.rxretrofitlibrary.Api.BaseApi;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Observable;

public class PublishActivityApi extends BaseApi {
    private HashMap<String, Object> mQueryMap = new HashMap<>();

    public PublishActivityApi(HttpListener listener, RxAppCompatActivity rxAppCompatActivity) {
        super(listener, rxAppCompatActivity);
        setCache(false);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpRequestInterface mRequestInterface = retrofit.create(HttpRequestInterface.class);
        return mRequestInterface.publishactivity(mQueryMap);
    }

    @Override
    public Object call(Object o) {
        return o;
    }

    public void setRequestBody(HashMap<String, Object> body) {
        this.mQueryMap = body;
    }
}
