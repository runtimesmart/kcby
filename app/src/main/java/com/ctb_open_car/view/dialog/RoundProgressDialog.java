package com.ctb_open_car.view.dialog;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ctb_open_car.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*******  圆形进度条  ******/
public class RoundProgressDialog extends DialogFragment {

    @BindView(R.id.dialog_icon)
    ImageView mDialogIcon;
    @BindView(R.id.dialog_tips)
    TextView mDialogTips;
    private String mMsg;

    public static RoundProgressDialog newInstance(String msg) {
        RoundProgressDialog frag = new RoundProgressDialog();
        Bundle args = new Bundle();
        args.putString("msg", msg);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.dialogTheme);
        mMsg = getArguments().getString("msg");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.round_progress_dialog, container, false);
        ButterKnife.bind(this, view);

        RotateAnimation mRotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mRotateAnimation.setDuration(1000);
        mRotateAnimation.setRepeatCount(-1);
        mDialogIcon.startAnimation(mRotateAnimation);
        mDialogTips.setText(mMsg);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

}
