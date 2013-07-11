<%--
signin depends on 
session.getAttribute(authInterface.SESSIONFIELDATTR);
request.getAttribute(authInterface.ERRORMESSAGE1ATTR);//can be null
request.getAttribute(authInterface.ERRORMESSAGE2ATTR);//can be null
--%>
<tr><td>
	<table width="100%">
<tr valign=top>
	<td width="45%" style="border-right: 1px solid #CCCCCC;">
<table width="100%">
<tr><td><h1>New User</h1></td></tr>
	<tr><td>
Signing up for an account is free and easy. As a registered user, you can
<ul>
	<li>Buy books</li>
	<li>Post/Sell books</li>
	<li>Post to wanted</li>
	<li>Manage your account</li>
</ul>
<form method="post" 
action="<%=response.encodeURL(URLInterface.URLAUTHREGISTER+"?"+fromURL)%>">
<center><input style="color: #333399" type="submit" value="Sign Up Today!"></center>
</form>
	</td></tr>
</table>
	</td>
<td width="50%">
<table width="100%">
<tr><td><h1>Returning User</h1></td></tr>
<tr><td>
<form method="post" action="<%=response.encodeURL(URLInterface.URLAUTHSIGNIN+"?"+fromURL)%>">
<input type=hidden name="<%=authInterface.SESSIONFIELDATTR%>" value="<%=request.getAttribute(authInterface.SESSIONFIELDATTR)%>">
Username:&nbsp;
<input type=text size="15" name="<%=authInterface.USERFIELD%>" value="">
<%-- error message --%>
<%if (request.getAttribute(authInterface.ERRORMESSAGE1ATTR)!=null){%>
<font color='#FF0000'><b>error:</b> <%=request.getAttribute(authInterface.ERRORMESSAGE1ATTR)%></font>
<%}%>
<br>
<a style="text-decoration: none;color: #333399;font-size:12px;" href="<%=response.encodeURL(URLInterface.URLAUTHFORGOT+"?u")%>">Forgot your username?</a><br>
Password:&nbsp;
<input type=password size="15" name="<%=authInterface.PASSFIELD%>" value="" style="margin-top:10px;">
<%-- error message --%>
<%if (request.getAttribute(authInterface.ERRORMESSAGE2ATTR)!=null){%>
<font color='#FF0000'><b>error:</b> <%=request.getAttribute(authInterface.ERRORMESSAGE2ATTR)%></font>
<%}%>
<br>
<a style="text-decoration: none;color: #333399;font-size:12px;" href="<%=response.encodeURL(URLInterface.URLAUTHFORGOT+"?p")%>">Forgot your password?</a><br>
<center>
<input style="color: #333399" type="submit" value="Sign In"></center>
</form>
</td>
</tr>
</table>
</td></tr>
</table>
</td>
</tr>