<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="true" %>
<%@ page import="server.*" %>
<%@ include file="/jsp/allTags.jsp" %>
<%
String bookID = (String)request.getAttribute(bookstoreAccount.BOOKIDATTR);
String title = (String)request.getAttribute(bookstoreAccount.TITLE_FIELD);
String author = (String)request.getAttribute(bookstoreAccount.AUTHOR_FIELD);
String isbn = (String)request.getAttribute(bookstoreAccount.ISBN_FIELD);
String copies = (String)request.getAttribute(bookstoreAccount.COPIES_FIELD);
String usedPrice = (String)request.getAttribute(bookstoreAccount.USEDPRICE_FIELD);
String newPrice = (String)request.getAttribute(bookstoreAccount.NEWPRICE_FIELD);
String comment = (String)request.getAttribute(bookstoreAccount.COMMENT_FIELD);
String[] conditions = (String[])request.getAttribute(bookstoreAccount.CONDITION_FIELD);
String error = (String)request.getAttribute(bookstoreAccount.ERRORMESSAGE);
if (bookID == null){
	bookID = "";
}
if (title == null){
	title = "";
}
if (author == null){
	author = "";
}
if (isbn == null){
	isbn = "";
}
if (comment == null){
	comment = "";
}
if (usedPrice == null){
	usedPrice = "";
}
if (newPrice == null){
	newPrice = "";
}
if (copies == null){
	copies = "";
}
%>
Please enter the following information about the book you wish to post<p>
<%if (error != null){%>
<font color=#FF0000><b>error: </b><%=error%></font>
<%}%>
<form method=post action="<%=response.encodeURL(URLInterface.URL_BOOKSTORE_ACCOUNT_POST+"?"+bookID)%>">
<input type=hidden name="<%=bookstoreAccount.HIDDENID%>" value="<%=(String)request.getAttribute(bookstoreAccount.HIDDENID)%>">
<table width="100%">
<tr valign=top><td>Title:</td><td><input type=text name="<%=bookstoreAccount.TITLE_FIELD%>" value="<%=tools.htmlEncode(title)%>" size="30" maxlength="<%=bookstoreBooksTable.TITLE_MAX%>"></td></tr>
<tr valign=top><td>Author:</td><td><input type=text name="<%=bookstoreAccount.AUTHOR_FIELD%>" value="<%=tools.htmlEncode(author)%>" size="30" maxlength="<%=bookstoreBooksTable.AUTHOR_MAX%>"></td></tr>
<tr valign=top><td>ISBN:<br><i><font size='2'>optional</font></i></td><td><input type=text name="<%=bookstoreAccount.ISBN_FIELD%>" value="<%=tools.htmlEncode(isbn)%>" size="10" maxlength="<%=bookstoreBooksTable.ISBN_LENGTH2%>"></td></tr>
<tr valign=top><td>Copies available:</td><td><input type=text name="<%=bookstoreAccount.COPIES_FIELD%>" value="<%=copies%>" size="3" maxlength="3"></td></tr>
<tr valign=top><td>Used Price:</td><td>$<input type=text name="<%=bookstoreAccount.USEDPRICE_FIELD%>" value="<%=usedPrice%>" size="3" maxlength="<%=bookstoreBooksTable.PRICE_MAX%>"></td></tr>
<tr valign=top><td>New Price:</td><td>$<input type=text name="<%=bookstoreAccount.NEWPRICE_FIELD%>" value="<%=newPrice%>" size="3" maxlength="<%=bookstoreBooksTable.PRICE_MAX%>"></td></tr>
<tr valign=top>
<td>Short comments:<br><i><font size='2'>optional</font></i></td>
<td><input type=text name="<%=bookstoreAccount.COMMENT_FIELD%>" value="<%=comment%>" size="40" maxlength="<%=bookstoreBooksTable.COMMENT_MAX%>"> ex: special coupons</td></tr>
<tr valign=top>
<td>Conditions available:<br>
<a target="_blank" 
		href="<%=response.encodeURL(URLInterface.URLCONDGUID)%>" 
		style="color=#0033CC;font-size:12">condition guideline</a><br>
<font size=2>use ctrl key to select multiple</font>
</td>
<td valign=top>
<select name="<%=bookstoreAccount.CONDITION_FIELD%>" multiple size=4>
<option <%
for (int i=0;i<conditions.length;i++){
	if (conditions[i].equals(bookstoreBooksTable.CONDITION_ACCEPTABLE+"")){
		out.println("selected");
	}
}
%> value="<%=bookstoreBooksTable.CONDITION_ACCEPTABLE%>"><%=bookstoreBooksTable.CONDITION_NAMES[bookstoreBooksTable.CONDITION_ACCEPTABLE]%></option>
<option <%
for (int i=0;i<conditions.length;i++){
	if (conditions[i].equals(bookstoreBooksTable.CONDITION_GOOD+"")){
		out.println("selected");
	}
}
%> value="<%=bookstoreBooksTable.CONDITION_GOOD%>"><%=bookstoreBooksTable.CONDITION_NAMES[bookstoreBooksTable.CONDITION_GOOD]%></option>
<option <%
for (int i=0;i<conditions.length;i++){
	if (conditions[i].equals(bookstoreBooksTable.CONDITION_LIKENEW+"")){
		out.println("selected");
	}
}
%> value="<%=bookstoreBooksTable.CONDITION_LIKENEW%>"><%=bookstoreBooksTable.CONDITION_NAMES[bookstoreBooksTable.CONDITION_LIKENEW]%></option>
<option <%
for (int i=0;i<conditions.length;i++){
	if (conditions[i].equals(bookstoreBooksTable.CONDITION_NEW+"")){
		out.println("selected");
	}
}
%> value="<%=bookstoreBooksTable.CONDITION_NEW%>"><%=bookstoreBooksTable.CONDITION_NAMES[bookstoreBooksTable.CONDITION_NEW]%></option>
</select>
</td></tr>
</table>
<p align=center><input type=submit value="post" style="color: #333399"></p>
</form>