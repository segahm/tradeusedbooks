package server;
/*
 *provides authentication related jsps/classes with name
 *convention
 */
public interface authInterface {
	public static final String HEADERTITLE = "headerTitle";
	//input fields
	public static final String USERFIELD = "username";
	public static final String PASSFIELD = "password";
	public static final String EMAILFIELD = "email";
	
	public static final String SESSIONFIELDATTR = "tempID";
	//error message names to be passed with request object
	public static final String ERRORMESSAGE1ATTR = "message1";
	public static final String ERRORMESSAGE2ATTR = "message2";	
	public static final String ERRORMESSAGE3ATTR = "message3";
	//attribute name that programs should use when setting
	//attributes
	public static final String OPTIONSATTR = "options";
	//options passed in url
	public static final String OPTION1 = "signin";
	public static final String OPTION2 = "register"; 
	public static final String OPTION3 = "forgot";
	public static final String OPTION4 = "signout";
	//specifies which page to show 1 or 2 or ...
	public static final String PAGEATTR = "pagevariant";
	public static final String PAGE1 = "1";
	public static final String PAGE2 = "2"; 
	//passes getQueryString in attribute
	public static final String QUERYATTR = "queryString";
	public static final String PATHAUTHORIZATION
		= "/jsp/auth/authorization.jsp"; 
}
