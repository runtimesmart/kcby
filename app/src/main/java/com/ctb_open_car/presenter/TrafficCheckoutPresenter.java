package com.ctb_open_car.presenter;

import android.content.Context;
import android.util.Log;

import com.ctb_open_car.bean.community.response.HotFeedData;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.ActivityLikedApi;
import com.ctb_open_car.engine.net.api.BaseLikeApi;
import com.ctb_open_car.engine.net.api.FeedLikedApi;
import com.ctb_open_car.engine.net.api.TrafficEventCheckApi;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class TrafficCheckoutPresenter {

    private SoftReference<Context> mContext;

    public static final int ACTION_TYPE_INCORRECT=111;
    public static final int ACTION_TYPE_CORRECT=222;
    public TrafficCheckoutPresenter(Context context) {
        this.mContext = new SoftReference<>(context);
    }

    /**
     * 动态点赞
     */
    public void incorrectCheck(String rcId, String checkType) {

        TrafficEventCheckApi trafficCheckApi = new TrafficEventCheckApi(listener, (RxAppCompatActivity) mContext.get());
        HashMap map = new HashMap();

        map.put("rcId", rcId);
        map.put("checkType", checkType);

        trafficCheckApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(trafficCheckApi);
    }

    /**
     * 取消动态点赞
     */
    public void correctCheck(String rcId, String checkType) {

        TrafficEventCheckApi trafficCheckApi = new TrafficEventCheckApi(listener, (RxAppCompatActivity) mContext.get());
        HashMap map = new HashMap();
        map.put("rcId", rcId);
        map.put("checkType", checkType);

        trafficCheckApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(trafficCheckApi);
    }


    private HttpListener listener = new HttpListener() {
        @Override
        public void onNext(Object o) {
            super.onNext(o);
            BaseResultEntity objDao = (BaseResultEntity) o;
            if (!"0".equals(objDao.getRet())) {
                Toasty.warning(mContext.get(), objDao.getMsg()).show();
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(Log.getStackTraceString(e));
            Toasty.error(mContext.get(), "评价失败").show();
        }
    };
}
