<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.Topic" %>
<%@ page import="com.chengxusheji.domain.TopicClass" %>
<%@ page import="com.chengxusheji.domain.Student" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //��ȡ���е�TopicClass��Ϣ
    List<TopicClass> topicClassList = (List<TopicClass>)request.getAttribute("topicClassList");
    //��ȡ���е�Student��Ϣ
    List<Student> studentList = (List<Student>)request.getAttribute("studentList");
    Topic topic = (Topic)request.getAttribute("topic");

%>
<HTML><HEAD><TITLE>�鿴����</TITLE>
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
    <td width=30%>����id:</td>
    <td width=70%><%=topic.getTopicId() %></td>
  </tr>

  <tr>
    <td width=30%>����:</td>
    <td width=70%><%=topic.getTitle() %></td>
  </tr>

  <tr>
    <td width=30%>�������:</td>
    <td width=70%>
      <%=topic.getTopicClass().getTopicClassName() %>
    </td>
  </tr>

  <tr>
    <td width=30%>����ͼƬ:</td>
    <td width=70%><img src="<%=basePath %><%=topic.getPhoto() %>" width="200px" border="0px"/></td>
  </tr>
  <tr>
    <td width=30%>����:</td>
    <td width=70%><%=topic.getContent() %></td>
  </tr>

  <tr>
    <td width=30%>ѧ��:</td>
    <td width=70%>
      <%=topic.getStudentObj().getStudentName() %>
    </td>
  </tr>

  <tr>
    <td width=30%>����ʱ��:</td>
    <td width=70%><%=topic.getAddTime() %></td>
  </tr>

  <tr>
      <td colspan="4" align="center">
        <input type="button" value="����" onclick="history.back();"/>
      </td>
    </tr>

</table>
</s:form>
   </TD></TR>
  </TBODY>
</TABLE>
</BODY>
</HTML>
