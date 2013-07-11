<%@ page import="server.*" %>
<%@ include file="/jsp/allTags.jsp" %>
<%--requirements: mailInterface.[USERATTR,EMAILATTR] --%>
<%
//all attributes should be passed as nonempty,nonnull strings
//no formating will be done here
String title = (String)request.getAttribute(
	postBInterface.TITLEFIELD);
String author = (String)request.getAttribute(
	postBInterface.AUTHORFIELD);
String isbn = (String)request.getAttribute(
	postBInterface.ISBNFIELD);
String condition = (String)request.getAttribute(
	postBInterface.CONDITIONFIELD);
String price = (String)request.getAttribute(
	postBInterface.PRICEFIELD);
String comments = (String)request.getAttribute(
	postBInterface.COMMENTSFIELD);
String college = (String)request.getAttribute(
	postBInterface.COLLEGEIDFIELD);
String department = (String)request.getAttribute(
	postBInterface.DEPARTMENTIDFIELD);
String course = (String)request.getAttribute(
	postBInterface.COURSEIDFIELD);
String teacher = (String)request.getAttribute(
	postBInterface.TEACHERIDFIELD);
%>
<my:sendmail emailAddress="<%=(String)request.getAttribute(mailInterface.EMAILATTR)%>" 
subject="posting confirmation from tradeusedbooks.com">
<html><body>
<p>Dear <%=(String)request.getAttribute(mailInterface.USERATTR)%>,</p>
<p>This email was sent to you as a confirmation of your posting.</p>
<p style='margin-top: 0; margin-bottom: 0'>Here is the book information that you 
posted:</p>
<p style='margin-top: 0; margin-bottom: 0'>Title: <b><%=tools.htmlEncode(title)%></b></p>
<p style='margin-top: 0; margin-bottom: 0'>Author: <b><%=tools.htmlEncode(author)%></b></p>
<p style='margin-top: 0; margin-bottom: 0'>ISBN: <b><%=isbn%></b></p>
<p style='margin-top: 0; margin-bottom: 0'>Price: <b>$<%=price%></b></p>
<p style='margin-top: 0; margin-bottom: 0'>Condition: <b><%=condition%></b></p>
<p style='margin-top: 0; margin-bottom: 0'>College: <b><%=college%></b></p>
<p style='margin-top: 0; margin-bottom: 0'>Department: <b><%=department%></b></p>
<p style='margin-top: 0; margin-bottom: 0'>Course: <b><%=course%></b></p>
<p style='margin-top: 0; margin-bottom: 0'>Teacher: <b><%=teacher%></b></p>
<p style='margin-top: 0;'>Comments: <b><%=tools.htmlEncode(comments)%></b></p>
<p>Remember that you can edit this information by going to &quot;My Account&quot; page on 
our website. For more information please refer to our help page.</p>
<p style='margin-top: 0; margin-bottom: 0'>Once again thanks for using 
tradeusedbooks.com,</p>
<%@ include file="mailFooter.jsp" %>
</my:sendmail>