package com.ctb_open_car.bean.roadcondition;

import java.io.Serializable;

public class RoadConditionBean implements Serializable {
    double latitude;
    double longitude; //上报类型 1快速上报2语音加照片
    int rcType; // 路况类型 1交警路检2违停贴条3免费停车4交警拍照5交通事故6严重拥
    int publishType;//上报类型 1快速上报2语音加照片
    int position;
    String positionName;
    String fileUrls;
    String address;
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setRcType(int rcType) {
        this.rcType = rcType;
    }

    public int getRcType() {
        return rcType;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPublishType(int publishType) {
        this.publishType = publishType;
    }

    public int getPublishType() {
        return publishType;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setFileUrls(String fileUrls) {
        this.fileUrls = fileUrls;
    }

    public String getFileUrls() {
        return fileUrls;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}