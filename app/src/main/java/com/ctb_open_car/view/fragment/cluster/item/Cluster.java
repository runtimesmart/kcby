package com.ctb_open_car.view.fragment.cluster.item;


import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiyi.qi on 16/10/10.
 */

public class Cluster {


    private LatLng mLatLng;
    private List<ClusterItem> mClusterItems;
    private Marker mMarker;

    public MarkerOptions getMarkerOptions() {
        return markerOptions;
    }

    public void setMarkerOptions(MarkerOptions markerOptions) {
        this.markerOptions = markerOptions;
    }

    private MarkerOptions markerOptions;


    public Cluster(LatLng latLng) {

        mLatLng = latLng;
        mClusterItems = new ArrayList<ClusterItem>();
    }

    public void addClusterItem(ClusterItem clusterItem) {
        mClusterItems.add(clusterItem);
    }

    public int getClusterCount() {
        return mClusterItems.size();
    }


    public LatLng getCenterLatLng() {
        return mLatLng;
    }

    public void setMarker(Marker marker) {
        mMarker = marker;
    }

    public Marker getMarker() {
        return mMarker;
    }

    public List<ClusterItem> getClusterItems() {
        return mClusterItems;
    }
}
