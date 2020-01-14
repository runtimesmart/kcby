package com.ctb_open_car.bean.community.response.forward;


import com.ctb_open_car.bean.community.ResourceFileDto;
import com.ctb_open_car.bean.community.response.feed.BasicFeedCardDto;
import com.ctb_open_car.bean.community.response.feed.FeedContentDto;
import com.ctb_open_car.bean.community.response.feed.FeedTopicDto;
import com.ctb_open_car.bean.community.response.user.BasicUserDto;

import java.util.List;

/**
 * @author L.Y.F
 * @create 2019-09-06 18:54
 */
public class ForwardFeedCardDto extends BasicFeedCardDto {

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
