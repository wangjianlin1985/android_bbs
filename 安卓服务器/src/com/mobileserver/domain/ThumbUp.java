package com.mobileserver.domain;

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
    private int topObj;
    public int getTopObj() {
        return topObj;
    }
    public void setTopObj(int topObj) {
        this.topObj = topObj;
    }

    /*ѧ��*/
    private String studentObj;
    public String getStudentObj() {
        return studentObj;
    }
    public void setStudentObj(String studentObj) {
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