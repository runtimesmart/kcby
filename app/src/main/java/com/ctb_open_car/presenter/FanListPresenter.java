package com.ctb_open_car.presenter;

import android.util.Log;

import com.ctb_open_car.bean.community.response.FanData;
import com.ctb_open_car.bean.community.response.FocusFeedData;
import com.ctb_open_car.bean.community.response.UserData;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.FanListApi;
import com.ctb_open_car.engine.net.api.FocusFeedsApi;
import com.ctb_open_car.engine.net.api.UserInfoApi;
import com.ctb_open_car.view.fragment.comminity.FanListFragment;
import com.ctb_open_car.view.fragment.comminity.UGCFocusFragment;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import timber.log.Timber;

public class FanListPresenter {

    private SoftReference<FanListFragment> mFragment;

    public FanListPresenter(FanListFragment fragment) {
        this.mFragment = new SoftReference<>(fragment);
    }

    public void requesetFanList(String userId, int pageNum) {
        if (1 == pageNum) {
            mFragment.get().mAdapter.mFocusUserList.clear();
        }
        FanListApi fanFeedsApi = new FanListApi(listener, null);
        HashMap map = new HashMap();
        map.put("targetUserId", userId);
        map.put("currentPage", pageNum + "");

        fanFeedsApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(fanFeedsApi);

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
                if (obj instanceof FanData) {
                    //本人信息
                    FanData fanDataDao = (FanData) obj;
                    if (null == fanDataDao.getPageData()
                            || null == fanDataDao.getPageData().getData()
                            || 0 == fanDataDao.getPageData().getData().size()) {
                        mFragment.get().loadMoreFailed();
                    } else {
                        mFragment.get().updateFanList(fanDataDao.getPageData().getData());
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
