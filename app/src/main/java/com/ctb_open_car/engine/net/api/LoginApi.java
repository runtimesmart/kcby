package com.ctb_open_car.engine.net.api;

import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.HttpRequestInterface;
import com.rxretrofitlibrary.Api.BaseApi;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Observable;

public class LoginApi extends BaseApi {
    private HashMap<String, Object> mQueryMap = new HashMap<>();
    private int mType = 0;
    public LoginApi(HttpListener listener, RxAppCompatActivity rxAppCompatActivity, int type) {
        super(listener, rxAppCompatActivity);
        mType = type;
        setCache(false);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpRequestInterface mRequestInterface = retrofit.create(HttpRequestInterface.class);
        switch (mType) {
            case 1:
                return mRequestInterface.loginPost(mQueryMap);
            case 2:
                return mRequestInterface.mobileBindPost(mQueryMap);
            case 3:
                return mRequestInterface.loginWeiXinPost(mQueryMap);
            default:
                 return null;
        }
    }

    @Override
    public Object call(Object o) {
        return o;
    }

    public void setRequestBody(HashMap<String, Object> body) {
        this.mQueryMap = body;
    }
}
