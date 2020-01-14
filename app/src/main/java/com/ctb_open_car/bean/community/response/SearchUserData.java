package com.ctb_open_car.bean.community.response;

import com.ctb_open_car.bean.community.response.user.UserCardDto;

import java.util.List;

public class SearchUserData {
    private List<UserCardDto> userCardList;

    public List<UserCardDto> getUserCardList() {
        return userCardList;
    }

    public void setUserCardList(List<UserCardDto> userCardList) {
        this.userCardList = userCardList;
    }

}
