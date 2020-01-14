package com.ctb_open_car.view.activity.map;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseMapActivity;
import com.ctb_open_car.engine.manager.GsonManager;
import com.ctb_open_car.ui.RecycleViewDivider;
import com.ctb_open_car.utils.Device;
import com.ctb_open_car.utils.StringUtils;
import com.ctb_open_car.view.fragment.dialog.MapPoiAdapter;
import com.ctb_open_car.view.fragment.dialog.PoiDetailDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.rxretrofitlibrary.RxRetrofitApp;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;


public class NaviSearchActivity extends BaseMapActivity implements LocationSource,
        AMapLocationListener, AMap.OnMyLocationChangeListener {

    @BindView(R.id.search_map)
    MapView mSearchMap;

    @BindView(R.id.map_search_food)
    Button mSearchFood;

    @BindView(R.id.map_search_petrol)
    Button mSearchPetrol;

    @BindView(R.id.map_search_supermarket)
    Button mSearchSupermarket;

    @BindView(R.id.map_search_toilet)
    Button mSearchToilet;

    private AMap mAMap;

    //定位样式
    private MyLocationStyle mLocationStyle;

    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    private AMapLocationClientOption mLocationOption;

    private LocationSource.OnLocationChangedListener mListener;
    private MapPoiAdapter poiAdapter;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);
        ButterKnife.bind(this);
        mSearchMap.onCreate(savedInstanceState);
        initMap();

//        filterPoiByIntent();
    }

    private void filterPoiByIntent() {
        Intent i = getIntent();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String key = intent.getStringExtra("filter_key");
        showSheetDialog(key, mCurrentCity);
    }

    private void initMap() {
        mAMap = mSearchMap.getMap();
        mAMap.setMapType(AMap.MAP_TYPE_NORMAL);
        mAMap.getUiSettings().setLogoBottomMargin(-50);

        mLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        mLocationStyle.interval(5000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        mLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        mLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        mAMap.setMyLocationStyle(mLocationStyle);//设置定位蓝点的Style
        mLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        mLocationStyle.showMyLocation(true);
        mAMap.getUiSettings().setZoomControlsEnabled(false);
        mAMap.getUiSettings().setLogoBottomMargin(-50);
        mAMap.setMyLocationStyle(mLocationStyle);
        mAMap.setTrafficEnabled(true);
        mAMap.setLocationSource(this);

        mAMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        mAMap.setOnMyLocationChangeListener(this);

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Timber.d(aMapLocation.getLatitude() + "=====" + aMapLocation.getLongitude());
        LatLng currentLoc = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
        Timber.d("定上位置");
        this.mCurrentLoc = currentLoc;
        this.mCurrentCity = aMapLocation.getCity();
        if (mListener != null) {
            if (aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点

                this.mCurrentPosition = aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getStreet()
                        + aMapLocation.getStreetNum() + aMapLocation.getAoiName();
                this.mLatitude = aMapLocation.getLatitude();
                this.mLongitude = aMapLocation.getLongitude();

                mAMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                mAMap.moveCamera(CameraUpdateFactory.changeTilt(90));
                StringUtils.mCurrentPosition = mCurrentPosition;
                StringUtils.mCurrentCity = aMapLocation.getCity();
                StringUtils.mCurrentProvince = aMapLocation.getProvince();
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Timber.e(errText);
            }
        }
    }

    /**
     * 位置发生变化后，动画聚焦当前位置，并设置地图级别范围
     */
    @Override
    public void onMyLocationChange(Location location) {
        double curLatitude = location.getLatitude();
        double curLongitude = location.getLongitude();
        mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                new LatLng(curLatitude, curLongitude), 17, 90, 0)));
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mLocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mLocationOption.setOnceLocation(true);
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();
        }
    }


    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    @OnClick({R.id.map_search_food
            , R.id.map_search_petrol
            , R.id.map_search_supermarket
            , R.id.map_search_toilet})
    void locFilterClick(View v) {
//        MapPoiListDialog poiDialog = new MapPoiListDialog(this);
//        Bundle b = new Bundle();

        String searchTxt = "";
        switch (v.getId()) {
            case R.id.map_search_food:
//                b.putString("search_key", "美食");
                searchTxt = "美食";
                break;
            case R.id.map_search_petrol:
//                b.putString("search_key", "加油站");
                searchTxt = "加油站";

                break;
            case R.id.map_search_supermarket:
//                b.putString("search_key", "超市");
                searchTxt = "超市";

                break;
            case R.id.map_search_toilet:
                searchTxt = "厕所";

//                b.putString("search_key", "厕所");
                break;
        }
//        String currentCity=
//        b.putString("current_city", mCurrentCity);
//
//        poiDialog.setArguments(b);
//        MapPoiListDialog poiDialog=new MapPoiListDialog(this,b);
//        poiDialog.show();
        showSheetDialog(searchTxt, mCurrentCity);
    }

    private void showSheetDialog(String searchKey, String city) {
        View view = View.inflate(this, R.layout.map_poi_dialog_layout, null);

        RecyclerView mPoiListView = view.findViewById(R.id.map_poi_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mPoiListView.setLayoutManager(layoutManager);
        mPoiListView.addItemDecoration(new RecycleViewDivider(this, DividerItemDecoration.VERTICAL, Color.parseColor("#F5F5F5"), 5));
        poiAdapter = new MapPoiAdapter(this);
        mPoiListView.setAdapter(poiAdapter);
        search(searchKey, city);
        if (null != bottomSheetDialog && bottomSheetDialog.isShowing()) {
            bottomSheetDialog.dismiss();
            bottomSheetDialog = null;
        }
        bottomSheetDialog = new BottomSheetDialog(this);
        BottomSheetBehavior bottomSheetBehavior = bottomSheetDialog.getBehavior();
        bottomSheetBehavior.setPeekHeight(Device.getScreenHeight() / 5 * 3, true);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }


    private void loadPoiMarker(List<PoiItem> poiList, String city) {
        mAMap.clear();
        int icon = R.drawable.poi_food;
        switch (city) {
            case "美食":
                icon = R.drawable.poi_food;
                break;
            case "加油站":
                icon = R.drawable.poi_gas;
                break;
            case "超市":
                icon = R.drawable.poi_supermarket;
                break;
            case "厕所":
                icon = R.drawable.poi_wc;
                break;
        }
        for (PoiItem poiItem : poiList) {
            LatLng latLng = new LatLng(poiItem.getLatLonPoint().getLatitude()
                    , poiItem.getLatLonPoint().getLongitude());
            Marker marker = mAMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(icon))
                    .position(latLng));
            marker.setObject(poiItem);
        }
        mAMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                PoiItem poiItem= (PoiItem) marker.getObject();
                PoiDetailDialog detailDialog=new PoiDetailDialog(NaviSearchActivity.this,poiItem);
                detailDialog.show(getSupportFragmentManager(),"poi_detail");
                return false;
            }
        });


    }


    private void search(String key, String currentCity) {

        String poiSearchType = "汽车服务|汽车销售|" +
                "//汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|" +
                "//住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|" +
                "//金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施";

        //keyWord表示搜索字符串，
        //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
        //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        PoiSearch.Query query = new PoiSearch.Query(key, poiSearchType, currentCity);
        query.setDistanceSort(true);
        query.setPageSize(100);// 设置每页最多返回多少条poiItem

        query.setPageNum(1);// 设置查第一页

        PoiSearch poiSearch = new PoiSearch(this, query);

        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                Timber.d(GsonManager.getInstance().GsonString(poiResult));
                poiAdapter.setData(poiResult.getPois(), mCurrentLoc);
                loadPoiMarker(poiResult.getPois(), key);
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
        LatLonPoint latLonPoint = new LatLonPoint(mCurrentLoc.latitude, mCurrentLoc.longitude);

        poiSearch.setBound(new PoiSearch.SearchBound(latLonPoint, 30000, true));//设置搜索范围
        poiSearch.searchPOIAsyn();// 异步搜索
    }

    @OnClick(R.id.search)
    void toSearch(View v) {
        Intent i = new Intent();
        i.setClass(this, LocSearchActivity.class);
        i.putExtra("current_city",mCurrentCity);
        i.putExtra("lat_lng",mCurrentLoc);
        startActivityForResult(i, 998);
    }

    @OnClick(R.id.btn_back)
    void back(View v) {
        this.finish();
    }

    @OnClick({R.id.map_search_loc, R.id.map_search_traffic})
    void onMapIconClick(View v) {
        int id = v.getId();
        if (R.id.map_search_traffic == id) {
            if (mAMap.isTrafficEnabled()) {
                mAMap.setTrafficEnabled(false);
            } else {
                mAMap.setTrafficEnabled(true);
            }
        } else if (R.id.map_search_loc == id) {
            mLocationOption.setOnceLocation(true);
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSearchMap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mSearchMap.onPause();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearchMap.onDestroy();
    }

    @Override
    public Object getTag() {
        return null;
    }

}
