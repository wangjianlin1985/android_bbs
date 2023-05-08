package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.SpecialInfoDAO;
import com.mobileserver.domain.SpecialInfo;

import org.json.JSONStringer;

public class SpecialInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*����רҵ��Ϣҵ������*/
	private SpecialInfoDAO specialInfoDAO = new SpecialInfoDAO();

	/*Ĭ�Ϲ��캯��*/
	public SpecialInfoServlet() {
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
			/*��ȡ��ѯרҵ��Ϣ�Ĳ�����Ϣ*/
			String collegeObj = "";
			if (request.getParameter("collegeObj") != null)
				collegeObj = request.getParameter("collegeObj");
			String specialNumber = request.getParameter("specialNumber");
			specialNumber = specialNumber == null ? "" : new String(request.getParameter(
					"specialNumber").getBytes("iso-8859-1"), "UTF-8");
			String specialName = request.getParameter("specialName");
			specialName = specialName == null ? "" : new String(request.getParameter(
					"specialName").getBytes("iso-8859-1"), "UTF-8");

			/*����ҵ���߼���ִ��רҵ��Ϣ��ѯ*/
			List<SpecialInfo> specialInfoList = specialInfoDAO.QuerySpecialInfo(collegeObj,specialNumber,specialName);

			/*2�����ݴ����ʽ��һ����xml�ļ���ʽ������ѯ�Ľ����ͨ��xml��ʽ������ͻ���
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<SpecialInfos>").append("\r\n");
			for (int i = 0; i < specialInfoList.size(); i++) {
				sb.append("	<SpecialInfo>").append("\r\n")
				.append("		<collegeObj>")
				.append(specialInfoList.get(i).getCollegeObj())
				.append("</collegeObj>").append("\r\n")
				.append("		<specialNumber>")
				.append(specialInfoList.get(i).getSpecialNumber())
				.append("</specialNumber>").append("\r\n")
				.append("		<specialName>")
				.append(specialInfoList.get(i).getSpecialName())
				.append("</specialName>").append("\r\n")
				.append("		<startDate>")
				.append(specialInfoList.get(i).getStartDate())
				.append("</startDate>").append("\r\n")
				.append("		<introduction>")
				.append(specialInfoList.get(i).getIntroduction())
				.append("</introduction>").append("\r\n")
				.append("	</SpecialInfo>").append("\r\n");
			}
			sb.append("</SpecialInfos>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//��2�ֲ���json��ʽ(����������)�� �ͻ��˲�ѯ��ͼ����󣬷���json���ݸ�ʽ
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(SpecialInfo specialInfo: specialInfoList) {
				  stringer.object();
			  stringer.key("collegeObj").value(specialInfo.getCollegeObj());
			  stringer.key("specialNumber").value(specialInfo.getSpecialNumber());
			  stringer.key("specialName").value(specialInfo.getSpecialName());
			  stringer.key("startDate").value(specialInfo.getStartDate());
			  stringer.key("introduction").value(specialInfo.getIntroduction());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* ���רҵ��Ϣ����ȡרҵ��Ϣ�������������浽�½���רҵ��Ϣ���� */ 
			SpecialInfo specialInfo = new SpecialInfo();
			String collegeObj = new String(request.getParameter("collegeObj").getBytes("iso-8859-1"), "UTF-8");
			specialInfo.setCollegeObj(collegeObj);
			String specialNumber = new String(request.getParameter("specialNumber").getBytes("iso-8859-1"), "UTF-8");
			specialInfo.setSpecialNumber(specialNumber);
			String specialName = new String(request.getParameter("specialName").getBytes("iso-8859-1"), "UTF-8");
			specialInfo.setSpecialName(specialName);
			Timestamp startDate = Timestamp.valueOf(request.getParameter("startDate"));
			specialInfo.setStartDate(startDate);
			String introduction = new String(request.getParameter("introduction").getBytes("iso-8859-1"), "UTF-8");
			specialInfo.setIntroduction(introduction);

			/* ����ҵ���ִ����Ӳ��� */
			String result = specialInfoDAO.AddSpecialInfo(specialInfo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*ɾ��רҵ��Ϣ����ȡרҵ��Ϣ��רҵ���*/
			String specialNumber = new String(request.getParameter("specialNumber").getBytes("iso-8859-1"), "UTF-8");
			/*����ҵ���߼���ִ��ɾ������*/
			String result = specialInfoDAO.DeleteSpecialInfo(specialNumber);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*��ɾ���Ƿ�ɹ���Ϣ���ظ��ͻ���*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*����רҵ��Ϣ֮ǰ�ȸ���specialNumber��ѯĳ��רҵ��Ϣ*/
			String specialNumber = new String(request.getParameter("specialNumber").getBytes("iso-8859-1"), "UTF-8");
			SpecialInfo specialInfo = specialInfoDAO.GetSpecialInfo(specialNumber);

			// �ͻ��˲�ѯ��רҵ��Ϣ���󣬷���json���ݸ�ʽ, ��List<Book>��֯��JSON�ַ���
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("collegeObj").value(specialInfo.getCollegeObj());
			  stringer.key("specialNumber").value(specialInfo.getSpecialNumber());
			  stringer.key("specialName").value(specialInfo.getSpecialName());
			  stringer.key("startDate").value(specialInfo.getStartDate());
			  stringer.key("introduction").value(specialInfo.getIntroduction());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* ����רҵ��Ϣ����ȡרҵ��Ϣ�������������浽�½���רҵ��Ϣ���� */ 
			SpecialInfo specialInfo = new SpecialInfo();
			String collegeObj = new String(request.getParameter("collegeObj").getBytes("iso-8859-1"), "UTF-8");
			specialInfo.setCollegeObj(collegeObj);
			String specialNumber = new String(request.getParameter("specialNumber").getBytes("iso-8859-1"), "UTF-8");
			specialInfo.setSpecialNumber(specialNumber);
			String specialName = new String(request.getParameter("specialName").getBytes("iso-8859-1"), "UTF-8");
			specialInfo.setSpecialName(specialName);
			Timestamp startDate = Timestamp.valueOf(request.getParameter("startDate"));
			specialInfo.setStartDate(startDate);
			String introduction = new String(request.getParameter("introduction").getBytes("iso-8859-1"), "UTF-8");
			specialInfo.setIntroduction(introduction);

			/* ����ҵ���ִ�и��²��� */
			String result = specialInfoDAO.UpdateSpecialInfo(specialInfo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
