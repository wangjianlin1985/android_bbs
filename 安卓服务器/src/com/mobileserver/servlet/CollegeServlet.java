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

	/*构造学院信息业务层对象*/
	private CollegeDAO collegeDAO = new CollegeDAO();

	/*默认构造函数*/
	public CollegeServlet() {
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
			/*获取查询学院信息的参数信息*/

			/*调用业务逻辑层执行学院信息查询*/
			List<College> collegeList = collegeDAO.QueryCollege();

			/*2种数据传输格式，一种是xml文件格式：将查询的结果集通过xml格式传输给客户端
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
			//第2种采用json格式(我们用这种)： 客户端查询的图书对象，返回json数据格式
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
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* 添加学院信息：获取学院信息参数，参数保存到新建的学院信息对象 */ 
			College college = new College();
			String collegeNumber = new String(request.getParameter("collegeNumber").getBytes("iso-8859-1"), "UTF-8");
			college.setCollegeNumber(collegeNumber);
			String collegeName = new String(request.getParameter("collegeName").getBytes("iso-8859-1"), "UTF-8");
			college.setCollegeName(collegeName);

			/* 调用业务层执行添加操作 */
			String result = collegeDAO.AddCollege(college);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*删除学院信息：获取学院信息的学院编号*/
			String collegeNumber = new String(request.getParameter("collegeNumber").getBytes("iso-8859-1"), "UTF-8");
			/*调用业务逻辑层执行删除操作*/
			String result = collegeDAO.DeleteCollege(collegeNumber);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*将删除是否成功信息返回给客户端*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*更新学院信息之前先根据collegeNumber查询某个学院信息*/
			String collegeNumber = new String(request.getParameter("collegeNumber").getBytes("iso-8859-1"), "UTF-8");
			College college = collegeDAO.GetCollege(collegeNumber);

			// 客户端查询的学院信息对象，返回json数据格式, 将List<Book>组织成JSON字符串
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
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* 更新学院信息：获取学院信息参数，参数保存到新建的学院信息对象 */ 
			College college = new College();
			String collegeNumber = new String(request.getParameter("collegeNumber").getBytes("iso-8859-1"), "UTF-8");
			college.setCollegeNumber(collegeNumber);
			String collegeName = new String(request.getParameter("collegeName").getBytes("iso-8859-1"), "UTF-8");
			college.setCollegeName(collegeName);

			/* 调用业务层执行更新操作 */
			String result = collegeDAO.UpdateCollege(college);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
