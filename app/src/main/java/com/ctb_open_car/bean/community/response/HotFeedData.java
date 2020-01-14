package com.ctb_open_car.bean.community.response;


import com.ctb_open_car.bean.community.response.feed.FeedDto;
import com.ctb_open_car.bean.community.response.user.UserCardDto;

import java.io.Serializable;
import java.util.List;

public class HotFeedData implements Serializable {

    private LackOfCntPageDto pageData;

    private List<UserCardDto>   recommendUser;

    public LackOfCntPageDto getPageData() {
        return pageData;
    }

    public void setPageData(LackOfCntPageDto pageData) {
        this.pageData = pageData;
    }

    public List<UserCardDto> getRecommendUser() {
        return recommendUser;
    }

    public void setRecommendUser(List<UserCardDto> recommendUser) {
        this.recommendUser = recommendUser;
    }
    public class LackOfCntPageDto {


        private List<FeedDto> data; //动态集合

        private boolean haveNext; //是否有下一页

        public List<FeedDto> getData() {
            return data;
        }

        public void setData(List<FeedDto> data) {
            this.data = data;
        }

        public boolean isHaveNext() {
            return haveNext;
        }

        public void setHaveNext(boolean haveNext) {
            this.haveNext = haveNext;
        }
    }



}
