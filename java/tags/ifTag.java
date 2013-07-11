package server;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import java.io.*;
/*
 *a class that takes two parameters as selected and unselected
 *with the word <void> specifying where to insert the word
 *in body
 */
public class ifTag extends BodyTagSupport{
	private boolean test;
	public void setTest(boolean s) {
		test = s;
	}
	public int doAfterBody() throws JspException{
		BodyContent body = getBodyContent();
		JspWriter out = body.getEnclosingWriter();
		try{
			if (test == true){
				out.println(body.getString());
			}
		}catch (IOException e){
		}
		return(SKIP_BODY);
	}
}