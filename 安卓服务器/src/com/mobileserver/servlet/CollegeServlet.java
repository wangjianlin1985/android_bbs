package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.CollegeDAO;
import com.mobileserver.domain.College;

import org.json.JSONStringer;

public class CollegeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*����ѧԺ��Ϣҵ������*/
	private CollegeDAO collegeDAO = new CollegeDAO();

	/*Ĭ�Ϲ��캯��*/
	public CollegeServlet() {
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
			/*��ȡ��ѯѧԺ��Ϣ�Ĳ�����Ϣ*/

			/*����ҵ���߼���ִ��ѧԺ��Ϣ��ѯ*/
			List<College> collegeList = collegeDAO.QueryCollege();

			/*2�����ݴ����ʽ��һ����xml�ļ���ʽ������ѯ�Ľ����ͨ��xml��ʽ������ͻ���
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<Colleges>").append("\r\n");
			for (int i = 0; i < collegeList.size(); i++) {
				sb.append("	<College>").append("\r\n")
				.append("		<collegeNumber>")
				.append(collegeList.get(i).getCollegeNumber())
				.append("</collegeNumber>").append("\r\n")
				.append("		<collegeName>")
				.append(collegeList.get(i).getCollegeName())
				.append("</collegeName>").append("\r\n")
				.append("	</College>").append("\r\n");
			}
			sb.append("</Colleges>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//��2�ֲ���json��ʽ(����������)�� �ͻ��˲�ѯ��ͼ����󣬷���json���ݸ�ʽ
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(College college: collegeList) {
				  stringer.object();
			  stringer.key("collegeNumber").value(college.getCollegeNumber());
			  stringer.key("collegeName").value(college.getCollegeName());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* ���ѧԺ��Ϣ����ȡѧԺ��Ϣ�������������浽�½���ѧԺ��Ϣ���� */ 
			College college = new College();
			String collegeNumber = new String(request.getParameter("collegeNumber").getBytes("iso-8859-1"), "UTF-8");
			college.setCollegeNumber(collegeNumber);
			String collegeName = new String(request.getParameter("collegeName").getBytes("iso-8859-1"), "UTF-8");
			college.setCollegeName(collegeName);

			/* ����ҵ���ִ����Ӳ��� */
			String result = collegeDAO.AddCollege(college);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*ɾ��ѧԺ��Ϣ����ȡѧԺ��Ϣ��ѧԺ���*/
			String collegeNumber = new String(request.getParameter("collegeNumber").getBytes("iso-8859-1"), "UTF-8");
			/*����ҵ���߼���ִ��ɾ������*/
			String result = collegeDAO.DeleteCollege(collegeNumber);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*��ɾ���Ƿ�ɹ���Ϣ���ظ��ͻ���*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*����ѧԺ��Ϣ֮ǰ�ȸ���collegeNumber��ѯĳ��ѧԺ��Ϣ*/
			String collegeNumber = new String(request.getParameter("collegeNumber").getBytes("iso-8859-1"), "UTF-8");
			College college = collegeDAO.GetCollege(collegeNumber);

			// �ͻ��˲�ѯ��ѧԺ��Ϣ���󣬷���json���ݸ�ʽ, ��List<Book>��֯��JSON�ַ���
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("collegeNumber").value(college.getCollegeNumber());
			  stringer.key("collegeName").value(college.getCollegeName());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* ����ѧԺ��Ϣ����ȡѧԺ��Ϣ�������������浽�½���ѧԺ��Ϣ���� */ 
			College college = new College();
			String collegeNumber = new String(request.getParameter("collegeNumber").getBytes("iso-8859-1"), "UTF-8");
			college.setCollegeNumber(collegeNumber);
			String collegeName = new String(request.getParameter("collegeName").getBytes("iso-8859-1"), "UTF-8");
			college.setCollegeName(collegeName);

			/* ����ҵ���ִ�и��²��� */
			String result = collegeDAO.UpdateCollege(college);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
