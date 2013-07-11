<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="true" %>
<%@ page import="server.*" %>
<%@ include file="/jsp/allTags.jsp" %>
<%
String hiddenID = (String)session.getAttribute(bookstoreAccount.HIDDENID);
String email = (String)request.getAttribute(bookstoreAccount.EMAIL_FIELD);
String firstname = (String)request.getAttribute(bookstoreAccount.FIRSTNAME_FIELD);
String lastname = (String)request.getAttribute(bookstoreAccount.LASTNAME_FIELD);
String storename = (String)request.getAttribute(bookstoreAccount.STORENAME_FIELD);
String contact = (String)request.getAttribute(bookstoreAccount.CONTACT_FIELD);
String address = (String)request.getAttribute(bookstoreAccount.ADDRESS_FIELD);
String moreinfo = (String)request.getAttribute(bookstoreAccount.MOREINFO_FIELD);
String website = (String)request.getAttribute(bookstoreAccount.WEBSITE_FIELD);
String error = (String)request.getAttribute(bookstoreAccount.ERRORMESSAGE);
if (website == null){
	website = "";
}
if (moreinfo == null){
	moreinfo = "";
}
%>
<%if (error != null){%>
<font color=#FF0000><b>error: </b><%=error%></font>
<%}%>
<form action="<%=response.encodeURL(URLInterface.URL_BOOKSTORE_ACCOUNT_UPDATE+"?user")%>" method=post>
<input type=hidden name="<%=bookstoreAccount.HIDDENID%>" value="<%=hiddenID%>">
<table width="100%">
<tr valign="top"><td colspan=2><b>Password change</b><br>&nbsp;</td></tr>
<tr valign="top"><td colspan=2>Please enter the following<br>&nbsp;</td></tr>
<tr valign="top">
<td style="padding-left:30px"><b>new password</b></td>
<td><input type=password name="<%=bookstoreAccount.NEWPASS1_FIELD%>" value="" size=15 maxlength="<%=bookstoreUsersTable.PASSWORD_MAX%>"> (leave blank if no change is going to be made)</td>
</tr>
<tr valign="top">
<td style="padding-left:30px"><b>verify:</b></td>
<td><input type=password name="<%=bookstoreAccount.NEWPASS2_FIELD%>" value="" size=15 maxlength="<%=bookstoreUsersTable.PASSWORD_MAX%>"> (re-enter the new password)</td>
</tr>
<tr valign="top"><td colspan=2>&nbsp;<br><b>Email change</b><br>&nbsp;</td></tr>
<tr valign="top"><td colspan=2>Your current email is <i><%=tools.htmlEncode(email)%></i><br>would you like to change it?<br>&nbsp;</td></tr>
<tr valign="top">
<td style="padding-left:30px"><b>new email</b></td>
<td><input type=text name="<%=bookstoreAccount.EMAIL_FIELD%>" value="" size=20 maxlength="<%=bookstoreUsersTable.EMAIL_MAX%>"> (leave blank if no change is going to be made)</td>
</tr>
<tr valign="top"><td colspan=2>&nbsp;<br><b>Identity Verification</b><br>&nbsp;</td></tr>
<tr valign="top"><td colspan=2>Please enter your <b>current password</b> as a verification of 
	your identity. This is to prevent unauthorized changes from being made to 
	your account.<br>&nbsp;</td></tr>
<tr valign="top">
<td style="padding-left:30px"><b>password:</b></td>
<td><input type=password name="<%=bookstoreAccount.OLDPASS_FIELD%>" value="" size=15 maxlength="<%=bookstoreUsersTable.PASSWORD_MAX%>"></td></tr>
</table>
<center><input type=submit style="color: #333399;" value="update user"></center>
</form>
<form action="<%=response.encodeURL(URLInterface.URL_BOOKSTORE_ACCOUNT_UPDATE+"?info")%>" method=post>
<input type=hidden name="<%=bookstoreAccount.HIDDENID%>" value="<%=hiddenID%>">
<table width="100%">
<tr><td colspan=2><b>Bookstore information change</b></td></tr>
<tr valign="top">
<td style="padding-left:30px" width="150"><font color="#cc3300">*</font>your First Name:</td>
<td align=left><input type=text name="<%=bookstoreAccount.FIRSTNAME_FIELD%>" value="<%=tools.htmlEncode(firstname)%>" size=30 maxlength="<%=bookstoreInfoTable.FIRSTNAME_MAX%>"></td>
</tr>
<tr valign="top">
<td style="padding-left:30px" width="150"><font color="#cc3300">*</font>your Last Name:</td>
<td align=left><input type=text name="<%=bookstoreAccount.LASTNAME_FIELD%>" value="<%=tools.htmlEncode(lastname)%>" size=30 maxlength="<%=bookstoreInfoTable.LASTNAME_MAX%>"></td>
</tr>
<tr valign="top">
<td style="padding-left:30px" width="150"><font color="#cc3300">*</font>Bookstore:</td>
<td align=left><input type=text name="<%=bookstoreAccount.STORENAME_FIELD%>" value="<%=tools.htmlEncode(storename)%>" size=30 maxlength="<%=bookstoreInfoTable.STORENAME_MAX%>"></td>
</tr>

<tr vAlign="top">
	<td style="padding-left:30px" width="150"><font color="#cc3300">*</font>Contact Info:<br>
	<font color="#000080" size="2">phone, fax, mail address...</font></td>
	<td align="left"><textarea name="<%=bookstoreAccount.CONTACT_FIELD%>" rows="5" cols="40" onkeyup="limitText(this,<%=bookstoreInfoTable.CONTACTINFO_MAX%>)"><%=tools.htmlEncode(contact)%></textarea> 
	</td>
</tr>
<tr vAlign="top">
	<td style="padding-left:30px" width="150"><font color="#cc3300">*</font>Address:<br>
	<font color="#000080" size="2">street, city, state...</font></td>
	<td align="left"><textarea name="<%=bookstoreAccount.ADDRESS_FIELD%>" rows="5" cols="40" onkeyup="limitText(this,<%=bookstoreInfoTable.ADDRESSINFO_MAX%>)"><%=tools.htmlEncode(address)%></textarea></td>
</tr>

<tr vAlign="top">
	<td width="150">existing website:</td>
	<td align="left">http://<input size="40" name="<%=bookstoreAccount.WEBSITE_FIELD%>" value="<%=tools.htmlEncode(website)%>" maxlength="<%=bookstoreInfoTable.WEBSITEINFO_MAX%>"></td>
</tr>
<tr vAlign="top">
	<td colSpan="2">a welcome message on your page and any additional 
	information:</td>
</tr>
<tr vAlign="top">
	<td align="left" colSpan="2"><textarea name="<%=bookstoreAccount.MOREINFO_FIELD%>" rows="5" cols="60" onkeyup="limitText(this,<%=bookstoreInfoTable.MOREINFO_MAX%>)"><%=tools.htmlEncode(moreinfo)%></textarea></td>
</tr>

</table>
<center><input type=submit style="color: #333399;" value="update bookstore"></center>
</form>