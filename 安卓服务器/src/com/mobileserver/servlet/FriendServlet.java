package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.FriendDAO;
import com.mobileserver.domain.Friend;

import org.json.JSONStringer;

public class FriendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*构造好友信息业务层对象*/
	private FriendDAO friendDAO = new FriendDAO();

	/*默认构造函数*/
	public FriendServlet() {
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
			/*获取查询好友信息的参数信息*/
			String studentObj1 = "";
			if (request.getParameter("studentObj1") != null)
				studentObj1 = request.getParameter("studentObj1");
			String studentObj2 = "";
			if (request.getParameter("studentObj2") != null)
				studentObj2 = request.getParameter("studentObj2");

			/*调用业务逻辑层执行好友信息查询*/
			List<Friend> friendList = friendDAO.QueryFriend(studentObj1,studentObj2);

			/*2种数据传输格式，一种是xml文件格式：将查询的结果集通过xml格式传输给客户端
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<Friends>").append("\r\n");
			for (int i = 0; i < friendList.size(); i++) {
				sb.append("	<Friend>").append("\r\n")
				.append("		<friendId>")
				.append(friendList.get(i).getFriendId())
				.append("</friendId>").append("\r\n")
				.append("		<studentObj1>")
				.append(friendList.get(i).getStudentObj1())
				.append("</studentObj1>").append("\r\n")
				.append("		<studentObj2>")
				.append(friendList.get(i).getStudentObj2())
				.append("</studentObj2>").append("\r\n")
				.append("		<addTime>")
				.append(friendList.get(i).getAddTime())
				.append("</addTime>").append("\r\n")
				.append("	</Friend>").append("\r\n");
			}
			sb.append("</Friends>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//第2种采用json格式(我们用这种)： 客户端查询的图书对象，返回json数据格式
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(Friend friend: friendList) {
				  stringer.object();
			  stringer.key("friendId").value(friend.getFriendId());
			  stringer.key("studentObj1").value(friend.getStudentObj1());
			  stringer.key("studentObj2").value(friend.getStudentObj2());
			  stringer.key("addTime").value(friend.getAddTime());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* 添加好友信息：获取好友信息参数，参数保存到新建的好友信息对象 */ 
			Friend friend = new Friend();
			int friendId = Integer.parseInt(request.getParameter("friendId"));
			friend.setFriendId(friendId);
			String studentObj1 = new String(request.getParameter("studentObj1").getBytes("iso-8859-1"), "UTF-8");
			friend.setStudentObj1(studentObj1);
			String studentObj2 = new String(request.getParameter("studentObj2").getBytes("iso-8859-1"), "UTF-8");
			friend.setStudentObj2(studentObj2);
			String addTime = new String(request.getParameter("addTime").getBytes("iso-8859-1"), "UTF-8");
			friend.setAddTime(addTime);

			/* 调用业务层执行添加操作 */
			String result = friendDAO.AddFriend(friend);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*删除好友信息：获取好友信息的记录编号*/
			int friendId = Integer.parseInt(request.getParameter("friendId"));
			/*调用业务逻辑层执行删除操作*/
			String result = friendDAO.DeleteFriend(friendId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*将删除是否成功信息返回给客户端*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*更新好友信息之前先根据friendId查询某个好友信息*/
			int friendId = Integer.parseInt(request.getParameter("friendId"));
			Friend friend = friendDAO.GetFriend(friendId);

			// 客户端查询的好友信息对象，返回json数据格式, 将List<Book>组织成JSON字符串
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("friendId").value(friend.getFriendId());
			  stringer.key("studentObj1").value(friend.getStudentObj1());
			  stringer.key("studentObj2").value(friend.getStudentObj2());
			  stringer.key("addTime").value(friend.getAddTime());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* 更新好友信息：获取好友信息参数，参数保存到新建的好友信息对象 */ 
			Friend friend = new Friend();
			int friendId = Integer.parseInt(request.getParameter("friendId"));
			friend.setFriendId(friendId);
			String studentObj1 = new String(request.getParameter("studentObj1").getBytes("iso-8859-1"), "UTF-8");
			friend.setStudentObj1(studentObj1);
			String studentObj2 = new String(request.getParameter("studentObj2").getBytes("iso-8859-1"), "UTF-8");
			friend.setStudentObj2(studentObj2);
			String addTime = new String(request.getParameter("addTime").getBytes("iso-8859-1"), "UTF-8");
			friend.setAddTime(addTime);

			/* 调用业务层执行更新操作 */
			String result = friendDAO.UpdateFriend(friend);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
