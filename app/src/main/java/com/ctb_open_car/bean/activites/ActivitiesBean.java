package com.ctb_open_car.bean.activites;

import java.io.Serializable;

public class ActivitiesBean implements Serializable {
    private String activityTitle; //	活动名称
    private String activityDeparturePlace;//活动出发地点
    private String activityInviteIcon;//活动邀请二维码图片相对路径
    private String activityImages;//活动的多张图片相对路径，多个图片间用逗号分隔	是
    private String activityBegintime;//活动开始时间，格式：yyyy-MM-dd
    private String activityEnrollEndtime;//活动开始时间，格式：yyyy-MM-dd	是
    private String activityEndtime;//活动开始时间，格式：yyyy-MM-dd	是
    private int activityEnrollLimit;//活动上限人数	是
    private String activityDesc;//活动介绍	是
    double longitude;//动态经度	是
    double latitude;//动态维度

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityDeparturePlace(String activityDeparturePlace) {
        this.activityDeparturePlace = activityDeparturePlace;
    }

    public String getActivityDeparturePlace() {
        return activityDeparturePlace;
    }

    public void setActivityInviteIcon(String activityInviteIcon) {
        this.activityInviteIcon = activityInviteIcon;
    }

    public String getActivityInviteIcon() {
        return activityInviteIcon;
    }

    public void setActivityBegintime(String activityBegintime) {
        this.activityBegintime = activityBegintime;
    }

    public String getActivityBegintime() {
        return activityBegintime;
    }

    public void setActivityEndtime(String activityEndtime) {
        this.activityEndtime = activityEndtime;
    }

    public String getActivityEndtime() {
        return activityEndtime;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityEnrollEndtime(String activityEnrollEndtime) {
        this.activityEnrollEndtime = activityEnrollEndtime;
    }

    public String getActivityEnrollEndtime() {
        return activityEnrollEndtime;
    }

    public void setActivityEnrollLimit(int activityEnrollLimit) {
        this.activityEnrollLimit = activityEnrollLimit;
    }

    public int getActivityEnrollLimit() {
        return activityEnrollLimit;
    }

    public void setActivityImages(String activityImages) {
        this.activityImages = activityImages;
    }

    public String getActivityImages() {
        return activityImages;
    }

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

}
