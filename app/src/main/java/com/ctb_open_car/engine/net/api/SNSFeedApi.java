package com.ctb_open_car.engine.net.api;

import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.HttpRequestInterface;
import com.rxretrofitlibrary.Api.BaseApi;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Observable;

public class SNSFeedApi extends BaseApi {
    private HashMap<String, String> mQueryMap = new HashMap<>();

    public SNSFeedApi(HttpListener listener, RxAppCompatActivity rxAppCompatActivity) {
        super(listener, rxAppCompatActivity);
    }

    @Override
    public Object call(Object o) {
        return o;
    }

    public void setRequestBody(HashMap<String, String> body) {
        this.mQueryMap = body;
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpRequestInterface httpInterface = retrofit.create(HttpRequestInterface.class);
        return  httpInterface.getMapSNS(mQueryMap);
    }
}
