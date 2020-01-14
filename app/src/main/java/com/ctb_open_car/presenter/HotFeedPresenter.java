package com.ctb_open_car.presenter;

import android.util.Log;
import android.widget.Toast;

import com.ctb_open_car.bean.community.response.HotFeedData;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.HotFeedsApi;
import com.ctb_open_car.engine.net.api.PersonHotFeedsApi;
import com.ctb_open_car.view.fragment.comminity.UGCHotFragment;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class HotFeedPresenter {

    private SoftReference<UGCHotFragment> mFragment;

    public HotFeedPresenter(UGCHotFragment fragment) {
        this.mFragment = new SoftReference<>(fragment);
    }


    /**
     * 获取个人主页动态
     */
    public void requestPersonFeedList(int pageNum, String userId) {
        if (1 == pageNum) {
            mFragment.get().hotFeedsAdapter.mHotFeedDtoList.clear();
        }
        PersonHotFeedsApi personFeedApi = new PersonHotFeedsApi(listener, (RxAppCompatActivity) mFragment.get().getActivity());
        HashMap map = new HashMap();
        map.put("currentPage", pageNum + "");
        map.put("targetUserId", userId);

        personFeedApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(personFeedApi);
    }

    /**
     * 获取社区动态
     */
    public void requestFeedList(int pageNum) {
        if (1 == pageNum) {
            mFragment.get().hotFeedsAdapter.mHotFeedDtoList.clear();
        }
        HotFeedsApi hotFeedApi = new HotFeedsApi(listener, (RxAppCompatActivity) mFragment.get().getActivity());
        HashMap map = new HashMap();
        map.put("currentPage", pageNum + "");

        hotFeedApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(hotFeedApi);
    }

    private HttpListener listener = new HttpListener() {
        @Override
        public void onNext(Object o) {
            super.onNext(o);

            BaseResultEntity<HotFeedData> hotDao = (BaseResultEntity<HotFeedData>) o;

            if (!"0".equals(hotDao.getRet())) {
                Toasty.warning(mFragment.get().getActivity(), hotDao.getMsg()).show();
            }
            if (null == hotDao || null == hotDao.getData()
                    || null == hotDao.getData().getPageData() || hotDao.getData().getPageData().getData().size() == 0) {
                mFragment.get().loadMoreFailed();
            } else {
                mFragment.get().updateHotFeedList(hotDao.getData());
            }

        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(Log.getStackTraceString(e));
        }
    };
}
