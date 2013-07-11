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
String HEADERTITLE = "Bookstore Registration";
String hiddenid = (String)request.getAttribute(bookstoreSignin.HIDDENID_FIELD);
String error = (String)request.getAttribute(bookstoreSignin.ERRORMESSAGE_ATTR);
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
<meta name="description" content="opening your own 
bookstore on tradeusedbooks.com is easy as ever">
<meta http-equiv="Cache-Control" content="no-cache" />
<META NAME="ROBOT" CONTENT="all">
<%@ include file="/html/meta.html" %>
<link rel="stylesheet" type="text/css" href="/styles/main.css">
<title>TradeUsedBooks.com - <%=HEADERTITLE%></title>
<script language=javascript>
function limitText(field,limit){
	if (field.value.length>limit){
		field.value = field.value.substr(0,limit);
	}
}
</script>
</head>
<body>
<%-- table is for <tr><td></td></tr> format for each include--%>
<table width="100%" border="0" cellspacing="0" cellpadding="0" align=center>
<%@ include file="/jsp/generalHeader.jsp" %>
<!-- main content begins -->
<tr><td>
<%if (defaultPage.equals("1")){%>
<table width="100%" cellspacing=5>
<tr valign=top>
<td style="border-right: 1px solid #CCCCCC;" width="200">
How will this be used?<ul>
	<li>a web page will be created and will include your location and contact 
	information</li>
	<li>we will use it to verify and confirm your registration</li>
	<li>college students will use it to better locate your store</li>
</ul>
</td>
<td><p>Please enter the following information about the bookstore and yourself</p>
<form action="<%=response.encodeURL(URLInterface.URLBOOKSTOREREGISTER+"?"+queryString)%>" method=post>
<input type=hidden name="<%=bookstoreSignin.HIDDENID_FIELD%>" value="<%=hiddenid%>">
<table width="100%">
<%if (error!=null){%>
	<tr><td colspan=2><font color=#FF0000><b>error: </b><%=error%></font></td></tr>
<%}%>
<tr valign=top><td width="150"><font color="#CC3300">*</font>Email:</td>
<td align=left><input type=text name="<%=bookstoreSignin.EMAIL_FIELD%>" value="<%=tools.htmlEncode(email)%>" size=30 maxlength=<%=bookstoreUsersTable.EMAIL_MAX%>></td></tr>
<tr valign=top><td width="150">Password:</td>
<td align=left><input type=password disabled style="background-color:#CCCCCC"><font color="#000080" size="2"> - a temporary password will be provided to you</font></td></tr>
<tr valign=top>
<td width="150"><font color="#CC3300">*</font>your First Name:</td>
<td align=left><input type=text name="<%=bookstoreSignin.FIRSTNAME_FIELD%>" value="<%=tools.htmlEncode(firstname)%>" size=30 maxlength=<%=bookstoreInfoTable.FIRSTNAME_MAX%>></td></tr>
<td width="150"><font color="#CC3300">*</font>your Last Name:</td>
<td align=left><input type=text name="<%=bookstoreSignin.LASTNAME_FIELD%>" value="<%=tools.htmlEncode(lastname)%>" size=30 maxlength=<%=bookstoreInfoTable.LASTNAME_MAX%>></td></tr>
<td width="150"><font color="#CC3300">*</font>Bookstore:</td>
<td align=left><input type=text name="<%=bookstoreSignin.BOOKSTORE_FIELD%>" value="<%=tools.htmlEncode(bookstore)%>" size=30 maxlength=<%=bookstoreInfoTable.STORENAME_MAX%>></td></tr>
<tr valign=top>
<td width="150"><font color="#CC3300">*</font>Contact Info:<br>
<font color="#000080" size="2">phone, fax, email...</font></td>
<td align=left>
<textarea name="<%=bookstoreSignin.CONTACT_FIELD%>" rows=5 cols=40 onkeyup="limitText(this,<%=bookstoreInfoTable.CONTACTINFO_MAX%>)"><%=tools.htmlEncode(contact)%></textarea>
</td>
</tr>
<tr valign=top>
<td width="150">
<font color="#CC3300">*</font>Address:<br><font color="#000080" size="2">street, city, state...</font></td>
<td align=left><textarea name="<%=bookstoreSignin.ADDRESS_FIELD%>" rows=5 cols=40 onkeyup="limitText(this,<%=bookstoreInfoTable.ADDRESSINFO_MAX%>)"><%=tools.htmlEncode(address)%></textarea></td></tr>
<tr valign=top>
<td width="150">existing website:</td>
<td align=left>
http://<input type=text name="<%=bookstoreSignin.WEBSITE_FIELD%>" value="<%=tools.htmlEncode(website)%>" size=40 maxlength=<%=bookstoreInfoTable.WEBSITEINFO_MAX%>></td>
<tr valign=top><td colspan=2>any additional information:</td></tr>
<tr valign=top>
<td align=left colspan=2>
<textarea name="<%=bookstoreSignin.MOREINFO_FIELD%>" rows=5 cols=60 onkeyup="limitText(this,<%=bookstoreInfoTable.MOREINFO_MAX%>)"><%=tools.htmlEncode(moreinfo)%></textarea></td></tr>
</tr>
</table>
<p align=center><textarea rows="10" readonly cols="40">
<%@ include file="/html/terms.txt" %>
</textarea></p>
<p align=left><b>Please check the box below: </b><br>
<input type="checkbox" name="<%=bookstoreSignin.AGREEFIELD%>" value="1">&nbsp;&nbsp;I accept the User Agreement and Privacy Policy above.</p>
<p align=center><input type=submit value="submit" style="color: #333399"></p>
</form>
</td>
</tr>
</table>
<%}else{%>
Thank you for registering with tradeusedbooks.com. Your registration still 
requires our confirmation and thus until we verify that all the data is indeed 
correct you will not have access to your account. This process should take no 
longer than a few days and once your registration is processed you will receive 
an email from us with some important information.<p>thank 
you for using tradeusedbooks.com,<br>tradeusedbooks.com team
<%}%>
</td></tr>
<!-- main content ends-->
<!-- footer begins -->
<%@ include file="/html/footer.html" %>
<!-- footer ends -->	
</table>
</body>
</html>