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

    /*�������Ҫ��ѯ������: ����*/
    private Topic topObj;
    public void setTopObj(Topic topObj) {
        this.topObj = topObj;
    }
    public Topic getTopObj() {
        return this.topObj;
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

    private int thumbUpId;
    public void setThumbUpId(int thumbUpId) {
        this.thumbUpId = thumbUpId;
    }
    public int getThumbUpId() {
        return thumbUpId;
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
    @Resource TopicDAO topicDAO;
    @Resource StudentDAO studentDAO;
    @Resource ThumbUpDAO thumbUpDAO;

    /*��������ThumbUp����*/
    private ThumbUp thumbUp;
    public void setThumbUp(ThumbUp thumbUp) {
        this.thumbUp = thumbUp;
    }
    public ThumbUp getThumbUp() {
        return this.thumbUp;
    }

    /*��ת�����ThumbUp��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�Topic��Ϣ*/
        List<Topic> topicList = topicDAO.QueryAllTopicInfo();
        ctx.put("topicList", topicList);
        /*��ѯ���е�Student��Ϣ*/
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        return "add_view";
    }

    /*���ThumbUp��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddThumbUp() {
        ActionContext ctx = ActionContext.getContext();
        try {
            Topic topObj = topicDAO.GetTopicByTopicId(thumbUp.getTopObj().getTopicId());
            thumbUp.setTopObj(topObj);
            Student studentObj = studentDAO.GetStudentByStudentNumber(thumbUp.getStudentObj().getStudentNumber());
            thumbUp.setStudentObj(studentObj);
            thumbUpDAO.AddThumbUp(thumbUp);
            ctx.put("message",  java.net.URLEncoder.encode("ThumbUp��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("ThumbUp���ʧ��!"));
            return "error";
        }
    }

    /*��ѯThumbUp��Ϣ*/
    public String QueryThumbUp() {
        if(currentPage == 0) currentPage = 1;
        List<ThumbUp> thumbUpList = thumbUpDAO.QueryThumbUpInfo(topObj, studentObj, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        thumbUpDAO.CalculateTotalPageAndRecordNumber(topObj, studentObj);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = thumbUpDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
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

    /*��̨������excel*/
    public String QueryThumbUpOutputToExcel() { 
        List<ThumbUp> thumbUpList = thumbUpDAO.QueryThumbUpInfo(topObj,studentObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "ThumbUp��Ϣ��¼"; 
        String[] headers = { "��¼���","����","ѧ��","����ʱ��"};
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
		HttpServletResponse response = null;//����һ��HttpServletResponse���� 
		OutputStream out = null;//����һ����������� 
		try { 
			response = ServletActionContext.getResponse();//��ʼ��HttpServletResponse���� 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"ThumbUp.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯThumbUp��Ϣ*/
    public String FrontQueryThumbUp() {
        if(currentPage == 0) currentPage = 1;
        List<ThumbUp> thumbUpList = thumbUpDAO.QueryThumbUpInfo(topObj, studentObj, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        thumbUpDAO.CalculateTotalPageAndRecordNumber(topObj, studentObj);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = thumbUpDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
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

    /*��ѯҪ�޸ĵ�ThumbUp��Ϣ*/
    public String ModifyThumbUpQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������thumbUpId��ȡThumbUp����*/
        ThumbUp thumbUp = thumbUpDAO.GetThumbUpByThumbUpId(thumbUpId);

        List<Topic> topicList = topicDAO.QueryAllTopicInfo();
        ctx.put("topicList", topicList);
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        ctx.put("thumbUp",  thumbUp);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�ThumbUp��Ϣ*/
    public String FrontShowThumbUpQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������thumbUpId��ȡThumbUp����*/
        ThumbUp thumbUp = thumbUpDAO.GetThumbUpByThumbUpId(thumbUpId);

        List<Topic> topicList = topicDAO.QueryAllTopicInfo();
        ctx.put("topicList", topicList);
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        ctx.put("thumbUp",  thumbUp);
        return "front_show_view";
    }

    /*�����޸�ThumbUp��Ϣ*/
    public String ModifyThumbUp() {
        ActionContext ctx = ActionContext.getContext();
        try {
            Topic topObj = topicDAO.GetTopicByTopicId(thumbUp.getTopObj().getTopicId());
            thumbUp.setTopObj(topObj);
            Student studentObj = studentDAO.GetStudentByStudentNumber(thumbUp.getStudentObj().getStudentNumber());
            thumbUp.setStudentObj(studentObj);
            thumbUpDAO.UpdateThumbUp(thumbUp);
            ctx.put("message",  java.net.URLEncoder.encode("ThumbUp��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("ThumbUp��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��ThumbUp��Ϣ*/
    public String DeleteThumbUp() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            thumbUpDAO.DeleteThumbUp(thumbUpId);
            ctx.put("message",  java.net.URLEncoder.encode("ThumbUpɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("ThumbUpɾ��ʧ��!"));
            return "error";
        }
    }

}
