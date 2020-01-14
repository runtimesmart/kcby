package com.ctb_open_car.bean.community.response;

import com.ctb_open_car.bean.community.response.feed.FeedDto;

import java.io.Serializable;
import java.util.List;

public class NearbyData implements Serializable {

    private FocusFeedData.LackOfCntPageDto pageData;

    public FocusFeedData.LackOfCntPageDto getPageData() {
        return pageData;
    }

    public void setPageData(FocusFeedData.LackOfCntPageDto pageData) {
        this.pageData = pageData;
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
