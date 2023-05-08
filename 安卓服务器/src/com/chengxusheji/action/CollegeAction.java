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
import com.chengxusheji.dao.CollegeDAO;
import com.chengxusheji.domain.College;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class CollegeAction extends BaseAction {

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

    private String collegeNumber;
    public void setCollegeNumber(String collegeNumber) {
        this.collegeNumber = collegeNumber;
    }
    public String getCollegeNumber() {
        return collegeNumber;
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
    @Resource CollegeDAO collegeDAO;

    /*��������College����*/
    private College college;
    public void setCollege(College college) {
        this.college = college;
    }
    public College getCollege() {
        return this.college;
    }

    /*��ת�����College��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        return "add_view";
    }

    /*���College��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddCollege() {
        ActionContext ctx = ActionContext.getContext();
        /*��֤ѧԺ����Ƿ��Ѿ�����*/
        String collegeNumber = college.getCollegeNumber();
        College db_college = collegeDAO.GetCollegeByCollegeNumber(collegeNumber);
        if(null != db_college) {
            ctx.put("error",  java.net.URLEncoder.encode("��ѧԺ����Ѿ�����!"));
            return "error";
        }
        try {
            collegeDAO.AddCollege(college);
            ctx.put("message",  java.net.URLEncoder.encode("College��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("College���ʧ��!"));
            return "error";
        }
    }

    /*��ѯCollege��Ϣ*/
    public String QueryCollege() {
        if(currentPage == 0) currentPage = 1;
        List<College> collegeList = collegeDAO.QueryCollegeInfo(currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        collegeDAO.CalculateTotalPageAndRecordNumber();
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = collegeDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = collegeDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("collegeList",  collegeList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        return "query_view";
    }

    /*��̨������excel*/
    public String QueryCollegeOutputToExcel() { 
        List<College> collegeList = collegeDAO.QueryCollegeInfo();
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "College��Ϣ��¼"; 
        String[] headers = { "ѧԺ���","ѧԺ����"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<collegeList.size();i++) {
        	College college = collegeList.get(i); 
        	dataset.add(new String[]{college.getCollegeNumber(),college.getCollegeName()});
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
			response.setHeader("Content-disposition","attachment; filename="+"College.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯCollege��Ϣ*/
    public String FrontQueryCollege() {
        if(currentPage == 0) currentPage = 1;
        List<College> collegeList = collegeDAO.QueryCollegeInfo(currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        collegeDAO.CalculateTotalPageAndRecordNumber();
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = collegeDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = collegeDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("collegeList",  collegeList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        return "front_query_view";
    }

    /*��ѯҪ�޸ĵ�College��Ϣ*/
    public String ModifyCollegeQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������collegeNumber��ȡCollege����*/
        College college = collegeDAO.GetCollegeByCollegeNumber(collegeNumber);

        ctx.put("college",  college);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�College��Ϣ*/
    public String FrontShowCollegeQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������collegeNumber��ȡCollege����*/
        College college = collegeDAO.GetCollegeByCollegeNumber(collegeNumber);

        ctx.put("college",  college);
        return "front_show_view";
    }

    /*�����޸�College��Ϣ*/
    public String ModifyCollege() {
        ActionContext ctx = ActionContext.getContext();
        try {
            collegeDAO.UpdateCollege(college);
            ctx.put("message",  java.net.URLEncoder.encode("College��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("College��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��College��Ϣ*/
    public String DeleteCollege() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            collegeDAO.DeleteCollege(collegeNumber);
            ctx.put("message",  java.net.URLEncoder.encode("Collegeɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Collegeɾ��ʧ��!"));
            return "error";
        }
    }

}
