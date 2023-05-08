package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.Student;
import com.mobileserver.util.DB;

public class StudentDAO {

	public List<Student> QueryStudent(String studentNumber,String specialObj,String studentName,Timestamp birthday) {
		List<Student> studentList = new ArrayList<Student>();
		DB db = new DB();
		String sql = "select * from Student where 1=1";
		if (!studentNumber.equals(""))
			sql += " and studentNumber like '%" + studentNumber + "%'";
		if (!specialObj.equals(""))
			sql += " and specialObj = '" + specialObj + "'";
		if (!studentName.equals(""))
			sql += " and studentName like '%" + studentName + "%'";
		if(birthday!=null)
			sql += " and birthday='" + birthday + "'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				Student student = new Student();
				student.setStudentNumber(rs.getString("studentNumber"));
				student.setPassword(rs.getString("password"));
				student.setSpecialObj(rs.getString("specialObj"));
				student.setStudentName(rs.getString("studentName"));
				student.setSex(rs.getString("sex"));
				student.setBirthday(rs.getTimestamp("birthday"));
				student.setStudentPhoto(rs.getString("studentPhoto"));
				student.setTelephone(rs.getString("telephone"));
				student.setAddress(rs.getString("address"));
				student.setMemo(rs.getString("memo"));
				studentList.add(student);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return studentList;
	}
	/* 传入学生信息对象，进行学生信息的添加业务 */
	public String AddStudent(Student student) {
		DB db = new DB();
		String result = "";
		try {
			/* 构建sql执行插入新学生信息 */
			String sqlString = "insert into Student(studentNumber,password,specialObj,studentName,sex,birthday,studentPhoto,telephone,address,memo) values (";
			sqlString += "'" + student.getStudentNumber() + "',";
			sqlString += "'" + student.getPassword() + "',";
			sqlString += "'" + student.getSpecialObj() + "',";
			sqlString += "'" + student.getStudentName() + "',";
			sqlString += "'" + student.getSex() + "',";
			sqlString += "'" + student.getBirthday() + "',";
			sqlString += "'" + student.getStudentPhoto() + "',";
			sqlString += "'" + student.getTelephone() + "',";
			sqlString += "'" + student.getAddress() + "',";
			sqlString += "'" + student.getMemo() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "学生信息添加成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "学生信息添加失败";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* 删除学生信息 */
	public String DeleteStudent(String studentNumber) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from Student where studentNumber='" + studentNumber + "'";
			db.executeUpdate(sqlString);
			result = "学生信息删除成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "学生信息删除失败";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* 根据学号获取到学生信息 */
	public Student GetStudent(String studentNumber) {
		Student student = null;
		DB db = new DB();
		String sql = "select * from Student where studentNumber='" + studentNumber + "'";
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				student = new Student();
				student.setStudentNumber(rs.getString("studentNumber"));
				student.setPassword(rs.getString("password"));
				student.setSpecialObj(rs.getString("specialObj"));
				student.setStudentName(rs.getString("studentName"));
				student.setSex(rs.getString("sex"));
				student.setBirthday(rs.getTimestamp("birthday"));
				student.setStudentPhoto(rs.getString("studentPhoto"));
				student.setTelephone(rs.getString("telephone"));
				student.setAddress(rs.getString("address"));
				student.setMemo(rs.getString("memo"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return student;
	}
	/* 更新学生信息 */
	public String UpdateStudent(Student student) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update Student set ";
			sql += "password='" + student.getPassword() + "',";
			sql += "specialObj='" + student.getSpecialObj() + "',";
			sql += "studentName='" + student.getStudentName() + "',";
			sql += "sex='" + student.getSex() + "',";
			sql += "birthday='" + student.getBirthday() + "',";
			sql += "studentPhoto='" + student.getStudentPhoto() + "',";
			sql += "telephone='" + student.getTelephone() + "',";
			sql += "address='" + student.getAddress() + "',";
			sql += "memo='" + student.getMemo() + "'";
			sql += " where studentNumber='" + student.getStudentNumber() + "'";
			db.executeUpdate(sql);
			result = "学生信息更新成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "学生信息更新失败";
		} finally {
			db.all_close();
		}
		return result;
	}
}
