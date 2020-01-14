package com.ctb_open_car.bean.im;

import java.io.Serializable;
import java.util.List;

public class CarLibaryBean implements Serializable {
    private List<CarStyleBean> carBrandList;

    public void setCarBrandList(List<CarStyleBean> carBrandList) {
        this.carBrandList = carBrandList;
    }

    public List<CarStyleBean> getCarBrandList() {
        return carBrandList;
    }
}
