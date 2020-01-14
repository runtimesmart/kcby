package com.ctb_open_car.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ctb_open_car.CTBApplication;
import com.ctb_open_car.MainActivity;
import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import timber.log.Timber;

import com.ctb_open_car.bean.community.response.SNSMapData;
import com.ctb_open_car.bean.community.response.map.SnsMapFeedDto;
import com.ctb_open_car.presenter.SNSMapPresenter;
import com.ctb_open_car.utils.Device;
import com.ctb_open_car.utils.StringUtils;
import com.ctb_open_car.view.DrawCenterButton;
import com.ctb_open_car.view.activity.map.NaviSearchActivity;
import com.ctb_open_car.view.activity.vehicletools.VehicleToolsActivity;
import com.ctb_open_car.view.fragment.cluster.another.ClusterOverlayMerchant;
import com.ctb_open_car.view.fragment.cluster.item.Cluster;
import com.ctb_open_car.view.fragment.cluster.item.ClusterClickListener;
import com.ctb_open_car.view.fragment.cluster.item.ClusterItem;
import com.ctb_open_car.view.fragment.cluster.item.ClusterItemImp;
import com.ctb_open_car.view.fragment.cluster.item.ClusterOverlay;
import com.ctb_open_car.view.fragment.dialog.MapFeedFilterDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 地图
 */
public class MapFragment extends BaseFragment implements LocationSource,
        AMapLocationListener, AMap.OnMyLocationChangeListener, AMap.OnCameraChangeListener {


    public MapFragment() {
    }

    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.map_btn_community)
    Button mBtnCommunity;

    @BindView(R.id.map_btn_navi)
    Button mBtnNavi;

    @BindView(R.id.map_top_filter)
    RadioGroup mFilterGroup;

    @BindView(R.id.map_filter_nearby)
    DrawCenterButton mFilterNearby;

    @BindView(R.id.map_filter_hot)
    DrawCenterButton mFilterHot;
    @BindView(R.id.map_filter_focus)
    DrawCenterButton mFilterFocus;
    @BindView(R.id.map_filter_customize)
    DrawCenterButton mFilterCustomize;

    private AMap mAMap;
    //定位样式
    private MyLocationStyle mLocationStyle;

    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    private AMapLocationClientOption mLocationOption;

    private LocationSource.OnLocationChangedListener mListener;

    private LatLng mCurrentLoc;
    private String mCurrentPosition; //定位当前位置

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, v);
        initView();
        initGPS();
        return v;
    }

    private void initView() {
        mFilterGroup.setOnCheckedChangeListener(radioGroupListener);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        mAMap = mMapView.getMap();

        initLocation();
    }

    private void initLocation() {
        mAMap.setMapType(AMap.MAP_TYPE_NORMAL);
        mLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        mLocationStyle.interval(5000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        mLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        mLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        mAMap.setMyLocationStyle(mLocationStyle);//设置定位蓝点的Style
        mLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        mLocationStyle.showMyLocation(true);
        mAMap.getUiSettings().setZoomControlsEnabled(false);
        mAMap.getUiSettings().setLogoBottomMargin(-50);
        mAMap.setTrafficEnabled(true);
        mAMap.setLocationSource(this);
        mAMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        mAMap.setOnMyLocationChangeListener(this);

        mAMap.setOnCameraChangeListener(this);

        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
    }


    @Override
    public void activate(LocationSource.OnLocationChangedListener listener) {
        mListener = listener;
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(getContext());
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


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        clusterOverlayUser.updateClusters();
    }

    /**
     * 定位回调
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Timber.d(aMapLocation.getLatitude() + "=====" + aMapLocation.getLongitude());
        mCurrentLoc = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());

        Timber.d("定上位置");
        if (mListener != null) {
            if (aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                mCurrentPosition = aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getStreet()
                        + aMapLocation.getStreetNum() + aMapLocation.getAoiName();

                mAMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                mAMap.moveCamera(CameraUpdateFactory.changeTilt(90));

//                mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
//                        new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 19, 45, 10)));
                StringUtils.mCurrentPosition = mCurrentPosition;
                StringUtils.mCurrentCity = aMapLocation.getCity();
                StringUtils.mCurrentProvince = aMapLocation.getProvince();
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Timber.e(errText);
            }
        }
        ((MainActivity) getActivity()).setmCurrentLoc(mCurrentLoc, mCurrentPosition);
        SNSMapPresenter presenter = new SNSMapPresenter(this);
        presenter.requestSNSList(aMapLocation.getLongitude() + "", aMapLocation.getLatitude() + "", 50000 + "");
    }

    @Override
    public void onMyLocationChange(Location location) {
        double curLatitude = location.getLatitude();
        double curLongitude = location.getLongitude();
        mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                new LatLng(curLatitude, curLongitude), 17, 90, 0)));
    }


    private ClusterOverlay clusterOverlayUser;
    private ClusterOverlayMerchant clusterOverlayMerchant;
    private List<ClusterItem> clusterItems = new ArrayList<>();
    private List<ClusterItem> clusterItemsMerchant = new ArrayList<>();
    private Map<Integer, Drawable> mBackDrawAbles = new HashMap<Integer, Drawable>();
    private Map<Integer, Drawable> mBackDrawAblesMerchant = new HashMap<Integer, Drawable>();
    private final LatLng centerLocation = new LatLng(31.206078, 121.602948);
    private final String TYPE_MERCHANT = "02";
    private final String TYPE_USER = "01";
    private final String TAG = "Moos";
    private int clusterRadius = 80;


    private RadioGroup.OnCheckedChangeListener radioGroupListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.map_filter_nearby:
                    mFilterNearby.setChecked(true);
                    break;
                case R.id.map_filter_hot:
                    mFilterHot.setChecked(true);
                    break;
                case R.id.map_filter_focus:
                    mFilterFocus.setChecked(true);

                    break;
                case R.id.map_filter_customize:
//                    mFilterCustomize.setChecked(true);

                    break;
            }
        }
    };

    @OnClick(R.id.map_filter_customize)
    void showBottomDilaog(View v) {
        MapFeedFilterDialog dialog = new MapFeedFilterDialog(getContext());
        dialog.show(getFragmentManager(), "map_dialog");
    }


    /**
     * by moos on 2018/01/12
     * func:添加个人聚合点
     *
     * @param sourceList
     */
    private void addUserClustersToMap(List<SnsMapFeedDto> sourceList) {

        for (int i = 0; i < sourceList.size(); i++) {
            LatLng latLng = new LatLng(sourceList.get(i).getLatitude(), sourceList.get(i).getLongitude());
            ClusterItemImp clusterImp = new ClusterItemImp(latLng, TYPE_USER);
            clusterItems.add(clusterImp);
        }
        if (clusterOverlayUser == null) {
            clusterOverlayUser = new ClusterOverlay(mAMap, clusterItems, Device.dip2px(clusterRadius), sourceList, getActivity());
        } else {
            clusterOverlayUser.onDestroy();
            clusterOverlayUser = null;
            clusterOverlayUser = new ClusterOverlay(mAMap, clusterItems, Device.dip2px(clusterRadius), sourceList, getActivity());
        }

//        clusterOverlayUser.setClusterRenderer(new ClusterRender() {
//            @Override
//            public Drawable getDrawAble(int clusterNum) {
//                if (clusterNum <= 5) {
//                    Drawable bitmapDrawable = mBackDrawAbles.get(2);
//                    if (bitmapDrawable == null) {
//                        bitmapDrawable =
//                                CTBApplication.getContext().getResources().getDrawable(
//                                        R.mipmap.ic_launcher);
//                        mBackDrawAbles.put(2, bitmapDrawable);
//                    }
//                    return bitmapDrawable;
//                } else {
//                    Drawable bitmapDrawable = mBackDrawAbles.get(3);
//                    if (bitmapDrawable == null) {
//                        bitmapDrawable =
//                                CTBApplication.getContext().getResources().getDrawable(
//                                        R.mipmap.ic_launcher);
//                        mBackDrawAbles.put(3, bitmapDrawable);
//                    }
//                    return bitmapDrawable;
//                }
//            }
//        });
//        clusterOverlayUser.setOnClusterClickListener(new ClusterClickListener() {
//            @Override
//            public void onClick(Marker marker, List<ClusterItem> clusterItems) {
//                Toasty.error(getContext(),"cluster show").show();
//                if (mAMap.getCameraPosition().zoom <= 18) {
//                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
//                    for (ClusterItem clusterItem : clusterItems) {
//                        builder.include(clusterItem.getPosition());
//                    }
//                    LatLngBounds latLngBounds = builder.build();
//                    mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 10));
//                }

//            }
//        });

    }


    /**
     * 添加marker view
     */
    public void inflateMarkerView(SNSMapData snsDto) {
//        clearMarkers();
        addUserClustersToMap(snsDto.getFeedList());

//        for (SnsMapFeedDto snsData : snsDto.getFeedList()) {
//
//            View view = LayoutInflater.from(getActivity()).inflate(
//                    R.layout.map_marker_layout, null);
//            EllipseTextView markerTxt = view.findViewById(R.id.map_marker_feed);
//            ImageView mainIcon = view.findViewById(R.id.maker_avatar_icon);
//            ImageView moodIcon = view.findViewById(R.id.maker_mood_icon);
//            ImageView feedThumb = view.findViewById(R.id.marker_feed_thumb);
//            feedThumb.setScaleType(ImageView.ScaleType.CENTER_CROP);
//
//            LatLng latLng = new LatLng(snsData.getLatitude(), snsData.getLongitude());
//            Cluster cluster = new Cluster(latLng);
//
//            markerTxt.setText(snsData.getFeedContent());
//
//            //动态图标
//            Glide.with(CTBApplication.getContext()).load(snsData.getFeedImage())
//                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
//                    .into(new SimpleTarget<Drawable>() {
//                        @Override
//                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                            feedThumb.setImageDrawable(resource);
//                            loadUserIcon(snsData, mainIcon, moodIcon, latLng, view, cluster);
//                        }
//
//                        @Override
//                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
//                            super.onLoadFailed(errorDrawable);
//                            loadUserIcon(snsData, mainIcon, moodIcon, latLng, view, cluster);
//                        }
//                    });
//
//
//            mAMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
//                @Override
//                public boolean onMarkerClick(Marker marker) {
//                    long feedId = (long) marker.getObject();
//                    Timber.d("get--" + feedId);
//                    Intent intent = new Intent();
//                    intent.setClass(getContext(), FeedsDetailActivity.class);
//                    intent.putExtra("feedId", feedId);
//                    getContext().startActivity(intent);
//                    return true;
//                }
//            });

//        }

    }


    private void loadUserIcon(SnsMapFeedDto snsData, ImageView mainIcon
            , ImageView moodIcon, LatLng latLng, View view, Cluster cluster) {
        //用户头像
        Glide.with(CTBApplication.getContext()).load(snsData.getFeedUser().getUserIcon().getResourceUrl())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        mainIcon.setImageDrawable(resource);
                        loadMoodIcon(snsData, moodIcon, latLng, view, cluster);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        mainIcon.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.default_avar_icon));
                        loadMoodIcon(snsData, moodIcon, latLng, view, cluster);
                    }
                });
    }

    private void loadMoodIcon(SnsMapFeedDto snsData, ImageView mood, LatLng latLng,
                              View view, Cluster cluster) {
        //用户心情图标
        Glide.with(CTBApplication.getContext()).load(snsData.getFeedUser().getUserMoodIcon())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))

                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        mood.setImageDrawable(resource);
                        Marker marker = mAMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .icon(BitmapDescriptorFactory.fromView(view)));
                        marker.setObject(snsData.getFeedId());


                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        mood.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.user_default_mood));


                        Marker marker = mAMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .icon(BitmapDescriptorFactory.fromView(view)));
                        marker.setObject(snsData.getFeedId());


//                        Animation animation = new ScaleAnimation(0, 1, 0, 1);
//                        long duration = 300L;
//                        animation.setDuration(duration);
//                        animation.setInterpolator(new LinearInterpolator());
//                        marker.setAnimation(animation);
//                        marker.startAnimation();
                    }
                });
    }

    @OnClick({R.id.map_btn_request_loc, R.id.map_btn_tool})
    void onRequestLoc(View v) {
        switch (v.getId()) {
            case R.id.map_btn_request_loc:
                mLocationOption.setOnceLocation(true);
                mLocationClient.setLocationOption(mLocationOption);
                mLocationClient.startLocation();
                break;
            case R.id.map_btn_tool:
                Intent intent = new Intent(getActivity(), VehicleToolsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    @OnClick(R.id.map_btn_navi)
    void toNavi() {
        Intent i = new Intent();
        i.setClass(getContext(), NaviSearchActivity.class);
        getContext().startActivity(i);
    }

    @OnClick(R.id.map_btn_community)
    void toCommunityClick(View v) {

        ((MainActivity) getActivity()).switchCommunity(true);

//        // TODO Auto-generated method stub
//        RotationHelper rotateHelper = new RotationHelper(getActivity(), DisplayNextView.Constants.KEY_FIRST_INVERSE);
//        rotateHelper.applyFirstRotation(layout1, 0, -90);
    }

//    @OnClick(R.id.map_filter_nearby)
//    void onNearbyFilter(View v) {
//
//    }
//
//    @OnClick(R.id.map_filter_hot)
//    void onHotFilter(View v) {
//
//        Drawable drawable = getResources().getDrawable(R.drawable.dot);
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//这句一定要加
//        mFilterHot.setCompoundDrawables(drawable, null, null, null);
//    }
//
//    @OnClick(R.id.map_filter_focus)
//    void onFocusFilter(View v) {
//
//    }
//
//    @OnClick(R.id.map_filter_customize)
//    void onCustomizeyFilter(View v) {
//
//    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected String getTAG() {
        return null;
    }


}



