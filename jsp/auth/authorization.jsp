<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="true" %>
<%@ page import="server.*" %>
<%-- includes custom tags and stdl --%>
<%@ include file="/jsp/allTags.jsp" %>
<%
//set no cache
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Expires", "0");
response.setHeader("Pragma", "no-cache");
//used for page headers
String HEADERTITLE = (String)request.getAttribute(authInterface.HEADERTITLE);
//onlu 3 options forward their request
String chosenOption = (String)request.getAttribute(authInterface.OPTIONSATTR);
String fromURL = (String)request.getAttribute(authInterface.QUERYATTR);
//prevent null from showing in html
if (fromURL == null){
	fromURL = "";
}
%>
<html>

<head>
<meta name="description" content="Authorization page.">
<meta http-equiv="Cache-Control" content="no-cache" />
<META NAME="Robots" CONTENT="noindex">
<%@ include file="/html/meta.html" %>
<link rel="stylesheet" type="text/css" href="/styles/main.css">
<title>TradeUsedBooks.com - <%=HEADERTITLE%></title>
<%--
<script language=javascript>
function whyemail(){
var newWin = window.open("","_blank","height = 200,width = 300,resizable = no",false);
newWin.document.write("<html><body><p style=\"font-size:14px\">We have received several complains "
+"from our users saying that some of the messages do not get through."
+" We have yet to discover what the problem is with college email"
+" accounts and thus we ask to refrain from using them for a short while (try hotmail/yahoo/gmail - all are free).</p><p style=\"font-size:12px\">thank you for your understanding,<br>tradeusedbooks.com team</p></body></html>");
}
</script>
--%>
</head>
<body bgColor=#ffffff>
<%-- table is for <tr><td></td></tr> format for each include--%>
<table width="100%" border="0" cellspacing="0" cellpadding="0" align=center>
<%@ include file="/jsp/generalHeader.jsp" %>
<!-- main content begins-->
<%if (chosenOption.equals(authInterface.OPTION1)){%>
<%@ include file="/jsp/auth/auth_signin.jsp" %>
<%}%>
<%if (chosenOption.equals(authInterface.OPTION2)){%>
<%@ include file="/jsp/auth/auth_register.jsp" %>
<%}%>
<%if (chosenOption.equals(authInterface.OPTION3)){%>
<%@ include file="/jsp/auth/auth_assist.jsp" %>
<%}%>
<!-- main content ends-->
<!-- footer begins -->
<%@ include file="/html/footer.html" %>
<!-- footer ends -->	
</table>
</body>
</html>