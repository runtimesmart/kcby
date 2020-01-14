package com.ctb_open_car.view.activity.cartool;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ReplacementTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctb_open_car.MainActivity;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.userInfo.UserInfoBean;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.MyInfoApi;
import com.ctb_open_car.eventbus.MessageEvent;
import com.ctb_open_car.view.activity.news.ColumnActivity;
import com.ctb_open_car.view.activity.vehicletools.VehicleManagementActivity;
import com.ctb_open_car.view.dialog.InputTextMsgDialog;
import com.ctb_open_car.view.dialog.ProvinceCodeDialog;
import com.ctb_open_car.view.dialog.RoundProgressDialog;
import com.google.gson.JsonObject;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class AddCarInfoActivity extends RxAppCompatActivity {

    @BindView(R.id.cancel)
    ImageView mCancel;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.province)
    TextView mProvince;
    @BindView(R.id.car_num_edit)
    EditText mCarNumEdit;
    @BindView(R.id.engine_numbery_name)
    EditText mEngineNumberyName;
    @BindView(R.id.activity_name)
    EditText mActivityName;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private RoundProgressDialog mRoundProgressDialog;

    public final int ADD_REQUEST_CODE = 0x1015; //添加车辆信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car_info);
        ButterKnife.bind(this);

        mCarNumEdit.setTransformationMethod(new InputCapLowerToUpper());
        mActivityName.setTransformationMethod(new InputCapLowerToUpper());
    }

    private void addCarInifo() {
        if (TextUtils.isEmpty(mCarNumEdit.getText().toString()) ||
                TextUtils.isEmpty(mEngineNumberyName.getText().toString()) ||
                TextUtils.isEmpty(mActivityName.getText().toString())) {
            Toasty.info(this, getString(R.string.activity_input_tip)).show();
        } else {
            if (mCarNumEdit.length() < 6) {
                Toasty.info(this, "车辆号码不正确").show();
            } else if(mActivityName.length() != 17) {
                Toasty.info(this, "车架号不正确").show();
            } else if(mEngineNumberyName.length() > 10 ||  mEngineNumberyName.length() < 6) {
                Toasty.info(this, "发动机号不正确").show();
            } else{
                postCarInfo();
            }

        }
    }

    public void postCarInfo() {
        showDialog("正在上传数据...");
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("plate",mProvince.getText().toString() + mCarNumEdit.getText().toString().toUpperCase());
        queryMap.put("engine",mEngineNumberyName.getText().toString());
        queryMap.put("evin",mActivityName.getText().toString().toUpperCase());
        //queryMap.put("brand","");
        MyInfoApi myInfoApi = new MyInfoApi(new HttpListener() {
            @Override
            public void onNext(Object object) {
                super.onNext(object);
                BaseResultEntity jsonObject = (BaseResultEntity)object;
                if (jsonObject.getRet().equals("0"))   {
                    MessageEvent messageEvent = new MessageEvent();
                    messageEvent.setType("update_data_me");
                    EventBus.getDefault().post(messageEvent);
                    dismissDiaLog();
                    Intent intent = new Intent();
                    setResult(ADD_REQUEST_CODE, intent);
                    finish();
                } else {
                    dismissDiaLog();
                    Toasty.info(AddCarInfoActivity.this, jsonObject.getMsg()).show();
                }
            }

            @Override
            public void onError(Throwable e){
                dismissDiaLog();
                Timber.e("e = %s", e.getMessage());
            }
        }, this, 2);
        myInfoApi.setRequestBody(queryMap);
        HttpManager.getInstance().doHttpDeal(myInfoApi);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @OnClick({R.id.cancel, R.id.btn_login, R.id.brand_type_lay,  R.id.province})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                onBackPressed();
                break;
            case R.id.btn_login:
                addCarInifo();
                break;
            case R.id.province:
                ProvinceCodeDialog dialog = new ProvinceCodeDialog(new ProvinceCodeDialog.SelectListener() {
                    @Override
                    public void selectListener(String inputText) {
                        mProvince.setText(inputText);
                    }
                });
                dialog.show(getSupportFragmentManager(), "comment");
                break;
            case R.id.brand_type_lay:
                Toasty.info(this, "开发中").show();
                break;
        }
    }

    class InputCapLowerToUpper extends ReplacementTransformationMethod {
        @Override
        protected char[] getOriginal() {
            char[] lower = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z' };
            return lower;
        }

        @Override
        protected char[] getReplacement() {
            char[] upper = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z' };
            return upper;
        }
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
