package com.ctb_open_car.bean.community;

import com.ctb_open_car.bean.community.response.ad.BannerDto;

import java.util.List;

public class BannerData {
    private List<BannerDto> bannerList;    //广告Banner列表

    public List<BannerDto> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<BannerDto> bannerList) {
        this.bannerList = bannerList;
    }

}
