package com.ctb_open_car.bean.community.response.ad;

import java.io.Serializable;

/**
 * @author L.Y.F
 * @create 2019-09-06 11:20
 */
public class BannerDto implements Serializable {

    /** 广告ID */
    private int adId;
    /** 广告Title */
    private String adTitle;
    /** 广告图片地址 */
    private String adIcon;
    /** 广告类型  1：站外跳转、2：站内活动、3：站内动态、4：站内用户主页、5：站内话题 */
    private int adType;
    /** adType==1时，打开这个外链地址 */
    private String outsiteUrl;
    /** adType!=1时，说明是站内跳转，该字段存储的为adType对应的业务目标ID */
    private String abBizId;

    public int getAdId() {
        return adId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public String getAdIcon() {
        return adIcon;
    }

    public void setAdIcon(String adIcon) {
        this.adIcon = adIcon;
    }

    public int getAdType() {
        return adType;
    }

    public void setAdType(int adType) {
        this.adType = adType;
    }

    public String getOutsiteUrl() {
        return outsiteUrl;
    }

    public void setOutsiteUrl(String outsiteUrl) {
        this.outsiteUrl = outsiteUrl;
    }

    public String getAbBizId() {
        return abBizId;
    }

    public void setAbBizId(String abBizId) {
        this.abBizId = abBizId;
    }
}
