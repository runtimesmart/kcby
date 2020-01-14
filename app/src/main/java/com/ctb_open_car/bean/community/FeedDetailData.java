package com.ctb_open_car.bean.community;

import com.ctb_open_car.bean.community.response.feed.FeedDto;

public class FeedDetailData {

    private FeedDto feed; //动态信息

    private int relationStatus;		//当前用户对此用户的关注状态  0：未关注、1：关注

    public FeedDto getFeed() {
        return feed;
    }

    public void setFeed(FeedDto feed) {
        this.feed = feed;
    }

    public int getRelationStatus() {
        return relationStatus;
    }

    public void setRelationStatus(int relationStatus) {
        this.relationStatus = relationStatus;
    }

}
