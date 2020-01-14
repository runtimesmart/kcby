package com.ctb_open_car.view.activity.shopMall;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseActivity;
import com.ctb_open_car.bean.shop.ShareViewJavascriptInterface;
import com.ctb_open_car.bean.shop.ShopBackViewJavascriptInterface;
import com.ctb_open_car.bean.shop.ShopWebViewJavascriptInterface;
import com.rxretrofitlibrary.RxRetrofitApp;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/******* 商城地址管理，订单详情 *****/
public class ShopManagementActivity extends BaseActivity {

    @BindView(R.id.main_webview)
    WebView mWebView;
    private String mTitle;
    private RxRetrofitApp mRxInstance;
    private String mUrl;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_management);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mTitle = intent.getStringExtra("shop_manger_title");
        mUrl = intent.getStringExtra("shop_manger_url");
        mRxInstance = RxRetrofitApp.Singleton.INSTANCE.get();

        initData();
}

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void initData() {
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient());

        WebSettings webSettings = mWebView .getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setDomStorageEnabled(true);
        mWebView.addJavascriptInterface(new ShopBackViewJavascriptInterface(ShopManagementActivity.this), "android");
        mWebView.addJavascriptInterface(new ShareViewJavascriptInterface(ShopManagementActivity.this), "android");

        if (!TextUtils.isEmpty(mRxInstance.mHeadBean.getUserToken()) && mRxInstance.mHeadBean.getUserId() > 0) {
            mUrl = mUrl + "?token=" + mRxInstance.mHeadBean.getUserToken() + "&user_id=" +mRxInstance.mHeadBean.getUserId() + "&from=webview";
        }

        mWebView.loadUrl(mUrl);
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) { // 表示按返回键
                        mWebView.goBack(); // 后退
                        return true; // 已处理
                    }
                }
                return false;
            }
        });
    }

    @Override
    public Object getTag() {
        return null;
    }
}
