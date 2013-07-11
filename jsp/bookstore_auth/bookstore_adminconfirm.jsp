<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="true" %>
<%@ page import="server.*" %>
<%@ include file="/jsp/allTags.jsp" %>
<%
//used for page headers
String error = (String)request.getAttribute(bookstoreSignin.ERRORMESSAGE_ATTR);
String HEADERTITLE = "Bookstore Administrative confirm";
String queryString = (String)request.getAttribute(bookstoreSignin.QUERYSTRING_ATTR);
String email = (String)request.getAttribute(bookstoreSignin.EMAIL_FIELD);
String contact = (String)request.getAttribute(bookstoreSignin.CONTACT_FIELD);
String address = (String)request.getAttribute(bookstoreSignin.ADDRESS_FIELD);
String website = (String)request.getAttribute(bookstoreSignin.WEBSITE_FIELD);
String moreinfo = (String)request.getAttribute(bookstoreSignin.MOREINFO_FIELD);
String firstname = (String)request.getAttribute(bookstoreSignin.FIRSTNAME_FIELD);
String lastname = (String)request.getAttribute(bookstoreSignin.LASTNAME_FIELD);
String bookstore = (String)request.getAttribute(bookstoreSignin.BOOKSTORE_FIELD);

String defaultPage = (String)request.getAttribute(bookstoreSignin.PAGE);

if (queryString==null){
	queryString = "";
}
if (email==null){
	email = "";
}
if (contact==null){
	contact = "";
}
if (address==null){
	address = "";
}
if (website==null){
	website = "";
}
if (moreinfo==null){
	moreinfo = "";
}
if (firstname==null){
	firstname = "";
}
if (lastname==null){
	lastname = "";
}
if (bookstore==null){
	bookstore = "";
}
%>
<html>

<head>
<meta name="description" content="">
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
<tr valign=top><td>
<%if (defaultPage.equals("1")){%>
<p>Please enter the following information about the bookstore and yourself</p>
<form action="<%=response.encodeURL(URLInterface.URLBOOKSTOREADMINCONFIRM+"?"+queryString)%>" method=post>
<table width="100%">
<%if (error!=null){%>
	<tr><td colspan=2><font color=#FF0000><b>error: </b><%=error%></font></td></tr>
<%}%>
<tr valign=top><td width="150"><font color="#CC3300">*</font>Email:</td>
<td align=left><input type=text name="<%=bookstoreSignin.EMAIL_FIELD%>" value="<%=email%>" size=30></td></tr>
<tr valign=top><td width="150">Password:</td>
<td align=left><input type=password disabled style="background-color:#CCCCCC"></td></tr>
<tr valign=top>
<td width="150"><font color="#CC3300">*</font>your First Name:</td>
<td align=left><input type=text name="<%=bookstoreSignin.FIRSTNAME_FIELD%>" value="<%=firstname%>" size=30></td></tr>
<td width="150"><font color="#CC3300">*</font>your Last Name:</td>
<td align=left><input type=text name="<%=bookstoreSignin.LASTNAME_FIELD%>" value="<%=lastname%>" size=30></td></tr>
<td width="150"><font color="#CC3300">*</font>Bookstore:</td>
<td align=left><input type=text name="<%=bookstoreSignin.BOOKSTORE_FIELD%>" value="<%=bookstore%>" size=30></td></tr>
<tr valign=top>
<td width="150"><font color="#CC3300">*</font>Contact Info:<br>
<font color="#000080" size="2">phone, fax, mail address...</font></td>
<td align=left>
<textarea name="<%=bookstoreSignin.CONTACT_FIELD%>" rows=5 cols=40><%=contact%></textarea>
</td>
</tr>
<tr valign=top>
<td width="150">
<font color="#CC3300">*</font>Address:<br><font color="#000080" size="2">street, city, state...</font></td>
<td align=left><textarea name="<%=bookstoreSignin.ADDRESS_FIELD%>" rows=5 cols=40><%=address%></textarea></td></tr>
<tr valign=top>
<td width="150">existing website:</td>
<td align=left>
http://<input type=text name="<%=bookstoreSignin.WEBSITE_FIELD%>" value="<%=website%>" size=40></td>
<tr valign=top><td colspan=2>a welcome message to your page and any additional information:</td></tr>
<tr valign=top>
<td align=left colspan=2>
<textarea name="<%=bookstoreSignin.MOREINFO_FIELD%>" rows=5 cols=60><%=moreinfo%></textarea></td></tr>
</tr>
</table>
<p align=center><input type=submit value="submit" style="color: #333399"></p>
</form>
<%}else{%>
confirmed
<%}%>
</td></tr>
<!-- main content ends-->
<!-- footer begins -->
<%@ include file="/html/footer.html" %>
<!-- footer ends -->	
</table>
</body>
</html>