<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="true" %>
<%@ page import="server.*" %>
<%@ include file="/jsp/allTags.jsp" %>
<%!
//declarations
%>
<%
final String HEADERTITLE = (String)request.getAttribute(confInterface.HEADERTITLE);
%>
<html>

<head>
<meta name="description" content="Confirmation page.">
<meta http-equiv="Cache-Control" content="no-cache" />
<META NAME="Robots" CONTENT="all">
<%@ include file="/html/meta.html" %>
<link rel="stylesheet" type="text/css" href="/styles/main.css">
<title>TradeUsedBooks.com - <%=HEADERTITLE%></title>
</head>
<body bgColor=#ffffff>
<table width="100%" border="0" cellspacing="0" cellpadding="0" align=center>
<!-- header begins -->
<%@ include file="/jsp/generalHeader.jsp" %>
<!-- header ends -->
<!-- main content begins -->
<tr><td>
<table width="100%" cellpadding="5">
<tr valign=top>
<td width="35%" style="border-right: 1px solid #CCCCCC;">
Why do we require this?<ul>
	<li>to make sure all emails are valid</li>
	<li>to prevent automatic registrations<li>to keep your information private</ul>
<p>What is my &quot;username&quot;?</p>
<p>Your username is the username you 
entered when you first signed up for this service.<br>
<font color="#CC0000">hint:</font> it 
can also be found in your email</p>
<p>What is my &quot;password&quot;?</p>
<p>This field refers to the temporary password that you were supposed to receive 
in the same email as the link to this page.<br>
<font color="#CC0000">hint:</font> look for the word &quot;password&quot;
<p>Has it been more than 3 days since your registration?<p>
Unfortunately all unconfirmed registrations are automatically deleted after 3 days. 
So, if you've tried confirming your registration and it has been more than 3 days, then we would advise you to register once again 
(but this time, be sure to confirm your email).
<p>Still need help?<p>If you are still having problems 
with this, email us at <a href="mailto:support@tradeusedbooks.com">support@tradeusedbooks.com</a> with your email address attached.</td>
<td>
<p align=left>Please enter the following:</p>
<form action="<%=response.encodeURL(URLInterface.URLCONFIRMUSER)%>" method=post>
<input type=hidden name="<%=confInterface.SESSIONFIELDATTR%>" value="<%=(String)session.getAttribute(confInterface.SESSIONFIELDATTR)%>">
<%if (request.getAttribute(confInterface.ERRORMESSAGEATTR)!=null){%>
	<font color='#FF0000'><b>error:</b> <%=(String)request.getAttribute(confInterface.ERRORMESSAGEATTR)%></font><br>
<%}%>
<p>Username:&nbsp;&nbsp;<input type=text name="<%=confInterface.USERFIELD%>" value="" size=15>
<%if (request.getAttribute(confInterface.ERRORMESSAGE1ATTR)!=null){%>
	<font color='#FF0000'><b>error:</b> <%=(String)request.getAttribute(confInterface.ERRORMESSAGE1ATTR)%></font>
<%}%>
</p>
<p>Password:&nbsp;&nbsp;<input type=password name="<%=confInterface.PASSFIELD%>" value="" size=15>
<%if (request.getAttribute(confInterface.ERRORMESSAGE2ATTR)!=null){%>
	<font color='#FF0000'><b>error:</b> <%=(String)request.getAttribute(confInterface.ERRORMESSAGE2ATTR)%></font>
<%}%>
</p>
<p align=center><textarea rows="10" readonly cols="40">
<%@ include file="/html/terms.txt" %>
</textarea></p>
<p align=left><b>Please check the box below: </b><br>
<input type="checkbox" name="<%=confInterface.AGREEFIELD%>" value="1">&nbsp;&nbsp;I accept the User Agreement and Privacy Policy above.</p>
<p align=center><input style="color: #333399" type="submit" value="register me"></p>
</form>
<p align="left">Once you submit this information you will be redirected to your 
account page; there you can change your password and manage your account.<p align="left">&nbsp;</td>
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