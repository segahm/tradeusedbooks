<%@ page session="true" %>
<%@ page import="server.*" %>
<%@ include file="/jsp/allTags.jsp" %>
<%
final String HEADERTITLE = "Purchase Confirmed";
%>
<html>
<head>
<%@ include file="/html/meta.html"%>
<meta name="description" content="">
<meta name="Robots" content="all">
<link rel="stylesheet" type="text/css" href="/styles/main.css">
<title>TradeUsedBooks.com Purchase Confirmed</title>
</head>
<body bgColor=#ffffff>
<table width=100%>
<%-- header begins --%>
<%@ include file="/jsp/generalHeader.jsp"%>
<%-- header ends --%>
<!--main content begins -->
<tr><td>
<p align="center" style="margin-top: 0; margin-bottom: 0">
<font size="5" color="#0000CC">Congradulations!</font></p>
<p align="center" style="margin-top: 0; margin-bottom: 0">&nbsp;<font color="#0000ff">-</font>
<font color="#0000FF">you have successfully proposed to buy this title</font></p>

<p align="left">Note: the seller of the book should contact you within a few days about the means to sell the book to you. We recommend that you and the seller discuss all the possible arrangements of time and place when each one of you is available in order to meet at the most convenient time for both of you. Some of the best places to meet are usually places where you can easily be recognized and places that are well known (such as an entrance to the bookstore or that of the library). Also, you may have noticed that we did not provide you with seller's contact information - this is done to keep our users' membership information private (i.e. we do not support spam).
<br>
good luck with your purchase,<br>
tradeusedbooks.com team
</p>

<p align="center"><a href="<%=response.encodeURL(URLInterface.URLACCOUNTBIDS)%>" class="refLinks">Go To My Account</a></p>
</td></tr>
<!-- main content ends -->

<%-- footer begins --%>
<%@ include file="/html/footer.html"%>
<%-- footer ends --%>
</table>
</body>
</html>