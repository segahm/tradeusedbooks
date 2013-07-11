<%@ page import="server.*" %>
<%@ include file="/jsp/allTags.jsp" %>
<%
usersTable asker = (usersTable)request.getAttribute(mailInterface.MOREATTR);
booksTable book = (booksTable)request.getAttribute(mailInterface.BOOKATTR);
%>
<my:sendmail emailAddress="<%=(String)request.getAttribute(mailInterface.EMAILATTR)%>" 
subject="question from tradeusedbooks.com user" sendAsHTML="false">
Dear <%=(String)request.getAttribute(mailInterface.USERATTR)%>,
This message was sent to you from tradeusedbooks.com because <<<%=asker.getUsername()%>>> wants to ask you a question regarding "<%=book.getTitle()%>" by <%=book.getAuthor()%> ($<%=book.getPrice()%>).
Here is what <<<%=asker.getUsername()%>>> (<%=asker.getEmail()%>) writes
>>>
<%=(String)request.getAttribute(mailInterface.MESSAGEATTR)%>
<<<
If you wish to reply to this question, please use the following email address:
<%=asker.getEmail()%>
If you find this message to be inappropriate or obscene, please report it to support@tradeusedbooks.com including the original message and headers. Please note: this message only represents a question about the item and not an official purchase of it. If you decide to sell your item by forgoing the actual selling process you will not have the privilege of using our services (such as leaving feedback). 
cheers,
<%@ include file="mailFooterText.jsp" %>
</my:sendmail>