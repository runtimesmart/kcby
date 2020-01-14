package com.ctb_open_car.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import com.ctb_open_car.di.component.DaggerFragmentComponent;
import com.ctb_open_car.di.component.FragmentComponent;
import com.ctb_open_car.di.module.FragmentModule;
import com.rxretrofitlibrary.RxRetrofitApp;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseFragment extends Fragment {
    public RxRetrofitApp mRxInstance;
    protected String mLoadId;

    private String pageTittle;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRxInstance = RxRetrofitApp.Singleton.INSTANCE.get();
    }

    protected FragmentComponent getFragmentComponent() {
        return DaggerFragmentComponent.builder()
                .activityComponent(((BaseActivity) getActivity()).getActivityComponent())
                .fragmentModule(new FragmentModule(this))
                .build();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getTag());
    }

    @Override
    public void onPause() {
        MobclickAgent.onPageEnd(getTag());
        super.onPause();
    }

    /**
     * 获取fragment 携带的userId
     */
    protected void getLoadArgs() {
        Bundle b = getArguments();
        if (null != b) {
            mLoadId = b.getString("user_id");
        }
    }

    protected void initGPS() {
        LocationManager locationManager = (LocationManager) getActivity()
                .getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则跳转至设置开启界面，设置完毕后返回到当前页面
        if (!locationManager
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder da = new AlertDialog.Builder(getContext());
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

    protected abstract String getTAG();


    public void onBackPressed() {

    }

    public String getPageTittle() {
        return pageTittle;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       /* RefWatcher refWatcher = CTBBaseApplication.getRefWatcher(getActivity());
        if(null != refWatcher) {
            refWatcher.watch(this);
        }*/
    }
}
