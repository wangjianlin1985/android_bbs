package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.College;
import com.mobileserver.util.DB;

public class CollegeDAO {

	public List<College> QueryCollege() {
		List<College> collegeList = new ArrayList<College>();
		DB db = new DB();
		String sql = "select * from College where 1=1";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				College college = new College();
				college.setCollegeNumber(rs.getString("collegeNumber"));
				college.setCollegeName(rs.getString("collegeName"));
				collegeList.add(college);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return collegeList;
	}
	/* ����ѧԺ��Ϣ���󣬽���ѧԺ��Ϣ�����ҵ�� */
	public String AddCollege(College college) {
		DB db = new DB();
		String result = "";
		try {
			/* ����sqlִ�в�����ѧԺ��Ϣ */
			String sqlString = "insert into College(collegeNumber,collegeName) values (";
			sqlString += "'" + college.getCollegeNumber() + "',";
			sqlString += "'" + college.getCollegeName() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "ѧԺ��Ϣ��ӳɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "ѧԺ��Ϣ���ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* ɾ��ѧԺ��Ϣ */
	public String DeleteCollege(String collegeNumber) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from College where collegeNumber='" + collegeNumber + "'";
			db.executeUpdate(sqlString);
			result = "ѧԺ��Ϣɾ���ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "ѧԺ��Ϣɾ��ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* ����ѧԺ��Ż�ȡ��ѧԺ��Ϣ */
	public College GetCollege(String collegeNumber) {
		College college = null;
		DB db = new DB();
		String sql = "select * from College where collegeNumber='" + collegeNumber + "'";
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				college = new College();
				college.setCollegeNumber(rs.getString("collegeNumber"));
				college.setCollegeName(rs.getString("collegeName"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return college;
	}
	/* ����ѧԺ��Ϣ */
	public String UpdateCollege(College college) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update College set ";
			sql += "collegeName='" + college.getCollegeName() + "'";
			sql += " where collegeNumber='" + college.getCollegeNumber() + "'";
			db.executeUpdate(sql);
			result = "ѧԺ��Ϣ���³ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "ѧԺ��Ϣ����ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
}
