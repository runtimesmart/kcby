package com.rxretrofitlibrary.http;

import android.text.TextUtils;

public class HeaderBean {

    private int versionCode;    //	客户端版本号	是
    private String versionName;     //客户端版本名称
    private int appType;     //客户端设备类型 1：IOS、2：Android	是
    private String network;        //客户端网络类型，例如：2G、3G、5G、WIFI等	尽可能获取
    private String imei;        //手机设备标识码	尽可能获取
    private String imsi;    //手机卡标识码	尽可能获取
    private String channel;        //渠道号，IOS端固定写"IOS"即可，Android端在未接入第三方渠道时默认为"chetuobang"，接入第三方时该值必须为第三方的渠道Code	是
    private String macAdress;        //手机网卡物理地址	尽可能获取
    private String osName;        //手机操作系统名称	尽可能获取
    private String deviceModel;    //	设备名称，具体的机型信息	尽可能获取
    private Double longitude;        //用户经度	尽可能获取
    private Double latitude;        //用户纬度	尽可能获取
    private Long userId;        //用户ID	除游客接口以外必填
    private String userToken;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getMacAdress() {
        return macAdress;
    }

    public void setMacAdress(String macAdress) {
        this.macAdress = macAdress;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserToken() {
        if (TextUtils.isEmpty(userToken) || "null".equals(userToken)) {
            userToken = "";
        }
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
