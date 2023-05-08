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
import com.chengxusheji.dao.SpecialInfoDAO;
import com.chengxusheji.domain.SpecialInfo;
import com.chengxusheji.dao.CollegeDAO;
import com.chengxusheji.domain.College;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class SpecialInfoAction extends BaseAction {

    /*�������Ҫ��ѯ������: ����ѧԺ*/
    private College collegeObj;
    public void setCollegeObj(College collegeObj) {
        this.collegeObj = collegeObj;
    }
    public College getCollegeObj() {
        return this.collegeObj;
    }

    /*�������Ҫ��ѯ������: רҵ���*/
    private String specialNumber;
    public void setSpecialNumber(String specialNumber) {
        this.specialNumber = specialNumber;
    }
    public String getSpecialNumber() {
        return this.specialNumber;
    }

    /*�������Ҫ��ѯ������: רҵ����*/
    private String specialName;
    public void setSpecialName(String specialName) {
        this.specialName = specialName;
    }
    public String getSpecialName() {
        return this.specialName;
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
    @Resource SpecialInfoDAO specialInfoDAO;

    /*��������SpecialInfo����*/
    private SpecialInfo specialInfo;
    public void setSpecialInfo(SpecialInfo specialInfo) {
        this.specialInfo = specialInfo;
    }
    public SpecialInfo getSpecialInfo() {
        return this.specialInfo;
    }

    /*��ת�����SpecialInfo��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�College��Ϣ*/
        List<College> collegeList = collegeDAO.QueryAllCollegeInfo();
        ctx.put("collegeList", collegeList);
        return "add_view";
    }

    /*���SpecialInfo��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddSpecialInfo() {
        ActionContext ctx = ActionContext.getContext();
        /*��֤רҵ����Ƿ��Ѿ�����*/
        String specialNumber = specialInfo.getSpecialNumber();
        SpecialInfo db_specialInfo = specialInfoDAO.GetSpecialInfoBySpecialNumber(specialNumber);
        if(null != db_specialInfo) {
            ctx.put("error",  java.net.URLEncoder.encode("��רҵ����Ѿ�����!"));
            return "error";
        }
        try {
            College collegeObj = collegeDAO.GetCollegeByCollegeNumber(specialInfo.getCollegeObj().getCollegeNumber());
            specialInfo.setCollegeObj(collegeObj);
            specialInfoDAO.AddSpecialInfo(specialInfo);
            ctx.put("message",  java.net.URLEncoder.encode("SpecialInfo��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("SpecialInfo���ʧ��!"));
            return "error";
        }
    }

    /*��ѯSpecialInfo��Ϣ*/
    public String QuerySpecialInfo() {
        if(currentPage == 0) currentPage = 1;
        if(specialNumber == null) specialNumber = "";
        if(specialName == null) specialName = "";
        List<SpecialInfo> specialInfoList = specialInfoDAO.QuerySpecialInfoInfo(collegeObj, specialNumber, specialName, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        specialInfoDAO.CalculateTotalPageAndRecordNumber(collegeObj, specialNumber, specialName);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = specialInfoDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = specialInfoDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("specialInfoList",  specialInfoList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("collegeObj", collegeObj);
        List<College> collegeList = collegeDAO.QueryAllCollegeInfo();
        ctx.put("collegeList", collegeList);
        ctx.put("specialNumber", specialNumber);
        ctx.put("specialName", specialName);
        return "query_view";
    }

    /*��̨������excel*/
    public String QuerySpecialInfoOutputToExcel() { 
        if(specialNumber == null) specialNumber = "";
        if(specialName == null) specialName = "";
        List<SpecialInfo> specialInfoList = specialInfoDAO.QuerySpecialInfoInfo(collegeObj,specialNumber,specialName);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "SpecialInfo��Ϣ��¼"; 
        String[] headers = { "����ѧԺ","רҵ���","רҵ����","��������"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<specialInfoList.size();i++) {
        	SpecialInfo specialInfo = specialInfoList.get(i); 
        	dataset.add(new String[]{specialInfo.getCollegeObj().getCollegeName(),
specialInfo.getSpecialNumber(),specialInfo.getSpecialName(),new SimpleDateFormat("yyyy-MM-dd").format(specialInfo.getStartDate())});
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
			response.setHeader("Content-disposition","attachment; filename="+"SpecialInfo.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯSpecialInfo��Ϣ*/
    public String FrontQuerySpecialInfo() {
        if(currentPage == 0) currentPage = 1;
        if(specialNumber == null) specialNumber = "";
        if(specialName == null) specialName = "";
        List<SpecialInfo> specialInfoList = specialInfoDAO.QuerySpecialInfoInfo(collegeObj, specialNumber, specialName, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        specialInfoDAO.CalculateTotalPageAndRecordNumber(collegeObj, specialNumber, specialName);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = specialInfoDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = specialInfoDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("specialInfoList",  specialInfoList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("collegeObj", collegeObj);
        List<College> collegeList = collegeDAO.QueryAllCollegeInfo();
        ctx.put("collegeList", collegeList);
        ctx.put("specialNumber", specialNumber);
        ctx.put("specialName", specialName);
        return "front_query_view";
    }

    /*��ѯҪ�޸ĵ�SpecialInfo��Ϣ*/
    public String ModifySpecialInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������specialNumber��ȡSpecialInfo����*/
        SpecialInfo specialInfo = specialInfoDAO.GetSpecialInfoBySpecialNumber(specialNumber);

        List<College> collegeList = collegeDAO.QueryAllCollegeInfo();
        ctx.put("collegeList", collegeList);
        ctx.put("specialInfo",  specialInfo);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�SpecialInfo��Ϣ*/
    public String FrontShowSpecialInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������specialNumber��ȡSpecialInfo����*/
        SpecialInfo specialInfo = specialInfoDAO.GetSpecialInfoBySpecialNumber(specialNumber);

        List<College> collegeList = collegeDAO.QueryAllCollegeInfo();
        ctx.put("collegeList", collegeList);
        ctx.put("specialInfo",  specialInfo);
        return "front_show_view";
    }

    /*�����޸�SpecialInfo��Ϣ*/
    public String ModifySpecialInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            College collegeObj = collegeDAO.GetCollegeByCollegeNumber(specialInfo.getCollegeObj().getCollegeNumber());
            specialInfo.setCollegeObj(collegeObj);
            specialInfoDAO.UpdateSpecialInfo(specialInfo);
            ctx.put("message",  java.net.URLEncoder.encode("SpecialInfo��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("SpecialInfo��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��SpecialInfo��Ϣ*/
    public String DeleteSpecialInfo() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            specialInfoDAO.DeleteSpecialInfo(specialNumber);
            ctx.put("message",  java.net.URLEncoder.encode("SpecialInfoɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("SpecialInfoɾ��ʧ��!"));
            return "error";
        }
    }

}
