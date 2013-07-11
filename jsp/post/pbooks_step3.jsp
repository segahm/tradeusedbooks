<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="true" %>
<%@ page import="server.*" %>
<%-- includes custom tags and stdl --%>
<%@ include file="/jsp/allTags.jsp" %>
<%
String HEADERTITLE = "Posting a Book";
String error = (String)request.getAttribute(
	postBInterface.ERRORMESSAGE);
String unqueFormID = (String)request.getAttribute(
	postBInterface.UNIQUEFORMATTR);
String price = request.getParameter(
	postBInterface.PRICEFIELD);
String comments = 	request.getParameter(
	postBInterface.COMMENTSFIELD);
postForm form = (postForm)session.getAttribute(unqueFormID);
if (form!=null){
	if (form.getComment()!=null){
		comments = form.getComment();
	}
	if (form.getPrice()!=0){
		price = form.getPrice()+"";
	}
}
if (comments == null){
	comments = "";
}
if (price == null){
	price = "";
}
comments = tools.htmlEncode(comments);
price = tools.htmlEncode(price);
%>
<html>
<head>
<meta name="description" content="posting is easy as 1,2,3!">
<meta http-equiv="Cache-Control" content="no-cache" />
<META NAME="ROBOT" CONTENT="all">
<%@ include file="/html/meta.html" %>
<link rel="stylesheet" type="text/css" href="/styles/main.css">
<title>TradeUsedBooks.com - Posting, Step 3</title>
<script>
function back(){
	document.mainForm.action = "<%=response.encodeURL(URLInterface.URLPBOOKS+"?back")%>";
	document.mainForm.submit();
}
</script>
</head>
<body bgColor=#ffffff>
<table width="100%" border="0" cellspacing="0" cellpadding="0" align=center>
<!-- header begins -->
<%@ include file="/jsp/generalHeader.jsp" %>
<!-- header ends -->
<!--main content begins -->
<tr><td>
<table width="100%">
<tr valign=top>
<td style="border-right: 1px solid #CCCCCC;" width="250" nowrap>
<font color="#800000">a few useful tips:</font><ul>
	<li>the best way to set a reasonable price is to research the web for your 
	book</li>
	<li>remember, there is no middleman which means you just need to set the 
	price a little bit under bookstore&#39;s and you will still get more money than 
	you would if you were to sell it back to the bookstore</li>
	<li>make sure to follow condition guideline because others will</li>
	<li>&quot;comments&quot; - use this field to provide any additional description about 
	the book (ex: professor still accepts this old edition)</li>
</ul>
</td>
<td>
<h1>Step III</h1>
Please enter the fields below and make sure to check our condition guideline before proceeding.<br><br>
<%if (error!=null){%>
<img src="/myImages/alert.gif">
<font color='#FF0000'><b>error: </b>
<%=error%></font>
<%}%>
<form name="mainForm" method=post 
action="<%=response.encodeURL(URLInterface.URLPBOOKS)%>">
<%-- pagestep parameter let's the prog. know where 
we came from--%>
<input type=hidden 
	name="<%=postBInterface.PAGESTEP%>" value="3">
<%-- this will identify this particular form and
should be passed from page to page using request attr.--%>
<input type=hidden 
	name="<%=postBInterface.UNIQUEFORMATTR%>" 
	value="<%=unqueFormID%>">
<table width="100%">
<tr>
	<td align=left valign=top>Condition:</td>
	<td align=left valign=top>
		<select size="1" name="<%=postBInterface.CONDITIONFIELD%>" >
			<option 
			<%if (form.getCondition() == booksTable.CONDITION_NEW){%>
			selected 
			<%}%>
			value="<%=booksTable.CONDITION_NEW%>">New</option>
			<option 
			<%if (form.getCondition() == booksTable.CONDITION_LIKENEW){%>
			selected 
			<%}%>
			value="<%=booksTable.CONDITION_LIKENEW%>">Like New</option>
			<option 
			<%if (form.getCondition() == booksTable.CONDITION_GOOD){%>
			selected 
			<%}%>
			value="<%=booksTable.CONDITION_GOOD%>">Good</option>
			<option 
			<%if (form.getCondition() == booksTable.CONDITION_ACCEPTABLE){%>
			selected 
			<%}%>
			value="<%=booksTable.CONDITION_ACCEPTABLE%>">Acceptable</option>
		</select>
		<a target="_blank" 
		href="<%=response.encodeURL(URLInterface.URLCONDGUID)%>" 
		style="color=#0033CC;font-family: Verdana; 
		font-weight:bold; 
		font-size:11">condition guideline</a>
	</td>
</tr>
<tr>
	<td align=left valign=top>Price: </td>
	<td align=left valign=top>$<input 
	name="<%=postBInterface.PRICEFIELD%>" value="<%=price%>" 
	size='3' maxlength='<%=booksTable.PRICE_MAX%>'>.00</td>
</tr>
<tr>
	<td align=left valign=top>Comments: <br><i><font 
	size='2'>optional</font></i></td>
	<td align=left valign='top'><input type="text" 
	name="<%=postBInterface.COMMENTSFIELD%>" 
	value="<%=comments%>" 
	size="40"  maxlength='<%=booksTable.COMMENT_MAX%>'></td>
</tr>
</table>
<center><input type=button onclick="back()" value="<<step back" style="color: #333399;">&nbsp;&nbsp;&nbsp;<input 
type=submit value="next step>>" style="color: #333399;"></center>
</form>
</td>
</tr>
</table>
</td></tr>
<!-- main content ends -->
<!-- footer begins -->
<%@ include file="/html/footer.html" %>
<!-- footer ends -->
</table>
</body>
</html>