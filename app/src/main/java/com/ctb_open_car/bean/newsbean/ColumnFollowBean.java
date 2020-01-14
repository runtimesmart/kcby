package com.ctb_open_car.bean.newsbean;

import java.io.Serializable;
import java.util.List;

/****  资讯栏目详情页 关注人模块 ****/
public class ColumnFollowBean implements Serializable {
    private String followsDesc;
    private String followFriendDesc;
    private List<FollowerBean> followers;

    public void setFollowsDesc(String followsDesc) {
        this.followsDesc = followsDesc;
    }

    public String getFollowsDesc() {
        return followsDesc;
    }

    public void setFollowFriendDesc(String followFriendDesc) {
        this.followFriendDesc = followFriendDesc;
    }

    public String getFollowFriendDesc() {
        return followFriendDesc;
    }

    public void setFollowers(List<FollowerBean> followers) {
        this.followers = followers;
    }

    public List<FollowerBean> getFollowers() {
        return followers;
    }

    /****  资讯栏目详情页 关注人模块 中的关注人信息 ****/
  public class FollowerBean implements Serializable {
        private String img;
        private String icon;
        private int followStatus;
        private String feed;
        private String nickname="";
        private long userId;
        public void setImg(String img) {
            this.img = img;
        }

        public String getImg() {
            return img;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getIcon() {
            return icon;
        }

        public void setFollowStatus(int followStatus) {
            this.followStatus = followStatus;
        }

        public int getFollowStatus() {
            return followStatus;
        }

        public void setFeed(String feed) {
            this.feed = feed;
        }

        public String getFeed() {
            return feed;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getNickname() {
            return nickname;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public long getUserId() {
            return userId;
        }
    }
}
