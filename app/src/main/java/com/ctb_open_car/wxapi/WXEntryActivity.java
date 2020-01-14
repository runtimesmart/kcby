package com.ctb_open_car.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ctb_open_car.base.BaseActivity;
import com.ctb_open_car.engine.net.api.LoginApi;
import com.ctb_open_car.eventbus.MessageEvent;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import okhttp3.HttpUrl;
import timber.log.Timber;

public class WXEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler {
    private IWXAPI wxApi;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册微信
         wxApi= WXAPIFactory.createWXAPI(this,"wx3c0ccbcc34600c72", true);
         wxApi.registerApp("wx3c0ccbcc34600c72");
         wxApi.handleIntent(getIntent(),this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        wxApi.handleIntent(intent, this);//必须调用此句话
    }


    public String getTag() {
        return WXEntryActivity.class.getSimpleName();
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
       // Toast.makeText(this, baseResp.errCode, Toast.LENGTH_LONG).show();
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                SendAuth.Resp sendResp = (SendAuth.Resp) baseResp;

                MessageEvent messageEvent = new MessageEvent();
                messageEvent.setType("WXAPI_Resp_Success");
                messageEvent.setObject(sendResp.code);
                EventBus.getDefault().post(messageEvent);
                //getAccessToken(sendResp.code);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                break;
            default:
                break;
        }
    }

    /**
     * 获取access_token：
     *
     * @param code 用户或取access_token的code，仅在ErrCode为0时有效
     */
    private void getAccessToken(final String code) {
        Map<String, String> params = new HashMap();
        params.put("appid", "");
        params.put("secret", "");
        params.put("code", code);
        params.put("grant_type", "refresh_token");

        String url="https://api.weixin.qq.com/sns/oauth2/refresh_token";
        HttpUrl.Builder urlBuilder= HttpUrl.parse(url)
                .newBuilder();
        urlBuilder.addQueryParameter("appid",params.get("appid"));
        urlBuilder.addQueryParameter("secret",params.get("secret"));
        urlBuilder.addQueryParameter("code",params.get("code"));
        urlBuilder.addQueryParameter("grant_type",params.get("grant_type"));
        urlBuilder.addQueryParameter("appid",params.get("appid"));

    }

    /**
     * 获取微信登录，用户授权后的个人信息
     *
     * @param access_token
     * @param openid
     * @param
     */
    private void getWXUserInfo(final String access_token, final String openid) {
        Map<String, String> params = new HashMap();
        params.put("access_token", access_token);
        params.put("openid", openid);

        String url="https://api.weixin.qq.com/sns/userinfo";
        HttpUrl.Builder urlBuilder= HttpUrl.parse(url)
                .newBuilder();
        urlBuilder.addQueryParameter("access_token",params.get("access_token"));
        urlBuilder.addQueryParameter("openid",params.get("openid"));
    }


}
