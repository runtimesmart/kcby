package com.ctb_open_car.view.activity.community;

import android.os.Bundle;

import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseActivity;

public class CarToolActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartool);
        setTitletName("车主工具");

    }

    @Override
    public Object getTag() {
        return null;
    }
}
