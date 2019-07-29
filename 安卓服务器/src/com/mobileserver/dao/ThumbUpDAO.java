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
	/* 传入点赞信息对象，进行点赞信息的添加业务 */
	public String AddThumbUp(ThumbUp thumbUp) {
		DB db = new DB();
		String result = "";
		try {
			/* 构建sql执行插入新点赞信息 */
			String sqlString = "insert into ThumbUp(topObj,studentObj,thumpTime) values (";
			sqlString += thumbUp.getTopObj() + ",";
			sqlString += "'" + thumbUp.getStudentObj() + "',";
			sqlString += "'" + thumbUp.getThumpTime() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "点赞信息添加成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "点赞信息添加失败";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* 删除点赞信息 */
	public String DeleteThumbUp(int thumbUpId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from ThumbUp where thumbUpId=" + thumbUpId;
			db.executeUpdate(sqlString);
			result = "点赞信息删除成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "点赞信息删除失败";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* 根据记录编号获取到点赞信息 */
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
	/* 更新点赞信息 */
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
			result = "点赞信息更新成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "点赞信息更新失败";
		} finally {
			db.all_close();
		}
		return result;
	}
}
