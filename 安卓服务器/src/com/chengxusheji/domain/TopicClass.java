package com.chengxusheji.domain;

import java.sql.Timestamp;
public class TopicClass {
    /*����id*/
    private int topicClassId;
    public int getTopicClassId() {
        return topicClassId;
    }
    public void setTopicClassId(int topicClassId) {
        this.topicClassId = topicClassId;
    }

    /*��������*/
    private String topicClassName;
    public String getTopicClassName() {
        return topicClassName;
    }
    public void setTopicClassName(String topicClassName) {
        this.topicClassName = topicClassName;
    }

}