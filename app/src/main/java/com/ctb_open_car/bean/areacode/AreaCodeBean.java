package com.ctb_open_car.bean.areacode;

import com.ctb_open_car.bean.im.CarStyleBean;

import java.io.Serializable;
import java.util.List;

public class AreaCodeBean implements Serializable {
    private List<AreaCodeDtoBean> areaCodeList;

    public void setAreaCodeList(List<AreaCodeDtoBean> areaCodeList) {
        this.areaCodeList = areaCodeList;
    }

    public List<AreaCodeDtoBean> getAreaCodeList() {
        return areaCodeList;
    }
}
