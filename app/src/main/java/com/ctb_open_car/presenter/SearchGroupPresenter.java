package com.ctb_open_car.presenter;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ctb_open_car.bean.community.response.GroupData;
import com.ctb_open_car.bean.community.response.group.GroupDto;
import com.ctb_open_car.bean.im.CarGroupSearchDto;
import com.ctb_open_car.bean.im.SearchGroupBean;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.MyGroupListApi;
import com.ctb_open_car.engine.net.api.SearchGroupApi;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class SearchGroupPresenter {

    private SoftReference<AppCompatActivity> mActivity;
    private UpdateListener mUpdateListener;

    @Inject
    public SearchGroupPresenter(AppCompatActivity appCompatActivity) {
        this.mActivity = new SoftReference<>(appCompatActivity);
    }

    public void requestMyGroupList(String searchWord, UpdateListener updateistener) {
        HashMap map = new HashMap();
        map.put("searchName", searchWord);
        SearchGroupApi searchGroupApi = new SearchGroupApi(listener, null);
        searchGroupApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(searchGroupApi);
        this.mUpdateListener = updateistener;
    }


    private HttpListener listener = new HttpListener() {
        @Override
        public void onNext(Object o) {
            super.onNext(o);

            BaseResultEntity<Object> objDao = (BaseResultEntity<Object>) o;

            Object obj = objDao.getData();
            if (null != obj) {
                if (obj instanceof SearchGroupBean) {
                    SearchGroupBean groupData = (SearchGroupBean) obj;
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
        void update(List<CarGroupSearchDto> groupList);
    }
}
