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

	/*���컰�����ҵ������*/
	private TopicClassDAO topicClassDAO = new TopicClassDAO();

	/*Ĭ�Ϲ��캯��*/
	public TopicClassServlet() {
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
			/*��ȡ��ѯ�������Ĳ�����Ϣ*/

			/*����ҵ���߼���ִ�л�������ѯ*/
			List<TopicClass> topicClassList = topicClassDAO.QueryTopicClass();

			/*2�����ݴ����ʽ��һ����xml�ļ���ʽ������ѯ�Ľ����ͨ��xml��ʽ������ͻ���
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
			//��2�ֲ���json��ʽ(����������)�� �ͻ��˲�ѯ��ͼ����󣬷���json���ݸ�ʽ
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* ��ӻ�����ࣺ��ȡ�������������������浽�½��Ļ��������� */ 
			TopicClass topicClass = new TopicClass();
			int topicClassId = Integer.parseInt(request.getParameter("topicClassId"));
			topicClass.setTopicClassId(topicClassId);
			String topicClassName = new String(request.getParameter("topicClassName").getBytes("iso-8859-1"), "UTF-8");
			topicClass.setTopicClassName(topicClassName);

			/* ����ҵ���ִ����Ӳ��� */
			String result = topicClassDAO.AddTopicClass(topicClass);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*ɾ��������ࣺ��ȡ�������ķ���id*/
			int topicClassId = Integer.parseInt(request.getParameter("topicClassId"));
			/*����ҵ���߼���ִ��ɾ������*/
			String result = topicClassDAO.DeleteTopicClass(topicClassId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*��ɾ���Ƿ�ɹ���Ϣ���ظ��ͻ���*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*���»������֮ǰ�ȸ���topicClassId��ѯĳ���������*/
			int topicClassId = Integer.parseInt(request.getParameter("topicClassId"));
			TopicClass topicClass = topicClassDAO.GetTopicClass(topicClassId);

			// �ͻ��˲�ѯ�Ļ��������󣬷���json���ݸ�ʽ, ��List<Book>��֯��JSON�ַ���
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* ���»�����ࣺ��ȡ�������������������浽�½��Ļ��������� */ 
			TopicClass topicClass = new TopicClass();
			int topicClassId = Integer.parseInt(request.getParameter("topicClassId"));
			topicClass.setTopicClassId(topicClassId);
			String topicClassName = new String(request.getParameter("topicClassName").getBytes("iso-8859-1"), "UTF-8");
			topicClass.setTopicClassName(topicClassName);

			/* ����ҵ���ִ�и��²��� */
			String result = topicClassDAO.UpdateTopicClass(topicClass);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
