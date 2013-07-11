<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="true" %>
<%@ page import="server.*" %>
<%@ include file="/jsp/allTags.jsp" %>
<%
//set no cache
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Expires", "0");
response.setHeader("Pragma", "no-cache");
//used for page headers
String HEADERTITLE = "Bookstore Password Assist";
String queryString = (String)request.getAttribute(bookstoreSignin.QUERYSTRING_ATTR);
String message = (String)request.getAttribute(bookstoreSignin.ERRORMESSAGE_ATTR);
if (queryString==null){
	queryString = "";
}
%>
<html>

<head>
<meta name="description" content="tradeusedbooks.com provides a variety of 
tools to assist you in managing your account">
<meta http-equiv="Cache-Control" content="no-cache" />
<META NAME="ROBOT" CONTENT="all">
<%@ include file="/html/meta.html" %>
<link rel="stylesheet" type="text/css" href="/styles/main.css">
<title>TradeUsedBooks.com - <%=HEADERTITLE%></title>
</head>
<body>
<%-- table is for <tr><td></td></tr> format for each include--%>
<table width="100%" border="0" cellspacing="0" cellpadding="0" align=center>
<%@ include file="/jsp/generalHeader.jsp" %>
<!-- main content begins -->
<tr><td align=center>
<p>Please enter the email address you use for this account.</p>
<form action="<%=response.encodeURL(URLInterface.URLBOOKSTOREFORGOT+"?"+queryString)%>" method=post>
<p>Email: <input type=text name=<%=bookstoreSignin.EMAIL_FIELD%> value="" size=25>
&nbsp;&nbsp;
<a class="refLinks" 
href="<%=response.encodeURL(URLInterface.URLBOOKSTORESIGNIN+"?"+queryString)%>">back to sign in</a>
<br>
<%if (message!=null){%>
<font color="blue">Message: </font><font color="#993333"><%=message%></font>
<%}%>
</p>
<center><input style="color: #333399" type=submit value="submit"></center>
</form>
</td></tr>
<!-- main content ends-->
<!-- footer begins -->
<%@ include file="/html/footer.html" %>
<!-- footer ends -->	
</table>
</body>
</html>