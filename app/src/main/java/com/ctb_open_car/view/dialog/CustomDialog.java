package com.ctb_open_car.view.dialog;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.ctb_open_car.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*******  圆形进度条  ******/
public class CustomDialog extends DialogFragment {
    @BindView(R.id.btn_ok)
    Button mDialogOk;
    @BindView(R.id.btn_cancel)
    Button mDialogCancel;
    @BindView(R.id.dialog_tips)
    TextView mDialogTips;

    public OnClickListener onClickListener;
    private String mCancelText;
    private String mOkText;
    private String mContentText;
    private int mLayoutId = -1;

    public static CustomDialog newInstance(String cancelText, String okText, String contentText) {
        CustomDialog frag = new CustomDialog();
        return frag;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.dialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = null;
        if (mLayoutId == -1) {
            view = inflater.inflate(R.layout.custom_dialog, container, false);
        } else {
            view = inflater.inflate(mLayoutId, container, false);
        }
        ButterKnife.bind(this, view);

        if (!TextUtils.isEmpty(mOkText)) {
            mDialogOk.setText(mOkText);
        }
        if (!TextUtils.isEmpty(mCancelText)) {
            mDialogCancel.setText(mCancelText);
        }

        if (!TextUtils.isEmpty(mContentText)) {
            mDialogTips.setText(mContentText);
        }

        return view;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            //在每个add事务前增加一个remove事务，防止连续的add
            manager.beginTransaction().remove(this).commit();
            super.show(manager, tag);
        } catch (Exception e) {
            //同一实例使用不同的tag会异常,这里捕获一下
            e.printStackTrace();
        }
    }

    public void setLayoutId(int layoutId) {
        mLayoutId = layoutId;
    }
    public void setDialogOkText(String text) {
        mOkText = text;
    }

    public void setDialogCancelText(String text) {
        mCancelText = text;
    }

    public void setDialogContentText(String text) {
        mContentText = text;
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @OnClick({R.id.btn_ok, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                if (onClickListener != null) {
                    onClickListener.setOkListener();
                }
                break;
            case R.id.btn_cancel:
                if (onClickListener != null) {
                    onClickListener.setCancelListener();
                }
                break;
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    public interface OnClickListener{
        void setOkListener();
        void setCancelListener();
    }
}
