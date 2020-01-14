package com.ctb_open_car.view.adapter.newsadapter;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ctb_open_car.bean.newsbean.CategoryName;
import com.ctb_open_car.bean.newsbean.NewsInfoBean;
import com.ctb_open_car.view.fragment.NewsContentFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息内容子页面适配器
 */
public class NewsContentFragmentAdapter extends FragmentPagerAdapter {
    private List<CategoryName> names;
    private List<NewsInfoBean.Category> mListParent;
    public NewsContentFragmentAdapter(FragmentManager fm) {
        super(fm);
        this.names = new ArrayList<>();
        this.mListParent = new ArrayList<>();
    }

    /**
     * 数据列表
     *
     * @param datas
     */
    public void setList(List<CategoryName> datas,List<NewsInfoBean.Category> listParent) {
        this.names.clear();
        this.names.addAll(datas);
        this.mListParent.clear();
        this.mListParent.addAll(listParent);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        NewsContentFragment fragment = new NewsContentFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("categoryName", names.get(position));
        if (position == 0) {
            bundle.putParcelableArrayList("ListParent", (ArrayList<? extends Parcelable>) mListParent);
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String plateName = names.get(position).getCategoryName();
        if (plateName == null) {
            plateName = "";
        } else if (plateName.length() > 15) {
            plateName = plateName.substring(0, 15) + "...";
        }
        return plateName;
    }
}
