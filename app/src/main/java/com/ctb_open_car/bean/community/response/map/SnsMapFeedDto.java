package com.ctb_open_car.bean.community.response.map;

import com.ctb_open_car.bean.community.response.user.UserDto;

public class SnsMapFeedDto {

    private long feedId;        //动态ID
    private String feedContent;//动态内容	该字段不一定有，动态有纯图片的形态
    private String feedImage;//动态图片，最多只有一张	该字段不一定有，动态有纯文本的形态
    private Double longitude;//动态经度	经、纬度一定同步存在或不存在，并且大部分情况下一定存在，只有当附近动态列表和首页路况推荐时可能会有缺失经纬度的情况。
    private UserDto feedUser;// 动态所属用户信息
    private Double latitude;// 动态纬度	经、纬度一定同步存在或不存在，并且大部分情况下一定存在，只有当附近动态列表和首页路况推荐时可能会有缺失经纬度的情况。
    private Long publishTime;        //动态发布时间戳	该字段大部分情况下一定存在，只有当附近动态列表和首页路况推荐时可能会有缺失经纬度的情况。


    public long getFeedId() {
        return feedId;
    }

    public void setFeedId(long feedId) {
        this.feedId = feedId;
    }

    public String getFeedContent() {
        return feedContent;
    }

    public void setFeedContent(String feedContent) {
        this.feedContent = feedContent;
    }

    public String getFeedImage() {
        return feedImage;
    }

    public void setFeedImage(String feedImage) {
        this.feedImage = feedImage;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public UserDto getFeedUser() {
        return feedUser;
    }

    public void setFeedUser(UserDto feedUser) {
        this.feedUser = feedUser;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Long publishTime) {
        this.publishTime = publishTime;
    }

}
