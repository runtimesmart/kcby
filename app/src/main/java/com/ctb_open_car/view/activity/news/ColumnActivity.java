package com.ctb_open_car.view.activity.news;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.amap.api.maps.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.newsbean.ColumnDataBean;
import com.ctb_open_car.bean.newsbean.ColumnFollowBean;
import com.ctb_open_car.bean.newsbean.ColumnTheme;
import com.ctb_open_car.bean.newsbean.NewsBean;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.NewshomeApi;
import com.ctb_open_car.utils.ScreenUtil;
import com.ctb_open_car.view.activity.login.LoginActivity;
import com.ctb_open_car.view.adapter.newsadapter.ColumnFragmentAdapter;
import com.ctb_open_car.view.adapter.newsadapter.ColumnRecyclerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.RxRetrofitApp;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.yinglan.scrolllayout.ScrollLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class ColumnActivity extends RxAppCompatActivity {
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.community_tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.scroll_down_layout)
    ScrollLayout mScrollLayout;
    @BindView(R.id.root)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.text_foot)
    TextView mTextFoot;
    @BindView(R.id.ic_back)
    ImageView mBack;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.titile_bar)
    RelativeLayout mTitileBar;
    @BindView(R.id.column_logo)
    ImageView mColumnLogo;
    @BindView(R.id.column_name)
    TextView mColumnName;
    @BindView(R.id.column_des)
    TextView mColumnDes;
    @BindView(R.id.column_lay)
    LinearLayout mColumnLay;
    @BindView(R.id.subscription)
    ImageView mSubscription;
    @BindView(R.id.column_info)
    RelativeLayout mColumnInfo;
    @BindView(R.id.column_tip)
    TextView mColumnTip;
    @BindView(R.id.column_subscription)
    TextView mColumnSubscription;
    @BindView(R.id.recyuclerview)
    RecyclerView mRecyuclerview;
    @BindView(R.id.group_head)
    TextView mGroupHead;
    @BindView(R.id.group_name1)
    TextView mGroupName1;
    @BindView(R.id.group_des1)
    TextView mGroupDes1;
    @BindView(R.id.group_name2)
    TextView mGroupName2;
    @BindView(R.id.group_des2)
    TextView mGroupDes2;
    @BindView(R.id.column_tip_lay)
    LinearLayout columnTipLay;

    private ColumnFragmentAdapter mAdapter;
    private ColumnRecyclerAdapter mColAdapter;
    private List<String> names;
    private List<ColumnFollowBean.FollowerBean> mListParent = new ArrayList<>();
    private ColumnTheme mColumnTheme;
    private ColumnDataBean mColumnDataBean;
    private int[] tabIcons = {
            R.drawable.dynamic,
            R.drawable.popular};
    private RxRetrofitApp mRxInstance;

    private ScrollLayout.OnScrollChangedListener mOnScrollChangedListener = new ScrollLayout.OnScrollChangedListener() {
        @Override
        public void onScrollProgressChanged(float currentProgress) {
            if (currentProgress >= 0) {
                float precent = 255 * currentProgress;
                if (precent > 255) {
                    precent = 255;
                } else if (precent < 0) {
                    precent = 0;
                }
                mScrollLayout.getBackground().setAlpha(255 - (int) precent);
            }
         //   if (mTextFoot.getVisibility() == View.VISIBLE)
            mTextFoot.setVisibility(View.VISIBLE);
            Drawable drawable = getDrawable(R.drawable.news_pull_down);
            mTextFoot.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null,null);
        }

        @Override
        public void onScrollFinished(ScrollLayout.Status currentStatus) {
            if (currentStatus.equals(ScrollLayout.Status.EXIT) || currentStatus.equals(ScrollLayout.Status.OPENED) ) {
                mTextFoot.setVisibility(View.VISIBLE);
                Drawable drawable = getDrawable(R.drawable.news_pull_up);
                mTextFoot.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null,null);
            }
        }

        @Override
        public void onChildScroll(int top) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_column);
        ButterKnife.bind(this);
        mRxInstance = RxRetrofitApp.Singleton.INSTANCE.get();
        mColumnTheme = (ColumnTheme) getIntent().getSerializableExtra("theme");
        mTitleTv.setText(mColumnTheme.getThemeName());
        // 初始化页卡
        initData();
        initPager();
        initView();
        setupTabIcons();
    }

    private void setupTabIcons() {
        mTabLayout.getTabAt(0).setCustomView(getTabView(0));
        mTabLayout.getTabAt(1).setCustomView(getTabView(1));
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.news_tab_item, null);
        TextView txt_title = (TextView) view.findViewById(R.id.img_title);
        txt_title.setText(names.get(position));
        ImageView img_title = (ImageView) view.findViewById(R.id.tab_image);
        img_title.setImageResource(tabIcons[position]);
        return view;
    }

    private void initView() {
        /**设置 setting*/
        mScrollLayout.setMinOffset(0);
        mScrollLayout.setMaxOffset((int) (ScreenUtil.getScreenHeight(this) * 0.5));
        mScrollLayout.setExitOffset(ScreenUtil.dip2px(this, 300));
        mScrollLayout.setIsSupportExit(true);
        mScrollLayout.setAllowHorizontalScroll(true);
        mScrollLayout.setOnScrollChangedListener(mOnScrollChangedListener);
        mScrollLayout.setToExit();

        mScrollLayout.getBackground().setAlpha(0);
        mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScrollLayout.scrollToExit();
            }
        });

        mTextFoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScrollLayout.setToOpen();
            }
        });

    }

    private void initData() {
        names = new ArrayList<>();
        names.add(getString(R.string.dynamic));
        names.add(getString(R.string.popular));
        getColumnDynamicDataList("1");
    }

    //获取动态 和 热门的 数据
    public void getColumnDynamicDataList(String type) {
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("themeId",String.valueOf(mColumnTheme.getThemeId()));
        queryMap.put("type",type);

        NewshomeApi newshomeApi = new NewshomeApi(new HttpListener() {
            @Override
            public void onNext(Object object) {
                super.onNext(object);
                BaseResultEntity<ColumnDataBean> beanBaseResultEntity = (BaseResultEntity<ColumnDataBean>)object;
                mColumnDataBean = beanBaseResultEntity.getData();
                setColumnData();
            }

            @Override
            public  void onError(Throwable e){
                Timber.e("getColumnDynamicDataList %s", e.toString());
            }
        }, ColumnActivity.this, 2);
        newshomeApi.setRequestBody(queryMap);
        HttpManager.getInstance().doHttpDeal(newshomeApi);
    }

    public void setColumnData() {
        mTitleTv.setText(mColumnTheme.getThemeName());
        Glide.with(this).asBitmap().circleCrop().load(mColumnDataBean.getTheme().getThemeImg()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                mColumnLogo.setImageBitmap(resource);
            }
        });

        mColumnName.setText(mColumnDataBean.getTheme().getThemeName());
        mColumnDes.setText(mColumnDataBean.getTheme().getThemeDesc());

        mColumnTip.setText(mColumnDataBean.getFollow().getFollowsDesc());
        mColumnSubscription.setText(mColumnDataBean.getFollow().getFollowFriendDesc());
        if (mColumnDataBean.getTheme() == null || mColumnDataBean.getTheme().getFollowStatus() ==1) {
            mSubscription.setSelected(true);
        } else {
            mSubscription.setSelected(false);
        }

        mListParent = mColumnDataBean.getFollow().getFollowers();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyuclerview.setLayoutManager(linearLayoutManager);
        mColAdapter = new ColumnRecyclerAdapter(this, mListParent);
        mRecyuclerview.setAdapter(mColAdapter);
    }

    public void getColumnFollow() {
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("themeId",String.valueOf(mColumnTheme.getThemeId()));
        NewshomeApi hostFeedsApi = new NewshomeApi(new HttpListener() {
            @Override
            public void onNext(Object object) {
                super.onNext(object);
                JsonObject jsonObject = (JsonObject)object;
                String code = jsonObject.get("code").toString();
                int codeIndex = Integer.parseInt(code =code .replace("\"", ""));
                if (codeIndex == 0)   {
                    Toasty.info(ColumnActivity.this, getString(R.string.subscription)).show();
                    mSubscription.setSelected(true);
                }
            }

            @Override
            public  void onError(Throwable e){
                Timber.e("getColumnFollow %s", e.toString());
            }
        }, ColumnActivity.this, 1);
        hostFeedsApi.setRequestBody(queryMap);
        HttpManager.getInstance().doHttpDeal(hostFeedsApi);
    }

    private void initPager() {
        mAdapter = new ColumnFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mTabLayout.setupWithViewPager(mViewPager);
        mAdapter.setList(names, mColumnTheme);
        // 关联切换
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 取消平滑切换
                mViewPager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @OnClick({R.id.ic_back, R.id.subscription})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ic_back:
                onBackPressed();
                break;
            case R.id.subscription:

                if (mRxInstance.mHeadBean.getUserId() > 0) {
                    if (mColumnDataBean.getTheme() == null || mColumnDataBean.getTheme().getFollowStatus() ==1) {
                        Toasty.info(this, getString(R.string.subscribed)).show();
                    } else {
                        getColumnFollow();
                    }
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    LatLng mCurrentLoc = new LatLng(mRxInstance.mHeadBean.getLatitude(), mRxInstance.mHeadBean.getLongitude());
                    intent.putExtra("LatLng", mCurrentLoc);
                    startActivity(intent);
                }


                break;
            default:
                break;
        }
    }
}
