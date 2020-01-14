package com.ctb_open_car.bean.community.response;

import com.ctb_open_car.bean.community.response.map.SnsMapFeedDto;

import java.util.List;

public class SNSMapData {
    private List<SnsMapFeedDto> feedList;

    public List<SnsMapFeedDto> getFeedList() {
        return feedList;
    }

    public void setFeedList(List<SnsMapFeedDto> feedList) {
        this.feedList = feedList;
    }

}
