package com.ctb_open_car.bean.areacode;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class AreaCodeDtoBean implements Serializable {
    private String areaCode;
    private String areaName;
    private List<AreaCodeDtoBean> childAreaCode;

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setChildAreaCode(List<AreaCodeDtoBean> childAreaCode) {
        this.childAreaCode = childAreaCode;
    }

    public List<AreaCodeDtoBean> getChildAreaCode() {
        return childAreaCode;
    }
}
