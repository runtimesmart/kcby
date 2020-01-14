package com.ctb_open_car.engine.net.api;

import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.HttpRequestInterface;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Observable;

public class SearchUserApi extends BaseLikeApi {
    private HashMap mBody;

    public SearchUserApi(HttpListener listener, RxAppCompatActivity rxAppCompatActivity) {
        super(listener, rxAppCompatActivity);
        setCache(false);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpRequestInterface mRequestInterface = retrofit.create(HttpRequestInterface.class);
        return mRequestInterface.searchUser(mBody);
    }


    public void setRequestBody(HashMap mapBody) {
        this.mBody = mapBody;
    }


    @Override
    public Object call(Object o) {
        return o;
    }
}
