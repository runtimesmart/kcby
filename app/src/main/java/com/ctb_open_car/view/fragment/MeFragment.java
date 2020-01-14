package com.ctb_open_car.view.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.amap.api.maps.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ctb_open_car.CTBApplication;
import com.ctb_open_car.MainActivity;
import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseFragment;
import com.ctb_open_car.bean.newsbean.NewsInfoBean;
import com.ctb_open_car.bean.userInfo.UserInfoBean;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.MyInfoApi;
import com.ctb_open_car.engine.net.api.NewshomeApi;
import com.ctb_open_car.eventbus.MessageEvent;
import com.ctb_open_car.utils.PreferenceUtils;
import com.ctb_open_car.view.activity.cartool.AddCarInfoActivity;
import com.ctb_open_car.view.activity.login.LoginActivity;
import com.ctb_open_car.view.activity.news.BloggerInfoActivity;
import com.ctb_open_car.view.activity.person.ModifyPersonalInfoActivity;
import com.ctb_open_car.view.activity.shopMall.ShopManagementActivity;
import com.ctb_open_car.view.activity.vehicletools.VehicleManagementActivity;
import com.ctb_open_car.view.dialog.RoundProgressDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wildma.pictureselector.PictureSelector;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import timber.log.Timber;


/**
 * 我
 */
public class MeFragment extends BaseFragment {

    @BindView(R.id.avar_icon)
    ImageView mAvarIcon;
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.user_id)
    TextView mUserId;
    @BindView(R.id.user_info)
    RelativeLayout mUserInfo;
    @BindView(R.id.car_coin_num)
    TextView mCarCoinNum;
    @BindView(R.id.rmb_num)
    TextView mRMBNum;
    @BindView(R.id.rmb_tab)
    TextView mRmbTab;
    @BindView(R.id.limit_row_logo)
    ImageView mLimitRowLogo;
    @BindView(R.id.limit_row_lay)
    RelativeLayout mLimitRowLay;
    @BindView(R.id.vehicle_rules_logo)
    ImageView mVehicleRulesLogo;
    @BindView(R.id.vehicle_rules_lay)
    RelativeLayout mVehicleRulesLay;
    @BindView(R.id.my_invitation_code)
    LinearLayout mMyInvitationCode;
    @BindView(R.id.my_order_lay)
    LinearLayout mMyOrderLay;
    @BindView(R.id.address_management_lay)
    LinearLayout mAddressManagementLay;
    @BindView(R.id.telephone_logo)
    ImageView mTelephoneLogo;
    @BindView(R.id.number_plate)
    TextView mNumberPlate;
    @BindView(R.id.car_type)
    TextView mCarType;
    @BindView(R.id.car_icon)
    ImageView mCarIcon;
    @BindView(R.id.car_info_lay)
    RelativeLayout mCarInfoLay;
    @BindView(R.id.add_info_car)
    LinearLayout mAddInfoCar;

    @BindView(R.id.customerService)
    TextView mCustomerService;

    private boolean isLogin = true;
    private String[] perms = {Manifest.permission.CALL_PHONE};
    private final int PERMS_REQUEST_CODE = 200;

    private RoundProgressDialog mRoundProgressDialog;
    UserInfoBean mUserInfoBean;

    public MeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        // getMyInfoData();
        return view;
    }

    @Override
    protected String getTAG() {
        return null;
    }

    @OnClick({R.id.user_info, R.id.limit_row_lay, R.id.vehicle_rules_lay, R.id.my_invitation_code, R.id.my_order_lay, R.id.address_management_lay
            , R.id.car_info_lay, R.id.add_info_car, R.id.telephone_lay, R.id.sign_out})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.user_info:
                intent = new Intent(getActivity(), ModifyPersonalInfoActivity.class);
                intent.putExtra("UserInfoDto", mUserInfoBean.getUserInfo());
                startActivity(intent);
                break;
            case R.id.limit_row_lay:
                break;
            case R.id.vehicle_rules_lay:
                break;
            case R.id.my_invitation_code:
                break;
            case R.id.my_order_lay:
                intent = new Intent(getActivity(), ShopManagementActivity.class);
                intent.putExtra("shop_manger_title", getString(R.string.my_order));
                intent.putExtra("shop_manger_url", "http://tocadmin.chetuobang.com/opencar_view/orderlist");
                startActivity(intent);
                break;
            case R.id.address_management_lay:
                intent = new Intent(getActivity(), ShopManagementActivity.class);
                intent.putExtra("shop_manger_title", getString(R.string.address_management));
                intent.putExtra("shop_manger_url", "http://tocadmin.chetuobang.com/opencar_view/addressList");
                startActivity(intent);
                break;
            case R.id.car_info_lay:
                intent = new Intent(getActivity(), VehicleManagementActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.add_info_car:
                intent = new Intent(getActivity(), AddCarInfoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.telephone_lay:
                requestPermissions(perms, PERMS_REQUEST_CODE);//请求权限
                break;
            case R.id.sign_out:
                signOut();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getEventBusMsg(MessageEvent event) {
        if (null == event.getType()) {
            return;
        }
        switch (event.getType()) {
            case "PictureSelector":
                Glide.with(getContext()).asBitmap().circleCrop().load((String) event.getObject()).into(mAvarIcon);
                break;
            case "login":
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("LatLng", (LatLng) event.getObject());
                ((MainActivity) getActivity()).startActivityForResult(intent, ((MainActivity) getActivity()).REQUEST_CODE_CHOOSE);
                break;
            case "update_data_shop":
            case "update_data":
            case "update_data_me":
                getMyInfoData();
                break;
        }
    }

    public void showDialog(String tips) {
        mRoundProgressDialog = RoundProgressDialog.newInstance(tips);
        mRoundProgressDialog.show(getChildFragmentManager(), "dialog");
    }

    public void dismissDiaLog() {
        if (mRoundProgressDialog != null) {
            mRoundProgressDialog.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void getMyInfoData() {
        // showDialog("正在获取数据");
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("unionid", "my");
        MyInfoApi myInfoApi = new MyInfoApi(new HttpListener() {
            @Override
            public void onNext(Object object) {
                super.onNext(object);
                BaseResultEntity<UserInfoBean> baseResultEntity = (BaseResultEntity<UserInfoBean>) object;
                if (baseResultEntity.getRet().equals("0")) {
                    mUserInfoBean = baseResultEntity.getData();
                    setData();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (e.getMessage().contains("-4")) {
                    mRxInstance.mHeadBean.setUserId(-1L);
                    mRxInstance.mHeadBean.setUserToken("");
                    PreferenceUtils.putLong(CTBApplication.getInstance(), "userId", -1L);
                    PreferenceUtils.putString(CTBApplication.getInstance(), "user_token", "");
                    isLogin = false;
                }
            }
        }, (MainActivity) getActivity(), 0);
        myInfoApi.setRequestBody(queryMap);
        HttpManager.getInstance().doHttpDeal(myInfoApi);
    }

    private void setData() {
        if (mUserInfoBean == null) {
            return;
        }
        mUserName.setText(mUserInfoBean.getUserInfo().getNickname());
        mCarCoinNum.setText(String.valueOf(mUserInfoBean.getIntegralCount()));
        mRMBNum.setText(String.valueOf(mUserInfoBean.getCashCount()));
        mUserId.setText(mUserInfoBean.getUserInfo().getUserId());
        mCustomerService.setText(mUserInfoBean.getCustomerService());
        Glide.with(getActivity()).asBitmap().circleCrop().load(mUserInfoBean.getUserInfo().getUserIcon()).placeholder(R.drawable.default_avar_icon).error(R.drawable.default_avar_icon).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                mAvarIcon.setImageBitmap(resource);
            }
        });

        if (mUserInfoBean.getCarList() != null && mUserInfoBean.getCarList().size() > 0) {
            mCarInfoLay.setVisibility(View.VISIBLE);
            mAddInfoCar.setVisibility(View.GONE);
            UserInfoBean.PlateDto plateDto = mUserInfoBean.getCarList().get(0);
            mNumberPlate.setText(plateDto.getPlate());
            if (TextUtils.isEmpty(plateDto.getBrand())) {
                mCarType.setVisibility(View.GONE);
            } else {
                mCarType.setVisibility(View.VISIBLE);
                mCarType.setText(plateDto.getBrand());
            }
        } else {
            mCarInfoLay.setVisibility(View.GONE);
            mAddInfoCar.setVisibility(View.VISIBLE);
        }
    }

    public void signOut() {
        MyInfoApi myInfoApi = new MyInfoApi(new HttpListener() {
            @Override
            public void onNext(Object object) {
                JsonObject jsonObject = (JsonObject) object;
                String code = jsonObject.get("code").toString();
                int codeIndex = Integer.parseInt(code = code.replace("\"", ""));
                Timber.e(" signOut codeIndex = %d", codeIndex);

                if (codeIndex == 0) {
                    mRxInstance.mHeadBean.setUserId(-1L);
                    mRxInstance.mHeadBean.setUserToken("");
                    PreferenceUtils.putLong(CTBApplication.getInstance(), "userId", -1L);
                    PreferenceUtils.putString(CTBApplication.getInstance(), "user_token", "");

                    PreferenceUtils.putString(CTBApplication.getInstance(), "em_id", "");
                    PreferenceUtils.putString(CTBApplication.getInstance(), "em_pass", "");
                    Toasty.info(getContext(), getString(R.string.sign_out_success)).show();
                    ((MainActivity) getActivity()).setViewPagerCurrentItem(0);
                } else if (codeIndex == -4) {
                    mRxInstance.mHeadBean.setUserId(-1L);
                    mRxInstance.mHeadBean.setUserToken("");
                    PreferenceUtils.putLong(CTBApplication.getInstance(), "userId", -1L);
                    PreferenceUtils.putString(CTBApplication.getInstance(), "user_token", "");
                    PreferenceUtils.putString(CTBApplication.getInstance(), "em_id", "");
                    PreferenceUtils.putString(CTBApplication.getInstance(), "em_pass", "");

                    Intent i = new Intent(CTBApplication.getContext(), LoginActivity.class);
                    CTBApplication.getContext().startActivity(i);
                } else {
                    Toasty.info(getContext(), getString(R.string.sign_out_failded)).show();
                }
                EMClient.getInstance().logout(true, new EMCallBack() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(int code, String error) {

                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(" signOut e = %s", e.getMessage());
            }
        }, (MainActivity) getActivity(), 1);
        HttpManager.getInstance().doHttpDeal(myInfoApi);
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
        switch (permsRequestCode) {
            case PERMS_REQUEST_CODE:
                boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (storageAccepted) {
                    callPhone();
                } else {
                    Log.i("MainActivity", "没有权限操作这个请求");
                }
                break;

        }
    }

    //拨打电话
    private void callPhone() {
        //检查拨打电话权限
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            String phone = mCustomerService.getText().toString();
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phone));
            startActivity(intent);
        }
    }
}
