package com.ctb_open_car.view.fragment.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
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

import com.ctb_open_car.R;
import com.ctb_open_car.constraints.AppContraint;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PoiEditDialog extends DialogFragment {
    @BindView(R.id.poi_cancel_action)
    TextView mCancel;

    @BindView(R.id.poi_edit_title)
    TextView mTitleName;
    private Context mContext;
    private ActionCallback mCallback;
    private int mType;
    private String mTitle;

    public PoiEditDialog(Context context, ActionCallback callback) {
        this.mContext = context;
        this.mCallback = callback;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public void setType(String titleName) {
        this.mTitle = titleName;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v = LayoutInflater.from(mContext).inflate(R.layout.poi_edit_dialog, null);
        ButterKnife.bind(this, v);
        if (AppContraint.POICollect.LOCATION_COMPANY_RESULT_CODE == mType) {
            mTitleName.setText("公司");
        } else if (AppContraint.POICollect.LOCATION_HOME_RESULT_CODE == mType) {
            mTitleName.setText("家");
        } else {
            mTitleName.setText(mTitle);
        }
        return v;
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

    @OnClick(R.id.poi_cancel_action)
    void dismissDialog(View v) {
        dismiss();
    }


    @OnClick(R.id.poi_delete_action)
    void deleteAction(View v) {
        if (mContext.getResources().getString(R.string.poi_type_home).equals(mTitle)) {
            mCallback.onEditListener(AppContraint.POICollect.LOCATION_HOME_RESULT_CODE);
        } else if (mContext.getResources().getString(R.string.poi_type_company).equals(mTitle)) {
            mCallback.onEditListener(AppContraint.POICollect.LOCATION_COMPANY_RESULT_CODE);
        } else {
            mCallback.onDeleteListener(mType);
        }
        dismiss();
    }

    @OnClick(R.id.poi_edit_action)
    void editAction(View v) {
        if (mContext.getResources().getString(R.string.poi_type_home).equals(mTitle)) {
            mCallback.onEditListener(AppContraint.POICollect.LOCATION_HOME_RESULT_CODE);
        } else if (mContext.getResources().getString(R.string.poi_type_company).equals(mTitle)) {
            mCallback.onEditListener(AppContraint.POICollect.LOCATION_COMPANY_RESULT_CODE);
        } else {
            mCallback.onEditListener(mType);
        }
        dismiss();
    }


    public interface ActionCallback {
        void onEditListener(int type);

        void onDeleteListener(int type);
    }
}
