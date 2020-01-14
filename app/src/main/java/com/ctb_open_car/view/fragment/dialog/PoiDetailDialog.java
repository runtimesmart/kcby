package com.ctb_open_car.view.fragment.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.PoiItem;
import com.ctb_open_car.R;
import com.ctb_open_car.utils.Device;
import com.ctb_open_car.view.activity.map.NaviActivity;
import com.ctb_open_car.view.activity.map.NaviSearchActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PoiDetailDialog extends DialogFragment {
    @BindView(R.id.poi_name)
    TextView mPoiName;

    @BindView(R.id.poi_distance)
    TextView mPoiDistance;

    @BindView(R.id.poi_address)
    TextView mPoiAddress;

    private NaviSearchActivity mActivity;
    private PoiItem mPoiItem;


    public PoiDetailDialog(NaviSearchActivity activity, PoiItem poiItem) {
        this.mActivity = activity;
        this.mPoiItem = poiItem;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        View v = LayoutInflater.from(mActivity).inflate(R.layout.poi_detail_layout, null);
        ButterKnife.bind(this, v);
        initData();
        return v;
    }

    private void initData() {
        mPoiName.setText(mPoiItem.getTitle());
        mPoiDistance.setText("距您" + getFormatDistance(mPoiItem.getDistance()));
        mPoiAddress.setText(mPoiItem.getSnippet());
    }


    private String getFormatDistance(int distance) {
        String friendly = "";
        if (distance >= 1000) {
            friendly = distance / 1000 + "公里";
        } else {
            friendly = distance + "米";
        }
        return friendly;

    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.5f;

        // 如果不设置这句代码, 那么弹框就会与四边都有一定的距离
        window.setBackgroundDrawableResource(android.R.color.transparent);
        // 设置动画
        window.setWindowAnimations(R.style.bottomDialog);

        windowParams.gravity = Gravity.BOTTOM;
        // 如果不设置宽度,那么即使你在布局中设置宽度为 match_parent 也不会起作用
        windowParams.width = getResources().getDisplayMetrics().widthPixels;
        window.setAttributes(windowParams);

    }

    @OnClick(R.id.poi_to_navi)
    void toNavi(View v) {
        Intent intent = new Intent(mActivity, NaviActivity.class);
        NaviLatLng latLng = new NaviLatLng(mPoiItem.getLatLonPoint().getLatitude()
                , mPoiItem.getLatLonPoint().getLongitude());
        intent.putExtra("lat_lng", latLng);
        mActivity.startActivity(intent);
    }
}
