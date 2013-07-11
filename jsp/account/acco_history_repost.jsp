<%@ page contentType="text/html; charset=utf-8"%>
<%@ page session="true"%>
<%@ page import="server.*"%>
<%@ include file="/jsp/allTags.jsp"%>
<%
String ufi = (String)request.getAttribute(postBInterface.UNIQUEFORMATTR);
%>
<p><font color="#FF0000">Please Note:</font> You are about to repost one of 
the books you previously sold. Some of the old information might no longer apply 
to this posting and hence you will have a chance to edit or enter that 
information.</p>
<form action="<%=URLInterface.URLPBOOKS%>" method=post>
	<input type=hidden name="<%=postBInterface.PAGESTEP%>" value="2">
	<input type=hidden name="<%=postBInterface.UNIQUEFORMATTR%>" value="<%=ufi%>">
	<p align=center><input type=submit value="proceed" style="color: #333399"></p>
</form>
