package com.ctb_open_car.bean.newsbean;

import java.io.Serializable;
import java.util.List;

public class NewsBean implements Serializable {
    private String columnName;
    private String columnIconUrl;
    private String columnDes;
    private List<BloggerInfo> bloggerInfoList;

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnIconUrl(String columnIconUrl) {
        this.columnIconUrl = columnIconUrl;
    }

    public String getColumnIconUrl() {
        return columnIconUrl;
    }

    public void setColumnDes(String columnDes) {
        this.columnDes = columnDes;
    }

    public String getColumnDes() {
        return columnDes;
    }

    public void setBloggerInfoList(List<BloggerInfo> bloggerInfoList) {
        this.bloggerInfoList = bloggerInfoList;
    }

    public List<BloggerInfo> getBloggerInfoList() {
        return bloggerInfoList;
    }

    public static class BloggerInfo{
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
