<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="true" %>
<%@ page import="server.*,java.sql.*" %>
<%-- includes custom tags and stdl --%>
<%@ include file="/jsp/allTags.jsp" %>
<%@ include file="/jsp/conPool.jsp" %>
<%!
//cuts the string to the maximum size of the select options
private String cutOption(String word){
	if (word.length()>65){
		word = word.substring(0,62)+"...";
	}
	return word;
}
private String centerOption(String word){
	String result = "";
	for (int i=0;i<((65-word.length())/2);i++){
		result += "-";
	}
	result +=word;
	for (int i=0;i<((65-word.length())/2);i++){
		result += "-";
	}
	return result;
}
%>
<%
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Expires", "0");
response.setHeader("Pragma", "no-cache");
String HEADERTITLE = "Posting to Wanted";
String error = (String)request.getAttribute(
	postW.ERRORMESSAGE_ATTR);
String pageNumber = (String)request.getAttribute(postW.PAGE_ATTR);
wantedTable table = (wantedTable)request.getAttribute(postW.TABLE_ATTR);
String isbn = (String)request.getAttribute(postW.ISBN_FIELD);
String title = tools.htmlEncode((String)request.getAttribute(postW.TITLE_FIELD));
String author = tools.htmlEncode((String)request.getAttribute(postW.AUTHOR_FIELD));
String expiration = (String)request.getAttribute(postW.EXPIRATION_FIELD);
String college_id = (String)request.getAttribute(postW.COLLEGESELECT_FIELD);
String college_short = tools.htmlEncode((String)request.getAttribute(postW.COLLEGEINPUTSHORT_FIELD));
String college_full = tools.htmlEncode((String)request.getAttribute(postW.COLLEGEINPUTFULL_FIELD));
if (isbn == null){
	isbn = "";
}
if (title == null){
	title = "";
}
if (author == null){
	author = "";
}
if (college_id == null){
	college_id = "0";
}
if (college_short == null){
	college_short = "";
}
if (college_full == null){
	college_full = "";
}
%>
<html>
<head>
<meta name="description" 
content="book alert - receive an email alert when 
the book you want becomes available">
<meta http-equiv="Cache-Control" content="no-cache" />
<META NAME="Robots" CONTENT="all">
<%@ include file="/html/meta.html" %>
<link rel="stylesheet" type="text/css" 
href="/styles/main.css">
<title>TradeUsedBooks.com - Posting to Wanted</title>
<script language=javascript>
function proc(){
	document.f1.scol.disabled = false;
	<%-- disabling select fields if needed --%>
	if (document.f1.icols.value != ""
		|| document.f1.icolf.value != ""){
		document.f1.scol.disabled = true;
	}
}
</script>
</head>
<body bgColor=#ffffff>
<table width="100%" border="0" cellspacing="0" 
cellpadding="0" align=center>
<!-- header begins -->
<%@ include file="/jsp/generalHeader.jsp" %>
<!-- header ends -->
<!--main content begins -->
<tr><td>
<%if (pageNumber.equals("1")){
ConnectionPool conPool = getConnectionPool();
Connection connection = conPool.getConnection();
try{
%>
<table width="100%">
<tr valign=top>
<td style="border-right: 1px solid #CCCCCC;" width="200" nowrap>
<p>Why should you post to &quot;wanted&quot;?<ul>
	<li>it allowes other students at your college know that someone is willing to 
	buy their books</li>
	<li>you will be notified by email when this book is available at your 
	college</li>
	</ul>
	</p>
	<p>If you have any questions or simply need help, check our
<a href="/html/help">help page</a> or email us at
<a href="mailto:support@tradeusedbooks.com">support@tradeusedbooks.com</a></td>
<td>
Please provide the following information about the book you are looking for (all 
fields are required).<br><br>
<%if (error!=null){%>
<font color='#FF0000'><b>error: </b><%=error%></font>
<%}%>
<form name="f1" method=post action="<%=response.encodeURL(URLInterface.URLPWANTED)%>">
<input type=hidden name="<%=postW.HIDDENID_FIELD%>" 
value="<%=(String)session.getAttribute(postW.HIDDENID_FIELD)%>">
<table width="100%">
<tr>
	<td align=left>ISBN:</td>
	<td align=left valign=top>
		<input type=text 
		name="<%=postW.ISBN_FIELD%>" 
		value="<%=tools.htmlEncode(isbn)%>" 
		maxlength='13' size='13'>
	</td>
</tr>
<tr>
	<td align=left>Title<b>:</b></td>
	<td align=left valign=top>
		<input type=text 
		name='<%=postW.TITLE_FIELD%>'
		value="<%=tools.htmlEncode(title)%>" 
		maxlength='100' size='40'>
	</td>
</tr>
<tr>
	<td align=left>Author:</td>
	<td align=left valign=top>
		<input type=text 
		name='<%=postW.AUTHOR_FIELD%>' 
		value="<%=tools.htmlEncode(author)%>" 
		maxlength='100' size='40'>
	</td>
</tr>
<tr>
	<td align=left>expiration date:</td>
	<td align=left valign=top>
		<select name="<%=postW.EXPIRATION_FIELD%>">
			<option value="2"><%=new DateObject().getDateAfterDays(2*7)%> - in 2 weeks</option>
			<option value="4"><%=new DateObject().getDateAfterDays(4*7)%> - in 4 weeks</option>
			<option value="12"><%=new DateObject().getDateAfterDays(12*7)%> - in 12 weeks</option>
		</select>
	</td>
</tr>
<tr>
	<td align=left valign=top nowrap><a name="1">Select a College:</a></td>
	<td align=left valign=top>
<%-- college select --%>
<select id="scol" size="7" name="<%=postW.COLLEGESELECT_FIELD%>"  align=left>
<%-- college options --%>
<option
<%
if (college_id.equals("0")){
out.print(" selected ");
}
%> 
value="0"><%=centerOption("Select your college")%></option>
<%
resultList college_list = postUtils.getCollegeList(connection);
collegeTable college_table = (collegeTable)college_list.next();
while(college_table!=null){
out.print("<option ");
if (college_id.equals(college_table.getID()+"")){
out.print("selected ");
}
out.println("value=\""
	+college_table.getID()+"\">"+cutOption(college_table.getFull())
	+"</option>");
college_table = (collegeTable)college_list.next();
}
%>
</select>
</td></tr>
<tr><td align=left valign=top nowrap colspan=2><font>
	<b>or</b> specify your own college:</font><br>
	Short name:&nbsp;<input id="icols" type=text 
	name="<%=postW.COLLEGEINPUTSHORT_FIELD%>" value="<%=tools.htmlEncode(college_short)%>"
	maxlength="<%=collegeTable.SHORTNAME_MAX%>" size="20" 
	onkeyup="proc()"><br>
	<i>ex: UC Berkeley</i><br>
	Full name:&nbsp;&nbsp;&nbsp;&nbsp;<input id="icolf"
	 type=text name="<%=postW.COLLEGEINPUTFULL_FIELD%>" value="<%=tools.htmlEncode(college_full)%>" 
	 maxlength="<%=collegeTable.FULLNAME_MAX%>" size="20" 
	 onkeyup="proc()"><br>
	 <i>ex: University of California Berkeley </i>
	</td>
</tr>
</table>
<center><input type=submit value="post" style="color: #333399;"></center>
</form>
</td>
</tr>
</table>
<%
}catch (Exception e){
    throw e;
}finally{
    //recycle connection
    conPool.free(connection);
    connection = null;
}
%>
<%}else{%>
<p align="left">
You have successfully placed a request for the book. Please note that we will notify you 
of the availability of this title as soon as it is posted at our market places.<br>
<a href="<%=URLInterface.URLACCOUNTWANTED%>" 
class="refLinks">click here to go to your account page</a>

</p> 

<%}%>
</td></tr>
<!-- main content ends -->
<!-- footer begins -->
<%@ include file="/html/footer.html" %>
<!-- footer ends -->	
</table>
</body>
</html>