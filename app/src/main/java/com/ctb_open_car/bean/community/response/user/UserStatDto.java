package com.ctb_open_car.bean.community.response.user;

import java.io.Serializable;

/**
 * @author L.Y.F
 * @create 2019-09-06 10:36
 */
public class UserStatDto implements Serializable {

    /** 用户动态数量 */
    private long feedCnt;
    /** 用户活动数量 */
    private long activityCnt;
    /** 用户车友群数量 */
    private long carGroupCnt;
    /** 此用户的关注数量 */
    private long attentionCnt;
    /** 此用户的粉丝数量 */
    private long fansCnt;

    public long getFeedCnt() {
        return feedCnt;
    }

    public void setFeedCnt(long feedCnt) {
        this.feedCnt = feedCnt;
    }

    public long getActivityCnt() {
        return activityCnt;
    }

    public void setActivityCnt(long activityCnt) {
        this.activityCnt = activityCnt;
    }

    public long getCarGroupCnt() {
        return carGroupCnt;
    }

    public void setCarGroupCnt(long carGroupCnt) {
        this.carGroupCnt = carGroupCnt;
    }

    public long getAttentionCnt() {
        return attentionCnt;
    }

    public void setAttentionCnt(long attentionCnt) {
        this.attentionCnt = attentionCnt;
    }

    public long getFansCnt() {
        return fansCnt;
    }

    public void setFansCnt(long fansCnt) {
        this.fansCnt = fansCnt;
    }
}
