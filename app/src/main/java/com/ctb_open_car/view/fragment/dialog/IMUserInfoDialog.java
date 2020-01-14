package com.ctb_open_car.view.fragment.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.ctb_open_car.CTBApplication;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.community.response.UserData;
import com.ctb_open_car.bean.community.response.user.UserHomeDto;
import com.ctb_open_car.presenter.UserFollowPresenter;
import com.ctb_open_car.presenter.UserInfoPresenter;
import com.ctb_open_car.utils.Device;
import com.ctb_open_car.utils.PreferenceUtils;
import com.ctb_open_car.view.activity.person.PersonHomeActivity;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import java.lang.ref.SoftReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class IMUserInfoDialog extends DialogFragment {
    @BindView(R.id.im_user_avatar)
    ImageView mIMUserAvatar;

    @BindView(R.id.im_user_nick)
    TextView mImUserNick;

    @BindView(R.id.im_user_focus)
    TextView mImUserFocus;

    @BindView(R.id.im_user_followed)
    TextView mImUserFollow;

    @BindView(R.id.im_user_activity_count)
    TextView mUserActivityCount;

    @BindView(R.id.im_user_feed_count)
    TextView mUserFeedCount;

    @BindView(R.id.im_user_focus_count)
    TextView mUserFocusCount;

    @BindView(R.id.im_action_kick)
    TextView mUserKick;

    @BindView(R.id.im_action_focus)
    TextView mUserFocus;

    UserInfoPresenter mUserInfoPresenter;
    UserFollowPresenter mUserFollowPresenter;
    private SoftReference<AppCompatActivity> mActivity;
    private String mUserId;
    private String mGroupId;
    private String mEmUserId;
    private String mNickName;

    public IMUserInfoDialog(AppCompatActivity compatActivity) {
        this.mActivity = new SoftReference<>(compatActivity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.TopDialog);
        /**获取参数*/
        Bundle param = getArguments();
        mEmUserId = param.getString("em_user_id");
        mUserId = param.getString("user_id");
        mGroupId = param.getString("group_id");
        mNickName = param.getString("nick_name");
        initRequest();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v = LayoutInflater.from(mActivity.get()).inflate(R.layout.chat_user_info_dialog, null);
        ButterKnife.bind(this, v);

        initView();
        return v;
    }


    private void initView() {
        String ownerId = EMClient.getInstance().groupManager().getGroup(mGroupId).getOwner();

        if (EMClient.getInstance().getCurrentUser().equals(ownerId)) {
            mUserKick.setVisibility(View.VISIBLE);
        }
        if (mUserId.equals(String.valueOf(PreferenceUtils.getLong(CTBApplication.getInstance(), "userId")))) {
            mUserFocus.setVisibility(View.GONE);
            mUserKick.setVisibility(View.GONE);
        }

    }

    private void initRequest() {
        mUserInfoPresenter = new UserInfoPresenter(mActivity.get());
        mUserInfoPresenter.requestUserInfo(mUserId, new UserInfoPresenter.UpdateListener() {
            @Override
            public void updateUserInfo(UserData userData) {
                loadData(userData);
            }
        });
        mUserFollowPresenter = new UserFollowPresenter(mActivity.get(), new UserFollowPresenter.FollowCallback() {
            @Override
            public void actionCancelSuccess() {
                Toasty.info(mActivity.get(), "已取消关注").show();
                mUserFocus.setText("关注");

                dismiss();
            }

            @Override
            public void actionFollowSuccess() {
                Toasty.info(mActivity.get(), "关注成功").show();
                mUserFocus.setText("取消关注");

                dismiss();
            }
        });
    }

    private void loadData(UserData userData) {
        UserHomeDto userHomeDto = userData.getUserHome();
        Glide.with(mActivity.get()).load(userHomeDto.getUserIcon().getResourceUrl())
                .placeholder(R.drawable.avatar)
                .transform(new RoundedCorners(Device.dip2px(45)))
                .into(mIMUserAvatar);
        mImUserNick.setText(userHomeDto.nickName);
        mImUserFocus.setText("关注：" + userHomeDto.getUserStat().getAttentionCnt());
        mImUserFollow.setText("粉丝：" + userHomeDto.getUserStat().getFansCnt());
        mUserActivityCount.setText(userHomeDto.getUserStat().getActivityCnt() + "");
        mUserFeedCount.setText(userHomeDto.getUserStat().getFeedCnt() + "");
        mUserFocusCount.setText(userHomeDto.getUserStat().getAttentionCnt() + "");
        if (0 == userHomeDto.getRelationStatus()) {
            mUserFocus.setText("关注");
        } else {
            mUserFocus.setText("取消关注");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();

        windowParams.width = Device.getScreenWidth() - Device.dip2px(50);

        window.setAttributes(windowParams);
    }

    @OnClick(R.id.im_action_page)
    void toUserHost(View v) {
        Intent intent = new Intent(mActivity.get(), PersonHomeActivity.class);
        intent.putExtra("user_id", Long.parseLong(mUserId));
        mActivity.get().startActivity(intent);
        dismiss();
    }

    @OnClick(R.id.im_action_focus)
    void toFocusUser(View v) {
        if (mUserFocus.getText().toString().contains("取消")) {
            mUserFollowPresenter.userCancelFollow(mUserId);
        } else {
            mUserFollowPresenter.userFollow(mUserId);
        }
    }

    @OnClick(R.id.im_action_kick)
    void toKickUser(View v) {
        EMClient.getInstance().groupManager().asyncRemoveUserFromGroup(mGroupId, mEmUserId, new EMCallBack() {
            @Override
            public void onSuccess() {
                mActivity.get().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toasty.info(mActivity.get(), "已移除用户：" + mNickName).show();
                    }
                });
            }

            @Override
            public void onError(int code, String error) {
                mActivity.get().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toasty.error(mActivity.get(), "移除失败:[" + code + "] " + error).show();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
        dismiss();
    }
}
