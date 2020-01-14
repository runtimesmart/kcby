package com.ctb_open_car;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.amap.api.maps.model.LatLng;
import com.ctb_open_car.base.BaseActivity;
import com.ctb_open_car.base.CTBBaseApplication;
import com.ctb_open_car.customview.NoScrollViewPager;
import com.ctb_open_car.eventbus.MessageEvent;
import com.ctb_open_car.utils.Device;
import com.ctb_open_car.utils.PermissionUtils;
import com.ctb_open_car.utils.PreferenceUtils;
import com.ctb_open_car.view.activity.dynamic.ReleaseActivity;
import com.ctb_open_car.view.activity.login.LoginActivity;
import com.ctb_open_car.view.adapter.MainFragmentAdapter;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.NetUtils;
import com.rxretrofitlibrary.RxRetrofitApp;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wildma.pictureselector.PictureSelector;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends BaseActivity {

    public MainFragmentAdapter mAdapter;

    @BindView(R.id.view_pager)
    NoScrollViewPager mViewPager;
    @BindView(R.id.tabs_rg)
    RadioGroup mTabRadioGroup;
    @BindView(R.id.home_tab)
    RadioButton mHomeTab;
    @BindView(R.id.news_tab)
    RadioButton mNewTab;
    @BindView(R.id.shop_tab)
    RadioButton mStopTab;
    @BindView(R.id.my_tab)
    RadioButton mMySelfTab;
    @BindView(R.id.release_tab)
    ImageView mReleaseTab;


    public int mRadioGroupIndex;
    public static final int REQUEST_CODE_CHOOSE = 1001;
    public static final int REQUEST_CODE_PUSHDYNMIC= 0x1002;
    private LatLng mCurrentLoc;
    private RxRetrofitApp mRxInstance;
    private String mCurrentPosition;
    private boolean isMeFragment = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        deviceInfo();
        // 初始化页卡
        initPager();
        PermissionUtils.requestPermissionList(this);
        CTBBaseApplication.getInstance().initLogin();
        EMClient.getInstance().addConnectionListener(new ChatConnectionListener());
    }

    @Override
    public Object getTag() {
        return null;
    }

    //实现ConnectionListener接口
    private class ChatConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
            Timber.d("环信登录成功");
        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                    } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录
                    } else {
                        if (NetUtils.hasNetwork(MainActivity.this)) {

                        } else {
                            //当前网络不可用，请检查网络设置
                        }
                    }
                }
                //连接不到聊天服务器

            });
        }
    }

    private boolean mFlagCommunity;

    public void switchCommunity(boolean toCommunity) {
//        MessageEvent messageEvent = new MessageEvent();
//        messageEvent.setType("RequestFeedList");
//        EventBus.getDefault().post(messageEvent);
        mViewPager.setCurrentItem(1);
    }

    private void deviceInfo() {
        mRxInstance = RxRetrofitApp.Singleton.INSTANCE.get();
        mRxInstance.mHeadBean.setAppType(2);
        mRxInstance.mHeadBean.setChannel(Device.getChannel());
        mRxInstance.mHeadBean.setImei(Device.getIMEI());
        mRxInstance.mHeadBean.setVersionCode(Device.getAppVersionCode());
        String versionName = Device.getAppVersionName();
        if (TextUtils.isEmpty(versionName) || "null".equals(versionName)) {
            versionName = "";
        }
        mRxInstance.mHeadBean.setVersionName(versionName);
        mRxInstance.mHeadBean.setUserToken(PreferenceUtils.getString(CTBApplication.getInstance(), "user_token"));
        mRxInstance.mHeadBean.setUserId(PreferenceUtils.getLong(CTBApplication.getInstance(), "userId"));

    }

    private void initPager() {
        int px = sp2px(this, 22);
        Drawable drawable_news = ContextCompat.getDrawable(this, R.drawable.tab_main_selector);
        drawable_news.setBounds(0, 0, px, px);
        mHomeTab.setCompoundDrawables(null, drawable_news, null, null);

        Drawable drawableTimingTab = ContextCompat.getDrawable(this, R.drawable.tab_main_news_selector);
        drawableTimingTab.setBounds(0, 0, px, px);
        mNewTab.setCompoundDrawables(null, drawableTimingTab, null, null);

        Drawable drawableStopWatchTab = ContextCompat.getDrawable(this, R.drawable.tab_main_shop_selector);
        drawableStopWatchTab.setBounds(0, 0, px, px);
        mStopTab.setCompoundDrawables(null, drawableStopWatchTab, null, null);

        Drawable drawableCountTab = ContextCompat.getDrawable(this, R.drawable.tab_main_me_selector);
        drawableCountTab.setBounds(0, 0, px, px);
        mMySelfTab.setCompoundDrawables(null, drawableCountTab, null, null);

        mAdapter = new MainFragmentAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setScanScroll(false);
        mViewPager.setAdapter(mAdapter);
        // register listener
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        mTabRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);

        mHomeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0);
            }
        });
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0 || position == 1) {
                RadioButton radioButton = (RadioButton) mTabRadioGroup.getChildAt(0);
                radioButton.setChecked(true);
            } else if (position == 2) {
                RadioButton radioButton = (RadioButton) mTabRadioGroup.getChildAt(position - 1);
                radioButton.setChecked(true);
            } else if (position == 3 || position == 4) {
                RadioButton radioButton = (RadioButton) mTabRadioGroup.getChildAt(position);
                radioButton.setChecked(true);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.home_tab:
                    isMeFragment = false;
                    mRadioGroupIndex = 0;
                    mViewPager.setCurrentItem(0, false);
                    break;
                case R.id.news_tab:
                    isMeFragment = false;
                    mRadioGroupIndex = 1;
                    mViewPager.setCurrentItem(2, false);
                    break;
                case R.id.shop_tab:
                    isMeFragment = false;
                    mRadioGroupIndex = 2;
                    mViewPager.setCurrentItem(3, false);
                    break;
                case R.id.my_tab:
                    isMeFragment = true;
                    if (mRxInstance.mHeadBean.getUserId() <= 0) {
                        MessageEvent messageEvent = new MessageEvent();
                        messageEvent.setType("login");
                        messageEvent.setObject(mCurrentLoc);
                        EventBus.getDefault().post(messageEvent);
                    } else {
                        MessageEvent messageEvent = new MessageEvent();
                        messageEvent.setType("update_data_me");
                        messageEvent.setObject(mCurrentLoc);
                        EventBus.getDefault().post(messageEvent);
                    }
                    mViewPager.setCurrentItem(4, false);
                    break;

            }
        }
    };

    public int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    @OnClick(R.id.release_tab)
    public void onViewClicked() {
        if (mRxInstance.mHeadBean.getUserId() <= 0) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("LatLng", mCurrentLoc);
            startActivityForResult(intent, REQUEST_CODE_CHOOSE);
        } else {
            Intent intent = new Intent(MainActivity.this, ReleaseActivity.class);
            intent.putExtra("CurrentPosition", mCurrentPosition);
            startActivityForResult(intent, REQUEST_CODE_PUSHDYNMIC);
        }
    }

    public void setmCurrentLoc(LatLng currentLoc, String currentPosition) {
        mCurrentPosition = currentPosition;
        mCurrentLoc = currentLoc;
        mRxInstance.mHeadBean.setLatitude(mCurrentLoc.latitude);
        mRxInstance.mHeadBean.setLongitude(mCurrentLoc.longitude);
    }

    public void setViewPagerCurrentItem(int item) {
        mViewPager.setCurrentItem(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getEventBusMsg(MessageEvent event) {
        if (null == event.getType()) {
            return;
        }
        switch (event.getType()) {
            case "default_radioGroup_index":
                mViewPager.setCurrentItem(mRadioGroupIndex);
                int index = mRadioGroupIndex;
                if (mRadioGroupIndex > 1) {
                    index = mRadioGroupIndex + 1;
                }
                RadioButton radioButton = (RadioButton) mTabRadioGroup.getChildAt(index);
                radioButton.setChecked(true);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*结果回调*/
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                String picturePath = data.getStringExtra(PictureSelector.PICTURE_PATH);
                //   mAvarIcon.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                MessageEvent messageEvent = new MessageEvent();
                messageEvent.setType("PictureSelector");
                messageEvent.setObject(picturePath);
                EventBus.getDefault().post(messageEvent);
            }
        } else if (requestCode == REQUEST_CODE_CHOOSE) {
            if (data != null && data.getBooleanExtra("loginSuccess", false)) {
                MessageEvent messageEvent = new MessageEvent();
                messageEvent.setType("update_data_me");
                EventBus.getDefault().post(messageEvent);

                // mViewPager.setCurrentItem(3);
            } else if (isMeFragment) {
                mViewPager.setCurrentItem(mRadioGroupIndex);
            }
        } else if (requestCode == REQUEST_CODE_PUSHDYNMIC) {
            mViewPager.setCurrentItem(1);
        }
    }
}
