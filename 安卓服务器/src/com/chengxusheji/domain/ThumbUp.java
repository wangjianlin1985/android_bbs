package com.chengxusheji.domain;

import java.sql.Timestamp;
public class ThumbUp {
    /*��¼���*/
    private int thumbUpId;
    public int getThumbUpId() {
        return thumbUpId;
    }
    public void setThumbUpId(int thumbUpId) {
        this.thumbUpId = thumbUpId;
    }

    /*����*/
    private Topic topObj;
    public Topic getTopObj() {
        return topObj;
    }
    public void setTopObj(Topic topObj) {
        this.topObj = topObj;
    }

    /*ѧ��*/
    private Student studentObj;
    public Student getStudentObj() {
        return studentObj;
    }
    public void setStudentObj(Student studentObj) {
        this.studentObj = studentObj;
    }

    /*����ʱ��*/
    private String thumpTime;
    public String getThumpTime() {
        return thumpTime;
    }
    public void setThumpTime(String thumpTime) {
        this.thumpTime = thumpTime;
    }

}