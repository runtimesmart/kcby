package com.ctb_open_car.presenter;

import android.util.Log;

import com.ctb_open_car.bean.community.response.HotFeedData;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.TransmitFeedApi;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class TransmitPresenter {

    private SoftReference<RxAppCompatActivity> mActivity;

    public TransmitPresenter(RxAppCompatActivity activity) {
        this.mActivity = new SoftReference<>(activity);
    }

    public void transmitFeed(String longitude, String latitude, String feedContent, String targetBizType, String targetBizId) {
        TransmitFeedApi transmitApi = new TransmitFeedApi(listener, mActivity.get());
        HashMap map = new HashMap();
        map.put("longitude", longitude);
        map.put("latitude", latitude);
        map.put("targetBizType", targetBizType);
        map.put("feedContent", feedContent);
        map.put("targetBizId", targetBizId);

        transmitApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(transmitApi);
    }

    private HttpListener listener = new HttpListener() {
        @Override
        public void onNext(Object o) {
            super.onNext(o);
            BaseResultEntity<Object> objDao = (BaseResultEntity<Object>) o;

            if ("0".equals(objDao.getRet())) {
                Toasty.info(mActivity.get(), "转发成功").show();
            } else {
                Toasty.error(mActivity.get(), "转发失败").show();
            }

        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(Log.getStackTraceString(e));
        }
    };
}
