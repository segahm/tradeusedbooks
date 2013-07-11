<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="true" %>
<%@ page import="server.*,java.sql.*" %>
<%@ include file="/jsp/allTags.jsp" %>
<%
ResultSet books = (ResultSet)request.getAttribute(bookstoreAccount.BOOKSATTR);
%>
<%=(String)request.getAttribute(bookstoreAccount.PAGES)%>
<table cellSpacing=1 cellPadding=3 width="100%" border=0>
<tr height=25>
	<td style="font-weight: bold; font-size: 16px;" align=center colSpan=6>Books</td>
</tr>
<tr style="font-size: 12px; COLOR: #ffffff; FONT-FAMILY: Verdana" bgColor=#6699cc height=25>
	<th>Book Info</th>
	<th>Edit</th>
	<th>Remove</th>
</tr>
<%
boolean rowOdd = false;
while (books.next()){
	String bookID = books.getString(bookstoreBooksTable.BOOKID);
   	String title = books.getString(bookstoreBooksTable.TITLE);
	String author = books.getString(bookstoreBooksTable.AUTHOR);
	String isbn = books.getString(bookstoreBooksTable.ISBN);
	String comment = books.getString(bookstoreBooksTable.COMMENT);
	String usedPrice = books.getString(bookstoreBooksTable.PRICEUSED);
	String newPrice = books.getString(bookstoreBooksTable.PRICENEW);
	String copies = books.getString(bookstoreBooksTable.COPIES);
	String date = books.getString(bookstoreBooksTable.DATE);
	String condition = books.getString(bookstoreBooksTable.CONDITIONS);
	condition = condition.replaceAll("/",", ");
	condition = condition.replaceAll("["+bookstoreBooksTable.CONDITION_ACCEPTABLE+"]",bookstoreBooksTable.CONDITION_NAMES[bookstoreBooksTable.CONDITION_ACCEPTABLE]);
    condition = condition.replaceAll("["+bookstoreBooksTable.CONDITION_GOOD+"]",bookstoreBooksTable.CONDITION_NAMES[bookstoreBooksTable.CONDITION_GOOD]);
	condition = condition.replaceAll("["+bookstoreBooksTable.CONDITION_LIKENEW+"]",bookstoreBooksTable.CONDITION_NAMES[bookstoreBooksTable.CONDITION_LIKENEW]);
	condition = condition.replaceAll("["+bookstoreBooksTable.CONDITION_NEW+"]",bookstoreBooksTable.CONDITION_NAMES[bookstoreBooksTable.CONDITION_NEW]);
	condition = condition.toUpperCase();
    rowOdd = !rowOdd;
    //books table values
    if (rowOdd){
    	out.print("<tr class=rowOdd valign=top>");
    }else{
    	out.print("<tr class=rowEven valign=top>");
    }
%>
	<td class=tableContent style="word-wrap: break-word" width='450'>
		<b>title: </b><a class=tableLinks target=_blank 
		href="<%=URLInterface.URL_BOOKSTORE_BOOK+bookID%>"><%=tools.htmlEncode(title)%></a><br>
		<b>author: </b><%=tools.htmlEncode(author)%><br>
		<%if (isbn!=null){%>
		<b>ISBN: </b><%=tools.formatISBN(isbn)%><br>
		<%}%>
		<%if (usedPrice != null){%>
		<b>used price: </b>$<%=usedPrice%><br>
		<%}%>
		<%if (newPrice != null){%>
		<b>new price: </b>$<%=newPrice%><br>
		<%}%>
		<%if (comment!=null){%>
		<b>comments: </b><%=tools.htmlEncode(comment)%><br>
		<%}%>
		<b>conditions available: </b><%=condition%><br>
		<b>copies available: </b><%=copies%><br>
		<b>date posted: </b><%=date%><br>
		<b>date expires: </b><%=new DateObject().getEndDate(date,bookstoreBooksTable.EXPIRATION)%><br>
	</td>
	<td align=center width="100">
	<a href="<%=response.encodeURL(URLInterface.URL_BOOKSTORE_ACCOUNT_BOOKS+"/edit?"+bookID)%>">
		<img border=0 src="/myImages/edit.gif" alt="edit this book">
	</a>
	</td>
	<td align=center width="100">
	<a href="<%=response.encodeURL(URLInterface.URL_BOOKSTORE_ACCOUNT_BOOKS+"/remove?"+bookID)%>">
		<img border=0 src="/myImages/delete.gif" alt="remove this book">
	</a>	
	</td>
</tr>
<%}%>
</table>
<%=(String)request.getAttribute(bookstoreAccount.PAGES)%>