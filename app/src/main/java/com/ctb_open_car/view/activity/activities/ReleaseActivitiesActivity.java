package com.ctb_open_car.view.activity.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.ctb_open_car.MainActivity;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.activites.ActivitiesBean;
import com.ctb_open_car.bean.activites.JsonBean;
import com.ctb_open_car.bean.releasedynamics.ReleaseDynamics;
import com.ctb_open_car.eventbus.MessageEvent;
import com.ctb_open_car.view.activity.dynamic.AblumSelecteActivity;
import com.ctb_open_car.view.activity.dynamic.ReleaseActivity;
import com.google.gson.Gson;
import com.library.BottomDialog;
import com.library.Item;
import com.library.OnItemClickListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.umeng.commonsdk.debug.I;
import com.wang.avi.AVLoadingIndicatorView;
import com.wildma.pictureselector.PictureSelector;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class ReleaseActivitiesActivity extends RxAppCompatActivity {

    @BindView(R.id.cancel)
    TextView mCancel;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.next_step)
    TextView mNextStep;
    @BindView(R.id.title_bar)
    RelativeLayout mTitleBar;
    @BindView(R.id.activity_name)
    EditText mTctivityName;
    @BindView(R.id.activity_name_lay)
    RelativeLayout mActivityNameLay;
    @BindView(R.id.activity_resort)
    TextView mActivityResort;
    @BindView(R.id.resort_lay)
    RelativeLayout mResortLay;
    @BindView(R.id.activity_start_time)
    TextView mActivityStartTime;
    @BindView(R.id.start_time_lay)
    RelativeLayout mStartTimeLay;
    @BindView(R.id.activity_stop_time)
    TextView mActivityStopTime;
    @BindView(R.id.start_stop_lay)
    RelativeLayout mStartStopLay;
    @BindView(R.id.activity_deadline)
    TextView mActivityDeadline;
    @BindView(R.id.deadline_lay)
    RelativeLayout mDeadlineLay;
    @BindView(R.id.activity_maximum)
    EditText mActivityMaximum;
    @BindView(R.id.maximum_lay)
    RelativeLayout mMaximumLay;
    @BindView(R.id.wechat_group_code)
    TextView mWechatGroupCode;
    @BindView(R.id.wechat_group_code_lay)
    RelativeLayout mWwechatGroupCodeLay;

    private List<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private static final int REQUEST_CODE_CHOOSE = 1001;

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_activities);
        ButterKnife.bind(this);

       new Thread(new Runnable() {
            @Override
            public void run() {
                // 子线程中解析省市区数据
                initJsonData();
            }
        }).start();

    }

    @OnClick({R.id.cancel, R.id.next_step, R.id.activity_name_lay, R.id.resort_lay, R.id.start_time_lay, R.id.start_stop_lay, R.id.deadline_lay, R.id.maximum_lay, R.id.wechat_group_code_lay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                onBackPressed();
                break;
            case R.id.next_step:
                setNewStep();
                break;
            case R.id.activity_name_lay:
                break;
            case R.id.resort_lay:
                initOptionPicker();
                break;
            case R.id.start_time_lay:
                showPickerView("startTime");
                break;
            case R.id.start_stop_lay:
                showPickerView("stopTime");
                break;
            case R.id.deadline_lay:
                showPickerView("registrationDeadline");
                break;
            case R.id.maximum_lay:
                break;
            case R.id.wechat_group_code_lay:
                PictureSelector
                        .create(this, PictureSelector.SELECT_REQUEST_CODE)
                        .selectPicture(false, 600, 600, 1, 1);        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                break;
        }
    }

    private void setNewStep(){
        if (TextUtils.isEmpty(mTctivityName.getText().toString()) ||
                TextUtils.isEmpty(mActivityResort.getText().toString()) ||
                TextUtils.isEmpty(mActivityStartTime.getText().toString()) ||
                TextUtils.isEmpty(mActivityStopTime.getText().toString()) ||
                TextUtils.isEmpty(mActivityDeadline.getText().toString()) ||
                TextUtils.isEmpty(mActivityMaximum.getText().toString()) ||
                TextUtils.isEmpty(mWechatGroupCode.getText().toString())) {
            Toasty.info(this, getString(R.string.activity_input_tip)).show();
        } else {
            try {
                Date dateStart = mFormatter.parse(mActivityStartTime.getText().toString());
                long startTime = dateStart.getTime();
                Date dateStop = mFormatter.parse(mActivityStopTime.getText().toString());
                long StopTime = dateStop.getTime();
                Date dateDead = mFormatter.parse(mActivityDeadline.getText().toString());
                long deadTime = dateDead.getTime();

                if (StopTime < startTime) {
                    Toasty.info(this, getString(R.string.activity_input_failed_1)).show();
                } else if (deadTime > startTime) {
                    Toasty.info(this, getString(R.string.activity_input_failed_2)).show();
                } else {
                    ActivitiesBean activitiesBean = new ActivitiesBean();
                    activitiesBean.setActivityTitle(mTctivityName.getText().toString());
                    activitiesBean.setActivityDeparturePlace(mActivityResort.getText().toString());
                    activitiesBean.setActivityBegintime(mActivityStartTime.getText().toString());
                    activitiesBean.setActivityEndtime(mActivityStopTime.getText().toString());
                    activitiesBean.setActivityEnrollEndtime(mActivityDeadline.getText().toString());
                    activitiesBean.setActivityEnrollLimit(Integer.valueOf(mActivityMaximum.getText().toString()));
                    activitiesBean.setActivityInviteIcon(mWechatGroupCode.getText().toString());
                    activitiesBean.setLatitude(mLatitude);
                    activitiesBean.setLongitude(mLongititude);

                    Intent intent = new Intent(this,PushActivitiesActivity.class);
                    intent.putExtra("ActivitiesBean", activitiesBean);
                    startActivity(intent);
                    onBackPressed();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }
    private void showPickerView(String seletctTime) {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(2018,0,1);
        endDate.set(2032,11,31);

        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date,View v) {//选中事件回调
                switch(seletctTime) {
                     case "startTime":
                         mActivityStartTime.setText(getTime(date));
                         break;
                     case "stopTime":
                         mActivityStopTime.setText(getTime(date));
                         break;
                     case "registrationDeadline":
                         mActivityDeadline.setText(getTime(date));
                         break;
                 }
            }
        }).build();
        pvTime.show();

    }

    private String getTime(Date date) {
        return mFormatter.format(date);
    }

    private void initOptionPicker() {
        OptionsPickerView pvOptions = new  OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText()
                        + options2Items.get(options1).get(option2)
                        + options3Items.get(options1).get(option2).get(options3);
                mActivityResort.setText(tx);
                getLatlon(tx.trim());
            }
        }).build();
        pvOptions.setTitleText("选择地区");
        pvOptions.setPicker(options1Items, options2Items, options3Items);
        pvOptions.show();

    }

    private void initJsonData() {//解析数据
        String JsonData = getJson(this, "province.json");//获取assets目录下的json文件数据
        List<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体
        options1Items = jsonBean;
        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String cityName = jsonBean.get(i).getCityList().get(c).getName();
                cityList.add(cityName);//添加城市
                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表
                city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }
            options2Items.add(cityList);
            options3Items.add(province_AreaList);
        }
    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    public String getJson(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

// 地址转化为经纬度
    double mLatitude;
    double mLongititude;
    private void getLatlon(String cityName){
        GeocodeSearch geocodeSearch=new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                if (i==1000){
                    if (geocodeResult!=null && geocodeResult.getGeocodeAddressList()!=null && geocodeResult.getGeocodeAddressList().size()>0){
                        GeocodeAddress geocodeAddress = geocodeResult.getGeocodeAddressList().get(0);
                        mLatitude = geocodeAddress.getLatLonPoint().getLatitude();//纬度
                        mLongititude = geocodeAddress.getLatLonPoint().getLongitude();//经度
                    }
                }
            }
        });
        GeocodeQuery geocodeQuery=new GeocodeQuery(cityName.trim(),"29");
        geocodeSearch.getFromLocationNameAsyn(geocodeQuery);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*结果回调*/
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                String picturePath = data.getStringExtra(PictureSelector.PICTURE_PATH);
                mWechatGroupCode.setText(picturePath);
            }
        }
    }

}
