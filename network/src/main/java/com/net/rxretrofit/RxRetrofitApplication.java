package com.net.rxretrofit;

import android.app.Application;

import com.net.rxretrofit.interfaces.LangConfigInterface;
import com.net.rxretrofit.interfaces.ResponseInterceptorInterface;

/**
 * 全局app
 */

public class RxRetrofitApplication extends Application {
    private Application application;
    private static boolean debug;

    private  String  version;
    private  String  imei;

    private  String  authorization;
    private static LangConfigInterface langConfig;
    private static ResponseInterceptorInterface reponseInterceptor;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }


    public RxRetrofitApplication(){

    }
    private static class RxRetrofitApplicationHolder {
        private static final RxRetrofitApplication INSTANCE = new RxRetrofitApplication();
    }

    public static RxRetrofitApplication getInstance() {
        return RxRetrofitApplication.RxRetrofitApplicationHolder.INSTANCE;
    }

    public void init(Application app, boolean debug,String version,String imei,String authorization, LangConfigInterface langConfigInterface, ResponseInterceptorInterface interceptor) {
        setDebug(debug);
        langConfig = langConfigInterface;
        reponseInterceptor = interceptor;
        setVersion(version);
        setImei(imei);
        setAuthorization(authorization);
    }


    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        RxRetrofitApplication.debug = debug;
    }

    public LangConfigInterface getLangConfigInstance() {
        return langConfig;
    }

    public ResponseInterceptorInterface getReponseInterceptor() {
        return reponseInterceptor;
    }
}
