package com.ctb_open_car.presenter;

import android.util.Log;

import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseActivity;
import com.ctb_open_car.base.BaseMapActivity;
import com.ctb_open_car.bean.community.response.EventData;
import com.ctb_open_car.bean.roadcondition.RoadConditionBean;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.EventLocalListApi;
import com.ctb_open_car.engine.net.api.EventNewsListApi;
import com.ctb_open_car.engine.net.api.PublishRoadConditionApi;
import com.ctb_open_car.view.activity.activities.PushActivitiesActivity;
import com.ctb_open_car.view.activity.map.NaviSearchActivity;
import com.ctb_open_car.view.fragment.MapFragment;
import com.ctb_open_car.view.fragment.comminity.UGCEventFragment;
import com.google.gson.JsonObject;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class ReportRoadConditionPresenter implements BasePresenter {

    private SoftReference<BaseActivity> mFragment;

    public ReportRoadConditionPresenter(BaseActivity fragment) {
        this.mFragment = new SoftReference<>(fragment);
    }

    public void reportRoadCondition(RoadConditionBean roadConditionBean) {
        HashMap map = new HashMap();
        map.put("rcType", roadConditionBean.getRcType());
        map.put("publishType", roadConditionBean.getPublishType());
        map.put("position", roadConditionBean.getLongitude() + "," + roadConditionBean.getLatitude());
        map.put("positionName", roadConditionBean.getPositionName());
        if (roadConditionBean.getPublishType() == 2) {
            map.put("fileUrls", roadConditionBean.getFileUrls());
        }

        PublishRoadConditionApi roadConditionApi = new PublishRoadConditionApi(listener, (RxAppCompatActivity) mFragment.get());
        roadConditionApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(roadConditionApi);

    }

    private HttpListener listener = new HttpListener() {
        @Override
        public void onNext(Object object) {
            super.onNext(object);
            JsonObject jsonObject = (JsonObject) object;
            String code = jsonObject.get("code").toString();
            int codeIndex = Integer.parseInt(code.replace("\"", ""));
            if (codeIndex == 0) {
                Toasty.info(mFragment.get(), mFragment.get().getString(R.string.release_dynamics_success)).show();
                ((BaseMapActivity)mFragment.get()).dismissVoiceDialog();
            } else {
                Toasty.info((RxAppCompatActivity) mFragment.get(), mFragment.get().getString(R.string.release_dynamics_failed)).show();
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(Log.getStackTraceString(e));
        }
    };
}
