package com.ctb_open_car.bean.community.response;

import java.util.List;

public class GroupRecommendData {

    private List<GroupRecommend> carGroupList;
    private String shows;

    public List<GroupRecommend> getCarGroupList() {
        return carGroupList;
    }

    public void setCarGroupList(List<GroupRecommend> carGroupList) {
        this.carGroupList = carGroupList;
    }

    public String getShows() {
        return shows;
    }

    public void setShows(String shows) {
        this.shows = shows;
    }

}
