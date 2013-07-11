<table width="100%">
<tr><td><table width="100%" height="100%">
<tr><td colspan=2>People are looking for...</td></tr>
<tr valign=top>
<!-- search results begin -->
<td valign=top width="80%">
	<table cellSpacing='0' cellPadding='5' width='100%' border='0'>
<%-- new result row --%>
<%
boolean firstRow = true;
Object[] row = search.next();
boolean rowOdd = false;
while (row!=null){
	rowOdd = !rowOdd;
	if (firstRow){%>
		<tr style='font-family: Arial'>
			<th align=middle width='100'>ISBN</th>
			<th align=left width='50%'>Title</th>
			<th align=left width='168'>Author</th>
			<th align=left width='168'>College</th></tr>
	<%}
	firstRow = false;
	wantedTable table = (wantedTable)row[0];
	String result_isbn = tools.formatISBN(table.getISBN());
	String result_title = table.getTitle();
	String result_author = tools.lengthFormat(table.getAuthor(),30);
	String result_college = (String)row[1];
%>
<tr 
<%if (rowOdd){%>
	class='rowOdd'
<%}else{%>
	class='rowEven'
<%}%> 
valign=top align=left>
<td noWrap align=middle 
style="font-size:12px;font-family: Times News Roman;">
<%=result_isbn%>
</td>
<td align=left width="200" 
style="word-wrap: break-word;font-size:12px;font-family: Times News Roman;">
<%=tools.htmlEncode(result_title)%>
</td>
<td noWrap 
style="font-size:12px;font-family: Times News Roman;">by 
<%=tools.htmlEncode(result_author)%>
</td>
<td noWrap 
style="font-size:12px;font-family: Times News Roman;"> 
<%=result_college%>
</td>
</tr>
<%
row = search.next();
}
%>
	</table>
</td>
	<!-- search results end -->
<td width="200" nowrap><%@ include file="/jsp/ads.jsp" %></td>
</tr></table>
</td></tr>
</table>