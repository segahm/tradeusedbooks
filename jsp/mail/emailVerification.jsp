<%@ page import="server.*" %>
<%@ include file="/jsp/allTags.jsp" %>
<%--requirements: mailInterface.[USERATTR,PASSATTR,EMAILATTR] --%>
<my:sendmail emailAddress="<%=(String)request.getAttribute(mailInterface.EMAILATTR)%>" 
subject="email confirmation required for a new user">
<html><body>
<p>Dear <%=(String)request.getAttribute(mailInterface.USERATTR)%>,</p>
<p>IMPORTANT: email confirmation is required (see below)</p>
<p>
What follows is your temporary password. This password can be 
changed once you complete the registration process.</p>
<p>
Password: <%=(String)request.getAttribute(mailInterface.PASSATTR)%></p>
<p>
Please follow the link below to confirm your email address. 
Your registration will not be complete until you follow this link.</p>
<p>
<a href='http://tradeusedbooks.com<%=URLInterface.URLCONFIRMUSER%>'>http://tradeusedbooks.com/confirm/user</a></p>
<%@ include file="mailFooter.jsp" %>
</my:sendmail>