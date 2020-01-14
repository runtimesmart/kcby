package com.ctb_open_car.view.fragment.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ctb_open_car.R;
import com.ctb_open_car.utils.DateUtils;
import com.ctb_open_car.utils.Device;
import com.ctb_open_car.view.activity.map.NaviActivity;
import com.ctb_open_car.view.activity.map.NaviSearchActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NaviFinishedDialog extends DialogFragment {


    @BindView(R.id.layout_food)
    LinearLayout mFoodFilter;

    @BindView(R.id.layout_wc)
    LinearLayout mWCFilter;

    @BindView(R.id.layout_gas)
    LinearLayout mGasFilter;

    @BindView(R.id.layout_supermarket)
    LinearLayout mMarketFilter;

    @BindView(R.id.navi_all_length)
    TextView mAllLengthTxt;

    @BindView(R.id.navi_all_time)
    TextView mAllTimeTxt;


    private NaviActivity mActivity;

    private int mAllLength;
    private int mAllTime;

    public NaviFinishedDialog(NaviActivity activity) {
        this.mActivity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.TrafficDialog);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Bundle b = getArguments();
        mAllLength = b.getInt("all_length");
        mAllTime = b.getInt("all_time");
        View v = LayoutInflater.from(mActivity).inflate(R.layout.navi_finished_dialog, null);
        ButterKnife.bind(this, v);
        initData();
        return v;
    }

    private void initData() {
        if (mAllLength >= 1000) {
            float f = (float) mAllLength / 1000.0F;
            f = (float) Math.round(f * 10.0F) / 10.0F;
            mAllLengthTxt.setText(f + " 公里");
        } else {
            mAllLengthTxt.setText(mAllLength + " 米");
        }

        mAllTimeTxt.setText(DateUtils.secToTime(mAllTime));
    }


    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.5f;
        windowParams.width = Device.getScreenWidth() - Device.dip2px(50);

        window.setAttributes(windowParams);
    }

    @OnClick(R.id.close_dialog)
    void dismissDialog() {
        dismiss();
    }

    @OnClick({R.id.layout_food, R.id.layout_wc
            , R.id.layout_gas,
            R.id.layout_supermarket})
    void onFilterClick(View v) {

        int id = v.getId();
        Intent i = new Intent(mActivity, NaviSearchActivity.class);
        switch (id) {
            case R.id.layout_food:
                i.putExtra("filter_key", "美食");
                break;
            case R.id.layout_gas:
                i.putExtra("filter_key", "加油站");
                break;
            case R.id.layout_wc:
                i.putExtra("filter_key", "厕所");
                break;
            case R.id.layout_supermarket:
                i.putExtra("filter_key", "超市");
                break;
        }
        mActivity.startActivity(i);
        mActivity.finishAct();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        mActivity.finishAct();

    }
}
