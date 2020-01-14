package com.ctb_open_car.bean.newsbean;

import java.io.Serializable;

public class NewsDataBean implements Serializable {
    private String msg;
    private String code;
    private NewsInfoBean data;
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }

    public void setData(NewsInfoBean data) {
        this.data = data;
    }
    public NewsInfoBean getData() {
        return data;
    }
}
