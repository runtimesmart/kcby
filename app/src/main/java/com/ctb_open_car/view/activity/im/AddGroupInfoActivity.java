package com.ctb_open_car.view.activity.im;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseActivity;
import com.ctb_open_car.bean.activites.JsonBean;
import com.ctb_open_car.bean.areacode.AreaCodeDtoBean;
import com.ctb_open_car.bean.im.CarLibaryBean;
import com.ctb_open_car.bean.im.CarModelBean;
import com.ctb_open_car.bean.im.CarStyleBean;
import com.ctb_open_car.bean.im.TagDtoBean;
import com.ctb_open_car.bean.im.TagListBean;
import com.ctb_open_car.view.fragment.im.ImCarLibaryFragment;
import com.ctb_open_car.view.fragment.im.ImCarModelFragment;
import com.ctb_open_car.view.fragment.im.ImGroupCityFragment;
import com.ctb_open_car.view.fragment.im.ImGroupLableFragment;
import com.ctb_open_car.view.fragment.im.ImProvinceFragment;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddGroupInfoActivity extends BaseActivity {
    private HashMap<String, ArrayList<String>> mCity = new HashMap<>();
    private List<String> mProvince = new ArrayList<>();
    public String mPrivoiceName;
    private AreaCodeDtoBean mCiryName;
    private List<TagDtoBean> mLablBeanList;
    public TagListBean mTagListBean;
    private FragmentManager mFragmentManager;
    private ImProvinceFragment mFragment;
    private ImGroupCityFragment mCityFragment;
    private ImGroupLableFragment mLableFragment;
    private ImCarLibaryFragment mCardLibaryFragment;
    private ImCarModelFragment mCarModelFragment;
    public CarStyleBean mCarStyleBean;
    private CarModelBean mCarModelBean;
    private AreaCodeDtoBean mAreaCodeDtoBean;
    private static final int REQUEST_CODE_CITY = 0x1001;
    private static final int REQUEST_CODE_LABLE = 0x1002;
    private static final int REQUEST_CODE_CARSYSTEM = 0x1003;
    private int mTpye;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_info);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); //透明导航栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.color_transparent00));
        }

        mTpye = getIntent().getIntExtra("select_info_type", 1);
        mFragmentManager = getSupportFragmentManager();
        if (mTpye == 1) {  // 城市
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            mFragment = new ImProvinceFragment();
            transaction.replace(R.id.fragment, mFragment);
            // transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        } else if(mTpye == 2) { // 标签
            mTagListBean = (TagListBean)getIntent().getSerializableExtra("select_info_lable");
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            mLableFragment = new ImGroupLableFragment();
            transaction.replace(R.id.fragment, mLableFragment);
            // transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        } else { // 车系
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            mCardLibaryFragment = new ImCarLibaryFragment();
            transaction.replace(R.id.fragment, mCardLibaryFragment);
            // transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        }
    }

    @Override
    public Object getTag() {
        return null;
    }


    public void setProvinceName(AreaCodeDtoBean cityAreaCode, String provinceName) {
        mPrivoiceName = provinceName;
        mCiryName = cityAreaCode;
        Intent intentTemp = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("select_info_city", mCiryName);
        bundle.putString("select_info_privoiceName", mPrivoiceName);
        intentTemp.putExtra("bundle", bundle);
        setResult(REQUEST_CODE_CITY,intentTemp);
        finish();
    }

    public void startImGroupCityFragment(AreaCodeDtoBean provinceName) {
        mPrivoiceName = provinceName.getAreaName();
        mAreaCodeDtoBean = provinceName;
        if (provinceName.getChildAreaCode() != null) {
            mCityFragment = new ImGroupCityFragment();
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.replace(R.id.fragment, mCityFragment);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        } else {
            Intent intentTemp = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("select_info_city", mAreaCodeDtoBean);
            bundle.putString("select_info_privoiceName", mPrivoiceName);
            intentTemp.putExtra("bundle", bundle);
            setResult(REQUEST_CODE_CITY,intentTemp);
            finish();
        }
    }

    public void setCityName(AreaCodeDtoBean cityName) {
        mCiryName = cityName;
        Intent intentTemp = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("select_info_city", mCiryName);
        bundle.putString("select_info_privoiceName", mPrivoiceName);
        intentTemp.putExtra("bundle", bundle);
        setResult(REQUEST_CODE_CITY,intentTemp);
        finish();
    }

    public void setLableName(TagListBean lable) {
        Intent intentTemp = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("select_info_lable", lable);
        intentTemp.putExtra("bundle", bundle);
        setResult(REQUEST_CODE_LABLE,intentTemp);
        finish();
    }

    public List<String> getProvinceList() {
       return mProvince;
    }

    public List<AreaCodeDtoBean> getCityList() {
        return mAreaCodeDtoBean.getChildAreaCode();
    }

    public void startImCarModelFragment(CarStyleBean carStyleBean) {
        mCarStyleBean = carStyleBean;
        mCarModelFragment = new ImCarModelFragment();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.fragment, mCarModelFragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    public void setCarModelBean(CarModelBean carModelBean) {
        mCarModelBean = carModelBean;
        Intent intentTemp = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("select_info_car_system", mCarModelBean);
        intentTemp.putExtra("bundle", bundle);
        setResult(REQUEST_CODE_CARSYSTEM,intentTemp);
        finish();
    }

    @Override
    public void onBackPressed() {
        if(mFragmentManager.getBackStackEntryCount() == 1){
            if (mTpye == 1) {
                getSupportFragmentManager().beginTransaction().hide(mCityFragment).commitAllowingStateLoss();
                getSupportFragmentManager().beginTransaction().show(mFragment).commitAllowingStateLoss();
            } else {
                getSupportFragmentManager().beginTransaction().hide(mCarModelFragment).commitAllowingStateLoss();
                getSupportFragmentManager().beginTransaction().show(mCardLibaryFragment).commitAllowingStateLoss();
            }

        }
        super.onBackPressed();
    }
}
