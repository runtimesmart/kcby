package com.ctb_open_car.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctb_open_car.R;
import com.ctb_open_car.di.component.ActivityComponent;
import com.ctb_open_car.di.component.DaggerActivityComponent;
import com.ctb_open_car.di.module.ActivityModule;
import com.ctb_open_car.utils.StatusBarUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseActivity extends RxAppCompatActivity {
    private boolean isSplashActivity = false;
    protected ImageView mBtnBack;
    protected TextView mTitleName;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getSupportActionBar()) {
            getSupportActionBar().hide();
            StatusBarUtils.setWindowStatusBarColor(this, android.R.color.transparent);
        }

    }

    protected ActivityComponent getActivityComponent() {
        return DaggerActivityComponent.builder()
                .applicationComponent(CTBBaseApplication.getInstance().getComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }


    protected void initLayout() {
        mBtnBack = findViewById(R.id.btn_back);
        if (null != mBtnBack) {
            mTitleName = findViewById(R.id.title_name);
            mBtnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity.this.finish();
                }
            });
        }
    }

    protected void initGPS() {
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则跳转至设置开启界面，设置完毕后返回到当前页面
        if (!locationManager
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder da = new AlertDialog.Builder(this);
            da.setTitle("提示：");
            da.setMessage("为了更好的为您服务，请您打开您的GPS!");
            da.setCancelable(false);
            //设置左边按钮监听
            da.setNeutralButton("确定",
                    new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            // 转到手机设置界面，用户设置GPS
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, 0); // 设置完成后返回到原来的界面

                        }
                    });
            //设置右边按钮监听
            da.setPositiveButton("取消",
                    new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            arg0.dismiss();
                        }
                    });
            da.show();
        } else {
        }
    }

    protected void setTitletName(String title) {
        if (null != mTitleName) {
            mTitleName.setText(title);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart((String) getTag()); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd((String) getTag()); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为
        // onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);

    }

    public abstract Object getTag();

    public void setSplashActivity() {
        isSplashActivity = true;
    }
}
