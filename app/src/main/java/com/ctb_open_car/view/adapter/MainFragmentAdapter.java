package com.ctb_open_car.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ctb_open_car.view.fragment.CommunityFragment;
import com.ctb_open_car.view.fragment.MeFragment;
import com.ctb_open_car.view.fragment.NewsFragment;
import com.ctb_open_car.view.fragment.MapFragment;
import com.ctb_open_car.view.fragment.ShoppingMallFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面底部菜单适配器
 */
public class MainFragmentAdapter extends FragmentPagerAdapter {
    public List<Fragment> fragments;
    public MainFragmentAdapter(FragmentManager fm) {
        super(fm);
        fragments=new ArrayList<Fragment>(){{
            add(new MapFragment());
            add(new CommunityFragment());
            add(new NewsFragment());
            add(new ShoppingMallFragment());
            add(new MeFragment());
        }};
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = fragments.get(i);
//        switch (i) {
//            case 0:
//                fragment = new MapFragment();
//                break;
//            case 1:
//                fragment = new NewsFragment();
//                break;
////            case 2:
////               // fragment = new ShoppingMallFragment();
////                break;
//            case 2:
//                fragment = new ShoppingMallFragment();
//                break;
//            case 3:
//                fragment = new MeFragment();
//                break;
//            default:
//                break;
//        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 5;
    }

}
