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
	/* ���뻰�������󣬽��л����������ҵ�� */
	public String AddTopicClass(TopicClass topicClass) {
		DB db = new DB();
		String result = "";
		try {
			/* ����sqlִ�в����»������ */
			String sqlString = "insert into TopicClass(topicClassName) values (";
			sqlString += "'" + topicClass.getTopicClassName() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "���������ӳɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "����������ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* ɾ��������� */
	public String DeleteTopicClass(int topicClassId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from TopicClass where topicClassId=" + topicClassId;
			db.executeUpdate(sqlString);
			result = "�������ɾ���ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "�������ɾ��ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* ���ݷ���id��ȡ��������� */
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
	/* ���»������ */
	public String UpdateTopicClass(TopicClass topicClass) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update TopicClass set ";
			sql += "topicClassName='" + topicClass.getTopicClassName() + "'";
			sql += " where topicClassId=" + topicClass.getTopicClassId();
			db.executeUpdate(sql);
			result = "���������³ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "����������ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
}
