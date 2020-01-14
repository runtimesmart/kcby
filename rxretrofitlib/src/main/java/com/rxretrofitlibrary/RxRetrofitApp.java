package com.rxretrofitlibrary;

import android.app.Application;
import android.util.Log;

import com.rxretrofitlibrary.http.HeaderBean;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * 全局app
 * Created by WZG on 2016/12/12.
 */

public class RxRetrofitApp {

    public HeaderBean mHeadBean;
    private Application application;
    private boolean debug;


    public enum Singleton {
        INSTANCE;
        private RxRetrofitApp mRxInstance;

        private Singleton() {
            mRxInstance = new RxRetrofitApp();
        }

        public RxRetrofitApp get() {
            return mRxInstance;
        }
    }

    public void initHeaderParas(HeaderBean headerBean) {
        this.mHeadBean = headerBean;
    }

    public void init(Application app) {
        setApplication(app);
        initHeaderParas(new HeaderBean());
        setDebug(true);
    }

    public void init(Application app, boolean debug) {
        setApplication(app);
        initHeaderParas(new HeaderBean());

        setDebug(debug);
    }

    public Application getApplication() {
        return application;
    }

    private void setApplication(Application application) {
        this.application = application;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {this.debug = debug;
    }
}
