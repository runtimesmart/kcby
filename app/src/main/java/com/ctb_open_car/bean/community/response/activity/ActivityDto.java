package com.ctb_open_car.bean.community.response.activity;

import com.ctb_open_car.bean.community.ResourceFileDto;
import com.ctb_open_car.bean.community.response.user.UserCardDto;
import com.ctb_open_car.bean.community.response.user.UserDto;

import java.io.Serializable;
import java.util.List;

/**
 * @author L.Y.F
 * @create 2019-09-06 15:32
 */
public class ActivityDto implements Serializable {

    /** 活动ID */
    private int activityId;
    /** 活动标题 */
    private String activityTitle;
    /** 活动出发地 */
    private String activityDeparturePlace;
    /** 活动邀请二维码 */
    private ResourceFileDto activityInviteIcon;
    /** 活动图片集合 */
    private List<ResourceFileDto> activityImageList;
    /** 活动所属用户信息 */
    private UserCardDto activityUser;
    /** 活动开始时间 */
    private long activityBegintime;
    /** 活动报名截止时间 */
    private long activityEnrollEndtime;
    /** 活动结束时间 */
    private long activityEndtime;
    /** 活动上限人数 */
    private int activityEnrollLimit;
    /** 活动介绍 */
    private String activityDesc;
    /** 活动发布时间戳 */
    private long publishTime;
    /** 活动相关数值信息 */
    private ActivityStatDto activityStat;

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getActivityDeparturePlace() {
        return activityDeparturePlace;
    }

    public void setActivityDeparturePlace(String activityDeparturePlace) {
        this.activityDeparturePlace = activityDeparturePlace;
    }

    public ResourceFileDto getActivityInviteIcon() {
        return activityInviteIcon;
    }

    public void setActivityInviteIcon(ResourceFileDto activityInviteIcon) {
        this.activityInviteIcon = activityInviteIcon;
    }

    public List<ResourceFileDto> getActivityImageList() {
        return activityImageList;
    }

    public void setActivityImageList(List<ResourceFileDto> activityImageList) {
        this.activityImageList = activityImageList;
    }

    public UserCardDto getActivityUser() {
        return activityUser;
    }

    public void setActivityUser(UserCardDto activityUser) {
        this.activityUser = activityUser;
    }

    public long getActivityBegintime() {
        return activityBegintime;
    }

    public void setActivityBegintime(long activityBegintime) {
        this.activityBegintime = activityBegintime;
    }

    public long getActivityEnrollEndtime() {
        return activityEnrollEndtime;
    }

    public void setActivityEnrollEndtime(long activityEnrollEndtime) {
        this.activityEnrollEndtime = activityEnrollEndtime;
    }

    public long getActivityEndtime() {
        return activityEndtime;
    }

    public void setActivityEndtime(long activityEndtime) {
        this.activityEndtime = activityEndtime;
    }

    public int getActivityEnrollLimit() {
        return activityEnrollLimit;
    }

    public void setActivityEnrollLimit(int activityEnrollLimit) {
        this.activityEnrollLimit = activityEnrollLimit;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public ActivityStatDto getActivityStat() {
        return activityStat;
    }

    public void setActivityStat(ActivityStatDto activityStat) {
        this.activityStat = activityStat;
    }
}
