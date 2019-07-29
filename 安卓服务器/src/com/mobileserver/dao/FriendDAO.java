package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.Friend;
import com.mobileserver.util.DB;

public class FriendDAO {

	public List<Friend> QueryFriend(String studentObj1,String studentObj2) {
		List<Friend> friendList = new ArrayList<Friend>();
		DB db = new DB();
		String sql = "select * from Friend where 1=1";
		if (!studentObj1.equals(""))
			sql += " and studentObj1 = '" + studentObj1 + "'";
		if (!studentObj2.equals(""))
			sql += " and studentObj2 = '" + studentObj2 + "'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				Friend friend = new Friend();
				friend.setFriendId(rs.getInt("friendId"));
				friend.setStudentObj1(rs.getString("studentObj1"));
				friend.setStudentObj2(rs.getString("studentObj2"));
				friend.setAddTime(rs.getString("addTime"));
				friendList.add(friend);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return friendList;
	}
	/* 传入好友信息对象，进行好友信息的添加业务 */
	public String AddFriend(Friend friend) {
		DB db = new DB();
		String result = "";
		try {
			/* 构建sql执行插入新好友信息 */
			String sqlString = "insert into Friend(studentObj1,studentObj2,addTime) values (";
			sqlString += "'" + friend.getStudentObj1() + "',";
			sqlString += "'" + friend.getStudentObj2() + "',";
			sqlString += "'" + friend.getAddTime() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "好友信息添加成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "好友信息添加失败";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* 删除好友信息 */
	public String DeleteFriend(int friendId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from Friend where friendId=" + friendId;
			db.executeUpdate(sqlString);
			result = "好友信息删除成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "好友信息删除失败";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* 根据记录编号获取到好友信息 */
	public Friend GetFriend(int friendId) {
		Friend friend = null;
		DB db = new DB();
		String sql = "select * from Friend where friendId=" + friendId;
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				friend = new Friend();
				friend.setFriendId(rs.getInt("friendId"));
				friend.setStudentObj1(rs.getString("studentObj1"));
				friend.setStudentObj2(rs.getString("studentObj2"));
				friend.setAddTime(rs.getString("addTime"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return friend;
	}
	/* 更新好友信息 */
	public String UpdateFriend(Friend friend) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update Friend set ";
			sql += "studentObj1='" + friend.getStudentObj1() + "',";
			sql += "studentObj2='" + friend.getStudentObj2() + "',";
			sql += "addTime='" + friend.getAddTime() + "'";
			sql += " where friendId=" + friend.getFriendId();
			db.executeUpdate(sql);
			result = "好友信息更新成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "好友信息更新失败";
		} finally {
			db.all_close();
		}
		return result;
	}
}
