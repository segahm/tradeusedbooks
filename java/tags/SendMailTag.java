package server;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import java.io.*;
/*
 *takes email address and subject as set parameters
 *and message as the body inbetween the tags;
 *and then sends mail using these parameters
 *mail is send in its own thread(no result is return)
 */
public class SendMailTag extends BodyTagSupport {
	private String email = null;
	private String subject = null;
	private boolean sendAsHTML = true;
	public void setEmailAddress(String v) {
		email = v;
	}
	public void setSubject(String v) {
		subject = v;
	}
	public void setSendAsHTML(String html){
		if (html==null || !html.equals("true")){
			sendAsHTML = false;
		}
	}
	public int doAfterBody(){
		BodyContent body = getBodyContent();
		SendMail mail = new SendMail(email,sendAsHTML);
		//send (message,subject)
		mail.send(body.getString(),subject);
		return(SKIP_BODY);
	}
}