package com.ctb_open_car.bean.vehicletools;

import java.io.Serializable;
import java.util.List;

/**** 车主工具 ****/
public class VehicleToolsBean implements Serializable {
    private List<CarInfoBean> plateList;
    private CarLimitNumberBean limitNumber;
    private OilPriceBean oilPrice;

    public void setPlateList(List<CarInfoBean> plateList) {
        this.plateList = plateList;
    }

    public List<CarInfoBean> getPlateList() {
        return plateList;
    }

    public void setLimitNumber(CarLimitNumberBean limitNumber) {
        this.limitNumber = limitNumber;
    }

    public CarLimitNumberBean getLimitNumber() {
        return limitNumber;
    }

    public void setOilPrice(OilPriceBean oilPrice) {
        this.oilPrice = oilPrice;
    }

    public OilPriceBean getOilPrice() {
        return oilPrice;
    }

    public class CarInfoBean implements Serializable {
        private String plate;
        private int punishAmountCount;
        private int pointCount;
        private int violateCount;
        private String violateDzyH5Url;
        private List<CarViolationBean> violateList;

        public void setPlate(String plate) {
            this.plate = plate;
        }

        public String getPlate() {
            return plate;
        }

        public void setPointCount(int pointCount) {
            this.pointCount = pointCount;
        }

        public int getPointCount() {
            return pointCount;
        }

        public void setPunishAmountCount(int punishAmountCount) {
            this.punishAmountCount = punishAmountCount;
        }

        public int getPunishAmountCount() {
            return punishAmountCount;
        }

        public void setViolateCount(int violateCount) {
            this.violateCount = violateCount;
        }

        public int getViolateCount() {
            return violateCount;
        }

        public void setViolateList(List<CarViolationBean> violateList) {
            this.violateList = violateList;
        }

        public List<CarViolationBean> getViolateList() {
            return violateList;
        }

        public void setViolateDzyH5Url(String violateDzyH5Url) {
            this.violateDzyH5Url = violateDzyH5Url;
        }

        public String getViolateDzyH5Url() {
            return violateDzyH5Url;
        }

    }


    public class CarViolationBean implements Serializable{
        private String reason;
        private String punishAmount;
        private String violateTime;
        private String location;
        private String point;

        public void setLocation(String location) {
            this.location = location;
        }

        public String getLocation() {
            return location;
        }

        public void setPoint(String point) {
            this.point = point;
        }

        public String getPoint() {
            return point;
        }

        public void setPunishAmount(String punishAmount) {
            this.punishAmount = punishAmount;
        }

        public String getPunishAmount() {
            return punishAmount;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getReason() {
            return reason;
        }

        public void setViolateTime(String violateTime) {
            this.violateTime = violateTime;
        }

        public String getViolateTime() {
            return violateTime;
        }
    }

    public class CarLimitNumberBean implements Serializable {
        private String limitNumber;
        private String remark;

        public void setLimitNumber(String limitNumber) {
            this.limitNumber = limitNumber;
        }

        public String getLimitNumber() {
            return limitNumber;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getRemark() {
            return remark;
        }
    }

    public class OilPriceBean implements Serializable {
        private String oil90;
        private String oil93;
        private String oil97;
        private String oilZero;

        public void setOil90(String oil90) {
            this.oil90 = oil90;
        }

        public String getOil90() {
            return oil90;
        }

        public void setOil93(String oil93) {
            this.oil93 = oil93;
        }

        public String getOil93() {
            return oil93;
        }

        public void setOil97(String oil97) {
            this.oil97 = oil97;
        }

        public String getOil97() {
            return oil97;
        }

        public void setOilZero(String oilZero) {
            this.oilZero = oilZero;
        }

        public String getOilZero() {
            return oilZero;
        }
    }
}
