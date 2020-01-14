package com.ctb_open_car.view.activity.im;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseActivity;
import com.ctb_open_car.bean.community.response.group.GroupDto;
import com.ctb_open_car.ui.RecycleViewDivider;
import com.ctb_open_car.utils.Device;
import com.ctb_open_car.view.adapter.im.MyGroupListAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyGroupMoreActivity extends BaseActivity {
    @BindView(R.id.my_group_tab)
    TabLayout mTabLayout;

    @BindView(R.id.my_empty_group_layout)
    RelativeLayout mEmptyGroupLayout;


    @BindView(R.id.my_group_list)
    RecyclerView mMyGroupListView;
    TextView mTabTitleText;
    View mTabIndicator;
    private static final int TAB_TEXT_SIZE = 17;
    private MyGroupListAdapter mMyGroupListAdapter;
    private List<GroupDto> mAllGroupList;
    private List<GroupDto> mMyGroupList;
    private RecycleViewDivider mMyDivider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAllGroupList = (List<GroupDto>) getIntent().getSerializableExtra("my_group_bean");
        setContentView(R.layout.im_my_group);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        String[] tabString = getResources().getStringArray(R.array.my_group_tag);
        for (int i = 0; i < tabString.length; i++) {
            TabLayout.Tab tab = mTabLayout.newTab();
            View v = LayoutInflater.from(this).inflate(R.layout.community_tab, null);
            tab.setCustomView(v);
            mTabTitleText = v.findViewById(R.id.community_title);
            mTabIndicator = v.findViewById(R.id.tab_item_indicator);

            ViewGroup.LayoutParams layoutParams = mTabIndicator.getLayoutParams();
            layoutParams.width = Device.dip2px(20);
            layoutParams.height = Device.dip2px(3);
            mTabIndicator.setLayoutParams(layoutParams);
            mTabTitleText.setText(tabString[i]);
            tab.setCustomView(v);
            mTabLayout.addTab(tab);

            //初始化第一个tab选中样式
            if (0 == i) {
                setTabStytle(tab, true);
            }
        }
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
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

    private void initListView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mMyGroupListView.setLayoutManager(layoutManager);
        /**防止fragment缓存item 导致每次加载divider导致 间隔不断变大*/
        if (null == mMyDivider) {
            mMyDivider = new RecycleViewDivider(this, DividerItemDecoration.VERTICAL, Color.parseColor("#F5F5F5"), 10);
            mMyGroupListView.addItemDecoration(mMyDivider);
        }
        mMyGroupListAdapter = new MyGroupListAdapter(this);
        mMyGroupListView.setAdapter(mMyGroupListAdapter);
    }

    @OnClick(R.id.btn_back)
    void toBackFinish(View v) {
        onBackPressed();
    }

    @OnClick(R.id.btn_create)
    void toCreateGroup(View v) {
        Intent intent = new Intent(this, CreateGroupActivity.class);
        startActivity(intent);
    }

    @Override
    public Object getTag() {
        return null;
    }

    private void setTabStytle(TabLayout.Tab tab, boolean selected) {
        mTabTitleText = tab.getCustomView().findViewById(R.id.community_title);
        mTabIndicator = tab.getCustomView().findViewById(R.id.tab_item_indicator);
        TextPaint textPaint = mTabTitleText.getPaint();
        mTabTitleText.setTextSize(TAB_TEXT_SIZE);
        if (selected) {
            mTabTitleText.setTextColor(getResources().getColor(R.color.color_3240DB));
            mTabIndicator.setVisibility(View.VISIBLE);
            textPaint.setFakeBoldText(true);
        } else {
            mTabTitleText.setTextColor(getResources().getColor(R.color.color_C8CCD4));
            mTabIndicator.setVisibility(View.INVISIBLE);
            textPaint.setFakeBoldText(false);
        }

        initListView();
        if (0 == tab.getPosition()) {
            mEmptyGroupLayout.setVisibility(View.GONE);
            if (null != mAllGroupList) {
                mMyGroupListAdapter.setData(mAllGroupList);
            }
        } else {
            mMyGroupList = new ArrayList<>();
            Iterator<GroupDto> iGroupList = mAllGroupList.iterator();
            while (iGroupList.hasNext()) {
                GroupDto groupDto = iGroupList.next();
                if (1 == groupDto.getIfHost()) {
                    mMyGroupList.add(groupDto);
                }
            }
            if (0 == mMyGroupList.size()) {
                mEmptyGroupLayout.setVisibility(View.VISIBLE);
            } else {
                mEmptyGroupLayout.setVisibility(View.GONE);
            }

            mMyGroupListAdapter.setData(mMyGroupList);
        }
    }
}
