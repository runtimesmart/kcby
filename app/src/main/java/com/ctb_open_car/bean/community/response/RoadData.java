package com.ctb_open_car.bean.community.response;

import com.ctb_open_car.bean.community.response.map.RoadDto;

import java.util.List;

public class RoadData {

    private List<RoadDto> roadConditionList;

    private int refreshFrequency;

    public int getRefreshFrequency() {
        return refreshFrequency;
    }

    public void setRefreshFrequency(int refreshFrequency) {
        this.refreshFrequency = refreshFrequency;
    }
    public List<RoadDto> getRoadConditionList() {
        return roadConditionList;
    }

    public void setRoadConditionList(List<RoadDto> roadConditionList) {
        this.roadConditionList = roadConditionList;
    }
}
