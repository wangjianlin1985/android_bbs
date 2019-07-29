package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.ThumbUpDAO;
import com.mobileserver.domain.ThumbUp;

import org.json.JSONStringer;

public class ThumbUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*构造点赞信息业务层对象*/
	private ThumbUpDAO thumbUpDAO = new ThumbUpDAO();

	/*默认构造函数*/
	public ThumbUpServlet() {
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
			/*获取查询点赞信息的参数信息*/
			int topObj = 0;
			if (request.getParameter("topObj") != null)
				topObj = Integer.parseInt(request.getParameter("topObj"));
			String studentObj = "";
			if (request.getParameter("studentObj") != null)
				studentObj = request.getParameter("studentObj");

			/*调用业务逻辑层执行点赞信息查询*/
			List<ThumbUp> thumbUpList = thumbUpDAO.QueryThumbUp(topObj,studentObj);

			/*2种数据传输格式，一种是xml文件格式：将查询的结果集通过xml格式传输给客户端
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<ThumbUps>").append("\r\n");
			for (int i = 0; i < thumbUpList.size(); i++) {
				sb.append("	<ThumbUp>").append("\r\n")
				.append("		<thumbUpId>")
				.append(thumbUpList.get(i).getThumbUpId())
				.append("</thumbUpId>").append("\r\n")
				.append("		<topObj>")
				.append(thumbUpList.get(i).getTopObj())
				.append("</topObj>").append("\r\n")
				.append("		<studentObj>")
				.append(thumbUpList.get(i).getStudentObj())
				.append("</studentObj>").append("\r\n")
				.append("		<thumpTime>")
				.append(thumbUpList.get(i).getThumpTime())
				.append("</thumpTime>").append("\r\n")
				.append("	</ThumbUp>").append("\r\n");
			}
			sb.append("</ThumbUps>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//第2种采用json格式(我们用这种)： 客户端查询的图书对象，返回json数据格式
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(ThumbUp thumbUp: thumbUpList) {
				  stringer.object();
			  stringer.key("thumbUpId").value(thumbUp.getThumbUpId());
			  stringer.key("topObj").value(thumbUp.getTopObj());
			  stringer.key("studentObj").value(thumbUp.getStudentObj());
			  stringer.key("thumpTime").value(thumbUp.getThumpTime());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* 添加点赞信息：获取点赞信息参数，参数保存到新建的点赞信息对象 */ 
			ThumbUp thumbUp = new ThumbUp();
			int thumbUpId = Integer.parseInt(request.getParameter("thumbUpId"));
			thumbUp.setThumbUpId(thumbUpId);
			int topObj = Integer.parseInt(request.getParameter("topObj"));
			thumbUp.setTopObj(topObj);
			String studentObj = new String(request.getParameter("studentObj").getBytes("iso-8859-1"), "UTF-8");
			thumbUp.setStudentObj(studentObj);
			String thumpTime = new String(request.getParameter("thumpTime").getBytes("iso-8859-1"), "UTF-8");
			thumbUp.setThumpTime(thumpTime);

			/* 调用业务层执行添加操作 */
			String result = thumbUpDAO.AddThumbUp(thumbUp);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*删除点赞信息：获取点赞信息的记录编号*/
			int thumbUpId = Integer.parseInt(request.getParameter("thumbUpId"));
			/*调用业务逻辑层执行删除操作*/
			String result = thumbUpDAO.DeleteThumbUp(thumbUpId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*将删除是否成功信息返回给客户端*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*更新点赞信息之前先根据thumbUpId查询某个点赞信息*/
			int thumbUpId = Integer.parseInt(request.getParameter("thumbUpId"));
			ThumbUp thumbUp = thumbUpDAO.GetThumbUp(thumbUpId);

			// 客户端查询的点赞信息对象，返回json数据格式, 将List<Book>组织成JSON字符串
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("thumbUpId").value(thumbUp.getThumbUpId());
			  stringer.key("topObj").value(thumbUp.getTopObj());
			  stringer.key("studentObj").value(thumbUp.getStudentObj());
			  stringer.key("thumpTime").value(thumbUp.getThumpTime());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* 更新点赞信息：获取点赞信息参数，参数保存到新建的点赞信息对象 */ 
			ThumbUp thumbUp = new ThumbUp();
			int thumbUpId = Integer.parseInt(request.getParameter("thumbUpId"));
			thumbUp.setThumbUpId(thumbUpId);
			int topObj = Integer.parseInt(request.getParameter("topObj"));
			thumbUp.setTopObj(topObj);
			String studentObj = new String(request.getParameter("studentObj").getBytes("iso-8859-1"), "UTF-8");
			thumbUp.setStudentObj(studentObj);
			String thumpTime = new String(request.getParameter("thumpTime").getBytes("iso-8859-1"), "UTF-8");
			thumbUp.setThumpTime(thumpTime);

			/* 调用业务层执行更新操作 */
			String result = thumbUpDAO.UpdateThumbUp(thumbUp);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
