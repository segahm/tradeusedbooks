package server;
public interface postBInterface {
	/*
	 *uniqueFormattr - is id # that's hidden inside 
	 *the form, it also is used as a name for for a form
	 *object stored in session, thus allowing only the
	 *carrier of this id to access it
	 */
	public static final String UNIQUEFORMATTR = "ufi";
	/*
	 *PAGESTEP - is a hidden input that let's us know
	 *where we came from right away
	 */
	public static final String PAGESTEP = "pS";
	public static final String ERRORMESSAGE = "eM";
	//field names:
	public static final String TITLEFIELD = "title";
	public static final String ISBNFIELD = "isbn";
	public static final String AUTHORFIELD = "author";
	public static final String CONDITIONFIELD = "condition";
	public static final String PRICEFIELD = "price";
	public static final String COMMENTSFIELD = "comments";
	public static final String COLLEGEIDFIELD = "college1";
	public static final String COLLEGESHORT = "college2";
	public static final String COLLEGEFULL = "college3";
	public static final String DEPARTMENTIDFIELD = "department";
	public static final String DEPARTMENTCUSTOM = "department1";
	public static final String COURSEIDFIELD = "course";
	public static final String COURSECUSTOM = "course1";
	public static final String TEACHERIDFIELD = "teacher";
	public static final String TEACHERCUSTOM = "teacher1";
	//post books paths:
	public static final String PATHPBOOKS1
		= "/jsp/post/pbooks_step1.jsp";
	public static final String PATHPBOOKS2
		= "/jsp/post/pbooks_step2.jsp";
	public static final String PATHPBOOKS3
		= "/jsp/post/pbooks_step3.jsp";
	public static final String PATHPBOOKS4
		= "/jsp/post/pbooks_step4.jsp";
	public static final String PATHPBOOKS5
		= "/jsp/post/pbooks_step5.jsp";
	public static final String PATHPBOOKS6
		= "/jsp/post/pbooks_step6.jsp";
}
