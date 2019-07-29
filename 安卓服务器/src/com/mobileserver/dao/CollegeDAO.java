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
	/* 传入学院信息对象，进行学院信息的添加业务 */
	public String AddCollege(College college) {
		DB db = new DB();
		String result = "";
		try {
			/* 构建sql执行插入新学院信息 */
			String sqlString = "insert into College(collegeNumber,collegeName) values (";
			sqlString += "'" + college.getCollegeNumber() + "',";
			sqlString += "'" + college.getCollegeName() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "学院信息添加成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "学院信息添加失败";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* 删除学院信息 */
	public String DeleteCollege(String collegeNumber) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from College where collegeNumber='" + collegeNumber + "'";
			db.executeUpdate(sqlString);
			result = "学院信息删除成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "学院信息删除失败";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* 根据学院编号获取到学院信息 */
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
	/* 更新学院信息 */
	public String UpdateCollege(College college) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update College set ";
			sql += "collegeName='" + college.getCollegeName() + "'";
			sql += " where collegeNumber='" + college.getCollegeNumber() + "'";
			db.executeUpdate(sql);
			result = "学院信息更新成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "学院信息更新失败";
		} finally {
			db.all_close();
		}
		return result;
	}
}
