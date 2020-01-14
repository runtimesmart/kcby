package com.ctb_open_car.presenter;

import android.util.Log;

import com.ctb_open_car.bean.community.response.NearbyData;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.manager.GsonManager;
import com.ctb_open_car.engine.net.api.NearbyFeedsApi;
import com.ctb_open_car.view.fragment.comminity.UGCNearbyFragment;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class NearbyFeedPresenter {

    private SoftReference<UGCNearbyFragment> mFragment;

    public NearbyFeedPresenter(UGCNearbyFragment fragment) {
        this.mFragment = new SoftReference<>(fragment);
    }

    public void requestNearbyList(int pageNum) {
        if (1 == pageNum) {
            mFragment.get().nearbyFeedsAdapter.mNearbyFeedList.clear();
        }
        NearbyFeedsApi focusFeedApi = new NearbyFeedsApi(listener, (RxAppCompatActivity) mFragment.get().getActivity());
        HashMap map = new HashMap();
        Timber.e("page...."+pageNum);
        map.put("currentPage", pageNum+"");

        focusFeedApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(focusFeedApi);
    }

    private HttpListener listener = new HttpListener() {
        @Override
        public void onNext(Object o) {
            super.onNext(o);
            BaseResultEntity<NearbyData> nearbyDao = (BaseResultEntity<NearbyData>) o;
            if(!"0".equals(nearbyDao.getRet())){
                Toasty.warning(mFragment.get().getActivity(),nearbyDao.getMsg()).show();
            }
            if(null==nearbyDao || null==nearbyDao.getData()
                    || null==nearbyDao.getData().getPageData() || nearbyDao.getData().getPageData().getData().size()==0){
                mFragment.get().loadMoreFailed();
            }else{
                mFragment.get().updateNearbyFeedList(nearbyDao.getData());
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(Log.getStackTraceString(e));
            Toasty.error(mFragment.get().getActivity(),Log.getStackTraceString(e)).show();
        }
    };
}
