<?php
function sendmsg($to, $from, $subject, $message) { 
   $uid = strtoupper(md5(uniqid(time()))); 
   $header = "From: info@tradeusedbooks.com\r\nReply-To: $from\r\n"; 
   $header .= "MIME-Version: 1.0\n"; 
   $header .= "Content-Type: text/plain; charset=\"ISO-8859-1\"\r\n"; 
   $header .= "Content-Transfer-Encoding: 8bit\r\n"; 
   $header .= "X-AntiAbuse: This header was added by tradeusedbooks.com to track abuse, please include it with any abuse report\n";
   $header .= "X-AntiAbuse: Sender's IP: " . $_SERVER['REMOTE_ADDR'] . ", Time: " . date("F j, Y, g:i a") . "\r\n";
   return mail($to, $subject, $message, $header);
} 
//security check
if (!isset($_POST['genid']) || empty($_POST['genid'])
		|| !isset($_SERVER['HTTP_REFERER']) || empty($_SERVER['HTTP_REFERER'])
		|| ($_SERVER['HTTP_REFERER'] != "http://www.tradeusedbooks.com/"
		&& $_SERVER['HTTP_REFERER'] != "http://tradeusedbooks.com/"
		&& $_SERVER['HTTP_REFERER'] != "http://www.slugtrade.com"
		&& $_SERVER['HTTP_REFERER'] != "http://slugtrade.com")){
	//redirect to home page
	header("Location: /");
}else if (!isset($_POST['from']) || !isset($_POST['to'])
			|| empty($_POST['from']) || empty($_POST['to'])
			|| !eregi('^[a-zA-Z ]+,[ ]*[a-zA-Z0-9_\-\.]+@[a-zA-Z0-9\-]+\.[a-zA-Z0-9\-\.]+$',trim($_POST['to']))
			|| !eregi('^[a-zA-Z ]+,[ ]*[a-zA-Z0-9_\-\.]+@[a-zA-Z0-9\-]+\.[a-zA-Z0-9\-\.]+$',trim($_POST['from']))){
	//if fields are not entered correctly
	header("Location: /");
}else{
	//sending mail
	$to_name = trim(ereg_replace("[[:space:]]*,.*","",$_POST['to']));
	$from_name = trim(ereg_replace("[[:space:]]*,.*","",$_POST['from']));
	$to_email = strtolower(trim(ereg_replace("[^,]+,[[:space:]]*","",$_POST['to'])));
	$from_email = strtolower(trim(ereg_replace("[^,]+,[[:space:]]*","",$_POST['from'])));
	$message = "Hey $to_name,\nI have been using a cool website called http://www.tradeusedbooks.com that allows you to trade textbooks with other college students directly. It connects you with other people at your college and makes the process of buying textbooks easy and fast. Get your books cheap - it's free!\n$from_name, $from_email\n\nPlease note that this message was sent to you from tradeusedbooks.com on behalf of the person mentioned above. This email was sent to you as invitation to visit tradeusedbooks.com and will not be repeated unless someone else enters your contact information again. Note that we do not store this information in any way and are commited to preventing abuse of this service. If you think that someone is abusing this service (i.e. sends repeated emails to your account through tradeusedbooks.com) please don't hesitate to report this to us at support@tradeusedbooks.com (please include all original email headers with your abuse report).";
	$subject = "this website will help you get textbooks cheap";
	$success = (bool)sendmsg($to_email, $from_email, $subject, $message); 
	$out = "send to $to_name, $to_email from $from_name, $from_email, ";
	$out .= $_SERVER['REMOTE_ADDR'] . ", success: $success \n";
	@ $fp = fopen("/home/slugt2/myLogs/invites.txt",'a');
	if ($fp){
		fwrite($fp,$out,strlen($out));
		fclose($fp);
	}
	//writing output
	?>
<html>

<head>
<meta http-equiv='Content-Language' content='en-us'>
<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>
<meta name="author" content="tradeusedbooks.com">
<meta name="distribution" content="IU">
<meta name="copyright" content="all rights reserved">
<link rel="stylesheet" type="text/css" href="/styles/main.css">
<title>Trade Used Books - Invitation</title>
</head>
<body>
<table width="640" border="0" cellspacing="0" cellpadding="0" align=left>
<!-- header begins -->

<tr valign=top>
<td>
<table width="100%" cellspacing="0" cellpadding="0" style="margin-bottom:10px;">
<tr height="70">
	<td width="200">
	<img src=/myImages/headerSmall.gif alt="Trade Used Books">
	</td>
</tr>
<tr valign=top height="20">
	<td style="border-left: 2px dotted #336699;border-right: 2px dotted #336699;" bgcolor="#336699">
	<font style="font-size: 16pt; margin-left:10px;" color="#FFFFFF">Invitation</font>
	</td>
</tr>
</table>
</td>
</tr>
<!-- header ends -->
<tr><td>
<p>Thank you for helping us spread the word about our site. Please come back to tradeusedbooks.com often and help us spread the word by telling all of your friends and classmates about us - after all, this will help you sell your textbooks and hopefully many more to come.</p>
<p>sincerely,<br>tradeusedbooks.com team</p>
<a href="/" class="refLinks">click here to return back to home page</a>
</td></tr>
<!-- footer begins -->
<tr valign=bottom style="padding-top:20px">
			<td width="100%"><center><font class="footer">
<a href="/" class="footer">home</a>
			&nbsp;|&nbsp;
<a href="/doc/support" class="footer">support</a>
			&nbsp;|&nbsp;
<a href="/doc/contact" class="footer">contact us</a>
			&nbsp;|&nbsp;
<a href="/doc/terms" class="footer">Terms of Service</a>
			&nbsp;|&nbsp;
<a href="/doc/help" class="footer">help</a>
</font><font style="font-size:10px">
<br>
			Copyright © 2005 Trade Used Books inc. All Rights Reserved.</font>
</center></td>
		</tr><!-- footer ends -->	
</table>
</body>
</html>
	<?php
}
?>