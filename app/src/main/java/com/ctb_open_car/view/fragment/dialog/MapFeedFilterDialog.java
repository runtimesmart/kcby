package com.ctb_open_car.view.fragment.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.ctb_open_car.R;
import com.ctb_open_car.utils.Device;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapFeedFilterDialog extends DialogFragment {

    //    @BindView(R.id.map_feed_filter_girl)
//    RadioButton mFilterGirl;
    private Context mContext;

    public MapFeedFilterDialog(Context context) {
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.TrafficDialog);
        View v = LayoutInflater.from(mContext).inflate(R.layout.map_dialog_bottom_sheet, null);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        // 如果不设置这句代码, 那么弹框就会与四边都有一定的距离
        window.setBackgroundDrawableResource(android.R.color.transparent);
        // 设置动画
        window.setWindowAnimations(R.style.bottomDialog);

        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        // 如果不设置宽度,那么即使你在布局中设置宽度为 match_parent 也不会起作用
        params.width = getResources().getDisplayMetrics().widthPixels;
        window.setAttributes(params);

    }

    @BindView(R.id.map_filter_girl)
    RelativeLayout mLayoutGirl;

    @BindView(R.id.map_filter_boy)
    RelativeLayout mLayoutBoy;

    @BindView(R.id.map_filter_all)
    RelativeLayout mLayoutAll;


    @BindView(R.id.map_filter_search)
    RelativeLayout mLayoutSearch;


    @BindView(R.id.map_feed_filter_cancel)
    TextView mLayoutCancel;


    private void setViewDrawable(ViewGroup viewGroup, int visibile) {
        TextView txt = (TextView) viewGroup.getChildAt(0);
        ImageView checked = (ImageView) viewGroup.getChildAt(1);
        if (View.VISIBLE == visibile) {
            txt.setTextColor(ContextCompat.getColor(getContext(), R.color.color_3240DB));
            checked.setVisibility(View.VISIBLE);
        } else {
            txt.setTextColor(ContextCompat.getColor(getContext(), R.color.color_666666));
            checked.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.map_filter_girl)
    void filterGirl(View v) {
        setViewDrawable(mLayoutGirl, View.VISIBLE);
        setViewDrawable(mLayoutBoy, View.GONE);
        setViewDrawable(mLayoutAll, View.GONE);

    }

    @OnClick(R.id.map_filter_boy)
    void filterBoy(View v) {
        setViewDrawable(mLayoutGirl, View.GONE);
        setViewDrawable(mLayoutBoy, View.VISIBLE);
        setViewDrawable(mLayoutAll, View.GONE);
    }

    @OnClick(R.id.map_filter_all)
    void filterAll(View v) {
        setViewDrawable(mLayoutGirl, View.GONE);
        setViewDrawable(mLayoutBoy, View.GONE);
        setViewDrawable(mLayoutAll, View.VISIBLE);
    }

    @OnClick(R.id.map_filter_search)
    void filterSearch(View v) {

    }

    @OnClick(R.id.map_feed_filter_cancel)
    void filterCancel(View v) {
        dismiss();
    }


}
