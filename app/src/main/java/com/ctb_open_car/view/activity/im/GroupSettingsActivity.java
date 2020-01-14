package com.ctb_open_car.view.activity.im;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseActivity;
import com.ctb_open_car.eventbus.MessageEvent;
import com.ctb_open_car.presenter.im.ChangeOwnerPresenter;
import com.ctb_open_car.presenter.im.RemoveGroupPresenter;
import com.ctb_open_car.view.dialog.CustomDialog;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.widget.EaseTitleBar;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class GroupSettingsActivity extends BaseActivity {
    private static final int CODE_MEMBER_SELECT = 19;

    @BindView(R.id.group_setting_title)
    EaseTitleBar mGroupTitle;

    @BindView(R.id.group_action_1)
    TextView mGroupAction1;

    @BindView(R.id.group_action_2)
    TextView mGroupAction2;

    private String mGroupId;
    private boolean mIsHost;
    private CustomDialog mDialog;
    @Inject
    RemoveGroupPresenter mRemoveGroupPresenter;

    @Inject
    ChangeOwnerPresenter mChangeOwnerPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_settings);
        getActivityComponent().inject(this);
        Intent intent = getIntent();
        mGroupId = intent.getStringExtra("group_id");
        mIsHost = intent.getBooleanExtra("is_host", false);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mGroupTitle.setTitle("群管理");
        if (mIsHost) {
            mGroupAction1.setText("群主转让");
            mGroupAction2.setText("解散本群");
        } else {
            mGroupAction1.setText("举报群聊");
            mGroupAction2.setText("退出群聊");
        }
        mGroupTitle.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @OnClick(R.id.group_action_1)
    void action1Click(View v) {
        if (mIsHost) {
            Intent intent = new Intent(this, ImGroupMemberListActivity.class);
            intent.putExtra("group_id", mGroupId);
            intent.putExtra("is_host", mIsHost);
            startActivityForResult(intent, 1);
        } else {
            /**举报群聊*/
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (CODE_MEMBER_SELECT == resultCode) {
            if (null != data) {
                String memberId = data.getStringExtra("member_id");
                String memberName = data.getStringExtra("member_name");

                if (mDialog == null) {
                    mDialog = new CustomDialog();
                }
                mDialog.setDialogOkText("确定");
                mDialog.setDialogCancelText("取消");
                mDialog.setDialogContentText(memberName + "即将成为该群群主，确定后你将立即失去群主身份");
                mDialog.setOnClickListener(new CustomDialog.OnClickListener() {
                    @Override
                    public void setOkListener() {
                        changeGroupOwner(memberId, memberName);
                        mDialog.dismiss();
                    }

                    @Override
                    public void setCancelListener() {
                        mDialog.dismiss();
                    }
                });
                mDialog.show(getSupportFragmentManager(), "dialog");

            }
        }
    }

    /**
     * 群主转让
     */
    private void changeGroupOwner(String memberId, String memberName) {
        mChangeOwnerPresenter.changeGroupOwnerById(mGroupId, memberId, new ChangeOwnerPresenter.UpdateListener() {
            @Override
            public void update(int result) {
                if (1 == result) {
                    Toasty.info(GroupSettingsActivity.this, "已转让给" + memberName).show();
                    MessageEvent messageEvent = new MessageEvent();
                    messageEvent.setType("transfer_group");
                    EventBus.getDefault().post(messageEvent);
                    GroupSettingsActivity.this.finish();
                }
            }
        });
    }

    @OnClick(R.id.group_action_2)
    void action2Click(View v) {
        if (mIsHost) {
            /**解散本群*/
            if (mDialog == null) {
                mDialog = new CustomDialog();
            }
            mDialog.setOnClickListener(new CustomDialog.OnClickListener() {
                @Override
                public void setOkListener() {
                    mRemoveGroupPresenter.removeGroupById(mGroupId);
                    mDialog.dismiss();

                }

                @Override
                public void setCancelListener() {
                    mDialog.dismiss();
                }
            });
            mDialog.show(getSupportFragmentManager(), "dialog");

        } else {
            /**退出群聊*/
            if (mDialog == null) {
                mDialog = new CustomDialog();
            }
            mDialog.setDialogOkText("确定退出");
            mDialog.setDialogCancelText("暂不退出");
            mDialog.setDialogContentText("相遇不易，确定要退出群吗?");
            mDialog.setOnClickListener(new CustomDialog.OnClickListener() {
                @Override
                public void setOkListener() {
                    goOutGroupIM();
                    mDialog.dismiss();
                }

                @Override
                public void setCancelListener() {
                    mDialog.dismiss();
                }
            });
            mDialog.show(getSupportFragmentManager(), "dialog");
        }
    }

    /**退出群聊*/
    public void goOutGroupIM() {
        mDialog.show(getSupportFragmentManager(), "dialog");
        EMClient.getInstance().groupManager().asyncLeaveGroup(mGroupId, new EMCallBack() {
            @Override
            public void onSuccess() {
                MessageEvent messageEvent = new MessageEvent();
                messageEvent.setType("finish_activity");
                EventBus.getDefault().post(messageEvent);
                GroupSettingsActivity.this.finish();
            }

            @Override
            public void onError(int code, String error) {
                Timber.e("error = %s", error);
                Timber.e(error);
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }
    @Override
    public Object getTag() {
        return null;
    }

}
