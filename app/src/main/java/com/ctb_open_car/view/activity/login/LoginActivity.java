package com.ctb_open_car.view.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.amap.api.maps.model.LatLng;
import com.ctb_open_car.MainActivity;
import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseActivity;
import com.ctb_open_car.eventbus.MessageEvent;
import com.ctb_open_car.presenter.LoginPresenter;
import com.ctb_open_car.ui.login.LoginView;
import com.ctb_open_car.view.dialog.RoundProgressDialog;
import com.rxretrofitlibrary.RxRetrofitApp;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import es.dmoral.toasty.Toasty;


public class LoginActivity extends RxAppCompatActivity {
    private static final String TAG = "LoginActivity";
    private LoginView mView;
    private LoginPresenter mLoginPresenter;
    private RoundProgressDialog mRoundProgressDialog;
    private LatLng mLatLng;
    private boolean isLoginSuccess = false;
    private boolean isWebView = false; //是否为webView启动的
    private int mLoginType = 1; // 1-手机号登录；2-微信登录

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLatLng = getIntent().getParcelableExtra("LatLng");
        isWebView = getIntent().getBooleanExtra("webView", false);
        mView = new LoginView(this);
        mLoginPresenter = new LoginPresenter(this, mView);
        mView.setLoginPresenter(mLoginPresenter);

        EventBus.getDefault().register(this);

    }

    public Object getTag() {
        return TAG;
    }

    public void setLoginType(int loginType) {
        mLoginType = loginType;
    }

    public LatLng getLatLng() {
        return mLatLng;
    }
    public void showToast(String tips) {
        Toasty.info(this, tips).show();
    }

    public void showDialog(String tips) {
        mRoundProgressDialog = RoundProgressDialog.newInstance(tips);
        mRoundProgressDialog.show(getSupportFragmentManager(),"dialog");
    }

    public void dismissDiaLog (int type){
        if (mRoundProgressDialog != null) {
            mRoundProgressDialog.dismiss();
        }

        if (type == 1) {
            if (isWebView) {
                MessageEvent messageEvent = new MessageEvent();
                messageEvent.setType("update_data_shop");
                EventBus.getDefault().post(messageEvent);
            } else {
                MessageEvent messageEvent = new MessageEvent();
                messageEvent.setType("update_data");
                EventBus.getDefault().post(messageEvent);
            }

            isLoginSuccess = true;
            Intent intent = new Intent();
            intent.putExtra("loginSuccess", isLoginSuccess);
            setResult(1001, intent);
            finish();
        }
    }

    public void setLoginStatus(boolean isLoginSuccess) {
        this.isLoginSuccess = isLoginSuccess;
    }

    @Override
    public void onPause() {
        super.onPause();
        //关闭软键盘
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
            if (!isLoginSuccess) {
                MessageEvent messageEvent = new MessageEvent();
                messageEvent.setType("default_radioGroup_index");
                EventBus.getDefault().post(messageEvent);
            }
            super.onBackPressed();

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getEventBusMsg(MessageEvent event) {

        switch (event.getType()) {
            case "WXAPI_Resp_Success":
                String code = (String)event.getObject();
                mLoginPresenter.requestWeiXinLogin(code);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
