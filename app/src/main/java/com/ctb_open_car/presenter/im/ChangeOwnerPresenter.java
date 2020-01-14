package com.ctb_open_car.presenter.im;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.ChangeGroupOwnerApi;
import com.ctb_open_car.engine.net.api.RemoveGroupApi;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class ChangeOwnerPresenter {
    private SoftReference<AppCompatActivity> mActivity;
    private UpdateListener mUpdateListener;

    @Inject
    public ChangeOwnerPresenter(AppCompatActivity activity) {
        this.mActivity = new SoftReference<>(activity);
    }


    public void changeGroupOwnerById(String groupId, String targetEmId, UpdateListener updateListener) {
        ChangeGroupOwnerApi changeGroupOwnerApi = new ChangeGroupOwnerApi(listener, null);
        HashMap map = new HashMap();
        map.put("groupId", groupId);
        map.put("targetEmId", targetEmId);

        changeGroupOwnerApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(changeGroupOwnerApi);
        mUpdateListener = updateListener;
    }


    private HttpListener listener = new HttpListener() {
        @Override
        public void onNext(Object o) {
            super.onNext(o);
            BaseResultEntity<Object> objDao = (BaseResultEntity<Object>) o;
            if(!objDao.getRet().equals("0")){
                Toasty.warning(mActivity.get(),objDao.getMsg()).show();
                return;
            }
            Object obj = objDao.getData();
            if (null != obj) {
                mUpdateListener.update(1);
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(Log.getStackTraceString(e));
        }
    };

    public interface UpdateListener {
        void update(int result);
    }
}
