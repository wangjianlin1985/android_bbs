<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.Topic" %>
<%@ page import="com.chengxusheji.domain.TopicClass" %>
<%@ page import="com.chengxusheji.domain.Student" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //获取所有的TopicClass信息
    List<TopicClass> topicClassList = (List<TopicClass>)request.getAttribute("topicClassList");
    //获取所有的Student信息
    List<Student> studentList = (List<Student>)request.getAttribute("studentList");
    Topic topic = (Topic)request.getAttribute("topic");

    String username=(String)session.getAttribute("username");
    if(username==null){
        response.getWriter().println("<script>top.location.href='" + basePath + "login/login_view.action';</script>");
    }
%>
<HTML><HEAD><TITLE>修改话题</TITLE>
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
    var title = document.getElementById("topic.title").value;
    if(title=="") {
        alert('请输入标题!');
        return false;
    }
    var content = document.getElementById("topic.content").value;
    if(content=="") {
        alert('请输入内容!');
        return false;
    }
    var addTime = document.getElementById("topic.addTime").value;
    if(addTime=="") {
        alert('请输入发布时间!');
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
    <TD align="left" vAlign=top ><s:form action="Topic/Topic_ModifyTopic.action" method="post" onsubmit="return checkForm();" enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3' class="tablewidth">
  <tr>
    <td width=30%>话题id:</td>
    <td width=70%><input id="topic.topicId" name="topic.topicId" type="text" value="<%=topic.getTopicId() %>" readOnly /></td>
  </tr>

  <tr>
    <td width=30%>标题:</td>
    <td width=70%><input id="topic.title" name="topic.title" type="text" size="20" value='<%=topic.getTitle() %>'/></td>
  </tr>

  <tr>
    <td width=30%>话题类别:</td>
    <td width=70%>
      <select name="topic.topicClass.topicClassId">
      <%
        for(TopicClass topicClass:topicClassList) {
          String selected = "";
          if(topicClass.getTopicClassId() == topic.getTopicClass().getTopicClassId())
            selected = "selected";
      %>
          <option value='<%=topicClass.getTopicClassId() %>' <%=selected %>><%=topicClass.getTopicClassName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>话题图片:</td>
    <td width=70%><img src="<%=basePath %><%=topic.getPhoto() %>" width="200px" border="0px"/><br/>
    <input type=hidden name="topic.photo" value="<%=topic.getPhoto() %>" />
    <input id="photoFile" name="photoFile" type="file" size="50" /></td>
  </tr>
  <tr>
    <td width=30%>内容:</td>
    <td width=70%><textarea id="topic.content" name="topic.content" rows=5 cols=50><%=topic.getContent() %></textarea></td>
  </tr>

  <tr>
    <td width=30%>学生:</td>
    <td width=70%>
      <select name="topic.studentObj.studentNumber">
      <%
        for(Student student:studentList) {
          String selected = "";
          if(student.getStudentNumber().equals(topic.getStudentObj().getStudentNumber()))
            selected = "selected";
      %>
          <option value='<%=student.getStudentNumber() %>' <%=selected %>><%=student.getStudentName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>发布时间:</td>
    <td width=70%><input id="topic.addTime" name="topic.addTime" type="text" size="20" value='<%=topic.getAddTime() %>'/></td>
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
