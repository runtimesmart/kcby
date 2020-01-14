package com.ctb_open_car.bean.im;

import androidx.annotation.NonNull;

import com.ctb_open_car.bean.community.ResourceFileDto;

import java.io.Serializable;

public class EmchatGroupMemberDto implements Serializable {
    private long userId; //用户ID
    private String nickName;//用户昵称
    private ResourceFileDto userIcon; //用户头像
    private String emId;//用户环信账号
    private int emGroupRole; //用户群组角色  1：群主、2：成员
    private boolean preset; // 是否为平台内置用户

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setUserIcon(ResourceFileDto userIcon) {
        this.userIcon = userIcon;
    }

    public ResourceFileDto getUserIcon() {
        return userIcon;
    }

    public void setEmId(String emId) {
        this.emId = emId;
    }

    public String getEmId() {
        return emId;
    }

    public void setEmGroupRole(int emGroupRole) {
        this.emGroupRole = emGroupRole;
    }

    public int getEmGroupRole() {
        return emGroupRole;
    }

    public void setPreset(boolean preset) {
        this.preset = preset;
    }

    public boolean isPreset() {
        return preset;
    }
}
