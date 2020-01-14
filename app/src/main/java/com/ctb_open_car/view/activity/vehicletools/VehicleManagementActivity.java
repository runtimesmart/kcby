package com.ctb_open_car.view.activity.vehicletools;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.model.LatLng;
import com.bumptech.glide.Glide;
import com.ctb_open_car.MainActivity;
import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseActivity;
import com.ctb_open_car.bean.userInfo.CarListBean;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.MyInfoApi;
import com.ctb_open_car.eventbus.MessageEvent;
import com.ctb_open_car.view.activity.cartool.AddCarInfoActivity;
import com.ctb_open_car.view.activity.login.LoginActivity;
import com.ctb_open_car.view.adapter.vehicletoolsadpter.CarListAdapter;
import com.ctb_open_car.view.dialog.RoundProgressDialog;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import timber.log.Timber;

/*****  车辆管理界面，车辆列表  *****/
public class VehicleManagementActivity extends BaseActivity {

    @BindView(R.id.ic_back)
    ImageView mBack;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.recyclerview)
    SwipeRecyclerView mRecyclerview;
    @BindView(R.id.add_info_car)
    LinearLayout mAddInfoCar;

    private CarListBean mCarListBean;
    private CarListAdapter mAdapter;
    private List<CarListBean.PlateDto> mCarList = new ArrayList<>(); //车辆列表

    private RoundProgressDialog mRoundProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_management);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mTitleTv.setVisibility(View.VISIBLE);
        mTitleTv.setText("车辆管理");
        initData();
        getCarData();
    }

    @Override
    public Object getTag() {
        return null;
    }

    public void initData() {
        mAdapter = new CarListAdapter(this, mCarList);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerview.setLayoutManager(linearLayoutManager1);
        // 创建菜单：
        SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(VehicleManagementActivity.this);
                deleteItem.setText("删除")
                        .setBackgroundColor(getResources().getColor(R.color.qmui_config_color_red))
                        .setTextColor(Color.WHITE) // 文字颜色。
                        .setTextSize(25) // 文字大小。
                        .setWidth(180)
                        .setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                rightMenu.addMenuItem(deleteItem);
            }
        };

        // 设置监听器。
        mRecyclerview.setSwipeMenuCreator(mSwipeMenuCreator);

        OnItemMenuClickListener mItemMenuClickListener = new OnItemMenuClickListener() {
            @Override
            public void onItemClick(com.yanzhenjie.recyclerview.SwipeMenuBridge menuBridge, int adapterPosition) {
                // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
                menuBridge.closeMenu();

                // 左侧还是右侧菜单：
                int direction = menuBridge.getDirection();
                // 菜单在Item中的Position：
                int menuPosition = menuBridge.getPosition();
                deleteCarData(mCarListBean.getCarList().get(adapterPosition).getPlate());
            }

        };

        // 菜单点击监听。
        mRecyclerview.setItemViewSwipeEnabled(false);
        mRecyclerview.setOnItemMenuClickListener(mItemMenuClickListener);
        mRecyclerview.setAdapter(mAdapter);
    }

    public void getCarData() {
        MyInfoApi carListApi = new MyInfoApi(new HttpListener() {
            @Override
            public void onNext(Object object) {
                BaseResultEntity<CarListBean> entity = (BaseResultEntity<CarListBean>)object;
                if (entity.getRet().equals("0")) {
                    mCarListBean = entity.getData();
                    mCarList = mCarListBean.getCarList();
                    mAdapter.setList(mCarList);

                    if (mCarList.size() >= 3) {
                        mAddInfoCar.setVisibility(View.GONE);
                    } else {
                        mAddInfoCar.setVisibility(View.VISIBLE);
                    }

                    dismissDiaLog();
                } else {
                    dismissDiaLog();
                    Toasty.info(VehicleManagementActivity.this, entity.getMsg()).show();
                }
            }

            @Override
            public void onError(Throwable e) {
                dismissDiaLog();
                Timber.e(" getCarData e = %s", e.getMessage());
            }
        }, this, 3);
        HttpManager.getInstance().doHttpDeal(carListApi);
    }

    public void deleteCarData(String plate) {
        showDialog("正在删除...");
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("plate",plate);
        MyInfoApi carListApi = new MyInfoApi(new HttpListener() {
            @Override
            public void onNext(Object object) {
                BaseResultEntity entity = (BaseResultEntity)object;
                if (entity.getRet().equals("0")) {
                    getCarData();
                    MessageEvent messageEvent = new MessageEvent();
                    messageEvent.setType("update_data_me");
                    EventBus.getDefault().post(messageEvent);
                } else {
                    dismissDiaLog();
                    Toasty.info(VehicleManagementActivity.this, entity.getMsg()).show();
                }
            }

            @Override
            public void onError(Throwable e) {
                dismissDiaLog();
                Timber.e(" signOut e = %s", e.getMessage());
            }
        }, this, 4);
        carListApi.setRequestBody(queryMap);
        HttpManager.getInstance().doHttpDeal(carListApi);
    }

    @OnClick({R.id.ic_back, R.id.add_info_car})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ic_back:
                onBackPressed();
                break;
            case R.id.add_info_car:
                Intent intent = new Intent(this, AddCarInfoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getEventBusMsg(MessageEvent event) {
        switch (event.getType()) {
            case "update_data_me":
                getCarData();
                break;
        }
    }

    public void showDialog(String tips) {
        mRoundProgressDialog = RoundProgressDialog.newInstance(tips);
        mRoundProgressDialog.show(getSupportFragmentManager(), "dialog");
    }

    public void dismissDiaLog() {
        if (mRoundProgressDialog != null) {
            mRoundProgressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
