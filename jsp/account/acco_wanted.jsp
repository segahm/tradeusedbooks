<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="true" %>
<%@ page import="server.*,java.sql.*" %>
<%@ include file="/jsp/allTags.jsp" %>
<%
ResultSet wanted = (ResultSet)request.getAttribute(accoInterface.BOOKS_WANTEDTABLE_ATTR);
%>
<table cellSpacing=1 cellPadding=3 width="100%" border=0>
<tr height=25>
	<td style="font-weight: bold; font-size: 16px;" align=center colSpan=6>Books I'm Looking for...</td>
</tr>
<tr style="font-size: 12px; COLOR: #ffffff; FONT-FAMILY: Verdana" bgColor=#6699cc height=25>
	<th>Request Info</th>
	<th>Remove</th>
</tr>
<%
boolean rowOdd = false;
while (wanted.next()){
    rowOdd = !rowOdd;
    //old books table values
    String isbn = wanted.getString(Tables.TABLEWANTED+"."+wantedTable.ISBN);
    String title = wanted.getString(Tables.TABLEWANTED+"."+wantedTable.TITLE);
    String author = wanted.getString(Tables.TABLEWANTED+"."+wantedTable.AUTHOR);
    String id = wanted.getString(Tables.TABLEWANTED+"."+wantedTable.ID);
    String exp = wanted.getString(Tables.TABLEWANTED+"."+wantedTable.EXPIRATION);
    String college = wanted.getString(Tables.TABLECOLLEGES+"."+collegeTable.FULLNAME);
    if (rowOdd){
    	out.print("<tr class=rowOdd valign=top>");
    }else{
    	out.print("<tr class=rowEven valign=top>");
    }
%>
	<td class=tableContent style="word-wrap: break-word" width='450'>
		<b>title: </b><%=tools.htmlEncode(title)%><br>
		<b>author: </b><%=tools.htmlEncode(author)%><br>
		<b>ISBN: </b><%=tools.formatISBN(isbn)%><br>
		<b>request expires on: </b><%=exp%><br>
		<b>college: </b><%=college%><br>
	</td>	
	<td align=center>
		<%-- if this is seller show repost --%>
		<a href="<%=
		response.encodeURL(
		URLInterface.URLACCOUNTWANTED+"/remove?"+id)%>"><img 
		border=0 alt="remove this request" src="/myImages/delete.gif"></a>
	</td>
</tr>
<%}%>
</table>