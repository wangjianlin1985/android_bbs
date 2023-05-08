package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.TopicClassDAO;
import com.mobileserver.domain.TopicClass;

import org.json.JSONStringer;

public class TopicClassServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*构造话题分类业务层对象*/
	private TopicClassDAO topicClassDAO = new TopicClassDAO();

	/*默认构造函数*/
	public TopicClassServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/*获取action参数，根据action的值执行不同的业务处理*/
		String action = request.getParameter("action");
		if (action.equals("query")) {
			/*获取查询话题分类的参数信息*/

			/*调用业务逻辑层执行话题分类查询*/
			List<TopicClass> topicClassList = topicClassDAO.QueryTopicClass();

			/*2种数据传输格式，一种是xml文件格式：将查询的结果集通过xml格式传输给客户端
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<TopicClasss>").append("\r\n");
			for (int i = 0; i < topicClassList.size(); i++) {
				sb.append("	<TopicClass>").append("\r\n")
				.append("		<topicClassId>")
				.append(topicClassList.get(i).getTopicClassId())
				.append("</topicClassId>").append("\r\n")
				.append("		<topicClassName>")
				.append(topicClassList.get(i).getTopicClassName())
				.append("</topicClassName>").append("\r\n")
				.append("	</TopicClass>").append("\r\n");
			}
			sb.append("</TopicClasss>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//第2种采用json格式(我们用这种)： 客户端查询的图书对象，返回json数据格式
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(TopicClass topicClass: topicClassList) {
				  stringer.object();
			  stringer.key("topicClassId").value(topicClass.getTopicClassId());
			  stringer.key("topicClassName").value(topicClass.getTopicClassName());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* 添加话题分类：获取话题分类参数，参数保存到新建的话题分类对象 */ 
			TopicClass topicClass = new TopicClass();
			int topicClassId = Integer.parseInt(request.getParameter("topicClassId"));
			topicClass.setTopicClassId(topicClassId);
			String topicClassName = new String(request.getParameter("topicClassName").getBytes("iso-8859-1"), "UTF-8");
			topicClass.setTopicClassName(topicClassName);

			/* 调用业务层执行添加操作 */
			String result = topicClassDAO.AddTopicClass(topicClass);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*删除话题分类：获取话题分类的分类id*/
			int topicClassId = Integer.parseInt(request.getParameter("topicClassId"));
			/*调用业务逻辑层执行删除操作*/
			String result = topicClassDAO.DeleteTopicClass(topicClassId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*将删除是否成功信息返回给客户端*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*更新话题分类之前先根据topicClassId查询某个话题分类*/
			int topicClassId = Integer.parseInt(request.getParameter("topicClassId"));
			TopicClass topicClass = topicClassDAO.GetTopicClass(topicClassId);

			// 客户端查询的话题分类对象，返回json数据格式, 将List<Book>组织成JSON字符串
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("topicClassId").value(topicClass.getTopicClassId());
			  stringer.key("topicClassName").value(topicClass.getTopicClassName());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* 更新话题分类：获取话题分类参数，参数保存到新建的话题分类对象 */ 
			TopicClass topicClass = new TopicClass();
			int topicClassId = Integer.parseInt(request.getParameter("topicClassId"));
			topicClass.setTopicClassId(topicClassId);
			String topicClassName = new String(request.getParameter("topicClassName").getBytes("iso-8859-1"), "UTF-8");
			topicClass.setTopicClassName(topicClassName);

			/* 调用业务层执行更新操作 */
			String result = topicClassDAO.UpdateTopicClass(topicClass);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
