<tr><td align=center>
<p>Please enter the email address you use for this account.</p>
<form action="<%=response.encodeURL(URLInterface.URLAUTHFORGOT+"?"+fromURL)%>" method=post>
<p>Email: <input type=text name=<%=authInterface.EMAILFIELD%> value="" size=25>
&nbsp;&nbsp;
<a class="refLinks" 
href="<%=response.encodeURL(URLInterface.URLAUTHSIGNIN)%>">back to sign in</a>
<br>
<%if (request.getAttribute(authInterface.ERRORMESSAGE1ATTR)!=null){%>
<font color="blue">Message: </font><font color="#993333"><%=request.getAttribute(authInterface.ERRORMESSAGE1ATTR)%></font>
<%}%>
</p>
<center><input style="color: #333399" type=submit value="submit"></center>
</form>
</td></tr>