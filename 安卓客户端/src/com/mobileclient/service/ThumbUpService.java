package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.ThumbUp;
import com.mobileclient.util.HttpUtil;

/*点赞信息管理业务逻辑层*/
public class ThumbUpService {
	/* 添加点赞信息 */
	public String AddThumbUp(ThumbUp thumbUp) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("thumbUpId", thumbUp.getThumbUpId() + "");
		params.put("topObj", thumbUp.getTopObj() + "");
		params.put("studentObj", thumbUp.getStudentObj());
		params.put("thumpTime", thumbUp.getThumpTime());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ThumbUpServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询点赞信息 */
	public List<ThumbUp> QueryThumbUp(ThumbUp queryConditionThumbUp) throws Exception {
		String urlString = HttpUtil.BASE_URL + "ThumbUpServlet?action=query";
		if(queryConditionThumbUp != null) {
			urlString += "&topObj=" + queryConditionThumbUp.getTopObj();
			urlString += "&studentObj=" + URLEncoder.encode(queryConditionThumbUp.getStudentObj(), "UTF-8") + "";
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		ThumbUpListHandler thumbUpListHander = new ThumbUpListHandler();
		xr.setContentHandler(thumbUpListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<ThumbUp> thumbUpList = thumbUpListHander.getThumbUpList();
		return thumbUpList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<ThumbUp> thumbUpList = new ArrayList<ThumbUp>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				ThumbUp thumbUp = new ThumbUp();
				thumbUp.setThumbUpId(object.getInt("thumbUpId"));
				thumbUp.setTopObj(object.getInt("topObj"));
				thumbUp.setStudentObj(object.getString("studentObj"));
				thumbUp.setThumpTime(object.getString("thumpTime"));
				thumbUpList.add(thumbUp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return thumbUpList;
	}

	/* 更新点赞信息 */
	public String UpdateThumbUp(ThumbUp thumbUp) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("thumbUpId", thumbUp.getThumbUpId() + "");
		params.put("topObj", thumbUp.getTopObj() + "");
		params.put("studentObj", thumbUp.getStudentObj());
		params.put("thumpTime", thumbUp.getThumpTime());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ThumbUpServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除点赞信息 */
	public String DeleteThumbUp(int thumbUpId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("thumbUpId", thumbUpId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ThumbUpServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "点赞信息信息删除失败!";
		}
	}

	/* 根据记录编号获取点赞信息对象 */
	public ThumbUp GetThumbUp(int thumbUpId)  {
		List<ThumbUp> thumbUpList = new ArrayList<ThumbUp>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("thumbUpId", thumbUpId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ThumbUpServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				ThumbUp thumbUp = new ThumbUp();
				thumbUp.setThumbUpId(object.getInt("thumbUpId"));
				thumbUp.setTopObj(object.getInt("topObj"));
				thumbUp.setStudentObj(object.getString("studentObj"));
				thumbUp.setThumpTime(object.getString("thumpTime"));
				thumbUpList.add(thumbUp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = thumbUpList.size();
		if(size>0) return thumbUpList.get(0); 
		else return null; 
	}
}
