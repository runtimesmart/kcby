package com.ctb_open_car.bean.newsbean;

import java.io.Serializable;
import java.util.List;

/**** 栏目详情页-栏目信息 ***/
public class ColumnInfoBean implements Serializable {
    private int themeId;
    private String themeName;
    private String themeImg;
    private String themeDesc;
    private int followStatus;

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeImg(String themeImg) {
        this.themeImg = themeImg;
    }

    public String getThemeImg() {
        return themeImg;
    }

    public void setThemeDesc(String themeDesc) {
        this.themeDesc = themeDesc;
    }

    public String getThemeDesc() {
        return themeDesc;
    }

    public void setFollowStatus(int followStatus) {
        this.followStatus = followStatus;
    }

    public int getFollowStatus() {
        return followStatus;
    }

    /****** 栏目详情页-栏目中的资讯内容信息****/
    public static class Information implements Serializable {
        private int informationId;
        private String title;
        private String coverUrl;
        private long publishTime;
        private int likes;
        private int comments;
        private int type = 0;

        private List<ColumnFollowBean.FollowerBean> bloggerInfoList;

        public void setBloggerInfoList(List<ColumnFollowBean.FollowerBean> bloggerInfoList) {
            this.bloggerInfoList = bloggerInfoList;
        }

        public List<ColumnFollowBean.FollowerBean> getBloggerInfoList() {
            return bloggerInfoList;
        }


        public void setInformationId(int informationId) {
            this.informationId = informationId;
        }

        public int getInformationId() {
            return informationId;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setCoverUrl(String coverUrl) {
            this.coverUrl = coverUrl;
        }

        public String getCoverUrl() {
            return coverUrl;
        }

        public void setPublishTime(long publishTime) {
            this.publishTime = publishTime;
        }

        public long getPublishTime() {
            return publishTime;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public int getLikes() {
            return likes;
        }

        public void setComments(int comments) {
            this.comments = comments;
        }

        public int getComments() {
            return comments;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    /****** 栏目详情页-栏目中的 达人列表item信息****/
    public static class BloggerInfo implements Serializable{
        private String bloggerName;
        private String bloggerIconUrl;
        private String bloggerDes;
        private String bloggerDesIconUrl;


        public void setBloggerName(String bloggerName) {
            this.bloggerName = bloggerName;
        }

        public String getBloggerName() {
            return bloggerName;
        }

        public void setBloggerIconUrl(String bloggerIconUrl) {
            this.bloggerIconUrl = bloggerIconUrl;
        }

        public String getBloggerIconUrl() {
            return bloggerIconUrl;
        }

        public void setBloggerDes(String bloggerDes) {
            this.bloggerDes = bloggerDes;
        }

        public String getBloggerDes() {
            return bloggerDes;
        }

        public void setBloggerDesIconUrl(String bloggerDesIconUrl) {
            this.bloggerDesIconUrl = bloggerDesIconUrl;
        }

        public String getBloggerDesIconUrl() {
            return bloggerDesIconUrl;
        }
    }
}
