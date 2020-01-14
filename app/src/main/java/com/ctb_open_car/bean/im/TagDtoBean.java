package com.ctb_open_car.bean.im;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class TagDtoBean implements Serializable {
    private int tagId;
    private String tagName;
    private String tagType;
    private boolean isSelect;

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    public String getTagType() {
        return tagType;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isSelect() {
        return isSelect;
    }

    @NonNull
    @Override
    public String toString() {
        return  "tagName = (" +tagName + ") tagType = (" + tagType + ") tagId = (" +tagId + ")";
    }
}
