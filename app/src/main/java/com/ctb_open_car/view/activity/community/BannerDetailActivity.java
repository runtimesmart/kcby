package com.ctb_open_car.view.activity.community;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.ctb_open_car.view.activity.shopMall.ShopManagementActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BannerDetailActivity extends BaseActivity {
    @BindView(R.id.banner_detail_webview)
    WebView mWebView;

    @BindView(R.id.title_name)
    TextView mTitleName;
    private String mBannerUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_detail_layout);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mBannerUrl = intent.getStringExtra("banner_url");
        initWebView();
        initView();
    }

    private void initView() {
        mTitleName.setText("");
    }

    private void initWebView() {
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient());

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setDomStorageEnabled(true);


        mWebView.loadUrl(mBannerUrl);
    }

    @OnClick(R.id.btn_back)
    void backClick(View v){
        this.finish();
    }
    @Override
    public Object getTag() {
        return null;
    }
}
