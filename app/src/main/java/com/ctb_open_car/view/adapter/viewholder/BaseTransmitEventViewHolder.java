package com.ctb_open_car.view.adapter.viewholder;

import android.view.View;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ctb_open_car.R;
import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class BaseTransmitEventViewHolder extends BaseEventViewHolder {
    //活动中动态消息的布局
    @BindView(R.id.feed_content_origin)
    public View mEventOriginView;

    @BindView(R.id.feed_content_transmit)
    public View mEventTransmitView;


    @BindView(R.id.ugc_feeds_transmitted_txt)
    public QMUISpanTouchFixTextView mEventTransmittedText;

    @BindView(R.id.feeds_extra_info)
    public RelativeLayout mLocationLayout;

    @BindView(R.id.ugc_feed_location)
    public TextView mLocationText;

    public BaseTransmitEventViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public boolean verifyStatus(long endTime) {
        if (endTime <= System.currentTimeMillis()) {
            return false;
        } else {
            return true;
        }
    }

    /**用户头像点击*/
    @OnClick(R.id.feed_user_layout)
    protected void onAvatarClick(View v){

    }

}
