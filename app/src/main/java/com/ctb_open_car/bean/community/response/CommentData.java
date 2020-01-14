package com.ctb_open_car.bean.community.response;

import com.ctb_open_car.bean.community.response.comment.CommentDto;

import java.util.List;

public class CommentData {
    private LackOfCntPageDto pageData;

    public LackOfCntPageDto getPageData() {
        return pageData;
    }

    public void setPageData(LackOfCntPageDto pageData) {
        this.pageData = pageData;
    }

    public class LackOfCntPageDto {


        private List<CommentDto> data; //动态集合

        private boolean haveNext; //是否有下一页

        public List<CommentDto> getData() {
            return data;
        }

        public void setData(List<CommentDto> data) {
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
