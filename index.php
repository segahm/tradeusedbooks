<?php
//setting secret id
$genid = str_shuffle('qwertyuiopasdfghjklzxcvbnm123456789');
//the following format will be used to pass header page index.php?path=$HEADER[n]
//the following are header names accepted by application
$HEADER1 = 'm';
$HEADER2 = 'w';
$HEADER3 = 'b';
//search field is passed using /query?q="some value"
?>
<?//if m or not (b and w)
if (!isset($_GET['path'])){
	$_GET['path'] = $HEADER1;
}?>
<html><head>
<meta http-equiv='Content-Language' content='en-us'>
<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>
<meta name="Robots" content="all">
<meta name="description" content="Buy and Sell your books on tradeusedbooks.com - a new college textbook exchange website">
<meta name="keywords" content="books,trade,book,exchange,book exchange,sell,author,textbooks,law books,law,print,out of print,days,adobe,the book exchange,sale,sold,rare,new and userd,bookstore,search,old and rare">
<?include_once("html/meta.html");?>
<link rel="Stylesheet" type="text/css" href="/styles/main.css">
<link rel="Shortcut Icon" href="/favicon.ico">
<title>College Textbooks - Trade Used Books.com</title>
<script language=javascript>
function validate(){
	if (!document.emails.to.value.match(/^[ ]*[a-zA-Z ]+,[ ]+[a-zA-Z0-9_\-\.]+@[a-zA-Z0-9\-]+\.[a-zA-Z0-9\-\.]+[ ]*$/)){
		alert("please use the following format for your friend's email: John Doe, john@example.com");
		return false;
	}else if (!document.emails.from.value.match(/^[ ]*[a-zA-Z ]+,[ ]+[a-zA-Z0-9_\-\.]+@[a-zA-Z0-9\-]+\.[a-zA-Z0-9\-\.]+[ ]*$/)){
		alert("please use the following format for your own email: My Name, name@example.com");
		return false;
	}else{
		return true;
	}
}
</script>
</head><body bgcolor=#EEECEC><table align=center>
<tr><td><form name=f action="/query" method=get>
	<table width=720 height=220 cellpadding=7 align=center background=/myImages/searchbox.gif style="background-repeat:no-repeat;">
	<tr height=100% width=100%>
	<td valign=top align=left width=430 style="padding-left:105px;padding-top:75px" nowrap>
		<table>
			<tr>
				<td>
					<input type=hidden value="<?echo $_GET['path']?>" name=h>
					<input type=text maxLength=256 value="" name=q size=55>
				</td>
				<td>
					<!--<input type=image src=/myImages/search.jpg name="" value="">-->
					<input type=submit style="background-color:#C9F226;border-color:#375871;color:#2F4B61;font-weight:bold" value="search">
				</td>
			</tr>
		</table>
	</td>
	<td align=left>
	<table height=100%><tr><td valign=top align=left style="padding-top:40px" style="font-size:14px" nowrap>
	<?if ($_GET['path'] == $HEADER1){?>
	<font color="#C9F226"><b>· market place</b></font><br>
	<?}else{?>
	· <a style="color:#33CCFF;" href="/p/<?echo $HEADER1?>">market place</a><br>
	<?}?>
	<?if ($_GET['path'] == $HEADER2){?>
	<font color="#C9F226"><b>· wanted</b></font><br>
	<?}else{?>
	· <a style="color:#33CCFF;" href="/p/<?echo $HEADER2?>">wanted</a><br>
	<?}?>
	<?if ($_GET['path'] == $HEADER3){?>
	<font color="#C9F226"><b>· bookstores</b></font><br>
	<?}else{?>
	· <a style="color:#33CCFF;" href="/p/<?echo $HEADER3?>">bookstores</a>
	<?}?>
	</td></tr></table></td></tr></table></form></td></tr><tr><td><table width=100%>
	<tr><td align=center valign=top style="padding-top:60px;" width=354 height=356 background=/myImages/aboutus.gif style="background-repeat:no-repeat;">
	<table cellspacing=10 width=300><tr><td align=left valign=center width=100% height=100%>
	<font color="#ffffff" style="font-size:14px">
		TradeUsedBooks is a student-founded web site that is dedicated to promoting textbook exchange at every college. 
		We ourselves spent a lot of time looking for cheap textbooks and finally realized that there is no cheaper way of buying textbooks than buying them from fellow friends and students directly. 
		And so tradeusedbooks.com was created with a mission to serve college students by providing an easy way to find and communicate with other sellers and buyers. 
		We now invite you to join our growing community by posting your own textbooks today!<br>-tradeusedbooks.com team</font></td></tr></table></td>
	<td align=right valign=top style="padding-top:10px;">
		<table background=/myImages/spreadtheword.gif cellspacing=10 width=324 height=269 style="background-repeat:no-repeat;">
		<tr><td align=left valign=center width=100% height=100% style="padding-left:10px;padding-top:30px">
			<form name="emails" action="/mailinvite.php" method=post onSubmit="return validate();"><font color=#33CC33 style="font-size:14px">
			-refer your friends and classmates!
			</font><table width=100% cellspacing=10>
			<tr valign=top><td style="color:#ffffff;font-size:18">From: </td><td align=left nowrap><input type=text name="from" value=""  size=20 maxlength=60><br><font color=#ffffff style="font-size:14px">ex: Myname, myemail@gmail.com</font></td></tr>
			<tr valign=top><td style="color:#ffffff;font-size:18">To:</td><td nowrap><input type=text name="to" value="" size=20 maxlength=60><br><font color=#ffffff style="font-size:14px">ex: Name, friendsemail@gmail.com</font></td></tr>
			<tr><td colspan=2 align=center>
				<input type=submit value="invite my friend" style="background-color:#C9F226;border-color:#375871;color:#2F4B61;font-weight:bold"></td></tr></table>
			<a style="color:#FF0000;font-size:14px" href="/doc/terms#nospam">read our no spam policy</a>
			<input type=hidden name="genid" value="<?echo $genid?>">
			</form>
		</td></tr></table></td></tr></table></td></tr><tr><td align=center>
		<a class="refLinks" href="/pbooks" title="post books you wish to sell">
		Post Books for Trade</a>
		·
		<a class="refLinks" href="/pwanted" title="put up a request for a textbook">
		Post Wanted Books</a>
		·
		<a class="refLinks" href="/account/books" title="manage books, bids, feedback, etc...">
		My Account</a>
	</td></tr>
	<?include_once("html/footer.html");?>
</table>
<script language=javascript>
document.f.q.focus();
</script></body></html>


