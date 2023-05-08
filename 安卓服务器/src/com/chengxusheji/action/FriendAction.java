package com.chengxusheji.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import com.opensymphony.xwork2.ActionContext;
import com.chengxusheji.dao.FriendDAO;
import com.chengxusheji.domain.Friend;
import com.chengxusheji.dao.StudentDAO;
import com.chengxusheji.domain.Student;
import com.chengxusheji.dao.StudentDAO;
import com.chengxusheji.domain.Student;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class FriendAction extends BaseAction {

    /*�������Ҫ��ѯ������: ѧ��1*/
    private Student studentObj1;
    public void setStudentObj1(Student studentObj1) {
        this.studentObj1 = studentObj1;
    }
    public Student getStudentObj1() {
        return this.studentObj1;
    }

    /*�������Ҫ��ѯ������: ����*/
    private Student studentObj2;
    public void setStudentObj2(Student studentObj2) {
        this.studentObj2 = studentObj2;
    }
    public Student getStudentObj2() {
        return this.studentObj2;
    }

    /*��ǰ�ڼ�ҳ*/
    private int currentPage;
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getCurrentPage() {
        return currentPage;
    }

    /*һ������ҳ*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    private int friendId;
    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }
    public int getFriendId() {
        return friendId;
    }

    /*��ǰ��ѯ���ܼ�¼��Ŀ*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*ҵ������*/
    @Resource StudentDAO studentDAO;
    @Resource FriendDAO friendDAO;

    /*��������Friend����*/
    private Friend friend;
    public void setFriend(Friend friend) {
        this.friend = friend;
    }
    public Friend getFriend() {
        return this.friend;
    }

    /*��ת�����Friend��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�Student��Ϣ*/
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        return "add_view";
    }

    /*���Friend��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddFriend() {
        ActionContext ctx = ActionContext.getContext();
        try {
            Student studentObj1 = studentDAO.GetStudentByStudentNumber(friend.getStudentObj1().getStudentNumber());
            friend.setStudentObj1(studentObj1);
            Student studentObj2 = studentDAO.GetStudentByStudentNumber(friend.getStudentObj2().getStudentNumber());
            friend.setStudentObj2(studentObj2);
            friendDAO.AddFriend(friend);
            ctx.put("message",  java.net.URLEncoder.encode("Friend��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Friend���ʧ��!"));
            return "error";
        }
    }

    /*��ѯFriend��Ϣ*/
    public String QueryFriend() {
        if(currentPage == 0) currentPage = 1;
        List<Friend> friendList = friendDAO.QueryFriendInfo(studentObj1, studentObj2, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        friendDAO.CalculateTotalPageAndRecordNumber(studentObj1, studentObj2);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = friendDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = friendDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("friendList",  friendList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("studentObj1", studentObj1);
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        ctx.put("studentObj2", studentObj2);
        return "query_view";
    }

    /*��̨������excel*/
    public String QueryFriendOutputToExcel() { 
        List<Friend> friendList = friendDAO.QueryFriendInfo(studentObj1,studentObj2);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Friend��Ϣ��¼"; 
        String[] headers = { "��¼���","ѧ��1","����","���ʱ��"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<friendList.size();i++) {
        	Friend friend = friendList.get(i); 
        	dataset.add(new String[]{friend.getFriendId() + "",friend.getStudentObj1().getStudentName(),
friend.getStudentObj2().getStudentName(),
friend.getAddTime()});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		HttpServletResponse response = null;//����һ��HttpServletResponse���� 
		OutputStream out = null;//����һ����������� 
		try { 
			response = ServletActionContext.getResponse();//��ʼ��HttpServletResponse���� 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"Friend.xls");//filename�����ص�xls���������������Ӣ�� 
			response.setContentType("application/msexcel;charset=UTF-8");//�������� 
			response.setHeader("Pragma","No-cache");//����ͷ 
			response.setHeader("Cache-Control","no-cache");//����ͷ 
			response.setDateHeader("Expires", 0);//��������ͷ  
			String rootPath = ServletActionContext.getServletContext().getRealPath("/");
			ex.exportExcel(rootPath,title,headers, dataset, out);
			out.flush();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}finally{
			try{
				if(out!=null){ 
					out.close(); 
				}
			}catch(IOException e){ 
				e.printStackTrace(); 
			} 
		}
		return null;
    }
    /*ǰ̨��ѯFriend��Ϣ*/
    public String FrontQueryFriend() {
        if(currentPage == 0) currentPage = 1;
        List<Friend> friendList = friendDAO.QueryFriendInfo(studentObj1, studentObj2, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        friendDAO.CalculateTotalPageAndRecordNumber(studentObj1, studentObj2);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = friendDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = friendDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("friendList",  friendList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("studentObj1", studentObj1);
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        ctx.put("studentObj2", studentObj2);
        return "front_query_view";
    }

    /*��ѯҪ�޸ĵ�Friend��Ϣ*/
    public String ModifyFriendQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������friendId��ȡFriend����*/
        Friend friend = friendDAO.GetFriendByFriendId(friendId);

        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        ctx.put("friend",  friend);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�Friend��Ϣ*/
    public String FrontShowFriendQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������friendId��ȡFriend����*/
        Friend friend = friendDAO.GetFriendByFriendId(friendId);

        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        ctx.put("friend",  friend);
        return "front_show_view";
    }

    /*�����޸�Friend��Ϣ*/
    public String ModifyFriend() {
        ActionContext ctx = ActionContext.getContext();
        try {
            Student studentObj1 = studentDAO.GetStudentByStudentNumber(friend.getStudentObj1().getStudentNumber());
            friend.setStudentObj1(studentObj1);
            Student studentObj2 = studentDAO.GetStudentByStudentNumber(friend.getStudentObj2().getStudentNumber());
            friend.setStudentObj2(studentObj2);
            friendDAO.UpdateFriend(friend);
            ctx.put("message",  java.net.URLEncoder.encode("Friend��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Friend��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��Friend��Ϣ*/
    public String DeleteFriend() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            friendDAO.DeleteFriend(friendId);
            ctx.put("message",  java.net.URLEncoder.encode("Friendɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Friendɾ��ʧ��!"));
            return "error";
        }
    }

}
