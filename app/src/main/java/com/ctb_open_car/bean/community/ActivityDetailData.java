package com.ctb_open_car.bean.community;

import com.ctb_open_car.bean.community.response.activity.ActivityDetailDto;

public class ActivityDetailData {

    private ActivityDetailDto activityDetail; //活动信息

    private int relationStatus;        //当前用户对此用户的关注状态  0：未关注、1：关注

    public ActivityDetailDto getActivityDetail() {
        return activityDetail;
    }

    public void setActivityDetail(ActivityDetailDto activityDetail) {
        this.activityDetail = activityDetail;
    }

    public int getRelationStatus() {
        return relationStatus;
    }

    public void setRelationStatus(int relationStatus) {
        this.relationStatus = relationStatus;
    }

}
