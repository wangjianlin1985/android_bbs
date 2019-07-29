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
import com.chengxusheji.dao.TopicDAO;
import com.chengxusheji.domain.Topic;
import com.chengxusheji.dao.TopicClassDAO;
import com.chengxusheji.domain.TopicClass;
import com.chengxusheji.dao.StudentDAO;
import com.chengxusheji.domain.Student;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class TopicAction extends BaseAction {

	/*图片或文件字段photo参数接收*/
	private File photoFile;
	private String photoFileFileName;
	private String photoFileContentType;
	public File getPhotoFile() {
		return photoFile;
	}
	public void setPhotoFile(File photoFile) {
		this.photoFile = photoFile;
	}
	public String getPhotoFileFileName() {
		return photoFileFileName;
	}
	public void setPhotoFileFileName(String photoFileFileName) {
		this.photoFileFileName = photoFileFileName;
	}
	public String getPhotoFileContentType() {
		return photoFileContentType;
	}
	public void setPhotoFileContentType(String photoFileContentType) {
		this.photoFileContentType = photoFileContentType;
	}
    /*界面层需要查询的属性: 标题*/
    private String title;
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }

    /*界面层需要查询的属性: 话题类别*/
    private TopicClass topicClass;
    public void setTopicClass(TopicClass topicClass) {
        this.topicClass = topicClass;
    }
    public TopicClass getTopicClass() {
        return this.topicClass;
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

    private int topicId;
    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }
    public int getTopicId() {
        return topicId;
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
    @Resource TopicClassDAO topicClassDAO;
    @Resource StudentDAO studentDAO;
    @Resource TopicDAO topicDAO;

    /*待操作的Topic对象*/
    private Topic topic;
    public void setTopic(Topic topic) {
        this.topic = topic;
    }
    public Topic getTopic() {
        return this.topic;
    }

    /*跳转到添加Topic视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*查询所有的TopicClass信息*/
        List<TopicClass> topicClassList = topicClassDAO.QueryAllTopicClassInfo();
        ctx.put("topicClassList", topicClassList);
        /*查询所有的Student信息*/
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        return "add_view";
    }

    /*添加Topic信息*/
    @SuppressWarnings("deprecation")
    public String AddTopic() {
        ActionContext ctx = ActionContext.getContext();
        try {
            TopicClass topicClass = topicClassDAO.GetTopicClassByTopicClassId(topic.getTopicClass().getTopicClassId());
            topic.setTopicClass(topicClass);
            Student studentObj = studentDAO.GetStudentByStudentNumber(topic.getStudentObj().getStudentNumber());
            topic.setStudentObj(studentObj);
            /*处理话题图片上传*/
            String photoPath = "upload/noimage.jpg"; 
       	 	if(photoFile != null)
       	 		photoPath = photoUpload(photoFile,photoFileContentType);
       	 	topic.setPhoto(photoPath);
            topicDAO.AddTopic(topic);
            ctx.put("message",  java.net.URLEncoder.encode("Topic添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Topic添加失败!"));
            return "error";
        }
    }

    /*查询Topic信息*/
    public String QueryTopic() {
        if(currentPage == 0) currentPage = 1;
        if(title == null) title = "";
        List<Topic> topicList = topicDAO.QueryTopicInfo(title, topicClass, studentObj, currentPage);
        /*计算总的页数和总的记录数*/
        topicDAO.CalculateTotalPageAndRecordNumber(title, topicClass, studentObj);
        /*获取到总的页码数目*/
        totalPage = topicDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = topicDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("topicList",  topicList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("title", title);
        ctx.put("topicClass", topicClass);
        List<TopicClass> topicClassList = topicClassDAO.QueryAllTopicClassInfo();
        ctx.put("topicClassList", topicClassList);
        ctx.put("studentObj", studentObj);
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        return "query_view";
    }

    /*后台导出到excel*/
    public String QueryTopicOutputToExcel() { 
        if(title == null) title = "";
        List<Topic> topicList = topicDAO.QueryTopicInfo(title,topicClass,studentObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Topic信息记录"; 
        String[] headers = { "话题id","标题","话题类别","话题图片","学生","发布时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<topicList.size();i++) {
        	Topic topic = topicList.get(i); 
        	dataset.add(new String[]{topic.getTopicId() + "",topic.getTitle(),topic.getTopicClass().getTopicClassName(),
topic.getPhoto(),topic.getStudentObj().getStudentName(),
topic.getAddTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Topic.xls");//filename是下载的xls的名，建议最好用英文 
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
    /*前台查询Topic信息*/
    public String FrontQueryTopic() {
        if(currentPage == 0) currentPage = 1;
        if(title == null) title = "";
        List<Topic> topicList = topicDAO.QueryTopicInfo(title, topicClass, studentObj, currentPage);
        /*计算总的页数和总的记录数*/
        topicDAO.CalculateTotalPageAndRecordNumber(title, topicClass, studentObj);
        /*获取到总的页码数目*/
        totalPage = topicDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = topicDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("topicList",  topicList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("title", title);
        ctx.put("topicClass", topicClass);
        List<TopicClass> topicClassList = topicClassDAO.QueryAllTopicClassInfo();
        ctx.put("topicClassList", topicClassList);
        ctx.put("studentObj", studentObj);
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        return "front_query_view";
    }

    /*查询要修改的Topic信息*/
    public String ModifyTopicQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键topicId获取Topic对象*/
        Topic topic = topicDAO.GetTopicByTopicId(topicId);

        List<TopicClass> topicClassList = topicClassDAO.QueryAllTopicClassInfo();
        ctx.put("topicClassList", topicClassList);
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        ctx.put("topic",  topic);
        return "modify_view";
    }

    /*查询要修改的Topic信息*/
    public String FrontShowTopicQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键topicId获取Topic对象*/
        Topic topic = topicDAO.GetTopicByTopicId(topicId);

        List<TopicClass> topicClassList = topicClassDAO.QueryAllTopicClassInfo();
        ctx.put("topicClassList", topicClassList);
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        ctx.put("topic",  topic);
        return "front_show_view";
    }

    /*更新修改Topic信息*/
    public String ModifyTopic() {
        ActionContext ctx = ActionContext.getContext();
        try {
            TopicClass topicClass = topicClassDAO.GetTopicClassByTopicClassId(topic.getTopicClass().getTopicClassId());
            topic.setTopicClass(topicClass);
            Student studentObj = studentDAO.GetStudentByStudentNumber(topic.getStudentObj().getStudentNumber());
            topic.setStudentObj(studentObj);
            /*处理话题图片上传*/
            if(photoFile != null) {
            	String photoPath = photoUpload(photoFile,photoFileContentType);
            	topic.setPhoto(photoPath);
            }
            topicDAO.UpdateTopic(topic);
            ctx.put("message",  java.net.URLEncoder.encode("Topic信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Topic信息更新失败!"));
            return "error";
       }
   }

    /*删除Topic信息*/
    public String DeleteTopic() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            topicDAO.DeleteTopic(topicId);
            ctx.put("message",  java.net.URLEncoder.encode("Topic删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Topic删除失败!"));
            return "error";
        }
    }

}
