package com.ctb_open_car.presenter;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ctb_open_car.bean.community.response.RecommendData;
import com.ctb_open_car.bean.community.response.group.GroupDto;
import com.ctb_open_car.bean.im.EmchatGroupMemberDto;
import com.ctb_open_car.bean.im.GroupMemberListBean;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.ImGroupUserListApi;
import com.ctb_open_car.engine.net.api.RecommendListApi;
import com.ctb_open_car.view.activity.im.ImGroupMemberListActivity;
import com.ctb_open_car.view.fragment.comminity.ExpertsFragment;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class ImGroupMembersPresenter {

    private SoftReference<AppCompatActivity> mActivity;
    private UpdateListener mUpdateListener;

    @Inject
    public ImGroupMembersPresenter(AppCompatActivity activity) {
        this.mActivity = new SoftReference<>(activity);
    }

    public void requestRecommendList(String groupId, UpdateListener updateListener) {
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("groupId", groupId);
        ImGroupUserListApi groupMemberApi = new ImGroupUserListApi(listener, null);
        groupMemberApi.setRequestBody(queryMap);
        HttpManager.getInstance().doHttpDeal(groupMemberApi);
        this.mUpdateListener = updateListener;
    }


    private HttpListener listener = new HttpListener() {
        @Override
        public void onNext(Object o) {
            BaseResultEntity<Object> objDao = (BaseResultEntity<Object>) o;

            if (objDao.getRet().equals("-4")) {
                mUpdateListener.logout();
            }
            super.onNext(o);

            Object obj = objDao.getData();
            if (null != obj) {
                if (obj instanceof GroupMemberListBean) {
                    //本人信息
                    GroupMemberListBean groupMemberListBean = (GroupMemberListBean) obj;
                    if (null == groupMemberListBean
                            || null == groupMemberListBean.getGroupMemberList()
                            || 0 == groupMemberListBean.getGroupMemberList().size()) {
                    } else {
                        mUpdateListener.update(groupMemberListBean.getGroupMemberList());
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
        void update(List<EmchatGroupMemberDto> groupMemberList);
        void logout();
    }
}
