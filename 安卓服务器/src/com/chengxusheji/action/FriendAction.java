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

    /*界面层需要查询的属性: 学生1*/
    private Student studentObj1;
    public void setStudentObj1(Student studentObj1) {
        this.studentObj1 = studentObj1;
    }
    public Student getStudentObj1() {
        return this.studentObj1;
    }

    /*界面层需要查询的属性: 好友*/
    private Student studentObj2;
    public void setStudentObj2(Student studentObj2) {
        this.studentObj2 = studentObj2;
    }
    public Student getStudentObj2() {
        return this.studentObj2;
    }

    /*当前第几页*/
    private int currentPage;
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getCurrentPage() {
        return currentPage;
    }

    /*一共多少页*/
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

    /*当前查询的总记录数目*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*业务层对象*/
    @Resource StudentDAO studentDAO;
    @Resource FriendDAO friendDAO;

    /*待操作的Friend对象*/
    private Friend friend;
    public void setFriend(Friend friend) {
        this.friend = friend;
    }
    public Friend getFriend() {
        return this.friend;
    }

    /*跳转到添加Friend视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*查询所有的Student信息*/
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        return "add_view";
    }

    /*添加Friend信息*/
    @SuppressWarnings("deprecation")
    public String AddFriend() {
        ActionContext ctx = ActionContext.getContext();
        try {
            Student studentObj1 = studentDAO.GetStudentByStudentNumber(friend.getStudentObj1().getStudentNumber());
            friend.setStudentObj1(studentObj1);
            Student studentObj2 = studentDAO.GetStudentByStudentNumber(friend.getStudentObj2().getStudentNumber());
            friend.setStudentObj2(studentObj2);
            friendDAO.AddFriend(friend);
            ctx.put("message",  java.net.URLEncoder.encode("Friend添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Friend添加失败!"));
            return "error";
        }
    }

    /*查询Friend信息*/
    public String QueryFriend() {
        if(currentPage == 0) currentPage = 1;
        List<Friend> friendList = friendDAO.QueryFriendInfo(studentObj1, studentObj2, currentPage);
        /*计算总的页数和总的记录数*/
        friendDAO.CalculateTotalPageAndRecordNumber(studentObj1, studentObj2);
        /*获取到总的页码数目*/
        totalPage = friendDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*后台导出到excel*/
    public String QueryFriendOutputToExcel() { 
        List<Friend> friendList = friendDAO.QueryFriendInfo(studentObj1,studentObj2);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Friend信息记录"; 
        String[] headers = { "记录编号","学生1","好友","添加时间"};
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
		HttpServletResponse response = null;//创建一个HttpServletResponse对象 
		OutputStream out = null;//创建一个输出流对象 
		try { 
			response = ServletActionContext.getResponse();//初始化HttpServletResponse对象 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"Friend.xls");//filename是下载的xls的名，建议最好用英文 
			response.setContentType("application/msexcel;charset=UTF-8");//设置类型 
			response.setHeader("Pragma","No-cache");//设置头 
			response.setHeader("Cache-Control","no-cache");//设置头 
			response.setDateHeader("Expires", 0);//设置日期头  
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
    /*前台查询Friend信息*/
    public String FrontQueryFriend() {
        if(currentPage == 0) currentPage = 1;
        List<Friend> friendList = friendDAO.QueryFriendInfo(studentObj1, studentObj2, currentPage);
        /*计算总的页数和总的记录数*/
        friendDAO.CalculateTotalPageAndRecordNumber(studentObj1, studentObj2);
        /*获取到总的页码数目*/
        totalPage = friendDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*查询要修改的Friend信息*/
    public String ModifyFriendQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键friendId获取Friend对象*/
        Friend friend = friendDAO.GetFriendByFriendId(friendId);

        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        ctx.put("friend",  friend);
        return "modify_view";
    }

    /*查询要修改的Friend信息*/
    public String FrontShowFriendQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键friendId获取Friend对象*/
        Friend friend = friendDAO.GetFriendByFriendId(friendId);

        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        ctx.put("friend",  friend);
        return "front_show_view";
    }

    /*更新修改Friend信息*/
    public String ModifyFriend() {
        ActionContext ctx = ActionContext.getContext();
        try {
            Student studentObj1 = studentDAO.GetStudentByStudentNumber(friend.getStudentObj1().getStudentNumber());
            friend.setStudentObj1(studentObj1);
            Student studentObj2 = studentDAO.GetStudentByStudentNumber(friend.getStudentObj2().getStudentNumber());
            friend.setStudentObj2(studentObj2);
            friendDAO.UpdateFriend(friend);
            ctx.put("message",  java.net.URLEncoder.encode("Friend信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Friend信息更新失败!"));
            return "error";
       }
   }

    /*删除Friend信息*/
    public String DeleteFriend() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            friendDAO.DeleteFriend(friendId);
            ctx.put("message",  java.net.URLEncoder.encode("Friend删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Friend删除失败!"));
            return "error";
        }
    }

}
