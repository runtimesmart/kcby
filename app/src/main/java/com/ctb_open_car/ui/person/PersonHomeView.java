package com.ctb_open_car.ui.person;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ctb_open_car.CTBApplication;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.community.response.UserData;
import com.ctb_open_car.constraints.AppContraint;
import com.ctb_open_car.presenter.UserFollowPresenter;
import com.ctb_open_car.presenter.UserInfoPresenter;
import com.ctb_open_car.ui.BaseView;
import com.ctb_open_car.utils.Device;
import com.ctb_open_car.utils.PreferenceUtils;
import com.ctb_open_car.view.BlurTransformation;
import com.ctb_open_car.view.activity.person.PersonHomeActivity;
import com.ctb_open_car.view.adapter.person.PersonFragmentAdapter;
import com.ctb_open_car.wxapi.WeiXinShareManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.library.BottomDialog;
import com.library.Item;
import com.library.OnItemClickListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class PersonHomeView implements BaseView, UserFollowPresenter.FollowCallback {


    private static final float TAB_TEXT_SIZE = 14;
    @BindView(R.id.person_tab_summary)
    TabLayout mSummaryTab;
    @BindView(R.id.person_sign)
    TextView mPersonSign;

    @BindView(R.id.person_name)
    TextView mPersonName;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.person_avatar)
    ImageView mPersonAvatar;

    @BindView(R.id.focus_btn)
    TextView mBtnFocus;

    @BindView(R.id.person_verfy)
    LinearLayout mVipLayout;

    @BindView(R.id.person_appbar)
    AppBarLayout mAppBar;

    @BindView(R.id.person_header)
    LinearLayout mPersonHeader;


    private PersonFragmentAdapter mAdapter;
    private UserInfoPresenter mUserPresenter;
    private final SoftReference<PersonHomeActivity> mActivity;

    private TextView mTxtCount;
    private TextView mTxtName;
    private View mIndicator;
    private HashMap mTabTitleCache = new HashMap();
    private HashMap mTabIndicatorCache = new HashMap();
    private HashMap mTabCountCache = new HashMap();

    private UserFollowPresenter mUserFollowPresenter;
    private String mUserId;
    private UserData mUserData;
    private List<String> mCounts;
    private List<String> mFilterNames = new ArrayList() {{
        add("动态");
        add("活动");
        add("关注");
        add("粉丝");
    }};

    public PersonHomeView(PersonHomeActivity activity, String userId) {
        this.mUserId = userId;
        mCounts = new ArrayList<>();
        mActivity = new SoftReference(activity);
        mUserFollowPresenter = new UserFollowPresenter(mActivity.get(), this);

        ButterKnife.bind(this, activity);
        setupTabs();
        initView();
        initViewPager();
        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                AppBarLayout.LayoutParams tabLayoutParams = new AppBarLayout.LayoutParams(mSummaryTab.getLayoutParams());
                if (Math.abs(verticalOffset) >= mAppBar.getTotalScrollRange()) { //折叠状态
                    tabLayoutParams.setMargins(0, 0, 0, 0);
                    mSummaryTab.setBackgroundColor(mActivity.get().getResources().getColor(android.R.color.white));
                } else {
                    //展开状态
                    tabLayoutParams.setMargins(Device.dip2px(20), Device.dip2px(-20), Device.dip2px(20), 0);

                    mSummaryTab.setBackground(mActivity.get().getResources().getDrawable(R.drawable.tab_round_shadow));
                }
                mSummaryTab.setLayoutParams(tabLayoutParams);
            }
        });
    }

    private void initView() {
        if (mUserId.equals(CTBApplication.getInstance().getRxApp().mHeadBean.getUserId().toString())) {
            mBtnFocus.setVisibility(View.GONE);
        }
    }

    private void setFocusStatus(int type) {
        if (mUserId.equals(PreferenceUtils.getLong(CTBApplication.getInstance(), "userId"))) {
            mBtnFocus.setVisibility(View.INVISIBLE);
            return;
        }
        if (0 == type) {
            mBtnFocus.setText("关注");
            mBtnFocus.setBackground(mActivity.get().getResources().getDrawable(R.drawable.person_unfocus_bg));
        } else {
            mBtnFocus.setText("已关注");
            mBtnFocus.setBackground(mActivity.get().getResources().getDrawable(R.drawable.person_focus_bg));
        }
    }


    private void updateMyInfo(UserData userData) {
        this.mUserData = userData;
        Glide.with(mActivity.get())
                .load(mUserData.getUserHome().getUserIcon().getResourceUrl())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(mPersonAvatar);

        Glide.with(mActivity.get())
                .load(mUserData.getUserHome().getUserIcon().getResourceUrl())
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(mActivity.get(), 25, 8)))
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        mPersonHeader.setBackground(resource);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                    }
                });

        mPersonName.setText(mUserData.getUserHome().getNickName());
        //设置关注状态
        setFocusStatus(mUserData.getUserHome().getRelationStatus());
        if (TextUtils.isEmpty(mUserData.getUserHome().getUserSign())) {
            mPersonSign.setVisibility(View.INVISIBLE);
        } else {
            mPersonSign.setText(mUserData.getUserHome().getUserSign());
        }
        if (0 == mUserData.getUserHome().getUserAuthStatus()) {
            mVipLayout.setVisibility(View.INVISIBLE);
        } else {
            mVipLayout.setVisibility(View.VISIBLE);
        }

        mCounts.add(mUserData.getUserHome().getUserStat().getFeedCnt() + "");
        mCounts.add(mUserData.getUserHome().getUserStat().getActivityCnt() + "");
        mCounts.add(mUserData.getUserHome().getUserStat().getAttentionCnt() + "");
        mCounts.add(mUserData.getUserHome().getUserStat().getFansCnt() + "");
        for (int i = 0; i < mCounts.size(); i++) {
            TabLayout.Tab tab = mSummaryTab.getTabAt(i);
            TextView txtCount = (TextView) mTabCountCache.get(tab);
            txtCount.setText(mCounts.get(i));
        }
    }


    private void initViewPager() {
        mAdapter = new PersonFragmentAdapter(mActivity.get().getSupportFragmentManager(), mUserId);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mSummaryTab));
        mSummaryTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition(), true);
                setTabStytle(tab, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setTabStytle(tab, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    /***
     * 设置TabLayout 子tab的文字
     * */
    private void setupTabs() {
        int i = -1;
        while (i++ < mFilterNames.size() - 1) {
            TabLayout.Tab tab = mSummaryTab.newTab();
            View v = LayoutInflater.from(mActivity.get()).inflate(R.layout.activity_person_tab_layout, null);
            tab.setCustomView(v);
            mTxtCount = v.findViewById(R.id.feed_count);
            mTxtName = v.findViewById(R.id.feed_name);
            mIndicator = v.findViewById(R.id.tab_item_indicator);

            mTxtName.setText(mFilterNames.get(i));
            mTxtCount.setText("0");

            ViewGroup.LayoutParams layoutParams = mIndicator.getLayoutParams();
            layoutParams.width = Device.dip2px(20);
            layoutParams.height = Device.dip2px(3);
            mIndicator.setLayoutParams(layoutParams);
            mSummaryTab.addTab(tab);
            //缓存view
            mTabTitleCache.put(tab, mTxtName);
            mTabIndicatorCache.put(tab, mIndicator);
            mTabCountCache.put(tab, mTxtCount);

            //初始化第一个tab选中样式
            if (0 == i) {
                setTabStytle(tab, true);
            }
        }
    }

    private void setTabStytle(TabLayout.Tab tab, boolean selected) {
        mTxtName = (TextView) mTabTitleCache.get(tab);
        mIndicator = (View) mTabIndicatorCache.get(tab);
        mTxtCount = (TextView) mTabCountCache.get(tab);

        TextPaint textPaint = mTxtName.getPaint();
        mTxtName.setTextSize(TAB_TEXT_SIZE);
        if (selected) {
            mTxtName.setTextColor(mActivity.get().getResources().getColor(R.color.color_F0BB5D));
            mTxtCount.setTextColor(mActivity.get().getResources().getColor(R.color.color_F0BB5D));
            mIndicator.setVisibility(View.VISIBLE);
            textPaint.setFakeBoldText(true);
        } else {
            mTxtName.setTextColor(mActivity.get().getResources().getColor(R.color.color_cccccc));

            mTxtCount.setTextColor(mActivity.get().getResources().getColor(R.color.color_333333));
            mIndicator.setVisibility(View.INVISIBLE);
            textPaint.setFakeBoldText(false);
        }
    }

    @OnClick(R.id.focus_btn)
    public void onFocusClick(View v) {
        if (mBtnFocus.getVisibility() == View.GONE) {
            return;
        }
        //未关注
        if (0 == mUserData.getUserHome().getRelationStatus()) {
            mUserFollowPresenter.userFollow(mUserId);
        } else {
            mUserFollowPresenter.userCancelFollow(mUserId);
        }
    }


    @OnClick(R.id.show_more)
    void showMore(View v) {
        BottomDialog bottomDialog = new BottomDialog(mActivity.get());
        bottomDialog.orientation(BottomDialog.HORIZONTAL);
        bottomDialog.inflateMenu(R.menu.menu_share, new OnItemClickListener() {
            @Override
            public void click(Item item) {
                int id = item.getId();
                switch (id) {
                    case R.id.wechat:
                        shareToWx(WeiXinShareManager.WECHAT_SHARE_TYPE_TALK);
                        break;
                    case R.id.pengyouquan:
                        shareToWx(WeiXinShareManager.WECHAT_SHARE_TYPE_FRENDS);
                        break;
                }
            }
        });
        bottomDialog.inflateMenu(R.menu.menu_other, new OnItemClickListener() {
            @Override
            public void click(Item item) {
                int id = item.getId();
                switch (id) {
                    case R.id.moments:
                        /**保存图片*/
                        bottomDialog.dismiss();
                        Bitmap bitmap = getScreenBitmap();
                        saveToPath(bitmap);
                        Ringtone ringtone = RingtoneManager.getRingtone(mActivity.get(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                        if (!ringtone.isPlaying()) {
                            ringtone.play();
                        }
                        Toasty.info(mActivity.get(), "已截图保存").show();
                        break;
                    case R.id.complaint:
                        /**投诉*/
                        Toasty.info(mActivity.get(), "已投诉").show();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + id);
                }
            }
        });
        bottomDialog.show();
    }

    public Bitmap getScreenBitmap() {
        View decorView = mActivity.get().getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        Bitmap bitmap = decorView.getDrawingCache();
        int decorViewTop = getDecorViewTop();
        Point screenPoint = new Point();
        mActivity.get().getWindowManager().getDefaultDisplay().getSize(screenPoint);
        int width = screenPoint.x;
        int height = screenPoint.y;
        Bitmap activityBitmap = Bitmap.createBitmap(bitmap, 0, decorViewTop, width, height - decorViewTop);
        return activityBitmap;
    }

    public void saveToPath(Bitmap bitmap) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "ctb");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".png";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            MediaStore.Images.Media.insertImage(mActivity.get().getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        mActivity.get().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.getAbsolutePath())));
    }

    /***/
    private int getDecorViewTop() {
        Rect frame = new Rect();
        mActivity.get().getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    @SuppressLint("CheckResult")
    private void shareToWx(int type) {

        WeiXinShareManager manager = WeiXinShareManager.getInstance(mActivity.get());
        if (manager.isWeixinAvilible(mActivity.get())) {
            Glide.with(mActivity.get()).load(mUserData.getUserHome().getUserIcon().getResourceUrl())
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            manager.shareByWebchat(manager.getShareContentWebpag(mUserData.getUserHome().getNickName() + "的主页"
                                    , mUserData.getUserHome().getUserSign(),
                                    AppContraint.WeiXin.sPersonUrl + mUserData.getUserHome().getUserId(), resource)
                                    , type);
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            manager.shareByWebchat(manager.getShareContentWebpag(mUserData.getUserHome().getNickName() + "的主页"
                                    , mUserData.getUserHome().getUserSign(),
                                    AppContraint.WeiXin.sPersonUrl + mUserData.getUserHome().getUserId(), R.mipmap.app_launcher)
                                    , type);
                        }
                    });
//                    .listener(new RequestListener<Drawable>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            manager.shareByWebchat(manager.getShareContentWebpag(mUserData.getUserHome().getNickName() + "的主页"
//                                    , mUserData.getUserHome().getUserSign(),
//                                    AppContraint.WeiXin.sPersonUrl + mUserData.getUserHome().getUserId(), R.mipmap.app_launcher)
//                                    , type);
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                            manager.shareByWebchat(manager.getShareContentWebpag(mUserData.getUserHome().getNickName() + "的主页"
//                                    , mUserData.getUserHome().getUserSign(),
//                                    AppContraint.WeiXin.sPersonUrl + mUserData.getUserHome().getUserId(), resource)
//                                    , type);
//                            return false;
//                        }
//                    }).into(new ImageView(mActivity.get()));

        } else {
            Toasty.warning(mActivity.get(), "检测到您未安装微信，请到应用市场下载安装", Toasty.LENGTH_LONG).show();
        }
    }

    @Override
    public void actionCancelSuccess() {
        setFocusStatus(0);
        mUserData.getUserHome().setRelationStatus(0);
    }

    @Override
    public void actionFollowSuccess() {
        setFocusStatus(1);
        mUserData.getUserHome().setRelationStatus(1);

    }

    @Override
    public void setPresenter(Object presenter) {
        this.mUserPresenter = (UserInfoPresenter) presenter;
        mUserPresenter.requestUserInfo(mUserId, new UserInfoPresenter.UpdateListener() {
            @Override
            public void updateUserInfo(UserData userData) {
                updateMyInfo(userData);
            }
        });
    }

    @Override
    public void drawTitleBar() {

    }


    @Override
    public void unbind() {

    }


}
