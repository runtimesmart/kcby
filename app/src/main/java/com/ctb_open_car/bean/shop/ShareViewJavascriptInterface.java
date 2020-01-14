package com.ctb_open_car.bean.shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.amap.api.maps.model.LatLng;
import com.bumptech.glide.util.Util;
import com.ctb_open_car.CTBApplication;
import com.ctb_open_car.R;
import com.ctb_open_car.view.activity.login.LoginActivity;
import com.rxretrofitlibrary.RxRetrofitApp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;

public class ShareViewJavascriptInterface {
    private RxRetrofitApp mRxInstance;
    private Context mContext;
    public ShareViewJavascriptInterface(Context context) {
        mRxInstance = RxRetrofitApp.Singleton.INSTANCE.get();
        mContext = context;
    }

    @SuppressLint("JavascriptInterface")
    @JavascriptInterface
    public void shareView(String order_no) {
        if (mRxInstance.mHeadBean.getUserId() > 0) {
  Log.e("xxx","xxxxxxxxx order_no" + order_no);
            String url  = "http://tocadmin.chetuobang.com/opencar_view/GroupOrderDetail?order_no="+order_no+"&share=webview";
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = url;
            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title ="订单详情 ";
            msg.description ="订单详情";
            Bitmap thumbBmp = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.app_launcher);
            msg.thumbData = bmpToByteArray(thumbBmp, true);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("webpage");
            req.message =msg;
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
           // req.userOpenId = mRxInstance.mHeadBean.getUserId() + "";
            CTBApplication.mWxApi.sendReq(req);
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
