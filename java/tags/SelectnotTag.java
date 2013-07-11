package server;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import java.io.*;
/*
 *a class that takes two parameters as selected and unselected
 *with the word <void> specifying where to insert the word
 *in body
 */
public class SelectnotTag extends BodyTagSupport{
	private String selected;
	private String notSelected;
	private String separator;
	private String insert;
	private String insert2;
	public void setSelected(String v) {
		selected = v;
	}
	public void setNotSelected(String v) {
		notSelected = v;
	}
	public void setSeparator(String v){
		separator = v;
	}
	public void setInsert(String v){
		insert = v;
	}
	public void setInsert2(String v){
		insert2 = v;
	}
	public int doAfterBody() throws JspException{
		BodyContent body = getBodyContent();
		JspWriter out = body.getEnclosingWriter();
		String[] param = body.getString().trim().split(separator);
		if (separator.length() != 1){
			throw new JspException("bad separator");
		}
		try{
			if (insert2 == null){
				for (int i=0;i<param.length;i+=2){
					if (param[i+1].equals("true")){
						out.println(selected.replaceAll(insert,param[i]));
					}else{
						out.println(notSelected.replaceAll(insert,param[i]));
					}
				}
			}else{
				for (int i=0;i<param.length;i+=3){
					if (param[i+2].equals("true")){
						out.println(
							selected.replaceAll(
							insert,param[i]).replaceAll(
							insert2,param[i+1]));
					}else{
						out.println(
							notSelected.replaceAll(
							insert,param[i]).replaceAll(
							insert2,param[i+1]));
					}
				}
			}
		}catch (Exception e){
			log.writeException(e.getMessage());
		}
		return(SKIP_BODY);
	}
}