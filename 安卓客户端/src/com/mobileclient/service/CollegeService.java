package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.College;
import com.mobileclient.util.HttpUtil;

/*学院信息管理业务逻辑层*/
public class CollegeService {
	/* 添加学院信息 */
	public String AddCollege(College college) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("collegeNumber", college.getCollegeNumber());
		params.put("collegeName", college.getCollegeName());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "CollegeServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询学院信息 */
	public List<College> QueryCollege(College queryConditionCollege) throws Exception {
		String urlString = HttpUtil.BASE_URL + "CollegeServlet?action=query";
		if(queryConditionCollege != null) {
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		CollegeListHandler collegeListHander = new CollegeListHandler();
		xr.setContentHandler(collegeListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<College> collegeList = collegeListHander.getCollegeList();
		return collegeList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<College> collegeList = new ArrayList<College>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				College college = new College();
				college.setCollegeNumber(object.getString("collegeNumber"));
				college.setCollegeName(object.getString("collegeName"));
				collegeList.add(college);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return collegeList;
	}

	/* 更新学院信息 */
	public String UpdateCollege(College college) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("collegeNumber", college.getCollegeNumber());
		params.put("collegeName", college.getCollegeName());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "CollegeServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除学院信息 */
	public String DeleteCollege(String collegeNumber) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("collegeNumber", collegeNumber);
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "CollegeServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "学院信息信息删除失败!";
		}
	}

	/* 根据学院编号获取学院信息对象 */
	public College GetCollege(String collegeNumber)  {
		List<College> collegeList = new ArrayList<College>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("collegeNumber", collegeNumber);
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "CollegeServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				College college = new College();
				college.setCollegeNumber(object.getString("collegeNumber"));
				college.setCollegeName(object.getString("collegeName"));
				collegeList.add(college);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = collegeList.size();
		if(size>0) return collegeList.get(0); 
		else return null; 
	}
}
