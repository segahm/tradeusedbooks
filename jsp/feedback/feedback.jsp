<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="true" %>
<%@ page import="server.*" %>
<%@ include file="/jsp/allTags.jsp" %>
<%
final String HEADERTITLE = "Feedback";
resultList list = (resultList)request.getAttribute(
	feedbackServlet.LISTATTR);
usersTable user = (usersTable)request.getAttribute(
	feedbackServlet.USERATTR);
int[] summaryFeedback = user.getFeedback();
int totalFeedback = summaryFeedback[0]+summaryFeedback[1]+summaryFeedback[2];
int summary_positive;
int summary_neutral;
int summary_negative;
if (totalFeedback != 0){
	summary_positive = (int)(100/totalFeedback*summaryFeedback[0]);
	summary_neutral = (int)(100/totalFeedback*summaryFeedback[1]);
	summary_negative = (int)(100/totalFeedback*summaryFeedback[2]);
}else{
	summary_positive = 0;
	summary_neutral = 0;
	summary_negative = 0;
}
%>
<html>
<head>
<%@ include file="/html/meta.html"%>
<meta name="description" content="">
<meta name="Robots" content="all">
<link rel="stylesheet" type="text/css" href="/styles/main.css">
<title>TradeUsedBooks.com feedback</title>
</head>
<body bgColor=#ffffff>
<table>
<%-- header begins --%>
<%@ include file="/jsp/generalSearchHeader.jsp"%>
<%-- header ends --%>
<%--main content begins --%>
<tr><td>
<p align="center"><b><font color="#000080"><%=user.getUsername()%>&#39;s feedback</font></b></p>
<p align="left"><font color="#333399">Summary:</font></p>
<p align="left">
<font color="#009B00">Positive: </font><%=summaryFeedback[0]%> (<%=summary_positive%>%)<br>
<font color="#000000">Neutral: </font><%=summaryFeedback[1]%> (<%=summary_neutral%>%)<br>
<font color="#CC3300">Negative: </font><%=summaryFeedback[2]%> (<%=summary_negative%>%)
</p>
<%if (list!=null){%>
<p align="left"><font color="#333399">Comments:</font><br></p>
<table width="600" cellpadding=0 cellspacing=0>
<%Object[] row = (Object[])list.next();
while (row != null){
String username = (String)row[1];
feedbackTable feedback = (feedbackTable)row[0];
%>
<tr bgColor='#eeeeee' style='font-size: 8pt; font-family: arial,helvetica,sans-serif'>
<td>book id</td><td colspan=2>
date posted: <%=feedback.getDate()%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;posted by <a href="/feedback?<%=feedback.getTraderID()%>" 
style="color:#000080"><%=username%></a></td></tr>
<tr bgColor='#ffffff' valign=top>
<td width='200' align=left><%="..."+feedback.getBookID().substring(feedback.getBookID().length()-6,feedback.getBookID().length())%></td>
<td width='150' align=left>
<%if (feedback.getPositiveness() == 1){%>
	<font color="#009B00">positive:</font>
<%}%>
<%if (feedback.getPositiveness() == 0){%>
	<font color="#000000">neutral:</font>
<%}%>
<%if (feedback.getPositiveness() == -1){%>
	<font color="#cc3300">negative:</font>
<%}%>
</td><td vAlign=top align=left width='78%' style='font-size:12pt;padding-left:5px'>
<%=tools.htmlEncode(feedback.getFeedback())%>
</td></tr>
<%row = (Object[])list.next();
}%>
</table>
<%}%>
</td></tr>
<%-- main content ends --%>
<%-- footer begins --%>
<%@ include file="/html/footer.html"%>
<%-- footer ends --%>
</table>
</body>
</html>