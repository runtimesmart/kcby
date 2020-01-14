package com.ctb_open_car.view.adapter.community;


import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ctb_open_car.view.fragment.comminity.CarGroupFragment;
import com.ctb_open_car.view.fragment.comminity.ExpertsFragment;
import com.ctb_open_car.view.fragment.comminity.UGCEventFragment;
import com.ctb_open_car.view.fragment.comminity.UGCFocusFragment;
import com.ctb_open_car.view.fragment.comminity.UGCHotFragment;
import com.ctb_open_car.view.fragment.comminity.UGCNearbyFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息内容子页面适配器
 */
public class CommunityFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragemnts;

    public CommunityFragmentAdapter(FragmentManager fm) {
        super(fm);
        this.mFragemnts = new ArrayList();
        mFragemnts.add(new UGCHotFragment());
        mFragemnts.add(new UGCNearbyFragment());
        mFragemnts.add(new UGCFocusFragment());
        mFragemnts.add(new UGCEventFragment());
        mFragemnts.add(new CarGroupFragment());
        mFragemnts.add(new ExpertsFragment());
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = mFragemnts.get(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return mFragemnts.size();
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }
}
