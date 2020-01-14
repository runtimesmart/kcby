package com.ctb_open_car.view.activity.im;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ctb_open_car.CTBApplication;
import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseActivity;
import com.ctb_open_car.bean.im.GroupDetailsBean;
import com.ctb_open_car.bean.im.TagDtoBean;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.ImGroupDetailsApi;
import com.ctb_open_car.eventbus.MessageEvent;
import com.ctb_open_car.utils.GaussianBlurBitmap;
import com.ctb_open_car.utils.PreferenceUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMucSharedFile;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class ImGroupInfoActivity extends BaseActivity implements EMGroupChangeListener {

    @BindView(R.id.cover_image)
    ImageView mCoverImage;
    @BindView(R.id.group_cover_image)
    ImageView mGroupCoverImage;
    @BindView(R.id.group_name)
    TextView mGroupName;
    @BindView(R.id.group_id)
    TextView mGroupId;
    @BindView(R.id.city_name)
    TextView mCityName;
    @BindView(R.id.select_car_system)
    TextView mCarSystem;
    @BindView(R.id.select_group_tab)
    TextView mGroupTab;
    @BindView(R.id.group_rule_edit)
    TextView mGroupRuleEdit;
    @BindView(R.id.group_user_num)
    TextView mGroupUserNum;
    @BindView(R.id.group_info)
    TextView mGroupInfo;
    @BindView(R.id.btn_login)
    Button mBtnLogin;

    @BindView(R.id.group_rule_lay)
    View mGroupRuleLay;
    @BindView(R.id.group_tab_lay)
    View mGroupTabLay;
    @BindView(R.id.car_system_lay)
    View mCarSystemLay;
    @BindView(R.id.city_lay)
    View mCityLay;
    @BindView(R.id.group_data_management)
    View mGroupDataManagement;
    @BindView(R.id.group_setting)
    View mGroupSeting;

    private GroupDetailsBean mGroupDetailsBean;
    private String mStrGroupId;

    @Override
    public Object getTag() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_group_info);
        //   getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //   getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        EMClient.getInstance().groupManager().addGroupChangeListener(this);

        mStrGroupId = (String) getIntent().getStringExtra("group_id");

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(mStrGroupId)) {
            getGroupDetails();
        } else {
            Toasty.info(this, "该群不存在").show();
        }
    }

    public void getGroupDetails() {
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("groupId", mStrGroupId);
        ImGroupDetailsApi myInfoApi = new ImGroupDetailsApi(new HttpListener() {
            @Override
            public void onNext(Object object) {
                super.onNext(object);
                BaseResultEntity<GroupDetailsBean> baseResultEntity = (BaseResultEntity<GroupDetailsBean>) object;
                if (baseResultEntity.getRet().equals("0")) {
                    mGroupDetailsBean = baseResultEntity.getData();
                    if (mGroupDetailsBean != null && mGroupDetailsBean.getGroupDetail() != null) {
                        initData();
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                Timber.e("e = %s", e.getMessage());
            }
        }, this);
        myInfoApi.setRequestBody(queryMap);
        HttpManager.getInstance().doHttpDeal(myInfoApi);
    }

    public void initData() {
       if (mGroupDetailsBean.getGroupDetail().getOwnerId().userId == PreferenceUtils.getLong(CTBApplication.getInstance(), "userId")) {
           mGroupDataManagement.setVisibility(View.VISIBLE);
           mGroupSeting.setVisibility(View.GONE);
           mBtnLogin.setVisibility(View.GONE);
       } else {
           mGroupDataManagement.setVisibility(View.GONE);
           if (mGroupDetailsBean.isExsited()) {
               mBtnLogin.setVisibility(View.GONE);
               mGroupSeting.setVisibility(View.VISIBLE);
           } else {
               mGroupSeting.setVisibility(View.GONE);
           }

       }

        Glide.with(this).asBitmap().circleCrop().load(mGroupDetailsBean.getGroupDetail().getGroupIcon().getResourceUrl()).placeholder( R.drawable.default_avar_icon).error(R.drawable.default_avar_icon).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                mGroupCoverImage.setImageBitmap(resource);
            }
        });

        Glide.with(this).asBitmap().load(mGroupDetailsBean.getGroupDetail().getGroupIcon().getResourceUrl()).placeholder(R.drawable.icon_group).error(R.drawable.default_avar_icon).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                mCoverImage.setImageBitmap(GaussianBlurBitmap.blurBitmap(resource, 20));
            }
        });

        mGroupName.setText(mGroupDetailsBean.getGroupDetail().getGroupName());
        mGroupId.setText(String.format("ID: %s", mGroupDetailsBean.getGroupDetail().getGroupId()));
        if (mGroupDetailsBean.getGroupDetail().getAreaCode() != null) {
            mCityName.setText(mGroupDetailsBean.getGroupDetail().getAreaCode().getAreaName());
        }
        if (mGroupDetailsBean.getGroupDetail().getCarModelId() != -1) {
            mCarSystem.setText(mGroupDetailsBean.getGroupDetail().getCarModelName());
        }

        StringBuilder tag = new StringBuilder();
        if (mGroupDetailsBean.getGroupDetail().getTagList() != null) {
            for (TagDtoBean tagDtoBean : mGroupDetailsBean.getGroupDetail().getTagList()) {
                tag.append(" #");
                tag.append(tagDtoBean.getTagName());

            }
            mGroupTab.setText(tag);
        }

        mGroupRuleEdit.setText(mGroupDetailsBean.getGroupDetail().getGroupRule());
        mGroupUserNum.setText(String.valueOf(mGroupDetailsBean.getGroupDetail().getMemberCnt()));
        mGroupInfo.setText(mGroupDetailsBean.getGroupDetail().getGroupDesc());

        if (mGroupDetailsBean.getGroupDetail().getMemberCnt() == 2000) {
            mBtnLogin.setEnabled(false);
            mBtnLogin.setBackgroundResource(R.drawable.button_enable_bg);
            mBtnLogin.setText("本群已满");
        }
        setLayoutView();
    }

    @OnClick({R.id.group_user_lay, R.id.btn_login, R.id.ic_back, R.id.group_data_management})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.group_user_lay:
                intent = new Intent(this, ImGroupMemberListActivity.class);
                intent.putExtra("group_id", mStrGroupId);
                intent.putExtra("GroupUserNum", mGroupUserNum.getText().toString());
                startActivity(intent);
                break;
            case R.id.btn_login:
                EMClient.getInstance().groupManager().asyncJoinGroup(mStrGroupId, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        mGroupDataManagement.setVisibility(View.GONE);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toasty.info(ImGroupInfoActivity.this, "加群成功").show();
                            }
                        });
                        Intent chatIntent = new Intent(ImGroupInfoActivity.this, ChatGroupActivity.class);
                        chatIntent.putExtra("group_id", mStrGroupId);
                        startActivity(chatIntent);
                    }

                    @Override
                    public void onError(int code, String error) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toasty.info(ImGroupInfoActivity.this, "加群失败" + error).show();
                            }
                        });
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }
                });

                break;
            case R.id.ic_back:
                onBackPressed();
                break;
            case R.id.group_data_management:
                intent = new Intent(this, CreateGroupActivity.class);
                intent.putExtra("GroupDetailsBean", mGroupDetailsBean);
                startActivity(intent);
                break;
        }
    }

    public void setLayoutView() {
        if (TextUtils.isEmpty(mGroupRuleEdit.getText().toString())) {
            mGroupRuleLay.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(mGroupTab.getText().toString())) {
            mGroupTabLay.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(mCarSystem.getText().toString())) {
            mCarSystemLay.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(mCityName.getText().toString())) {
            mCityLay.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.group_setting)
    void toSettings(View v) {
        Intent i = new Intent(this, GroupSettingsActivity.class);
        boolean isHost = false;
        if (null == mGroupDetailsBean) {
            Toasty.info(this, "请稍等").show();
            return;
        }

        i.putExtra("group_id", mStrGroupId);
        i.putExtra("is_host", isHost);
        startActivity(i);
    }

    @Override
    public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {

    }

    @Override
    public void onRequestToJoinReceived(String groupId, String groupName, String applicant, String reason) {

    }

    @Override
    public void onRequestToJoinAccepted(String groupId, String groupName, String accepter) {

    }

    @Override
    public void onRequestToJoinDeclined(String groupId, String groupName, String decliner, String reason) {

    }

    @Override
    public void onInvitationAccepted(String groupId, String invitee, String reason) {

    }

    @Override
    public void onInvitationDeclined(String groupId, String invitee, String reason) {

    }

    @Override
    public void onUserRemoved(String groupId, String groupName) {

    }

    @Override
    public void onGroupDestroyed(String groupId, String groupName) {
        this.finish();
    }

    @Override
    public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {

    }

    @Override
    public void onMuteListAdded(String groupId, List<String> mutes, long muteExpire) {

    }

    @Override
    public void onMuteListRemoved(String groupId, List<String> mutes) {

    }

    @Override
    public void onAdminAdded(String groupId, String administrator) {

    }

    @Override
    public void onAdminRemoved(String groupId, String administrator) {

    }

    @Override
    public void onOwnerChanged(String groupId, String newOwner, String oldOwner) {
        this.finish();
    }

    @Override
    public void onMemberJoined(String groupId, String member) {

    }

    @Override
    public void onMemberExited(String groupId, String member) {

    }

    @Override
    public void onAnnouncementChanged(String groupId, String announcement) {

    }

    @Override
    public void onSharedFileAdded(String groupId, EMMucSharedFile sharedFile) {

    }

    @Override
    public void onSharedFileDeleted(String groupId, String fileId) {

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
