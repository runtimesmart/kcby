package com.ctb_open_car.presenter;


import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ctb_open_car.CTBApplication;
import com.ctb_open_car.base.CTBBaseApplication;
import com.ctb_open_car.bean.login.UserBean;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.LoginApi;
import com.ctb_open_car.engine.net.api.SendValidateCodeApi;
import com.ctb_open_car.ui.login.LoginView;
import com.ctb_open_car.utils.Device;
import com.ctb_open_car.utils.PreferenceUtils;
import com.ctb_open_car.view.activity.login.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.RxRetrofitApp;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;


public class LoginPresenter {
    private SoftReference<LoginActivity> mContext;
    private IWXAPI api;
    private LoginView mLoginView;
    private RxRetrofitApp mRxInstance;
    private long mUserId = -1L;

    public LoginPresenter(LoginActivity c, LoginView v) {
        mLoginView = v;
        mContext = new SoftReference<>(c);
        mRxInstance = RxRetrofitApp.Singleton.INSTANCE.get();
    }

    /**
     * 请求验证码接口
     */
    public void requestVerificationCode(String mobile, int source) {
        mContext.get().showDialog("正在获取验证码");
        SendValidateCodeApi newsDetailApi = new SendValidateCodeApi(listener, (RxAppCompatActivity) mContext.get());
        HashMap map = new HashMap();
        map.put("mobile", mobile);
        map.put("source", source);

        newsDetailApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(newsDetailApi);
    }

    /**
     * 请求登录接口
     */
    public void requestLogin(String mobile, String validateCode) {
        mContext.get().showDialog("正在登录...");
        LoginApi newsDetailApi = new LoginApi(logiinListener, (RxAppCompatActivity) mContext.get(), 1);
        HashMap map = new HashMap();
        map.put("mobile", mobile);
        map.put("validateCode", validateCode);
        newsDetailApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(newsDetailApi);
    }

    /**
     * 微信登录成功，绑定手机号
     */
    public void bindMoblie(long userId, String mobile, String validateCode) {
        mContext.get().showDialog("正在登录...");
        mUserId = userId;
        LoginApi newsDetailApi = new LoginApi(bindMobileListener, (RxAppCompatActivity) mContext.get(), 2);
        HashMap map = new HashMap();
        map.put("userId", userId);
        map.put("mobile", mobile);
        map.put("validateCode", validateCode);
        newsDetailApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(newsDetailApi);
    }

    /**
     * 验证码请求回调
     */
    private HttpListener listener = new HttpListener() {
        @Override
        public void onNext(Object object) {
            JsonObject jsonObject = (JsonObject) object;
            String code = jsonObject.get("code").toString();
            String message = jsonObject.get("msg").toString();
            int codeIndex = Integer.parseInt(code.replace("\"", ""));
            if (codeIndex == 0) {
                mContext.get().showToast("验证码已经发送到你的手机");
            } else {
                mContext.get().showToast(message);
            }
            mContext.get().dismissDiaLog(0);
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(Log.getStackTraceString(e));
            mContext.get().dismissDiaLog(0);
        }
    };

    /**
     * 登录请求回调
     */
    private HttpListener logiinListener = new HttpListener() {
        @Override
        public void onNext(Object o) {
            BaseResultEntity<UserBean> objDao = (BaseResultEntity<UserBean>) o;
            if (objDao.getRet().equals("0")) {
                mContext.get().showToast("登录成功");
                UserBean userBean = objDao.getData();

                PreferenceUtils.putString(CTBApplication.getInstance(), "nickName", userBean.getUser().getNickName());
                PreferenceUtils.putInt(CTBApplication.getInstance(), "userAuthStatus", userBean.getUser().getUserAuthStatus());
                PreferenceUtils.putInt(CTBApplication.getInstance(), "userSex", userBean.getUser().getUserSex());
                PreferenceUtils.putLong(CTBApplication.getInstance(), "userId", userBean.getUser().getUserId());
                PreferenceUtils.putString(CTBApplication.getInstance(), "user_token", userBean.getUserToken());
                PreferenceUtils.putString(CTBApplication.getInstance(), "em_id", userBean.getUser().getEmchatBind().getEmId());
                PreferenceUtils.putString(CTBApplication.getInstance(), "em_pass", userBean.getUser().getEmchatBind().getEmPwd());
                CTBBaseApplication.getInstance().initLogin();

                mRxInstance.mHeadBean.setUserId(userBean.getUser().getUserId());
                mRxInstance.mHeadBean.setLatitude(mContext.get().getLatLng().latitude);
                mRxInstance.mHeadBean.setLongitude(mContext.get().getLatLng().longitude);
                mRxInstance.mHeadBean.setAppType(2);
                mRxInstance.mHeadBean.setChannel(Device.getChannel());
                mRxInstance.mHeadBean.setImei(Device.getIMEI());
                mRxInstance.mHeadBean.setVersionCode(Device.getAppVersionCode());
                mRxInstance.mHeadBean.setVersionName(Device.getAppVersionName());
                mRxInstance.mHeadBean.setUserToken(userBean.getUserToken());

                mContext.get().setLoginStatus(true);

                mContext.get().dismissDiaLog(1);

            } else {
                mContext.get().showToast(objDao.getMsg());
                mContext.get().dismissDiaLog(0);
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(Log.getStackTraceString(e));
            mContext.get().dismissDiaLog(0);
        }
    };

    public void regToWx() {
       // UMShareAPI.get(mContext.get()).getPlatformInfo(mContext.get() , SHARE_MEDIA.WEIXIN, authListener);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_ctb";
        CTBApplication.mWxApi.sendReq(req);
    }

    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(mContext.get(), "成功了", Toast.LENGTH_LONG).show();

        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {

            Toast.makeText(mContext.get(), "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
          //  Toast.makeText(mContext, "取消了", Toast.LENGTH_LONG).show();
        }
    };

    /**
     * 微信授权后请求登录接口
     */
    public void requestWeiXinLogin(String validateCode) {
        mContext.get().showDialog("正在登录...");
        LoginApi newsDetailApi = new LoginApi(weixinListener, mContext.get() ,3);
        HashMap map = new HashMap();
        map.put("code", validateCode);
        newsDetailApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(newsDetailApi);
    }
    /**
     * 验证码请求回调
     */
    private HttpListener weixinListener = new HttpListener() {
        @Override
        public void onNext(Object object) {
            super.onNext(object);
            JsonObject jsonObject = (JsonObject) object;

            int code = jsonObject.get("code").getAsInt();
            if (code == 0) {
                JsonObject jsData = jsonObject.get("data").getAsJsonObject();
                boolean bindMobile = jsData.get("bindMobile").getAsBoolean();
                JsonObject jsUser = jsData.get("user").getAsJsonObject();
                long userID = jsUser.get("userId").getAsLong();

                mRxInstance.mHeadBean.setAppType(2);

                mContext.get().setLoginStatus(true);

                if(!bindMobile) {
                    mContext.get().setLoginType(2);
                    mLoginView.setLoginType(2, userID);
                    mContext.get().dismissDiaLog(0);
                } else {
                    JsonObject emchatBind = jsUser.get("emchatBind").getAsJsonObject();
                    String emID = emchatBind.get("emId").getAsString();
                    String emPwd = emchatBind.get("emPwd").getAsString();

                    PreferenceUtils.putString(CTBApplication.getInstance(), "em_id", emID);
                    PreferenceUtils.putString(CTBApplication.getInstance(), "em_pass", emPwd);
                    String userToken = jsData.get("userToken").getAsString();
                    PreferenceUtils.putString(CTBApplication.getInstance(),"user_token",userToken);
                    mRxInstance.mHeadBean.setUserToken(userToken);
                    PreferenceUtils.putLong(CTBApplication.getInstance(),"userId",userID);
                    mRxInstance.mHeadBean.setUserId(userID);
                    mContext.get().dismissDiaLog(1);
                }
            } else {
                mContext.get().dismissDiaLog(0);
            }

        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(Log.getStackTraceString(e));
            mContext.get().dismissDiaLog(0);
        }
    };

    /**
     * 绑定手机号请求回调
     */
    private HttpListener bindMobileListener = new HttpListener() {
        @Override
        public void onNext(Object object) {
            JsonObject jsonObject = (JsonObject) object;
            String code = jsonObject.get("code").toString();
            String message = jsonObject.get("msg").toString();

            int codeIndex = Integer.parseInt(code.replace("\"", ""));
            if (codeIndex == 0) {
                JsonObject jsData = jsonObject.get("data").getAsJsonObject();
                String userToken = jsData.get("userToken").toString();
                PreferenceUtils.putString(CTBApplication.getInstance(), "user_token", userToken);
                mRxInstance.mHeadBean.setUserToken(userToken);
                PreferenceUtils.putLong(CTBApplication.getInstance(), "userId", mUserId);
                mRxInstance.mHeadBean.setUserId(mUserId);
                mContext.get().dismissDiaLog(1);
            } else {
                mContext.get().showToast(message);
                mContext.get().dismissDiaLog(0);
            }

        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(Log.getStackTraceString(e));
            mContext.get().dismissDiaLog(0);
        }
    };
}
