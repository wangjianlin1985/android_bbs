package com.chengxusheji.domain;

import java.sql.Timestamp;
public class ThumbUp {
    /*记录编号*/
    private int thumbUpId;
    public int getThumbUpId() {
        return thumbUpId;
    }
    public void setThumbUpId(int thumbUpId) {
        this.thumbUpId = thumbUpId;
    }

    /*话题*/
    private Topic topObj;
    public Topic getTopObj() {
        return topObj;
    }
    public void setTopObj(Topic topObj) {
        this.topObj = topObj;
    }

    /*学生*/
    private Student studentObj;
    public Student getStudentObj() {
        return studentObj;
    }
    public void setStudentObj(Student studentObj) {
        this.studentObj = studentObj;
    }

    /*点赞时间*/
    private String thumpTime;
    public String getThumpTime() {
        return thumpTime;
    }
    public void setThumpTime(String thumpTime) {
        this.thumpTime = thumpTime;
    }

}