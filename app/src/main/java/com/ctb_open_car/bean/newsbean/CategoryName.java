package com.ctb_open_car.bean.newsbean;

import java.io.Serializable;

/****  资讯首页 分类信息 ****/
public class CategoryName implements Serializable {

    private int categoryId;
    private String categoryName;
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public String getCategoryName() {
        return categoryName;
    }

}