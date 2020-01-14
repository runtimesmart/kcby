package com.ctb_open_car.presenter.im;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.RemoveGroupApi;
import com.ctb_open_car.engine.net.api.UserInfoApi;
import com.ctb_open_car.eventbus.MessageEvent;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import javax.inject.Inject;

import timber.log.Timber;

public class RemoveGroupPresenter {

    private SoftReference<AppCompatActivity> mActivity;

    @Inject
    public RemoveGroupPresenter(AppCompatActivity activity) {
        this.mActivity = new SoftReference<>(activity);
    }

    public void removeGroupById(String groupId) {
        RemoveGroupApi removeGroupApi = new RemoveGroupApi(listener, null);
        HashMap map = new HashMap();
        map.put("groupId", groupId);

        removeGroupApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(removeGroupApi);

    }


    private HttpListener listener = new HttpListener() {
        @Override
        public void onNext(Object o) {
            super.onNext(o);
            BaseResultEntity<Object> objDao = (BaseResultEntity<Object>) o;

            Object obj = objDao.getData();
            if (null != obj) {
                MessageEvent messageEvent = new MessageEvent();
                messageEvent.setType("finish_activity");
                EventBus.getDefault().post(messageEvent);
                mActivity.get().finish();
            }

        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(Log.getStackTraceString(e));
        }
    };
}
