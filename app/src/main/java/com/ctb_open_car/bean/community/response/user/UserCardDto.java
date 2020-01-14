package com.ctb_open_car.bean.community.response.user;

/**
 * @author L.Y.F
 * @create 2019-09-05 18:24
 */
public class UserCardDto extends UserDto {

    /** 当前用户对此用户的关注状态  0：未关注、1：关注、2：粉丝、3：互相关注 */
    private int relationStatus;
    /** 此用户的关注数量 */
    private long attentionCnt;
    /** 此用户的粉丝数量 */
    private long fansCnt;

    public int getRelationStatus() {
        return relationStatus;
    }

    public void setRelationStatus(int relationStatus) {
        this.relationStatus = relationStatus;
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
