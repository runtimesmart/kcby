package com.ctb_open_car.view.fragment.cluster.item;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.AlphaAnimation;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ctb_open_car.CTBApplication;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.community.response.map.SnsMapFeedDto;
import com.ctb_open_car.ui.RecycleViewDivider;
import com.ctb_open_car.utils.Device;
import com.ctb_open_car.view.EllipseTextView;
import com.ctb_open_car.view.activity.community.FeedsDetailActivity;
import com.ctb_open_car.view.adapter.map.MapFeedAdapter;
import com.ctb_open_car.view.fragment.dialog.MapPoiAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import timber.log.Timber;

import static com.ctb_open_car.CTBApplication.getContext;


/**
 * Created by yiyi.qi on 16/10/10.
 * 整体设计采用了两个线程,一个线程用于计算组织聚合数据,一个线程负责处理Marker相关操作
 */
public class ClusterOverlay {
    private AMap mAMap;
    private SoftReference<Context> mContext;
    private List<ClusterItem> mClusterItems;
    private List<Cluster> mClusters;
    private int mClusterSize;
    private ClusterClickListener mClusterClickListener;
    private ClusterRender mClusterRender;
    private List<Marker> mAddMarkers = new ArrayList<Marker>();
    private List<SnsMapFeedDto> mSourceList;
    private List<SnsMapFeedDto> mVisibleList;
    private double mClusterDistance;
    private LruCache<Integer, BitmapDescriptor> mLruCache;
    private HandlerThread mMarkerHandlerThread = new HandlerThread("addMarker");
    private HandlerThread mSignClusterThread = new HandlerThread("calculateCluster");
    private Handler mMarkerhandler;
    private Handler mSignClusterHandler;
    private float mPXInMeters;
    private boolean mIsCanceled = false;

    /**
     * 构造函数
     *
     * @param amap
     * @param clusterSize 聚合范围的大小（指点像素单位距离内的点会聚合到一个点显示）
     * @param context
     */
    public ClusterOverlay(AMap amap, int clusterSize, Context context) {
//        this(amap, null, clusterSize, context);


    }

    /**
     * 构造函数,批量添加聚合元素时,调用此构造函数
     *
     * @param amap
     * @param clusterItems 聚合元素
     * @param clusterSize
     * @param context
     */
    public ClusterOverlay(AMap amap, List<ClusterItem> clusterItems,
                          int clusterSize, List<SnsMapFeedDto> sourceList, Context context) {
        //默认最多会缓存80张图片作为聚合显示元素图片,根据自己显示需求和app使用内存情况,可以修改数量
        mLruCache = new LruCache<Integer, BitmapDescriptor>(80) {
            protected void entryRemoved(boolean evicted, Integer key, BitmapDescriptor oldValue, BitmapDescriptor newValue) {
                oldValue.getBitmap().recycle();
            }
        };
        if (clusterItems != null) {
            mClusterItems = clusterItems;
        } else {
            mClusterItems = new ArrayList<ClusterItem>();
        }
        this.mSourceList = sourceList;
        this.mVisibleList = new ArrayList<>();
        mContext = new SoftReference<>(context);
        mClusters = new ArrayList<Cluster>();
        this.mAMap = amap;
        mClusterSize = clusterSize;
        mPXInMeters = mAMap.getScalePerPixel();
        mClusterDistance = mPXInMeters * mClusterSize;
        initThreadHandler();
        assignClusters();
    }

    /**
     * 设置聚合点的点击事件
     *
     * @param clusterClickListener
     */
    public void setOnClusterClickListener(ClusterClickListener clusterClickListener) {
        mClusterClickListener = clusterClickListener;
    }

    /**
     * 添加一个聚合点
     *
     * @param item
     */
    public void addClusterItem(ClusterItem item) {
        Message message = Message.obtain();
        message.what = SignClusterHandler.CALCULATE_SINGLE_CLUSTER;
        message.obj = item;
        mSignClusterHandler.sendMessage(message);
    }

    /**
     * 设置聚合元素的渲染样式，不设置则默认为气泡加数字形式进行渲染
     *
     * @param render
     */
    public void setClusterRenderer(ClusterRender render) {
        mClusterRender = render;
    }

    public void onDestroy() {
        mIsCanceled = true;
        mSignClusterHandler.removeCallbacksAndMessages(null);
        mMarkerhandler.removeCallbacksAndMessages(null);
        mSignClusterThread.quit();
        mMarkerHandlerThread.quit();
        for (Marker marker : mAddMarkers) {
            marker.remove();

        }
        mAddMarkers.clear();
        mLruCache.evictAll();
    }

    //初始化Handler
    private void initThreadHandler() {
        mMarkerHandlerThread.start();
        mSignClusterThread.start();
        mMarkerhandler = new MarkerHandler(mMarkerHandlerThread.getLooper());
        mSignClusterHandler = new SignClusterHandler(mSignClusterThread.getLooper());
    }


    /**
     * 将聚合元素添加至地图上
     */
    private void addClusterToMap(List<Cluster> clusters) {
        mVisibleList.clear();

        ArrayList<Marker> removeMarkers = new ArrayList<>();
        removeMarkers.addAll(mAddMarkers);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        MyAnimationListener myAnimationListener = new MyAnimationListener(removeMarkers);
        for (Marker marker : removeMarkers) {
            marker.setAnimation(alphaAnimation);
            marker.setAnimationListener(myAnimationListener);
            marker.startAnimation();
        }

        for (Cluster cluster : clusters) {
            addSingleClusterToMap(cluster);
        }
    }

    private AlphaAnimation mADDAnimation = new AlphaAnimation(0, 1);

    /**
     * 将单个聚合元素添加至地图显示
     *
     * @param cluster
     */
    private void addSingleClusterToMap(Cluster cluster) {
//        LatLng latlng = cluster.getCenterLatLng();
//        MarkerOptions markerOptions = cl;
//        markerOptions.anchor(0.5f, 0.5f)
//                .icon(getBitmapDes(cluster.getClusterCount())).position(latlng);
//        Marker marker = mAMap.addMarker(markerOptions);
//        marker.setAnimation(mADDAnimation);
//        marker.setObject(cluster);
//
//        marker.startAnimation();
//        cluster.setMarker(marker);
//        mAddMarkers.add(marker);
        for (SnsMapFeedDto snsMapData : mSourceList) {
            for (ClusterItem clusterItem : cluster.getClusterItems()) {
                if (snsMapData.getLatitude() == clusterItem.getPosition().latitude) {
                    if (!mVisibleList.contains(snsMapData)) {
                        mVisibleList.add(snsMapData);
                    }
                }
            }
        }
        for (SnsMapFeedDto snsMapData : mSourceList) {
            if (snsMapData.getLatitude() == cluster.getCenterLatLng().latitude) {
                //保存可视区域内的marker数据

                View view = LayoutInflater.from(mContext.get()).inflate(
                        R.layout.map_marker_layout, null);
                EllipseTextView markerTxt = view.findViewById(R.id.map_marker_feed);
                ImageView mainIcon = view.findViewById(R.id.maker_avatar_icon);
                ImageView moodIcon = view.findViewById(R.id.maker_mood_icon);
                ImageView feedThumb = view.findViewById(R.id.marker_feed_thumb);
                TextView markerNum = view.findViewById(R.id.marker_avatar_num);
                feedThumb.setScaleType(ImageView.ScaleType.CENTER_CROP);
                if (cluster.getClusterCount() > 1) {
                    int markerCount = cluster.getClusterCount();
                    String showNum = (markerCount > 99) ? "99+" : String.valueOf(markerCount);
                    markerNum.setText(showNum);
                } else {
                    markerNum.setVisibility(View.GONE);
                }
                markerTxt.setText(snsMapData.getFeedContent());
                LatLng latLng = new LatLng(snsMapData.getLatitude(), snsMapData.getLongitude());

                //动态图标
                Glide.with(CTBApplication.getInstance()).load(snsMapData.getFeedImage())
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                feedThumb.setImageDrawable(resource);
                                loadUserIcon(snsMapData, mainIcon, moodIcon, latLng, view, cluster.getClusterCount());
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                super.onLoadFailed(errorDrawable);
                                loadUserIcon(snsMapData, mainIcon, moodIcon, latLng, view, cluster.getClusterCount());
                            }
                        });
            }
        }

        mAMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String obj = (String) marker.getObject();
                String[] str = obj.split("&");
                int num = Integer.parseInt(str[1]);
                if (num > 1) {
                    showSheetDialog();
                } else {
                    String feedId = str[0];
                    Timber.d("get--" + feedId);
                    Intent intent = new Intent();
                    intent.setClass(mContext.get(), FeedsDetailActivity.class);
                    intent.putExtra("feedId", Long.parseLong(feedId));
                    mContext.get().startActivity(intent);
                }

                return true;
            }
        });
    }


    /**
     * 显示聚合的动态数据
     */
    private void showSheetDialog() {
        View view = View.inflate(mContext.get(), R.layout.map_feed_dialog, null);
        TextView ellipsize = view.findViewById(R.id.ellipsize_txt);
        RecyclerView mFeedListView = view.findViewById(R.id.map_feed_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext.get());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mFeedListView.setLayoutManager(layoutManager);
        mFeedListView.addItemDecoration(new RecycleViewDivider(mContext.get(), DividerItemDecoration.VERTICAL, Color.parseColor("#F5F5F5"), 5));

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext.get());
        MapFeedAdapter feedAdapter = new MapFeedAdapter(bottomSheetDialog);
        mFeedListView.setAdapter(feedAdapter);

        BottomSheetBehavior bottomSheetBehavior = bottomSheetDialog.getBehavior();
        bottomSheetBehavior.setPeekHeight(Device.getScreenHeight() / 2, true);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
        ellipsize.setText("此区域共" + mVisibleList.size() + "条动态");
        view.findViewById(R.id.feed_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        feedAdapter.setData(mVisibleList);
    }
//    private void inflateMarker(){
//        View view = LayoutInflater.from(mContext.get()).inflate(
//                R.layout.map_marker_layout, null);
//        EllipseTextView markerTxt = view.findViewById(R.id.map_marker_feed);
//        ImageView mainIcon = view.findViewById(R.id.maker_avatar_icon);
//        ImageView moodIcon = view.findViewById(R.id.maker_mood_icon);
//        ImageView feedThumb = view.findViewById(R.id.marker_feed_thumb);
//        feedThumb.setScaleType(ImageView.ScaleType.CENTER_CROP);
//
//        LatLng latLng = new LatLng(snsData.getLatitude(), snsData.getLongitude());
//        Cluster cluster = new Cluster(latLng);
//
//        markerTxt.setText(snsData.getFeedContent());
//
//        //动态图标
//        Glide.with(CTBApplication.getContext()).load(snsData.getFeedImage())
//                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
//                .into(new SimpleTarget<Drawable>() {
//                    @Override
//                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                        feedThumb.setImageDrawable(resource);
//                        loadUserIcon(snsData, mainIcon, moodIcon, latLng, view, cluster);
//                    }
//
//                    @Override
//                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
//                        super.onLoadFailed(errorDrawable);
//                        loadUserIcon(snsData, mainIcon, moodIcon, latLng, view, cluster);
//                    }
//                });
//    }


    private void calculateClusters() {
        mIsCanceled = false;
        mClusters.clear();
        mAddMarkers.clear();
        LatLngBounds visibleBounds = mAMap.getProjection().getVisibleRegion().latLngBounds;

        for (ClusterItem clusterItem : mClusterItems) {
            if (mIsCanceled) {
                return;
            }
            LatLng latlng = clusterItem.getPosition();
            //地图可视区域是否包含此坐标
            if (visibleBounds.contains(latlng)) {
                Cluster cluster = getCluster(latlng, mClusters);
                if (cluster != null) {
                    cluster.addClusterItem(clusterItem);
                } else {
                    cluster = new Cluster(latlng);
                    mClusters.add(cluster);
                    cluster.addClusterItem(clusterItem);
                }

            }
        }

        //复制一份数据，规避同步
        List<Cluster> clusters = new ArrayList<Cluster>();
        clusters.addAll(mClusters);
        Message message = Message.obtain();
        message.what = MarkerHandler.ADD_CLUSTER_LIST;
        message.obj = clusters;
        if (mIsCanceled) {
            return;
        }
        mMarkerhandler.sendMessage(message);
    }

    /**
     * 对点进行聚合
     */
    private void assignClusters() {
        mIsCanceled = true;
        mSignClusterHandler.removeMessages(SignClusterHandler.CALCULATE_CLUSTER);
        mSignClusterHandler.sendEmptyMessage(SignClusterHandler.CALCULATE_CLUSTER);
    }

    /**
     * 在已有的聚合基础上，对添加的单个元素进行聚合
     *
     * @param clusterItem
     */
//    private void calculateSingleCluster(ClusterItem clusterItem) {
//        LatLngBounds visibleBounds = mAMap.getProjection().getVisibleRegion().latLngBounds;
//        LatLng latlng = clusterItem.getPosition();
//        if (!visibleBounds.contains(latlng)) {
//            return;
//        }
//        Cluster cluster = getCluster(latlng, mClusters);
//        if (cluster != null) {
//            cluster.addClusterItem(clusterItem);
//            Message message = Message.obtain();
//            message.what = MarkerHandler.UPDATE_SINGLE_CLUSTER;
//
//            message.obj = cluster;
//            mMarkerhandler.removeMessages(MarkerHandler.UPDATE_SINGLE_CLUSTER);
//            mMarkerhandler.sendMessageDelayed(message, 5);
//
//
//        } else {
//
//            cluster = new Cluster(latlng);
//            mClusters.add(cluster);
//            cluster.addClusterItem(clusterItem);
//            Message message = Message.obtain();
//            message.what = MarkerHandler.ADD_SINGLE_CLUSTER;
//            message.obj = cluster;
//            mMarkerhandler.sendMessage(message);
//
//        }
//    }

    /**
     * 根据一个点获取是否可以依附的聚合点，没有则返回null
     *
     * @param latLng
     * @return
     */
    private Cluster getCluster(LatLng latLng, List<Cluster> clusters) {
        for (Cluster cluster : clusters) {
            LatLng clusterCenterPoint = cluster.getCenterLatLng();
            double distance = AMapUtils.calculateLineDistance(latLng, clusterCenterPoint);
            if (distance < mClusterDistance && mAMap.getCameraPosition().zoom < 19) {
                return cluster;
            }
        }

        return null;
    }


    /**
     * 获取每个聚合点的绘制样式
     */
//    private BitmapDescriptor getBitmapDes(int num) {
//        BitmapDescriptor bitmapDescriptor = mLruCache.get(num);
//        if (bitmapDescriptor == null) {
//            TextView textView = new TextView(mContext);
//
//            if (num >= 1 && num <= 5) {
//                String tile = String.valueOf(num);
//                textView.setText(tile);
//            } else {
//                textView.setText("");
//            }
//            textView.setGravity(Gravity.CENTER);
//            textView.setTextColor(Color.WHITE);
//            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
//            if (mClusterRender != null && mClusterRender.getDrawAble(num) != null) {
//                textView.setBackgroundDrawable(mClusterRender.getDrawAble(num));
//            } else {
//                textView.setBackgroundResource(R.mipmap.ic_launcher);
//            }
//            bitmapDescriptor = BitmapDescriptorFactory.fromView(textView);
//            mLruCache.put(num, bitmapDescriptor);
//
//        }
//        return bitmapDescriptor;
//    }

    /**
     * 更新已加入地图聚合点的样式
     */
//    private void updateCluster(Cluster cluster) {
//
//        Marker marker = cluster.getMarker();
//        marker.setIcon(getBitmapDes(cluster.getClusterCount()));
//
//
//    }

    /**
     * by moos on 2018/01/12
     * func:刷新聚合点
     */
    public void updateClusters() {
        mPXInMeters = mAMap.getScalePerPixel();
        mClusterDistance = mPXInMeters * mClusterSize;
        assignClusters();
    }

    /**
     * by moos on 2018/01/12
     * func:响应聚合点的点击事件
     *
     * @param arg0
     */
    public void responseClusterClickEvent(Marker arg0) {
        Cluster cluster = (Cluster) arg0.getObject();
        LatLng latLng = new LatLng(arg0.getPosition().latitude, arg0.getPosition().longitude);
        mAMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        if (cluster != null) {
            mClusterClickListener.onClick(arg0, cluster.getClusterItems());
        }
    }


//-----------------------辅助内部类用---------------------------------------------

    /**
     * marker渐变动画，动画结束后将Marker删除
     */
    class MyAnimationListener implements Animation.AnimationListener {
        private List<Marker> mRemoveMarkers;

        MyAnimationListener(List<Marker> removeMarkers) {
            mRemoveMarkers = removeMarkers;
        }

        @Override
        public void onAnimationStart() {

        }

        @Override
        public void onAnimationEnd() {
            for (Marker marker : mRemoveMarkers) {
                marker.remove();
            }
            mRemoveMarkers.clear();
        }
    }

    /**
     * 处理market添加，更新等操作
     */
    class MarkerHandler extends Handler {

        static final int ADD_CLUSTER_LIST = 0;

//        static final int ADD_SINGLE_CLUSTER = 1;
//
//        static final int UPDATE_SINGLE_CLUSTER = 2;

        MarkerHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {

            switch (message.what) {
                case ADD_CLUSTER_LIST:
                    List<Cluster> clusters = (List<Cluster>) message.obj;
                    addClusterToMap(clusters);
                    break;
//                case ADD_SINGLE_CLUSTER:
//                    Cluster cluster = (Cluster) message.obj;
//                    addSingleClusterToMap(cluster);
//                    break;
//                case UPDATE_SINGLE_CLUSTER:
//                    Cluster updateCluster = (Cluster) message.obj;
//                    updateCluster(updateCluster);
//                    break;
            }
        }
    }


    private void loadUserIcon(SnsMapFeedDto snsData, ImageView mainIcon
            , ImageView moodIcon, LatLng latLng, View view, int clusterSize) {
        //用户头像
        Glide.with(CTBApplication.getInstance()).load(snsData.getFeedUser().getUserIcon().getResourceUrl())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        mainIcon.setImageDrawable(resource);
                        loadMoodIcon(snsData, moodIcon, latLng, view, clusterSize);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        mainIcon.setImageDrawable(mContext.get().getResources().getDrawable(R.drawable.default_avar_icon));
                        loadMoodIcon(snsData, moodIcon, latLng, view, clusterSize);
                    }
                });
    }

    private void loadMoodIcon(SnsMapFeedDto snsData, ImageView mood, LatLng latLng,
                              View view, int clusterSize) {
        //用户心情图标
        Glide.with(CTBApplication.getInstance()).load(snsData.getFeedUser().getUserMoodIcon())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))

                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        mood.setImageDrawable(resource);
                        Marker marker = mAMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .icon(BitmapDescriptorFactory.fromView(view)));
                        marker.setObject(snsData.getFeedId() + "&" + clusterSize);
                        Animation animation = new AlphaAnimation(0, 1);
                        long duration = 200L;
                        animation.setDuration(duration);
                        animation.setInterpolator(new LinearInterpolator());
                        marker.setAnimation(animation);
                        marker.startAnimation();
                        mAddMarkers.add(marker);


                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        mood.setImageDrawable(mContext.get().getResources().getDrawable(R.drawable.user_default_mood));


                        Marker marker = mAMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .icon(BitmapDescriptorFactory.fromView(view)));
                        marker.setObject(snsData.getFeedId() + "&" + clusterSize);

                        Animation animation = new AlphaAnimation(0, 1);
                        long duration = 200L;
                        animation.setDuration(duration);
                        animation.setInterpolator(new LinearInterpolator());
                        marker.setAnimation(animation);
                        marker.startAnimation();
                        mAddMarkers.add(marker);


                    }
                });
    }


    /**
     * 处理聚合点算法线程
     */
    class SignClusterHandler extends Handler {
        static final int CALCULATE_CLUSTER = 0;
        static final int CALCULATE_SINGLE_CLUSTER = 1;

        SignClusterHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case CALCULATE_CLUSTER:
                    calculateClusters();
                    break;
//                case CALCULATE_SINGLE_CLUSTER:
//                    ClusterItem item = (ClusterItem) message.obj;
//                    mClusterItems.add(item);
//                    Log.i("yiyi.qi", "calculate single cluster");
//                    calculateSingleCluster(item);
//                    break;
            }
        }
    }
}