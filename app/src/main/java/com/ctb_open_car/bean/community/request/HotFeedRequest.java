package com.ctb_open_car.bean.community.request;

public class HotFeedRequest {

    private long targetUserId;        //目标用户ID	是
    private int currentPage;      //  当前请求页数，默认从1开始

    public long getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(long targetUserId) {
        this.targetUserId = targetUserId;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
