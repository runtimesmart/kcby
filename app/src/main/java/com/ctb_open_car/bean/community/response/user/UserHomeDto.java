package com.ctb_open_car.bean.community.response.user;

/**
 * 用户主页实体类
 *
 * @author L.Y.F
 * @create 2019-09-05 11:38
 */
public class UserHomeDto extends UserDto {
    /**
     * 当前用户对此用户的关注状态  0：未关注、1：关注、2：粉丝、3：互相关注
     */
    private int relationStatus;

    public int getRelationStatus() {
        return relationStatus;
    }

    public void setRelationStatus(int relationStatus) {
        this.relationStatus = relationStatus;
    }


}
