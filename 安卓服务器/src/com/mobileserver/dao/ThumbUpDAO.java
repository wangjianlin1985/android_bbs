package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.ThumbUp;
import com.mobileserver.util.DB;

public class ThumbUpDAO {

	public List<ThumbUp> QueryThumbUp(int topObj,String studentObj) {
		List<ThumbUp> thumbUpList = new ArrayList<ThumbUp>();
		DB db = new DB();
		String sql = "select * from ThumbUp where 1=1";
		if (topObj != 0)
			sql += " and topObj=" + topObj;
		if (!studentObj.equals(""))
			sql += " and studentObj = '" + studentObj + "'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				ThumbUp thumbUp = new ThumbUp();
				thumbUp.setThumbUpId(rs.getInt("thumbUpId"));
				thumbUp.setTopObj(rs.getInt("topObj"));
				thumbUp.setStudentObj(rs.getString("studentObj"));
				thumbUp.setThumpTime(rs.getString("thumpTime"));
				thumbUpList.add(thumbUp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return thumbUpList;
	}
	/* ���������Ϣ���󣬽��е�����Ϣ�����ҵ�� */
	public String AddThumbUp(ThumbUp thumbUp) {
		DB db = new DB();
		String result = "";
		try {
			/* ����sqlִ�в����µ�����Ϣ */
			String sqlString = "insert into ThumbUp(topObj,studentObj,thumpTime) values (";
			sqlString += thumbUp.getTopObj() + ",";
			sqlString += "'" + thumbUp.getStudentObj() + "',";
			sqlString += "'" + thumbUp.getThumpTime() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "������Ϣ��ӳɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "������Ϣ���ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* ɾ��������Ϣ */
	public String DeleteThumbUp(int thumbUpId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from ThumbUp where thumbUpId=" + thumbUpId;
			db.executeUpdate(sqlString);
			result = "������Ϣɾ���ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "������Ϣɾ��ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* ���ݼ�¼��Ż�ȡ��������Ϣ */
	public ThumbUp GetThumbUp(int thumbUpId) {
		ThumbUp thumbUp = null;
		DB db = new DB();
		String sql = "select * from ThumbUp where thumbUpId=" + thumbUpId;
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				thumbUp = new ThumbUp();
				thumbUp.setThumbUpId(rs.getInt("thumbUpId"));
				thumbUp.setTopObj(rs.getInt("topObj"));
				thumbUp.setStudentObj(rs.getString("studentObj"));
				thumbUp.setThumpTime(rs.getString("thumpTime"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return thumbUp;
	}
	/* ���µ�����Ϣ */
	public String UpdateThumbUp(ThumbUp thumbUp) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update ThumbUp set ";
			sql += "topObj=" + thumbUp.getTopObj() + ",";
			sql += "studentObj='" + thumbUp.getStudentObj() + "',";
			sql += "thumpTime='" + thumbUp.getThumpTime() + "'";
			sql += " where thumbUpId=" + thumbUp.getThumbUpId();
			db.executeUpdate(sql);
			result = "������Ϣ���³ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "������Ϣ����ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
}
