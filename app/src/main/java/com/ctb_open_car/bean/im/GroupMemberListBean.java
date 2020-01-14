package com.ctb_open_car.bean.im;

import java.io.Serializable;
import java.util.List;

public class GroupMemberListBean implements Serializable {
    private List<EmchatGroupMemberDto> groupMemberList;

    public void setGroupMemberList(List<EmchatGroupMemberDto> groupMemberList) {
        this.groupMemberList = groupMemberList;
    }

    public List<EmchatGroupMemberDto> getGroupMemberList() {
        return groupMemberList;
    }
}
