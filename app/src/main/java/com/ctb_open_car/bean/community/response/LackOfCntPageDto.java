package com.ctb_open_car.bean.community.response;


import java.io.Serializable;
import java.util.List;

public class LackOfCntPageDto<T> implements Serializable {


    private List<T> data; //动态集合

    private boolean haveNext; //是否有下一页

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public boolean isHaveNext() {
        return haveNext;
    }

    public void setHaveNext(boolean haveNext) {
        this.haveNext = haveNext;
    }
}
