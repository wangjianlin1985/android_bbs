package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.Topic;
import com.mobileserver.util.DB;

public class TopicDAO {

	public List<Topic> QueryTopic(String title,int topicClass,String studentObj) {
		List<Topic> topicList = new ArrayList<Topic>();
		DB db = new DB();
		String sql = "select * from Topic where 1=1";
		if (!title.equals(""))
			sql += " and title like '%" + title + "%'";
		if (topicClass != 0)
			sql += " and topicClass=" + topicClass;
		if (!studentObj.equals(""))
			sql += " and studentObj = '" + studentObj + "'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				Topic topic = new Topic();
				topic.setTopicId(rs.getInt("topicId"));
				topic.setTitle(rs.getString("title"));
				topic.setTopicClass(rs.getInt("topicClass"));
				topic.setPhoto(rs.getString("photo"));
				topic.setContent(rs.getString("content"));
				topic.setStudentObj(rs.getString("studentObj"));
				topic.setAddTime(rs.getString("addTime"));
				topicList.add(topic);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return topicList;
	}
	/* ���뻰����󣬽��л�������ҵ�� */
	public String AddTopic(Topic topic) {
		DB db = new DB();
		String result = "";
		try {
			/* ����sqlִ�в����»��� */
			String sqlString = "insert into Topic(title,topicClass,photo,content,studentObj,addTime) values (";
			sqlString += "'" + topic.getTitle() + "',";
			sqlString += topic.getTopicClass() + ",";
			sqlString += "'" + topic.getPhoto() + "',";
			sqlString += "'" + topic.getContent() + "',";
			sqlString += "'" + topic.getStudentObj() + "',";
			sqlString += "'" + topic.getAddTime() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "������ӳɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "�������ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* ɾ������ */
	public String DeleteTopic(int topicId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from Topic where topicId=" + topicId;
			db.executeUpdate(sqlString);
			result = "����ɾ���ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "����ɾ��ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* ���ݻ���id��ȡ������ */
	public Topic GetTopic(int topicId) {
		Topic topic = null;
		DB db = new DB();
		String sql = "select * from Topic where topicId=" + topicId;
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				topic = new Topic();
				topic.setTopicId(rs.getInt("topicId"));
				topic.setTitle(rs.getString("title"));
				topic.setTopicClass(rs.getInt("topicClass"));
				topic.setPhoto(rs.getString("photo"));
				topic.setContent(rs.getString("content"));
				topic.setStudentObj(rs.getString("studentObj"));
				topic.setAddTime(rs.getString("addTime"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return topic;
	}
	/* ���»��� */
	public String UpdateTopic(Topic topic) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update Topic set ";
			sql += "title='" + topic.getTitle() + "',";
			sql += "topicClass=" + topic.getTopicClass() + ",";
			sql += "photo='" + topic.getPhoto() + "',";
			sql += "content='" + topic.getContent() + "',";
			sql += "studentObj='" + topic.getStudentObj() + "',";
			sql += "addTime='" + topic.getAddTime() + "'";
			sql += " where topicId=" + topic.getTopicId();
			db.executeUpdate(sql);
			result = "������³ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "�������ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
}
