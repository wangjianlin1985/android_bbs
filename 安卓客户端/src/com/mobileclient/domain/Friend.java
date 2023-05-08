package com.mobileclient.domain;

import java.io.Serializable;

public class Friend implements Serializable {
    /*记录编号*/
    private int friendId;
    public int getFriendId() {
        return friendId;
    }
    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    /*学生1*/
    private String studentObj1;
    public String getStudentObj1() {
        return studentObj1;
    }
    public void setStudentObj1(String studentObj1) {
        this.studentObj1 = studentObj1;
    }

    /*好友*/
    private String studentObj2;
    public String getStudentObj2() {
        return studentObj2;
    }
    public void setStudentObj2(String studentObj2) {
        this.studentObj2 = studentObj2;
    }

    /*添加时间*/
    private String addTime;
    public String getAddTime() {
        return addTime;
    }
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

}