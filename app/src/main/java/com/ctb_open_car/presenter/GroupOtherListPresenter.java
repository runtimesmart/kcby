package com.ctb_open_car.presenter;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.ctb_open_car.bean.community.response.GroupData;
import com.ctb_open_car.bean.community.response.GroupRecommend;
import com.ctb_open_car.bean.community.response.GroupRecommendData;
import com.ctb_open_car.bean.community.response.group.GroupDto;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.GroupHotListApi;
import com.ctb_open_car.engine.net.api.GroupOtherListApi;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class GroupOtherListPresenter {

    private SoftReference<Fragment> mFragment;
    private UpdateListener mUpdateListener;

    @Inject
    public GroupOtherListPresenter(Fragment fragment) {
        this.mFragment = new SoftReference<>(fragment);
    }

    public void requestOtherGroupList(UpdateListener updateistener) {
        GroupOtherListApi groupOtherListApi = new GroupOtherListApi(listener, null);
        HashMap map = new HashMap();
        groupOtherListApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(groupOtherListApi);
        this.mUpdateListener = updateistener;
    }


    private HttpListener listener = new HttpListener() {
        @Override
        public void onNext(Object o) {
            super.onNext(o);

            BaseResultEntity<Object> objDao = (BaseResultEntity<Object>) o;

            Object obj = objDao.getData();
            if (null != obj) {
                if (obj instanceof GroupRecommendData) {
                    GroupRecommendData groupData = (GroupRecommendData) obj;
                    if (null == groupData
                            || null == groupData.getCarGroupList()
                            || 0 == groupData.getCarGroupList().size()) {
                    } else {
                        mUpdateListener.update(groupData.getCarGroupList());
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

    public interface UpdateListener {
        void update(List<GroupRecommend> groupList);
    }
}
