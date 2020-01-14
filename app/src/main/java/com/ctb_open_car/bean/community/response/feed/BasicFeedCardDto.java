package com.ctb_open_car.bean.community.response.feed;

import com.ctb_open_car.bean.community.ResourceFileDto;
import com.ctb_open_car.bean.community.response.user.BasicUserDto;

import java.io.Serializable;
import java.util.List;

/**
 * @author L.Y.F
 * @create 2019-09-06 18:50
 */
public class BasicFeedCardDto implements Serializable {

    private String forwardBizId;
    /** 转发动态Card类型  1：动态、2：活动 */
    private int forwardBizType;

    public BasicFeedCardDto() {}

    public BasicFeedCardDto(String forwardBizId, int forwardBizType) {
        this.forwardBizId = forwardBizId;
        this.forwardBizType = forwardBizType;
    }

    public String getForwardBizId() {
        return forwardBizId;
    }

    public void setForwardBizId(String forwardBizId) {
        this.forwardBizId = forwardBizId;
    }

    public int getForwardBizType() {
        return forwardBizType;
    }

    public void setForwardBizType(int forwardBizType) {
        this.forwardBizType = forwardBizType;
    }


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



    /** 动态ID */
    private long feedId;
    /** 动态内容 */
    private List<FeedContentDto> feedContents;
    /** 动态图片集合 */
    private List<ResourceFileDto> feedImageList;
    /** 动态所属用户信息 */
    private BasicUserDto feedUser;
    /** 动态经度 */
    private double longitude;
    /** 动态纬度 */
    private double latitude;
    /** 话题 */
    private FeedTopicDto feedTopic;

    public long getFeedId() {
        return feedId;
    }

    public void setFeedId(long feedId) {
        this.feedId = feedId;
    }

    public List<FeedContentDto> getFeedContents() {
        return feedContents;
    }

    public void setFeedContents(List<FeedContentDto> feedContents) {
        this.feedContents = feedContents;
    }

    public List<ResourceFileDto> getFeedImageList() {
        return feedImageList;
    }

    public void setFeedImageList(List<ResourceFileDto> feedImageList) {
        this.feedImageList = feedImageList;
    }

    public BasicUserDto getFeedUser() {
        return feedUser;
    }

    public void setFeedUser(BasicUserDto feedUser) {
        this.feedUser = feedUser;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public FeedTopicDto getFeedTopic() {
        return feedTopic;
    }

    public void setFeedTopic(FeedTopicDto feedTopic) {
        this.feedTopic = feedTopic;
    }
}
