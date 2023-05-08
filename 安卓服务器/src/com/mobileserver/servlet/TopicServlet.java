package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.TopicDAO;
import com.mobileserver.domain.Topic;

import org.json.JSONStringer;

public class TopicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*���컰��ҵ������*/
	private TopicDAO topicDAO = new TopicDAO();

	/*Ĭ�Ϲ��캯��*/
	public TopicServlet() {
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
			/*��ȡ��ѯ����Ĳ�����Ϣ*/
			String title = request.getParameter("title");
			title = title == null ? "" : new String(request.getParameter(
					"title").getBytes("iso-8859-1"), "UTF-8");
			int topicClass = 0;
			if (request.getParameter("topicClass") != null)
				topicClass = Integer.parseInt(request.getParameter("topicClass"));
			String studentObj = "";
			if (request.getParameter("studentObj") != null)
				studentObj = request.getParameter("studentObj");

			/*����ҵ���߼���ִ�л����ѯ*/
			List<Topic> topicList = topicDAO.QueryTopic(title,topicClass,studentObj);

			/*2�����ݴ����ʽ��һ����xml�ļ���ʽ������ѯ�Ľ����ͨ��xml��ʽ������ͻ���
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<Topics>").append("\r\n");
			for (int i = 0; i < topicList.size(); i++) {
				sb.append("	<Topic>").append("\r\n")
				.append("		<topicId>")
				.append(topicList.get(i).getTopicId())
				.append("</topicId>").append("\r\n")
				.append("		<title>")
				.append(topicList.get(i).getTitle())
				.append("</title>").append("\r\n")
				.append("		<topicClass>")
				.append(topicList.get(i).getTopicClass())
				.append("</topicClass>").append("\r\n")
				.append("		<photo>")
				.append(topicList.get(i).getPhoto())
				.append("</photo>").append("\r\n")
				.append("		<content>")
				.append(topicList.get(i).getContent())
				.append("</content>").append("\r\n")
				.append("		<studentObj>")
				.append(topicList.get(i).getStudentObj())
				.append("</studentObj>").append("\r\n")
				.append("		<addTime>")
				.append(topicList.get(i).getAddTime())
				.append("</addTime>").append("\r\n")
				.append("	</Topic>").append("\r\n");
			}
			sb.append("</Topics>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//��2�ֲ���json��ʽ(����������)�� �ͻ��˲�ѯ��ͼ����󣬷���json���ݸ�ʽ
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(Topic topic: topicList) {
				  stringer.object();
			  stringer.key("topicId").value(topic.getTopicId());
			  stringer.key("title").value(topic.getTitle());
			  stringer.key("topicClass").value(topic.getTopicClass());
			  stringer.key("photo").value(topic.getPhoto());
			  stringer.key("content").value(topic.getContent());
			  stringer.key("studentObj").value(topic.getStudentObj());
			  stringer.key("addTime").value(topic.getAddTime());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* ��ӻ��⣺��ȡ����������������浽�½��Ļ������ */ 
			Topic topic = new Topic();
			int topicId = Integer.parseInt(request.getParameter("topicId"));
			topic.setTopicId(topicId);
			String title = new String(request.getParameter("title").getBytes("iso-8859-1"), "UTF-8");
			topic.setTitle(title);
			int topicClass = Integer.parseInt(request.getParameter("topicClass"));
			topic.setTopicClass(topicClass);
			String photo = new String(request.getParameter("photo").getBytes("iso-8859-1"), "UTF-8");
			topic.setPhoto(photo);
			String content = new String(request.getParameter("content").getBytes("iso-8859-1"), "UTF-8");
			topic.setContent(content);
			String studentObj = new String(request.getParameter("studentObj").getBytes("iso-8859-1"), "UTF-8");
			topic.setStudentObj(studentObj);
			String addTime = new String(request.getParameter("addTime").getBytes("iso-8859-1"), "UTF-8");
			topic.setAddTime(addTime);

			/* ����ҵ���ִ����Ӳ��� */
			String result = topicDAO.AddTopic(topic);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*ɾ�����⣺��ȡ����Ļ���id*/
			int topicId = Integer.parseInt(request.getParameter("topicId"));
			/*����ҵ���߼���ִ��ɾ������*/
			String result = topicDAO.DeleteTopic(topicId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*��ɾ���Ƿ�ɹ���Ϣ���ظ��ͻ���*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*���»���֮ǰ�ȸ���topicId��ѯĳ������*/
			int topicId = Integer.parseInt(request.getParameter("topicId"));
			Topic topic = topicDAO.GetTopic(topicId);

			// �ͻ��˲�ѯ�Ļ�����󣬷���json���ݸ�ʽ, ��List<Book>��֯��JSON�ַ���
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("topicId").value(topic.getTopicId());
			  stringer.key("title").value(topic.getTitle());
			  stringer.key("topicClass").value(topic.getTopicClass());
			  stringer.key("photo").value(topic.getPhoto());
			  stringer.key("content").value(topic.getContent());
			  stringer.key("studentObj").value(topic.getStudentObj());
			  stringer.key("addTime").value(topic.getAddTime());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* ���»��⣺��ȡ����������������浽�½��Ļ������ */ 
			Topic topic = new Topic();
			int topicId = Integer.parseInt(request.getParameter("topicId"));
			topic.setTopicId(topicId);
			String title = new String(request.getParameter("title").getBytes("iso-8859-1"), "UTF-8");
			topic.setTitle(title);
			int topicClass = Integer.parseInt(request.getParameter("topicClass"));
			topic.setTopicClass(topicClass);
			String photo = new String(request.getParameter("photo").getBytes("iso-8859-1"), "UTF-8");
			topic.setPhoto(photo);
			String content = new String(request.getParameter("content").getBytes("iso-8859-1"), "UTF-8");
			topic.setContent(content);
			String studentObj = new String(request.getParameter("studentObj").getBytes("iso-8859-1"), "UTF-8");
			topic.setStudentObj(studentObj);
			String addTime = new String(request.getParameter("addTime").getBytes("iso-8859-1"), "UTF-8");
			topic.setAddTime(addTime);

			/* ����ҵ���ִ�и��²��� */
			String result = topicDAO.UpdateTopic(topic);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
