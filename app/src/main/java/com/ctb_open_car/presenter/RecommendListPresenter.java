package com.ctb_open_car.presenter;

import android.util.Log;

import com.ctb_open_car.bean.community.response.FocusData;
import com.ctb_open_car.bean.community.response.RecommendData;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.FocusListApi;
import com.ctb_open_car.engine.net.api.RecommendListApi;
import com.ctb_open_car.view.fragment.comminity.ExpertsFragment;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import timber.log.Timber;

public class RecommendListPresenter {

    private SoftReference<ExpertsFragment> mFragment;

    public RecommendListPresenter(ExpertsFragment fragment) {
        this.mFragment = new SoftReference<>(fragment);
    }

    public void requesetRecommendList(int pageNum) {
        if (1 == pageNum) {
            mFragment.get().mAdapter.mFocusUserList.clear();
        }
        RecommendListApi recommendListApi = new RecommendListApi(listener, null);
        HashMap map = new HashMap();
        map.put("currentPage", pageNum + "");

        recommendListApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(recommendListApi);

    }


    private HttpListener listener = new HttpListener() {
        @Override
        public void onNext(Object o) {
            super.onNext(o);

            BaseResultEntity<Object> objDao = (BaseResultEntity<Object>) o;

            Object obj = objDao.getData();
            if (null != obj) {
                if (obj instanceof RecommendData) {
                    //本人信息
                    RecommendData recommendData = (RecommendData) obj;
                    if (null == recommendData.getPageData()
                            || null == recommendData.getPageData().getData()
                            || 0 == recommendData.getPageData().getData().size()) {
                        mFragment.get().loadMoreFailed();
                    } else {
                        mFragment.get().updateExpertList(recommendData.getPageData().getData());
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
