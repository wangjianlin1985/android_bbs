package com.mobileserver.domain;

public class SpecialInfo {
    /*����ѧԺ*/
    private String collegeObj;
    public String getCollegeObj() {
        return collegeObj;
    }
    public void setCollegeObj(String collegeObj) {
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
    private java.sql.Timestamp startDate;
    public java.sql.Timestamp getStartDate() {
        return startDate;
    }
    public void setStartDate(java.sql.Timestamp startDate) {
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