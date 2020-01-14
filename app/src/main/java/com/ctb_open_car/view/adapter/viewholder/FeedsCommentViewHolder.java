package com.ctb_open_car.view.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ctb_open_car.R;
import com.ctb_open_car.bean.community.response.comment.CommentDto;
import com.ctb_open_car.presenter.LikedPresenter;
import com.ctb_open_car.utils.Device;
import com.ctb_open_car.view.activity.community.FeedsDetailActivity;
import com.ctb_open_car.view.activity.person.PersonHomeActivity;
import com.ctb_open_car.view.adapter.community.FeedCommentsAdapter;
import com.library.InputTextMsgDialog;
import com.qmuiteam.qmui.span.QMUITouchableSpan;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Yl
 */
public class FeedsCommentViewHolder extends RecyclerView.ViewHolder {


    //        用户头像
    @BindView(R.id.ugc_user_avatar)
    public ImageView mUserAvatar;

    //        用户名字
    @BindView(R.id.ugc_news_user)
    public TextView mUserName;

    //        用户签名
    @BindView(R.id.ugc_news_signature)
    public TextView mUserSign;

    //        用户认证状态
    @BindView(R.id.ugc_user_auth_status)
    public ImageView mUserAuthState;
    //        用户心情图标
    @BindView(R.id.ugc_user_mood)
    public ImageView mUserMood;
    //        用户发布日期
    @BindView(R.id.ugc_pub_date)
    public TextView mUserPubDate;
    /**
     * 隐藏用户右侧状态
     */
    @BindView(R.id.user_state_layout)
    LinearLayout mUserStateLayout;

    @BindView(R.id.ugc_feeds_current_txt)
    public QMUISpanTouchFixTextView mNewsOrigin;
    @BindView(R.id.user_name_state_layout)
    public LinearLayout mUserStateInfoLayout;


    private FeedsDetailActivity mActivity;
    private FeedCommentsAdapter mAdapter;
    /**
     * 二级评论-隐藏回复和转发
     */
    @BindView(R.id.cmt_reply)
    TextView mReplyComment;

    /**
     * 二级评论-隐藏回复和转发
     */
    @BindView(R.id.cmt_transmit)
    TextView mTransmitComment;

    /**
     * 二级评论布局
     */
    @BindView(R.id.secondary_cmt_layout)
    public View mSecondaryCmtLayout;

    /**
     * 评论发布日期
     */
    @BindView(R.id.cmt_pub_date)
    public TextView mCmtPubDate;

    @BindView(R.id.cmt_like)
    public TextView mCmtLikeCount;


    private int highlightTextNormalColor;
    private int highlightTextPressedColor;
    private int highlightBgNormalColor;
    private int highlightBgPressedColor;
    /**
     * 二级评论内容
     */
    @BindView(R.id.sub_comment_content)
    public TextView mSecondaryCmtContent;

    private Context mContext;

    public FeedsCommentViewHolder(@NonNull View itemView, FeedsDetailActivity context, FeedCommentsAdapter adapter) {
        super(itemView);
        this.mActivity = context;
        mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);
        mAdapter = adapter;
        relayoutView();
        initColor();
    }

    private void initColor() {
        highlightTextNormalColor = ContextCompat.getColor(mContext, R.color.color_text_highlight);
        highlightTextPressedColor = ContextCompat.getColor(mContext, R.color.color_text_highlight);
        highlightBgNormalColor = ContextCompat.getColor(mContext, R.color.transmit_content_color);
        highlightBgPressedColor = QMUIResHelper.getAttrColor(mContext, R.attr.qmui_config_color_gray_6);
    }


    @OnClick({R.id.cmt_like})
    void onCmtLike(View v) {
        LikedPresenter presenter = new LikedPresenter(mActivity);
//        if (!mCommentDao.isAlreadyCommentPraise()) {
        String userId = "";
        String cmtId = "";
        //一级评论
        userId = mAdapter.getCommentData(getAdapterPosition()).getCommentUser().userId + "";
        cmtId = mAdapter.getCommentData(getAdapterPosition()).getCommentId() + "";
        presenter.feedCommentLike(mAdapter.getFeeds().getFeedId() + "", cmtId
                , userId);

        mCmtLikeCount.setText(Integer.parseInt(mCmtLikeCount.getText().toString()) + 1 + "");
        mAdapter.getCommentData(getAdapterPosition()).setAlreadyCommentPraise(true);
//        } else {

//        }
    }

    /**
     * 点击跳转到个人主页
     */
    @OnClick(R.id.feed_user_layout)
    protected void onAvatarClick(View v) {
        Intent i = new Intent(mActivity, PersonHomeActivity.class);
        long userId = mAdapter.getCommentData(getAdapterPosition()).getCommentUser().userId;

        i.putExtra("user_id", userId);
        mActivity.startActivity(i);
    }

    @OnClick({R.id.cmt_reply, R.id.cmt_transmit})
    void onReplyClick(View v) {
        if (v.getId() == R.id.cmt_reply) {
            InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(mActivity, R.style.dialog_center);
            inputTextMsgDialog.setHint("写评论...");
            inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                @Override
                public void onTextSend(String msg) {
                    //点击发送按钮后，回调此方法，msg为输入的值
                    mActivity.mDetailPresenter.pubFeedComment(mAdapter.getFeeds().getFeedId() + "", msg, mAdapter.getCommentData(getAdapterPosition()).getCommentId() + "");
                }
            });
            inputTextMsgDialog.show();
        } else if (v.getId() == R.id.cmt_transmit) {
            InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(mActivity, R.style.dialog_center);
            inputTextMsgDialog.setHint("写分享...");
            inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                @Override
                public void onTextSend(String msg) {
                    //点击发送按钮后，回调此方法，msg为输入的值
                    mActivity.mDetailPresenter.pubFeedComment(mAdapter.getFeeds().getFeedId() + "", msg, mAdapter.getCommentData(getAdapterPosition()).getCommentId() + "");
                }
            });
            inputTextMsgDialog.show();
        }

    }

    /**
     * 隐藏用户头像右侧状态布局
     * 隐藏二级评论中回复和转发布局
     */
    private void hideUnuseLayout() {
        mUserStateLayout.setVisibility(View.GONE);
    }

    private void relayoutView() {
        hideUnuseLayout();
        RelativeLayout.LayoutParams vgLayoutPara = new RelativeLayout.LayoutParams(mUserStateInfoLayout.getLayoutParams());
        vgLayoutPara.setMargins(Device.dip2px(40), 0, 0, Device.dip2px(5));
        mUserStateInfoLayout.setLayoutParams(vgLayoutPara);
    }

    public SpannableString generateSp(String text) {
        String highlightStart = "@";
        String highlightEnd = "：";
        SpannableString sp = new SpannableString(text);
        int start = 0, end;
        int index;
        while ((index = text.indexOf(highlightStart, start)) > -1) {
            end = text.indexOf(highlightEnd, start);
            sp.setSpan(new QMUITouchableSpan(highlightTextNormalColor, highlightTextPressedColor,
                    highlightBgNormalColor, highlightBgPressedColor) {
                @Override
                public void onSpanClick(View widget) {
                    Intent i = new Intent(widget.getContext(), PersonHomeActivity.class);
                    long userId = mAdapter.getCommentData(getAdapterPosition()).getCommentUser().userId;

                    i.putExtra("user_id", userId + "");
                    widget.getContext().startActivity(i);
                }
            }, index, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            start = end;
        }
        return sp;
    }

}
