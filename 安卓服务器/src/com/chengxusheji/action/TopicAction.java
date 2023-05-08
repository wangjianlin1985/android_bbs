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

	/*ͼƬ���ļ��ֶ�photo��������*/
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
    /*�������Ҫ��ѯ������: ����*/
    private String title;
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }

    /*�������Ҫ��ѯ������: �������*/
    private TopicClass topicClass;
    public void setTopicClass(TopicClass topicClass) {
        this.topicClass = topicClass;
    }
    public TopicClass getTopicClass() {
        return this.topicClass;
    }

    /*�������Ҫ��ѯ������: ѧ��*/
    private Student studentObj;
    public void setStudentObj(Student studentObj) {
        this.studentObj = studentObj;
    }
    public Student getStudentObj() {
        return this.studentObj;
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

    private int topicId;
    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }
    public int getTopicId() {
        return topicId;
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
    @Resource TopicClassDAO topicClassDAO;
    @Resource StudentDAO studentDAO;
    @Resource TopicDAO topicDAO;

    /*��������Topic����*/
    private Topic topic;
    public void setTopic(Topic topic) {
        this.topic = topic;
    }
    public Topic getTopic() {
        return this.topic;
    }

    /*��ת�����Topic��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�TopicClass��Ϣ*/
        List<TopicClass> topicClassList = topicClassDAO.QueryAllTopicClassInfo();
        ctx.put("topicClassList", topicClassList);
        /*��ѯ���е�Student��Ϣ*/
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        return "add_view";
    }

    /*���Topic��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddTopic() {
        ActionContext ctx = ActionContext.getContext();
        try {
            TopicClass topicClass = topicClassDAO.GetTopicClassByTopicClassId(topic.getTopicClass().getTopicClassId());
            topic.setTopicClass(topicClass);
            Student studentObj = studentDAO.GetStudentByStudentNumber(topic.getStudentObj().getStudentNumber());
            topic.setStudentObj(studentObj);
            /*������ͼƬ�ϴ�*/
            String photoPath = "upload/noimage.jpg"; 
       	 	if(photoFile != null)
       	 		photoPath = photoUpload(photoFile,photoFileContentType);
       	 	topic.setPhoto(photoPath);
            topicDAO.AddTopic(topic);
            ctx.put("message",  java.net.URLEncoder.encode("Topic��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Topic���ʧ��!"));
            return "error";
        }
    }

    /*��ѯTopic��Ϣ*/
    public String QueryTopic() {
        if(currentPage == 0) currentPage = 1;
        if(title == null) title = "";
        List<Topic> topicList = topicDAO.QueryTopicInfo(title, topicClass, studentObj, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        topicDAO.CalculateTotalPageAndRecordNumber(title, topicClass, studentObj);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = topicDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
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

    /*��̨������excel*/
    public String QueryTopicOutputToExcel() { 
        if(title == null) title = "";
        List<Topic> topicList = topicDAO.QueryTopicInfo(title,topicClass,studentObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Topic��Ϣ��¼"; 
        String[] headers = { "����id","����","�������","����ͼƬ","ѧ��","����ʱ��"};
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
		HttpServletResponse response = null;//����һ��HttpServletResponse���� 
		OutputStream out = null;//����һ����������� 
		try { 
			response = ServletActionContext.getResponse();//��ʼ��HttpServletResponse���� 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"Topic.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯTopic��Ϣ*/
    public String FrontQueryTopic() {
        if(currentPage == 0) currentPage = 1;
        if(title == null) title = "";
        List<Topic> topicList = topicDAO.QueryTopicInfo(title, topicClass, studentObj, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        topicDAO.CalculateTotalPageAndRecordNumber(title, topicClass, studentObj);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = topicDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
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

    /*��ѯҪ�޸ĵ�Topic��Ϣ*/
    public String ModifyTopicQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������topicId��ȡTopic����*/
        Topic topic = topicDAO.GetTopicByTopicId(topicId);

        List<TopicClass> topicClassList = topicClassDAO.QueryAllTopicClassInfo();
        ctx.put("topicClassList", topicClassList);
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        ctx.put("topic",  topic);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�Topic��Ϣ*/
    public String FrontShowTopicQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������topicId��ȡTopic����*/
        Topic topic = topicDAO.GetTopicByTopicId(topicId);

        List<TopicClass> topicClassList = topicClassDAO.QueryAllTopicClassInfo();
        ctx.put("topicClassList", topicClassList);
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        ctx.put("topic",  topic);
        return "front_show_view";
    }

    /*�����޸�Topic��Ϣ*/
    public String ModifyTopic() {
        ActionContext ctx = ActionContext.getContext();
        try {
            TopicClass topicClass = topicClassDAO.GetTopicClassByTopicClassId(topic.getTopicClass().getTopicClassId());
            topic.setTopicClass(topicClass);
            Student studentObj = studentDAO.GetStudentByStudentNumber(topic.getStudentObj().getStudentNumber());
            topic.setStudentObj(studentObj);
            /*������ͼƬ�ϴ�*/
            if(photoFile != null) {
            	String photoPath = photoUpload(photoFile,photoFileContentType);
            	topic.setPhoto(photoPath);
            }
            topicDAO.UpdateTopic(topic);
            ctx.put("message",  java.net.URLEncoder.encode("Topic��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Topic��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��Topic��Ϣ*/
    public String DeleteTopic() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            topicDAO.DeleteTopic(topicId);
            ctx.put("message",  java.net.URLEncoder.encode("Topicɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Topicɾ��ʧ��!"));
            return "error";
        }
    }

}
