<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.Comment" %>
<%@ page import="com.chengxusheji.domain.Topic" %>
<%@ page import="com.chengxusheji.domain.Student" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //获取所有的Topic信息
    List<Topic> topicList = (List<Topic>)request.getAttribute("topicList");
    //获取所有的Student信息
    List<Student> studentList = (List<Student>)request.getAttribute("studentList");
    Comment comment = (Comment)request.getAttribute("comment");

%>
<HTML><HEAD><TITLE>查看评论</TITLE>
<STYLE type=text/css>
body{margin:0px; font-size:12px; background-image:url(<%=basePath%>images/bg.jpg); background-position:bottom; background-repeat:repeat-x; background-color:#A2D5F0;}
.STYLE1 {color: #ECE9D8}
.label {font-style.:italic; }
.errorLabel {font-style.:italic;  color:red; }
.errorMessage {font-weight:bold; color:red; }
</STYLE>
 <script src="<%=basePath %>calendar.js"></script>
</HEAD>
<BODY><br/><br/>
<s:fielderror cssStyle="color:red" />
<TABLE align="center" height="100%" cellSpacing=0 cellPadding=0 width="80%" border=0>
  <TBODY>
  <TR>
    <TD align="left" vAlign=top ><s:form action="" method="post" onsubmit="return checkForm();" enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3'  class="tablewidth">
  <tr>
    <td width=30%>记录编号:</td>
    <td width=70%><%=comment.getCommentId() %></td>
  </tr>

  <tr>
    <td width=30%>评论的话题:</td>
    <td width=70%>
      <%=comment.getTopicObj().getTitle() %>
    </td>
  </tr>

  <tr>
    <td width=30%>评论内容:</td>
    <td width=70%><%=comment.getContent() %></td>
  </tr>

  <tr>
    <td width=30%>评论的学生:</td>
    <td width=70%>
      <%=comment.getStudentObj().getStudentName() %>
    </td>
  </tr>

  <tr>
    <td width=30%>评论时间:</td>
    <td width=70%><%=comment.getCommentTime() %></td>
  </tr>

  <tr>
      <td colspan="4" align="center">
        <input type="button" value="返回" onclick="history.back();"/>
      </td>
    </tr>

</table>
</s:form>
   </TD></TR>
  </TBODY>
</TABLE>
</BODY>
</HTML>
