package com.ctb_open_car.bean.community.response.map;

import androidx.annotation.Nullable;

import com.ctb_open_car.bean.community.ResourceFileDto;
import com.ctb_open_car.bean.community.response.user.UserDto;

import java.io.Serializable;
import java.util.List;

public class RoadDto implements Serializable {

    private int rcId;        //路况ID
    private int rcType;        //路况事件类型  1：交警路检、2：违停贴条、3：免费停车、4：交警拍照、5：交通事故、6：严重拥堵
    private long publishTime;    //	发布时间戳
    private double longitude;    //	路况经度
    private double latitude;    //路况纬度
    private UserDto user;
    private List<ResourceFileDto> roadFiles;


    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public List<ResourceFileDto> getRoadFiles() {
        return roadFiles;
    }

    public void setRoadFiles(List<ResourceFileDto> roadFiles) {
        this.roadFiles = roadFiles;
    }

    public int getRcId() {
        return rcId;
    }

    public void setRcId(int rcId) {
        this.rcId = rcId;
    }

    public int getRcType() {
        return rcType;
    }

    public void setRcType(int rcType) {
        this.rcType = rcType;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
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


    @Override
    public int hashCode() {
        return rcId;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return rcId == ((RoadDto) obj).rcId;
    }
}
