package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.TopicClass;
import com.mobileclient.util.HttpUtil;

/*话题分类管理业务逻辑层*/
public class TopicClassService {
	/* 添加话题分类 */
	public String AddTopicClass(TopicClass topicClass) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("topicClassId", topicClass.getTopicClassId() + "");
		params.put("topicClassName", topicClass.getTopicClassName());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "TopicClassServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询话题分类 */
	public List<TopicClass> QueryTopicClass(TopicClass queryConditionTopicClass) throws Exception {
		String urlString = HttpUtil.BASE_URL + "TopicClassServlet?action=query";
		if(queryConditionTopicClass != null) {
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		TopicClassListHandler topicClassListHander = new TopicClassListHandler();
		xr.setContentHandler(topicClassListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<TopicClass> topicClassList = topicClassListHander.getTopicClassList();
		return topicClassList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<TopicClass> topicClassList = new ArrayList<TopicClass>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				TopicClass topicClass = new TopicClass();
				topicClass.setTopicClassId(object.getInt("topicClassId"));
				topicClass.setTopicClassName(object.getString("topicClassName"));
				topicClassList.add(topicClass);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return topicClassList;
	}

	/* 更新话题分类 */
	public String UpdateTopicClass(TopicClass topicClass) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("topicClassId", topicClass.getTopicClassId() + "");
		params.put("topicClassName", topicClass.getTopicClassName());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "TopicClassServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除话题分类 */
	public String DeleteTopicClass(int topicClassId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("topicClassId", topicClassId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "TopicClassServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "话题分类信息删除失败!";
		}
	}

	/* 根据分类id获取话题分类对象 */
	public TopicClass GetTopicClass(int topicClassId)  {
		List<TopicClass> topicClassList = new ArrayList<TopicClass>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("topicClassId", topicClassId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "TopicClassServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				TopicClass topicClass = new TopicClass();
				topicClass.setTopicClassId(object.getInt("topicClassId"));
				topicClass.setTopicClassName(object.getString("topicClassName"));
				topicClassList.add(topicClass);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = topicClassList.size();
		if(size>0) return topicClassList.get(0); 
		else return null; 
	}
}
