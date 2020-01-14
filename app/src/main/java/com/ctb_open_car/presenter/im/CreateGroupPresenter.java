package com.ctb_open_car.presenter.im;


import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ctb_open_car.CTBApplication;
import com.ctb_open_car.base.CTBBaseApplication;
import com.ctb_open_car.bean.login.UserBean;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.CreateImGroupApi;
import com.ctb_open_car.engine.net.api.LoginApi;
import com.ctb_open_car.engine.net.api.SendValidateCodeApi;
import com.ctb_open_car.engine.net.api.UpdateImGroupInfoApi;
import com.ctb_open_car.ui.im.CreateGroupView;
import com.ctb_open_car.ui.login.LoginView;
import com.ctb_open_car.utils.AliossUtils;
import com.ctb_open_car.utils.Device;
import com.ctb_open_car.utils.PreferenceUtils;
import com.ctb_open_car.view.activity.im.CreateGroupActivity;
import com.ctb_open_car.view.activity.login.LoginActivity;
import com.google.gson.JsonObject;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.RxRetrofitApp;
import com.rxretrofitlibrary.http.HttpManager;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;
import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

import static com.ctb_open_car.utils.StringUtils.generateUUID;
import static com.ctb_open_car.utils.StringUtils.getFileSuffix;


public class CreateGroupPresenter {
    private SoftReference<CreateGroupActivity> mContext;
    private CreateGroupView mLoginView;
    private RxRetrofitApp mRxInstance;
    private String mPicturePath;
    private String mObjectKey;
    public CreateGroupPresenter(CreateGroupActivity c,CreateGroupView v) {
        mLoginView = v;
        mContext = new SoftReference<>(c);
        mRxInstance = RxRetrofitApp.Singleton.INSTANCE.get();
    }

    public void createImGroup(String groupName, String groupDesc, String groupRule){
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("groupName",groupName);
        queryMap.put("groupIcon", mObjectKey);
        queryMap.put("groupDesc", groupDesc);
        if (!TextUtils.isEmpty(groupRule)) {
            queryMap.put("groupRule", groupRule);
        }
        if (mContext.get().mAreaCodeDtoBean != null) {
            queryMap.put("cityCode", mContext.get().mAreaCodeDtoBean.getAreaCode());
        }
        if (!TextUtils.isEmpty(mContext.get().mTagIds)) {
            queryMap.put("tagIds", mContext.get().mTagIds);
        }
        if (mContext.get().mCarModelBean != null && mContext.get().mCarModelBean.getBrandDetailId() > 0) {
            queryMap.put("carModelId", mContext.get().mCarModelBean.getBrandDetailId());
        }

        CreateImGroupApi myInfoApi = new CreateImGroupApi(new HttpListener() {
            @Override
            public void onNext(Object object) {
                super.onNext(object);
                BaseResultEntity baseResultEntity = (BaseResultEntity) object;
                if (baseResultEntity.getRet().equals("0")) {
                    // String aa  = (String)baseResultEntity.getData();
                    mContext.get().dismissDiaLog();
                    Toasty.info(mContext.get(), "车友群 创建成功").show();
                    mContext.get().finish();
                } else {
                    mContext.get().dismissDiaLog();
                    Toasty.info(mContext.get(), baseResultEntity.getMsg()).show();
                }
            }

            @Override
            public void onError(Throwable e) {
                mContext.get().dismissDiaLog();
                Toasty.info(mContext.get(), "车友群 创建失败").show();
                Timber.e("e = " + e.getMessage());
            }
        }, mContext.get());
        myInfoApi.setRequestBody(queryMap);
        HttpManager.getInstance().doHttpDeal(myInfoApi);
    }

    public class MyThread extends Thread {
        String mGroupName;
        String mGroupDesc;
        String mGroupRule;
        int mType;  //1-创建群；2-更新群资料

        MyThread(String groupName, String groupDesc, String groupRule,int type){
             mGroupName = groupName;
             mGroupDesc = groupDesc;
             mGroupRule = groupRule;
             mType = type;
        }
        @Override
        public void run() {
            Calendar calendar = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String strDate = sdf.format(calendar.getTime());

            String filePath = mPicturePath;
            if (!TextUtils.isEmpty(filePath)) {
                mObjectKey = "ctb/opencar/image/emchat/group/icon/" + strDate + File.separator + generateUUID() + "." + getFileSuffix(filePath);
                String path = AliossUtils.getSingleton().updateImage(mObjectKey, filePath);
                Log.e("xxx", "path = " + path);
                if (!TextUtils.isEmpty(path)) {
                    mContext.get().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(mType == 1) {
                                createImGroup(mGroupName, mGroupDesc, mGroupRule);
                            } else {
                                updataGroupInfo(mGroupName, mGroupDesc, mGroupRule);
                            }
                        }
                    });
                } else {
                    mContext.get().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mContext.get().dismissDiaLog();
                            Toasty.info(mContext.get(), "数据保存失败").show();
                        }
                    });
                }
            } else {
                mContext.get().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mContext.get().dismissDiaLog();
                        Toasty.info(mContext.get(), "请添加群头像").show();
                    }
                });

            }
        }
    }

    public void updataGroupInfo(String groupName, String groupDesc, String groupRule) {
         HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("groupName", groupName);
        queryMap.put("groupId", mContext.get().mGroupDetailsBean.getGroupDetail().getGroupId());

        if (TextUtils.isEmpty(mObjectKey)) {
            queryMap.put("groupIcon", mContext.get().mGroupDetailsBean.getGroupDetail().getGroupIcon().getResourceUrl());
        } else {
            queryMap.put("groupIcon", mObjectKey);
        }
        queryMap.put("groupDesc", groupDesc);
        if (!TextUtils.isEmpty(groupRule)) {
            queryMap.put("groupRule", groupRule);
        }
        if (mContext.get().mAreaCodeDtoBean != null) {
            queryMap.put("cityCode", mContext.get().mAreaCodeDtoBean.getAreaCode());
        }
        if (!TextUtils.isEmpty(mContext.get().mTagIds)) {
            queryMap.put("tagIds", mContext.get().mTagIds);
        }
        if (mContext.get().mCarModelBean != null && mContext.get().mCarModelBean.getBrandDetailId() > 0) {
            queryMap.put("carModelId", mContext.get().mCarModelBean.getBrandDetailId());
        }
        UpdateImGroupInfoApi myInfoApi = new UpdateImGroupInfoApi(new HttpListener() {
            @Override
            public void onNext(Object object) {
                super.onNext(object);
                BaseResultEntity baseResultEntity = (BaseResultEntity) object;
                if (baseResultEntity.getRet().equals("0")) {
                    // String aa  = (String)baseResultEntity.getData();
                    mContext.get().dismissDiaLog();
                    Toasty.info(mContext.get(), "群资料修改成功").show();
                    mContext.get().finish();
                } else {
                    mContext.get().dismissDiaLog();
                    Toasty.info(mContext.get(), baseResultEntity.getMsg()).show();
                }
            }

            @Override
            public void onError(Throwable e) {
                mContext.get().dismissDiaLog();
                Toasty.info(mContext.get(), "群资料修改失败").show();
                Timber.e("e = " + e.getMessage());
            }
        }, mContext.get());
        myInfoApi.setRequestBody(queryMap);
        HttpManager.getInstance().doHttpDeal(myInfoApi);
    }

    public void createGroup(String picturePath,String groupName, String groupDesc, String groupRule) {
        mPicturePath = picturePath;
        new MyThread(groupName, groupDesc, groupRule, 1).start();
    }

    public void updataGroup(String picturePath, String groupName, String groupDesc, String groupRule) {
        if (TextUtils.isEmpty(picturePath))  {
            updataGroupInfo(groupName, groupDesc, groupRule);
        } else {
            mPicturePath = picturePath;
            new MyThread(groupName, groupDesc, groupRule, 2).start();
        }
    }
}
