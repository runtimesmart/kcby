package com.ctb_open_car.view.adapter.person;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ctb_open_car.view.fragment.comminity.ExpertsFragment;
import com.ctb_open_car.view.fragment.comminity.FanListFragment;
import com.ctb_open_car.view.fragment.comminity.UGCEventFragment;
import com.ctb_open_car.view.fragment.comminity.UGCHotFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息内容子页面适配器
 */
public class PersonFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragemnts;
    private String mUserId;
    private Bundle bundle=new Bundle();
    public PersonFragmentAdapter(FragmentManager fm,String userId) {
        super(fm);
        this.mUserId=userId;
        this.mFragemnts = new ArrayList();
        mFragemnts.add(new UGCHotFragment());
        mFragemnts.add(new UGCEventFragment());
        mFragemnts.add(new ExpertsFragment());
        mFragemnts.add(new FanListFragment());
        bundle.putString("user_id",mUserId);
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = mFragemnts.get(position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return mFragemnts.size();
    }


}
