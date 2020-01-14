package com.ctb_open_car.bean.community.response;

import com.ctb_open_car.bean.community.response.group.GroupDto;

import java.util.List;

public class GroupData {

    private List<GroupDto> carGroupList;
    private int shows;

    public List<GroupDto> getCarGroupList() {
        return carGroupList;
    }

    public void setCarGroupList(List<GroupDto> carGroupList) {
        this.carGroupList = carGroupList;
    }

    public int getShows() {
        return shows;
    }

    public void setShows(int shows) {
        this.shows = shows;
    }

}
