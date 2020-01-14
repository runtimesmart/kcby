package com.ctb_open_car.bean.im;

import java.io.Serializable;
import java.util.List;

public class CarStyleBean implements Serializable {
    private List<CarModelBean> detail;
    private int thirdId;
    private String name;
    private String remoteLogo;
    private String logo;


    public void setDetail(List<CarModelBean> detail) {
        this.detail = detail;
    }

    public List<CarModelBean> getDetail() {
        return detail;
    }

    public void setThirdId(int thirdId) {
        this.thirdId = thirdId;
    }

    public int getThirdId() {
        return thirdId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setRemoteLogo(String remoteLogo) {
        this.remoteLogo = remoteLogo;
    }

    public String getRemoteLogo() {
        return remoteLogo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogo() {
        return logo;
    }
}
