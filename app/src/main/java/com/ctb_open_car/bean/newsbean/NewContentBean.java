package com.ctb_open_car.bean.newsbean;

import java.io.Serializable;
import java.util.List;

/*******  资讯内容 数据   *****/
public class NewContentBean implements Serializable {
    private String informationTitle;
    private String themeName;
    private long publishTime;
    private String informationImg;
    private int type;
    private String content;
    private String informationDesc;
    private String linksType;
    private String informationVideo;
    private String informationUrl;
    private int comments;
    private int likes;
    private int likeStatus;
    private List<ContentInfo> commentList;
    public void setInformationTitle(String informationTitle) {
        this.informationTitle = informationTitle;
    }

    public String getInformationTitle() {
        return informationTitle;
    }
    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setInformationImg(String informationImg) {
        this.informationImg = informationImg;
    }

    public String getInformationImg() {
        return informationImg;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setInformationDesc(String informationDesc) {
        this.informationDesc = informationDesc;
    }

    public String getInformationDesc() {
        return informationDesc;
    }

    public void setLinksType(String linksType) {
        this.linksType = linksType;
    }

    public String getLinksType() {
        return linksType;
    }

    public void setInformationVideo(String informationVideo) {
        this.informationVideo = informationVideo;
    }

    public String getInformationVideo() {
        return informationVideo;
    }

    public void setInformationUrl(String informationUrl) {
        this.informationUrl = informationUrl;
    }

    public String getInformationUrl() {
        return informationUrl;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getComments() {
        return comments;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikeStatus(int likeStatus) {
        this.likeStatus = likeStatus;
    }

    public int getLikeStatus() {
        return likeStatus;
    }

    public void setCommentList(List<ContentInfo> commentList) {
        this.commentList = commentList;
    }

    public List<ContentInfo> getCommentList() {
        return commentList;
    }

    /*******  资讯内容评论数据  *****/
    public class ContentInfo{
        private String commentId; // "评论Id",
        private String commentContent; //评论内容",
        private CommentUserInfo commentUser;
        private ReplyCommentUser replyComment;
        private int alreadyCommentPraise; //是否已经点过赞
        private int commentPraiseCnt; //评论点赞数量,
        private long commentTime; //评论时间戳

        public void setCommentId(String commentId) {
            this.commentId = commentId;
        }

        public String getCommentId() {
            return commentId;
        }

        public void setCommentContent(String commentContent) {
            this.commentContent = commentContent;
        }

        public String getCommentContent() {
            return commentContent;
        }

        public void setCommentUser(CommentUserInfo commentUser) {
            this.commentUser = commentUser;
        }

        public CommentUserInfo getCommentUser() {
            return commentUser;
        }

        public void setReplyComment(ReplyCommentUser replyComment) {
            this.replyComment = replyComment;
        }

        public ReplyCommentUser getReplyComment() {
            return replyComment;
        }

        public void setAlreadyCommentPraise(int alreadyCommentPraise) {
            this.alreadyCommentPraise = alreadyCommentPraise;
        }

        public int getAlreadyCommentPraise() {
            return alreadyCommentPraise;
        }

        public void setCommentPraiseCnt(int commentPraiseCnt) {
            this.commentPraiseCnt = commentPraiseCnt;
        }

        public int getCommentPraiseCnt() {
            return commentPraiseCnt;
        }

        public void setCommentTime(long commentTime) {
            this.commentTime = commentTime;
        }

        public long getCommentTime() {
            return commentTime;
        }
    }

    /*******  对资讯内容 进行评论 的用户信息  *****/
    public class CommentUserInfo{
        private String userId; //用户ID
        private String nickname; //用户昵称
       // private ContentUserIcon userIcon;
        private String userIcon;
        private String userAuthStatus; //用户认证状态 0：默认未认证、1：认证大V"
        private String  userMoodIcon; //"用户心情图标

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserId() {
            return userId;
        }

        public void setNickName(String nickName) {
            this.nickname = nickName;
        }

        public String getNickName() {
            return nickname;
        }

        public void setUserIcon(String userIcon) {
            this.userIcon = userIcon;
        }

        public String getUserIcon() {
            return userIcon;
        }
//        public void setUserIcon(ContentUserIcon userIcon) {
//            this.userIcon = userIcon;
//        }
//
//        public ContentUserIcon getUserIcon() {
//            return userIcon;
//        }

        public void setUserAuthStatus(String userAuthStatus) {
            this.userAuthStatus = userAuthStatus;
        }

        public String getUserAuthStatus() {
            return userAuthStatus;
        }

        public void setUserMoodIcon(String userMoodIcon) {
            this.userMoodIcon = userMoodIcon;
        }

        public String getUserMoodIcon() {
            return userMoodIcon;
        }
    }

    public class ContentUserIcon{
        private String resourceId; //资源文件ID
        private String resourceUrl; //资源文件网络路径

        public void setResourceId(String resourceId) {
            this.resourceId = resourceId;
        }

        public String getResourceId() {
            return resourceId;
        }

        public void setResourceUrl(String resourceUrl) {
            this.resourceUrl = resourceUrl;
        }

        public String getResourceUrl() {
            return resourceUrl;
        }
    }

    /*******  对资讯内容评论 点赞的用户信息  *****/
    public class ReplyCommentUser{
        private String commentId; //评论ID
        private String commentContent; //评论内容
        private CommentUserInfo commentUser;
        private int commentPraiseCnt;  //评论点赞数量
        private long commentTime; //评论时间戳

        public void setCommentId(String commentId) {
            this.commentId = commentId;
        }

        public String getCommentId() {
            return commentId;
        }

        public void setCommentContent(String commentContent) {
            this.commentContent = commentContent;
        }

        public String getCommentContent() {
            return commentContent;
        }

        public void setCommentUser(CommentUserInfo commentUser) {
            this.commentUser = commentUser;
        }

        public CommentUserInfo getCommentUser() {
            return commentUser;
        }

        public void setCommentPraiseCnt(int commentPraiseCnt) {
            this.commentPraiseCnt = commentPraiseCnt;
        }

        public int getCommentPraiseCnt() {
            return commentPraiseCnt;
        }

        public void setCommentTime(long commentTime) {
            this.commentTime = commentTime;
        }

        public long getCommentTime() {
            return commentTime;
        }
    }
}
