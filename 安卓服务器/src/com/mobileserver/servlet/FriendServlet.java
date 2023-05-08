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

	/*���������Ϣҵ������*/
	private FriendDAO friendDAO = new FriendDAO();

	/*Ĭ�Ϲ��캯��*/
	public FriendServlet() {
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
			String studentObj1 = "";
			if (request.getParameter("studentObj1") != null)
				studentObj1 = request.getParameter("studentObj1");
			String studentObj2 = "";
			if (request.getParameter("studentObj2") != null)
				studentObj2 = request.getParameter("studentObj2");

			/*����ҵ���߼���ִ�к�����Ϣ��ѯ*/
			List<Friend> friendList = friendDAO.QueryFriend(studentObj1,studentObj2);

			/*2�����ݴ����ʽ��һ����xml�ļ���ʽ������ѯ�Ľ����ͨ��xml��ʽ������ͻ���
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
			//��2�ֲ���json��ʽ(����������)�� �ͻ��˲�ѯ��ͼ����󣬷���json���ݸ�ʽ
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* ��Ӻ�����Ϣ����ȡ������Ϣ�������������浽�½��ĺ�����Ϣ���� */ 
			Friend friend = new Friend();
			int friendId = Integer.parseInt(request.getParameter("friendId"));
			friend.setFriendId(friendId);
			String studentObj1 = new String(request.getParameter("studentObj1").getBytes("iso-8859-1"), "UTF-8");
			friend.setStudentObj1(studentObj1);
			String studentObj2 = new String(request.getParameter("studentObj2").getBytes("iso-8859-1"), "UTF-8");
			friend.setStudentObj2(studentObj2);
			String addTime = new String(request.getParameter("addTime").getBytes("iso-8859-1"), "UTF-8");
			friend.setAddTime(addTime);

			/* ����ҵ���ִ����Ӳ��� */
			String result = friendDAO.AddFriend(friend);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*ɾ��������Ϣ����ȡ������Ϣ�ļ�¼���*/
			int friendId = Integer.parseInt(request.getParameter("friendId"));
			/*����ҵ���߼���ִ��ɾ������*/
			String result = friendDAO.DeleteFriend(friendId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*��ɾ���Ƿ�ɹ���Ϣ���ظ��ͻ���*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*���º�����Ϣ֮ǰ�ȸ���friendId��ѯĳ��������Ϣ*/
			int friendId = Integer.parseInt(request.getParameter("friendId"));
			Friend friend = friendDAO.GetFriend(friendId);

			// �ͻ��˲�ѯ�ĺ�����Ϣ���󣬷���json���ݸ�ʽ, ��List<Book>��֯��JSON�ַ���
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
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* ���º�����Ϣ����ȡ������Ϣ�������������浽�½��ĺ�����Ϣ���� */ 
			Friend friend = new Friend();
			int friendId = Integer.parseInt(request.getParameter("friendId"));
			friend.setFriendId(friendId);
			String studentObj1 = new String(request.getParameter("studentObj1").getBytes("iso-8859-1"), "UTF-8");
			friend.setStudentObj1(studentObj1);
			String studentObj2 = new String(request.getParameter("studentObj2").getBytes("iso-8859-1"), "UTF-8");
			friend.setStudentObj2(studentObj2);
			String addTime = new String(request.getParameter("addTime").getBytes("iso-8859-1"), "UTF-8");
			friend.setAddTime(addTime);

			/* ����ҵ���ִ�и��²��� */
			String result = friendDAO.UpdateFriend(friend);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
