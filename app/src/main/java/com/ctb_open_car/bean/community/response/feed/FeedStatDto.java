package com.ctb_open_car.bean.community.response.feed;

import java.io.Serializable;

/**
 * @author L.Y.F
 * @create 2019-09-05 16:37
 */
public class FeedStatDto implements Serializable {

    /** 动态转发数 */
    private long forwardCnt;
    /** 动态阅读数 */
    private long readCnt;
    /** 动态点赞数 */
    private long praiseCnt;
    /** 动态评论数 */
    private long commentCnt;

    public long getForwardCnt() {
        return forwardCnt;
    }

    public void setForwardCnt(long forwardCnt) {
        this.forwardCnt = forwardCnt;
    }

    public long getReadCnt() {
        return readCnt;
    }

    public void setReadCnt(long readCnt) {
        this.readCnt = readCnt;
    }

    public long getPraiseCnt() {
        return praiseCnt;
    }

    public void setPraiseCnt(long praiseCnt) {
        this.praiseCnt = praiseCnt;
    }

    public long getCommentCnt() {
        return commentCnt;
    }

    public void setCommentCnt(long commentCnt) {
        this.commentCnt = commentCnt;
    }
}
