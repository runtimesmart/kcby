package com.ctb_open_car.utils;

import com.ctb_open_car.base.CTBBaseApplication;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengCallback;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import java.util.HashMap;

public class UmengUtil {
    private final static UmengUtil INSTANCE = new UmengUtil();
    private final static String TEST_SECRET = "";
    private final static String SECRET = "";

    public static int initState = 0;//注册标识，与网络无关，一般无需判断不会失败
    public static int enableState = 0;
    public static int disableState = 0;
    public static HashMap<String, String> addAlias = new HashMap<>();
    public static HashMap<String, String> removeAlias = new HashMap<>();

    private static PushAgent mPushAgent;

    public static UmengUtil getInstance() {
        return INSTANCE;
    }

    public void initUmeng() {
        UMConfigure.setLogEnabled(true);
        UMConfigure.init(CTBBaseApplication.getInstance(), UMConfigure.DEVICE_TYPE_PHONE, TEST_SECRET);
        if (mPushAgent == null) {
            mPushAgent = PushAgent.getInstance(CTBBaseApplication.getInstance());
        }
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                initState = 1;
            }

            @Override
            public void onFailure(String s, String s1) {
                initState = -1;
            }
        });

        mPushAgent.onAppStart();

        //mPushAgent.setPushIntentServiceClass(PushIntentService.class);
    }

    public void enable() {
        if (mPushAgent == null) {
            mPushAgent = PushAgent.getInstance(CTBBaseApplication.getInstance());
        }
        mPushAgent.enable(new IUmengCallback() {

            @Override
            public void onSuccess() {
                enableState = 1;
                disableState = 0;
            }

            @Override
            public void onFailure(String s, String s1) {
                enableState = -1;
                disableState = 0;
            }
        });
    }

    public void disable() {
        if (mPushAgent == null) {
            mPushAgent = PushAgent.getInstance(CTBBaseApplication.getInstance());
        }
        mPushAgent.disable(new IUmengCallback() {
            @Override
            public void onSuccess() {
                disableState = 1;
                enableState = 0;
            }

            @Override
            public void onFailure(String s, String s1) {
                disableState = -1;
                enableState = 0;
            }
        });
    }

    public void updateAdd(String params, String paramsId, boolean state) {
        if (state) {
            if (addAlias.containsKey(paramsId) && params.equals(addAlias.get(paramsId))) {
                addAlias.remove(paramsId);
            }
        } else {
            if (!addAlias.containsKey(paramsId) || !params.equals(addAlias.get(paramsId))) {
                addAlias.put(paramsId, params);
            }
        }
    }


    public void updateDelete(String params, String paramsId, boolean state) {
        if (state) {
            if (removeAlias.containsKey(paramsId) && params.equals(removeAlias.get(paramsId))) {
                removeAlias.remove(paramsId);
            }
        } else {
            if (!removeAlias.containsKey(paramsId) || !removeAlias.equals(addAlias.get(paramsId))) {
                removeAlias.put(paramsId, params);
            }
        }
    }

}
