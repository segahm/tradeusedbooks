<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="false" %>
<%@ page import="server.*" %>
<%@ include file="/jsp/allTags.jsp" %>
<%
final String HEADERTITLE = "Book";
bookstoreBooksTable book = (bookstoreBooksTable)request.getAttribute(bookstoreBook.BOOKATTR);
isbnTable isbnInfo = (isbnTable)request.getAttribute(bookstoreBook.ISBNATTR);
String[] store = (String[])request.getAttribute(bookstoreBook.STOREATTR);
String conditions = "";
for (int i=0;i<(book.getCondition().length-1);i++){
	conditions += bookstoreBooksTable.CONDITION_NAMES[Integer.parseInt(book.getCondition()[i])]+", ";
}
conditions += bookstoreBooksTable.CONDITION_NAMES[Integer.parseInt(book.getCondition()[book.getCondition().length-1])];
%>
<html>
<head>
<%@ include file="/html/meta.html" %>
<meta name="description" content="find <%=tools.htmlEncode(book.getTitle())%> and other college textbooks on tradeusedbooks.com">
<meta name="Robots" content="all">
<meta name="keywords" content="<%=tools.htmlEncode(book.getTitle())%>">
<link rel="stylesheet" type="text/css" href="/styles/main.css">
<title>Trade Used Books.com Book: <%=tools.htmlEncode(book.getTitle())%></title>
</head>
<body bgColor=#ffffff>
<table width="100%">
<%-- header begins --%>
<%@ include file="/jsp/generalSearchHeader.jsp" %>
<%-- header ends --%>
<%-- main content begins --%>
<tr>
<td>
<table>
	<tr valign=top>
		<%-- book info begins --%>
		<td width="100%" style="padding-left:10px;word-wrap:break-word">
		<table cellpadding="5">
		<tr><td colspan=2>
		<p><b><font face="Verdana"><%=tools.htmlEncode(book.getTitle())%></font></b><br>
		by <%=tools.htmlEncode(book.getAuthor())%><br>
		<b>date posted:</b> <%=book.getDate()%></p>
		</td></tr>
		<tr><td><% if (isbnInfo==null || isbnInfo.getMimage()==null){%>
				<img alt="picture not available" src="/myImages/book.gif" border=1>
			<%}else{%>
				<img alt="<%=isbnInfo.getTitle()%>" src="<%=isbnInfo.getMimage()%>" border=1>
			<%}%></td>
		<td valign=top align=left>
			<b><font face="Times New Roman">Bookstore: </font></b>
				<a title="click here to see other books from this store"
					href="<%=URLInterface.URLSEARCH
					+"?"+SearchInterface.QUERY+"="+java.net.URLEncoder.encode("</storeid:"+book.getStoreID()+"/>","UTF-8")
					+"&"+SearchInterface.HEADER+"="+SearchInterface.SITE[2][0]%>" 
					style="color:#000080"><%=tools.htmlEncode(store[bookstoreInfoTable.STORENAME_FIELD])%></a><br>
				<%if (store[bookstoreInfoTable.WEBSITE_FIELD]!=null){%>
				<b><font face="Times New Roman">Website: </font></b>
				<a title="store's website" target=_blank 
				href="http://<%=java.net.URLEncoder.encode(store[bookstoreInfoTable.WEBSITE_FIELD],"UTF-8")%>" 
				style="color:#000080"><%=tools.htmlEncode(store[bookstoreInfoTable.WEBSITE_FIELD])%></a><br>
				<%}%>
			<%if (isbnInfo != null){%>
				<b><font color="#333399">Book details </b>(information obtained based on isbn):</font><br>
				<%if (isbnInfo.getPublisher() != null){%>
					<b>Publisher: </b><%=isbnInfo.getPublisher()%><br>
				<%}%>
				<%if (isbnInfo.getBinding() != null){%>
					<b>Binding: </b><%=isbnInfo.getBinding()%><br>
				<%}%>
				<%if (isbnInfo.getListPrice() != null){%>
					<b>Retail Price: </b><%=isbnInfo.getListPrice()%><br>
				<%}%>
				<%if (isbnInfo.getPages() != null){%>
					<b># of Pages: </b><%=isbnInfo.getPages()%><br>
				<%}%>
			<%}%>
		</td>
		</tr>
		<tr><td align=left>
			<%if (book.getUsedPrice()!=null){%>
			<b>Used Price: <font color="#FF3300">$<%=book.getUsedPrice()%></font></b>
			<%}%>
		</td>
		<td align=left>
			<%if (book.getNewPrice()!=null){%>
			<b>New Price: <font color="#FF3300">$<%=book.getNewPrice()%></font></b>
			<%}%>
		</td>
		</tr>
		<%if (book.getISBN()!=null){%>
		<tr><td align=left colspan=2><b>ISBN: </b><%=tools.formatISBN(book.getISBN())%></td></tr>
		<%}%>
		<tr><td colspan=2><font color="#333399"><b>Information supplied by store manager:</b></td></tr>
		<tr><td colspan=2 align=left>
		<b>Conditions available:</b> <%=conditions%><br>
		</td></tr>
		<tr><td colspan=2 align=left>
		<b># of Copies available:</b> <%=book.getCopies()%><br>
		</td></tr>
		<tr><td colspan=2><font color="#333399"><b>Store Information:</b></td></tr>
		<tr valign=top><td><b>Address:</b></td><td><textarea cols=40 rows=5 readonly><%=tools.htmlEncode(store[bookstoreInfoTable.ADDRESS_FIELD])%></textarea></td></tr>
		<tr valign=top><td><b>Contact:</b></td><td><textarea cols=40 rows=5 readonly><%=tools.htmlEncode(store[bookstoreInfoTable.CONTACTINFO_FIELD])%></textarea></td></tr>
		<%if (store[bookstoreInfoTable.MOREINFO_FIELD]!=null){%>
		<tr valign=top><td nowrap><b>Additional Information:</b></td><td><textarea cols=40 rows=5 readonly><%=tools.htmlEncode(store[bookstoreInfoTable.MOREINFO_FIELD])%></textarea></td></tr>
		<%}%>
		</table>
		</td>
		<%-- book info ends --%>
		<!-- adds begin -->
		<td width="200" nowrap>
		<%@ include file="/jsp/ads.jsp" %>
		</td>
		<!-- adds end -->
	</tr>
</table>
</td>
</tr>
<%-- main content ends --%>
<%-- footer begins --%>
<%@ include file="/html/footer.html" %>
<%-- footer ends --%>
</table>
</body>
</html>