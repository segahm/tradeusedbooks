<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="true" %>
<%@ page import="server.*" %>
<%-- includes custom tags and stdl --%>
<%@ include file="/jsp/allTags.jsp" %>
<%
String HEADERTITLE = "Posting a Book";
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
<title>TradeUsedBooks.com - Posted!</title>
</head>
<body bgColor=#ffffff>
<table width="100%" border="0" cellspacing="0" 
cellpadding="0" align=center>
<!-- header begins -->
<%@ include file="/jsp/generalHeader.jsp" %>
<!-- header ends -->
<!--main content begins -->
<tr><td>
<p align="center" style="margin-top: 0; margin-bottom: 0">
<font size="5" color="#0000CC">Congradulations!</font></p>
<p align="center" style="margin-top: 0; margin-bottom: 0">&nbsp;<font color="#0000ff">-</font><font color="#0000FF">you 
have successfully posted your book</font></p>&nbsp;

		<p align="left">In a few minutes you should receive a confirmation email 
		from us with the details about this posting for your own personal reference.
		<br>
		Please continue coming to our website in order to stay up to date on the 
		status of your book. If someone buys your book we will notify you by 
		email and provide you with some instructions on what to do next. We 
		recommend that you visit &quot;My Account&quot; page to find 
		out about the status of this and all the other books that you have 
		previously posted. There you will also be able to find some other 
		important information about your previous book exchanges.
		<p align="center"><a href="<%=response.encodeURL(URLInterface.URLACCOUNTBOOKS)%>" class="refLinks">Go To My Account</a>
		</td></tr>
<!-- main content ends -->
<!-- footer begins -->
<%@ include file="/html/footer.html" %>
<!-- footer ends -->	
</table>
</body>
</html>
