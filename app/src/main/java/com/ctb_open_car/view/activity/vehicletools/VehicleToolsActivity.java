package com.ctb_open_car.view.activity.vehicletools;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseActivity;
import com.ctb_open_car.bean.newsbean.NewsInfoBean;
import com.ctb_open_car.bean.vehicletools.VehicleToolsBean;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.VehicleToolsApi;
import com.ctb_open_car.eventbus.MessageEvent;
import com.ctb_open_car.utils.StringUtils;
import com.ctb_open_car.view.activity.cartool.AddCarInfoActivity;
import com.ctb_open_car.view.adapter.vehicletoolsadpter.VehicleToolsTabAdapter;
import com.ctb_open_car.view.dialog.RoundProgressDialog;
import com.google.android.material.tabs.TabLayout;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.RxRetrofitApp;
import com.rxretrofitlibrary.http.HttpManager;
import com.wildma.pictureselector.PictureSelector;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import timber.log.Timber;

/**
 * 车主工具-主页面
 **/
public class VehicleToolsActivity extends BaseActivity {

    @BindView(R.id.ic_back)
    ImageView mBack;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.add_car)
    ImageView mAddCar;
    @BindView(R.id.add_info_car)
    RelativeLayout mAddInfoCar;
    @BindView(R.id.info_car)
    RelativeLayout mInfoCar;
    @BindView(R.id.add_car_bt)
    ImageButton mAddCarBt;
    @BindView(R.id.community_tab_layout)
    TabLayout mCommunityTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.location)
    TextView mLocation;
    @BindView(R.id.limit_number)
    TextView mLimitNumber;
    @BindView(R.id.limit_remark)
    TextView mLimitRemark;

    @BindView(R.id.oil_90)
    TextView mOil90;
    @BindView(R.id.oil_93)
    TextView mOil93;
    @BindView(R.id.oil_97)
    TextView mOil97;
    @BindView(R.id.oil_0)
    TextView mOil0;

    private VehicleToolsTabAdapter mAdapter;
    private VehicleToolsBean mVehicleToolsBean;
    private RxRetrofitApp mRxInstance;
    private RoundProgressDialog mRoundProgressDialog;
    public final int ADD_REQUEST_CODE = 0x1015; //添加车辆信息
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_tools);
        ButterKnife.bind(this);

        mRxInstance = RxRetrofitApp.Singleton.INSTANCE.get();
        mAdapter = new VehicleToolsTabAdapter(getSupportFragmentManager());
        mTitleTv.setText(getString(R.string.vehicle_tools));
        mTitleTv.setVisibility(View.VISIBLE);
        mLocation.setText(StringUtils.mCurrentCity);

        showDialog("正在加载...");
        getCarTab();
    }

    @Override
    public Object getTag() {
        return null;
    }

    @OnClick({R.id.ic_back, R.id.add_info_car, R.id.add_car_bt})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ic_back:
                onBackPressed();
                break;
            case R.id.add_info_car:
            case R.id.add_car_bt:

//                intent = new Intent(VehicleToolsActivity.this, ViolationWebViewActivity.class);
//                intent.putExtra("violateDzyH5Url", "https://m.jiaofabao.com/in/checkResult.html?cc=ctb_app&carId=902876");
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);

                intent = new Intent(VehicleToolsActivity.this, AddCarInfoActivity.class);
                intent.putExtra("addCar", "addCar");
                startActivityForResult(intent, ADD_REQUEST_CODE);
                break;
        }
    }

    private void getCarTab() {
        HashMap<String, Object> queryMap = new HashMap<>();
        //queryMap.put("unionid", "oJc0XvylKV0cNYWGtxXROL_Mxw_4");
        queryMap.put("province", StringUtils.mCurrentProvince);
        queryMap.put("city", StringUtils.mCurrentCity);
        VehicleToolsApi vehicleToolsApi = new VehicleToolsApi(new HttpListener() {
            @Override
            public void onNext(Object object) {
                BaseResultEntity<VehicleToolsBean> baseResultEntity = (BaseResultEntity<VehicleToolsBean>) object;
                if (baseResultEntity.getRet().equals("0")) {
                    mVehicleToolsBean = baseResultEntity.getData();
                } else {
                    Toasty.info(VehicleToolsActivity.this, baseResultEntity.getMsg()).show();
                }
                setCarData();
            }

            @Override
            public void onError(Throwable e) {
                setCarData();
                Timber.e(" getCarTab e = %s", e.getMessage());
            }
        }, this);
        vehicleToolsApi.setRequestBody(queryMap);
        HttpManager.getInstance().doHttpDeal(vehicleToolsApi);
    }

    public void setCarData() {
        dismissDiaLog();
        if (mVehicleToolsBean != null) {
            if (mVehicleToolsBean.getPlateList() != null && mVehicleToolsBean.getPlateList().size() > 0) {
                mAddInfoCar.setVisibility(View.GONE);
                mInfoCar.setVisibility(View.VISIBLE);
                mAdapter.setList(mVehicleToolsBean.getPlateList());
                mViewPager.setAdapter(mAdapter);
                mCommunityTabLayout.setupWithViewPager(mViewPager);
            } else {
                mAddInfoCar.setVisibility(View.VISIBLE);
                mInfoCar.setVisibility(View.GONE);
            }

            if (mVehicleToolsBean.getPlateList() != null && mVehicleToolsBean.getPlateList().size() > 2) {
                mAddCarBt.setVisibility(View.GONE);
            } else {
                mAddCarBt.setVisibility(View.VISIBLE);
            }

            if (mVehicleToolsBean.getLimitNumber() != null && mVehicleToolsBean.getLimitNumber().getLimitNumber().length() > 2) {
                SpannableString spannableString = new SpannableString(mVehicleToolsBean.getLimitNumber().getLimitNumber());
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#3240DB")), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#3240DB")), 2, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                mLimitNumber.setText(spannableString);
                mLimitRemark.setText(mVehicleToolsBean.getLimitNumber().getRemark());
            } else if (mVehicleToolsBean.getLimitNumber() != null) {
                mLimitNumber.setText(mVehicleToolsBean.getLimitNumber().getLimitNumber());
                mLimitRemark.setText(mVehicleToolsBean.getLimitNumber().getRemark());
            }

            mOil90.setText(String.format("%s元/升", mVehicleToolsBean.getOilPrice().getOil90()));
            mOil93.setText(String.format("%s元/升", mVehicleToolsBean.getOilPrice().getOil93()));
            mOil97.setText(String.format("%s元/升", mVehicleToolsBean.getOilPrice().getOil97()));
            mOil0.setText(String.format("%s元/升", mVehicleToolsBean.getOilPrice().getOilZero()));
        } else {
            mAddInfoCar.setVisibility(View.VISIBLE);
            mInfoCar.setVisibility(View.GONE);
        }
    }

    public void showDialog(String tips) {
        mRoundProgressDialog = RoundProgressDialog.newInstance(tips);
        mRoundProgressDialog.show(getSupportFragmentManager(),"dialog");
    }

    public void dismissDiaLog () {
        if (mRoundProgressDialog != null) {
            mRoundProgressDialog.dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*结果回调*/
        if (requestCode == ADD_REQUEST_CODE) {
            getCarTab();
        }
    }
}
