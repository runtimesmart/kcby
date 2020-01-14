package com.ctb_open_car.bean.im;

import java.io.Serializable;
import java.util.List;

public class CarModelBean implements Serializable {
    private int id;
    private String showName;
    private int brandThirdId;
    private String factoryName;
    private int brandDetailId = 1;
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getShowName() {
        return showName;
    }

    public void setBrandThirdId(int brandThirdId) {
        this.brandThirdId = brandThirdId;
    }

    public int getBrandThirdId() {
        return brandThirdId;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setBrandDetailId(int brandDetailId) {
        this.brandDetailId = brandDetailId;
    }

    public int getBrandDetailId() {
        return brandDetailId;
    }
}
