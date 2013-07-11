package server;

public interface bookInterface{
	//attribute names to be used in request for jsp
	public static final String ATTR_BOOKINFO
		= "bookinfoattr";
	public static final String ATTR_ISBNINFO
		= "isbninfoattr";
	public static final String ATTR_USERINFO
		= "attr_userinfo";
	public static final String FULLNAME_COLLEGE
		= "fullcollegename";
	public static final String SHORTNAME_COLLEGE
		= "shortcollegename";
	public static final String NAME_DEPARTMENT
		= "departmentname";
	public static final String NAME_COURSE
		= "coursename";
	public static final String NAME_TEACHER
		= "teachername";
	//form parameter names
	public static final String FIELD_ID
		= "id";
	public static final String FIELD_SELLERID
		= "sid";
	public static final String FIELD_HIDDENID
		= "genid";
	//book path
	public static final String PATHBOOK
		= "/jsp/book/book.jsp";
}
