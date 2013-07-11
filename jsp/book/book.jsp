<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="false" %>
<%@ page import="server.*" %>
<%@ include file="/jsp/allTags.jsp" %>
<%
final String HEADERTITLE = "Book";
booksTable book = (booksTable)request.getAttribute(bookInterface.ATTR_BOOKINFO);
isbnTable bookInfo = (isbnTable)request.getAttribute(bookInterface.ATTR_ISBNINFO);
usersTable seller = (usersTable)request.getAttribute(bookInterface.ATTR_USERINFO);
int[] feedback = seller.getFeedback();
double feedbackPerc = 0.0;
if ((feedback[0]+feedback[1]+feedback[2])!=0){
	feedbackPerc = (100/(feedback[0]+feedback[1]+feedback[2]))*feedback[0];
}
String fullname_college = (String)request.getAttribute(bookInterface.FULLNAME_COLLEGE);
String shortname_college = (String)request.getAttribute(bookInterface.SHORTNAME_COLLEGE);
String name_department = (String)request.getAttribute(bookInterface.NAME_DEPARTMENT);
String name_course = (String)request.getAttribute(bookInterface.NAME_COURSE);
String name_teacher = (String)request.getAttribute(bookInterface.NAME_TEACHER);
String encodedTitle = java.net.URLEncoder.encode(book.getTitle(),"UTF-8");
%>
<html>
<head>
<%@ include file="/html/meta.html" %>
<meta name="description" content="find <%=tools.htmlEncode(book.getTitle())%> and other college textbooks on tradeusedbooks.com">
<meta name="Robots" content="all">
<meta name="keywords" content="<%=tools.htmlEncode(book.getTitle())%>">
<link rel="stylesheet" type="text/css" href="/styles/main.css">
<title>Trade Used Books.com Book: <%=tools.htmlEncode(book.getTitle())%></title>
<style>
.questionimg a {display:block;width: 100px;height: 20px;padding:10px 0px 10px 10px;
	font: 12px Verdana;color:#000000;background: url("/myImages/question.gif") 0 0 no-repeat;
	background-position: 0 8px;text-decoration: none;
}
.questionimg a:hover { 
	color: #333399;
	background: url("/myImages/questionhover.gif") 0 0 no-repeat;
	background-position: 0 8px;
}
</style>
</head>
<body bgColor=#ffffff>
<table>
<%-- header begins --%>
<%@ include file="/jsp/generalSearchHeader.jsp" %>
<%-- header ends --%>
<%-- main content begins --%>
<tr>
	<td width="100%">
	<table width="100%">
	<tr valign=top>
		<td style="padding-left:10px;word-wrap:break-word">
		<table cellpadding="5">
		<tr><td colspan=2>
		<%-- 
		creating quick search links for this title,
		links contain title as query and
		--%>
		<a class="refLinks" style="TEXT-DECORATION: none" href="/query?q=<%=encodedTitle%>&h=m">
		-</a>
		<%if (shortname_college != null){%>
			&nbsp;<a class="refLinks" 
			href="/query?q=<%=encodedTitle%>&h=m&c=<%=book.getCollegeID()%>"><%=shortname_college%></a>
			<%if (name_department != null){%>
				&nbsp;&gt;&nbsp;<a class="refLinks" 
				href="/query?q=<%=encodedTitle%>&h=m&c=<%=book.getCollegeID()%>&d=<%=book.getDepartmentID()%>"><%=tools.lengthFormat(name_department,20)%></a>
			<%}%>
			<%if (name_course != null){%>
				&nbsp;&gt;&nbsp;<a class="refLinks" 
				href="/query?q=<%=encodedTitle%>&h=m&c=<%=book.getCollegeID()%>&cr=<%=book.getCourseID()%>"><%=tools.lengthFormat(name_course,20)%></a>
			<%}%>
			<%if (name_teacher != null){%>
				&nbsp;&gt;&nbsp;<a class="refLinks" 
				href="/query?q=<%=encodedTitle%>&h=m&c=<%=book.getCollegeID()%>&t=<%=book.getTeacherID()%>"><%=tools.lengthFormat(name_teacher,20)%></a>
			<%}%>
		<%}%>
		<p><b><font face="Verdana"><%=tools.htmlEncode(book.getTitle())%></font></b><br>
		by <%=tools.htmlEncode(book.getAuthor())%><br>
		<b>date posted:</b> <%=book.getDate()%></p>
		</td></tr>
		<tr>
			<td>
			<% if (bookInfo==null || bookInfo.getMimage()==null){%>
				<img alt="picture not available" src="/myImages/book.gif" border=1>
			<%}else{%>
				<img width=90 height=140 alt="<%=bookInfo.getTitle()%>" src="<%=bookInfo.getMimage()%>" border=1>
			<%}%>
			</td>
			<td valign=top align=left nowrap>
				<table><tr>
				<td nowrap>
					<b><font face="Times New Roman">Seller: </font></b>
					<a title="click here to see other books by this user"
					href="<%=URLInterface.URLSEARCH
					+"?"+SearchInterface.QUERY+"="+java.net.URLEncoder.encode("</userid:"+seller.getID()+"/>","UTF-8")
					+"&"+SearchInterface.HEADER+"="+SearchInterface.SITE[0][0]%>"
					style="color:#000080"><%=seller.getUsername()%></a> 
					( <a title="seller's feedback score" href="<%=URLInterface.URLFEEDBACK%>?<%=seller.getID()%>" 
					style="color:#000080;"><%=(feedback[0]-feedback[2])+""%></a> )
				</td>
				<td>
					<a title="seller has a positive rating of <%=feedbackPerc%>%"><table><tr><td width="100" nowrap background="/myImages/feedbackbg.gif"><img src="/myImages/positive.gif" width="<%=Math.ceil(feedbackPerc)%>" height="10"></td></tr></table></a>
				</td>
				</tr></table>
			<%if (bookInfo != null){%>
				<b><font color="#333399">Book details </b>(information obtained 
				based on isbn):</font><br>
				<%if (bookInfo.getPublisher() != null){%>
					<b>Publisher:</b> <%=bookInfo.getPublisher()%><br>
				<%}%>
				<%if (bookInfo.getBinding() != null){%>
					<b>Binding:</b> <%=bookInfo.getBinding()%><br>
				<%}%>
				<%if (bookInfo.getListPrice() != null){%>
					<b>Retail Price:</b> <%=bookInfo.getListPrice()%><br>
				<%}%>
				<%if (bookInfo.getPages() != null){%>
					<b># of Pages:</b> <%=bookInfo.getPages()%><br>
				<%}%>
			<%}%>
			<div class=questionimg>	
				<b><a href="<%=URLInterface.URLQUESTION+"?"+QuestionServlet.BOOKID_FIELD+"="+book.getID()+"&"+QuestionServlet.SELLERID_FIELD+"="+book.getSellerID()%>">&nbsp;&nbsp;question?</a></b>
			</div>
			</td>
		</tr>
		<tr><td align=left><b>Price: <font color="#FF3300">$<%=book.getPrice()%></font></b></td>
		<td align=left>
		<%if (book.getISBN() != null){%>
			<b>ISBN:</b> <%=tools.formatISBN(book.getISBN())%>
		<%}%>
		</td></tr>
		<tr><td colspan=2 align=left><b><font color="#333399">Information given by seller:</b></td></tr>
		<tr><td colspan=2 align=left>
		<b>Condition:</b> <%=booksTable.CONDITIONS[book.getCondition()].toUpperCase()%><br>
		<%if (book.getComment() != null){%>
			<b>Comments:</b> <%=tools.htmlEncode(book.getComment())%>
		<%}%>
		</td></tr>
		<tr><td colspan=2 align=left>
		<%-- printing college info --%>
		<%if (fullname_college != null){%>
			<font face="Courier">College: </font><a class="refLinks" 
			href="/query?q=<%=encodedTitle%>&h=m&c=<%=book.getCollegeID()%>"><%=fullname_college%></a><br>
			<%if (name_department != null){%>
				<font face="Courier">Department: </font><a 
				class="refLinks" href="/query?q=<%=encodedTitle%>&h=m&c=<%=book.getCollegeID()%>&d=<%=book.getDepartmentID()%>"><%=name_department%></a><br>
			<%}%>
			<%if (name_course != null){%>
				<font face="Courier">Course: </font><a 
				class="refLinks" href="/query?q=<%=encodedTitle%>&h=m&c=<%=book.getCollegeID()%>&cr=<%=book.getCourseID()%>"><%=name_course%></a><br>
			<%}%>
			<%if (name_teacher != null){%>
				<font face="Courier">Teacher: </font><a 
				class="refLinks" href="/query?q=<%=encodedTitle%>&h=m&c=<%=book.getCollegeID()%>&t=<%=book.getTeacherID()%>"><%=name_teacher%></a><br>
			<%}%>
		<%}%>
		</td></tr>
		</table>
		<form action="<%=URLInterface.URLBUY%>" method=get>
		<input type=hidden name="<%=bookInterface.FIELD_ID%>" value="<%=book.getID()%>">
		<input type=hidden name="<%=bookInterface.FIELD_SELLERID%>" value="<%=book.getSellerID()%>">
		<input type=hidden name="<%=bookInterface.FIELD_HIDDENID%>" value="<%=Math.abs(book.getID().hashCode())%>">
		<center><input type=submit value="propose to buy" style="color: #333399"></center>
		</form>
		</td>
		<!-- brief note begins -->
		<td width=200 style="padding-top:10px" align=right>
		<table width="100%">
		<tr><td bgcolor=#336699 style="color: #FFFFFF;">Please Note:</td></tr>
		<tr><td style="border: 2 solid #000000;" bgcolor="#eeeeee">The 
			description of the book is based on the information the seller 
			provided. If you wish to purchase this title you may do so by 
			clicking on &quot;propose to buy&quot; button where upon your 
			request you will have a chance to provide your contact information 
			to the seller.</td></tr>
		</table>
		<table width="100%">
		<tr><td bgcolor=#336699 style="color: #FFFFFF;">How it works?</td></tr>
		<tr><td style="border: 2 solid #000000;" bgcolor="#eeeeee">We will disclose your contact information to the seller only once you purchase this title. This contact information will then provide the two parties (you and the seller) with means to arrange the best place and time to exchange the book/money. The two parties will also be allowed to leave feedback for one another - thus helping and promoting tradeusedbooks community.</td></tr>
		</table>
		</td>
		<!-- brief note ends -->
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