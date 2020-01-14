package com.ctb_open_car.view.activity.person;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseActivity;
import com.ctb_open_car.bean.releasedynamics.ReleaseDynamics;
import com.ctb_open_car.bean.userInfo.UserInfoBean;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.PublishDetailApi;
import com.ctb_open_car.engine.net.api.UserInfoModifyApi;
import com.ctb_open_car.eventbus.MessageEvent;
import com.ctb_open_car.utils.AliossUtils;
import com.ctb_open_car.view.activity.community.CommunityActivty;
import com.ctb_open_car.view.activity.dynamic.ReleaseActivity;
import com.ctb_open_car.view.dialog.RoundProgressDialog;
import com.google.gson.JsonObject;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;
import com.wildma.pictureselector.PictureSelector;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import timber.log.Timber;

import static com.ctb_open_car.utils.StringUtils.generateUUID;
import static com.ctb_open_car.utils.StringUtils.getFileSuffix;

public class ModifyPersonalInfoActivity extends BaseActivity {

    @BindView(R.id.ic_back)
    ImageView mBack;
    @BindView(R.id.personal_avatar_logo)
    ImageView mPersonalAvatar;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.personal_nick_name)
    EditText mPersonalNickName;
    @BindView(R.id.radioButton_man)
    RadioButton mPersonalGenderMan;
    @BindView(R.id.radioButton)
    RadioButton  mPersonalGender;
    @BindView(R.id.personal_age)
    TextView mPersonalAge;
    @BindView(R.id.btn_save)
    Button BtnSave;

    private int mGender = -1;
    private UserInfoBean.UserInfoDto mUserInfoDto;
    private String mPicturePath;
    private String mObjectKey;
    private RoundProgressDialog mRoundProgressDialog;

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_personal_info);
        ButterKnife.bind(this);

        mTitleTv.setText("修改个人资料");
        mTitleTv.setVisibility(View.VISIBLE);
        mUserInfoDto = (UserInfoBean.UserInfoDto)getIntent().getSerializableExtra("UserInfoDto");
        if (mUserInfoDto != null) {
            mPersonalNickName.setText(mUserInfoDto.getNickname());
            Glide.with(this).asBitmap().circleCrop().load(mUserInfoDto.getUserIcon()).placeholder( R.drawable.default_avar_icon).error(R.drawable.default_avar_icon).into(mPersonalAvatar);
            if (AliossUtils.getSingleton().getOss() == null) {
                AliossUtils.getSingleton().getAliStsToken(this);
            }
            mGender = mUserInfoDto.getSex();
            if (mGender == 1) {
                mPersonalGenderMan.setChecked(true);
            } else if (mGender == 2) {
                mPersonalGender.setChecked(true);
            }

            if (!TextUtils.isEmpty(mUserInfoDto.getBirthday())) {
                mPersonalAge.setText(mUserInfoDto.getBirthday());
            }
        }

    }

    @Override
    public Object getTag() {
        return null;
    }

    @OnClick({R.id.ic_back, R.id.personal_avatar_logo, R.id.nickname_lay, R.id.radioButton_man, R.id.radioButton, R.id.personal_age_lay, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ic_back:
                onBackPressed();
                break;
            case R.id.personal_avatar_logo:
                File file = new File("storage/emulated/0/PictureSelector.temp.jpg");
                if (file != null && file.exists()){
                  file.delete();
                }
                PictureSelector
                      .create(this, PictureSelector.SELECT_REQUEST_CODE)
                      .selectPicture(true, 200, 200, 1, 1);
                break;
            case R.id.nickname_lay:
                break;
            case R.id.radioButton_man:
                mGender = 1;
                break;
            case R.id.radioButton:
                mGender = 2;
                break;
            case R.id.personal_age_lay:
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow( mPersonalNickName.getWindowToken(), 0);
                showPickerView();
                break;
            case R.id.btn_save:
                if (mUserInfoDto != null) {
                    if (TextUtils.isEmpty(mPersonalNickName.getText().toString()) && !TextUtils.isEmpty(mUserInfoDto.getNickname())) {
                        Toasty.info(ModifyPersonalInfoActivity.this, "昵称不能为空").show();
                    } else if (TextUtils.isEmpty(mPersonalAge.getText().toString()) && !TextUtils.isEmpty(mUserInfoDto.getBirthday())) {
                        Toasty.info(ModifyPersonalInfoActivity.this, "生日不能为空").show();
                    } else if ( mGender == -1 && mUserInfoDto.getSex() != -1) {
                        Toasty.info(ModifyPersonalInfoActivity.this, "请选择性别").show();
                    } else {
                        if (!TextUtils.isEmpty(mPicturePath)) {
                            new MyThread().start();
                        } else {
                            pushUserInfo("");
                        }
                    }
                } else {
                    if (!TextUtils.isEmpty(mPicturePath)) {
                        new MyThread().start();
                    } else {
                        pushUserInfo("");
                    }
                }
                break;
        }
    }

    private void showPickerView() {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(2018,0,1);
        endDate.set(2032,11,31);

        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                mPersonalAge.setText(getTime(date));
            }
        }).build();
        pvTime.show();
    }

    private String getTime(Date date) {
        return mFormatter.format(date);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*结果回调*/
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                mPicturePath = data.getStringExtra(PictureSelector.PICTURE_PATH);
                Glide.with(this).asBitmap().circleCrop().load(mPicturePath).skipMemoryCache(true) // 不使用内存缓存
                        .diskCacheStrategy(DiskCacheStrategy.NONE).into(mPersonalAvatar);
            }
        }
    }

    class MyThread extends Thread {
        @Override
        public void run() {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String yestoday = sdf.format(calendar.getTime());

                String filePath = mPicturePath;
                if (!TextUtils.isEmpty(filePath)) {
                    mObjectKey = "ctb/opencar/image/user/icon/" + yestoday + File.separator + generateUUID() + "." + getFileSuffix(filePath);
                    String path = AliossUtils.getSingleton().updateImage(mObjectKey, filePath);

                    if (!TextUtils.isEmpty(path)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pushUserInfo(mObjectKey);
                            }
                        });
                    }
                }
        }
    }

    private void pushUserInfo(String userIconUrl) {
        showDialog("正在修改...");
        HashMap<String, Object> queryMap = new HashMap<>();
        if (!TextUtils.isEmpty(mPersonalNickName.getText().toString())) {
            queryMap.put("nickName", mPersonalNickName.getText().toString());
        }

        if (!TextUtils.isEmpty(userIconUrl)) {
            queryMap.put("userIcon", userIconUrl);
        }

        if (mGender != -1) {
            queryMap.put("userSex", mGender);
        }

        if (!TextUtils.isEmpty(mPersonalAge.getText().toString())) {
            queryMap.put("userBirthday", mPersonalAge.getText().toString());
        }

        UserInfoModifyApi publishDetail = new UserInfoModifyApi(new HttpListener() {
            @Override
            public void onNext(Object object) {
                super.onNext(object);
                BaseResultEntity jsonObject = (BaseResultEntity) object;
                String code = jsonObject.getRet();
                if (code.equals("0")) {
                    MessageEvent messageEvent = new MessageEvent();
                    messageEvent.setType("update_data_me");
                    EventBus.getDefault().post(messageEvent);
                    Toasty.info(ModifyPersonalInfoActivity.this, "用户信息修改成功").show();
                    onBackPressed();
                    dismissDiaLog();
                } else {
                    dismissDiaLog();
                    Toasty.info(ModifyPersonalInfoActivity.this, jsonObject.getMsg()).show();
                }
            }

            @Override
            public void onError(Throwable e) {
                dismissDiaLog();
                Toasty.info(ModifyPersonalInfoActivity.this, "用户信息修改失败").show();
                Timber.e("mListNewBean " + e.toString());
            }
        }, this);

        publishDetail.setRequestBody(queryMap);
        HttpManager.getInstance().doHttpDeal(publishDetail);
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
}
