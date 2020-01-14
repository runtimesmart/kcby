package com.ctb_open_car.view.activity.community;

import android.content.Intent;
import android.os.Bundle;

import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseActivity;
import com.ctb_open_car.presenter.ActivityDetailPresenter;
import com.ctb_open_car.ui.community.ActivityDetailView;

import butterknife.ButterKnife;

public class ActivityDetailActivity extends BaseActivity {

    public ActivityDetailPresenter mDetailPresenter;
    private ActivityDetailView mDetailView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ugc_topic_layout);
        ButterKnife.bind(this);
        initLayout();
        setTitletName("活动详情");


        Intent detailIntent = getIntent();
        int activityId = detailIntent.getIntExtra("activityId", 0);

        mDetailView = new ActivityDetailView(this, activityId + "");
        mDetailPresenter = new ActivityDetailPresenter(this, mDetailView);
        //此方法中会请求详情接口和评论列表
        mDetailView.setPresenter(mDetailPresenter);
    }


    @Override
    public Object getTag() {
        return null;
    }
}
