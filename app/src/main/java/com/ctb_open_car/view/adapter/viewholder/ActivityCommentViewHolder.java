package com.ctb_open_car.view.adapter.viewholder;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctb_open_car.R;
import com.ctb_open_car.bean.community.response.activity.ActivityDetailDto;
import com.ctb_open_car.bean.community.response.comment.CommentDto;
import com.ctb_open_car.presenter.LikedPresenter;
import com.ctb_open_car.utils.Device;
import com.ctb_open_car.view.activity.community.ActivityDetailActivity;
import com.ctb_open_car.view.activity.community.FeedsDetailActivity;
import com.ctb_open_car.view.adapter.community.ActivityCommentsAdapter;
import com.library.InputTextMsgDialog;
import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * @author Yl
 */
public class ActivityCommentViewHolder extends RecyclerView.ViewHolder {

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

    @BindView(R.id.cmt_reply)
    public TextView mReplyComment;


    private ActivityDetailActivity mActivity;
    /**
     * 二级评论-隐藏回复和转发
     */

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
     * 二级评论内容
     */
    @BindView(R.id.sub_comment_content)
    public TextView mSecondaryCmtContent;

    @BindView(R.id.cmt_like)
    public TextView mCmtLikeCount;

    /**
     * 评论发布日期
     */
    @BindView(R.id.cmt_pub_date)
    public TextView mCmtPubDate;

    private ActivityCommentsAdapter mAdapter;

    public ActivityCommentViewHolder(@NonNull View itemView, ActivityDetailActivity context, ActivityCommentsAdapter activityCommentsAdapter) {
        super(itemView);
        this.mActivity = context;
        this.mAdapter = activityCommentsAdapter;
        ButterKnife.bind(this, itemView);

        relayoutView();
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
        presenter.activityCommentLike(mAdapter.getActivitys().getActivityId() + "", cmtId
                , userId);


        mCmtLikeCount.setText(Integer.parseInt(mCmtLikeCount.getText().toString()) + 1 + "");
        mAdapter.getCommentData(getAdapterPosition()).setAlreadyCommentPraise(true);
//        } else {

//        }
    }


    @OnClick({R.id.cmt_reply, R.id.cmt_transmit})
    void onReplyClick(View v) {
        CommentDto commentDto = mAdapter.getCommentData(getAdapterPosition());
        if (v.getId() == R.id.cmt_reply) {
            InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(mActivity, R.style.dialog_center);
            inputTextMsgDialog.setHint("写评论...");
            inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                @Override
                public void onTextSend(String msg) {
                    //点击发送按钮后，回调此方法，msg为输入的值
                    mActivity.mDetailPresenter.pubFeedComment(mAdapter.getActivitys().getActivityId() + "", msg,
                            commentDto.getCommentId() + "");
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
                    mActivity.mDetailPresenter.pubFeedComment(mAdapter.getActivitys().getActivityId() + "",
                            msg, commentDto.getCommentId() + "");
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

}
