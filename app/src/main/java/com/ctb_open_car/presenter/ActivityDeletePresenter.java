package com.ctb_open_car.presenter;

import android.content.Context;
import android.util.Log;

import com.ctb_open_car.bean.community.response.FocusFeedData;
import com.ctb_open_car.bean.community.response.UserData;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.ActivityDeleteApi;
import com.ctb_open_car.engine.net.api.FocusFeedsApi;
import com.ctb_open_car.engine.net.api.UserInfoApi;
import com.ctb_open_car.view.fragment.comminity.UGCFocusFragment;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class ActivityDeletePresenter {

    private SoftReference<Context> mContext;

    public ActivityDeletePresenter(Context context) {
        this.mContext = new SoftReference<>(context);
    }

    public void deleteActivity(String activityId) {
        ActivityDeleteApi activityDeleteApi = new ActivityDeleteApi(listener, null);
        HashMap map = new HashMap();
        map.put("activityId", activityId);

        activityDeleteApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(activityDeleteApi);

    }


    private HttpListener listener = new HttpListener() {
        @Override
        public void onNext(Object o) {
            super.onNext(o);

            BaseResultEntity<Object> objDao = (BaseResultEntity<Object>) o;

            if ("0".equals(objDao.getRet())) {
                Toasty.info(mContext.get(), "删除成功").show();
            } else {
                Toasty.error(mContext.get(), "删除失败").show();
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(Log.getStackTraceString(e));
        }
    };
}
