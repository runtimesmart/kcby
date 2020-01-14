package com.ctb_open_car.presenter;

import android.util.Log;

import com.ctb_open_car.bean.community.response.EventData;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.EventLocalListApi;
import com.ctb_open_car.engine.net.api.EventNewsListApi;
import com.ctb_open_car.engine.net.api.PersonEventListApi;
import com.ctb_open_car.view.fragment.comminity.UGCEventFragment;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.ref.SoftReference;
import java.util.HashMap;


import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class EventPresenter implements BasePresenter {
    public int eventType = 1;
    private SoftReference<UGCEventFragment> mFragment;

    public EventPresenter(UGCEventFragment fragment) {
        this.mFragment = new SoftReference<>(fragment);
    }

    /**
     * 获取个人主页活动列表
     */
    public void requestPersonEventList(int pageNum, String userId) {
        if (1 == pageNum) {
            mFragment.get().eventsAdapter.mActivityCardList.clear();
        }
        HashMap map = new HashMap();
        map.put("currentPage", pageNum + "");
        map.put("targetUserId", userId);

        PersonEventListApi personEventListApi = new PersonEventListApi(listener, (RxAppCompatActivity) mFragment.get().getActivity());
        personEventListApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(personEventListApi);

    }

    /**
     * 获取社区活动列表
     */
    public void requestEventList(int pageNum, int type) {
        if (1 == pageNum) {
            mFragment.get().eventsAdapter.mActivityCardList.clear();
        }
        HashMap map = new HashMap();
        map.put("currentPage", pageNum + "");

        if (1 == type) {
            eventType = 1;
            EventNewsListApi eventNewsListApi = new EventNewsListApi(listener, (RxAppCompatActivity) mFragment.get().getActivity());
            eventNewsListApi.setRequestBody(map);
            HttpManager.getInstance().doHttpDeal(eventNewsListApi);
        } else {
            eventType = 2;
            EventLocalListApi eventLocalListApi = new EventLocalListApi(listener, (RxAppCompatActivity) mFragment.get().getActivity());
            eventLocalListApi.setRequestBody(map);
            HttpManager.getInstance().doHttpDeal(eventLocalListApi);
        }
    }


    private HttpListener listener = new HttpListener() {
        @Override
        public void onNext(Object o) {
            super.onNext(o);
            BaseResultEntity<EventData> eventDao = (BaseResultEntity<EventData>) o;
            if (!"0".equals(eventDao.getRet())) {
                Toasty.warning(mFragment.get().getActivity(), eventDao.getMsg()).show();
            }
            if (null == eventDao || null == eventDao.getData()
                    || null == eventDao.getData().getPageData() || eventDao.getData().getPageData().getData().size() == 0) {
                mFragment.get().loadMoreFailed();
            } else {
                mFragment.get().updateEventList(eventDao.getData());
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(Log.getStackTraceString(e));
        }
    };
}
