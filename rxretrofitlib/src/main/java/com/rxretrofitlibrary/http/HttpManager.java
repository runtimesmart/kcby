package com.rxretrofitlibrary.http;

import android.text.TextUtils;
import android.util.Log;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.rxretrofitlibrary.Api.BaseApi;
import com.rxretrofitlibrary.RxRetrofitApp;
import com.rxretrofitlibrary.exception.RetryWhenNetworkException;
import com.rxretrofitlibrary.http.cookie.CookieInterceptor;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.rxretrofitlibrary.subscribers.ProgressSubscriber;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * http交互处理类
 * Created by WZG on 2016/7/16.
 */
public class HttpManager {
    private volatile static HttpManager INSTANCE;
    private volatile static RxRetrofitApp sRxInstance;

    //构造方法私有
    private HttpManager() {
        sRxInstance = RxRetrofitApp.Singleton.INSTANCE.get();
    }

    //获取单例
    public static HttpManager getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 处理http请求
     *
     * @param basePar 封装的请求数据
     */
    public void doHttpDeal(final BaseApi basePar) {
        //手动创建一个OkHttpClient并设置超时时间缓存等设置
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(basePar.getConnectionTime(), TimeUnit.SECONDS);
        builder.readTimeout(basePar.getConnectionTime(), TimeUnit.SECONDS);
        builder.writeTimeout(basePar.getConnectionTime(), TimeUnit.SECONDS);
        builder.addInterceptor(new CookieInterceptor(basePar.isCache(), basePar.getUrl()));
        if (RxRetrofitApp.Singleton.INSTANCE.get().isDebug()) {
            builder.addInterceptor(getHttpLoggingInterceptor());
        }

        //添加自定义Header
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request origin = chain.request();

                Double longitude = sRxInstance.mHeadBean.getLongitude();
                Double latitude = sRxInstance.mHeadBean.getLatitude();

                String versionName = sRxInstance.mHeadBean.getVersionName();
                if (TextUtils.isEmpty(versionName) ||"null".equals(versionName)) {
                    versionName = "";
                }

                Request.Builder requesetBuild = origin.newBuilder()
                .addHeader("version_code", String.valueOf(sRxInstance.mHeadBean.getVersionCode()))
                .addHeader("version_name", versionName)
                .addHeader("app_type", String.valueOf(sRxInstance.mHeadBean.getAppType()))
                .addHeader("channel", sRxInstance.mHeadBean.getChannel())
                .addHeader("user_token", sRxInstance.mHeadBean.getUserToken())
                .addHeader("user_id", String.valueOf(sRxInstance.mHeadBean.getUserId()))
                .addHeader("longitude", null == longitude ? "" : longitude +"")
                .addHeader("latitude", null == latitude ? "" : latitude +"")
                .addHeader("Content-Type", "application/x-www-form-urlencoded");
//                Headers.Builder headsBuilder = new Headers.Builder();
//                requesetBuild.headers(buildHeaders(headsBuilder));
                return chain.proceed(requesetBuild.build());
            }
        });

        /*创建retrofit对象*/
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(basePar.getBaseUrl())
                .build();


        /*rx处理*/
        ProgressSubscriber subscriber = new ProgressSubscriber(basePar);
        Observable observable = basePar.getObservable(retrofit)
                /*失败后的retry配置*/
                .retryWhen(new RetryWhenNetworkException(basePar.getRetryCount(),
                        basePar.getRetryDelay(), basePar.getRetryIncreaseDelay()))
                /*生命周期管理*/
//                .compose(basePar.getRxAppCompatActivity().bindToLifecycle())

                /*http请求线程*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*结果判断*/
                .map(basePar);

        if (null != basePar.getRxAppCompatActivity()) {
            observable.compose(basePar.getRxAppCompatActivity().bindUntilEvent(ActivityEvent.PAUSE));
        }
        /*链接式对象返回*/
        SoftReference<HttpOnNextListener> httpOnNextListener = basePar.getListener();
        if (httpOnNextListener != null && httpOnNextListener.get() != null) {
            httpOnNextListener.get().onNext(observable);
        }

        /*数据回调*/
        observable.subscribe(subscriber);

    }

    //构造Header
    private Headers buildHeaders(Headers.Builder headsBuilder) {
        headsBuilder.add("Content-Type", "application/x-www-form-urlencoded");
//        headsBuilder.add("version_code", String.valueOf(sRxInstance.mHeadBean.getVersionCode()));
//        headsBuilder.add("version_name", sRxInstance.mHeadBean.getVersionName());
//        headsBuilder.add("app_type", String.valueOf(sRxInstance.mHeadBean.getAppType()));

        headsBuilder.add("version_code", "19090401");
        headsBuilder.add("version_name", "v1.0.0");
        headsBuilder.add("app_type", "2");
        headsBuilder.add("channel", "chetuobang");
        headsBuilder.add("user_id", "1536735");
//        headsBuilder.add("network", sRxInstance.mHeadBean.getNetwork());
//        headsBuilder.add("imei", sRxInstance.mHeadBean.getImei());
//        headsBuilder.add("imsi", sRxInstance.mHeadBean.getImsi());
//        headsBuilder.add("channel", sRxInstance.mHeadBean.getChannel());
//        headsBuilder.add("mac_adress", sRxInstance.mHeadBean.getMacAdress());
//        headsBuilder.add("os_name", sRxInstance.mHeadBean.getOsName());
//        headsBuilder.add("device_model", sRxInstance.mHeadBean.getDeviceModel());
//        headsBuilder.add("longitude", String.valueOf(sRxInstance.mHeadBean.getLongitude()));
//        headsBuilder.add("latitude", String.valueOf(sRxInstance.mHeadBean.getLatitude()));
//        headsBuilder.add("user_id", String.valueOf(sRxInstance.mHeadBean.getUserId()));
//        headsBuilder.add("user_token", sRxInstance.mHeadBean.getUserToken());
        return headsBuilder.build();
    }

    /**
     * 日志输出
     * 自行判定是否添加
     *
     * @return
     */
    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        //日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BASIC;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("RxRetrofit", "Retrofit====Message:" + message);
            }
        });
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }
}
