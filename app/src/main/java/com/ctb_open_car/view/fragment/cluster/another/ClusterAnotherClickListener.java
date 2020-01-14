package com.ctb_open_car.view.fragment.cluster.another;

import com.amap.api.maps.model.Marker;
import com.ctb_open_car.view.fragment.cluster.item.ClusterItem;

import java.util.List;


/**
 * Created by moos on 2018/1/12.
 */

public interface ClusterAnotherClickListener {
    /**
     * 点击聚合点的回调处理函数
     *
     * @param marker
     *            点击的聚合点
     * @param clusterItems
     *            聚合点所包含的元素
     */
    public void onAnotherClick(Marker marker, List<ClusterItem> clusterItems);
}
