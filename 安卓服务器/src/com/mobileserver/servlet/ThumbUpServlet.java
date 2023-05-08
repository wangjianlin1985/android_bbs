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

	/*���������Ϣҵ������*/
	private ThumbUpDAO thumbUpDAO = new ThumbUpDAO();

	/*Ĭ�Ϲ��캯��*/
	public ThumbUpServlet() {
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
			/*��ȡ��ѯ������Ϣ�Ĳ�����Ϣ*/
			int topObj = 0;
			if (request.getParameter("topObj") != null)
				topObj = Integer.parseInt(request.getParameter("topObj"));
			String studentObj = "";
			if (request.getParameter("studentObj") != null)
				studentObj = request.getParameter("studentObj");

			/*����ҵ���߼���ִ�е�����Ϣ��ѯ*/
			List<ThumbUp> thumbUpList = thumbUpDAO.QueryThumbUp(topObj,studentObj);

			/*2�����ݴ����ʽ��һ����xml�ļ���ʽ������ѯ�Ľ����ͨ��xml��ʽ������ͻ���
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
			//��2�ֲ���json��ʽ(����������)�� �ͻ��˲�ѯ��ͼ����󣬷���json���ݸ�ʽ
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* ��ӵ�����Ϣ����ȡ������Ϣ�������������浽�½��ĵ�����Ϣ���� */ 
			ThumbUp thumbUp = new ThumbUp();
			int thumbUpId = Integer.parseInt(request.getParameter("thumbUpId"));
			thumbUp.setThumbUpId(thumbUpId);
			int topObj = Integer.parseInt(request.getParameter("topObj"));
			thumbUp.setTopObj(topObj);
			String studentObj = new String(request.getParameter("studentObj").getBytes("iso-8859-1"), "UTF-8");
			thumbUp.setStudentObj(studentObj);
			String thumpTime = new String(request.getParameter("thumpTime").getBytes("iso-8859-1"), "UTF-8");
			thumbUp.setThumpTime(thumpTime);

			/* ����ҵ���ִ����Ӳ��� */
			String result = thumbUpDAO.AddThumbUp(thumbUp);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*ɾ��������Ϣ����ȡ������Ϣ�ļ�¼���*/
			int thumbUpId = Integer.parseInt(request.getParameter("thumbUpId"));
			/*����ҵ���߼���ִ��ɾ������*/
			String result = thumbUpDAO.DeleteThumbUp(thumbUpId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*��ɾ���Ƿ�ɹ���Ϣ���ظ��ͻ���*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*���µ�����Ϣ֮ǰ�ȸ���thumbUpId��ѯĳ��������Ϣ*/
			int thumbUpId = Integer.parseInt(request.getParameter("thumbUpId"));
			ThumbUp thumbUp = thumbUpDAO.GetThumbUp(thumbUpId);

			// �ͻ��˲�ѯ�ĵ�����Ϣ���󣬷���json���ݸ�ʽ, ��List<Book>��֯��JSON�ַ���
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* ���µ�����Ϣ����ȡ������Ϣ�������������浽�½��ĵ�����Ϣ���� */ 
			ThumbUp thumbUp = new ThumbUp();
			int thumbUpId = Integer.parseInt(request.getParameter("thumbUpId"));
			thumbUp.setThumbUpId(thumbUpId);
			int topObj = Integer.parseInt(request.getParameter("topObj"));
			thumbUp.setTopObj(topObj);
			String studentObj = new String(request.getParameter("studentObj").getBytes("iso-8859-1"), "UTF-8");
			thumbUp.setStudentObj(studentObj);
			String thumpTime = new String(request.getParameter("thumpTime").getBytes("iso-8859-1"), "UTF-8");
			thumbUp.setThumpTime(thumpTime);

			/* ����ҵ���ִ�и��²��� */
			String result = thumbUpDAO.UpdateThumbUp(thumbUp);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
