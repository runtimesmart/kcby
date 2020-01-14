package com.ctb_open_car.presenter;

import android.util.Log;

import com.ctb_open_car.bean.community.ActivityDetailData;
import com.ctb_open_car.bean.community.FeedDetailData;
import com.ctb_open_car.bean.community.response.CommentData;
import com.ctb_open_car.bean.community.response.FocusFeedData;
import com.ctb_open_car.bean.community.response.UserData;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.FocusFeedsApi;
import com.ctb_open_car.engine.net.api.UserInfoApi;
import com.ctb_open_car.view.fragment.comminity.UGCFocusFragment;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class FocusFeedPresenter {

    private SoftReference<UGCFocusFragment> mFragment;

    public FocusFeedPresenter(UGCFocusFragment fragment) {
        this.mFragment = new SoftReference<>(fragment);
    }

    public void requesetUserInfo(String userId) {
        UserInfoApi userInfoApi = new UserInfoApi(listener, null);
        HashMap map = new HashMap();
        map.put("targetUserId", userId);

        userInfoApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(userInfoApi);

    }

    public void requestFeedList(int pageNum) {
        if (1 == pageNum) {
            mFragment.get().focusFeedsAdapter.mFocusFeedList.clear();
        }
        FocusFeedsApi focusFeedApi = new FocusFeedsApi(listener, (RxAppCompatActivity) mFragment.get().getActivity());
        HashMap map = new HashMap();
        map.put("currentPage", pageNum + "");

        focusFeedApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(focusFeedApi);
    }

    private HttpListener listener = new HttpListener() {
        @Override
        public void onNext(Object o) {
            super.onNext(o);
//            BaseResultEntity<FocusFeedData> fucusDao = (BaseResultEntity<FocusFeedData>) o;
//            if(!"0".equals(fucusDao.getRet())){
//                Toasty.warning(mFragment.get().getActivity(),fucusDao.getMsg()).show();
//            }
//            if (null == fucusDao || null == fucusDao.getData()
//                    || null == fucusDao.getData().getPageData() || fucusDao.getData().getPageData().getData().size() == 0) {
//                mFragment.get().loadMoreFailed();
//            } else {
//                mFragment.get().updateFocusFeedList(fucusDao.getData());
//            }

            BaseResultEntity<Object> objDao = (BaseResultEntity<Object>) o;

            Object obj = objDao.getData();
            if (null != obj) {
                if (obj instanceof UserData) {
                    //本人信息
                    UserData userDao = (UserData) obj;

                    mFragment.get().updateHostData(userDao);
                } else if (obj instanceof FocusFeedData) {
                    FocusFeedData fucusDao = (FocusFeedData) obj;
                    //动态详情
                    if (null == objDao || null == objDao.getData()
                            || null == fucusDao.getPageData() || fucusDao.getPageData().getData().size() == 0) {
                        mFragment.get().loadMoreFailed();
                    } else {
                        mFragment.get().updateFocusFeedList(fucusDao);
                    }
                }
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(Log.getStackTraceString(e));
        }
    };
}
