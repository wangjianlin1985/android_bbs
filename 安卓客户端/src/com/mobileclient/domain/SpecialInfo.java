package com.mobileclient.domain;

import java.io.Serializable;

public class SpecialInfo implements Serializable {
    /*所属学院*/
    private String collegeObj;
    public String getCollegeObj() {
        return collegeObj;
    }
    public void setCollegeObj(String collegeObj) {
        this.collegeObj = collegeObj;
    }

    /*专业编号*/
    private String specialNumber;
    public String getSpecialNumber() {
        return specialNumber;
    }
    public void setSpecialNumber(String specialNumber) {
        this.specialNumber = specialNumber;
    }

    /*专业名称*/
    private String specialName;
    public String getSpecialName() {
        return specialName;
    }
    public void setSpecialName(String specialName) {
        this.specialName = specialName;
    }

    /*开办日期*/
    private java.sql.Timestamp startDate;
    public java.sql.Timestamp getStartDate() {
        return startDate;
    }
    public void setStartDate(java.sql.Timestamp startDate) {
        this.startDate = startDate;
    }

    /*专业介绍*/
    private String introduction;
    public String getIntroduction() {
        return introduction;
    }
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

}