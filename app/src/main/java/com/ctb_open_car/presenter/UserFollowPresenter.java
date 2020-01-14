package com.ctb_open_car.presenter;

import android.content.Context;
import android.util.Log;

import com.ctb_open_car.bean.community.response.HotFeedData;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.UserCancelFollowApi;
import com.ctb_open_car.engine.net.api.UserFollowApi;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class UserFollowPresenter {

    private SoftReference<Context> mContext;
    private int mActionType = 0;

    private FollowCallback mCallback;

    public UserFollowPresenter(Context context, FollowCallback callback) {
        this.mContext = new SoftReference<>(context);
        mCallback = callback;
    }

    /**
     * 用户关注
     */
    public void userFollow(String targetUserId) {
        mActionType = 1;
        UserFollowApi userFollowApi = new UserFollowApi(listener, (RxAppCompatActivity) mContext.get());
        HashMap map = new HashMap();
        map.put("targetUserId", targetUserId);
        userFollowApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(userFollowApi);
    }

    /**
     * 取消关注
     */
    public void userCancelFollow(String targetUserId) {
        mActionType = 0;

        UserCancelFollowApi userCancelFollowApi = new UserCancelFollowApi(listener, (RxAppCompatActivity) mContext.get());
        HashMap map = new HashMap();
        map.put("targetUserId", targetUserId);

        userCancelFollowApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(userCancelFollowApi);
    }


    private HttpListener listener = new HttpListener() {
        @Override
        public void onNext(Object o) {
            super.onNext(o);
            BaseResultEntity<Object> objDao = (BaseResultEntity<Object>) o;
            if (!"0".equals(objDao.getRet())) {
                Toasty.warning(mContext.get(), objDao.getMsg()).show();
            }
            if(objDao.getRet().equals("-4")){
                return;
            }
            //操作成功
            if (0 == mActionType) {
                //取消关注成功
                mCallback.actionCancelSuccess();
            } else {
                //关注成功
                mCallback.actionFollowSuccess();
            }

        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(Log.getStackTraceString(e));
            Toasty.error(mContext.get(), "失败").show();
        }
    };

    public interface FollowCallback {
        void actionCancelSuccess();
        void actionFollowSuccess();
    }
}
