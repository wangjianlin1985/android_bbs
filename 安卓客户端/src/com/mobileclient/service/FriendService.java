package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.Friend;
import com.mobileclient.util.HttpUtil;

/*好友信息管理业务逻辑层*/
public class FriendService {
	/* 添加好友信息 */
	public String AddFriend(Friend friend) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("friendId", friend.getFriendId() + "");
		params.put("studentObj1", friend.getStudentObj1());
		params.put("studentObj2", friend.getStudentObj2());
		params.put("addTime", friend.getAddTime());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "FriendServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询好友信息 */
	public List<Friend> QueryFriend(Friend queryConditionFriend) throws Exception {
		String urlString = HttpUtil.BASE_URL + "FriendServlet?action=query";
		if(queryConditionFriend != null) {
			urlString += "&studentObj1=" + URLEncoder.encode(queryConditionFriend.getStudentObj1(), "UTF-8") + "";
			urlString += "&studentObj2=" + URLEncoder.encode(queryConditionFriend.getStudentObj2(), "UTF-8") + "";
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		FriendListHandler friendListHander = new FriendListHandler();
		xr.setContentHandler(friendListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<Friend> friendList = friendListHander.getFriendList();
		return friendList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<Friend> friendList = new ArrayList<Friend>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Friend friend = new Friend();
				friend.setFriendId(object.getInt("friendId"));
				friend.setStudentObj1(object.getString("studentObj1"));
				friend.setStudentObj2(object.getString("studentObj2"));
				friend.setAddTime(object.getString("addTime"));
				friendList.add(friend);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return friendList;
	}

	/* 更新好友信息 */
	public String UpdateFriend(Friend friend) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("friendId", friend.getFriendId() + "");
		params.put("studentObj1", friend.getStudentObj1());
		params.put("studentObj2", friend.getStudentObj2());
		params.put("addTime", friend.getAddTime());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "FriendServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除好友信息 */
	public String DeleteFriend(int friendId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("friendId", friendId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "FriendServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "好友信息信息删除失败!";
		}
	}

	/* 根据记录编号获取好友信息对象 */
	public Friend GetFriend(int friendId)  {
		List<Friend> friendList = new ArrayList<Friend>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("friendId", friendId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "FriendServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Friend friend = new Friend();
				friend.setFriendId(object.getInt("friendId"));
				friend.setStudentObj1(object.getString("studentObj1"));
				friend.setStudentObj2(object.getString("studentObj2"));
				friend.setAddTime(object.getString("addTime"));
				friendList.add(friend);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = friendList.size();
		if(size>0) return friendList.get(0); 
		else return null; 
	}
}
