package com.ctb_open_car.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;

import com.ctb_open_car.CTBApplication;
import com.ctb_open_car.MainActivity;
import com.ctb_open_car.di.component.ApplicationComponent;
import com.ctb_open_car.di.component.DaggerApplicationComponent;
import com.ctb_open_car.di.module.ApplicationModule;
import com.ctb_open_car.utils.PreferenceUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.rxretrofitlibrary.BuildConfig;
import com.rxretrofitlibrary.RxRetrofitApp;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;


public class CTBBaseApplication extends Application {

    /**
     * 主线程ID
     */
    private int mMainThreadId = -1;
    /**
     * 主线程Looper
     */
    private Looper mMainLooper;
    /**
     * 记录所有活动的Activity
     */
    private List<BaseActivity> mActivities = new LinkedList<>();

    // 0-在线
    private int HUD_STATUS = -1;

    public IWXAPI wxapi;


    @Override
    public void onCreate() {
        super.onCreate();

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
// 如果APP启用了远程的service，此application:onCreate会被调用2次
// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null || !processAppName.equalsIgnoreCase(getPackageName())) {
            Timber.d("enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        mMainThreadId = android.os.Process.myTid();
        mMainLooper = getMainLooper();
        //初始化网络请求框架

        //初始化Fresco框架

        Logger.addLogAdapter(new AndroidLogAdapter());

        registerLiveListener();

        //初始化Retrofit模块
        RxRetrofitApp.Singleton.INSTANCE.get().init(this, BuildConfig.DEBUG);


        //输出log到文件
        //CrashHandler crashHandler = CrashHandler.getInstance();
        //crashHandler.init(getApplicationContext());
        //UmengUtil.getInstance().initUmeng();
        //initUrl();
        initWeiXin();

        initEMClient();

    }

    public void initLogin() {
        String emId = PreferenceUtils.getString(CTBApplication.getInstance(), "em_id");
        String emPass = PreferenceUtils.getString(CTBApplication.getInstance(), "em_pass");
        if (TextUtils.isEmpty(emId) || TextUtils.isEmpty(emPass)) {
            Timber.e("环信账号或密码为空");
//            Toasty.warning(this, "环信账号或密码为空").show();
            return;
        }

        EMClient.getInstance().login(emId, emPass, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                try {
                    EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                EMClient.getInstance().chatManager().loadAllConversations();
                Timber.d("登录聊天服务器成功！");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Timber.d("登录聊天服务器失败！");
            }
        });
    }

    private void initEMClient() {
        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
// 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoTransferMessageAttachments(true);
// 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true);
//初始化
        EMClient.getInstance().init(this, options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源

        EMClient.getInstance().setDebugMode(true);
    }


    ApplicationComponent mApplicationComponent;

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }

    private void initWeiXin() {

    }


    private static class CTBApplicationHolder {
        private static final CTBBaseApplication INSTANCE = new CTBBaseApplication();
    }

    public static CTBBaseApplication getInstance() {
        return CTBBaseApplication.CTBApplicationHolder.INSTANCE;
    }


    public List<BaseActivity> getAllActivities() {
        return mActivities;
    }

    /**
     * 关闭所有Activity
     */
    public void finishAll() {
        List<BaseActivity> copy;
        synchronized (mActivities) {
            copy = new ArrayList<>(mActivities);
        }
        for (BaseActivity activity : copy) {
            activity.finish();
        }
    }

    /**
     * 退出应用
     */
    public void exitApp() {
        finishAll();
    }

    public long getMainThreadId() {
        return mMainThreadId;
    }

    /**
     * 获取主线程的looper
     */

    public Looper getMainThreadLooper() {
        return mMainLooper;
    }

    /**
     * 关闭
     */
    public static void stopService() {

    }

    public void stopTransfer() {

    }

    //横竖屏切换需要重置语言，不然出现中文
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private SoftReference<BaseActivity> currentActivity;
    private int mFinalCount;

    private void registerLiveListener() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                mFinalCount++;
                if (mFinalCount == 1) {
                    //前台
                    Logger.d(" front desk");
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (activity instanceof BaseActivity) {
                    currentActivity = new SoftReference<>((BaseActivity) activity);
                }

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                mFinalCount--;
                if (mFinalCount == 0) {
                    //后台
                    Logger.e("background");
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public BaseActivity getCurrentActivity() {
        if (null == currentActivity) {
            return null;
        }
        return currentActivity.get();
    }

    public boolean applicationIsFront() {
        return mFinalCount == 1;
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}
