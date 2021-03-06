package com.ctb_open_car.bean.community.response;

import com.ctb_open_car.bean.community.response.activity.ActivityCardDto;

import java.io.Serializable;
import java.util.List;

public class EventData implements Serializable {
    private LackOfCntPageDto pageData;

    public LackOfCntPageDto getPageData() {
        return pageData;
    }

    public void setPageData(LackOfCntPageDto pageData) {
        this.pageData = pageData;
    }

    public class LackOfCntPageDto {


        private List<ActivityCardDto> data; //动态集合

        private boolean haveNext; //是否有下一页

        public List<ActivityCardDto> getData() {
            return data;
        }

        public void setData(List<ActivityCardDto> data) {
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
