package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.TopicClass;
import com.mobileserver.util.DB;

public class TopicClassDAO {

	public List<TopicClass> QueryTopicClass() {
		List<TopicClass> topicClassList = new ArrayList<TopicClass>();
		DB db = new DB();
		String sql = "select * from TopicClass where 1=1";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				TopicClass topicClass = new TopicClass();
				topicClass.setTopicClassId(rs.getInt("topicClassId"));
				topicClass.setTopicClassName(rs.getString("topicClassName"));
				topicClassList.add(topicClass);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return topicClassList;
	}
	/* 传入话题分类对象，进行话题分类的添加业务 */
	public String AddTopicClass(TopicClass topicClass) {
		DB db = new DB();
		String result = "";
		try {
			/* 构建sql执行插入新话题分类 */
			String sqlString = "insert into TopicClass(topicClassName) values (";
			sqlString += "'" + topicClass.getTopicClassName() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "话题分类添加成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "话题分类添加失败";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* 删除话题分类 */
	public String DeleteTopicClass(int topicClassId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from TopicClass where topicClassId=" + topicClassId;
			db.executeUpdate(sqlString);
			result = "话题分类删除成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "话题分类删除失败";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* 根据分类id获取到话题分类 */
	public TopicClass GetTopicClass(int topicClassId) {
		TopicClass topicClass = null;
		DB db = new DB();
		String sql = "select * from TopicClass where topicClassId=" + topicClassId;
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				topicClass = new TopicClass();
				topicClass.setTopicClassId(rs.getInt("topicClassId"));
				topicClass.setTopicClassName(rs.getString("topicClassName"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return topicClass;
	}
	/* 更新话题分类 */
	public String UpdateTopicClass(TopicClass topicClass) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update TopicClass set ";
			sql += "topicClassName='" + topicClass.getTopicClassName() + "'";
			sql += " where topicClassId=" + topicClass.getTopicClassId();
			db.executeUpdate(sql);
			result = "话题分类更新成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "话题分类更新失败";
		} finally {
			db.all_close();
		}
		return result;
	}
}
