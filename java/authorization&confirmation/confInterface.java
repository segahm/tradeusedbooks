package server;
public interface confInterface{
	public static final String HEADERTITLE = "headerTitle";
	//input fields
	public static final String USERFIELD = "username";
	public static final String PASSFIELD = "password";
	//used both as input name and session attribute name
	public static final String SESSIONFIELDATTR = "tempID";
	public static final String AGREEFIELD = "agree";
	//options that this servlet supports in its path
	public static final String OPTIONSATTR = "options";
	public static final String OPTION1 = "user";
	public static final String ERRORMESSAGEATTR = "errormessage";
	public static final String ERRORMESSAGE1ATTR = "message1";
	public static final String ERRORMESSAGE2ATTR = "message2";	
	public static final String PATHCONFIRMUSER 
		= "/jsp/confirmation/confirmUser.jsp";
}