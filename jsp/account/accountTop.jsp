<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="true" %>
<%@ page import="server.*" %>
<%@ include file="/jsp/allTags.jsp" %>
<%
final String HEADERTITLE = (String)request.getAttribute(
	accoInterface.HEADERTITLE);
final String option = (String)request.getAttribute(
	accoInterface.OPTIONSATTR);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//Dtd HTML 4.01 Transitional//EN">
<html>

<head>
<meta name="description" content="Account Page.">
<meta http-equiv="Cache-Control" content="no-cache" />
<META NAME="Robots" CONTENT="noindex">
<%@ include file="/html/meta.html" %>
<link rel="stylesheet" type="text/css" href="/styles/main.css">
<link rel="stylesheet" type="text/css" href="/styles/account.css">
<title>TradeUsedBooks.com - My Account</title>
<script language=javascript>
function limitText(field,limit){
field.value = field.value.substr(0,limit);
}
</script>
</head>
<body bgColor=#ffffff>
<table width="100%" border="0" cellspacing="0" cellpadding="0" align=center>
<!-- header begins -->
<%@ include file="/jsp/generalHeader.jsp" %>
<!-- header ends -->
<tr>
<td>
<table width="100%">
<tr valign=top>
<!-- sidebar begins -->
<td width="120"><table><tr>
<td style="border-bottom: 1px solid #CCCCCC;border-top: 1px solid #CCCCCC;">
<table>
<%-- selectnot tag read info --%>
<my:selectnot separator=":" insert="<insert>" 
insert2="<insert2>" selected="<tr><td class=\"st\" nowrap><insert></td></tr>"
notSelected="<tr><td class=\"nst\" nowrap><a class=\"nsl\" href=\"<insert2>\"><insert></a></td></tr>">
account update:<%=response.encodeURL(URLInterface.URLACCOUNTUPDATE)%>:<%=accoInterface.OPTION1.equals(option)%>:
manage my books:<%=response.encodeURL(URLInterface.URLACCOUNTBOOKS)%>:<%=accoInterface.OPTION2.equals(option)%>:
my bids:<%=response.encodeURL(URLInterface.URLACCOUNTBIDS)%>:<%=accoInterface.OPTION3.equals(option)%>:
my history:<%=response.encodeURL(URLInterface.URLACCOUNTHISTORY)%>:<%=accoInterface.OPTION4.equals(option)%>:
books I'm looking for:<%=response.encodeURL(URLInterface.URLACCOUNTWANTED)%>:<%=accoInterface.OPTION5.equals(option)%></my:selectnot>
</table>
</td>
</tr></table></td>
<!-- sidebar ends -->
<td valign=top>
<!-- main content begins-->
<p align=right><a class="refLinks" href="<%=response.encodeURL(URLInterface.URLAUTHSIGNOUT)%>">sign out</a></p>