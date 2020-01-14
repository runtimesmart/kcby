package com.ctb_open_car.bean.shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.amap.api.maps.model.LatLng;
import com.ctb_open_car.view.activity.login.LoginActivity;
import com.rxretrofitlibrary.RxRetrofitApp;

public class ShopWebViewJavascriptInterface {
    private RxRetrofitApp mRxInstance;
    private Context mContext;
    public ShopWebViewJavascriptInterface(Context context) {
        mRxInstance = RxRetrofitApp.Singleton.INSTANCE.get();
        mContext = context;
    }
    @SuppressLint("JavascriptInterface")
    @JavascriptInterface
    public void viewGlogin(String url) {
        if (mRxInstance.mHeadBean.getUserId() <= 0) {
            LatLng mCurrentLoc = new LatLng(mRxInstance.mHeadBean.getLatitude(), mRxInstance.mHeadBean.getLongitude());
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("LatLng", mCurrentLoc);
            intent.putExtra("webView", true);
            mContext.startActivity(intent);
        }
    }
}
