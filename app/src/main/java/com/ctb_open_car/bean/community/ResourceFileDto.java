package com.ctb_open_car.bean.community;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * @author L.Y.F
 * @create 2019-09-06 14:43
 */
public class ResourceFileDto implements Serializable {

    /**
     * 资源文件ID
     */
    private long resourceId;
    /**
     * 资源文件网络路径
     */
    private String resourceUrl;


    private int resourceType;

    public ResourceFileDto() {
    }

    public ResourceFileDto(long resourceId) {
        this.resourceId = resourceId;
    }

    public long getResourceId() {
        return resourceId;
    }

    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }

    public int getResourceType() {
        return resourceType;
    }

    public void setResourceType(int resourceType) {
        this.resourceType = resourceType;
    }
    public String getResourceUrl() {
        if (!TextUtils.isEmpty(this.resourceUrl)) {
            return resourceUrl;
        } else {
            return resourceUrl;
        }
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }
}
