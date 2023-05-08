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

	/*��������ҵ������*/
	private CommentDAO commentDAO = new CommentDAO();

	/*Ĭ�Ϲ��캯��*/
	public CommentServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/*��ȡaction����������action��ִֵ�в�ͬ��ҵ����*/
		String action = request.getParameter("action");
		if (action.equals("query")) {
			/*��ȡ��ѯ���۵Ĳ�����Ϣ*/
			int topicObj = 0;
			if (request.getParameter("topicObj") != null)
				topicObj = Integer.parseInt(request.getParameter("topicObj"));
			String studentObj = "";
			if (request.getParameter("studentObj") != null)
				studentObj = request.getParameter("studentObj");

			/*����ҵ���߼���ִ�����۲�ѯ*/
			List<Comment> commentList = commentDAO.QueryComment(topicObj,studentObj);

			/*2�����ݴ����ʽ��һ����xml�ļ���ʽ������ѯ�Ľ����ͨ��xml��ʽ������ͻ���
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
			//��2�ֲ���json��ʽ(����������)�� �ͻ��˲�ѯ��ͼ����󣬷���json���ݸ�ʽ
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* ������ۣ���ȡ���۲������������浽�½������۶��� */ 
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

			/* ����ҵ���ִ����Ӳ��� */
			String result = commentDAO.AddComment(comment);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*ɾ�����ۣ���ȡ���۵ļ�¼���*/
			int commentId = Integer.parseInt(request.getParameter("commentId"));
			/*����ҵ���߼���ִ��ɾ������*/
			String result = commentDAO.DeleteComment(commentId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*��ɾ���Ƿ�ɹ���Ϣ���ظ��ͻ���*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*��������֮ǰ�ȸ���commentId��ѯĳ������*/
			int commentId = Integer.parseInt(request.getParameter("commentId"));
			Comment comment = commentDAO.GetComment(commentId);

			// �ͻ��˲�ѯ�����۶��󣬷���json���ݸ�ʽ, ��List<Book>��֯��JSON�ַ���
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* �������ۣ���ȡ���۲������������浽�½������۶��� */ 
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

			/* ����ҵ���ִ�и��²��� */
			String result = commentDAO.UpdateComment(comment);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
