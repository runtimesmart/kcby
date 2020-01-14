package com.ctb_open_car.bean.community.response.user;

/**
 * @author L.Y.F
 * @create 2019-09-07 19:47
 */
public class UserDto extends BasicUserDto {

    /** 用户认证Title */
    public String userAuthTitle;
    /** 用户签名 */
    private String userSign;
    /** 用户身份： 0：外部真实用户、1：虚拟机器人 */
    private int userVirtual;
    /** 用户敏感信息 */
    private UserSensitiveDto userSensitive;
    /** 用户相关数值信息 */
    private UserStatDto userStat;

    public UserDto() {}

    public UserDto(long userId) {
        super.userId = userId;
    }

    public String getUserAuthTitle() {
        return userAuthTitle;
    }

    public void setUserAuthTitle(String userAuthTitle) {
        this.userAuthTitle = userAuthTitle;
    }

    public String getUserSign() {
        return userSign;
    }

    public void setUserSign(String userSign) {
        this.userSign = userSign;
    }

    public int getUserVirtual() {
        return userVirtual;
    }

    public void setUserVirtual(int userVirtual) {
        this.userVirtual = userVirtual;
    }

    public UserSensitiveDto getUserSensitive() {
        return userSensitive;
    }

    public void setUserSensitive(UserSensitiveDto userSensitive) {
        this.userSensitive = userSensitive;
    }

    public UserStatDto getUserStat() {
        return userStat;
    }

    public void setUserStat(UserStatDto userStat) {
        this.userStat = userStat;
    }

    public void setUser(UserDto user) {
        if (null != user) {
            this.setNickName(user.getNickName());
            this.setUserIcon(user.getUserIcon());
            this.setUserSign(user.getUserSign());
            this.setUserStatus(user.getUserStatus());
            this.setUserAuthStatus(user.getUserAuthStatus());
            this.setUserAuthTitle(user.getUserAuthTitle());
            this.setUserSex(user.getUserSex());
            this.setUserMoodIcon(user.getUserMoodIcon());
            this.setUserVirtual(user.getUserVirtual());
            this.setUserSensitive(user.getUserSensitive());
            this.setUserStat(user.getUserStat());
        }
    }

}
