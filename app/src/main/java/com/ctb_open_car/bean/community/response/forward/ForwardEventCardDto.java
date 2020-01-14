package com.ctb_open_car.bean.community.response.forward;


import com.ctb_open_car.bean.community.ResourceFileDto;
import com.ctb_open_car.bean.community.response.feed.BasicFeedCardDto;
import com.ctb_open_car.bean.community.response.user.BasicUserDto;

/**
 * @author L.Y.F
 * @create 2019-09-06 18:55
 */
public class ForwardEventCardDto extends BasicFeedCardDto {

    /** 活动ID */
    private int activityId;
    /** 活动标题 */
    private String activityTitle;
    /** 活动出发地 */
    private String activityDeparturePlace;
    /** 活动所属用户信息 */
    private BasicUserDto activityUser;
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
    /** 活动封面图 */
    private ResourceFileDto activityCoverImage;
    /** 活动发布时间戳 */
    private long publishTime;

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

    public BasicUserDto getActivityUser() {
        return activityUser;
    }

    public void setActivityUser(BasicUserDto activityUser) {
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

    public ResourceFileDto getActivityCoverImage() {
        return activityCoverImage;
    }

    public void setActivityCoverImage(ResourceFileDto activityCoverImage) {
        this.activityCoverImage = activityCoverImage;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }
}
