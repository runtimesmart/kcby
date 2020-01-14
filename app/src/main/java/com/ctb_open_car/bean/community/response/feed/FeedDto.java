package com.ctb_open_car.bean.community.response.feed;

import com.ctb_open_car.bean.community.ResourceFileDto;
import com.ctb_open_car.bean.community.response.user.UserCardDto;
import com.ctb_open_car.bean.community.response.user.UserDto;

import java.io.Serializable;
import java.util.List;

/**
 * @author L.Y.F
 * @create 2019-09-05 13:39
 */
public class FeedDto implements Serializable {

    /** 动态ID */
    private long feedId;
    /** 动态内容 */
    private List<FeedContentDto> feedContents;
    /** 动态图片集合 */
    private List<ResourceFileDto> feedImageList;
    /** 动态所属用户信息 */
    private UserCardDto feedUser;

    private int feedType;		//动态类型 1：原生动态、2：转发动态
    private int feedUgcType; 	//转发动态Card类型 1：动态Card、2：活动Card

    /** 转发动态Card内容 */
    private BasicFeedCardDto forwardFeedCard;
    /** 动态经度 */
    private double longitude;
    /** 动态纬度 */
    private double latitude;
    /** 动态显示的地理位置 */
    private String feedPlaceName;
    /** 动态状态 0：不通过、1：通过、2：删除 */
    private int feedStatus;
    /** 话题 */
    private FeedTopicDto feedTopic;
    /** 发布时间戳 */
    private Long publishTime;
    /** 动态相关数值 */
    private FeedStatDto feedStat;
    /** 是否点赞过 */
    private boolean praised;

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

    public UserCardDto getFeedUser() {
        return feedUser;
    }

    public void setFeedUser(UserCardDto feedUser) {
        this.feedUser = feedUser;
    }

    public int getFeedType() {
        return feedType;
    }

    public void setFeedType(int feedType) {
        this.feedType = feedType;
    }

    public int getFeedUgcType() {
        return feedUgcType;
    }

    public void setFeedUgcType(int feedUgcType) {
        this.feedUgcType = feedUgcType;
    }

    public BasicFeedCardDto getForwardFeedCard() {
        return forwardFeedCard;
    }

    public void setForwardFeedCard(BasicFeedCardDto forwardFeedCard) {
        this.forwardFeedCard = forwardFeedCard;
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

    public String getFeedPlaceName() {
        return feedPlaceName;
    }

    public void setFeedPlaceName(String feedPlaceName) {
        this.feedPlaceName = feedPlaceName;
    }

    public int getFeedStatus() {
        return feedStatus;
    }

    public void setFeedStatus(int feedStatus) {
        this.feedStatus = feedStatus;
    }

    public FeedTopicDto getFeedTopic() {
        return feedTopic;
    }

    public void setFeedTopic(FeedTopicDto feedTopic) {
        this.feedTopic = feedTopic;
    }

    public Long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Long publishTime) {
        this.publishTime = publishTime;
    }

    public FeedStatDto getFeedStat() {
        return feedStat;
    }

    public void setFeedStat(FeedStatDto feedStat) {
        this.feedStat = feedStat;
    }

    public boolean isPraised() {
        return praised;
    }

    public void setPraised(boolean praised) {
        this.praised = praised;
    }
}
