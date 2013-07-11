<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="false" %>
<%@ page import="server.*" %>
<%@ include file="/jsp/allTags.jsp" %>
<%
int search_page = Integer.parseInt((String)request.getAttribute(
	SearchInterface.SELECTEDSITE));
int search_pageNumber = Integer.parseInt((String)request.getAttribute(
	SearchInterface.PAGENUMBERATTR));
String pageLine = (String)request.getAttribute(
	SearchInterface.PAGELINEATTR);
Search search = (Search)request.getAttribute(
	SearchInterface.SEARCHATTR);
String queryString = (String)request.getAttribute(SearchInterface.QUERYSTRING);
String search_query = search.getQuery();
String search_urlQuery = java.net.URLEncoder.encode(search_query,"UTF-8");
response.setDateHeader("Expires",System.currentTimeMillis()
	+15000);
%>
<html>

<head>
<meta name="description" content="Find <%=tools.htmlEncode(search_query)%> and other college textbooks 
at the cheapest prices on tradeusedbooks.com">
<META NAME="Robots" CONTENT="all">
<meta name="keywords" content="<%=tools.htmlEncode(search_query)%>">
<%@ include file="/html/meta.html" %>
<link rel="stylesheet" type="text/css" href="/styles/main.css">
<link rel="stylesheet" type="text/css" href="/styles/search.css">
<title>Trade Used Books.com Search: <%=tools.htmlEncode(search_query)%></title>
<script language=javascript>
<!--
function limitResults()
{
	var newWindow = window.open("","limit","top = 200,left = "+(document.body.clientWidth-150)+",height = 100,width = 100,resizable = no,status = no,scrollbars = no,titlebar = no,toolbar = no,menubar = no,location = no",false);
	newWindow.document.write("");
	newWindow.document.close();
	newWindow.document.write("<html><head>"
    	+"<script>function change(){"
    	+"opener.location = '<%=URLInterface.URLSEARCH+"?"
	+queryString.replaceAll("&"+SearchInterface.RESULTLIMIT
	+"=[^&]*","")+"&"+SearchInterface.RESULTLIMIT
	+"="%>'+document.forms[0].limit.value;self.close();}"
    	+"<\/script><\/head><body bgColor=#C3D9FF style='border: solid black 1px;'>"
    	+"<form onSubmit=\"javascript:change()\"><input type=text name=limit size=2 maxlength=2 value='10'>"
    	+"<input type=button value=change onclick=\"javascript:change()\"></form><script>document.forms[0].limit.focus();<\/script></body></html>");
}
//-->
</script>
</head>

<body bgColor=#ffffff>
	<table width="100%">
		<!-- header begins -->
		<!-- <tr><td><table2></td></tr> -->
		<%@ include file="/jsp/search/search_header.jsp" %>
		<!-- header ends -->
		<!-- main content begins -->
		<tr><td valign=top>
			<%if (search.getTotalResults()==0){%>
			<p>&nbsp;</p>
			<p><img alt='alert' src='/myImages/alert.gif'> we were unable to find a match for &quot;<%=tools.htmlEncode(search_query)%>&quot;
			<p>There maybe no postings for this book at this time.
			<p>Nevertheless, we advise you to consider the following:
			<ul><li>try <a class="refLinks" href="<%=URLInterface.URLSEARCH+"?"
	+queryString.replaceAll(SearchInterface.QUERY
	+"=[^&]*",SearchInterface.QUERY+"=%3C%2Fall%3Aall%2F%3E")%>">browsing through all postings</a></li><li>check your search keywords (make sure everything is accurate)</li><li>it is often times helpful to make the search criteria broader (for example, when searching for author &quot;James Stewart&quot; use &quot;Stewart&quot; instead ) </li></ul>
			</p>
			<p>Still can&#39;t find it?</p>
			<p>This free service depends on how many people know about us, so 
			help us spread the word by tellings all your friends about how <b>cool</b> it is.</p>
			</p>
			<%}else if (search_page == 0){%>
				<%@ include file="/jsp/search/search_market.jsp" %>
			<%}else if (search_page == 1){%>
				<%@ include file="/jsp/search/search_wanted.jsp" %>
			<%}else if (search_page == 2){%>
				<%@ include file="/jsp/search/search_bookstores.jsp" %>
			<%}%>
		</td></tr>
		<tr><td><%=pageLine%></td></tr>
		<!-- main content ends -->
<%@ include file="/html/footer.html" %>
</table></body></html>