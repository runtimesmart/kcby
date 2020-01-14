package com.ctb_open_car.bean.im;

import java.io.Serializable;
import java.util.List;

public class TagListBean implements Serializable {
    private List<TagDtoBean> tagList;

    public List<TagDtoBean> getTagList() {
        return tagList;
    }

    public void setTagList(List<TagDtoBean> tagList) {
        this.tagList = tagList;
    }
}
