package com.ctb_open_car.presenter;

import android.util.Log;

import com.ctb_open_car.bean.community.response.FocusData;
import com.ctb_open_car.bean.community.response.FocusFeedData;
import com.ctb_open_car.bean.community.response.UserData;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.FocusFeedsApi;
import com.ctb_open_car.engine.net.api.FocusListApi;
import com.ctb_open_car.engine.net.api.UserInfoApi;
import com.ctb_open_car.view.fragment.comminity.ExpertsFragment;
import com.ctb_open_car.view.fragment.comminity.UGCFocusFragment;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import timber.log.Timber;

public class FocusListPresenter {

    private SoftReference<ExpertsFragment> mFragment;

    public FocusListPresenter(ExpertsFragment fragment) {
        this.mFragment = new SoftReference<>(fragment);
    }

    public void requesetHostFocusList(String userId, int pageNum) {
        if (1 == pageNum) {
            mFragment.get().mAdapter.mFocusUserList.clear();
        }
        FocusListApi focusApi = new FocusListApi(listener, null);
        HashMap map = new HashMap();
        map.put("targetUserId", userId);
        map.put("currentPage", pageNum + "");

        focusApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(focusApi);

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
                if (obj instanceof FocusData) {
                    //本人信息
                    FocusData focusData = (FocusData) obj;
                    if (null == focusData.getPageData()
                            || null == focusData.getPageData().getData()
                            || 0 == focusData.getPageData().getData().size()) {
                        mFragment.get().loadMoreFailed();
                    } else {
                        mFragment.get().updateExpertList(focusData.getPageData().getData());
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
