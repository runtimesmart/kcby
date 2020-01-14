package com.net.rxretrofit.interfaces;


public interface LangConfigInterface {
    String getVersionMsg();
    String hudImei="";
    String getHudImei();
    String authorization="";
    String getAuthorization();
    void setAuthorization(String authorization);
    void setHudImei(String imei);
}
