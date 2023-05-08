package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.Comment;
import com.mobileserver.util.DB;

public class CommentDAO {

	public List<Comment> QueryComment(int topicObj,String studentObj) {
		List<Comment> commentList = new ArrayList<Comment>();
		DB db = new DB();
		String sql = "select * from Comment where 1=1";
		if (topicObj != 0)
			sql += " and topicObj=" + topicObj;
		if (!studentObj.equals(""))
			sql += " and studentObj = '" + studentObj + "'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				Comment comment = new Comment();
				comment.setCommentId(rs.getInt("commentId"));
				comment.setTopicObj(rs.getInt("topicObj"));
				comment.setContent(rs.getString("content"));
				comment.setStudentObj(rs.getString("studentObj"));
				comment.setCommentTime(rs.getString("commentTime"));
				commentList.add(comment);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return commentList;
	}
	/* �������۶��󣬽������۵����ҵ�� */
	public String AddComment(Comment comment) {
		DB db = new DB();
		String result = "";
		try {
			/* ����sqlִ�в��������� */
			String sqlString = "insert into Comment(topicObj,content,studentObj,commentTime) values (";
			sqlString += comment.getTopicObj() + ",";
			sqlString += "'" + comment.getContent() + "',";
			sqlString += "'" + comment.getStudentObj() + "',";
			sqlString += "'" + comment.getCommentTime() + "'";
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
	public String DeleteComment(int commentId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from Comment where commentId=" + commentId;
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

	/* ���ݼ�¼��Ż�ȡ������ */
	public Comment GetComment(int commentId) {
		Comment comment = null;
		DB db = new DB();
		String sql = "select * from Comment where commentId=" + commentId;
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				comment = new Comment();
				comment.setCommentId(rs.getInt("commentId"));
				comment.setTopicObj(rs.getInt("topicObj"));
				comment.setContent(rs.getString("content"));
				comment.setStudentObj(rs.getString("studentObj"));
				comment.setCommentTime(rs.getString("commentTime"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return comment;
	}
	/* �������� */
	public String UpdateComment(Comment comment) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update Comment set ";
			sql += "topicObj=" + comment.getTopicObj() + ",";
			sql += "content='" + comment.getContent() + "',";
			sql += "studentObj='" + comment.getStudentObj() + "',";
			sql += "commentTime='" + comment.getCommentTime() + "'";
			sql += " where commentId=" + comment.getCommentId();
			db.executeUpdate(sql);
			result = "���۸��³ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "���۸���ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
}
