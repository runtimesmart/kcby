package com.net.rxretrofit.base;

import com.net.rxretrofit.RxRetrofitApplication;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.net.rxretrofit.exception.HttpTimeException;
import com.net.rxretrofit.listener.HttpOnCompleteListener;

import java.lang.ref.SoftReference;

import retrofit2.Retrofit;
import rx.Observable;
import rx.functions.Func1;

/**
 * 请求数据统一封装类
 */
public abstract class BaseObjApi<T> implements Func1<BaseResultEntity<T>, T>, BaseApi {
    //rx生命周期管理
    private SoftReference<RxAppCompatActivity> rxAppCompatActivity;
    /*回调*/
    private HttpOnCompleteListener listener;
    /*是否能取消加载框*/
    private boolean cancel;
    /*是否显示加载框*/
    private boolean showProgress;
    /*是否需要缓存处理*/
    private boolean cache;
    /*基础url*/
    private String baseUrl = BaseNetUrl.getInstance().getGateApiUrl();

    private String url;

    /*方法-如果需要缓存必须设置这个参数；不需要不用設置*/
    private String method = "";
    /*超时时间-默认6秒*/
    private int connectionTime = 6;
    /*超时时间-默认6秒*/
    private int readTime = 6;
    /*超时时间-默认6秒*/
    private int writeTime = 6;

    /*有网情况下的本地缓存时间默认60秒*/
    private int cookieNetWorkTime = 60;
    /*无网络的情况下本地缓存时间默认30天*/
    private int cookieNoNetWorkTime = 24 * 60 * 60 * 30;
    /* 失败后retry次数*/
    private int retryCount = 1;
    /*失败后retry延迟*/
    private long retryDelay = 100;
    /*失败后retry叠加延迟*/
    private long retryIncreaseDelay = 10;
    /*缓存url-可手动设置*/
    private String cacheUrl;

    public BaseObjApi(HttpOnCompleteListener listener, RxAppCompatActivity rxAppCompatActivity) {
        setListener(listener);
        setRxAppCompatActivity(rxAppCompatActivity);
        setShowProgress(false);
        setCache(true);
    }

    /**
     * 设置参数
     *
     * @param retrofit
     * @return
     */
    public abstract Observable getObservable(Retrofit retrofit);

    public int getCookieNoNetWorkTime() {
        return cookieNoNetWorkTime;
    }

    public void setCookieNoNetWorkTime(int cookieNoNetWorkTime) {
        this.cookieNoNetWorkTime = cookieNoNetWorkTime;
    }

    public int getCookieNetWorkTime() {
        return cookieNetWorkTime;
    }

    public void setCookieNetWorkTime(int cookieNetWorkTime) {
        this.cookieNetWorkTime = cookieNetWorkTime;
    }

    public int getConnectionTime() {
        return connectionTime;
    }

    public void setConnectionTime(int connectionTime) {
        this.connectionTime = connectionTime;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getUrl() {
        /*在没有手动设置url情况下，简单拼接*/
        if (null == getCacheUrl() || "".equals(getCacheUrl())) {
            return getBaseUrl() + getMethod();
        }
        return url;
    }

    public void setRxAppCompatActivity(RxAppCompatActivity rxAppCompatActivity) {
        this.rxAppCompatActivity = new SoftReference(rxAppCompatActivity);
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public HttpOnCompleteListener getListener() {
        return listener;
    }

    public void setListener(HttpOnCompleteListener listener) {
        this.listener = listener;
    }


    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public long getRetryDelay() {
        return retryDelay;
    }

    public void setRetryDelay(long retryDelay) {
        this.retryDelay = retryDelay;
    }

    public long getRetryIncreaseDelay() {
        return retryIncreaseDelay;
    }

    public void setRetryIncreaseDelay(long retryIncreaseDelay) {
        this.retryIncreaseDelay = retryIncreaseDelay;
    }

    /*
     * 获取当前rx生命周期
     * @return
     */
    public RxAppCompatActivity getRxAppCompatActivity() {
        return rxAppCompatActivity.get();
    }

    @Override
    public T call(BaseResultEntity<T> httpResult) {
        if (httpResult.getCode() != 0) {
            throw new HttpTimeException(httpResult);
        }
        return httpResult.getData();
    }

    public String getCacheUrl() {
        return cacheUrl;
    }

    public void setCacheUrl(String cacheUrl) {
        this.cacheUrl = cacheUrl;
    }

    public int getReadTimeout() {
        return readTime;
    }

    public void setReadTimeout(int readTime) {
        this.readTime = readTime;
    }

    public int getWriteTimeout() {
        return writeTime;
    }

    public void setWriteTimeout(int writeTime) {
        this.writeTime = writeTime;
    }
}
