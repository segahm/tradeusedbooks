<%@ page import="server.*" %>
<%@ include file="/jsp/allTags.jsp" %>
<%--requirements: mailInterface.[USERATTR,EMAILATTR,
BOOKATTR,MOREATTR,MESSAGEATTR] --%>
<%
usersTable buyer = (usersTable)request.getAttribute(mailInterface.MOREATTR);
booksTable book = (booksTable)request.getAttribute(mailInterface.BOOKATTR);
String college = (String)request.getAttribute(BuyServlet.ATTR_COLLEGE);
String buyer_id = (String)request.getAttribute("buyer_id");
String book_id = (String)request.getAttribute("book_id");

%>
<my:sendmail emailAddress="<%=(String)request.getAttribute(mailInterface.EMAILATTR)%>" 
subject="someone has requested to buy your book on tradeusedbooks.com" sendAsHTML="false">
Dear <%=(String)request.getAttribute(mailInterface.USERATTR)%>,
Congratulations! We have just received a confirmation from 
<%=buyer.getUsername()%> (<%=buyer.getEmail()%>) proposing to buy "<%=book.getTitle()%>" by <%=book.getAuthor()%> for $<%=book.getPrice()%> within the area of <%=college%>. Please contact the buyer ASAP for he/she might start looking for another book. Please DO NOT REPLY to this message--for it was sent from tradeusedbooks.com and not from the buyer.
Here is what <%=buyer.getUsername()%> writes:
>>>
<%=(String)request.getAttribute(mailInterface.MESSAGEATTR)%>
<<<
Also, to help you sell this book we have decided to give you a few pointers on what to do next. 
Here is what we were able to come up with:
1) contact the buyer about the best place and time to meet letting him/her know about your time schedule
3) exchange cell phone numbers, aim, icq  - whatever you can come up with (sending endless emails to each other is a hassle)
4) give him/her a call on the day you meet to make sure that everything goes as planned
5) meet at the scheduled time and place (and don't forget to bring your textbook :-)
6) and finally, come back to tradeusedbooks.com and leave your feedback or follow the link below
http://www.tradeusedbooks.com<%=response.encodeURL(
URLInterface.URLACCOUNTFEEDBACK+"?"+accoInterface.BOOKS_BOOKID
+"="+book_id+"&"+accoInterface.BOOKS_BIDS_USERID+"="+buyer_id)%>
we wish you good luck in selling this one and many more to come,
<%@ include file="mailFooterText.jsp" %>
</my:sendmail>