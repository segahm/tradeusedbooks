<%@ page contentType="text/html; charset=utf-8"%>
<%@ page session="true"%>
<%@ page import="server.*"%>
<%@ include file="/jsp/allTags.jsp"%>
<%
String ufi = (String)request.getAttribute(postBInterface.UNIQUEFORMATTR);
booksTable book = (booksTable)request.getAttribute(accoInterface.BOOKS_BOOKID);
%>
<p align=left>
	<font color="#FF0000">Please Note: </font>
	you are about to edit &quot;<a target=_blank 
	href="<%=URLInterface.URLBOOK+book.getID()%>">
		<%=book.getTitle()%>
	</a>&quot;!&nbsp;
	Once you proceed with this action your <b>old posting</b> will be <b>deleted!</b></p>
	<form action="<%=URLInterface.URLACCOUNTREPOST+"?"
	+accoInterface.BOOKS_BOOKID+"="+book.getID()%>" method=post>
	<input type=hidden name="<%=postBInterface.PAGESTEP%>" value="2">
	<input type=hidden name="<%=postBInterface.UNIQUEFORMATTR%>" value="<%=ufi%>">
	<p align=center><input type=submit value="edit" style="color: #333399"></p>
</form>
