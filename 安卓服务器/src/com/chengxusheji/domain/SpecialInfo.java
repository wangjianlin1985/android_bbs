package com.chengxusheji.domain;

import java.sql.Timestamp;
public class SpecialInfo {
    /*所属学院*/
    private College collegeObj;
    public College getCollegeObj() {
        return collegeObj;
    }
    public void setCollegeObj(College collegeObj) {
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
    private Timestamp startDate;
    public Timestamp getStartDate() {
        return startDate;
    }
    public void setStartDate(Timestamp startDate) {
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