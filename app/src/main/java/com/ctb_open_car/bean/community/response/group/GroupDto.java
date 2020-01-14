package com.ctb_open_car.bean.community.response.group;

import java.io.Serializable;

public class GroupDto implements Serializable {

    private long id;
    private String groupId;
    private String groupName;
    private String groupIcon;
    private String cityCode;
    private String tagId;
    private String carModelId;
    private String carModelName;
    private String tagName;
    private int ifInside; //   当前登录用户是否在群理
    private int ifHost; //   是否是群主

    public int getIfHost() {
        return ifHost;
    }

    public void setIfHost(int ifHost) {
        this.ifHost = ifHost;
    }


    public int getIfInside() {
        return ifInside;
    }

    public void setIfInside(int ifInside) {
        this.ifInside = ifInside;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupIcon() {
        return groupIcon;
    }

    public void setGroupIcon(String groupIcon) {
        this.groupIcon = groupIcon;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getCarModelId() {
        return carModelId;
    }

    public void setCarModelId(String carModelId) {
        this.carModelId = carModelId;
    }

    public String getCarModelName() {
        return carModelName;
    }

    public void setCarModelName(String carModelName) {
        this.carModelName = carModelName;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }


}
