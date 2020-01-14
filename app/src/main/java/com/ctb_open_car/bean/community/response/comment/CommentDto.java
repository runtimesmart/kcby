package com.ctb_open_car.bean.community.response.comment;


import com.ctb_open_car.bean.community.response.user.BasicUserDto;

import java.io.Serializable;

/**
 * @author L.Y.F
 * @create 2019-09-06 14:05
 */
public class CommentDto implements Serializable {

    /** 评论ID */
    private long commentId;
    /** 评论内容 */
    private String commentContent;
    /** 评论用户信息 */
    private BasicUserDto commentUser;
    /** 回复的评论信息 */
    private ReplyCommentDto replyComment;
    /** 是否已经点过赞 */
    private boolean alreadyCommentPraise;
    /** 评论点赞数量 */
    private long commentPraiseCnt;
    /** 评论时间戳 */
    private long commentTime;

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public BasicUserDto getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(BasicUserDto commentUser) {
        this.commentUser = commentUser;
    }

    public ReplyCommentDto getReplyComment() {
        return replyComment;
    }

    public void setReplyComment(ReplyCommentDto replyComment) {
        this.replyComment = replyComment;
    }

    public boolean isAlreadyCommentPraise() {
        return alreadyCommentPraise;
    }

    public void setAlreadyCommentPraise(boolean alreadyCommentPraise) {
        this.alreadyCommentPraise = alreadyCommentPraise;
    }

    public long getCommentPraiseCnt() {
        return commentPraiseCnt;
    }

    public void setCommentPraiseCnt(long commentPraiseCnt) {
        this.commentPraiseCnt = commentPraiseCnt;
    }

    public long getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(long commentTime) {
        this.commentTime = commentTime;
    }

}
