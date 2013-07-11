<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="true" %>
<%@ page import="server.*" %>
<%-- includes custom tags and stdl --%>
<%@ include file="/jsp/allTags.jsp" %>
<%
String HEADERTITLE = "Posting a Book";
String unqueFormID = (String)request.getAttribute(
	postBInterface.UNIQUEFORMATTR);
//all attributes should be passed as nonempty,nonnull strings
//no formating will be done here
String title = (String)request.getAttribute(
	postBInterface.TITLEFIELD);
String author = (String)request.getAttribute(
	postBInterface.AUTHORFIELD);
String isbn = (String)request.getAttribute(
	postBInterface.ISBNFIELD);
String condition = (String)request.getAttribute(
	postBInterface.CONDITIONFIELD);
String price = (String)request.getAttribute(
	postBInterface.PRICEFIELD);
String comments = (String)request.getAttribute(
	postBInterface.COMMENTSFIELD);
String college = (String)request.getAttribute(
	postBInterface.COLLEGEIDFIELD);
String department = (String)request.getAttribute(
	postBInterface.DEPARTMENTIDFIELD);
String course = (String)request.getAttribute(
	postBInterface.COURSEIDFIELD);
String teacher = (String)request.getAttribute(
	postBInterface.TEACHERIDFIELD);
%>
<html>
<head>
<meta name="description" 
content="posting is easy as 1,2,3!">
<meta http-equiv="Cache-Control" content="no-cache" />
<META NAME="ROBOT" CONTENT="all">
<%@ include file="/html/meta.html" %>
<link rel="stylesheet" type="text/css" 
href="/styles/main.css">
<title>TradeUsedBooks.com - Posting, Step 5</title>
<script>
function back(){
	document.mainForm.action = "<%=response.encodeURL(URLInterface.URLPBOOKS+"?back")%>";
	document.mainForm.submit();
}
</script>
</head>
<body bgColor=#ffffff>
<table width="100%" border="0" cellspacing="0" 
cellpadding="0" align=center>
<!-- header begins -->
<%@ include file="/jsp/generalHeader.jsp" %>
<!-- header ends -->
<!--main content begins -->
<tr><td>
<h1>Step V</h1>
<%-- from every page form is submited to the same url --%>
<form name="mainForm" method=post 
action="<%=response.encodeURL(URLInterface.URLPBOOKS)%>">
<%-- pagestep parameter let's the prog. know where 
we came from--%>
<input type=hidden 
	name="<%=postBInterface.PAGESTEP%>" value="5">
<%-- this will identify this particular form and
should be passed from page to page using request attr.--%>
<input type=hidden 
	name="<%=postBInterface.UNIQUEFORMATTR%>" 
	value="<%=unqueFormID%>">
<p>Please review the data you entered before submitting (Note:<font color="#FF0000">
do not</font> use the back button, use <font color="#FF0000">edit</font> 
instead)</p>
<table width="100%">
TITLE: <b><%=tools.htmlEncode(title)%></b><br>
by AUTHOR: <b><%=tools.htmlEncode(author)%></b><br>
ISBN: <b><%=isbn%></b><br>
CONDITION: <b><%=condition%></b><br>
PRICE: <b>$<%=price%>.00</b><br>
COMMENTS: <b><%=tools.htmlEncode(comments)%></b><br>
COLLEGE: <b><%=college%></b><br>
DEPARTMENT: <b><%=department%></b><br>
COURSE: <b><%=course%></b><br>
TEACHER: <b><%=teacher%></b><br>
</table>
<center><input type=button value="edit" onclick="back()" 
style="color: #333399;">&nbsp;&nbsp;&nbsp;<input type=submit value="post" 
style="color: #333399;"></center>
</form>
</td></tr>
<!-- main content ends -->
<!-- footer begins -->
<%@ include file="/html/footer.html" %>
<!-- footer ends -->	
</table>
</body>
</html>