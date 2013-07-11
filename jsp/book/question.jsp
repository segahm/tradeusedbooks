<%@ page session="true" %>
<%@ page import="server.*" %>
<%@ include file="/jsp/allTags.jsp" %>
<%
final String HEADERTITLE = "Question";
booksTable book = (booksTable)request.getAttribute(QuestionServlet.BOOK_ATTR);
usersTable seller = (usersTable)request.getAttribute(QuestionServlet.SELLER_ATTR);
usersTable asker = (usersTable)request.getAttribute(QuestionServlet.USER_ATTR);
boolean sentOccured = false;
if (request.getAttribute(QuestionServlet.SUCCESS_SEND) != null){
	sentOccured = true;
}
%>
<html>
<head>
<%@ include file="/html/meta.html"%>
<meta name="Robots" content="noindex">
<link rel="stylesheet" type="text/css" href="/styles/main.css">
<title>TradeUsedBooks.com Question</title>
<script language=javascript>
function limitText(field,limit){
	if (field.value.length>limit){
		field.value = field.value.substr(0,limit);
	}
}
</script>
</head>
<body bgColor=#ffffff>
<table width=100%>
<%-- header begins --%>
<%@ include file="/jsp/generalHeader.jsp"%>
<%-- header ends --%>
<!--main content begins -->
<%if (!sentOccured){%>
<tr><td>
<p>Please fill out the form below to send an email to the seller of this title. Please note that your message will be send as plain text only (i.e. no HTML). We ask you to refrain from using any obscenity as it is inappropriate and will result in your account suspension. <br><font size=-1>We take a record of your IP address and user id on this page.</font></p>
<p style="margin-left:10px"><font color=#FF0000># of questions used today is <%=asker.getNumberMessages()%> out of <%=usersTable.DAILY_MESSAGES_MAX%> allowed</font></p>
<form action="<%=URLInterface.URLQUESTION%>" method=post>
<input type=hidden name="<%=QuestionServlet.BOOKID_FIELD%>" value="<%=book.getID()%>">
<input type=hidden name="<%=QuestionServlet.SELLERID_FIELD%>" value="<%=seller.getID()%>">
<%--simply security feature--%>
<input type=hidden name="<%=QuestionServlet.HIDDENID_FIELD%>" value="<%=(String)request.getAttribute(QuestionServlet.HIDDENID_FIELD)%>">
<table>
<tr valign=top><td width=100><b>To:</b></td><td><%=seller.getUsername()%></td></tr>
<tr valign=top><td width=100><b>From:</b></td><td><%=asker.getUsername()%> (<%=asker.getEmail()%>)</td></tr>
<tr valign=top><td width=100><b>Title:</b></td><td>"<%=tools.htmlEncode(book.getTitle())%>" by <%=tools.htmlEncode(book.getAuthor())%> ($<%=book.getPrice()%>)</td></tr>
<tr valign=top><td width=100><b>Message:</b></td><td>
<textarea name=<%=QuestionServlet.MESSAGE_FIELD%> rows=5 cols=50 onkeyup="limitText(this,1000)"></textarea>
<br><font color=#FF0000 size=-1>note: your contact information will be automatically included</font></td></tr>
</table>
<center><input type=submit value="send question"></center>
</form>
</td></tr>
<%}else{%>
<tr><td>
Your question was sent successfully.<p>
<a href="<%=response.encodeURL(URLInterface.URLBOOK)+book.getID()%>" 
class="refLinks">click here to return back to the book page</a>
</td></tr>
<%}%>
<!-- main content ends -->
<%-- footer begins --%>
<%@ include file="/html/footer.html"%>
<%-- footer ends --%>
</table>
</body>
</html>