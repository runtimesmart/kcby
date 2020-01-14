package com.ctb_open_car.bean.community.response.user;


import com.ctb_open_car.bean.community.ResourceFileDto;

import java.io.Serializable;

/**
 * @author L.Y.F
 * @create 2019-09-05 10:42
 */
public class BasicUserDto implements Serializable {

    /** 用户ID */
    public long userId;
    /** 用户昵称 */
    public String nickName;
    /** 用户头像 */
    public ResourceFileDto userIcon;
    /** 用户性别   -1：未知、1：男、2：女 */
    public int userSex;
    /** 用户认证状态  0：默认未认证、1：认证大V */
    public int userAuthStatus;
    /** 用户心情图标 */
    public String userMoodIcon;

    /** 用户状态 0：正常 */
    public int userStatus;


    public BasicUserDto() {}

    public BasicUserDto(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public ResourceFileDto getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(ResourceFileDto userIcon) {
        this.userIcon = userIcon;
    }

    public int getUserSex() {
        return userSex;
    }

    public void setUserSex(int userSex) {
        this.userSex = userSex;
    }

    public int getUserAuthStatus() {
        return userAuthStatus;
    }

    public void setUserAuthStatus(int userAuthStatus) {
        this.userAuthStatus = userAuthStatus;
    }

    public String getUserMoodIcon() {
        return userMoodIcon;
    }

    public void setUserMoodIcon(String userMoodIcon) {
        this.userMoodIcon = userMoodIcon;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }
}
