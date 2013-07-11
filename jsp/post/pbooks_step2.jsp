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
String author = (String)request.getAttribute(
	postBInterface.AUTHORFIELD);
String unqueFormID = (String)request.getAttribute(
	postBInterface.UNIQUEFORMATTR);
postForm form = (postForm)session.getAttribute(unqueFormID);
if (form!=null){
	if (form.getTitle()!=null){
		title = form.getTitle();
	}
	if (form.getAuthor()!=null){
		author = form.getAuthor();
	}
	if (form.getISBN()!=null){
		isbn = form.getISBN();
	}
}
if (title == null){
	title = "";
}
if (isbn == null){
	isbn = "";
}
if (author == null){
	author = "";
}
isbn = tools.htmlEncode(isbn);
title = tools.htmlEncode(title);
author = tools.htmlEncode(author);
%>
<html>
<head>
<meta name="description" 
content="posting is easy as 1,2,3">
<meta http-equiv="Cache-Control" content="no-cache" />
<META NAME="ROBOT" CONTENT="all">
<%@ include file="/html/meta.html" %>
<link rel="stylesheet" type="text/css" href="/styles/main.css">
<title>TradeUsedBooks.com - Posting, Step 2</title>
</head>
<body bgColor=#ffffff>
<table width="100%" border="0" cellspacing="0" cellpadding="0" align=center>
<!-- header begins -->
<%@ include file="/jsp/generalHeader.jsp" %>
<!-- header ends -->
<!--main content begins -->
<tr><td>
<table width="100%">
<tr valign=top>
<td style="border-right: 1px solid #CCCCCC;" width="200" nowrap>How will this be used?<ul>
	<li>a page will be created specifically for your book</li>
	<li>your book will become available to the public through our search engine</li>
</ul>
<p>A few facts about our search engine:</p>
<ul>
	<li>while we do a combined search, entering ISBN is still the best way to 
	sell your book</li>
	<li>entering the book&#39;s title and author exactly as printed on the book will 
	yield better results</li>
</ul>
<p>If you have any questions or simply need help, check our
<a href="/doc/help">help page</a> or email us at
<a href="mailto:support@tradeusedbooks.com">support@tradeusedbooks.com</a></td>
<td>
<h1>Step II</h1>
Please provide the following information about your book (note: you will be 
later given space to leave comments and other info regarding the condition of 
this book).<br><br>
<%if (error!=null){%>
<img src="/myImages/alert.gif">
<font color='#FF0000'><b>error: </b>
<%=error%></font>
<%}%>
<%-- from every page form is submited to the same url --%>
<form name="mainForm" method=post 
action="<%=response.encodeURL(URLInterface.URLPBOOKS)%>">
<%-- pagestep parameter let's the prog. know where 
we came from--%>
<input type=hidden 
	name="<%=postBInterface.PAGESTEP%>" value="2">
<%-- this will identify this particular form and
should be passed from page to page using request attr.--%>
<input type=hidden 
	name="<%=postBInterface.UNIQUEFORMATTR%>" 
	value="<%=unqueFormID%>">
<table width="100%">
<tr>
	<td align=left>Title<b>:</b></td>
	<td align=left valign=top>
		<input type=text 
		name='<%=postBInterface.TITLEFIELD%>'
		value="<%=title%>" 
		maxlength='<%=booksTable.TITLE_MAX%>' size='40'>
	</td>
</tr>
<tr>
	<td align=left>Author:</td>
	<td align=left valign=top>
		<input type=text 
		name='<%=postBInterface.AUTHORFIELD%>' 
		value="<%=author%>" 
		maxlength='<%=booksTable.AUTHOR_MAX%>' size='40'>
	</td>
</tr>
<tr>
	<td align=left>ISBN:<br><i><font size='2'>optional</font></i>
	</td>
	<td align=left valign=top>
		<input type=text 
		name="<%=postBInterface.ISBNFIELD%>" 
		value="<%=isbn%>" 
		maxlength='<%=booksTable.ISBN_LENGTH2%>' size='13'>
	</td>
</tr>
</table>
<center><input type=submit value="next step>>" 
style="color: #333399;"></center>
</form>
</td>
</tr>
</table>
</td></tr>
<!-- main content ends -->
<!-- footer begins -->
<%@ include file="/html/footer.html" %>
<!-- footer ends -->
</table>
</body>
</html>