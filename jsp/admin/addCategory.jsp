<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="true" %>
<%@ page import="server.*" %>
<html>
<head>
<meta name="description" content="Authorization page.">
<meta http-equiv="Cache-Control" content="no-cache" />
<META NAME="Robots" CONTENT="noindex">
<%@ include file="/html/meta.html" %>
<title>TradeUsedBooks.com - add category</title>
</head>
<body>
<form method=post enctype="multipart/form-data" action="/admin/category">
	<p>file <input type=file name="<%=addCategory.FILE_UPLOAD_NAME%>" size="20"></p>
	<p>college id <input type=text name="<%=addCategory.COLLEGE_TEXT_ID%>"></p>
	<p>department id <input type=text name="<%=addCategory.DEPARTMENT_TEXT_ID%>"></p>
	choose one:<br>
departments <input type=radio value="<%=addCategory.DEPARTMENT_RADIO_VALUE%>" name="<%=addCategory.CATEGORY_RADIO%>"><br>
courses <input type=radio value="<%=addCategory.COURSE_RADIO_VALUE%>" name="<%=addCategory.CATEGORY_RADIO%>"><br>
teachers <input type=radio value="<%=addCategory.TEACHER_RADIO_VALUE%>" name="<%=addCategory.CATEGORY_RADIO%>"><br>
<input type=submit value="submit">
</form>
</body>
</html>