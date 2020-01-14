package com.ctb_open_car.bean.newsbean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/***  资讯首页 数据信息（分类，栏目，）***/
public class NewsInfoBean implements Serializable {
    private List<Category> categoryList;
    private List<CategoryName> categoryNameList;
    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryNameList(List<CategoryName> categoryNameList) {
        this.categoryNameList = categoryNameList;
    }
    public List<CategoryName> getCategoryNameList() {
        return categoryNameList;
    }

    /***  资讯首页 栏目管理类***/
    public static class Category implements Parcelable {
        private ColumnTheme theme;

        protected Category(Parcel in) {
            if(null==in){
                return;
            }
            theme = (ColumnTheme) in.readSerializable();
        }

        public static final Parcelable.Creator<Category> CREATOR = new Creator<Category>() {
            @Override
            public Category createFromParcel(Parcel in) {
                if(null==in){
                    new Category(Parcel.obtain());
                }
                Category teacher = new Category(in);
                teacher.setColumnTheme((ColumnTheme) in.readSerializable());
                return new Category(in);
            }

            @Override
            public Category[] newArray(int size) {
                return new Category[size];
            }
        };

        public void setColumnTheme(ColumnTheme columnTheme) {
            this.theme = columnTheme;
        }
        public ColumnTheme getColumnTheme() {
            return theme;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeSerializable(theme);
        }
    }

    /***  资讯首页 栏目下列表item 信息***/
    public class Information implements Serializable{
        private int informationId;
        private String title;
        private String coverUrl;
        private long publishTime;
        private int likes;
        private int comments;
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

    }
}
