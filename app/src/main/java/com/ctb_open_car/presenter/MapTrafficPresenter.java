package com.ctb_open_car.presenter;

import android.util.Log;

import com.ctb_open_car.bean.community.response.RoadData;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.MapTrafficApi;
import com.ctb_open_car.view.activity.map.NaviActivity;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class MapTrafficPresenter {
    private SoftReference<NaviActivity> mActivity;

    public MapTrafficPresenter(NaviActivity activity) {
        this.mActivity = new SoftReference<>(activity);
    }

    public void requestTraffic(String longitude, String latitude, String radius) {
        MapTrafficApi mapTrafficApi = new MapTrafficApi(listener, (RxAppCompatActivity) mActivity.get());
        HashMap map = new HashMap();
        map.put("longitude", longitude);
        map.put("latitude", latitude);
        map.put("radius", radius);

        mapTrafficApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(mapTrafficApi);
    }

    private HttpListener listener = new HttpListener() {
        @Override
        public void onNext(Object o) {
super.onNext(o);
            BaseResultEntity<RoadData> roadData = (BaseResultEntity<RoadData>) o;

            if(!"0".equals(roadData.getRet())){
                Toasty.warning(mActivity.get(),roadData.getMsg()).show();
            }
            if(null==roadData || null==roadData.getData()
                    || null==roadData.getData().getRoadConditionList()
                    || roadData.getData().getRoadConditionList().size()==0){
            }else{
                mActivity.get().updateTrafficData(roadData.getData());
            }

        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(Log.getStackTraceString(e));
        }
    };

}
