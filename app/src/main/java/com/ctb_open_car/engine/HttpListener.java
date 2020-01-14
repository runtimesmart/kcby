package com.ctb_open_car.engine;

import android.content.Intent;
import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.ctb_open_car.CTBApplication;
import com.ctb_open_car.view.activity.login.LoginActivity;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.RxRetrofitApp;
import com.rxretrofitlibrary.listener.HttpOnNextListener;

public class HttpListener extends HttpOnNextListener {
    @Override
    public void onNext(Object object) {

        if (object instanceof BaseResultEntity) {
            BaseResultEntity<Object> obj = (BaseResultEntity<Object>) object;
            if (obj.getRet().equals("-4")) {
                RxRetrofitApp rxInstance = RxRetrofitApp.Singleton.INSTANCE.get();
                rxInstance.mHeadBean.setUserId(-1L);
                rxInstance.mHeadBean.setUserToken("");
                EMClient.getInstance().logout(true, new EMCallBack() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(int code, String error) {

                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }
                });
                Double longitude = CTBApplication.getInstance().getRxApp().mHeadBean.getLongitude();
                Double latitude = CTBApplication.getInstance().getRxApp().mHeadBean.getLatitude();
                LatLng latLng = new LatLng(latitude, longitude);
                Intent i = new Intent(CTBApplication.getContext(), LoginActivity.class);
                i.putExtra("LatLng", latLng);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                CTBApplication.getContext().startActivity(i);
                return;
            }
        }

    }
}
