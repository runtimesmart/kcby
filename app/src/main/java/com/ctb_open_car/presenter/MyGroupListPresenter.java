package com.ctb_open_car.presenter;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.ctb_open_car.bean.community.response.GroupData;
import com.ctb_open_car.bean.community.response.group.GroupDto;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.MyGroupListApi;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class MyGroupListPresenter {

    private SoftReference<Fragment> mFragment;
    private UpdateListener mUpdateListener;

    @Inject
    public MyGroupListPresenter(Fragment fragment) {
        this.mFragment = new SoftReference<>(fragment);
    }

    public void requestMyGroupList(UpdateListener updateistener) {
        MyGroupListApi myGroupApi = new MyGroupListApi(listener, null);
        HashMap map = new HashMap();
        myGroupApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(myGroupApi);
        this.mUpdateListener = updateistener;
    }


    private HttpListener listener = new HttpListener() {
        @Override
        public void onNext(Object o) {
            super.onNext(o);

            BaseResultEntity<Object> objDao = (BaseResultEntity<Object>) o;

            Object obj = objDao.getData();
            if (null != obj) {
                if (obj instanceof GroupData) {
                    GroupData groupData = (GroupData) obj;
                    if (null == groupData || null == groupData.getCarGroupList()) {
                        List<GroupDto> groupDtos = new ArrayList<>();
                        mUpdateListener.update(groupDtos);
                    }
                    mUpdateListener.update(groupData.getCarGroupList());
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
        void update(List<GroupDto> groupList);
    }
}
