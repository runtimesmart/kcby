package com.ctb_open_car.engine.net.api;

import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.HttpRequestInterface;
import com.rxretrofitlibrary.Api.BaseApi;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;

public class TopicListApi extends BaseApi {

    private HashMap mBody;

    public TopicListApi(HttpListener listener, RxAppCompatActivity rxAppCompatActivity) {
        super(listener, rxAppCompatActivity);
        setCache(false);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpRequestInterface requestInterface=retrofit.create(HttpRequestInterface.class);
        return requestInterface.getTopitList(mBody);
    }

    public void setBody(HashMap body){
        this.mBody=body;
    }
    @Override
    public Object call(Object o) {
        return null;
    }
}
