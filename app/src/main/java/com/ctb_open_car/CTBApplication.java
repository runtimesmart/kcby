package com.ctb_open_car;

import android.content.Context;

import com.ctb_open_car.base.CTBBaseApplication;
import com.ctb_open_car.constraints.AppContraint;
import com.ctb_open_car.utils.Density;
import com.rxretrofitlibrary.RxRetrofitApp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.zlw.main.recorderlib.RecordManager;

import timber.log.Timber;

public class CTBApplication extends CTBBaseApplication {
    private static CTBApplication mApplication;
    private static Context mContext;
    public static IWXAPI mWxApi;
    public RxRetrofitApp mRxRetrofitApp;


    {
//        PlatformConfig.setWeixin("wx3c0ccbcc34600c72", "2d0a0c06b0ed824e2d27bb96c08fede5");
//          PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
        //  PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        mContext = getApplicationContext();
        initWXAPI();
        initWBAPI();
        setLogAuto();
        initUMeng();
        Density.setDensity(this);
    }

    public static Context getContext() {
        return mContext;
    }

    public static CTBApplication getInstance() {
        return mApplication;
    }

    public RxRetrofitApp getRxApp() {
        return RxRetrofitApp.Singleton.INSTANCE.get();
    }

    public static void setLogAuto() {
        if (BuildConfig.DEBUG) {//debug版本
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void initUMeng() {
        //  UMConfigure.setLogEnabled(true);
        //初始化组件化基础库, 统计SDK/推送SDK/分享SDK都必须调用此初始化接口
//        UMConfigure.init(this, "5cadc5e63fc19595a90000b9", "Umeng",
//                UMConfigure.DEVICE_TYPE_PHONE,"");
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "5cadc5e63fc19595a90000b9");
        //统计SDK是否支持采集在子进程中打点的自定义事件，默认不支持
        UMConfigure.setProcessEvent(true);//支持多进程打点
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        PlatformConfig.setSinaWeibo(AppContraint.WeiBo.APP_KEY, AppContraint.WeiBo.APP_SECRET, "http://sns.whalecloud.com");
    }

    private void initWXAPI() {
        mWxApi = WXAPIFactory.createWXAPI(mContext,"wx3c0ccbcc34600c72", true);
        mWxApi.registerApp("wx3c0ccbcc34600c72");

    }


    private void initWBAPI() {
//        AuthInfo authInfo=new AuthInfo(this, AppContraint.WeiBo.APP_KEY,AppContraint.WeiBo.REDIRECT_URL,AppContraint.WeiBo.APP_KEY);
//        WbSdk.install(this,authInfo);
    }

}
