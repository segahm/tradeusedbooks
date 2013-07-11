<%--requirements: mailInterface.[USERATTR,EMAILATTR] --%>
<%@ page import="server.*" %>
<%@ include file="/jsp/allTags.jsp" %>
<my:sendmail emailAddress="<%=(String)request.getAttribute(mailInterface.EMAILATTR)%>" 
subject="a welcome message from tradeusedbooks.com">
<html><body>
<p>Dear <%=(String)request.getAttribute(mailInterface.USERATTR)%>,</p>
<p>Thank you for registering with tradeusedbooks.com. Let us assure you that your privacy 
is very important to us, which is why your personal information will never be 
shared with others without your personal consent. And in such cases that we do 
need to share your personal information (such as an email address), 
we will only share it with other tradeusedbooks.com users to insure the 
effectiveness of our services. For more information please refer to our 
<a href='http://www.tradeusedbooks.com<%=URLInterface.URLPRIVACY%>'>privacy 
policy</a>.</p>
<p>Welcome to our community! <br>
Sincerely,</p>
<%@ include file="mailFooter.jsp" %>
</my:sendmail>