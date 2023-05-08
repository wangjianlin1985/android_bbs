package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.SpecialInfo;
import com.mobileserver.util.DB;

public class SpecialInfoDAO {

	public List<SpecialInfo> QuerySpecialInfo(String collegeObj,String specialNumber,String specialName) {
		List<SpecialInfo> specialInfoList = new ArrayList<SpecialInfo>();
		DB db = new DB();
		String sql = "select * from SpecialInfo where 1=1";
		if (!collegeObj.equals(""))
			sql += " and collegeObj = '" + collegeObj + "'";
		if (!specialNumber.equals(""))
			sql += " and specialNumber like '%" + specialNumber + "%'";
		if (!specialName.equals(""))
			sql += " and specialName like '%" + specialName + "%'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				SpecialInfo specialInfo = new SpecialInfo();
				specialInfo.setCollegeObj(rs.getString("collegeObj"));
				specialInfo.setSpecialNumber(rs.getString("specialNumber"));
				specialInfo.setSpecialName(rs.getString("specialName"));
				specialInfo.setStartDate(rs.getTimestamp("startDate"));
				specialInfo.setIntroduction(rs.getString("introduction"));
				specialInfoList.add(specialInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return specialInfoList;
	}
	/* ����רҵ��Ϣ���󣬽���רҵ��Ϣ�����ҵ�� */
	public String AddSpecialInfo(SpecialInfo specialInfo) {
		DB db = new DB();
		String result = "";
		try {
			/* ����sqlִ�в�����רҵ��Ϣ */
			String sqlString = "insert into SpecialInfo(collegeObj,specialNumber,specialName,startDate,introduction) values (";
			sqlString += "'" + specialInfo.getCollegeObj() + "',";
			sqlString += "'" + specialInfo.getSpecialNumber() + "',";
			sqlString += "'" + specialInfo.getSpecialName() + "',";
			sqlString += "'" + specialInfo.getStartDate() + "',";
			sqlString += "'" + specialInfo.getIntroduction() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "רҵ��Ϣ��ӳɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "רҵ��Ϣ���ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* ɾ��רҵ��Ϣ */
	public String DeleteSpecialInfo(String specialNumber) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from SpecialInfo where specialNumber='" + specialNumber + "'";
			db.executeUpdate(sqlString);
			result = "רҵ��Ϣɾ���ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "רҵ��Ϣɾ��ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* ����רҵ��Ż�ȡ��רҵ��Ϣ */
	public SpecialInfo GetSpecialInfo(String specialNumber) {
		SpecialInfo specialInfo = null;
		DB db = new DB();
		String sql = "select * from SpecialInfo where specialNumber='" + specialNumber + "'";
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				specialInfo = new SpecialInfo();
				specialInfo.setCollegeObj(rs.getString("collegeObj"));
				specialInfo.setSpecialNumber(rs.getString("specialNumber"));
				specialInfo.setSpecialName(rs.getString("specialName"));
				specialInfo.setStartDate(rs.getTimestamp("startDate"));
				specialInfo.setIntroduction(rs.getString("introduction"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return specialInfo;
	}
	/* ����רҵ��Ϣ */
	public String UpdateSpecialInfo(SpecialInfo specialInfo) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update SpecialInfo set ";
			sql += "collegeObj='" + specialInfo.getCollegeObj() + "',";
			sql += "specialName='" + specialInfo.getSpecialName() + "',";
			sql += "startDate='" + specialInfo.getStartDate() + "',";
			sql += "introduction='" + specialInfo.getIntroduction() + "'";
			sql += " where specialNumber='" + specialInfo.getSpecialNumber() + "'";
			db.executeUpdate(sql);
			result = "רҵ��Ϣ���³ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "רҵ��Ϣ����ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
}
