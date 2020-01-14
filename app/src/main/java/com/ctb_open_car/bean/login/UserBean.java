package com.ctb_open_car.bean.login;

import com.ctb_open_car.bean.community.ResourceFileDto;

public class UserBean {
    String userToken;
    User user;


    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public class User {
        private long userId;   //	用户ID
        private String nickName;//	用户昵称
        private ResourceFileDto userIcon;//		用户头像
        private int userSex;   // 用户性别   -1：未知、1：男、2：女
        private int userAuthStatus;  //用户认证状态  0：默认未认证、1：认证大V
        private int userStatus;
        private int userBirthday;
        private int userVirtual;
        private EmchatBindDto emchatBind;

        public EmchatBindDto getEmchatBind() {
            return emchatBind;
        }

        public void setEmchatBind(EmchatBindDto emchatBind) {
            this.emchatBind = emchatBind;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserIcon(ResourceFileDto userIcon) {
            this.userIcon = userIcon;
        }

        public ResourceFileDto getUserIcon() {
            return userIcon;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getNickName() {
            return nickName;
        }

        public void setUserAuthStatus(int userAuthStatus) {
            this.userAuthStatus = userAuthStatus;
        }

        public int getUserAuthStatus() {
            return userAuthStatus;
        }

        public void setUserSex(int userSex) {
            this.userSex = userSex;
        }

        public int getUserSex() {
            return userSex;
        }

        public void setUserBirthday(int userBirthday) {
            this.userBirthday = userBirthday;
        }

        public int getUserBirthday() {
            return userBirthday;
        }

        public void setUserStatus(int userStatus) {
            this.userStatus = userStatus;
        }

        public int getUserStatus() {
            return userStatus;
        }

        public void setUserVirtual(int userVirtual) {
            this.userVirtual = userVirtual;
        }

        public int getUserVirtual() {
            return userVirtual;
        }
    }

}
