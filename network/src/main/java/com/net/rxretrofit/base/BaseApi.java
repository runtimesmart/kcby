package com.net.rxretrofit.base;

import com.net.rxretrofit.listener.HttpOnCompleteListener;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import retrofit2.Retrofit;
import rx.Observable;

public interface BaseApi {
    String getBaseUrl();

    boolean isCache();

    String getUrl();

    int getConnectionTime();

    Observable getObservable(Retrofit retrofit);

    int getRetryCount();

    long getRetryDelay();

    long getRetryIncreaseDelay();

    RxAppCompatActivity getRxAppCompatActivity();

    HttpOnCompleteListener getListener();

    boolean isShowProgress();

    boolean isCancel();

    int getCookieNetWorkTime();

    int getCookieNoNetWorkTime();

    int getReadTimeout();

    int getWriteTimeout();
}
