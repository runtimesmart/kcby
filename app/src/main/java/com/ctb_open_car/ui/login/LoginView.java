package com.ctb_open_car.ui.login;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.ctb_open_car.R;
import com.ctb_open_car.presenter.LoginPresenter;
import com.ctb_open_car.view.activity.activities.PushActivitiesActivity;
import com.ctb_open_car.view.activity.login.LoginActivity;
import com.rxretrofitlibrary.RxRetrofitApp;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.SoftReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

public class LoginView{
    @BindView(R.id.tv_app_about)
    TextView mTvAppabout;
    @BindView(R.id.edit_photo)
    EditText mEditMobile;
    @BindView(R.id.edit_verification_code)
    EditText mEditVerifCode;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.other_login)
    View mOtherLoginView;

    private final SoftReference<LoginActivity> mActivity;
    private Unbinder unbinder;
    private LoginPresenter mLoginPresenter;
    private int mLoginType = 1; // 1-手机号登录；2-微信登录
    private RxRetrofitApp sRxInstance;
    private long mUserId;

    @OnClick({R.id.login_wechat, R.id.btn_login, R.id.bt_verification_code})
    public void onClick(View view) {
        String mobile;
        switch (view.getId()) {
            case R.id.login_wechat:
                //待添加功能
                mLoginPresenter.regToWx();
                break;
            case R.id.btn_login:
                mobile = mEditMobile.getText().toString();
                String verifCode = mEditVerifCode.getText().toString();
                if (!TextUtils.isEmpty(mobile) && !TextUtils.isEmpty(verifCode)) {
                    if (mLoginType == 1) {
                        mLoginPresenter.requestLogin(mobile, verifCode);
                    } else {
                        mLoginPresenter.bindMoblie(mUserId, mobile, verifCode);
                    }
                } else {
                    Toasty.info(mActivity.get(), mActivity.get().getString(R.string.mobile_verrf_code_empty)).show();
                }
                break;
            case R.id.bt_verification_code:
                mobile = mEditMobile.getText().toString();
                if (!TextUtils.isEmpty(mobile)) {
                    mLoginPresenter.requestVerificationCode(mobile, 1);
                } else {
                    Toasty.info(mActivity.get(), "请输入手机号").show();
                }
                break;
            default:
                break;
        }
    }

    public LoginView(final LoginActivity activity) {
        mActivity = new SoftReference<>(activity);
        unbinder = ButterKnife.bind(this, mActivity.get());
        sRxInstance = RxRetrofitApp.Singleton.INSTANCE.get();
        mTvAppabout.setText(String.format("%s %s", mActivity.get().getString(R.string.app_name_en), sRxInstance.mHeadBean.getVersionName()));
    }

    public void setLoginType(int loginType, long userId) {
        mUserId = userId;
        mLoginType = loginType;
        mOtherLoginView.setVisibility(View.GONE);
        mBtnLogin.setText( mActivity.get().getString(R.string.bind_mobile));
    }
    public void setLoginPresenter(LoginPresenter loginPresenter) {
        mLoginPresenter = loginPresenter;
    }
}
