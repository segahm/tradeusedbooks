<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="true" %>
<%@ page import="server.*" %>
<%@ include file="/jsp/allTags.jsp" %>
<%
final String HEADERTITLE = "Error";
String errorMessage = (String)request.getAttribute(
	StringInterface.ERRORPAGEATTR);
%>
<html>

<head>
<meta name="description" content="Account Page.">
<meta http-equiv="Cache-Control" content="no-cache" />
<META NAME="Robots" CONTENT="all">
<%@ include file="/html/meta.html" %>
<link rel="stylesheet" type="text/css" href="/styles/main.css">
<link rel="stylesheet" type="text/css" href="/styles/account.css">
<title>TradeUsedBooks.com - Error Page</title>
</head>
<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0" align=center>
<!-- header begins -->
<%@ include file="/jsp/generalHeader.jsp" %>
<!-- header ends -->
<!-- main content begins-->
<tr>
	<td>
	<p>We are sorry but the following error occurred:<br>
	
	<font color=red><%=errorMessage%></font></p>
	
					If you have any questions or simply need help, check our
					<a href="<%=URLInterface.URLHELPPAGE%>">help page</a> or email 
					us at <a href="mailto:support@tradeusedbooks.com">
					support@tradeusedbooks.com</a>
	
	</td>
</tr>
<!-- main content ends -->
<!-- footer begins -->
<%@ include file="/html/footer.html" %>
<!-- footer ends -->	
</table>
</body>
</html>