package com.ctb_open_car.view.activity.community;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseActivity;
import com.ctb_open_car.view.fragment.CommunityFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommunityActivty extends BaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        ButterKnife.bind(this);
        getSupportFragmentManager()    //
                .beginTransaction()
                .add(R.id.community_layout,new CommunityFragment())   // 此处的R.id.fragment_container是要盛放fragment的父容器
                .commit();
    }

    @Override
    public Object getTag() {
        return null;
    }
}
