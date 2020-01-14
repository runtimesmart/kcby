package com.ctb_open_car.eventbus;

import com.ctb_open_car.bean.releasedynamics.ReleaseDynamics;

import java.util.List;

public class ReportRoadEvent {


    private String type;
    private ReleaseDynamics object;
    private List<ReleaseDynamics> objectList;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setObjectList(List<ReleaseDynamics> objectList) {
        this.objectList = objectList;
    }

    public List<ReleaseDynamics> getObjectList() {
        return objectList;
    }

    public void setObject(ReleaseDynamics object) {
        this.object = object;
    }

    public ReleaseDynamics getObject() {
        return object;
    }
}
