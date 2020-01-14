package com.ctb_open_car.view.activity.map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapPickerActivity extends BaseActivity implements AMap.OnCameraChangeListener, AMapLocationListener {
    private int mType;
    @BindView(R.id.map_picker)
    MapView mMapView;
    private AMap mMap;
    private MyLocationStyle mLocationStyle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_poi_picker);
        ButterKnife.bind(this);
        mMapView.onCreate(savedInstanceState);
        mType = getIntent().getIntExtra("type", 0);
        initView();
        initData();
    }

    private void initData() {
        LatLng latLng = getIntent().getParcelableExtra("lat_lng");
        if (null == latLng) {
            startOnceLocation();
        } else {
            mMap.addMarker(new MarkerOptions()
                    .position(latLng));
        }
    }

    private void initView() {
        mMap = mMapView.getMap();
        mMap.setMapType(AMap.MAP_TYPE_NORMAL);
        mLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        mLocationStyle.interval(5000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        mLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        mLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        mMap.setMyLocationStyle(mLocationStyle);//设置定位蓝点的Style
        mLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        mLocationStyle.showMyLocation(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setLogoBottomMargin(-50);
        mMap.setTrafficEnabled(true);

        mMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        mMap.setOnCameraChangeListener(this);
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
        mMap.addMarker(new MarkerOptions()
                .position(latLng));
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
    public Object getTag() {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }
}
