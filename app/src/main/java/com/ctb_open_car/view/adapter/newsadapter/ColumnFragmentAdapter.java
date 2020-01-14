package com.ctb_open_car.view.adapter.newsadapter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ctb_open_car.bean.newsbean.ColumnTheme;
import com.ctb_open_car.view.fragment.news.ColumnFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息内容子页面适配器
 */
public class ColumnFragmentAdapter extends FragmentPagerAdapter {
    private List<String> mTablayList;
    private ColumnTheme mColumnTheme;
    public ColumnFragmentAdapter(FragmentManager fm) {
        super(fm);
        this.mTablayList = new ArrayList<>();
    }

    /**
     * 数据列表
     *
     * @param datas
     */
    public void setList(List<String> datas, ColumnTheme columnTheme) {
        this.mTablayList.clear();
        this.mTablayList.addAll(datas);
        mColumnTheme = columnTheme;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        ColumnFragment fragment = new ColumnFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", mTablayList.get(position));
        bundle.putSerializable("theme", mColumnTheme);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return mTablayList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String plateName = mTablayList.get(position);
        if (plateName == null) {
            plateName = "";
        } else if (plateName.length() > 15) {
            plateName = plateName.substring(0, 15) + "...";
        }
        return plateName;
    }
}
