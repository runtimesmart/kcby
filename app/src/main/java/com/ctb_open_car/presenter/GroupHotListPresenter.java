package com.ctb_open_car.presenter;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ctb_open_car.base.BaseFragment;
import com.ctb_open_car.bean.community.response.FocusData;
import com.ctb_open_car.bean.community.response.GroupData;
import com.ctb_open_car.bean.community.response.group.GroupDto;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.FocusListApi;
import com.ctb_open_car.engine.net.api.GroupHotListApi;
import com.ctb_open_car.view.fragment.comminity.ExpertsFragment;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class GroupHotListPresenter {

    private SoftReference<Fragment> mFragment;
    private UpdateListener mUpdateListener;
    @Inject
    public GroupHotListPresenter(Fragment fragment) {
        this.mFragment = new SoftReference<>(fragment);
    }

    public void requestHotGroupList(UpdateListener updateistener) {
        GroupHotListApi focusApi = new GroupHotListApi(listener, null);
        HashMap map = new HashMap();
        focusApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(focusApi);
        this.mUpdateListener=updateistener;
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

    public interface UpdateListener{
        void update(List<GroupDto> groupList);
    }
}
