package com.ctb_open_car.view.activity.vehicletools;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseActivity;
import com.ctb_open_car.bean.shop.ShareViewJavascriptInterface;
import com.ctb_open_car.bean.shop.ShopWebViewJavascriptInterface;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViolationWebViewActivity extends BaseActivity {

    @BindView(R.id.ic_back)
    ImageView mBack;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.webview)
    WebView mWebView;

    private String mUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.violation_webview_activity);
        ButterKnife.bind(this);
        mUrl = getIntent().getStringExtra("violateDzyH5Url");
        initData();
    }

    public void initData() {
        mTitleTv.setText("违章处理");
        mTitleTv.setVisibility(View.VISIBLE);
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

        mWebView.loadUrl(mUrl);
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) { // 表示按返回键
                        // 时的操作
                        mWebView.goBack(); // 后退
                        // webview.goForward();//前进
                        return true; // 已处理
                    }
                }
                return false;
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            String referer = "https://m.jiaofabao.com";
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    if (url.startsWith("weixin://") || url.startsWith("alipays://")) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                } catch (Exception e) {
                    return false;
                }

                if (url.contains("https://wx.tenpay.com")) {
                    Map<String, String> extraHeaders = new HashMap<>();
                    extraHeaders.put("Referer", referer);
                    view.loadUrl(url, extraHeaders);
                    // referer = url;
                    return true;
                }
                view.loadUrl(url);
                return true;
            }
        });

    }
    @Override
    public Object getTag() {
        return null;
    }

    @OnClick(R.id.ic_back)
    public void onViewClicked() {
        onBackPressed();
    }
}
