package server;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Authenticator;
import java.util.*;
import java.io.*;
import javax.activation.*;


/**
Some SMTP servers require a username and password 
authentication before you can use their Server 
for Sending mail. This is most common with couple
of ISP's who provide SMTP Address to Send Mail.
This Program is designed to do SMTP Authentication
(User and Password verification).

To use this program, change values for the following three constants,

    SMTP_HOST_NAME -- Has your SMTP Host Name
    SMTP_AUTH_USER -- Has your SMTP Authentication UserName
    SMTP_AUTH_PWD  -- Has your SMTP Authentication Password

Next change values for fields

  emailMsgTxt  -- Message Text for the Email
  emailSubjectTxt  -- Subject for email
  emailFromAddress -- Email Address whose name will appears as "from" address

Next change value for "emailList".
  This String array has List of all Email Addresses to Email Email needs to be sent to.

Methods:
	send()
*/

public class SendMail implements StringInterface
{
	private final String SMTP_HOST_NAME = "slugtrade.com";
  	private final String SMTP_AUTH_USER = "slugt2";
  	private final String SMTP_AUTH_PWD  = "blosomkaint";

	private String emailSubjectTxt;
	private String emailMsgTxt;
	//let's send know wherever it needs to wait for thread to finish
	private boolean wait = false;
	private static final String emailFromAddress = "info@tradeusedbooks.com";
	// Add List of Email address to who email needs to be sent to
	//next 2 are used to generate code
	private String[] emailList = null;
	private boolean html = true;
	public SendMail(String to, boolean html){
		this.html = html;
		// Add Email address to who email needs to be sent to
		emailList = new String[1];
		emailList[0] = to;
	}
	public SendMail(String[] to, boolean html){
		this.html = html;
		// Add List of Email address to who email needs to be sent to
		emailList = to;
	}
	public void setWait(boolean w){
		wait = w;
	} //end setWait
	//sends m (message) and subj (subject)
	public boolean send(String m,String subj)
	{
		emailMsgTxt = m;
		emailSubjectTxt = subj;
		postMail p = new postMail( emailList, emailSubjectTxt, emailMsgTxt);
		p.start();
		if (wait){
			while(p.isAlive()){
			}
			return p.getResult();
		}else{
			return true;
		}
	} //end send
  	class postMail extends Thread{
  		String recipients[];
  		String subject;
  		String message;
  		boolean result;
  		postMail( String r[], String s, String m){
  			recipients = r;
  			subject = s;
  			message = m;
  		}
  		public void run(){
  			PrintStream stream = null;
  			try{
  				boolean debug = true;
			
			    //Set the host smtp address
			    Properties props = new Properties();
			    props.put("mail.smtp.host", SMTP_HOST_NAME);
			    props.put("mail.smtp.auth", "true");
		
			    Authenticator auth = new SMTPAuthenticator();
			    Session session = Session.getDefaultInstance(props, auth);
			    session.setDebug(debug);
			    stream = new PrintStream(new FileOutputStream(logPath+"mailLog.txt"));
			    session.setDebugOut(stream);
			
			    // create a message
			    Message msg = new MimeMessage(session);
			
	   			// set the from and to address
	    		InternetAddress addressFrom = new InternetAddress(emailFromAddress);
	    		msg.setFrom(addressFrom);
			
    			InternetAddress[] addressTo = new InternetAddress[recipients.length];
    			for (int i = 0; i < recipients.length; i++){
    			    addressTo[i] = new InternetAddress(recipients[i].toLowerCase());
    			}
    			msg.setRecipients(Message.RecipientType.TO, addressTo);
			
		
			    // Setting the Subject and Content Type
			    msg.setSubject(subject);
			    if (html){
    				msg.setContent(message,"text/html");
    			}else{
    				msg.setContent(message,"text/plain");
    			}
    			Transport.send(msg);
    			result = true;
    		}catch (Exception e){
    			log.writeException(e.getMessage());
    			result = false;
    		}
    		finally{
    			stream.close();
    		}
    	}
    	public boolean getResult(){
    		return result;
    	}
 	} //end postMail
/**
* SimpleAuthenticator is used to do simple authentication
* when the SMTP server requires it.
*/
	private class SMTPAuthenticator extends Authenticator
	{
	
	    public PasswordAuthentication getPasswordAuthentication()
	    {
	    	   String username = SMTP_AUTH_USER;
	    	   String password = SMTP_AUTH_PWD;
	    	   return new PasswordAuthentication(username, password);
	    }
	}
}


