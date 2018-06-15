<%@page import="java.util.Enumeration"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
	<%
		String dataName=request.getParameter("dataName");
		if(null!=dataName && dataName.length()>0){
			String dataValue=request.getParameter("dataValue");
			session.setAttribute(dataName, dataValue);
		}
	%>
	Server Info:
	<%
		String date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date);
		out.println("<br>["+request.getLocalAddr()+":"+request.getLocalPort()+"]"+date+"<br><br>")
	%>
	
	Server Info:
	<%
		out.println("<br>Session ID:"+session.getId()+"<br>");
		Enumeration e=Session.getAttributeNames();
		while(e.hasMoreElements()){
			String name=e.nextElement().toString();
			String value=session.getAttribute(name).toString();
		}
	%>
	<form action="index.jsp" method="post">
		name:<input type="text" name="dataName">
		<br>
		value:<input type="text" name="dataValue">
		<br>
		<input type="submit" value="提交">
	</form>
</body>
</html>