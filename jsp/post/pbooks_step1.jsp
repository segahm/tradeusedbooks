<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="true" %>
<%@ page import="server.*" %>
<%-- includes custom tags and stdl --%>
<%@ include file="/jsp/allTags.jsp" %>
<%
String HEADERTITLE = "Posting a Book";
String error = (String)request.getAttribute(
	postBInterface.ERRORMESSAGE);
String isbn = (String)request.getAttribute(
	postBInterface.ISBNFIELD);
String title = (String)request.getAttribute(
	postBInterface.TITLEFIELD);
String unqueFormID = (String)request.getAttribute(
	postBInterface.UNIQUEFORMATTR);
if (title == null){
	title = "";
}
if (isbn == null){
	isbn = "";
}
%>
<html>
<head>
<meta name="description" 
content="posting is easy as 1,2,3!">
<meta http-equiv="Cache-Control" content="no-cache" />
<META NAME="Robots" CONTENT="all">
<%@ include file="/html/meta.html" %>
<link rel="stylesheet" type="text/css" 
href="/styles/main.css">
<title>TradeUsedBooks.com - Posting, Step 1</title>
</head>
<body bgColor=#ffffff>
<table width="100%" border="0" cellspacing="0" 
cellpadding="0" align=center>
<!-- header begins -->
<%@ include file="/jsp/generalHeader.jsp" %>
<!-- header ends -->
<!--main content begins -->
<tr><td><table width="100%"><tr>
<td style="border-right: 1px solid #CCCCCC;"
 width="300" nowrap><table width="100%"><tr>
<td style="font-size: 20px; color: #990000">
What is an ISBN?</td></tr>
<tr><td>
<p>ISBN stands for &quot;International 
Standard Book Number.&quot; The ISBN is a 
10-digit (starting 2005 a 13-digit) 
identification system that allows booksellers and 
libraries to easily differentiate between books and 
other media when ordering. The ISBN refers to the 
specific edition (e.g. trade paperback, hardcover) and 
is usually located on the back cover of the book and on 
the copyright page. </p>
<p><i>example:</i> 
7-88497-523-8</p>
<p><i><font size="2">When you type ISBN, please enter the 10-digit ISBN number without any dashes like this:</font></i> 7884975238</p></td>
</tr>
</table></td><td valign=top style="padding-left:10px">
<h1>Step I</h1>
Please enter one of the following:
<%-- from every page form is submited to the same url --%>
<form name="mainForm" method=post 
action="<%=response.encodeURL(URLInterface.URLPBOOKS)%>">
<%-- pagestep parameter let's the prog. know where 
we came from--%>
<input type=hidden 
	name="<%=postBInterface.PAGESTEP%>" value="1">
<%-- this will identify this particular form and
should be passed from page to page using request attr.--%>
<input type=hidden 
	name="<%=postBInterface.UNIQUEFORMATTR%>" 
	value="<%=unqueFormID%>">
<table width="100%">
<%if (error!=null){%>
<tr><td colspan="2"><img src="/myImages/alert.gif">
<font color='#FF0000'><b>error: </b>
<%=error%></font></td></tr>
<%}%>
<tr><td><table style="margin-left:20px" width="50%">
<tr><td><input
<%if (isbn!=null){%>
 checked 
<%}%> 
type=radio name="r" id="isbnR" 
value="<%=postBInterface.ISBNFIELD%>"></td>
<td><label for="isbnR">book&#39;s ISBN #</label></td>
</tr><tr><td></td><td>
<input type=text name="<%=postBInterface.ISBNFIELD%>" 
value="<%=isbn%>" 
size="20" maxlength="<%=booksTable.ISBN_LENGTH2%>" 
onFocus="document.mainForm.isbnR.checked = true"></td>
</tr><tr><td><input
<%if (title!=null){%>
 checked 
<%}%> 
type=radio name="r" id="titleR" 
value="<%=postBInterface.TITLEFIELD%>"></td><td>
<label for="titleR">book&#39;s Title</label></td>
</tr><tr><td></td><td><input type=text 
name="<%=postBInterface.TITLEFIELD%>" 
value="<%=title%>" 
size="20" maxlength="<%=booksTable.TITLE_MAX%>" 
onFocus="document.mainForm.titleR.checked = true"></td>
</tr></table></td><td><img src="/myImages/barcode.jpg" 
alt="ISBN example">
</td></tr></table><center><input type=submit 
value="next step>>" 
style="color: #333399;"></center></form>
</td></tr></table></td></tr>
<!-- main content ends -->
<!-- footer begins -->
<%@ include file="/html/footer.html" %>
<!-- footer ends -->	
</table>
</body>
</html>