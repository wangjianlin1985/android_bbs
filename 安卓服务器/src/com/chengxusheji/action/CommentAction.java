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
import com.chengxusheji.dao.CommentDAO;
import com.chengxusheji.domain.Comment;
import com.chengxusheji.dao.TopicDAO;
import com.chengxusheji.domain.Topic;
import com.chengxusheji.dao.StudentDAO;
import com.chengxusheji.domain.Student;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class CommentAction extends BaseAction {

    /*界面层需要查询的属性: 评论的话题*/
    private Topic topicObj;
    public void setTopicObj(Topic topicObj) {
        this.topicObj = topicObj;
    }
    public Topic getTopicObj() {
        return this.topicObj;
    }

    /*界面层需要查询的属性: 评论的学生*/
    private Student studentObj;
    public void setStudentObj(Student studentObj) {
        this.studentObj = studentObj;
    }
    public Student getStudentObj() {
        return this.studentObj;
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

    private int commentId;
    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }
    public int getCommentId() {
        return commentId;
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
    @Resource TopicDAO topicDAO;
    @Resource StudentDAO studentDAO;
    @Resource CommentDAO commentDAO;

    /*待操作的Comment对象*/
    private Comment comment;
    public void setComment(Comment comment) {
        this.comment = comment;
    }
    public Comment getComment() {
        return this.comment;
    }

    /*跳转到添加Comment视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*查询所有的Topic信息*/
        List<Topic> topicList = topicDAO.QueryAllTopicInfo();
        ctx.put("topicList", topicList);
        /*查询所有的Student信息*/
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        return "add_view";
    }

    /*添加Comment信息*/
    @SuppressWarnings("deprecation")
    public String AddComment() {
        ActionContext ctx = ActionContext.getContext();
        try {
            Topic topicObj = topicDAO.GetTopicByTopicId(comment.getTopicObj().getTopicId());
            comment.setTopicObj(topicObj);
            Student studentObj = studentDAO.GetStudentByStudentNumber(comment.getStudentObj().getStudentNumber());
            comment.setStudentObj(studentObj);
            commentDAO.AddComment(comment);
            ctx.put("message",  java.net.URLEncoder.encode("Comment添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Comment添加失败!"));
            return "error";
        }
    }

    /*查询Comment信息*/
    public String QueryComment() {
        if(currentPage == 0) currentPage = 1;
        List<Comment> commentList = commentDAO.QueryCommentInfo(topicObj, studentObj, currentPage);
        /*计算总的页数和总的记录数*/
        commentDAO.CalculateTotalPageAndRecordNumber(topicObj, studentObj);
        /*获取到总的页码数目*/
        totalPage = commentDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = commentDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("commentList",  commentList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("topicObj", topicObj);
        List<Topic> topicList = topicDAO.QueryAllTopicInfo();
        ctx.put("topicList", topicList);
        ctx.put("studentObj", studentObj);
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        return "query_view";
    }

    /*后台导出到excel*/
    public String QueryCommentOutputToExcel() { 
        List<Comment> commentList = commentDAO.QueryCommentInfo(topicObj,studentObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Comment信息记录"; 
        String[] headers = { "记录编号","评论的话题","评论内容","评论的学生","评论时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<commentList.size();i++) {
        	Comment comment = commentList.get(i); 
        	dataset.add(new String[]{comment.getCommentId() + "",comment.getTopicObj().getTitle(),
comment.getContent(),comment.getStudentObj().getStudentName(),
comment.getCommentTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Comment.xls");//filename是下载的xls的名，建议最好用英文 
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
    /*前台查询Comment信息*/
    public String FrontQueryComment() {
        if(currentPage == 0) currentPage = 1;
        List<Comment> commentList = commentDAO.QueryCommentInfo(topicObj, studentObj, currentPage);
        /*计算总的页数和总的记录数*/
        commentDAO.CalculateTotalPageAndRecordNumber(topicObj, studentObj);
        /*获取到总的页码数目*/
        totalPage = commentDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = commentDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("commentList",  commentList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("topicObj", topicObj);
        List<Topic> topicList = topicDAO.QueryAllTopicInfo();
        ctx.put("topicList", topicList);
        ctx.put("studentObj", studentObj);
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        return "front_query_view";
    }

    /*查询要修改的Comment信息*/
    public String ModifyCommentQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键commentId获取Comment对象*/
        Comment comment = commentDAO.GetCommentByCommentId(commentId);

        List<Topic> topicList = topicDAO.QueryAllTopicInfo();
        ctx.put("topicList", topicList);
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        ctx.put("comment",  comment);
        return "modify_view";
    }

    /*查询要修改的Comment信息*/
    public String FrontShowCommentQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键commentId获取Comment对象*/
        Comment comment = commentDAO.GetCommentByCommentId(commentId);

        List<Topic> topicList = topicDAO.QueryAllTopicInfo();
        ctx.put("topicList", topicList);
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        ctx.put("comment",  comment);
        return "front_show_view";
    }

    /*更新修改Comment信息*/
    public String ModifyComment() {
        ActionContext ctx = ActionContext.getContext();
        try {
            Topic topicObj = topicDAO.GetTopicByTopicId(comment.getTopicObj().getTopicId());
            comment.setTopicObj(topicObj);
            Student studentObj = studentDAO.GetStudentByStudentNumber(comment.getStudentObj().getStudentNumber());
            comment.setStudentObj(studentObj);
            commentDAO.UpdateComment(comment);
            ctx.put("message",  java.net.URLEncoder.encode("Comment信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Comment信息更新失败!"));
            return "error";
       }
   }

    /*删除Comment信息*/
    public String DeleteComment() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            commentDAO.DeleteComment(commentId);
            ctx.put("message",  java.net.URLEncoder.encode("Comment删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Comment删除失败!"));
            return "error";
        }
    }

}
