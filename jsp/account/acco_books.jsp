<%@ page contentType="text/html; charset=utf-8"%>
<%@ page session="true"%>
<%@ page import="server.*,java.sql.*"%>
<%@ include file="/jsp/allTags.jsp"%>
<%
ResultSet booksSelling = (ResultSet)request.getAttribute(accoInterface.BOOKS_SELLINGTABLE_ATTR);
ResultSet booksSold = (ResultSet)request.getAttribute(accoInterface.BOOKS_SOLDTABLE_ATTR);
%>
Note: your opportunity to leave feedback for a seller/buyer will usually expire in 30 days after the day of the transaction.
<!-- BOOKS SELLING -->
<table cellSpacing=1 cellPadding=3 width="100%" border=0>
<tr height=25>
	<td style="font-weight: bold; font-size: 16px;" align=center colSpan=6>Books Currently Selling</td>
</tr>
<tr style="font-size: 12px; COLOR: #ffffff; FONT-FAMILY: Verdana" bgColor=#6699cc height=25>
	<th>Book Title</th>
	<th noWrap>Started</th>
	<th noWrap>Ends</th>
	<th noWrap>Item Price </th>
	<th>Edit</th>
	<th>Remove</th>
</tr>
<%
boolean rowOdd = false;
while (booksSelling.next()){
    rowOdd = !rowOdd;
    booksTable book = new booksTable();
    book.setID(booksSelling.getString(booksTable.ID));
    book.setTitle(booksSelling.getString(booksTable.TITLE));
    book.setPrice(booksSelling.getString(booksTable.PRICE));
   	book.setDate(booksSelling.getString(booksTable.DATE));
    if (rowOdd){
    	out.print("<tr class=rowOdd valign=top>");
    }else{
    	out.print("<tr class=rowEven valign=top>");
    }
%>
<td width='300' style="word-wrap: break-word">
	<a class=tableLinks target=_blank href="<%=
	response.encodeURL(URLInterface.URLBOOK+book.getID())%>" 
	title="opens in new window">
	<%=tools.htmlEncode(book.getTitle())%>
	</a>
</td>
<td class=tableContent align=center>
	<%=book.getDate()%>
</td>
<td class=tableContent align=center>
	<%=new DateObject().getEndDate(book.getDate(),booksTable.EXPIRATION)%>
</td>
<td class=tableContent align=center>
	$<%=book.getPrice()%>
</td>
<td align=center>
	<a href="<%=response.encodeURL(URLInterface.URLACCOUNTBOOKS+"/edit?"+accoInterface.BOOKS_BOOKID+"="+book.getID())%>">
		<img border=0 src="/myImages/edit.gif" alt="edit this book">
	</a>
</td>
<td align=center>
	<a href="javascript:window.confirm('Are you sure you want to remove this posting?')?location = '<%=response.encodeURL(URLInterface.URLACCOUNTBOOKS+"/remove?"+accoInterface.BOOKS_BOOKID+"="+book.getID())%>':location = '<%=response.encodeURL(URLInterface.URLACCOUNTBOOKS)%>'">
		<img border=0 src="/myImages/delete.gif" alt="remove this book">
	</a>
</td>
</tr>
<%}%>
</table>
<!-- BOOKS SOLD -->
<table cellSpacing=1 cellPadding=3 width="100%" border=0>
<tr height=25>
<td style="font-weight: bold; font-size: 16px;" align=center colSpan=6>
	Books Sold
</td>
</tr>
<tr style="font-size: 12px; color: #ffffff; font-family: Verdana" bgColor=#6699cc height=25>
<th>Book Title</th>
<th noWrap>Buyer</th>
<th noWrap>Contact Information</th>
<th nowrap>Leave Feedback</th>
</tr>
<%
rowOdd = false;
while (booksSold.next()){
	rowOdd = !rowOdd;
	String sold_title = booksSold.getString(Tables.TABLEOLDBOOKS+"."+oldBooksTable.TITLE);
    String sold_bookid = booksSold.getString(Tables.TABLEBUYERS+"."+buyersTable.BOOKID);
    String sold_buyer_id = booksSold.getString(Tables.TABLEBUYERS+"."+buyersTable.BUYERID);
    String sold_buyer_username = booksSold.getString(Tables.USERSTABLE+"."+usersTable.USERNAME);
    String sold_buyer_email = booksSold.getString(Tables.USERSTABLE+"."+usersTable.EMAIL);
	//printing row
	if (rowOdd){
    	out.print("<tr class=rowOdd valign=top>");
    }else{
    	out.print("<tr class=rowEven valign=top>");
    }
%>
<td class=tableContent style="word-wrap: break-word" width="200">
<%=tools.htmlEncode(sold_title)%>
</td>
<td class=tableContent align=center>
	<a title="click here to look at the user's feedback" 
		style="text-decoration:none;color:#333399" 
		href="<%=response.encodeURL(URLInterface.URLFEEDBACK+"?"+sold_buyer_id)%>">
	<%=sold_buyer_username%></a>
</td>
<td class=tableContent align=center>
	<a href="mailto:<%=sold_buyer_email%>">
		<%=sold_buyer_email%>
	</a>
</td>
<td align=center>
	<input type=button class=accountButton1 
		onclick="javascript:document.location = '<%=
		response.encodeURL(
		URLInterface.URLACCOUNTFEEDBACK+"?"
		+accoInterface.BOOKS_BOOKID+"="
		+sold_bookid+"&"+accoInterface.BOOKS_BIDS_USERID
		+"="+sold_buyer_id)%>'" 
		value="leave feedback">
</td></tr>
<%}%>
</table>