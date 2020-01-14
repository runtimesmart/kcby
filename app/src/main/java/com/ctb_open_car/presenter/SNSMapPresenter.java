package com.ctb_open_car.presenter;

import android.util.Log;

import com.ctb_open_car.bean.community.response.SNSMapData;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.SNSFeedApi;
import com.ctb_open_car.view.fragment.MapFragment;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class SNSMapPresenter {

    private SoftReference<MapFragment> mFragment;

    public SNSMapPresenter(MapFragment fragment) {
        this.mFragment = new SoftReference<>(fragment);
    }

    public void requestSNSList(String longitude, String latitude, String radius) {
        SNSFeedApi snsFeedApi = new SNSFeedApi(listener, (RxAppCompatActivity) mFragment.get().getActivity());
        HashMap map = new HashMap();
        map.put("longitude", longitude);
        map.put("latitude", latitude);
        map.put("radius", radius);

        snsFeedApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(snsFeedApi);
    }

    private HttpListener listener = new HttpListener() {
        @Override
        public void onNext(Object o) {
            super.onNext(o);
            BaseResultEntity<SNSMapData> snsDao = (BaseResultEntity<SNSMapData>) o;

            if (null == snsDao || null == snsDao.getData()
                    || null == snsDao.getData().getFeedList()|| snsDao.getData().getFeedList().size() == 0) {
            } else {
                mFragment.get().inflateMarkerView(snsDao.getData());
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(Log.getStackTraceString(e));
            Toasty.error(mFragment.get().getActivity(), Log.getStackTraceString(e)).show();
        }
    };
}
