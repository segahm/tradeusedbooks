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
String HEADERTITLE = "Bookstore Sign In";
String hiddenid = (String)request.getAttribute(bookstoreSignin.HIDDENID_FIELD);
String error = (String)request.getAttribute(bookstoreSignin.ERRORMESSAGE_ATTR);
String queryString = (String)request.getAttribute(bookstoreSignin.QUERYSTRING_ATTR);
if (queryString==null){
	queryString = "";
}
%>
<html>

<head>
<meta name="description" content="open your own book 
store and display your book inventory to college students">
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
<tr><td>
<table width="100%" cellspacing=5>
<tr valign=top>
<td>
	<b>Increase textbook sales</b><p>This portion of the site 
	allows you as a merchant bookstore owner to display your book inventory 
	for free. Here you can</p>
	<ul>
		<li>let people know where your store is located</li>
		<li>provide ways to contact you</li>
		<li>let college students know what books you have</li>
		<li>have a web page specifically generated for you that would be referenced by every book you post</li>
	</ul>
	<p align=center>
		<form method=post action="<%=response.encodeURL(URLInterface.URLBOOKSTOREREGISTER+"?"+queryString)%>">
		<input style="color: #333399" type="submit" value="Register Today!">
		</form>
	</p>
</td>
<!-- sign in sidebar begins-->
<td width="200" nowrap align=left style="border-left: 1px solid #CCCCCC;">
	<p style="padding-left:10px"><b>Bookstore Sign In</b></p>
	<form action="<%=response.encodeURL(URLInterface.URLBOOKSTORESIGNIN+"?"+queryString)%>" method=post>
	<input type=hidden name="<%=bookstoreSignin.HIDDENID_FIELD%>" value="<%=hiddenid%>">
	<table width="100%">
	<%if (error!=null){%>
	<tr><td colspan=2><font color=#FF0000><b>error: </b><%=error%></font></td></tr>
	<%}%>
	<tr>
	<td align=right>Email:</td>
	<td><input type=text name="<%=bookstoreSignin.EMAIL_FIELD%>" value="" size="25"></td>
	</tr>
	<tr>
	<td align=right>Password:</td>
	<td><input type=password name="<%=bookstoreSignin.PASSWORD_FIELD%>" value="" size="25"></td>
	</tr>
	<tr><td colspan=2 align=left>
		<a style="text-decoration: none;color: #333399;font-size:12px;" 
		href="<%=response.encodeURL(URLInterface.URLBOOKSTOREFORGOT+"?"+queryString)%>">Forgot your password?</a></td></tr>
	<tr><td colspan=2 align=center><br><input style="color: #333399" type=submit value="sign in"></td></tr>
	</table>
	</form>
</td>
<!-- sign in sidebar ends-->
</tr>
</table></td></tr>
<!-- main content ends-->
<!-- footer begins -->
<%@ include file="/html/footer.html" %>
<!-- footer ends -->	
</table>
</body>
</html>