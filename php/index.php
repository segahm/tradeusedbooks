<html>

<head>
<meta http-equiv='Content-Language' content='en-us'>
<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>
<META NAME="ROBOT" CONTENT="all">
<meta name="description" content="Buy and Sell your books on tradeusedbooks.com - a new college textbook exchange website">
<meta name="keywords" content="books,trade,book,exchange,book exchange,sell,author,textbooks,law books,law,print,out of print,days,adobe,the book exchange,sale,sold,rare,new and userd,bookstore,search,old and rare">
<?include_once("html/meta.html");?>
<link rel="stylesheet" type="text/css" href="/styles/main.css">
<title>Trade Used Books</title>
</head>

<body>
	<table width="100%" height="90%" border="0" cellspacing="0" cellpadding="0" align=center>
		<!-- main picture goes here -->
		<tr>
			<td align=center>
			<img src="/myImages/header.gif" alt="trade used books.com">
			</td>
		</tr>
		<!-- start main content -->
		<!-- search box and buttons go here -->
		<tr>
			<td>
			<table width="100%">
				<tr>
					<td align=center>
<?
//the following format will be used to pass header page index.php?path=$HEADER[n]
//the following are header names accepted by application
$HEADER1 = 'm';
$HEADER2 = 'w';
$HEADER3 = 'b';
$set = "";
//search field is passed using /query?q="some value"
?>
<?//if m or not (b and w)
if (($_GET['path'] == $HEADER1) || (($_GET['path'] != $HEADER2) && ($_GET['path'] != $HEADER3) )){
?>
	<font class="headSelect">market place</font>
	<?$set = $HEADER1;?>
<?}else{?>
	<a class="headNotSelect" href="/p/<?echo "$HEADER1"?>">market place</a>
<?}?>
<?if ($_GET['path'] == $HEADER2){?>
	<font class="headSelect">wanted</font>
	<?$set = $HEADER2;?>
<?}else{?>
	<a class="headNotSelect" href="/p/<?echo "$HEADER2"?>">wanted</a>
<?}?>
<?if ($_GET['path'] == $HEADER3){?>
	<font class="headSelect">bookstores</font>
	<?$set = $HEADER3;?>
<?}else{?>
	<a class="headNotSelect" href="/p/<?echo "$HEADER3"?>">bookstores</a>
<?}?>
					</td>
				</tr>
				<tr>
					<td>
					<form name=f action="/query" method=get>
					<table width="100%">
						<tr>
							<td width="25%"></td>
							<td align=center>
								<input type=text maxLength=256 size=55 value="" name=q><br>
								<input type=hidden value=<?echo $set?> name=h>
								<input type=submit value="Search Used Books">
							</td>
							<td align=left valign=top width="25%">
<!--								<a class="genLinks" href="/advSearch">Advanced Search</a><br>-->
							</td>
						</tr>
					</table>
					</form>
					</td>
				</tr>
				<tr>
					<td align=center>
					<a class="refLinks" href="/pbooks">Post to Books</a>
					&#183;
					<a class="refLinks" href="/pwanted">Post to Wanted</a>
					&#183;
					<a class="refLinks" href="/account/books">My Account</a>
                    &#183;
                    <a class="refLinks" href="/bookstores/account/update">Bookstore Owners</a>
					</td>
				</tr>
			</table>
			</td>
		</tr>
        	<?include_once("html/footer.html");?>
		<!-- end main content -->
	</table>
<script>
document.f.q.focus();
</script>
</body>
</html>