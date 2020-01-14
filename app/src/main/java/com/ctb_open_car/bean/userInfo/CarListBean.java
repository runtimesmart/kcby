package com.ctb_open_car.bean.userInfo;

import java.io.Serializable;
import java.util.List;

public class CarListBean implements Serializable {

    List<PlateDto> carList; //车辆列表

    public void setCarList(List<PlateDto> carList) {
        this.carList = carList;
    }

    public List<PlateDto> getCarList() {
        return carList;
    }

    public class PlateDto implements Serializable{
        String plate; //    车牌号
        String brand; //	车型

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getBrand() {
            return brand;
        }

        public void setPlate(String plate) {
            this.plate = plate;
        }

        public String getPlate() {
            return plate;
        }
    }
}
