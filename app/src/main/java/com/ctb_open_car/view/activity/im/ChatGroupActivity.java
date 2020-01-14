package com.ctb_open_car.view.activity.im;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseActivity;
import com.ctb_open_car.eventbus.MessageEvent;
import com.ctb_open_car.ui.im.ChatGroupView;
import com.ctb_open_car.view.dialog.CustomDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;


public class ChatGroupActivity extends BaseActivity {


    @Inject
    ChatGroupView mChatGroupView;
    ClipboardManager clipboard;
    private static final int REQUEST_CODE_SELECT_VIDEO = 11;
    private static final int REQUEST_CODE_SELECT_FILE = 12;
    private static final int REQUEST_CODE_GROUP_DETAIL = 13;
    private static final int REQUEST_CODE_CONTEXT_MENU = 14;
    private static final int REQUEST_CODE_SELECT_AT_USER = 15;
    private static final int CODE_MEMBER_SELECT = 20;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.im_chat_group_layout);
        EventBus.getDefault().register(this);

        String groupId = getIntent().getStringExtra("group_id");
        getActivityComponent().inject(this);
        mChatGroupView.setGroupId(groupId);

    }

    @Override
    public Object getTag() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getEventBusMsg(MessageEvent event) {
        if (event.getType() != null && "finish_activity".equals(event.getType())) {
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
            switch (resultCode) {
                case ContextMenuActivity.RESULT_CODE_COPY: // copy
                    mChatGroupView.clip();
                    break;
                case ContextMenuActivity.RESULT_CODE_DELETE: // delete
                    mChatGroupView.deleteMsg();
                    break;
                case ContextMenuActivity.RESULT_CODE_COMPLAIN: // delete
                    Toasty.info(ChatGroupActivity.this, "已投诉").show();
                    break;

//                case ContextMenuActivity.RESULT_CODE_FORWARD: // forward
//                    Intent intent = new Intent(getActivity(), ForwardMessageActivity.class);
//                    intent.putExtra("forward_msg_id", contextMenuMessage.getMsgId());
//                    startActivity(intent);

                default:
                    break;
            }
        } else if (CODE_MEMBER_SELECT == resultCode) {
            if (null != data) {
                String memberId = data.getStringExtra("member_id");
                String memberName = data.getStringExtra("member_name");

                mChatGroupView.setSelectName("@"+memberName);
            }
        }
    }
}
