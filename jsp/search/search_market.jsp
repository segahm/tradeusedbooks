<%
resultList list = (resultList)request.getAttribute(SearchInterface.CATEGORYLISTATTR);
int resultView = Integer.parseInt((String)request.getAttribute(SearchInterface.RESULTVIEW));
int opositeResultView = 0;
if (resultView == 0){
	opositeResultView = 1;
}
%>
<table width="100%">
<%-- category path  begins --%>
<tr height="23"><td valign=top>
<table width="100%"><tr>
<td>
<%if (!((String)request.getAttribute(SearchInterface.CATEGORYHEADERATTR)).equals("")){%>
<%=(String)request.getAttribute(SearchInterface.CATEGORYHEADERATTR)%>
<%}else{
String[] messages = new String[]{"<font color=#cc0000>Tip: </font>use quotes around your search keyword[s] to restrict the search to fewer results",
"<font color=#cc0000>quick message: </font>help spread the word, this free service depends on it!",
"<font color=#cc0000>just sold a book? </font>don't forget to leave your feedback for the other user",
"<font color=#cc0000>just purchased a book? </font>don't forget to leave your feedback for the other user",
"<font color=#cc0000>Tip: </font>can't find the textbook? Use \"wanted\" to be notified when the book becomes available",
"<font color=#cc0000>did you know? </font>the search is done based on three fields: isbn, title, and author",
"<font color=#cc0000>did you know? </font>your postings can also be found by major search engines"};
out.println("<font size=2>"+messages[(int)(Math.random()*messages.length)]+"</font>");
}%>
</td>
<td valign=top align=right nowrap><a class="refLinks" href="<%=URLInterface.URLSEARCH%>?
<%=SearchInterface.QUERY+"=%3C%2Fall%3Aall%2F%3E"%>&<%=SearchInterface.HEADER+"="+SearchInterface.SITE[0][0]%>">browse all</a>&nbsp;
<a class="refLinks" href="javascript:limitResults()">limit results</a>&nbsp;
<a class="refLinks" href="<%=URLInterface.URLSEARCH+"?"
	+queryString.replaceAll("&"+SearchInterface.RESULTVIEW
	+"=[^&]*","")+"&"+SearchInterface.RESULTVIEW
	+"="+opositeResultView%>">change view</a></td>
</tr></table>
</td></tr>
<%-- category path ends --%>
<tr><td><table width="100%" height="100%"><tr valign=top>
<!-- search results begin -->
<td valign=top width="100%">
	<table cellSpacing='0' cellPadding='5' width='100%' border='0'>
<%
boolean firstRow = true;
Object[] row = search.next();
boolean rowOdd = false;
while (row!=null){%>
<!-- new result row -->
<%
	rowOdd = !rowOdd;
	if (!firstRow && resultView==0){%>
		<tr><td colspan="2"><hr></td></tr>
	<%}else if(firstRow && resultView!=0){%>
		<tr style='font-family: Arial'><th align=middle width='100'>ISBN</th><th align=left width='50%'>Title</th><th align=left width='168'>Author</th><th align=middle width='50'>Price</th></tr>
	<%}
	firstRow = false;
	booksTable table = (booksTable)row[0];
	isbnTable table2 = (isbnTable)row[1];
	String result_imageURL = table2.getSimage();
	String result_imageTitle = table2.getTitle();
	String result_publisher = table2.getPublisher();
	String result_numberOfPages = table2.getPages();
	if (result_imageTitle == null){
		result_imageTitle = "click to view full description";
	}
	String result_id = table.getID();
	String result_isbn = tools.formatISBN(table.getISBN());
	String result_title = table.getTitle();
	String result_author = tools.lengthFormat(table.getAuthor(),30);
	String result_price = table.getPrice()+"";
if (resultView == 0){
%>
	<%@ include file="/jsp/search/searchM_results1.jsp" %>
<%}else{%>
	<%@ include file="/jsp/search/searchM_results2.jsp" %>
<%}%>
	<%row = search.next();%>
	<!-- result row ends -->
<%}%>
	</table>
</td>
	<!-- search results end -->
	<!-- category begins -->
<td width="150" nowrap>
<%if (list != null){%>
	<table><tr><td valign=top 
	style="border-left: 1px solid #6699FF;padding-left: 5" nowrap>

	<p align=left><font color=#6f6f6f size=-1>Matching Categories</font></p>
	<%-- category link begins --%>
	<%if (list.getOption() == list.COLLEGELIST){%>
		<my:categoryList insertID="<id>" insertNAME="<name>" attributeName="<%=SearchInterface.CATEGORYLISTATTR%>">
		<p style='margin-bottom:5px;margin-top:0px'>
	<a class='categoryLinks' 
	href="<%=URLInterface.URLSEARCH%>?<%=queryString%>&<%=SearchInterface.COLLEGE%>=<id>"><name></a></p>
		</my:categoryList>
	<%}else if (list.getOption() == list.DEPARTMENTLIST){%>
		<my:categoryList insertID="<id>" insertNAME="<name>" attributeName="<%=SearchInterface.CATEGORYLISTATTR%>">
		<p style='margin-bottom:5px;margin-top:0px'>
	<a class='categoryLinks' 
	href="<%=URLInterface.URLSEARCH%>?<%=queryString%>&<%=SearchInterface.DEPARTMENT%>=<id>"><name></a></p>
		</my:categoryList>
	<%}else if (list.getOption() == list.COURSELIST){%>
		<my:categoryList insertID="<id>" insertNAME="<name>" attributeName="<%=SearchInterface.CATEGORYLISTATTR%>">
		<p style='margin-bottom:5px;margin-top:0px'>
	<a class='categoryLinks' 
	href="<%=URLInterface.URLSEARCH%>?<%=queryString%>&<%=SearchInterface.COURSE%>=<id>"><name></a></p>
		</my:categoryList>
	<%}else if (list.getOption() == list.TEACHERLIST){%>
		<my:categoryList insertID="<id>" insertNAME="<name>" attributeName="<%=SearchInterface.CATEGORYLISTATTR%>">
		<p style='margin-bottom:5px;margin-top:0px'>
	<a class='categoryLinks' 
	href="<%=URLInterface.URLSEARCH%>?<%=queryString%>&<%=SearchInterface.TEACHER%>=<id>"><name></a></p>
		</my:categoryList>
	<%}%>
	</td></tr></table>
	<%-- category link ends --%>
<%}else{%>
	<%@ include file="/jsp/ads.jsp" %>
<%}%>
</td>
		<!-- category ends -->
</tr></table></td></tr>
</table>