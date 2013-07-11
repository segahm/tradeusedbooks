<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="true" %>
<%@ page import="server.*" %>
<%@ include file="/jsp/allTags.jsp" %>
<%if (accoInterface.PAGE1.equals((String)request.getAttribute(accoInterface.PAGEATTR))){%>
<h1>Account Update</h1>
	<%if (request.getAttribute(accoInterface.UPDATE_ERRORMESSAGE) != null){%>
	<img src="/myImages/alert.gif">
	<font color=#FF0000>
	<b>error: </b>
	<%=(String)request
		.getAttribute(accoInterface.UPDATE_ERRORMESSAGE)%></font>
	<%}%>
	<form method="post" 
		action="<%=response.encodeURL(URLInterface.URLACCOUNTUPDATE)%>">
	<%-- making sure that the form is submited from this page --%>
	<input type=hidden 
		name="<%=accoInterface.SESSIONFIELDATTR%>" value="<%=(String)session.getAttribute(accoInterface.SESSIONFIELDATTR)%>">
	<h4>Password Change</h4>
	<p>Please enter the following</p>
	<table style="MARGIN-LEFT: 20px" 
		cellSpacing="0" cellPadding="5" border="0">
	<tr>
		<td><b>new password</b>:</td>
		<td><input type=password size="15" value="" 
			name="<%=accoInterface.UPDATE_NEWPASS1%>"> (leave blank if no change is going to be made)</td>
	</tr>
	<tr>
		<td><b>verify</b>:</td>
		<td><input type=password size="15" value="" 
			name="<%=accoInterface.UPDATE_NEWPASS2%>"> (re-enter the new password) </td>
	</tr>
	</table>
	<h4>Username Change</h4>
	<p>your current username is <i><%=(String)request
		.getAttribute(accoInterface.UPDATE_USERATTR)%></i><br>
	would you like to change it?</p>
	<table style="MARGIN-LEFT: 20px" cellSpacing="0" cellPadding="5" border="0">
	<tr>
		<td><b>new username</b>:</td>
		<td><input type=text size="15" value="" 
		name="<%=accoInterface.UPDATE_NEWUSERNAME%>"> (leave blank if no change is going to be made)</td>
	</tr>
	</table>
	<h4>Email Change</h4>
	<p>your current email is <i><%=(String)request
		.getAttribute(accoInterface.UPDATE_EMAILATTR)%></i><br>
	would you like to change it?</p>
	<table style="MARGIN-LEFT: 20px" cellSpacing="0" cellPadding="5" border="0">
	<tr>
		<td><b>new email</b>:</td>
		<td><input type=text size="25" value="" 
			name="<%=accoInterface.UPDATE_NEWEMAIL%>"> (leave blank if no change is going to be made)</td>
	</tr>
	</table>
	<h4>Identity Verification</h4>
	<p>Please enter your <b>current password</b> as a verification 
	of your identity. This is to prevent unauthorized changes 
	from being made to your account.</p>
	<table style="MARGIN-LEFT: 20px" cellSpacing="0" cellPadding="5" border="0">
	<tr>
		<td width="80"><b>password</b>:</td>
		<td><input type=password size="15" value="" 
		name="<%=accoInterface.UPDATE_OLDPASS%>"> </td>
	</tr>
	</table>
		<center><input type="submit" value="submit" name="submit" style="color: #333399;"></center>
	</form>
<%}else{%>
<h1>Account Update</h1>
Your account has been updated.<p>
<a href="<%=response.encodeURL(URLInterface.URLACCOUNTUPDATE)%>" 
class="refLinks">click here to return back to account update</a>
<%}%>