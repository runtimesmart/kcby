package com.net.rxretrofit.http;

import android.util.Log;

import com.net.rxretrofit.RxRetrofitApplication;
import com.net.rxretrofit.base.BaseApi;
import com.net.rxretrofit.exception.RetryWhenNetworkException;
import com.net.rxretrofit.gsonfactory.GsonDConverterFactory;
import com.net.rxretrofit.http.cookie.CookieInterceptor;
import com.net.rxretrofit.listener.HttpOnCompleteListener;
import com.net.rxretrofit.subscribers.ProgressSubscriber;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * http交互处理类
 */
public class HttpManager {
    private Gson gson;

    //构造方法私有
    private HttpManager() {
        gson = new GsonBuilder()
            .setLenient()// json宽松
            .enableComplexMapKeySerialization()//支持Map的key为复杂对象的形式
            .serializeNulls() //智能null
            .setPrettyPrinting()// 调教格式
            .disableHtmlEscaping() //默认是GSON把HTML 转义的
            .create();
    }

    private static class HttpManagerHolder {
        private static final HttpManager INSTANCE = new HttpManager();
    }

    public static HttpManager getInstance() {
        return HttpManagerHolder.INSTANCE;
    }

    /**
     *
     * @param baseWebApi 封装的请求数据
     */
    public Subscriber doHttpDeal(BaseApi baseWebApi) {
        //手动创建一个OkHttpClient并设置超时时间缓存等设置
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(baseWebApi.getConnectionTime(), TimeUnit.SECONDS);
        builder.readTimeout(baseWebApi.getReadTimeout(), TimeUnit.SECONDS);
        builder.writeTimeout(baseWebApi.getWriteTimeout(), TimeUnit.SECONDS);
        builder.addInterceptor(new CookieInterceptor(baseWebApi.isCache(), baseWebApi.getBaseUrl()));

//        if (baseWebApi.getUrl().contains("https")) {
//            //有证书验证，允许特定的host地址
//            builder.sslSocketFactory(HttpsFactory.getSSLSocketFactory(RxRetrofitApplication.getInstance().getApplication(), HttpsFactory.certificates));
//            builder.hostnameVerifier(HttpsFactory.getHostnameVerifier(HttpsFactory.hosts));
//        } else  {
//            //允许所有的HTTPS
//            builder.sslSocketFactory(HTTPSTrustManager.allowAllSSL());
//            builder.hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//        }

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        //.header("Content-Type", "application/x-www-form-urlencoded ");
                        .header("Version", RxRetrofitApplication.getInstance().getVersion())
                        .header("Imei", RxRetrofitApplication.getInstance().getImei())
                        .header("Authorization", "Bearer "+RxRetrofitApplication.getInstance().getAuthorization())
                        .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                Request request = requestBuilder.build();
                Response response= chain.proceed(request);
                return response;
            }
        });

        if (RxRetrofitApplication.getInstance().isDebug()) {
            builder.addInterceptor(getHttpLoggingInterceptor());
        }

        /*创建retrofit对象*/
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())

                .addConverterFactory(GsonDConverterFactory.create(gson))//自定义GsonDConverterFactory，当code！=100的时候返回格式不一样，导致toast不出错误信息
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseWebApi.getBaseUrl())
                .build();

        /*rx处理*/
        ProgressSubscriber subscriber = new ProgressSubscriber(baseWebApi);

        final Observable<Object> observable = baseWebApi.getObservable(retrofit).
                 /*失败后的retry配置*/
                retryWhen(new RetryWhenNetworkException(baseWebApi.getRetryCount(),
                        baseWebApi.getRetryDelay(), baseWebApi.getRetryIncreaseDelay()))
                /*生命周期管理*/
//                .compose(baseWebApi.getRxAppCompatActivity().bindUntilEvent(ActivityEvent.DESTROY))
                /*http请求线程*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*结果判断*/
                .map((Func1) baseWebApi);

        /*链接式对象返回*/
        final HttpOnCompleteListener httpOnNextListener = baseWebApi.getListener();
        if (httpOnNextListener != null) {
            httpOnNextListener.onNext(observable);

        }

/*数据回调*/
        observable.subscribe(subscriber);
        return subscriber;
    }

    /**
     *
     * @param baseWebApi 封装的请求数据
     */
    public Subscriber doHttpDealForApplication(BaseApi baseWebApi) {
        //手动创建一个OkHttpClient并设置超时时间缓存等设置
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.connectTimeout(baseWebApi.getConnectionTime(), TimeUnit.SECONDS);
        builder.readTimeout(baseWebApi.getReadTimeout(), TimeUnit.SECONDS);
        builder.writeTimeout(baseWebApi.getWriteTimeout(), TimeUnit.SECONDS);
        builder.addInterceptor(new CookieInterceptor(baseWebApi.isCache(), baseWebApi.getUrl()));

//        if (basePar.getUrl().equals(BaseNetUrl.API_DEBUG_URL_PROTOCOL_HOSTNAME_PORT)) {
//            //有证书验证，允许特定的host地址
//            builder.sslSocketFactory(HttpsFactory.getSSLSocketFactory(RxRetrofitApplication.getInstance().getApplication(), HttpsFactory.certificates));
//            builder.hostnameVerifier(HttpsFactory.getHostnameVerifier(HttpsFactory.hosts));
//        } else  {
//            //允许所有的HTTPS
//            builder.sslSocketFactory(HTTPSTrustManager.allowAllSSL());
//            builder.hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//        }

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Version", RxRetrofitApplication.getInstance().getVersion())
                        .header("Imei", RxRetrofitApplication.getInstance().getImei())
                        .header("Authorization",RxRetrofitApplication.getInstance().getAuthorization())
                        .header("Content-Type", "application/x-www-form-urlencoded");
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        if (RxRetrofitApplication.getInstance().isDebug()) {
            builder.addInterceptor(getHttpLoggingInterceptor());
        }

        /*创建retrofit对象*/
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonDConverterFactory.create(gson))//自定义GsonDConverterFactory，当code！=100的时候返回格式不一样，导致toast不出错误信息
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseWebApi.getBaseUrl())
                .build();

        /*rx处理*/
        ProgressSubscriber subscriber = new ProgressSubscriber(baseWebApi);
        Observable observable = baseWebApi.getObservable(retrofit).
                 /*失败后的retry配置*/
                 retryWhen(new RetryWhenNetworkException(baseWebApi.getRetryCount(),
                            baseWebApi.getRetryDelay(), baseWebApi.getRetryIncreaseDelay()))
                /*http请求线程*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*结果判断*/
                .map((Func1) baseWebApi);

        /*链接式对象返回*/
        HttpOnCompleteListener httpOnNextListener = baseWebApi.getListener();
        if (httpOnNextListener != null) {
            httpOnNextListener.onNext(observable);
        }

        /*数据回调*/
        observable.subscribe(subscriber);

        return subscriber;
    }

    /**
     * 日志输出
     * 自行判定是否添加
     *
     * @return
     */
    public HttpLoggingInterceptor getHttpLoggingInterceptor() {
        //日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("http", "message:" + message);
            }
        });
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }
}
