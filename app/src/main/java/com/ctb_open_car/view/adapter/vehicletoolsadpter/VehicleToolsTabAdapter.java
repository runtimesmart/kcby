package com.ctb_open_car.view.adapter.vehicletoolsadpter;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ctb_open_car.bean.newsbean.CategoryName;
import com.ctb_open_car.bean.newsbean.NewsInfoBean;
import com.ctb_open_car.bean.vehicletools.VehicleToolsBean;
import com.ctb_open_car.view.fragment.NewsContentFragment;
import com.ctb_open_car.view.fragment.vehicletools.VehicleTabFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 车主工具-主页展示车辆信息
 */
public class VehicleToolsTabAdapter extends FragmentPagerAdapter {
    private List<VehicleToolsBean.CarInfoBean> mCarInfoBeans;
    public VehicleToolsTabAdapter(FragmentManager fm) {
        super(fm);
        this.mCarInfoBeans = new ArrayList<>();
    }


    @Override
    public Fragment getItem(int position) {
        VehicleTabFragment fragment = new VehicleTabFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("CarInfoBeans", mCarInfoBeans.get(position));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return mCarInfoBeans.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String plateName = mCarInfoBeans.get(position).getPlate();
        if (plateName == null) {
            plateName = "";
        } else if (plateName.length() > 15) {
            plateName = plateName.substring(0, 15) + "...";
        }
        return plateName;
    }

    public void setList(List<VehicleToolsBean.CarInfoBean> datas) {
        this.mCarInfoBeans.clear();
        this.mCarInfoBeans.addAll(datas);
        notifyDataSetChanged();
    }
}
