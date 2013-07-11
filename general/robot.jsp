<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="true" %>
<%@ page import="server.*" %>
<%@ include file="/jsp/allTags.jsp" %>
<%
keywordHolder keyword = new keywordHolder(StringInterface.KEYWORDS);
String keywords = keyword.constructKeywords(20);
%>
<html>
<head>
<meta http-equiv='Content-Language' content='en-us'>
<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>
<meta name="Robots" CONTENT="all">
<meta name="description" content="Buy and Sell your books on tradeusedbooks.com - a new college textbook exchange website">
<meta name="keywords" content="<%=keywords%>">
<meta http-equiv='Content-Language' content='en-us'>
<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>
<meta name="author" content="tradeusedbooks.com">
<meta name="distribution" content="IU">
<meta name="copyright" content="all rights reserved">
<title>College Textbooks - Trade Used Books.com</title>
</head>
<body>
<%-- 1 time links --%>
<%for (int i=0;i<SearchInterface.SITE.length;i++){%>
	<a href="<%=URLInterface.URLSEARCH%>?
			<%=SearchInterface.QUERY+"=%3C%2Fall%3Aall%2F%3E"%>&
			<%=SearchInterface.HEADER+"="+SearchInterface.SITE[i][0]%>">
			<%=SearchInterface.SITE[i][1]%>
	</a><br>						
<%}%>
<a href="/pbooks"><%=keyword.getRandomKeyword()%></a><br>
<a href="/pwanted"><%=keyword.getRandomKeyword()%></a><br>
<a href="/account/books"><%=keyword.getRandomKeyword()%></a><br>
<a href="/bookstores/account/update"><%=keyword.getRandomKeyword()%></a><br>
<a href="/"><%=keyword.getRandomKeyword()%></a><br>
<a href="/doc/support"><%=keyword.getRandomKeyword()%></a><br>
<a href="/doc/contact"><%=keyword.getRandomKeyword()%></a><br>
<a href="/doc/terms"><%=keyword.getRandomKeyword()%></a><br>
<a href="/doc/help"><%=keyword.getRandomKeyword()%></a><br>
<a href="http://www.highrankings.com/forum/index.php?showtopic=12675&st=0&p=125174&#entry125174"><%=keyword.getRandomKeyword()%></a><br>
<a href="http://www.highrankings.com/forum/index.php?showtopic=12533&st=0&p=125176&#entry125176"><%=keyword.getRandomKeyword()%></a><br>
<a href="http://blakeross.com/index.php?p=36"><%=keyword.getRandomKeyword()%></a><br>
<a href="http://www.inthe00s.com/index.php/topic,4779.msg300014.html#msg300014"><%=keyword.getRandomKeyword()%></a><br>
<a href="http://www.livejournal.com/users/tradeusedbooks/465.html"><%=keyword.getRandomKeyword()%></a><br>
<a href="http://finance.messages.yahoo.com/bbs?.mm=FN&action=m&board=13247466&tid=ebay&sid=13247466&mid=492006"><%=keyword.getRandomKeyword()%></a><br>
<a href="http://tradeusedbooks.blogspot.com"><%=keyword.getRandomKeyword()%></a><br>
<a href="http://www.screwbookprices.org">www.Screw Book Prices</a>
<a href="http://swapbooks.blogspot.com"><%=keyword.getRandomKeyword()%></a><br>
<a href="http://screwbookprices.blogspot.com"><%=keyword.getRandomKeyword()%></a><br>
<a href="http://people.ucsc.edu/~smirkin/"><%=keyword.getRandomKeyword()%></a><br>
<a href="http://spaces.msn.com/members/tradeusedbooks/"><%=keyword.getRandomKeyword()%></a><br>
<a href="http://www.jayde.com/cgi-bin/search.pl?a=2&query=tradeusedbooks&engine=domain&search=search">College Textbooks - used & rare</a>
<a href="/url/www.ebay.com">ebay</a><br>
<a href="/url/www.amazon.com">amazon</a><br>
<a href="/url/www.screwbookprices.com">screwbookprices</a><br>
<a href="/url/www.campusi.com">campusi</a><br>
<a href="/url/www.campusbooks.com">campus books</a><br>
<a href="http://auctionfire.com/">
Auctions</a> : Buy or sell almost anything.  No listing fees, free image hosting, and automatic re-list.  Sellers may link ads
to their own websites.
<a href="http://www.linkmarket.net/" title="#1 Free Link Exchange Directory On The Web - Link Market" target="_blank">#1 Free Link Exchange Directory On The Web - Link Market</a><br>Have you ever tried to exchange links, swap links, or trade links? Was it hard? Use link market instead; - it is easy to use, free and very smart. It will save you hours of work.
</body>
</html>