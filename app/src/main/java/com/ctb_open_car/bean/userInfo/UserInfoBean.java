package com.ctb_open_car.bean.userInfo;

import java.io.Serializable;
import java.util.List;

public class UserInfoBean implements Serializable {
    UserInfoDto userInfo;//		用户基本信息
    String customerService;//	客服电话
    int integralCount;	//积分余额
    int cashCount;//	int	现金余额
    List<PlateDto> carList; //车辆列表

    public void setUserInfo(UserInfoDto userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfoDto getUserInfo() {
        return userInfo;
    }

    public void setCustomerService(String customerService) {
        this.customerService = customerService;
    }

    public String getCustomerService() {
        return customerService;
    }

    public void setCarList(List<PlateDto> carList) {
        this.carList = carList;
    }

    public List<PlateDto> getCarList() {
        return carList;
    }

    public void setCashCount(int cashCount) {
        this.cashCount = cashCount;
    }

    public int getCashCount() {
        return cashCount;
    }

    public void setIntegralCount(int integralCount) {
        this.integralCount = integralCount;
    }

    public int getIntegralCount() {
        return integralCount;
    }

    public class UserInfoDto implements Serializable{
        String nickname;  //	String	昵称
        String userIcon;  //	String	用户头像
        String userId;    //	String	用户id
        int sex;
        String birthday;

        public void setUserIcon(String userIcon) {
            this.userIcon = userIcon;
        }

        public String getUserIcon() {
            return userIcon;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserId() {
            return userId;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getNickname() {
            return nickname;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getSex() {
            return sex;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getBirthday() {
            return birthday;
        }
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
