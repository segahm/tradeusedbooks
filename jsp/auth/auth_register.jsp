<%if (request.getAttribute(authInterface.PAGEATTR).equals(authInterface.PAGE1)){%>
<%
String field_email = tools.htmlEncode(
	(String)request.getAttribute(authInterface.EMAILFIELD));
String field_user = tools.htmlEncode(
	(String)request.getAttribute(authInterface.USERFIELD));
String error_user = (String)request
	.getAttribute(authInterface.ERRORMESSAGE1ATTR);
String error_email = (String)request
	.getAttribute(authInterface.ERRORMESSAGE2ATTR); 
%>
<tr>
<td>
<table width="100%" cellpadding="5">
<tr valign=top>
<td>
<h1>Sign up</h1>
Please fill out the
form below and read our <a class="refLinks" target=_blank href="<%=response.encodeURL(URLInterface.URLPRIVACY)%>">privacy policy</a> regarding information you provide.
<br>
<form method=post action="<%=response.encodeURL(URLInterface.URLAUTHREGISTER+"?"+fromURL)%>">
<input type=hidden name="<%=authInterface.SESSIONFIELDATTR%>" value="<%=session.getAttribute(authInterface.SESSIONFIELDATTR)%>">
<table width="100%">
<tr><td nowrap width="80">Username:</td><td nowrap>
<input type=text name=<%=authInterface.USERFIELD%> 
value="<%=tools.htmlEncode(field_user)%>" 
size="15" maxlength="<%=usersTable.USERNAME_MAX%>">
<font color="#333399" style="font-size:14px">- username (4 - 20 characters long) that others would be able to see</font>
</td></tr>
	<%if (error_user!=null){%>
	<tr><td colspan=2><font color='#FF0000'>
	<b>error:</b> <%=error_user%></font></td></tr>
	<%}%>
<tr><td>Email:</td><td><input type=text 
name=<%=authInterface.EMAILFIELD%> 
value="<%=tools.htmlEncode(field_email)%>" 
size="30" maxlength="<%=usersTable.EMAIL_MAX%>">
<font color="#333399" style="font-size:14px">- email verification letter will be send to this address</font>
<%--<font size="2" color="#FF0000" face="Times New Roman"><br>note: 
	please do <b>not</b> use your college account</font> - 
	<a class="refLinks" href="javascript:whyemail()">read why</a>--%>
</td></tr>
	<%if (error_email!=null){%>
	<tr><td colspan=2><font color='#FF0000'>
	<b>error:</b> <%=error_email%></font></td></tr>
	<%}%>
<tr><td>Password:</td><td nowrap><input type=text disabled size="10" style="background-color:#CCCCCC">
<font color="#333399" style="font-size:14px">- a temporary password will be provided to you by email</font>
</td></tr>
</table>
<center><input style="color: #333399" type=submit value="Submit"></center>
</form>
</td>
</tr>
</table>
</td>
</tr>
<%}else{%>
<tr>
<td>
<p align=center><font size="4" color="#FF0000">PLEASE NOTE: You must take 
further action to complete this registration!</font></p>
<p>In a few minutes you should receive an email from<i>
server@tradeusedbooks.com</i> that will 
contain a confirmation link. Once you click on it you will be asked to enter 
your username and password. We require this in order to make sure that your 
email is valid. Please confirm your registration within 3 days, as we delete 
unconfirmed registrations from our database after that period of time. However, we would advise 
you to confirm it as soon as possible.</p>
</td>
</tr>
<%}%>