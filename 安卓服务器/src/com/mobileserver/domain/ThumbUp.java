package com.mobileserver.domain;

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
    private int topObj;
    public int getTopObj() {
        return topObj;
    }
    public void setTopObj(int topObj) {
        this.topObj = topObj;
    }

    /*学生*/
    private String studentObj;
    public String getStudentObj() {
        return studentObj;
    }
    public void setStudentObj(String studentObj) {
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