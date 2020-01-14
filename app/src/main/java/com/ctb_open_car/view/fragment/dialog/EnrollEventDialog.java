package com.ctb_open_car.view.fragment.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.ctb_open_car.R;
import com.ctb_open_car.utils.Device;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EnrollEventDialog extends DialogFragment {
    @BindView(R.id.close_dialog)
    ImageView mCloseBtn;
    @BindView(R.id.enroll_image)
    ImageView mEnrollImage;
    @BindView(R.id.event_title)
    TextView mEventTitle;
    private Context mContext;

    public EnrollEventDialog(Context context) {
        this.mContext = context;
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
        View v = LayoutInflater.from(mContext).inflate(R.layout.enroll_event_layout, null);
        ButterKnife.bind(this, v);
        loadData();
        return v;
    }


    private void loadData() {
        Bundle b = getArguments();
        String title = b.getString("title");
        String image = b.getString("image");
        Glide.with(mContext).load(image).into(mEnrollImage);
        mEventTitle.setText(title);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();

        windowParams.width=Device.getScreenWidth()-Device.dip2px(50);
        windowParams.dimAmount = 0.7f;

        window.setAttributes(windowParams);
    }

    @OnClick(R.id.close_dialog)
    void closeDialog(View v) {
        dismiss();
    }
}
