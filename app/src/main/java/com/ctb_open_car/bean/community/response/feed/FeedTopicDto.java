package com.ctb_open_car.bean.community.response.feed;

import java.io.Serializable;

/**
 * @author L.Y.F
 * @create 2019-09-05 18:58
 */
public class FeedTopicDto implements Serializable {

    /** 话题ID */
    private int topicId;
    /** 话题名称 */
    private String topicName;

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

}
