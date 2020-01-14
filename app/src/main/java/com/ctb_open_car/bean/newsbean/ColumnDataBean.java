package com.ctb_open_car.bean.newsbean;

import java.util.List;

/************* 栏目详情页 ***********/
public class ColumnDataBean {
        private ColumnInfoBean theme;   // 栏目信息
        private List<String> groupList; // 群聊
        private ColumnFollowBean follow;    // 本栏目关注人
        private List<ColumnInfoBean.Information> informationList; // 动态 或者 热门 列表

        public void setTheme(ColumnInfoBean theme) {
            this.theme = theme;
        }

        public ColumnInfoBean getTheme() {
            return theme;
        }

        public void setGroupList(List<String> groupList) {
            this.groupList = groupList;
        }

        public List<String> getGroupList() {
            return groupList;
        }

        public void setFollow(ColumnFollowBean follow) {
            this.follow = follow;
        }

        public ColumnFollowBean getFollow() {
            return follow;
        }

        public void setInformationList(List<ColumnInfoBean.Information> informationList) {
            this.informationList = informationList;
        }

        public List<ColumnInfoBean.Information> getInformationList() {
            return informationList;
        }


}
