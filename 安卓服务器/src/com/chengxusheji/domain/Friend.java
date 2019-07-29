package com.chengxusheji.domain;

import java.sql.Timestamp;
public class Friend {
    /*记录编号*/
    private int friendId;
    public int getFriendId() {
        return friendId;
    }
    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    /*学生1*/
    private Student studentObj1;
    public Student getStudentObj1() {
        return studentObj1;
    }
    public void setStudentObj1(Student studentObj1) {
        this.studentObj1 = studentObj1;
    }

    /*好友*/
    private Student studentObj2;
    public Student getStudentObj2() {
        return studentObj2;
    }
    public void setStudentObj2(Student studentObj2) {
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