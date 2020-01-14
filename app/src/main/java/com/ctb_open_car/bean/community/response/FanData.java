package com.ctb_open_car.bean.community.response;

import com.ctb_open_car.bean.community.response.user.UserCardDto;

public class FanData {


    private LackOfCntPageDto<UserCardDto> pageData;//分页达人集合


    public LackOfCntPageDto<UserCardDto> getPageData() {
        return pageData;
    }

    public void setPageData(LackOfCntPageDto<UserCardDto> pageData) {
        this.pageData = pageData;
    }


}
