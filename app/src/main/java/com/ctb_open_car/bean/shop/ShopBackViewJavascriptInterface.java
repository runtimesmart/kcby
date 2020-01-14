package com.ctb_open_car.bean.shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.amap.api.maps.model.LatLng;
import com.ctb_open_car.view.activity.login.LoginActivity;
import com.ctb_open_car.view.activity.shopMall.ShopManagementActivity;
import com.rxretrofitlibrary.RxRetrofitApp;

public class ShopBackViewJavascriptInterface {
    private ShopManagementActivity mContext;
    public ShopBackViewJavascriptInterface(ShopManagementActivity context) {
        mContext = context;
    }

    @SuppressLint("JavascriptInterface")
    @JavascriptInterface
    public void backView(String url) {
        Log.e("xxx","xxxxxx url = " +url);
        mContext.finish();
    }
}