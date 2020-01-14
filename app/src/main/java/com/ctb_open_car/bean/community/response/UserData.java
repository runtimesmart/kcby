package com.ctb_open_car.bean.community.response;

import com.ctb_open_car.bean.community.response.user.UserHomeDto;


public class UserData {

    private UserHomeDto userHome;     //用户基础信息


    private boolean existBackList;        //目标用户是否在当前用户黑名单中

    public UserHomeDto getUserHome() {
        return userHome;
    }

    public void setUserHome(UserHomeDto userHome) {
        this.userHome = userHome;
    }

    public boolean isExistBackList() {
        return existBackList;
    }

    public void setExistBackList(boolean existBackList) {
        this.existBackList = existBackList;
    }


}
