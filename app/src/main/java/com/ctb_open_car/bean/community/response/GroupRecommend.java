package com.ctb_open_car.bean.community.response;

import com.ctb_open_car.bean.community.response.group.GroupDto;

import java.io.Serializable;
import java.util.List;

public class GroupRecommend implements Serializable {

    private String tagId;
    private List<GroupDto> groupList;
    private String tagName;

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public List<GroupDto> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<GroupDto> groupList) {
        this.groupList = groupList;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }


}
