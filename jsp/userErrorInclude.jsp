<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="true" %>
<%@ page import="server.*" %>
<%@ include file="/jsp/allTags.jsp" %>
<%
final String HEADERTITLE = "Error";
String errorMessage = (String)request.getAttribute(
	StringInterface.ERRORPAGEATTR);
%>
<p>We are sorry but the following error occurred:<br>
<font color=red><%=errorMessage%></font></p>
If you have any questions or simply need help, check our
<a href="<%=URLInterface.URLHELPPAGE%>">help page</a> or email us at <a href="mailto:support@tradeusedbooks.com">
support@tradeusedbooks.com</a>
