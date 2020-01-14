package com.ctb_open_car.bean.community.response.feed;

import java.io.Serializable;

/**
 * @author L.Y.F
 * @create 2019-09-05 16:28
 */
public class FeedContentDto implements Serializable {

    /** 被转发的动态所属人ID */
    private Long userId;
    /** 被转发的动态所属人名称 */
    private String nickName;
    /** 被转发的动态内容 */
    private String feedContent;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getFeedContent() {
        return feedContent;
    }

    public void setFeedContent(String feedContent) {
        this.feedContent = feedContent;
    }
}
