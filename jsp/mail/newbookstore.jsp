<%@ page import="server.*" %>
<%@ include file="/jsp/allTags.jsp" %>
<%--requirements: mailInterface.[USERATTR,PASSATTR,EMAILATTR] --%>
<my:sendmail emailAddress="<%=(String)request.getAttribute(mailInterface.EMAILATTR)%>" 
subject="newly registered bookstore">
<html><body>
<p>Dear <%=(String)request.getAttribute(mailInterface.USERATTR)%>,</p>
<p>Your bookstore registration has finally been confirmed and you now have full 
access to your account. As a newly registered bookstore manager you receive a temporary 
password - this is so that we can verify that your email is indeed valid and can 
be used for further communication with you.</p>
<p>Your <b>password</b> is <%=(String)request.getAttribute(mailInterface.PASSATTR)%></p>
<p>If you would like to change this password it can be done by going to your 
account update page. There you will also find ways to post and edit your book 
inventory.</p>
<p style="margin-top: 0; margin-bottom: 0">welcome to tradeusedbooks.com 
community,<br></p>
<%@ include file="mailFooter.jsp" %>
</my:sendmail>