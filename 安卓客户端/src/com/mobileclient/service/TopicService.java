package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.Topic;
import com.mobileclient.util.HttpUtil;

/*话题管理业务逻辑层*/
public class TopicService {
	/* 添加话题 */
	public String AddTopic(Topic topic) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("topicId", topic.getTopicId() + "");
		params.put("title", topic.getTitle());
		params.put("topicClass", topic.getTopicClass() + "");
		params.put("photo", topic.getPhoto());
		params.put("content", topic.getContent());
		params.put("studentObj", topic.getStudentObj());
		params.put("addTime", topic.getAddTime());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "TopicServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询话题 */
	public List<Topic> QueryTopic(Topic queryConditionTopic) throws Exception {
		String urlString = HttpUtil.BASE_URL + "TopicServlet?action=query";
		if(queryConditionTopic != null) {
			urlString += "&title=" + URLEncoder.encode(queryConditionTopic.getTitle(), "UTF-8") + "";
			urlString += "&topicClass=" + queryConditionTopic.getTopicClass();
			urlString += "&studentObj=" + URLEncoder.encode(queryConditionTopic.getStudentObj(), "UTF-8") + "";
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		TopicListHandler topicListHander = new TopicListHandler();
		xr.setContentHandler(topicListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<Topic> topicList = topicListHander.getTopicList();
		return topicList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<Topic> topicList = new ArrayList<Topic>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Topic topic = new Topic();
				topic.setTopicId(object.getInt("topicId"));
				topic.setTitle(object.getString("title"));
				topic.setTopicClass(object.getInt("topicClass"));
				topic.setPhoto(object.getString("photo"));
				topic.setContent(object.getString("content"));
				topic.setStudentObj(object.getString("studentObj"));
				topic.setAddTime(object.getString("addTime"));
				topicList.add(topic);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return topicList;
	}

	/* 更新话题 */
	public String UpdateTopic(Topic topic) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("topicId", topic.getTopicId() + "");
		params.put("title", topic.getTitle());
		params.put("topicClass", topic.getTopicClass() + "");
		params.put("photo", topic.getPhoto());
		params.put("content", topic.getContent());
		params.put("studentObj", topic.getStudentObj());
		params.put("addTime", topic.getAddTime());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "TopicServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除话题 */
	public String DeleteTopic(int topicId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("topicId", topicId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "TopicServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "话题信息删除失败!";
		}
	}

	/* 根据话题id获取话题对象 */
	public Topic GetTopic(int topicId)  {
		List<Topic> topicList = new ArrayList<Topic>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("topicId", topicId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "TopicServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Topic topic = new Topic();
				topic.setTopicId(object.getInt("topicId"));
				topic.setTitle(object.getString("title"));
				topic.setTopicClass(object.getInt("topicClass"));
				topic.setPhoto(object.getString("photo"));
				topic.setContent(object.getString("content"));
				topic.setStudentObj(object.getString("studentObj"));
				topic.setAddTime(object.getString("addTime"));
				topicList.add(topic);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = topicList.size();
		if(size>0) return topicList.get(0); 
		else return null; 
	}
}
