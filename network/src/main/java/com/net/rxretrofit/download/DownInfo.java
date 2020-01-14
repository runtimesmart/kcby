package com.net.rxretrofit.download;

import com.net.rxretrofit.listener.HttpDownOnNextListener;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.Objects;

/**
 * 数据库中关于下载数据的基础类
 */

@Entity
public class DownInfo {
    @Id(autoincrement = true)
    private Long id;
    /*用户标识*/
    private String userId;
    /*盒子标识*/
    private String deviceDid;
    /*存储位置*/
    private String savePath;
    /*文件总长度*/
    private long countLength;
    /*下载长度*/
    private long readLength;
    /*下载唯一的HttpService*/
    @Transient
    private HttpDownService service;
    /*回调监听*/
    @Transient
    private HttpDownOnNextListener listener;
    /*超时设置*/
    private int connectonTime = 6;
    /*state状态数据库保存*/
    private int stateInte;
    /*url*/
    private String url;
    /*完整url*/
    @Transient
    private String realUrl;
    /*分享标志*/
    private boolean isShare;

    /* 文件名*/
    private String fileName;

    /* 文件类型*/
    private String fileType;

    /* 图片、视频的缩略图*/
    private String icon;

    /* 文件下载到sd卡的时间*/
    private String createTime;
    /*文件出错的原因*/
    private String errorMessage;

//可能缺少的字段
//    private String type;


//    private Integer subFile1;
//    private Integer subFile2;
//    private Integer subFile3;
//    private Integer subFile4;
//    private Integer subFile5;


    @Generated(hash = 47572021)
    public DownInfo(Long id, String userId, String deviceDid, String savePath, long countLength, long readLength, int connectonTime, int stateInte, String url, boolean isShare, String fileName,
            String fileType, String icon, String createTime, String errorMessage) {
        this.id = id;
        this.userId = userId;
        this.deviceDid = deviceDid;
        this.savePath = savePath;
        this.countLength = countLength;
        this.readLength = readLength;
        this.connectonTime = connectonTime;
        this.stateInte = stateInte;
        this.url = url;
        this.isShare = isShare;
        this.fileName = fileName;
        this.fileType = fileType;
        this.icon = icon;
        this.createTime = createTime;
        this.errorMessage = errorMessage;
    }

    @Generated(hash = 928324469)
    public DownInfo() {
    }

    public DownInfo(DownInfo list) {
        this.id = list.id;
        this.userId = list.userId;
        this.deviceDid = list.deviceDid;
        this.savePath = list.savePath;
        this.countLength = list.countLength;
        this.readLength = list.readLength;
        this.connectonTime = list.connectonTime;
        this.stateInte = list.stateInte;
        this.url = list.url;
        this.isShare = list.isShare;
        this.fileName = list.fileName;
        this.fileType = list.fileType;
        this.icon = list.icon;
        this.createTime = list.createTime;


    }

    public DownState getState() {
        switch (getStateInte()) {
            case 0:
                return DownState.START;
            case 1:
                return DownState.DOWN;
            case 2:
                return DownState.PAUSE;
            case 3:
                return DownState.STOP;
            case 4:
                return DownState.ERROR;
            case 5:
                return DownState.FINISH;
            default:
                return DownState.WAIT;
        }
    }

    public String getDeviceDid() {
        return deviceDid;
    }

    public void setDeviceDid(String deviceDid) {
        this.deviceDid = deviceDid;
    }

    public void setState(DownState state) {
        setStateInte(state.getState());
    }

    public int getStateInte() {
        return stateInte;
    }

    public void setStateInte(int stateInte) {
        this.stateInte = stateInte;
    }

    public HttpDownOnNextListener getListener() {
        return listener;
    }

    public void setListener(HttpDownOnNextListener listener) {
        this.listener = listener;
    }

    public HttpDownService getService() {
        return service;
    }

    public void setService(HttpDownService service) {
        this.service = service;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRealUrl() {
        return realUrl;
    }

    public void setRealUrl(String realUrl) {
        this.realUrl = realUrl;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }


    public long getCountLength() {
        return countLength;
    }

    public void setCountLength(long countLength) {
        this.countLength = countLength;
    }


    public long getReadLength() {
        return readLength;
    }

    public void setReadLength(long readLength) {
        this.readLength = readLength;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getConnectonTime() {
        return this.connectonTime;
    }

    public void setConnectonTime(int connectonTime) {
        this.connectonTime = connectonTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isShare() {
        return isShare;
    }

    public void setShare(boolean share) {
        isShare = share;
    }

    @Override
    public String toString() {
        return "DownInfo{" +
                "id=" + id +
                ", savePath='" + savePath + '\'' +
                ", countLength=" + countLength +
                ", readLength=" + readLength +
                ", stateInte=" + stateInte +
                ", url='" + url + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DownInfo other = (DownInfo) obj;
        return (Objects.equals(userId, other.getUserId()) && Objects.equals(deviceDid, other.getDeviceDid()) && Objects.equals(url, other.getUrl()) && Objects.equals(countLength, other.getCountLength()));
    }

    public boolean getIsShare() {
        return this.isShare;
    }

    public void setIsShare(boolean isShare) {
        this.isShare = isShare;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
