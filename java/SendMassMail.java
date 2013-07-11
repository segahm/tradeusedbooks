package server;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Authenticator;
import java.util.*;
import java.io.*;
import javax.activation.*;
/**
 *SendMassMail is a program that creates a session
 *with a smtp server upon instantiation and then
 *sends individual messages through send
 */
public class SendMassMail implements StringInterface
{
	private final String SMTP_HOST_NAME = "mail.tradeusedbooks.com";
  	private final String SMTP_AUTH_USER = "server@tradeusedbooks.com";
  	private final String SMTP_AUTH_PWD  = "blosomkaint";
	//let's send know wherever it needs to wait for thread to finish
	private static final String emailFromAddress = "info@tradeusedbooks.com";
	// Add List of Email address to who email needs to be sent to
	private PrintStream stream;
	private InternetAddress addressFrom;
	private Session session;
	/**instantiate a session with smtp server*/
	public SendMassMail() throws Exception{
		try
  		{
  			boolean debug = false;
			//Set the host smtp address
			Properties props = new Properties();
			props.put("mail.smtp.host", SMTP_HOST_NAME);
			props.put("mail.smtp.auth", "true");
		
			Authenticator auth = new SMTPAuthenticator();
			session = Session.getDefaultInstance(props, auth);

			addressFrom = new InternetAddress(emailFromAddress);
		 	session.setDebug(debug);
			stream = new PrintStream(new FileOutputStream(logPath+"mailLog.txt"));
			session.setDebugOut(stream);
    	}catch (Exception e){
    		e.printStackTrace();
    		log.writeException(e.getMessage());
    		throw e;
    	}
	}
	/**sends m (message) and subj (subject) to the recipient*/
	public void send(String recipient,String emailMsgTxt,
		String emailSubjectTxt) throws Exception{
		try{
			// create a message
			Message msg = new MimeMessage(session);
			// set the from and to address
	    	msg.setFrom(addressFrom);
    		InternetAddress[] addressTo = new InternetAddress[1];
    		addressTo[0] = new InternetAddress(recipient.toLowerCase());
    		msg.setRecipients(Message.RecipientType.TO, addressTo);
			// Setting the Subject and Content Type
			msg.setSubject(emailSubjectTxt);
    		msg.setContent(emailMsgTxt,"text/plain");
    		Transport.send(msg);
    	}catch(Exception e){
    		e.printStackTrace();
    		log.writeException(e.getMessage());
    		throw e;
    	}
	} //end send
	/**closes the stream */
 	public void close(){
 		stream.close();
 		addressFrom = null;
		session = null;
 	} //end close
 	/**provides authentication for mail session */
 	private class SMTPAuthenticator 
 		extends Authenticator{
		public PasswordAuthentication getPasswordAuthentication()
	    {
	    	   String username = SMTP_AUTH_USER;
	    	   String password = SMTP_AUTH_PWD;
	    	   return new PasswordAuthentication(username, password);
	    }
	}
}


