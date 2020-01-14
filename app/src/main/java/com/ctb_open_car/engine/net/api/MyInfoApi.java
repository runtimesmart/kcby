package com.ctb_open_car.engine.net.api;

import android.widget.Switch;

import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.HttpRequestInterface;
import com.rxretrofitlibrary.Api.BaseApi;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Observable;

public class MyInfoApi extends BaseApi {
    private HashMap<String, Object> mQueryMap = new HashMap<>();
    private int mType = 0;
    public MyInfoApi(HttpListener listener, RxAppCompatActivity rxAppCompatActivity, int type) {
        super(listener, rxAppCompatActivity);
        mType = type;
        setCache(false);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpRequestInterface mRequestInterface = retrofit.create(HttpRequestInterface.class);

        switch (mType) {
            case 1:
                return mRequestInterface.signOut();
            case 2:
                return mRequestInterface.addCarInfo(mQueryMap);
            case 3:
                return mRequestInterface.getCarList();
            case 4:
                return mRequestInterface.deleteCarInfo(mQueryMap);
            default:
               return mRequestInterface.getMyInifo(mQueryMap);
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
