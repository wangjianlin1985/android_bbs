package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.CommentDAO;
import com.mobileserver.domain.Comment;

import org.json.JSONStringer;

public class CommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*构造评论业务层对象*/
	private CommentDAO commentDAO = new CommentDAO();

	/*默认构造函数*/
	public CommentServlet() {
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
			/*获取查询评论的参数信息*/
			int topicObj = 0;
			if (request.getParameter("topicObj") != null)
				topicObj = Integer.parseInt(request.getParameter("topicObj"));
			String studentObj = "";
			if (request.getParameter("studentObj") != null)
				studentObj = request.getParameter("studentObj");

			/*调用业务逻辑层执行评论查询*/
			List<Comment> commentList = commentDAO.QueryComment(topicObj,studentObj);

			/*2种数据传输格式，一种是xml文件格式：将查询的结果集通过xml格式传输给客户端
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<Comments>").append("\r\n");
			for (int i = 0; i < commentList.size(); i++) {
				sb.append("	<Comment>").append("\r\n")
				.append("		<commentId>")
				.append(commentList.get(i).getCommentId())
				.append("</commentId>").append("\r\n")
				.append("		<topicObj>")
				.append(commentList.get(i).getTopicObj())
				.append("</topicObj>").append("\r\n")
				.append("		<content>")
				.append(commentList.get(i).getContent())
				.append("</content>").append("\r\n")
				.append("		<studentObj>")
				.append(commentList.get(i).getStudentObj())
				.append("</studentObj>").append("\r\n")
				.append("		<commentTime>")
				.append(commentList.get(i).getCommentTime())
				.append("</commentTime>").append("\r\n")
				.append("	</Comment>").append("\r\n");
			}
			sb.append("</Comments>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//第2种采用json格式(我们用这种)： 客户端查询的图书对象，返回json数据格式
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(Comment comment: commentList) {
				  stringer.object();
			  stringer.key("commentId").value(comment.getCommentId());
			  stringer.key("topicObj").value(comment.getTopicObj());
			  stringer.key("content").value(comment.getContent());
			  stringer.key("studentObj").value(comment.getStudentObj());
			  stringer.key("commentTime").value(comment.getCommentTime());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* 添加评论：获取评论参数，参数保存到新建的评论对象 */ 
			Comment comment = new Comment();
			int commentId = Integer.parseInt(request.getParameter("commentId"));
			comment.setCommentId(commentId);
			int topicObj = Integer.parseInt(request.getParameter("topicObj"));
			comment.setTopicObj(topicObj);
			String content = new String(request.getParameter("content").getBytes("iso-8859-1"), "UTF-8");
			comment.setContent(content);
			String studentObj = new String(request.getParameter("studentObj").getBytes("iso-8859-1"), "UTF-8");
			comment.setStudentObj(studentObj);
			String commentTime = new String(request.getParameter("commentTime").getBytes("iso-8859-1"), "UTF-8");
			comment.setCommentTime(commentTime);

			/* 调用业务层执行添加操作 */
			String result = commentDAO.AddComment(comment);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*删除评论：获取评论的记录编号*/
			int commentId = Integer.parseInt(request.getParameter("commentId"));
			/*调用业务逻辑层执行删除操作*/
			String result = commentDAO.DeleteComment(commentId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*将删除是否成功信息返回给客户端*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*更新评论之前先根据commentId查询某个评论*/
			int commentId = Integer.parseInt(request.getParameter("commentId"));
			Comment comment = commentDAO.GetComment(commentId);

			// 客户端查询的评论对象，返回json数据格式, 将List<Book>组织成JSON字符串
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("commentId").value(comment.getCommentId());
			  stringer.key("topicObj").value(comment.getTopicObj());
			  stringer.key("content").value(comment.getContent());
			  stringer.key("studentObj").value(comment.getStudentObj());
			  stringer.key("commentTime").value(comment.getCommentTime());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* 更新评论：获取评论参数，参数保存到新建的评论对象 */ 
			Comment comment = new Comment();
			int commentId = Integer.parseInt(request.getParameter("commentId"));
			comment.setCommentId(commentId);
			int topicObj = Integer.parseInt(request.getParameter("topicObj"));
			comment.setTopicObj(topicObj);
			String content = new String(request.getParameter("content").getBytes("iso-8859-1"), "UTF-8");
			comment.setContent(content);
			String studentObj = new String(request.getParameter("studentObj").getBytes("iso-8859-1"), "UTF-8");
			comment.setStudentObj(studentObj);
			String commentTime = new String(request.getParameter("commentTime").getBytes("iso-8859-1"), "UTF-8");
			comment.setCommentTime(commentTime);

			/* 调用业务层执行更新操作 */
			String result = commentDAO.UpdateComment(comment);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
