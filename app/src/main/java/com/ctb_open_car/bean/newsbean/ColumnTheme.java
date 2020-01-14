package com.ctb_open_car.bean.newsbean;

import java.io.Serializable;
import java.util.List;

/**** 资讯首页 栏目信息 ***/
public class ColumnTheme implements Serializable {
    private int themeId;
    private String themeName;
    private String themeImg;
    private String tips;
    private List<NewsInfoBean.Information> informationList;
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

    public void setTips(String tips) {
        this.tips = tips;
    }
    public String getTips() {
        return tips;
    }

    public void setInformationList(List<NewsInfoBean.Information> informationList) {
        this.informationList = informationList;
    }
    public List<NewsInfoBean.Information> getInformationList() {
        return informationList;
    }

}
