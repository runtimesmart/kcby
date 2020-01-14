package com.ctb_open_car.view.fragment.dialog;


import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ctb_open_car.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/*******   发布动态，显示附近地理位置的列表  ******/
public class ReportRoadConditionDialogFragment extends DialogFragment {

    private RoadConditionClickListener mRoadListener;

    public static ReportRoadConditionDialogFragment newInstance(String title, String msg) {
        ReportRoadConditionDialogFragment frag = new ReportRoadConditionDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("msg", msg);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.map_report_road_dialog, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        attributes.gravity = Gravity.BOTTOM;
        window.setAttributes(attributes);
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick({R.id.close_image, R.id.police_road_inspection, R.id.violation_sticker, R.id.free_parking, R.id.police_taking_pictures, R.id.traffic_accident, R.id.severe_congestion})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close_image:
                dismiss();
                break;
            case R.id.police_road_inspection:
                mRoadListener.onClickListenerRoadCondition(getString(R.string.police_road_inspection),1);
                dismiss();
                break;
            case R.id.violation_sticker:
                mRoadListener.onClickListenerRoadCondition(getString(R.string.violation_sticker),2);
                dismiss();
                break;
            case R.id.free_parking:
                mRoadListener.onClickListenerRoadCondition(getString(R.string.free_parking),3);
                dismiss();
                break;
            case R.id.police_taking_pictures:
                mRoadListener.onClickListenerRoadCondition(getString(R.string.police_taking_pictures),4);
                dismiss();
                break;
            case R.id.traffic_accident:
                mRoadListener.onClickListenerRoadCondition(getString(R.string.traffic_accident),5);
                dismiss();
                break;
            case R.id.severe_congestion:
                mRoadListener.onClickListenerRoadCondition(getString(R.string.severe_congestion),6);
                dismiss();
                break;
        }
    }

    @OnLongClick({R.id.police_road_inspection, R.id.violation_sticker, R.id.free_parking, R.id.police_taking_pictures, R.id.traffic_accident, R.id.severe_congestion})
    public boolean OnLongClick(View view) {
        switch (view.getId()) {
            case R.id.police_road_inspection:
                mRoadListener.onClickLongListenerRoadCondition(getString(R.string.police_road_inspection), 1);
                dismiss();
                break;
            case R.id.violation_sticker:
                mRoadListener.onClickLongListenerRoadCondition(getString(R.string.violation_sticker), 2);
                dismiss();
                break;
            case R.id.free_parking:
                mRoadListener.onClickLongListenerRoadCondition(getString(R.string.free_parking),3);
                dismiss();
                break;
            case R.id.police_taking_pictures:
                mRoadListener.onClickLongListenerRoadCondition(getString(R.string.police_taking_pictures),4);
                dismiss();
                break;
            case R.id.traffic_accident:
                mRoadListener.onClickLongListenerRoadCondition(getString(R.string.traffic_accident),5);
                dismiss();
                break;
            case R.id.severe_congestion:
                mRoadListener.onClickLongListenerRoadCondition(getString(R.string.severe_congestion),6);
                dismiss();
                break;
        }
        return true;
    }

    public void setmRoadListener(RoadConditionClickListener onClick) {
        mRoadListener = onClick;
    }
    public interface RoadConditionClickListener{
        void onClickListenerRoadCondition(String rcName, int rcThype);
        void onClickLongListenerRoadCondition(String rcName, int rcThype);
    }

}
