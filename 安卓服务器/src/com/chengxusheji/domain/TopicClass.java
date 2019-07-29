package com.chengxusheji.domain;

import java.sql.Timestamp;
public class TopicClass {
    /*分类id*/
    private int topicClassId;
    public int getTopicClassId() {
        return topicClassId;
    }
    public void setTopicClassId(int topicClassId) {
        this.topicClassId = topicClassId;
    }

    /*分类名称*/
    private String topicClassName;
    public String getTopicClassName() {
        return topicClassName;
    }
    public void setTopicClassName(String topicClassName) {
        this.topicClassName = topicClassName;
    }

}