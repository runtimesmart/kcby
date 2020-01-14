package com.ctb_open_car.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.ctb_open_car.MainActivity;
import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseFragment;
import com.ctb_open_car.bean.community.response.ad.BannerDto;
import com.ctb_open_car.orriveride.BannerImageLoader;
import com.ctb_open_car.presenter.BannerPresenter;
import com.ctb_open_car.utils.Device;
import com.ctb_open_car.view.activity.community.BannerDetailActivity;
import com.ctb_open_car.view.adapter.community.CommunityFragmentAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 社区
 * <p>展示平均分配位置的tab页卡</p>
 */
public class CommunityFragment extends BaseFragment {

    @BindView(R.id.community_tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.auto_banner)
    Banner mAutoBanner;
    @BindView(R.id.community_appbar)
    AppBarLayout mAppBar;

    TextView mTabTitleText;
    View mTabIndicator;

    private HashMap mTabTitleCache = new HashMap();
    private HashMap mTabIndicatorCache = new HashMap();


    private static final int TAB_TEXT_SIZE = 14;
    private CommunityFragmentAdapter mAdapter;
    private BannerPresenter mBannerPresenter;

    private ArrayList mFilterNames = new ArrayList() {{
        add("热门");
        add("附近");
        add("关注");
        add("活动");
        add("车友群");
        add("达人榜");
    }};


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBannerPresenter = new BannerPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        ButterKnife.bind(this, view);
        configAutoBanner();
        setupTabs();

        initViewPager();
        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                AppBarLayout.LayoutParams tabLayoutParams = new AppBarLayout.LayoutParams(mTabLayout.getLayoutParams());
                if (Math.abs(verticalOffset) >= mAppBar.getTotalScrollRange()) { //折叠状态
                    tabLayoutParams.setMargins(0, 0, 0, 0);
                    mTabLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
                } else {
                    //展开状态
                    tabLayoutParams.setMargins(Device.dip2px(20), Device.dip2px(-6), Device.dip2px(20), 0);

                    mTabLayout.setBackground(getResources().getDrawable(R.drawable.tab_round_shadow));
                }
                mTabLayout.setLayoutParams(tabLayoutParams);
            }
        });
        return view;
    }

    private void initViewPager() {
        mAdapter = new CommunityFragmentAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

    private void setTabStytle(TabLayout.Tab tab, boolean selected) {
        mTabTitleText = (TextView) mTabTitleCache.get(tab);
        mTabIndicator = (View) mTabIndicatorCache.get(tab);

        TextPaint textPaint = mTabTitleText.getPaint();
        mTabTitleText.setTextSize(TAB_TEXT_SIZE);
        if (selected) {
            mTabTitleText.setTextColor(getContext().getResources().getColor(R.color.community_tab_txt_focus_color));
            mTabIndicator.setVisibility(View.VISIBLE);
            textPaint.setFakeBoldText(true);
        } else {
            mTabTitleText.setTextColor(getContext().getResources().getColor(R.color.community_tab_txt_normal_color));
            mTabIndicator.setVisibility(View.INVISIBLE);
            textPaint.setFakeBoldText(false);
        }
    }


    /***
     * 设置TabLayout 子tab的文字
     * */
    private void setupTabs() {
        int i = -1;
        while (i++ < mFilterNames.size() - 1) {
            TabLayout.Tab tab = mTabLayout.newTab();
            View v = LayoutInflater.from(getContext()).inflate(R.layout.community_tab, null);
            tab.setCustomView(v);
            mTabTitleText = v.findViewById(R.id.community_title);
            mTabIndicator = v.findViewById(R.id.tab_item_indicator);


            ViewGroup.LayoutParams layoutParams = mTabIndicator.getLayoutParams();
            layoutParams.width = Device.dip2px(20);
            layoutParams.height = Device.dip2px(3);
            mTabIndicator.setLayoutParams(layoutParams);
            mTabTitleText.setText(mFilterNames.get(i).toString());
            mTabLayout.addTab(tab);

            //缓存view
            mTabTitleCache.put(tab, mTabTitleText);
            mTabIndicatorCache.put(tab, mTabIndicator);

            //初始化第一个tab选中样式
            if (0 == i) {
                setTabStytle(tab, true);
            }
        }
    }

    public void setBannerList(List<BannerDto> bannerList) {
        List bannerImages = new ArrayList();
        for (BannerDto banner : bannerList) {
            bannerImages.add(banner.getAdIcon());
        }
        mAutoBanner.setImages(bannerImages);
        mAutoBanner.start();

        mAutoBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent i = new Intent(getActivity(), BannerDetailActivity.class);
                i.putExtra("banner_url", bannerList.get(position).getOutsiteUrl());
                getActivity().startActivity(i);
            }
        });
    }

    /**
     * 配置轮播图
     */
    private void configAutoBanner() {
        mAutoBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mAutoBanner.setImageLoader(new BannerImageLoader());
        mAutoBanner.setBannerAnimation(Transformer.DepthPage);


        mAutoBanner.isAutoPlay(true);
        mAutoBanner.setDelayTime(3000);
        mAutoBanner.setIndicatorGravity(BannerConfig.CENTER);
    }


    @Override
    public void onStart() {
        super.onStart();
        //启动轮播
        mBannerPresenter.requestBannerList();


    }

    @Override
    public void onStop() {
        super.onStop();
        //停止轮播
//        mAutoBanner.stopAutoPlay();
    }


    @Override
    protected String getTAG() {
        return null;
    }
}
