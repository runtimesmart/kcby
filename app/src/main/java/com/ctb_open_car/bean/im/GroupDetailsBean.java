package com.ctb_open_car.bean.im;

import com.ctb_open_car.bean.areacode.AreaCodeDtoBean;
import com.ctb_open_car.bean.community.ResourceFileDto;
import com.ctb_open_car.bean.community.response.user.BasicUserDto;

import java.io.Serializable;
import java.util.List;

public class GroupDetailsBean implements Serializable {
    private EmchatGroupDto groupDetail; // 群详情
    private boolean exsited; // 当前用户是否已经在群

    public void setExsited(boolean exsited) {
        this.exsited = exsited;
    }

    public boolean isExsited() {
        return exsited;
    }

    public void setGroupDetail(EmchatGroupDto groupDetail) {
        this.groupDetail = groupDetail;
    }

    public EmchatGroupDto getGroupDetail() {
        return groupDetail;
    }

    public class EmchatGroupDto implements Serializable {
        private String groupId;//群组ID
        private String groupName; // 群组名称
        private ResourceFileDto groupIcon; //群组头像
        private String groupDesc; //群组介绍
        private String groupRule; // 群组规则
        private List<TagDtoBean> tagList; //群组标签
        private int carModelId; //群车系ID -1代表此群未关联任何车系
        private String carModelName; // 群车系名称 carModelId=-1时无车系名称
        private int memberCnt; // 群成员数量
        private AreaCodeDtoBean areaCode;
        private BasicUserDto ownerId;

        public void setTagList(List<TagDtoBean> tagList) {
            this.tagList = tagList;
        }

        public List<TagDtoBean> getTagList() {
            return tagList;
        }

        public void setCarModelId(int carModelId) {
            this.carModelId = carModelId;
        }

        public int getCarModelId() {
            return carModelId;
        }

        public void setCarModelName(String carModelName) {
            this.carModelName = carModelName;
        }

        public String getCarModelName() {
            return carModelName;
        }

        public void setGroupDesc(String groupDesc) {
            this.groupDesc = groupDesc;
        }

        public String getGroupDesc() {
            return groupDesc;
        }

        public void setGroupIcon(ResourceFileDto groupIcon) {
            this.groupIcon = groupIcon;
        }

        public ResourceFileDto getGroupIcon() {
            return groupIcon;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupRule(String groupRule) {
            this.groupRule = groupRule;
        }

        public String getGroupRule() {
            return groupRule;
        }

        public void setMemberCnt(int memberCnt) {
            this.memberCnt = memberCnt;
        }

        public int getMemberCnt() {
            return memberCnt;
        }

        public void setAreaCode(AreaCodeDtoBean areaCode) {
            this.areaCode = areaCode;
        }

        public AreaCodeDtoBean getAreaCode() {
            return areaCode;
        }

        public void setOwnerId(BasicUserDto ownerId) {
            this.ownerId = ownerId;
        }

        public BasicUserDto getOwnerId() {
            return ownerId;
        }
    }
}
