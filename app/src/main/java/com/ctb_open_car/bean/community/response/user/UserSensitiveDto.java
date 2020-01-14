package com.ctb_open_car.bean.community.response.user;

import java.io.Serializable;

/**
 * @author L.Y.F
 * @create 2019-09-05 11:12
 */
public class UserSensitiveDto  implements Serializable {

    /** 用户手机号 */
    private String userMobile;
    /** 用户登录密码 */
    private String userLoginPwd;

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserLoginPwd() {
        return userLoginPwd;
    }

    public void setUserLoginPwd(String userLoginPwd) {
        this.userLoginPwd = userLoginPwd;
    }
}
