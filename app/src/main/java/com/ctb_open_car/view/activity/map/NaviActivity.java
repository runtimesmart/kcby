package com.ctb_open_car.view.activity.map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.NaviSetting;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AMapTrafficStatus;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.TrafficProgressBar;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseActivity;
import com.ctb_open_car.base.BaseMapActivity;
import com.ctb_open_car.bean.community.response.RoadData;
import com.ctb_open_car.bean.community.response.feed.FeedDto;
import com.ctb_open_car.bean.community.response.map.RoadDto;
import com.ctb_open_car.engine.manager.GsonManager;
import com.ctb_open_car.eventbus.MessageEvent;
import com.ctb_open_car.presenter.FeedDeletePresenter;
import com.ctb_open_car.presenter.MapTrafficPresenter;
import com.ctb_open_car.presenter.TrafficCheckoutPresenter;
import com.ctb_open_car.utils.DateUtils;
import com.ctb_open_car.view.fragment.dialog.NaviFinishedDialog;
import com.ctb_open_car.view.fragment.dialog.NaviTrafficDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import rx.Subscriber;
import timber.log.Timber;

public class NaviActivity extends BaseMapActivity implements AMapNaviListener
        , LocationSource, AMapLocationListener {
    @BindView(R.id.navi_view)
    AMapNaviView mAMapNaviView;

    @BindView(R.id.navi_traffic_bar)
    TrafficProgressBar mTrafficeBar;

    @BindView(R.id.navi_road_type_icon)
    ImageView mRoadIcon;

    @BindView(R.id.navi_road_distance)
    TextView mRoadDistance;

    @BindView(R.id.navi_road_name)
    TextView mRoadName;

    @BindView(R.id.navi_remain_distance)
    TextView mTotalRemainDistance;

    @BindView(R.id.navi_remain_time)
    TextView mTotalRemainTime;

    @BindView(R.id.navi_bottom_layout)
    LinearLayout mBottomLayout;

    @BindView(R.id.report_road)
    ImageView mReportRoad;

    @BindView(R.id.navi_top_layout)
    RelativeLayout mTopLayout;


    private static final long DIALOG_SHOW = 20000L;
    private static final String TRAFFIC_RADIUS = "4000";
    private AMapNavi mAMapNavi;
    private boolean isStartNavi;
    private RoadData mRoadData;
    private RoadDto mRoadDto;
    private static final int MSG_WHAT_QUERY = 1;
    private static final int MSG_WHAT_POP_DIALOG = 0;

    /**
     * 起点坐标
     */
    private final List<NaviLatLng> startList = new ArrayList<NaviLatLng>();

    /**
     * 终点坐标
     */
    private final List<NaviLatLng> endList = new ArrayList<NaviLatLng>();

    /**
     * 途经点坐标
     */
    private List<NaviLatLng> mWayPointList = new ArrayList<NaviLatLng>();

    /**
     * 声明mLocationOption对象
     */
    private AMapLocationClientOption mLocationOption = null;

    /**
     * 声明mlocationClient对象
     */
    private AMapLocationClient mlocationClient = null;
    private String[] mCurrentLocation = new String[2];
    private TrafficCheckoutPresenter mPresenter;
    /**
     * 导航方式
     */
    private String naviWay;
    private CountDownTimer timer;
    private MapTrafficPresenter mapTrafficPresenter;

    private long mRefreshFrequency = 20000L;
    private static final long POP_DELAY = 30000L;
    private LinkedHashSet<RoadDto> cacheTraffic = new LinkedHashSet<>();
    private HandlerThread mHandlerThread = new HandlerThread("traffic");
    private Handler mTrafficHandler;
    private Handler mQueryHandler;
    private NaviTrafficDialog naviTrafficDialog;
    private Subscriber subscriber;
    private AMap mAMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        mPresenter = new TrafficCheckoutPresenter(this);
        mAMapNaviView.onCreate(savedInstanceState);

        initGPS();
        initMap();
        Intent i = getIntent();
        NaviLatLng naviLatLng = i.getParcelableExtra("lat_lng");
        endList.add(naviLatLng);
        initLocation();
        mapTrafficPresenter = new MapTrafficPresenter(this);

        initTrafficHandler();
        mHandlerThread.start();
    }


    @Subscribe()
    public void getTapMsg(MessageEvent s) {
        if (null == mRoadDto) {
            return;
        }
        if (TrafficCheckoutPresenter.ACTION_TYPE_CORRECT == s.getId()) {
            mPresenter.correctCheck(mRoadDto.getRcId() + "", "1");
            Toasty.info(this, "感谢评价").show();

        } else if (TrafficCheckoutPresenter.ACTION_TYPE_INCORRECT == s.getId()) {
            mPresenter.incorrectCheck(mRoadDto.getRcId() + "", "0");
            Toasty.info(this, "感谢评价").show();

        }
    }

    private void initMap() {
        mAMap = mAMapNaviView.getMap();
        mAMap.getUiSettings().setLogoBottomMargin(-50);
    }

    private void initNavi() {
        //获取AMapNavi实例
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.setUseInnerVoice(true);
        //添加监听回调，用于处理算路成功
        setAmapNaviViewOptions();
        mAMapNavi.addAMapNaviListener(this);
        NaviSetting naviSetting = mAMapNavi.getNaviSetting();
        naviSetting.setScreenAlwaysBright(true);
    }


    /**
     * 初始化请求数据handler，并发起请求
     **/
    private void requestDataHandler() {
        mQueryHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (MSG_WHAT_QUERY == msg.what) {
                    //发起请求
                    if (!TextUtils.isEmpty(mCurrentLocation[0])) {
                        mapTrafficPresenter.requestTraffic(mCurrentLocation[0], mCurrentLocation[1], TRAFFIC_RADIUS);
                    }
                    //轮询
                    mQueryHandler.sendEmptyMessageDelayed(MSG_WHAT_QUERY, mRefreshFrequency);
                }
            }
        };
        mQueryHandler.sendEmptyMessage(MSG_WHAT_QUERY);
    }

    private List<Marker> mDestroyMarkers = new ArrayList<>();

    private void renderTrafficMarker() {
        for (Marker marker : mDestroyMarkers) {
            marker.destroy();
        }
        mDestroyMarkers.clear();
        Iterator<RoadDto> iRoadDto = cacheTraffic.iterator();
        while (iRoadDto.hasNext()) {
            RoadDto roadDto = iRoadDto.next();
            int icon = R.drawable.icon_traffic_accident;
            // 1：交警路检、2：违停贴条、3：免费停车、4：交警拍照、5：交通事故、6：严重拥堵
            switch (roadDto.getRcType()) {
                case 1:
                    icon = R.drawable.icon_police_check;
                    break;
                case 2:
                    icon = R.drawable.icon_traffic_ticket;

                    break;
                case 3:
                    icon = R.drawable.icon_free_park;

                    break;
                case 4:
                    icon = R.drawable.icon_traffic_camera;

                    break;
                case 5:
                    icon = R.drawable.icon_traffic_accident;

                    break;
                case 6:
                    icon = R.drawable.icon_traffic_jam;
                    break;
            }
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(roadDto.getLatitude(), roadDto.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromResource(icon));
            Marker marker = mAMap.addMarker(markerOptions);
            mDestroyMarkers.add(marker);
            marker.setObject(roadDto);
            mAMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    RoadDto roadDto = (RoadDto) marker.getObject();
                    popDialog(roadDto);
                    return true;
                }
            });
        }

    }

    @SuppressLint("HandlerLeak")
    private void initTrafficHandler() {
        mTrafficHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (MSG_WHAT_POP_DIALOG == msg.what) {
                    if (null != naviTrafficDialog) {
                        if (!naviTrafficDialog.getAudioPlaying()) {
                            naviTrafficDialog.dismiss();
                            naviTrafficDialog = null;
                        }
                    } else {
                        /**弹框为空后，在获取数据，弹框、播音；*/
                        Iterator<RoadDto> iRoadDto = cacheTraffic.iterator();
                        if (iRoadDto.hasNext()) {
                            RoadDto dto = iRoadDto.next();
                            mRoadDto = dto;
                            Timber.i("移除" + GsonManager.getInstance().GsonString(dto));
                            Timber.i("移除前" + cacheTraffic.size());

                            iRoadDto.remove();
                            Timber.i("移除后" + cacheTraffic.size());

                            popDialog(dto);
                        }

                    }
                    mTrafficHandler.sendEmptyMessageDelayed(MSG_WHAT_POP_DIALOG, POP_DELAY);
                }
            }
        };
    }

    /**
     * 获取数据，并弹框，播放
     **/
    private void popDialog(RoadDto dto) {
        naviTrafficDialog = new NaviTrafficDialog();
        Bundle b = new Bundle();
        b.putSerializable("traffic", dto);
        naviTrafficDialog.setArguments(b);
        naviTrafficDialog.show(getSupportFragmentManager(), "traffic");
    }

    /**
     * 高德地图--获取定位坐标
     */
    public void initLocation() {
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
        //如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会。
        mLocationOption.setOnceLocationLatest(true);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
    }


    /**
     * 获取到路况数据后，存入队列，等待定时器获取
     */
    public void updateTrafficData(RoadData roadData) {
        mRoadData = roadData;
        //如果获取到的刷新频率与当前不一致，则设置
        if (null != mRoadData && mRoadData.getRefreshFrequency() != mRefreshFrequency) {
            mRefreshFrequency = roadData.getRefreshFrequency() * 1000;
        }
//        Timber.i("查询获得数据" + GsonManager.getInstance().GsonString(roadData));
        //去重
        cacheTraffic.addAll(roadData.getRoadConditionList());
        renderTrafficMarker();
        Timber.i("路况数量:" + cacheTraffic.size());
//        Timber.i("去重后" + GsonManager.getInstance().GsonString(cacheTraffic));
    }

    /**
     * 设置导航参数
     */
    private void setAmapNaviViewOptions() {
        if (mAMapNaviView == null) {
            return;
        }
        AMapNaviViewOptions viewOptions = new AMapNaviViewOptions();
        viewOptions.setSensorEnable(true);
        viewOptions.setAfterRouteAutoGray(true);
        // 设置是否显示路口放大图(实景图)
        viewOptions.setRealCrossDisplayShow(true);
        viewOptions.setModeCrossDisplayShow(false);
        viewOptions.setSettingMenuEnabled(false);//设置菜单按钮是否在导航界面显示
        viewOptions.setReCalculateRouteForYaw(true);//设置偏航时是否重新计算路径
        viewOptions.setReCalculateRouteForTrafficJam(true);//前方拥堵时是否重新计算路径
        viewOptions.setTrafficInfoUpdateEnabled(true);//设置交通播报是否打开
        viewOptions.setCameraInfoUpdateEnabled(true);//设置摄像头播报是否打开
        viewOptions.setAutoChangeZoom(true);
        viewOptions.setScreenAlwaysBright(true);//设置导航状态下屏幕是否一直开启。
        viewOptions.setTrafficBarEnabled(true);  //设置 返回路况光柱条是否显示（只适用于驾车导航，需要联网）
        viewOptions.setMonitorCameraEnabled(true); //设置摄像头图标是否显示 是
        viewOptions.setAutoLockCar(true);
        viewOptions.setLayoutVisible(false);  //设置导航界面UI是否显示
        // 设置路线上的摄像头气泡是否显示
        viewOptions.setCameraBubbleShow(true);
        // 设置是否显示道路信息view
        viewOptions.setLaneInfoShow(true);
        //设置默认路况显示
        mAMapNaviView.setTrafficLine(true);
        // 右侧的全程光柱的是否显示(默认)
        viewOptions.setTrafficBarEnabled(false);
        // 设置 导航路线的路况是否显示
        viewOptions.setTrafficLine(true);
        viewOptions.setTilt(60);  //2D显示
        mAMapNaviView.setViewOptions(viewOptions);
    }


    private void setNaviIcon(int type) {
        if (type > 10) {
            mRoadIcon.setImageDrawable(getDrawable(R.drawable.icon_9_straight));
            return;
        }
        switch (type) {
            case 2:
                mRoadIcon.setImageDrawable(getDrawable(R.drawable.icon_2_left));
                break;
            case 3:
                mRoadIcon.setImageDrawable(getDrawable(R.drawable.icon_3_right));
                break;
            case 4:
                mRoadIcon.setImageDrawable(getDrawable(R.drawable.icon_4_left_front));
                break;
            case 5:
                mRoadIcon.setImageDrawable(getDrawable(R.drawable.icon_5_right_front));
                break;
            case 6:
                mRoadIcon.setImageDrawable(getDrawable(R.drawable.icon_6_left_back));
                break;
            case 7:
                mRoadIcon.setImageDrawable(getDrawable(R.drawable.icon_7_right_back));
                break;
            case 8:
                mRoadIcon.setImageDrawable(getDrawable(R.drawable.icon_8_left_turn_around));
                break;
            case 9:
                mRoadIcon.setImageDrawable(getDrawable(R.drawable.icon_9_straight));
                break;
        }
    }


    @Override
    public void onInitNaviFailure() {

    }

    /**
     * 记录当前位置
     **/
    private void recordCurrentLocation(AMapLocation aMapLocation) {
        //记录当前位置
        mCurrentLocation[0] = String.valueOf(aMapLocation.getLongitude());
        mCurrentLocation[1] = String.valueOf(aMapLocation.getLatitude());
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            Timber.i("请求位置" + aMapLocation.getLongitude() + "--" + aMapLocation.getLatitude());
            recordCurrentLocation(aMapLocation);
            LatLng currentLoc = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());

            this.mCurrentLoc = currentLoc;
            this.mCurrentCity = aMapLocation.getCity();
            this.mCurrentPosition = aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getStreet()
                    + aMapLocation.getStreetNum() + aMapLocation.getAoiName();
            this.mLatitude = aMapLocation.getLatitude();
            this.mLongitude = aMapLocation.getLongitude();

            if (aMapLocation.getErrorCode() == 0) {
                if (!isStartNavi) {
                    //定位成功回调信息，设置相关消息
                    startList.add(new NaviLatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                    initNavi();
                    onInitNaviSuccess();
                }

            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:" + aMapLocation.getErrorCode() +
                        ", errInfo:" + aMapLocation.getErrorInfo());
                Toast.makeText(this, "location Error, ErrCode:" + aMapLocation.getErrorCode() +
                        ", errInfo:" + aMapLocation.getErrorInfo(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onInitNaviSuccess() {
        /**
         * 方法:
         *   int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute);
         * 参数:
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         * 说明:
         *      以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
         * 注意:
         *      不走高速与高速优先不能同时为true
         *      高速优先与避免收费不能同时为true
         */
        int strategy = 0;
        try {
            strategy = mAMapNavi.strategyConvert(true, false, false, true, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAMapNavi.calculateDriveRoute(startList, endList, mWayPointList, strategy);
    }

    @Override
    public void onStartNavi(int i) {
        if (isStartNavi) {
            return;
        }
        isStartNavi = true;
        mBottomLayout.setVisibility(View.VISIBLE);
        mTopLayout.setVisibility(View.VISIBLE);
        mReportRoad.setVisibility(View.VISIBLE);
        //初始化handler，并发起请求
        requestDataHandler();

        //初始化轮询路况弹框
        mTrafficHandler.sendEmptyMessage(MSG_WHAT_POP_DIALOG);
    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {
        int allLength = mAMapNavi.getNaviPath().getAllLength();
        List<AMapTrafficStatus> trafficStatuses = mAMapNavi.getTrafficStatuses(0, 0);
        mTrafficeBar.update(allLength, naviInfo.getPathRetainDistance(), trafficStatuses);
        mTrafficeBar.setVisibility(View.VISIBLE);

        int type = naviInfo.getIconType();
        setNaviIcon(type);

        int pathRetainDistance = naviInfo.getPathRetainDistance();
        if (pathRetainDistance >= 1000) {
            float f = (float) pathRetainDistance / 1000.0F;
            f = (float) Math.round(f * 10.0F) / 10.0F;
            mTotalRemainDistance.setText(f + " 公里");
        } else {
            mTotalRemainDistance.setText(pathRetainDistance + " 米");
        }

        //设置中上的导航信息
        int mCurStepRetainDistance = naviInfo.getCurStepRetainDistance();
        String nextRoadName = naviInfo.getNextRoadName();

        mRoadName.setText(nextRoadName);
        String cur = "0";
        if (mCurStepRetainDistance >= 1000) {
            float f = (float) mCurStepRetainDistance / 1000.0F;
            f = (float) Math.round(f * 10.0F) / 10.0F;
            mRoadDistance.setText(f + "公里");
        } else {
            mRoadDistance.setText(mCurStepRetainDistance + "米");
        }

        //设置中下剩余时间
        int time = naviInfo.getPathRetainTime();

        mTotalRemainTime.setText("" + DateUtils.secToTime(time));
    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

        releaseHandler();

        NaviFinishedDialog dialog = new NaviFinishedDialog(this);
        Bundle bundle = new Bundle();
        bundle.putInt("all_length", mAMapNavi.getNaviPath().getAllLength());
        bundle.putInt("all_time", mAMapNavi.getNaviPath().getAllTime());
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "navi_finished");
    }

    @Override
    public void onArriveDestination() {
        releaseHandler();
        NaviFinishedDialog dialog = new NaviFinishedDialog(this);
        Bundle bundle = new Bundle();
        bundle.putInt("all_length", mAMapNavi.getNaviPath().getAllLength());
        bundle.putInt("all_time", mAMapNavi.getNaviPath().getAllTime());
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "navi_finished");


    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }


    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
//        mAMapNavi.startNavi(NaviType.EMULATOR);
//        mAMapNavi.setEmulatorNaviSpeed(120);
        mAMapNavi.startNavi(NaviType.GPS);

    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {

    }


    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }

    @OnClick(R.id.navi_exit)
    void exitNavi(View v) {
        confirmDialog();
    }

    private void confirmDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("退出导航")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAct();

                    }
                }).show();
    }

    /**
     * 释放资源，结束act
     */
    public void finishAct() {
        mAMapNavi.stopSpeak();
        mAMapNavi.stopNavi();
        mAMapNaviView.onDestroy();
        releaseHandler();
        this.finish();
    }

    @Override
    public void onBackPressed() {
        confirmDialog();
    }

    private void releaseHandler() {
        if (null != mTrafficHandler) {
            mTrafficHandler.removeCallbacksAndMessages(null);
        }
        if (null != mQueryHandler) {
            mQueryHandler.removeCallbacksAndMessages(null);
        }
        if (null != mHandlerThread) {
            mHandlerThread.quit();
        }
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mAMapNaviView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAMapNaviView.onDestroy();
    }

    @Override
    public Object getTag() {
        return null;
    }
}
