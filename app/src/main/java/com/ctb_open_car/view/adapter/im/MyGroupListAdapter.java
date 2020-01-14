package com.ctb_open_car.view.adapter.im;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.community.response.group.GroupDto;
import com.ctb_open_car.utils.Device;
import com.ctb_open_car.utils.PreferenceUtils;
import com.ctb_open_car.view.activity.im.ChatGroupActivity;
import com.ctb_open_car.view.activity.im.MyGroupMoreActivity;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMLocationMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.chat.adapter.message.EMAMessageBody;
import com.hyphenate.easeui.widget.EaseConversationList;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyGroupListAdapter extends RecyclerView.Adapter {


    private SoftReference<AppCompatActivity> mActivity;
    private List<GroupDto> mGroupList;

    public MyGroupListAdapter(AppCompatActivity appCompatActivity) {
        mActivity = new SoftReference<>(appCompatActivity);
    }

    public void setData(List<GroupDto> groupList) {
        this.mGroupList = groupList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mActivity.get()).inflate(R.layout.im_chat_group_item, parent, false);
        return new MyGroupListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GroupDto groupDto = getGroupItem(position);
        MyGroupListViewHolder viewHolder = (MyGroupListViewHolder) holder;
        viewHolder.mGroupName.setText(groupDto.getGroupName());
        Glide.with(mActivity.get()).load(groupDto.getGroupIcon())
                .transform(new RoundedCorners(Device.dip2px(40)))
                .into(viewHolder.mGroupThumb);

        viewHolder.mLastMsg.setText(getLastMsg(groupDto));
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(groupDto.getGroupId(), EMConversation.EMConversationType.GroupChat);
        if (null == conversation) {
            viewHolder.mMsgCount.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.mMsgCount.setText(conversation.getUnreadMsgCount() + "");
        }
        if (pickAtMeMsg(conversation)) {
            viewHolder.mRemindText.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mRemindText.setVisibility(View.GONE);
        }
    }

    public boolean pickAtMeMsg(EMConversation conversation) {
        List<EMMessage> messages = conversation.getAllMessages();
        String currentUser = PreferenceUtils.getString(mActivity.get(), "nickName");
        Iterator<EMMessage> iMsg = messages.iterator();
        while (iMsg.hasNext()) {
            EMMessage msg = iMsg.next();
            if (EMMessage.Type.TXT == msg.getType()) {
                EMTextMessageBody textMessageBody = (EMTextMessageBody) msg.getBody();
                if (textMessageBody.getMessage().contains("@" + currentUser)
                        && msg.isUnread()) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getLastMsg(GroupDto groupDto) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(groupDto.getGroupId(), EMConversation.EMConversationType.GroupChat);
        if (null == conversation) {
            return "";
        }
        EMMessage lastMessage = conversation.getLastMessage();
        if (null == lastMessage) {
            return "";
        }
        EMMessage.Type msgType = lastMessage.getType();
        EMMessageBody messageBody;
        if (msgType == EMMessage.Type.TXT) {
            EMTextMessageBody textMessageBody = (EMTextMessageBody) lastMessage.getBody();
            return textMessageBody.getMessage();
        } else if (msgType == EMMessage.Type.IMAGE) {
//            EMImageMessageBody imageMessageBody = (EMImageMessageBody) lastMessage.getBody();
            return "[图片消息]";
        } else if (msgType == EMMessage.Type.VOICE) {
//            EMVoiceMessageBody voiceMessageBody = (EMVoiceMessageBody) lastMessage.getBody();
            return "[语音消息]";

        } else if (msgType == EMMessage.Type.FILE) {
//            EMFileMessageBody fileMessageBody = (EMFileMessageBody) lastMessage.getBody();
            return "[文件消息]";

        } else if (msgType == EMMessage.Type.LOCATION) {
//            EMLocationMessageBody locationMessageBody = (EMLocationMessageBody) lastMessage.getBody();
            return "[位置消息]";
        } else
            return "";
    }

    public GroupDto getGroupItem(int position) {
        return mGroupList.get(position);
    }

    @Override
    public int getItemCount() {
        if (null == mGroupList) {
            return Collections.EMPTY_LIST.size();
        }
        if (mActivity.get() instanceof MyGroupMoreActivity) {
            return mGroupList.size();
        } else if (mGroupList.size() >= 4) {
            return 4;
        } else {
            return mGroupList.size();
        }
    }

    public class MyGroupListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.im_group_avatar)
        ImageView mGroupThumb;
        @BindView(R.id.im_group_name)
        TextView mGroupName;

        @BindView(R.id.im_reminder_tag)
        TextView mRemindText;

        @BindView(R.id.im_group_last_msg)
        TextView mLastMsg;

        @BindView(R.id.im_msg_count)
        TextView mMsgCount;

        public MyGroupListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.im_my_group_panel)
        void toChatGroup(View v) {
            int position = getAdapterPosition();
            GroupDto groupDto = getGroupItem(position);
            Intent i = new Intent(mActivity.get(), ChatGroupActivity.class);
            i.putExtra("group_id", groupDto.getGroupId());
            mActivity.get().startActivity(i);
        }
    }
}
