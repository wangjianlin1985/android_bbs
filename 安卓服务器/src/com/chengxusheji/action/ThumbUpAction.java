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
import com.chengxusheji.dao.ThumbUpDAO;
import com.chengxusheji.domain.ThumbUp;
import com.chengxusheji.dao.TopicDAO;
import com.chengxusheji.domain.Topic;
import com.chengxusheji.dao.StudentDAO;
import com.chengxusheji.domain.Student;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class ThumbUpAction extends BaseAction {

    /*界面层需要查询的属性: 话题*/
    private Topic topObj;
    public void setTopObj(Topic topObj) {
        this.topObj = topObj;
    }
    public Topic getTopObj() {
        return this.topObj;
    }

    /*界面层需要查询的属性: 学生*/
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

    private int thumbUpId;
    public void setThumbUpId(int thumbUpId) {
        this.thumbUpId = thumbUpId;
    }
    public int getThumbUpId() {
        return thumbUpId;
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
    @Resource ThumbUpDAO thumbUpDAO;

    /*待操作的ThumbUp对象*/
    private ThumbUp thumbUp;
    public void setThumbUp(ThumbUp thumbUp) {
        this.thumbUp = thumbUp;
    }
    public ThumbUp getThumbUp() {
        return this.thumbUp;
    }

    /*跳转到添加ThumbUp视图*/
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

    /*添加ThumbUp信息*/
    @SuppressWarnings("deprecation")
    public String AddThumbUp() {
        ActionContext ctx = ActionContext.getContext();
        try {
            Topic topObj = topicDAO.GetTopicByTopicId(thumbUp.getTopObj().getTopicId());
            thumbUp.setTopObj(topObj);
            Student studentObj = studentDAO.GetStudentByStudentNumber(thumbUp.getStudentObj().getStudentNumber());
            thumbUp.setStudentObj(studentObj);
            thumbUpDAO.AddThumbUp(thumbUp);
            ctx.put("message",  java.net.URLEncoder.encode("ThumbUp添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("ThumbUp添加失败!"));
            return "error";
        }
    }

    /*查询ThumbUp信息*/
    public String QueryThumbUp() {
        if(currentPage == 0) currentPage = 1;
        List<ThumbUp> thumbUpList = thumbUpDAO.QueryThumbUpInfo(topObj, studentObj, currentPage);
        /*计算总的页数和总的记录数*/
        thumbUpDAO.CalculateTotalPageAndRecordNumber(topObj, studentObj);
        /*获取到总的页码数目*/
        totalPage = thumbUpDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = thumbUpDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("thumbUpList",  thumbUpList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("topObj", topObj);
        List<Topic> topicList = topicDAO.QueryAllTopicInfo();
        ctx.put("topicList", topicList);
        ctx.put("studentObj", studentObj);
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        return "query_view";
    }

    /*后台导出到excel*/
    public String QueryThumbUpOutputToExcel() { 
        List<ThumbUp> thumbUpList = thumbUpDAO.QueryThumbUpInfo(topObj,studentObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "ThumbUp信息记录"; 
        String[] headers = { "记录编号","话题","学生","点赞时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<thumbUpList.size();i++) {
        	ThumbUp thumbUp = thumbUpList.get(i); 
        	dataset.add(new String[]{thumbUp.getThumbUpId() + "",thumbUp.getTopObj().getTitle(),
thumbUp.getStudentObj().getStudentName(),
thumbUp.getThumpTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"ThumbUp.xls");//filename是下载的xls的名，建议最好用英文 
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
    /*前台查询ThumbUp信息*/
    public String FrontQueryThumbUp() {
        if(currentPage == 0) currentPage = 1;
        List<ThumbUp> thumbUpList = thumbUpDAO.QueryThumbUpInfo(topObj, studentObj, currentPage);
        /*计算总的页数和总的记录数*/
        thumbUpDAO.CalculateTotalPageAndRecordNumber(topObj, studentObj);
        /*获取到总的页码数目*/
        totalPage = thumbUpDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = thumbUpDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("thumbUpList",  thumbUpList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("topObj", topObj);
        List<Topic> topicList = topicDAO.QueryAllTopicInfo();
        ctx.put("topicList", topicList);
        ctx.put("studentObj", studentObj);
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        return "front_query_view";
    }

    /*查询要修改的ThumbUp信息*/
    public String ModifyThumbUpQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键thumbUpId获取ThumbUp对象*/
        ThumbUp thumbUp = thumbUpDAO.GetThumbUpByThumbUpId(thumbUpId);

        List<Topic> topicList = topicDAO.QueryAllTopicInfo();
        ctx.put("topicList", topicList);
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        ctx.put("thumbUp",  thumbUp);
        return "modify_view";
    }

    /*查询要修改的ThumbUp信息*/
    public String FrontShowThumbUpQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键thumbUpId获取ThumbUp对象*/
        ThumbUp thumbUp = thumbUpDAO.GetThumbUpByThumbUpId(thumbUpId);

        List<Topic> topicList = topicDAO.QueryAllTopicInfo();
        ctx.put("topicList", topicList);
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        ctx.put("thumbUp",  thumbUp);
        return "front_show_view";
    }

    /*更新修改ThumbUp信息*/
    public String ModifyThumbUp() {
        ActionContext ctx = ActionContext.getContext();
        try {
            Topic topObj = topicDAO.GetTopicByTopicId(thumbUp.getTopObj().getTopicId());
            thumbUp.setTopObj(topObj);
            Student studentObj = studentDAO.GetStudentByStudentNumber(thumbUp.getStudentObj().getStudentNumber());
            thumbUp.setStudentObj(studentObj);
            thumbUpDAO.UpdateThumbUp(thumbUp);
            ctx.put("message",  java.net.URLEncoder.encode("ThumbUp信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("ThumbUp信息更新失败!"));
            return "error";
       }
   }

    /*删除ThumbUp信息*/
    public String DeleteThumbUp() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            thumbUpDAO.DeleteThumbUp(thumbUpId);
            ctx.put("message",  java.net.URLEncoder.encode("ThumbUp删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("ThumbUp删除失败!"));
            return "error";
        }
    }

}
