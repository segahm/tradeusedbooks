<%@ page contentType="text/html; charset=utf-8"%>
<%@ page session="true"%>
<%@ page import="server.*,java.sql.*"%>
<%@ include file="/jsp/allTags.jsp"%>
<%
ResultSet booksBought = (ResultSet)request.getAttribute(accoInterface.BOOKS_BOUGHTTABLE_ATTR);
%>
<%-- printing table header --%>
<table cellSpacing=1 cellPadding=3 width="100%" border=0>
<tr height=25>
	<td style="font-weight: bold; font-size: 16px;" align=center colSpan=6>Books Bought</td>
</tr>
<tr style="font-size: 12px; COLOR: #ffffff; FONT-FAMILY: Verdana" bgColor=#6699cc height=25>
	<th>Book Title</th>
	<th noWrap>Price</th>
	<th noWrap>Seller</th>
	<th nowrap>Leave Feedback</th>
</tr>
<%
boolean rowOdd = false;
while (booksBought.next()){
    rowOdd = !rowOdd;
    String bookID = booksBought.getString(Tables.TABLEOLDBOOKS+"."+oldBooksTable.BOOKID);
    String title = booksBought.getString(Tables.TABLEOLDBOOKS+"."+oldBooksTable.TITLE);
    String price = booksBought.getString(Tables.TABLEOLDBOOKS+"."+oldBooksTable.PRICE);
    String sellerID = booksBought.getString(Tables.USERSTABLE+"."+usersTable.ID);
    String sellerUsername = booksBought.getString(Tables.USERSTABLE+"."+usersTable.USERNAME);
    if (rowOdd){
    	out.print("<tr class=rowOdd valign=top>");
    }else{
    	out.print("<tr class=rowEven valign=top>");
    }
%>
	<td class=tableContent style="word-wrap: break-word" width='250'>
		<%=tools.htmlEncode(title)%>
	</td>
	<td class=tableContent align=center>$<%=price%></td>
	<td class=tableContent align=center>
		<a title="click here to look at the user's feedback" 
		style="text-decoration:none;color:#333399;" 
		 href="<%=response.encodeURL(URLInterface.URLFEEDBACK+"?"+sellerID)%>"><%=sellerUsername%></a>
	</td>
	<td align=center>
		<input type=button class=accountButton1 
		onclick="javascript:document.location = '<%=
		response.encodeURL(
		URLInterface.URLACCOUNTFEEDBACK+"?"+
		accoInterface.BOOKS_BOOKID+"="+bookID+"&"+
		accoInterface.BOOKS_BIDS_USERID+"="+sellerID)%>'" value="leave feedback"></a>
	</td>
</tr>
<%}%>
</table>