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
import com.chengxusheji.dao.TopicClassDAO;
import com.chengxusheji.domain.TopicClass;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class TopicClassAction extends BaseAction {

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

    private int topicClassId;
    public void setTopicClassId(int topicClassId) {
        this.topicClassId = topicClassId;
    }
    public int getTopicClassId() {
        return topicClassId;
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

    /*��������TopicClass����*/
    private TopicClass topicClass;
    public void setTopicClass(TopicClass topicClass) {
        this.topicClass = topicClass;
    }
    public TopicClass getTopicClass() {
        return this.topicClass;
    }

    /*��ת�����TopicClass��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        return "add_view";
    }

    /*���TopicClass��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddTopicClass() {
        ActionContext ctx = ActionContext.getContext();
        try {
            topicClassDAO.AddTopicClass(topicClass);
            ctx.put("message",  java.net.URLEncoder.encode("TopicClass��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("TopicClass���ʧ��!"));
            return "error";
        }
    }

    /*��ѯTopicClass��Ϣ*/
    public String QueryTopicClass() {
        if(currentPage == 0) currentPage = 1;
        List<TopicClass> topicClassList = topicClassDAO.QueryTopicClassInfo(currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        topicClassDAO.CalculateTotalPageAndRecordNumber();
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = topicClassDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = topicClassDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("topicClassList",  topicClassList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        return "query_view";
    }

    /*��̨������excel*/
    public String QueryTopicClassOutputToExcel() { 
        List<TopicClass> topicClassList = topicClassDAO.QueryTopicClassInfo();
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "TopicClass��Ϣ��¼"; 
        String[] headers = { "����id","��������"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<topicClassList.size();i++) {
        	TopicClass topicClass = topicClassList.get(i); 
        	dataset.add(new String[]{topicClass.getTopicClassId() + "",topicClass.getTopicClassName()});
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
			response.setHeader("Content-disposition","attachment; filename="+"TopicClass.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯTopicClass��Ϣ*/
    public String FrontQueryTopicClass() {
        if(currentPage == 0) currentPage = 1;
        List<TopicClass> topicClassList = topicClassDAO.QueryTopicClassInfo(currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        topicClassDAO.CalculateTotalPageAndRecordNumber();
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = topicClassDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = topicClassDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("topicClassList",  topicClassList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        return "front_query_view";
    }

    /*��ѯҪ�޸ĵ�TopicClass��Ϣ*/
    public String ModifyTopicClassQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������topicClassId��ȡTopicClass����*/
        TopicClass topicClass = topicClassDAO.GetTopicClassByTopicClassId(topicClassId);

        ctx.put("topicClass",  topicClass);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�TopicClass��Ϣ*/
    public String FrontShowTopicClassQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������topicClassId��ȡTopicClass����*/
        TopicClass topicClass = topicClassDAO.GetTopicClassByTopicClassId(topicClassId);

        ctx.put("topicClass",  topicClass);
        return "front_show_view";
    }

    /*�����޸�TopicClass��Ϣ*/
    public String ModifyTopicClass() {
        ActionContext ctx = ActionContext.getContext();
        try {
            topicClassDAO.UpdateTopicClass(topicClass);
            ctx.put("message",  java.net.URLEncoder.encode("TopicClass��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("TopicClass��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��TopicClass��Ϣ*/
    public String DeleteTopicClass() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            topicClassDAO.DeleteTopicClass(topicClassId);
            ctx.put("message",  java.net.URLEncoder.encode("TopicClassɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("TopicClassɾ��ʧ��!"));
            return "error";
        }
    }

}
