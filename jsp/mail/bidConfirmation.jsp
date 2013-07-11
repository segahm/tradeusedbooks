<%@ page import="server.*" %>
<%@ include file="/jsp/allTags.jsp" %>
<%--requirements: mailInterface.[USERATTR,EMAILATTR] --%>
<%
//all attributes should be passed as nonempty,nonnull strings
//no formating will be done here
booksTable book = (booksTable)request.getAttribute(
	mailInterface.BOOKATTR);
String title = book.getTitle();
String author = book.getAuthor();
String condition = booksTable.CONDITIONS[book.getCondition()];
String price = book.getPrice()+"";

String seller_id = (String)request.getAttribute("seller_id");
String book_id = (String)request.getAttribute("book_id");
%>
<my:sendmail emailAddress="<%=(String)request.getAttribute(mailInterface.EMAILATTR)%>" 
subject="Bid confirmation from tradeusedbooks.com">
<html><body>
<p>Dear <%=(String)request.getAttribute(mailInterface.USERATTR)%>,</p>
<p>This email was sent to you as a confirmation of your purchase. We recomend that you 
follow the general guidelines provided on our web site in order to make the process easier. Also, 
don't forget to leave your feedback for this user by going to <a href="http://www.tradeusedbooks.com<%=response.encodeURL(
URLInterface.URLACCOUNTFEEDBACK+"?"+accoInterface.BOOKS_BOOKID
+"="+book_id+"&"+accoInterface.BOOKS_BIDS_USERID+"="+seller_id)%>">http://www.tradeusedbooks.com<%=response.encodeURL(
URLInterface.URLACCOUNTFEEDBACK+"?"+accoInterface.BOOKS_BOOKID
+"="+book_id+"&"+accoInterface.BOOKS_BIDS_USERID+"="+seller_id)%></a>.<br> And of course, we wish you good luck in purchasing this textbook.</p>
<p style='margin-top: 0; margin-bottom: 0'>Here is the book that you have placed your bid on:</p>
<p style='margin-top: 0; margin-bottom: 0'>Title: <b><%=tools.htmlEncode(title)%></b></p>
<p style='margin-top: 0; margin-bottom: 0'>Author: <b><%=tools.htmlEncode(author)%></b></p>
<p style='margin-top: 0; margin-bottom: 0'>Price: <b>$<%=price%></b></p>
<p style='margin-top: 0; margin-bottom: 0'>Condition: <b><%=condition%></b></p>
<p>For information about this book please visit your account page.</p>
<p style='margin-bottom: 0'>Once again, thanks for using 
tradeusedbooks.com,</p>
<%@ include file="mailFooter.jsp" %>
</my:sendmail>