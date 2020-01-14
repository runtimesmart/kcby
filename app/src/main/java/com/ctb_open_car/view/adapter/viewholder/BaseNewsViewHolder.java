package com.ctb_open_car.view.adapter.viewholder;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ctb_open_car.R;
import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView;

import butterknife.BindView;

public class BaseNewsViewHolder extends BaseViewHolder {
    @BindView(R.id.ugc_feed_location)
    public TextView mUserLocation;

    @BindView(R.id.bottom_divider)
    public View mBottomDivider;

    @BindView(R.id.ugc_feeds_current_txt)
    public QMUISpanTouchFixTextView mOriginFeedText;

    @BindView(R.id.ugc_feeds_transmitted_txt)
    public QMUISpanTouchFixTextView mTransmitOriginFeedText;

    @BindView(R.id.ugc_feed_ellipsize)
    public TextView mFeedEllipsizeText;

    @BindView(R.id.ugc_transmit_feed_ellipsize)
    public TextView mTransmitFeedEllipsizeText;

    public BaseNewsViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void ellipsizeOriginDescText(Context context, int ellipsizeText) {
        mOriginFeedText.post(new Runnable() {
            @Override
            public void run() {
                //获取省略的字符数，0表示没有省略
                int ellipsisCount = mOriginFeedText.getLayout().getEllipsisCount(mOriginFeedText.getLineCount() - 1);
//                    mEventsHolder.mEventCurrentFeedText.getLayout().getEllipsisCount(mEventsHolder.mEventCurrentFeedText.getLineCount() - 1);
                //ellipsisCount>0说明没有显示全部，存在省略部分。
                if (ellipsisCount > 0) {
                    mFeedEllipsizeText.setVisibility(View.VISIBLE);
                    String txt = context.getString(ellipsizeText);
                    SpannableString spannableString = new SpannableString(txt);
                    spannableString.setSpan(new UnderlineSpan(), 0, txt.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new ForegroundColorSpan(mOriginFeedText.getContext().getResources().getColor(R.color.color_3240DB)),
                            0, txt.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    mFeedEllipsizeText.setText(spannableString);
//
                } else {
                    mFeedEllipsizeText.setVisibility(View.GONE);
                }
            }
        });
    }


//    public void ellipsizeTransmitDescText(Context context, int ellipsizeText) {
//        mTransmitOriginFeedText.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //获取省略的字符数，0表示没有省略
//                int ellipsisCount = mTransmitOriginFeedText.getLayout().getEllipsisCount(mTransmitOriginFeedText.getLineCount() - 1);
////                    mEventsHolder.mEventCurrentFeedText.getLayout().getEllipsisCount(mEventsHolder.mEventCurrentFeedText.getLineCount() - 1);
//                //ellipsisCount>0说明没有显示全部，存在省略部分。
//                if (ellipsisCount > 0) {
//                    mTransmitFeedEllipsizeText.setVisibility(View.VISIBLE);
//                    String txt = context.getString(ellipsizeText);
//                    SpannableString spannableString = new SpannableString(txt);
//                    spannableString.setSpan(new UnderlineSpan(), 0, txt.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                    spannableString.setSpan(new ForegroundColorSpan(mTransmitFeedEllipsizeText.getContext().getResources().getColor(R.color.color_3240DB)),
//                            0, txt.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//                    mTransmitFeedEllipsizeText.setText(spannableString);
////
//                } else {
//                }
//            }
//        },5000L);
//    }
}
