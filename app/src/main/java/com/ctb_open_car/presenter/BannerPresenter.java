package com.ctb_open_car.presenter;

import android.util.Log;

import com.ctb_open_car.bean.community.BannerData;
import com.ctb_open_car.bean.community.response.HotFeedData;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.BannerApi;
import com.ctb_open_car.engine.net.api.HotFeedsApi;
import com.ctb_open_car.view.fragment.CommunityFragment;
import com.ctb_open_car.view.fragment.comminity.UGCHotFragment;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import timber.log.Timber;

public class BannerPresenter {

    private SoftReference<CommunityFragment> mFragment;

    public BannerPresenter(CommunityFragment fragment) {
        this.mFragment = new SoftReference<>(fragment);
    }

    public void requestBannerList() {
        BannerApi bannerApi = new BannerApi(listener, (RxAppCompatActivity) mFragment.get().getActivity());
        HttpManager.getInstance().doHttpDeal(bannerApi);
    }

    private HttpListener listener = new HttpListener() {
        @Override
        public void onNext(Object o) {
            super.onNext(o);
            BaseResultEntity<BannerData> bannerDao = (BaseResultEntity<BannerData>) o;

            if (null != bannerDao && null != bannerDao.getData()) {
                mFragment.get().setBannerList(bannerDao.getData().getBannerList());
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(Log.getStackTraceString(e));
        }
    };
}
