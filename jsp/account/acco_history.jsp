<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="true" %>
<%@ page import="server.*,java.sql.*" %>
<%@ include file="/jsp/allTags.jsp" %>
<%
ResultSet booksHistory = (ResultSet)request.getAttribute(accoInterface.BOOKS_HISTORYTABLE_ATTR);
%>
Note: only the last 10 transactions are displayed - unless they have expired, in 
which case we no longer have any record of them (see our <a href="<%=URLInterface.URLTERMS%>">
terms of conditions</a> in regards to that).
<table cellSpacing=1 cellPadding=3 width="100%" border=0>
<tr height=25>
	<td style="font-weight: bold; font-size: 16px;" align=center colSpan=6>History</td>
</tr>
<tr style="font-size: 12px; COLOR: #ffffff; FONT-FAMILY: Verdana" bgColor=#6699cc height=25>
	<th>Book Info</th>
	<th nowrap>User Info</th>
	<th>Repost</th>
</tr>
<%
boolean rowOdd = false;
while (booksHistory.next()){
    rowOdd = !rowOdd;
    //old books table values
    String sellerID = booksHistory.getString(Tables.TABLEOLDBOOKS+"."+oldBooksTable.SELLERID);
	String buyerID = booksHistory.getString(Tables.TABLEOLDBOOKS+"."+oldBooksTable.BUYERID);
	String bookID = booksHistory.getString(Tables.TABLEOLDBOOKS+"."+oldBooksTable.BOOKID);
	String title = booksHistory.getString(Tables.TABLEOLDBOOKS+"."+oldBooksTable.TITLE);
	String author = booksHistory.getString(Tables.TABLEOLDBOOKS+"."+oldBooksTable.AUTHOR);
	String isbn = tools.formatISBN(booksHistory.getString(Tables.TABLEOLDBOOKS+"."+oldBooksTable.ISBN));
	String condition = booksTable.CONDITIONS[booksHistory.getInt(Tables.TABLEOLDBOOKS+"."+oldBooksTable.CONDITION)].toUpperCase();
	String price = booksHistory.getString(Tables.TABLEOLDBOOKS+"."+oldBooksTable.PRICE);
	String comment = booksHistory.getString(Tables.TABLEOLDBOOKS+"."+oldBooksTable.COMMENT);
	String date = booksHistory.getString(Tables.TABLEOLDBOOKS+"."+oldBooksTable.DATE);
    //college values
    String college = booksHistory.getString(Tables.TABLECOLLEGES+"."+collegeTable.FULLNAME);
    //user values
    String userID = booksHistory.getString(Tables.USERSTABLE+"."+usersTable.ID);
	String username = booksHistory.getString(Tables.USERSTABLE+"."+usersTable.USERNAME);
    if (rowOdd){
    	out.print("<tr class=rowOdd valign=top>");
    }else{
    	out.print("<tr class=rowEven valign=top>");
    }
%>
	<td class=tableContent style="word-wrap: break-word" width='450'>
		<b>title: </b><%=tools.htmlEncode(title)%><br>
		<b>author: </b><%=tools.htmlEncode(author)%><br>
		<%if (isbn!=null){%>
		<b>ISBN: </b><%=isbn%><br>
		<%}%>
		<b>price: </b>$<%=price%><br>
		<%if (comment!=null){%>
		<b>comments: </b><%=tools.htmlEncode(comment)%><br>
		<%}%>
		<b>condition: </b><%=condition%><br>
		<b>transaction date: </b><%=date%><br>
		<b>college: </b><%=college%><br>
		<%if (userID.equals(sellerID)){%>
			<b>status: </b>bought<br>
		<%}%>
		<%if (userID.equals(buyerID)){%>
			<b>status: </b>sold<br>
		<%}%>
	</td>
	<td class=tableContent align=center width="100">
		<a style="text-decoration:none;color:#333399" 
		href="<%=response.encodeURL(URLInterface.URLFEEDBACK+"?"+userID)%>"><%=username%></a></td>
	
	<td align=center>
		<%-- if this is seller show repost --%>
	<%if (userID.equals(buyerID)){%>
		<a href="<%=
		response.encodeURL(
		URLInterface.URLACCOUNTHISTORY+"/repost?"
		+accoInterface.BOOKS_BOOKID+"="+bookID)%>"><img 
		border=0 alt="repost this book for sale" src="/myImages/post.gif"></a>
	<%}%>
	</td>
</tr>
<%}%>
</table>