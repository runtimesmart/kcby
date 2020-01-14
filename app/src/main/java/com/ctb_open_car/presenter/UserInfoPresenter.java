package com.ctb_open_car.presenter;


import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ctb_open_car.bean.community.response.UserData;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.UserInfoApi;
import com.ctb_open_car.view.activity.person.PersonHomeActivity;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class UserInfoPresenter {

    private SoftReference<AppCompatActivity> mActivity;
    public UpdateListener mUpdateListener;
    public UserInfoPresenter(AppCompatActivity activity) {
        this.mActivity = new SoftReference<>(activity);
    }

    public void requestUserInfo(String userId, UpdateListener updateListener) {
        UserInfoApi userInfoApi = new UserInfoApi(listener, null);
        HashMap map = new HashMap();
        map.put("targetUserId", userId);

        userInfoApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(userInfoApi);
        this.mUpdateListener=updateListener;
    }

    private HttpListener listener = new HttpListener() {
        @Override
        public void onNext(Object o) {
            super.onNext(o);
            BaseResultEntity<UserData> userDao = (BaseResultEntity<UserData>) o;
            if (!"0".equals(userDao.getRet())) {
                Toasty.warning(mActivity.get(), userDao.getMsg()).show();
            }

            if (null == userDao.getData() || null == userDao.getData().getUserHome()) {
                return;
            }
            mUpdateListener.updateUserInfo(userDao.getData());
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(Log.getStackTraceString(e));
        }
    };

    public interface UpdateListener{
        void updateUserInfo(UserData userData);
    }
}
