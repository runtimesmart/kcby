package com.ctb_open_car.ui.im;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.ctb_open_car.R;
import com.ctb_open_car.bean.im.EmchatGroupMemberDto;
import com.ctb_open_car.presenter.ImGroupMembersPresenter;
import com.ctb_open_car.ui.BaseView;
import com.ctb_open_car.utils.PreferenceUtils;
import com.ctb_open_car.view.activity.im.ContextMenuActivity;
import com.ctb_open_car.view.activity.im.ImGroupInfoActivity;
import com.ctb_open_car.view.activity.im.ImGroupMemberListActivity;
import com.ctb_open_car.view.fragment.dialog.IMUserInfoDialog;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseDingMessageHelper;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class ChatGroupView implements BaseView {


    @BindView(R.id.chat_layout)
    FrameLayout mChatLayout;
    private static final int REQUEST_CODE_CONTEXT_MENU = 14;

    private EaseChatFragment mEaseChatFragment;
    private SoftReference<AppCompatActivity> mActivity;
    private String mGroupId;
    private static HashMap<String, EmchatGroupMemberDto> mHashGroupMembers;
    ImGroupMembersPresenter mGroupMemberresenter;

    @Inject
    public ChatGroupView(AppCompatActivity appCompatActivity) {
        mActivity = new SoftReference<>(appCompatActivity);
        ButterKnife.bind(this, mActivity.get());
        mGroupMemberresenter = new ImGroupMembersPresenter(appCompatActivity);
    }

    public void setGroupId(String groupId) {
        this.mGroupId = groupId;
        mGroupMemberresenter.requestRecommendList(mGroupId, new ImGroupMembersPresenter.UpdateListener() {
            @Override
            public void update(List<EmchatGroupMemberDto> groupMemberList) {
                mHashGroupMembers = new HashMap<>();
                Iterator<EmchatGroupMemberDto> iMembers = groupMemberList.iterator();
                while (iMembers.hasNext()) {
                    EmchatGroupMemberDto memberDto = iMembers.next();
                    mHashGroupMembers.put(memberDto.getEmId(), memberDto);
                }
                EaseUI.getInstance().setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
                    @Override
                    public EaseUser getUser(String username) {
                        EaseUser easeUser = new EaseUser(username);
                        if (null == mHashGroupMembers.get(username)) {
                            return easeUser;
                        }
                        if (EMClient.getInstance().groupManager().getGroup(mGroupId).getOwner().equals(username)) {
                            easeUser.setNickname("群主 " + mHashGroupMembers.get(username).getNickName());
                        } else {
                            easeUser.setNickname(mHashGroupMembers.get(username).getNickName());
                        }
                        easeUser.setAvatar(mHashGroupMembers.get(username).getUserIcon().getResourceUrl());
                        return easeUser;
                    }
                });
                initView();

            }

            @Override
            public void logout() {
                mActivity.get().finish();
            }
        });
    }

    private void initView() {
        mEaseChatFragment = new EaseChatFragment();
        mEaseChatFragment.setArguments(getChatFragmentBundle());
        FragmentTransaction transaction = mActivity.get().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.chat_layout, mEaseChatFragment).commit();

        /**设置会话列表中点击事件*/
        mEaseChatFragment.setChatFragmentHelper(chatFragmentHelper);
        mEaseChatFragment.setRightLayoutClickListener(new EaseChatFragment.RightLayoutClickListener() {
            @Override
            public void OnRightClickListener(View v) {
                Intent intent = new Intent(mActivity.get(), ImGroupInfoActivity.class);
                intent.putExtra("group_id", mGroupId);
                mActivity.get().startActivity(intent);
            }
        });

        mEaseChatFragment.setTypeListener(new EaseChatFragment.TypingListener() {
            @Override
            public void onTyping(CharSequence s, int start, int before, int count) {
                if (count == 1 && "@".equals(String.valueOf(s.charAt(start)))) {
                    mActivity.get().startActivityForResult(new Intent(mActivity.get(), ImGroupMemberListActivity.class).
                            putExtra("group_id", mGroupId).putExtra("is_select", true), 1);
                }
            }
        });
    }

    public void clip() {
        mEaseChatFragment.clipboard.setPrimaryClip(ClipData.newPlainText(null,
                ((EMTextMessageBody) mEaseChatFragment.contextMenuMessage.getBody()).getMessage()));
    }

    public void setSelectName(String txt) {
        EditText editText = mEaseChatFragment.inputMenu.getPrimaryMenu().getEditText();
        editText.setText(txt);
        editText.setSelection(txt.length());
    }

    public void deleteMsg() {
        mEaseChatFragment.conversation.removeMessage(mEaseChatFragment.contextMenuMessage.getMsgId());
        mEaseChatFragment.messageList.refresh();
        // To delete the ding-type message native stored acked users.
        EaseDingMessageHelper.get().delete(mEaseChatFragment.contextMenuMessage);
    }

    private EaseChatFragment.EaseChatFragmentHelper chatFragmentHelper = new EaseChatFragment.EaseChatFragmentHelper() {
        @Override
        public void onSetMessageAttributes(EMMessage message) {

        }

        @Override
        public void onEnterToChatDetails() {

        }

        @Override
        public void onAvatarClick(String username) {
            if (null == mHashGroupMembers.get(username)) {
                Toasty.info(mActivity.get(), "此用户不存在").show();
                return;
            }
            IMUserInfoDialog imUserInfoDialog = new IMUserInfoDialog(mActivity.get());
            Bundle params = new Bundle();
            params.putString("user_id", mHashGroupMembers.get(username).getUserId() + "");
            params.putString("group_id", mGroupId);
            params.putString("em_user_id", username);
            params.putString("nick_name", mHashGroupMembers.get(username).getNickName());
            imUserInfoDialog.setArguments(params);
            imUserInfoDialog.show(mActivity.get().getSupportFragmentManager(), "user_info");
        }

        @Override
        public void onAvatarLongClick(String username) {
            if (null == mHashGroupMembers.get(username)) {
                Toasty.info(mActivity.get(), "此用户不存在").show();
                return;
            }
            if (mHashGroupMembers.get(username).getUserId()
                    == PreferenceUtils.getLong(mActivity.get(), "userId")) {
                return;
            }
            String userNick = mHashGroupMembers.get(username).getNickName();
            EditText editText = mEaseChatFragment.inputMenu.getPrimaryMenu().getEditText();
            editText.setText(String.format("@%s", userNick));
            editText.setSelection(userNick.length() + 1);
        }

        @Override
        public boolean onMessageBubbleClick(EMMessage message) {
            return false;
        }

        @Override
        public void onMessageBubbleLongClick(EMMessage message) {
            mActivity.get().startActivityForResult((new Intent(mActivity.get(), ContextMenuActivity.class)).putExtra("message", message)
                            .putExtra("ischatroom", false),
                    REQUEST_CODE_CONTEXT_MENU);
        }

        @Override
        public boolean onExtendMenuItemClick(int itemId, View view) {
            return false;
        }

        @Override
        public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
            return null;
        }
    };

    private Bundle getChatFragmentBundle() {
        Bundle args = new Bundle();
        int type = mActivity.get().getIntent().getIntExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, type);

        args.putString(EaseConstant.EXTRA_USER_ID, mGroupId);
        return args;
    }

    private void getGroupList() {
        List<EMGroup> groups = EMClient.getInstance().groupManager().getAllGroups();
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    public void drawTitleBar() {

    }

    @Override
    public void unbind() {

    }

}
