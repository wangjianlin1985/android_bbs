package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.Comment;
import com.mobileclient.util.HttpUtil;

/*评论管理业务逻辑层*/
public class CommentService {
	/* 添加评论 */
	public String AddComment(Comment comment) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("commentId", comment.getCommentId() + "");
		params.put("topicObj", comment.getTopicObj() + "");
		params.put("content", comment.getContent());
		params.put("studentObj", comment.getStudentObj());
		params.put("commentTime", comment.getCommentTime());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "CommentServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询评论 */
	public List<Comment> QueryComment(Comment queryConditionComment) throws Exception {
		String urlString = HttpUtil.BASE_URL + "CommentServlet?action=query";
		if(queryConditionComment != null) {
			urlString += "&topicObj=" + queryConditionComment.getTopicObj();
			urlString += "&studentObj=" + URLEncoder.encode(queryConditionComment.getStudentObj(), "UTF-8") + "";
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		CommentListHandler commentListHander = new CommentListHandler();
		xr.setContentHandler(commentListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<Comment> commentList = commentListHander.getCommentList();
		return commentList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<Comment> commentList = new ArrayList<Comment>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Comment comment = new Comment();
				comment.setCommentId(object.getInt("commentId"));
				comment.setTopicObj(object.getInt("topicObj"));
				comment.setContent(object.getString("content"));
				comment.setStudentObj(object.getString("studentObj"));
				comment.setCommentTime(object.getString("commentTime"));
				commentList.add(comment);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return commentList;
	}

	/* 更新评论 */
	public String UpdateComment(Comment comment) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("commentId", comment.getCommentId() + "");
		params.put("topicObj", comment.getTopicObj() + "");
		params.put("content", comment.getContent());
		params.put("studentObj", comment.getStudentObj());
		params.put("commentTime", comment.getCommentTime());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "CommentServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除评论 */
	public String DeleteComment(int commentId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("commentId", commentId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "CommentServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "评论信息删除失败!";
		}
	}

	/* 根据记录编号获取评论对象 */
	public Comment GetComment(int commentId)  {
		List<Comment> commentList = new ArrayList<Comment>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("commentId", commentId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "CommentServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Comment comment = new Comment();
				comment.setCommentId(object.getInt("commentId"));
				comment.setTopicObj(object.getInt("topicObj"));
				comment.setContent(object.getString("content"));
				comment.setStudentObj(object.getString("studentObj"));
				comment.setCommentTime(object.getString("commentTime"));
				commentList.add(comment);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = commentList.size();
		if(size>0) return commentList.get(0); 
		else return null; 
	}
}
