package com.ctb_open_car.view.activity.im;

import android.content.Intent;
import android.os.Bundle;

import com.amap.api.maps.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ctb_open_car.MainActivity;
import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseActivity;
import com.ctb_open_car.bean.areacode.AreaCodeDtoBean;
import com.ctb_open_car.bean.im.CarModelBean;
import com.ctb_open_car.bean.im.GroupDetailsBean;
import com.ctb_open_car.bean.im.TagDtoBean;
import com.ctb_open_car.bean.im.TagListBean;
import com.ctb_open_car.eventbus.MessageEvent;
import com.ctb_open_car.presenter.im.CreateGroupPresenter;
import com.ctb_open_car.ui.im.CreateGroupView;
import com.ctb_open_car.utils.AliossUtils;
import com.ctb_open_car.view.activity.login.LoginActivity;
import com.ctb_open_car.view.dialog.CustomDialog;
import com.ctb_open_car.view.dialog.InputTextMsgDialog;
import com.ctb_open_car.view.dialog.RoundProgressDialog;
import com.rxretrofitlibrary.RxRetrofitApp;
import com.wildma.pictureselector.PictureSelector;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

public class CreateGroupActivity extends BaseActivity {

    private String mPicturePath;
    private RoundProgressDialog mRoundProgressDialog;

    private CreateGroupView mView;
    private CreateGroupPresenter mLoginPresenter;

    public RxRetrofitApp mRxInstance;
    public static final int REQUEST_CODE_CITY = 0x1001;
    public static final int REQUEST_CODE_LABLE = 0x1002;
    public static final int REQUEST_CODE_CARSYSTEM = 0x1003;

    public TagListBean mTagListBean;
    public String mTagIds;
    public CarModelBean mCarModelBean;
    public AreaCodeDtoBean mAreaCodeDtoBean;
    public GroupDetailsBean mGroupDetailsBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        mRxInstance = RxRetrofitApp.Singleton.INSTANCE.get();
        mGroupDetailsBean = (GroupDetailsBean)getIntent().getSerializableExtra("GroupDetailsBean");
        if (AliossUtils.getSingleton().getOss() == null) {
            AliossUtils.getSingleton().getAliStsToken(this);
        }

        mView = new CreateGroupView(this);
        mLoginPresenter = new CreateGroupPresenter(this, mView);
        mView.setCreateGroupPresenter(mLoginPresenter);
    }

    @Override
    public Object getTag() {
        return "创建车友群";
    }


    public void showDialog(String tips) {
        mRoundProgressDialog = RoundProgressDialog.newInstance(tips);
        mRoundProgressDialog.show(getSupportFragmentManager(), "dialog");
    }

    public void dismissDiaLog() {
        if (mRoundProgressDialog != null) {
            mRoundProgressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (mGroupDetailsBean != null) {
           super.onBackPressed();
        } else{
            CustomDialog dialog = new CustomDialog();
            dialog.setDialogOkText("以后再说");
            dialog.setDialogCancelText("继续创建");
            dialog.setDialogContentText("群主大人，确定要放弃创建车友群吗？");
            dialog.setOnClickListener(new CustomDialog.OnClickListener() {
                @Override
                public void setOkListener() {
                    finish();
                }

                @Override
                public void setCancelListener() {
                    dialog.dismiss();
                }
            });
            dialog.show(getSupportFragmentManager(), "dialog");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PictureSelector.SELECT_REQUEST_CODE:
                if (null != data) {
                    mPicturePath = data.getStringExtra(PictureSelector.PICTURE_PATH);
                    mView.setGroupAvatar(mPicturePath);
                }
                break;
            case REQUEST_CODE_CITY:
                if ( null != data) {
                    Bundle bundle = data.getBundleExtra("bundle");
                    mAreaCodeDtoBean = (AreaCodeDtoBean)bundle.getSerializable("select_info_city");
                    String privoiceName = bundle.getString("select_info_privoiceName");
                    mView.setCityName(privoiceName + "-" + mAreaCodeDtoBean.getAreaName());
                }
                break;
            case REQUEST_CODE_LABLE:
                if ( null != data) {
                    Bundle bundle = data.getBundleExtra("bundle");
                    mTagListBean = (TagListBean)bundle.getSerializable("select_info_lable");
                    StringBuilder tag = new StringBuilder();
                    StringBuilder response = new StringBuilder();
                    for (int i = 0; mTagListBean != null && i < mTagListBean.getTagList().size(); i ++) {
                        TagDtoBean tagDtoBean = mTagListBean.getTagList().get(i);
                        tag.append(" #");
                        tag.append(tagDtoBean.getTagName());
                        response.append(",");
                        response.append(String.valueOf(tagDtoBean.getTagId()));
                    }
                    response.append(",");
                    mTagIds = response.toString();
                    mView.setGroupTab(tag.toString());
                }
                break;
            case REQUEST_CODE_CARSYSTEM:
                if ( null != data) {
                    Bundle bundle = data.getBundleExtra("bundle");
                    mCarModelBean = (CarModelBean)bundle.getSerializable("select_info_car_system");
                    mView.setCarSystem(mCarModelBean.getShowName());
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getEventBusMsg(MessageEvent event) {
        if (event.getType() != null && "finish_activity".equals(event.getType())) {
            finish();
        }

        if (event.getType() != null && "transfer_group".equals(event.getType())) {
            finish();
        }
    }
}
