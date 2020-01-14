package com.ctb_open_car.customview;


import java.util.List;

import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

/**
 * @author 作者  刘明昆 Q:571039838
 * @version 创建时间：2018年8月27日 下午4:47:48
 * 类说明
 */
public class ViewPagerAdapter extends PagerAdapter {

    private List<View> viewList;

    public ViewPagerAdapter(List<View> viewList){
        this.viewList=viewList;
    }

    @Override
    public int getCount() {
        if(viewList!=null){
            return viewList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;
    }

    //初始化节点
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    //销毁节点
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }
}