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
	/* 传入专业信息对象，进行专业信息的添加业务 */
	public String AddSpecialInfo(SpecialInfo specialInfo) {
		DB db = new DB();
		String result = "";
		try {
			/* 构建sql执行插入新专业信息 */
			String sqlString = "insert into SpecialInfo(collegeObj,specialNumber,specialName,startDate,introduction) values (";
			sqlString += "'" + specialInfo.getCollegeObj() + "',";
			sqlString += "'" + specialInfo.getSpecialNumber() + "',";
			sqlString += "'" + specialInfo.getSpecialName() + "',";
			sqlString += "'" + specialInfo.getStartDate() + "',";
			sqlString += "'" + specialInfo.getIntroduction() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "专业信息添加成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "专业信息添加失败";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* 删除专业信息 */
	public String DeleteSpecialInfo(String specialNumber) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from SpecialInfo where specialNumber='" + specialNumber + "'";
			db.executeUpdate(sqlString);
			result = "专业信息删除成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "专业信息删除失败";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* 根据专业编号获取到专业信息 */
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
	/* 更新专业信息 */
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
			result = "专业信息更新成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "专业信息更新失败";
		} finally {
			db.all_close();
		}
		return result;
	}
}
