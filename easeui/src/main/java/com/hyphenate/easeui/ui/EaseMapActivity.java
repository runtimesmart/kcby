/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyphenate.easeui.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.R2;
import com.hyphenate.easeui.widget.EaseTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EaseMapActivity extends EaseBaseActivity implements AMap.OnCameraChangeListener, AMapLocationListener {

    @BindView(R2.id.map_picker)
    MapView mMapView;

    @BindView(R2.id.title_bar)
    EaseTitleBar mTitleBar;
    private double mLat;
    private double mLng;
    private String mAddress;

    private AMapLocation mAmapLocation;
    private AMap mMap;
    private MyLocationStyle mLocationStyle;

    private Marker mCurrentMarker;
    private Marker mChatMarker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ctb_map);
        ButterKnife.bind(this);
        mMapView.onCreate(savedInstanceState);
        mLat = getIntent().getDoubleExtra("latitude", 0);
        mLng = getIntent().getDoubleExtra("longitude", 0);
        mAddress = getIntent().getStringExtra("address");

        initView();
        if (!TextUtils.isEmpty(mAddress)) {
            LatLng latLng = new LatLng(mLat, mLng);
            mChatMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng));
            mTitleBar.setRightTxtVisibile(View.GONE);
        } else {
            startOnceLocation();
        }
    }


    private void initView() {
        mTitleBar.setRightTxt("发送");

        mMap = mMapView.getMap();
        mMap.setMapType(AMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setLogoBottomMargin(-50);
//        mMap.setTrafficEnabled(true);

        mMap.setMyLocationEnabled(false);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        mMap.setOnCameraChangeListener(this);

        mTitleBar.setRightTxtListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == mAmapLocation) {
                    return;
                }
                sendLocation(mAmapLocation);
            }
        });

        mTitleBar.setLeftLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void startOnceLocation() {
        AMapLocationClient mlocationClient = new AMapLocationClient(this);
        mlocationClient.setLocationListener(this);
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        mLocationOption.setNeedAddress(true);
        mLocationOption.setOnceLocation(true);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
        mCurrentMarker = mMap.addMarker(new MarkerOptions()
                .position(latLng));
        this.mAmapLocation = aMapLocation;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if (null != mCurrentMarker) {
            mCurrentMarker.setPosition(cameraPosition.target);
        }
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if (null != mCurrentMarker) {
            mCurrentMarker.setPosition(cameraPosition.target);

        }
    }

    //
    public void sendLocation(AMapLocation lastLocation) {
        Intent intent = this.getIntent();
        intent.putExtra("latitude", lastLocation.getLatitude());
        intent.putExtra("longitude", lastLocation.getLongitude());
        intent.putExtra("address", lastLocation.getAddress());
        this.setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

}
