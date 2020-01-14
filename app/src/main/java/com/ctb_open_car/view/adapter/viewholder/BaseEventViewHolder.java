package com.ctb_open_car.view.adapter.viewholder;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.ctb_open_car.R;
import com.ctb_open_car.view.fragment.dialog.EnrollEventDialog;
import com.ctb_open_car.wxapi.WeiXinShareManager;
import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView;

import butterknife.BindView;

import static com.ctb_open_car.wxapi.WeiXinShareManager.WECHAT_SHARE_WAY_TEXT;

public class BaseEventViewHolder extends BaseViewHolder {
    // 活动开始日期
    @BindView(R.id.event_begin_date)
    public TextView mStartDate;
    @BindView(R.id.event_apply_deadline)
    public TextView mEventDeadline;

    // 活动报名人数
    @BindView(R.id.event_max_members)
    public TextView mEventMaxMembers;

    //        活动标题
    @BindView(R.id.event_title)
    public TextView mEventTitle;
    //        活动状态
    @BindView(R.id.event_status)
    public TextView mEventStatus;

    //活动日期布局
    @BindView(R.id.feed_event_info)
    public LinearLayout mEventInfoLayout;

    //活动title布局
    @BindView(R.id.feed_event_titleinfo)
    public RelativeLayout mEventTitleInfoLayout;

    //活动封面布局
    @BindView(R.id.feed_event_covers)
    public LinearLayout mEventCoverLayout;
    //活动中原发内容
    @BindView(R.id.ugc_feeds_current_txt)
    public QMUISpanTouchFixTextView mEventCurrentFeedText;

    @BindView(R.id.ugc_feed_ellipsize)
    public TextView mEventEllipsizeText;


    @BindView(R.id.feed_content_origin)
    public View mEventOriginView;

    @BindView(R.id.feed_event_cta)
    public View mEventCTAView;


    public BaseEventViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void ellipsizeOriginEventText(Context context, int ellipsizeText) {
        mEventCurrentFeedText.post(new Runnable() {
            @Override
            public void run() {
                //获取省略的字符数，0表示没有省略
                int ellipsisCount = mEventCurrentFeedText.getLayout().getEllipsisCount(mEventCurrentFeedText.getLineCount() - 1);
//                    mEventsHolder.mEventCurrentFeedText.getLayout().getEllipsisCount(mEventsHolder.mEventCurrentFeedText.getLineCount() - 1);
                //ellipsisCount>0说明没有显示全部，存在省略部分。
                if (ellipsisCount > 0) {
                    mEventEllipsizeText.setVisibility(View.VISIBLE);
                    String txt = context.getString(ellipsizeText);
                    SpannableString spannableString = new SpannableString(txt);
                    spannableString.setSpan(new UnderlineSpan(), 0, txt.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new ForegroundColorSpan(mEventCurrentFeedText.getContext().getResources().getColor(R.color.color_3240DB)),
                            0, txt.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    mEventEllipsizeText.setText(spannableString);
//
                } else {
                    mEventEllipsizeText.setVisibility(View.GONE);
                }
            }
        });
    }

}