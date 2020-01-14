package com.ctb_open_car.view.activity.community;

import android.content.Intent;

import android.os.Bundle;

import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseActivity;
import com.ctb_open_car.presenter.FeedDetailPresenter;
import com.ctb_open_car.ui.community.FeedsDetailView;

import butterknife.ButterKnife;
import timber.log.Timber;

public class FeedsDetailActivity extends BaseActivity {

    private FeedsDetailView mDetailView;
    public FeedDetailPresenter mDetailPresenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ugc_topic_layout);
        ButterKnife.bind(this);
        initLayout();
        setTitletName("动态详情");

        initMsgType();
    }

    /**
     * 获取消息类型
     */
    private void initMsgType() {
        Intent fromIntent = getIntent();
        long feedId = fromIntent.getLongExtra("feedId",0);
        Timber.i("feedId==" + feedId);

        initView(feedId+"");
    }

    private void initView(String feedId){
        mDetailView = new FeedsDetailView(this,feedId);
        mDetailPresenter = new FeedDetailPresenter(this, mDetailView);
        mDetailView.setPresenter(mDetailPresenter);
    }


    @Override
    public Object getTag() {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
