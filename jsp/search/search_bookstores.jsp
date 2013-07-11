<table width="100%">
<tr><td><table width="100%" height="100%"><tr valign=top>
<!-- search results begin -->
<td valign=top width="100%">
	<table cellSpacing='0' cellPadding='5' width='100%' border='0'>
<%-- new result row --%>
<%
boolean firstRow = true;
Object[] row = search.next();
boolean rowOdd = false;
while (row!=null){
	rowOdd = !rowOdd;
	if (!firstRow){%>
		<tr><td colspan="2"><hr></td></tr>
	<%}
	firstRow = false;
	bookstoreBooksTable table = (bookstoreBooksTable)row[0];
	String storename = (String)row[1];
	isbnTable table2 = (isbnTable)row[2];
	String result_imageURL = table2.getSimage();
	String result_imageTitle = table2.getTitle();
	if (result_imageTitle == null){
		result_imageTitle = "click to view full description";
	}
	String result_bookid = table.getID();
	String result_storeid = table.getStoreID();
	String result_isbn = tools.formatISBN(table.getISBN());
	String result_title = table.getTitle();
	String result_author = tools.lengthFormat(table.getAuthor(),30);
	String result_usedPrice = table.getUsedPrice();
	String result_newPrice = table.getNewPrice();
	String result_copies = table.getCopies();
%>
	<tr valign=top>
	<td width="125" 
	style="font-size:14px;font-family:Arial" align=center 
	nowrap>
		<%if (result_isbn != null){%>
			<%=result_isbn%><br>
		<%}%>
			<a href="<%=URLInterface.URL_BOOKSTORE_BOOK+result_bookid%>">
		<%if (result_imageURL != null){%>
			<img alt="<%=result_imageTitle%>" 
			border=1 style="border-color:#000000" src="<%=result_imageURL%>">
		<%}%>
		<%if (result_imageURL == null){%>
			<img alt="<%=result_imageTitle%>" 
			border=0 src="/myImages/noimage.gif" width="90" height="90">
		<%}%>
			</a>
	</td>
	<td width="400" style="word-wrap:break-word;" 
	align=left>
		<a class="titleLinks" 
		href="<%=URLInterface.URL_BOOKSTORE_BOOK+result_bookid%>"><%=tools.htmlEncode(result_title)%></a>
			<br><font face="Courier">Author:
			</font> <%=tools.htmlEncode(result_author)%>
			<%if (result_usedPrice != null){%>
			<br><font face="Courier">Used Price:
			</font> &#36;<%=result_usedPrice%>
			<%}%>
			<%if (result_newPrice != null){%>
			<br><font face="Courier">New Price:
			</font> &#36;<%=result_newPrice%>
			<%}%>
			<br><font face="Courier"># of Copies:
			</font> <%=result_copies%>
			<br><font face="Courier">Bookstore:
			</font> <%=tools.htmlEncode(storename)%>
	</td>
</tr>
	<%row = search.next();%>
<%}%>
	</table>
</td>
	<!-- search results end -->
<td width="200" nowrap>
	<%@ include file="/jsp/ads.jsp" %>
</td>
</tr></table></td></tr>
</table>