package com.chengxusheji.domain;

import java.sql.Timestamp;
public class Comment {
    /*��¼���*/
    private int commentId;
    public int getCommentId() {
        return commentId;
    }
    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    /*���۵Ļ���*/
    private Topic topicObj;
    public Topic getTopicObj() {
        return topicObj;
    }
    public void setTopicObj(Topic topicObj) {
        this.topicObj = topicObj;
    }

    /*��������*/
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    /*���۵�ѧ��*/
    private Student studentObj;
    public Student getStudentObj() {
        return studentObj;
    }
    public void setStudentObj(Student studentObj) {
        this.studentObj = studentObj;
    }

    /*����ʱ��*/
    private String commentTime;
    public String getCommentTime() {
        return commentTime;
    }
    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

}