package com.ctb_open_car.view.adapter.viewholder;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ctb_open_car.R;
import com.ctb_open_car.presenter.FeedDetailPresenter;
import com.ctb_open_car.presenter.TransmitPresenter;
import com.ctb_open_car.presenter.UserFollowPresenter;
import com.ctb_open_car.wxapi.WeiXinShareManager;
import com.library.InputTextMsgDialog;
import com.qmuiteam.qmui.span.QMUITouchableSpan;
import com.qmuiteam.qmui.util.QMUIResHelper;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

import static com.ctb_open_car.wxapi.WeiXinShareManager.WECHAT_SHARE_WAY_TEXT;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder implements UserFollowPresenter.FollowCallback {

    //用户部分

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

    //转评赞
    @BindView(R.id.ugc_feed_transmit_count)
    public TextView mUserTransCount;

    @BindView(R.id.ugc_feed_cmt_count)
    public TextView mUserCmtCount;
    @BindView(R.id.ugc_feed_like_count)
    public TextView mUserLikeCount;

    @BindView(R.id.ugc_feed_read_count)
    public TextView mUserReadCount;

    @BindView(R.id.ugc_follow_status)
    public ImageView mUserFollowStatus;

    @BindView(R.id.ugc_item_delete)
    public TextView mUserDelete;


    protected TransmitPresenter mTransmitPresenter;
    private int highlightTextNormalColor;
    private int highlightTextPressedColor;
    private int highlightBgNormalColor;
    private int highlightBgPressedColor;


    private View mView;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
    }

    protected void initColor(Context context) {
        highlightTextNormalColor = ContextCompat.getColor(context, R.color.color_text_highlight);
        highlightTextPressedColor = ContextCompat.getColor(context, R.color.color_text_highlight);
        highlightBgNormalColor = ContextCompat.getColor(context, R.color.transmit_content_color);
        highlightBgPressedColor = QMUIResHelper.getAttrColor(context, R.attr.qmui_config_color_gray_6);
    }


    public void setmShareWx(Context context, String title, String content, String link, int pictureResource) {
        WeiXinShareManager manager = WeiXinShareManager.getInstance(context);
        if (manager.isWeixinAvilible(context)) {
            manager.shareByWebchat(manager.getShareContentWebpag(title, content, link, pictureResource)
                    , WeiXinShareManager.WECHAT_SHARE_TYPE_TALK);
        } else {
            Toasty.warning(context, "检测到您未安装微信，请到应用市场下载安装", Toasty.LENGTH_LONG).show();
        }
        
    }

    public void setmShareWxPengyou(Context context, String title, String content, String link, int pictureResource) {
        WeiXinShareManager manager = WeiXinShareManager.getInstance(context);

        if (manager.isWeixinAvilible(context)) {
            manager.shareByWebchat(manager.getShareContentWebpag(title, content, link, pictureResource), WeiXinShareManager.WECHAT_SHARE_TYPE_FRENDS);
        } else {
            Toasty.warning(context, "检测到您未安装微信，请到应用市场下载安装", Toasty.LENGTH_LONG).show();
        }
    }


    /**
     * 设置用户用vip状态
     */
    public void setUserVipStatus(int type) {
        if (1 == type) {
            mUserAuthState.setVisibility(View.VISIBLE);
        } else {
            mUserAuthState.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 设置用户取消关注回调
     */
    @Override
    public void actionCancelSuccess() {
        setFollowStatus(0);
    }

    /**
     * 设置用户关注回调
     */
    @Override
    public void actionFollowSuccess() {
        setFollowStatus(1);
    }

    /**
     * 设置用户关注状态
     */
    public void getFollowStatus(int type) {
        if (0 == type) {
            mUserFollowStatus.setImageDrawable(mView.getContext().getResources().getDrawable(R.drawable.follow));
        } else {
            mUserFollowStatus.setImageDrawable(mView.getContext().getResources().getDrawable(R.drawable.followed));
        }
    }

    /**
     * 设置用户关注状态
     */
    public void setFollowStatus(int type) {
        if (0 == type) {
            mUserFollowStatus.setImageDrawable(mView.getContext().getResources().getDrawable(R.drawable.follow));
            Toasty.info(mView.getContext(), "已取消关注").show();
        } else {
            mUserFollowStatus.setImageDrawable(mView.getContext().getResources().getDrawable(R.drawable.followed));
            Toasty.info(mView.getContext(), "已关注").show();
        }
    }

    /**
     * 用户头像点击
     */
    @OnClick({R.id.ugc_user_avatar, R.id.ugc_user_auth_status, R.id.user_name_state_layout})
    protected void onAvatarClick(View v) {

    }

    @OnClick(R.id.ugc_item_delete)
    protected void onDeleteItemClick(View v){

    }

    protected void showDialog(Context context, String hint, InputTextMsgDialog.OnTextSendListener listener) {
        InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(context, R.style.dialog_center);
        inputTextMsgDialog.setHint(hint);

        inputTextMsgDialog.setmOnTextSendListener(listener);
        inputTextMsgDialog.show();
    }

    protected void showDialog(Context context, InputTextMsgDialog.OnTextSendListener listener) {
        InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(context, R.style.dialog_center);
        inputTextMsgDialog.setHint("写分享...");

        inputTextMsgDialog.setmOnTextSendListener(listener);
        inputTextMsgDialog.show();
    }

    /**
     * 用户转发
     */
    @OnClick(R.id.feed_transmit_layout)
    protected void onTransmitClick(View v) {

    }

    /**
     * 用户关注点击
     */
    @OnClick(R.id.user_state_layout)
    protected void onFocusClick(View v) {

    }

}
