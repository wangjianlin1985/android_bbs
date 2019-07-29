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

    private int topicClassId;
    public void setTopicClassId(int topicClassId) {
        this.topicClassId = topicClassId;
    }
    public int getTopicClassId() {
        return topicClassId;
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

    /*待操作的TopicClass对象*/
    private TopicClass topicClass;
    public void setTopicClass(TopicClass topicClass) {
        this.topicClass = topicClass;
    }
    public TopicClass getTopicClass() {
        return this.topicClass;
    }

    /*跳转到添加TopicClass视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        return "add_view";
    }

    /*添加TopicClass信息*/
    @SuppressWarnings("deprecation")
    public String AddTopicClass() {
        ActionContext ctx = ActionContext.getContext();
        try {
            topicClassDAO.AddTopicClass(topicClass);
            ctx.put("message",  java.net.URLEncoder.encode("TopicClass添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("TopicClass添加失败!"));
            return "error";
        }
    }

    /*查询TopicClass信息*/
    public String QueryTopicClass() {
        if(currentPage == 0) currentPage = 1;
        List<TopicClass> topicClassList = topicClassDAO.QueryTopicClassInfo(currentPage);
        /*计算总的页数和总的记录数*/
        topicClassDAO.CalculateTotalPageAndRecordNumber();
        /*获取到总的页码数目*/
        totalPage = topicClassDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = topicClassDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("topicClassList",  topicClassList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        return "query_view";
    }

    /*后台导出到excel*/
    public String QueryTopicClassOutputToExcel() { 
        List<TopicClass> topicClassList = topicClassDAO.QueryTopicClassInfo();
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "TopicClass信息记录"; 
        String[] headers = { "分类id","分类名称"};
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
		HttpServletResponse response = null;//创建一个HttpServletResponse对象 
		OutputStream out = null;//创建一个输出流对象 
		try { 
			response = ServletActionContext.getResponse();//初始化HttpServletResponse对象 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"TopicClass.xls");//filename是下载的xls的名，建议最好用英文 
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
    /*前台查询TopicClass信息*/
    public String FrontQueryTopicClass() {
        if(currentPage == 0) currentPage = 1;
        List<TopicClass> topicClassList = topicClassDAO.QueryTopicClassInfo(currentPage);
        /*计算总的页数和总的记录数*/
        topicClassDAO.CalculateTotalPageAndRecordNumber();
        /*获取到总的页码数目*/
        totalPage = topicClassDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = topicClassDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("topicClassList",  topicClassList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        return "front_query_view";
    }

    /*查询要修改的TopicClass信息*/
    public String ModifyTopicClassQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键topicClassId获取TopicClass对象*/
        TopicClass topicClass = topicClassDAO.GetTopicClassByTopicClassId(topicClassId);

        ctx.put("topicClass",  topicClass);
        return "modify_view";
    }

    /*查询要修改的TopicClass信息*/
    public String FrontShowTopicClassQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键topicClassId获取TopicClass对象*/
        TopicClass topicClass = topicClassDAO.GetTopicClassByTopicClassId(topicClassId);

        ctx.put("topicClass",  topicClass);
        return "front_show_view";
    }

    /*更新修改TopicClass信息*/
    public String ModifyTopicClass() {
        ActionContext ctx = ActionContext.getContext();
        try {
            topicClassDAO.UpdateTopicClass(topicClass);
            ctx.put("message",  java.net.URLEncoder.encode("TopicClass信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("TopicClass信息更新失败!"));
            return "error";
       }
   }

    /*删除TopicClass信息*/
    public String DeleteTopicClass() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            topicClassDAO.DeleteTopicClass(topicClassId);
            ctx.put("message",  java.net.URLEncoder.encode("TopicClass删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("TopicClass删除失败!"));
            return "error";
        }
    }

}
