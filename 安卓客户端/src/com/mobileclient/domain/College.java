package com.mobileclient.domain;

import java.io.Serializable;

public class College implements Serializable {
    /*学院编号*/
    private String collegeNumber;
    public String getCollegeNumber() {
        return collegeNumber;
    }
    public void setCollegeNumber(String collegeNumber) {
        this.collegeNumber = collegeNumber;
    }

    /*学院名称*/
    private String collegeName;
    public String getCollegeName() {
        return collegeName;
    }
    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

}