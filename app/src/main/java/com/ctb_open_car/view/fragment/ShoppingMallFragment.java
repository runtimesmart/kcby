package com.ctb_open_car.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseFragment;
import com.ctb_open_car.bean.shop.ShopWebViewJavascriptInterface;
import com.ctb_open_car.bean.shop.ShareViewJavascriptInterface;
import com.ctb_open_car.eventbus.MessageEvent;
import com.rxretrofitlibrary.RxRetrofitApp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * 通讯录
 * <p>展示居中位置的tab页卡</p>
 */
public class ShoppingMallFragment extends BaseFragment {
    @BindView(R.id.main_webview)
    WebView mWebView;

    private RxRetrofitApp mRxInstance;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRxInstance = RxRetrofitApp.Singleton.INSTANCE.get();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_mall, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initData();
        return view;
    }


    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void initData() {
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient());

        WebSettings webSettings = mWebView .getSettings();
        // 允许JS交互
        webSettings.setJavaScriptEnabled(true);
        // 允许通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 将图片调整到适合webview的大小
        webSettings.setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webSettings.setLoadWithOverviewMode(true);
        //支持缩放，默认为true。是下面那个的前提。
        webSettings.setSupportZoom(false);
        //设置内置的缩放控件。 这个取决于setSupportZoom(), 若setSupportZoom(false)，则该WebView不可缩放，这个不管设置什么都不能缩放。
        webSettings.setBuiltInZoomControls(true);
        //隐藏原生的缩放控件
        webSettings.setDisplayZoomControls(false);
        webSettings.setDomStorageEnabled(true);
        mWebView.addJavascriptInterface(new ShopWebViewJavascriptInterface(getActivity()), "android");
        mWebView.addJavascriptInterface(new ShareViewJavascriptInterface(getActivity()), "android");
        String url = "http://tocadmin.chetuobang.com/opencar_view/";
        if (!TextUtils.isEmpty(mRxInstance.mHeadBean.getUserToken()) && mRxInstance.mHeadBean.getUserId() > 0) {
            url = url + "?token=" + mRxInstance.mHeadBean.getUserToken() + "&user_id=" +mRxInstance.mHeadBean.getUserId();
        }

        mWebView.loadUrl(url);
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
            String referer = "http://tocadmin.chetuobang.com";
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


    public void callBackWebView() {
        String functionName= "'\""+mRxInstance.mHeadBean.getUserToken()+"\"'" + "," + "'\"" +mRxInstance.mHeadBean.getUserId()+"\"'";
        String js = "javascript:setUserIdToken" + "(" + functionName + ")";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//sdk>19才有用
            mWebView.evaluateJavascript(js, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Timber.e("onReceiveValue = %s", value);
                }
            });
        } else {
            mWebView.loadUrl(js);
        }


    }
    @Override
    protected String getTAG() {
        return null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getEventBusMsg(MessageEvent event) {
        if(null==event.getType()){
            return;
        }
        switch (event.getType()) {
            case "update_data_shop":
                callBackWebView();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
