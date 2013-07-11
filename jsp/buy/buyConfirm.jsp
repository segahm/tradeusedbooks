<%@ page session="true" %>
<%@ page import="server.*" %>
<%@ include file="/jsp/allTags.jsp" %>
<%
final String HEADERTITLE = "Confirm Purchase";
booksTable book = (booksTable)request.getAttribute(BuyServlet.ATTR_BOOK);
usersTable seller = (usersTable)request.getAttribute(BuyServlet.ATTR_SELLER);
usersTable buyer = (usersTable)request.getAttribute(BuyServlet.ATTR_BUYER);
String college = (String)request.getAttribute(BuyServlet.ATTR_COLLEGE);
%>
<html>
<head>
<%@ include file="/html/meta.html"%>
<meta name="Robots" content="noindex">
<link rel="stylesheet" type="text/css" href="/styles/main.css">
<title>TradeUsedBooks.com Confirm Purchase</title>
<script language=javascript>
function limitText(field,limit){
	if (field.value.length>limit){
		field.value = field.value.substr(0,limit);
	}
}
</script>
</head>
<body bgColor=#ffffff>
<table width=100%>
<%-- header begins --%>
<%@ include file="/jsp/generalHeader.jsp"%>
<%-- header ends --%>
<!--main content begins -->
<tr><td>
<p>Thank you for your interest in this book. Please fill out the form below 
specifying any important information for the seller of this title. Please note 
that your message will be send as plain text only and no HTML will be processed.</p>
<p style="margin-left:10px"><font color=#FF0000>Please do not use this form for 
any purpose other than confirming your intention to buy this book within the area of the college specified. If you do not intend to purchase this title please 
do not use this form. We also ask you to refrain from using any obscenity as it is inappropriate and will result in further actions on our part. 
Any repeated violations of these rules will result in 
account suspension. Note that we take a record of your IP address and user id on 
this page.</font></p>
<form action="<%=URLInterface.URLBUY%>" method=post>
<input type=hidden name="<%=bookInterface.FIELD_ID%>" value="<%=book.getID()%>">
<input type=hidden name="<%=bookInterface.FIELD_SELLERID%>" value="<%=book.getSellerID()%>">
<%--simply security feature--%>
<input type=hidden name="<%=bookInterface.FIELD_HIDDENID%>" value="<%=Math.abs(book.getID().hashCode())%>">
<table>
<tr valign=top><td width=100><b>Seller:</b></td><td><%=seller.getUsername()%></td></tr>
<tr valign=top><td width=100><b>Buyer:</b></td><td><%=buyer.getUsername()%> (<%=buyer.getEmail()%>)</td></tr>
<tr valign=top><td width=100><b>College:</b></td><td><%=college%></td></tr>
<tr valign=top><td width=100><b>Title:</b></td><td>"<%=tools.htmlEncode(book.getTitle())%>" by <%=tools.htmlEncode(book.getAuthor())%> ($<%=book.getPrice()%>)</td></tr>
<tr valign=top><td width=100><b>Message:</b></td><td>
<textarea name=<%=BuyServlet.FIELD_MESSAGE%> rows=5 cols=50 onkeyup="limitText(this,1000)">
I need the book by:
We can meet at:
</textarea><br><font color=#FF0000 size=-1>note: your contact information will be automatically included</font></td></tr>
</table>
<center><input type=submit value="confirm purchase"></center>
</form>
</td></tr>
<!-- main content ends -->
<%-- footer begins --%>
<%@ include file="/html/footer.html"%>
<%-- footer ends --%>
</table>
</body>
</html>