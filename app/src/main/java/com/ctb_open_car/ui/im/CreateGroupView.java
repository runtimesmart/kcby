package com.ctb_open_car.ui.im;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.im.TagDtoBean;
import com.ctb_open_car.bean.im.TagListBean;
import com.ctb_open_car.presenter.LoginPresenter;
import com.ctb_open_car.presenter.im.CreateGroupPresenter;
import com.ctb_open_car.view.activity.im.AddGroupInfoActivity;
import com.ctb_open_car.view.activity.im.CreateGroupActivity;
import com.ctb_open_car.view.activity.im.GroupSettingsActivity;
import com.ctb_open_car.view.activity.login.LoginActivity;
import com.rxretrofitlibrary.RxRetrofitApp;
import com.wildma.pictureselector.PictureSelector;

import java.io.File;
import java.lang.ref.SoftReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

public class CreateGroupView {
    @BindView(R.id.statusbar_title)
    TextView mStatusbarTitle;
    @BindView(R.id.statusbar)
    RelativeLayout mStatusbar;
    @BindView(R.id.group_avatar)
    ImageView mGroupAvatar;
    @BindView(R.id.group_name_edit)
    EditText mGroupNameEdit;
    @BindView(R.id.group_content_edit)
    EditText mGroupContentEdit;
    @BindView(R.id.input_num)
    TextView mInputNum;
    @BindView(R.id.city_name)
    TextView mCityName;
    @BindView(R.id.select_car_system)
    TextView mSelectCarSystem;
    @BindView(R.id.select_group_tab)
    TextView mSelectGroupTab;
    @BindView(R.id.group_rule_edit)
    EditText mGroupRuleEdit;
    @BindView(R.id.group_rule_input_num)
    TextView mGroupRuleInputNum;

    @BindView(R.id.edit_group_info)
    View mViewEditGroupInfo;
    @BindView(R.id.statusbar_right)
    TextView mStatusbarRight;

    private final SoftReference<CreateGroupActivity> mActivity;
    private Unbinder unbinder;
    private CreateGroupPresenter mCreateGroupPresenter;
    private int mLoginType = 1; // 1-手机号登录；2-微信登录
    private RxRetrofitApp sRxInstance;
    private long mUserId;
    private String mPicturePath;

    @OnClick({R.id.ic_back, R.id.statusbar_right, R.id.group_avatar, R.id.city_lay, R.id.car_system_lay, R.id.group_tab_lay, R.id.btn_save, R.id.group_management})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ic_back:
                mActivity.get().onBackPressed();
                break;
            case R.id.statusbar_right:
                if (TextUtils.isEmpty(mGroupNameEdit.getText().toString()) || mGroupNameEdit.length() < 2) {
                    Toasty.info(mActivity.get(), "群名称必须大于2个字符").show();
                } else if (TextUtils.isEmpty(mGroupContentEdit.getText().toString()) || mGroupContentEdit.length() < 4) {
                    Toasty.info(mActivity.get(), "群介绍必须大于4个字符").show();
                } else if (TextUtils.isEmpty(mPicturePath)) {
                    Toasty.info(mActivity.get(), "请添加群头像").show();
                } else {
                    mActivity.get().showDialog("正在创建...");
                    mCreateGroupPresenter.createGroup(mPicturePath, mGroupNameEdit.getText().toString(), mGroupContentEdit.getText().toString(),
                            mGroupRuleEdit.getText().toString());
                }
                break;
            case R.id.group_avatar:
                File file = new File("storage/emulated/0/PictureSelector.temp.jpg");
                if (file != null && file.exists()){
                    file.delete();
                }

                PictureSelector.create(mActivity.get(), PictureSelector.SELECT_REQUEST_CODE)
                        .selectPicture(true, 200, 200, 1, 1);
                break;
            case R.id.city_lay:
                intent = new Intent(mActivity.get(), AddGroupInfoActivity.class);
                intent.putExtra("select_info_type", 1);
                mActivity.get().startActivityForResult(intent, mActivity.get().REQUEST_CODE_CITY);
                break;
            case R.id.car_system_lay:
                intent = new Intent(mActivity.get(), AddGroupInfoActivity.class);
                intent.putExtra("select_info_type", 3);
                mActivity.get().startActivityForResult(intent, mActivity.get().REQUEST_CODE_CARSYSTEM);
                break;
            case R.id.group_tab_lay:
                intent = new Intent(mActivity.get(), AddGroupInfoActivity.class);
                intent.putExtra("select_info_lable", mActivity.get().mTagListBean);
                intent.putExtra("select_info_type", 2);
                mActivity.get().startActivityForResult(intent, mActivity.get().REQUEST_CODE_LABLE);
                break;
            case R.id.btn_save:
                mActivity.get().showDialog("正在创建...");
                mCreateGroupPresenter.updataGroup(mPicturePath, mGroupNameEdit.getText().toString(), mGroupContentEdit.getText().toString(),
                            mGroupRuleEdit.getText().toString());
                break;
            case R.id.group_management:
                intent = new Intent(mActivity.get(), GroupSettingsActivity.class);
                intent.putExtra("group_id", mActivity.get().mGroupDetailsBean.getGroupDetail().getGroupId());
                intent.putExtra("is_host", true);
                mActivity.get().startActivity(intent);
                break;
        }
    }
    public void initData() {
        mGroupContentEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mInputNum.setText(s.length()+"/200");
            }
        });

        mGroupRuleEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mGroupRuleInputNum.setText(s.length()+"/200");
            }
        });

        if (mActivity.get().mGroupDetailsBean != null) {
            editGroupInfo();
        }
    }

    public CreateGroupView(final CreateGroupActivity activity) {
        mActivity = new SoftReference<>(activity);
        unbinder = ButterKnife.bind(this, mActivity.get());
        sRxInstance = RxRetrofitApp.Singleton.INSTANCE.get();
        initData();
    }
    public void setCreateGroupPresenter(CreateGroupPresenter loginPresenter) {
        mCreateGroupPresenter = loginPresenter;
    }

    public void editGroupInfo() {
        mStatusbarTitle.setText("修改群资料");
        mViewEditGroupInfo.setVisibility(View.VISIBLE);
        mStatusbarRight.setVisibility(View.INVISIBLE);
        Glide.with(mActivity.get()).asBitmap().circleCrop().load(mActivity.get().mGroupDetailsBean.getGroupDetail().getGroupIcon().getResourceUrl()).placeholder( R.drawable.icon_group).error(R.drawable.icon_group).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                mGroupAvatar.setImageBitmap(resource);

            }
        });

        mGroupNameEdit.setText(mActivity.get().mGroupDetailsBean.getGroupDetail().getGroupName());
        if (mActivity.get().mGroupDetailsBean.getGroupDetail().getAreaCode() != null) {
            mCityName.setText(mActivity.get().mGroupDetailsBean.getGroupDetail().getAreaCode().getAreaName());
        }
        if (mActivity.get().mGroupDetailsBean.getGroupDetail().getCarModelId() != -1) {
            mSelectCarSystem.setText(mActivity.get().mGroupDetailsBean.getGroupDetail().getCarModelName());
        }

        StringBuilder tag = new StringBuilder();
        StringBuilder response = new StringBuilder();
        if (mActivity.get().mGroupDetailsBean.getGroupDetail().getTagList() != null) {
            for (TagDtoBean tagDtoBean : mActivity.get().mGroupDetailsBean.getGroupDetail().getTagList()) {
                tag.append(" #");
                tag.append(tagDtoBean.getTagName());

                response.append(",");
                response.append(String.valueOf(tagDtoBean.getTagId()));

            }

            if (mActivity.get().mTagListBean == null) {
                mActivity.get().mTagListBean = new TagListBean();
            }
            mActivity.get().mTagListBean.setTagList(mActivity.get().mGroupDetailsBean.getGroupDetail().getTagList());
            response.append(",");
            mActivity.get().mTagIds = response.toString();
            mSelectGroupTab.setText(tag);
        }
        mGroupRuleEdit.setText(mActivity.get().mGroupDetailsBean.getGroupDetail().getGroupRule());
        mGroupContentEdit.setText(mActivity.get().mGroupDetailsBean.getGroupDetail().getGroupDesc());
    }

    public void setGroupAvatar(String imageUrl) {
        this.mPicturePath = imageUrl;
        Glide.with(mActivity.get()).asBitmap().circleCrop().load(mPicturePath).skipMemoryCache(true) // 不使用内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE).into(mGroupAvatar);
    }

    public void setCityName(String cityName) {
        mCityName.setText(cityName);
    }

    public void setGroupTab(String groupTab){
        mSelectGroupTab.setText(groupTab);
    }

    public void setCarSystem(String carSystem){
        mSelectCarSystem.setText(carSystem);
    }
}
