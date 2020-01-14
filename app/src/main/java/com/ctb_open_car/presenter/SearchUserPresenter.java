package com.ctb_open_car.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ctb_open_car.bean.community.response.SearchUserData;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.SearchUserApi;
import com.ctb_open_car.ui.search.SearchUserView;
import com.ctb_open_car.view.SearchView;
import com.ctb_open_car.view.activity.search.SearchUserActivity;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class SearchUserPresenter {

    private SoftReference<SearchUserActivity> mActivity;
    private SearchUserView mView;

    public SearchUserPresenter(SearchUserActivity activity, SearchUserView view) {
        this.mActivity = new SoftReference<>(activity);
        this.mView = view;
    }

    /**
     * 动态点赞
     */
    public void seachUser(String searchKey) {

        SearchUserApi searchUserApi = new SearchUserApi(listener, (RxAppCompatActivity) mActivity.get());
        HashMap map = new HashMap();
        map.put("keyword", searchKey);

        searchUserApi.setRequestBody(map);
        HttpManager.getInstance().doHttpDeal(searchUserApi);
    }

    private HttpListener listener = new HttpListener() {
        @Override
        public void onNext(Object o) {
            super.onNext(o);
            BaseResultEntity<SearchUserData> objDao = (BaseResultEntity<SearchUserData>) o;
            if ("0".equals(objDao.getRet())) {
                mView.updateSearchData(objDao.getData().getUserCardList());
            } else {
                Toasty.warning(mActivity.get(), "查询异常").show();
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Timber.e(Log.getStackTraceString(e));
        }
    };
}
