<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%>
<%@ page import="com.chengxusheji.domain.College" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //获取所有的College信息
    List<College> collegeList = (List<College>)request.getAttribute("collegeList");
    String username=(String)session.getAttribute("username");
    if(username==null){
        response.getWriter().println("<script>top.location.href='" + basePath + "login/login_view.action';</script>");
    }
%>
<HTML><HEAD><TITLE>添加专业信息</TITLE> 
<STYLE type=text/css>
BODY {
    	MARGIN-LEFT: 0px; BACKGROUND-COLOR: #ffffff
}
.STYLE1 {color: #ECE9D8}
.label {font-style.:italic; }
.errorLabel {font-style.:italic;  color:red; }
.errorMessage {font-weight:bold; color:red; }
</STYLE>
 <script src="<%=basePath %>calendar.js"></script>
<script language="javascript">
/*验证表单*/
function checkForm() {
    var specialNumber = document.getElementById("specialInfo.specialNumber").value;
    if(specialNumber=="") {
        alert('请输入专业编号!');
        return false;
    }
    var specialName = document.getElementById("specialInfo.specialName").value;
    if(specialName=="") {
        alert('请输入专业名称!');
        return false;
    }
    return true; 
}
 </script>
</HEAD>

<BODY background="<%=basePath %>images/adminBg.jpg">
<s:fielderror cssStyle="color:red" />
<TABLE align="center" height="100%" cellSpacing=0 cellPadding=0 width="80%" border=0>
  <TBODY>
  <TR>
    <TD align="left" vAlign=top >
    <s:form action="SpecialInfo/SpecialInfo_AddSpecialInfo.action" method="post" id="specialInfoAddForm" onsubmit="return checkForm();"  enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3' class="tablewidth">

  <tr>
    <td width=30%>专业编号:</td>
    <td width=70%><input id="specialInfo.specialNumber" name="specialInfo.specialNumber" type="text" /></td>
  </tr>

  <tr>
    <td width=30%>所属学院:</td>
    <td width=70%>
      <select name="specialInfo.collegeObj.collegeNumber">
      <%
        for(College college:collegeList) {
      %>
          <option value='<%=college.getCollegeNumber() %>'><%=college.getCollegeName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>专业名称:</td>
    <td width=70%><input id="specialInfo.specialName" name="specialInfo.specialName" type="text" size="20" /></td>
  </tr>

  <tr>
    <td width=30%>开办日期:</td>
    <td width=70%><input type="text" readonly id="specialInfo.startDate"  name="specialInfo.startDate" onclick="setDay(this);"/></td>
  </tr>

  <tr>
    <td width=30%>专业介绍:</td>
    <td width=70%><textarea id="specialInfo.introduction" name="specialInfo.introduction" rows="5" cols="50"></textarea></td>
  </tr>

  <tr bgcolor='#FFFFFF'>
      <td colspan="4" align="center">
        <input type='submit' name='button' value='保存' >
        &nbsp;&nbsp;
        <input type="reset" value='重写' />
      </td>
    </tr>

</table>
</s:form>
   </TD></TR>
  </TBODY>
</TABLE>
</BODY>
</HTML>
