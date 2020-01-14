package com.ctb_open_car.view.activity.map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;


import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseSearchActivity;
import com.ctb_open_car.constraints.AppContraint;
import com.ctb_open_car.ui.RecycleViewDivider;
import com.ctb_open_car.view.adapter.map.PoiResultAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;


public class FindAddressActivity extends BaseSearchActivity implements TextWatcher, TextView.OnEditorActionListener {

    @BindView(R.id.search_edit_text)
    EditText mEditLocation;

    @BindView(R.id.lsv_SuggestResult)
    RecyclerView mRecyclerView;

    private int mPoiType;
    private PoiResultAdapter mAdapter;
    private int mCollectIndex = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_address);
        ButterKnife.bind(this);
        mPoiType = getIntent().getIntExtra("type", 0);

        mCollectIndex = getIntent().getIntExtra("collect_index", -1);
        startOnceLocation();
        initView();

    }

    private void initView() {
        mEditLocation.addTextChangedListener(this);
        mEditLocation.setOnEditorActionListener(this);

        LinearLayoutManager ll = new LinearLayoutManager(this);
        ll.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(this, DividerItemDecoration.VERTICAL, Color.parseColor("#F5F5F5"), 2));
        mRecyclerView.setLayoutManager(ll);
        mAdapter = new PoiResultAdapter(this, mCollectIndex);
        mRecyclerView.setAdapter(mAdapter);
    }


    /**
     * 搜索结果返回
     */
    @Override
    public void onPoiSearched(PoiResult poiResult, int r) {
        Timber.e(poiResult.getPois().toString() + "" + r);
        if (r == 1000) {
            List<PoiItem> datas = new ArrayList<>();
            ArrayList<PoiItem> pois = poiResult.getPois();

            mAdapter.setData(pois);
        }
    }

    @OnClick(R.id.search_cancel)
    void toCancel(View v) {
        this.finish();
    }

    @OnClick(R.id.location_from_map)
    void toMapLocation(View v) {
        Intent i = new Intent(this, MapPickerActivity.class);
        i.putExtra("type", mPoiType);
        startActivity(i);
    }

    @OnClick(R.id.location_current)
    void getCurrentLocation(View v) {
        mNearbySearch = true;

        startOnceLocation();
    }

    @Override
    public Object getTag() {
        return null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Intent resultIntent = new Intent();
        LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
        if (TextUtils.isEmpty(mCurrentCity)) {
            mCurrentLoc = latLng;
            mCurrentCity = aMapLocation.getAddress();
        } else {
            resultIntent.putExtra("lat_lng", latLng);
            resultIntent.putExtra("address", aMapLocation.getAddress());
            resultIntent.putExtra("poi_name", aMapLocation.getPoiName());
            resultIntent.putExtra("collect_index", mCollectIndex);
            setResult(AppContraint.POICollect.LOCATION_COMPANY_RESULT_CODE, resultIntent);
            finish();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            mNearbySearch = false;
            keyWord = mEditLocation.getText().toString().trim();
            doSearchQuery(keyWord, mCurrentCity);
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


}
