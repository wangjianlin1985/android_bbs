package com.chengxusheji.domain;

import java.sql.Timestamp;
public class SpecialInfo {
    /*����ѧԺ*/
    private College collegeObj;
    public College getCollegeObj() {
        return collegeObj;
    }
    public void setCollegeObj(College collegeObj) {
        this.collegeObj = collegeObj;
    }

    /*רҵ���*/
    private String specialNumber;
    public String getSpecialNumber() {
        return specialNumber;
    }
    public void setSpecialNumber(String specialNumber) {
        this.specialNumber = specialNumber;
    }

    /*רҵ����*/
    private String specialName;
    public String getSpecialName() {
        return specialName;
    }
    public void setSpecialName(String specialName) {
        this.specialName = specialName;
    }

    /*��������*/
    private Timestamp startDate;
    public Timestamp getStartDate() {
        return startDate;
    }
    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    /*רҵ����*/
    private String introduction;
    public String getIntroduction() {
        return introduction;
    }
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

}