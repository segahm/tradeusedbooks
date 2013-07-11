package server;
public interface accoInterface{
	public static final String HEADERTITLE = "headerTitle";
	//fields for account update
	public static final String UPDATE_NEWPASS1 = "newpwd1";
	public static final String UPDATE_NEWPASS2 = "newpwd2";
	public static final String UPDATE_OLDPASS = "oldpass";
	public static final String UPDATE_NEWUSERNAME = "newusername";
	public static final String UPDATE_NEWEMAIL = "newemail";
	public static final String UPDATE_ERRORMESSAGE = "errormessage";
	public static final String UPDATE_EMAILATTR = "emailattr";
	public static final String UPDATE_USERATTR = "userattr";
	//field names for books page
	public static final String BOOKS_BOOKID = "id";
	public static final String BOOKS_BIDS_USERID = "userid";
	public static final String BOOKS_SELLINGTABLE_ATTR
		= "books_sellingtable_aatr";
	public static final String BOOKS_SOLDTABLE_ATTR
		= "books_soldtable_attr";
	public static final String BOOKS_BOUGHTTABLE_ATTR
		= "books_boughttable_attr";
	public static final String BOOKS_HISTORYTABLE_ATTR
		= "books_historytable_attr";
	public static final String BOOKS_WANTEDTABLE_ATTR
		= "books_wantedtable_attr";
	//feedback text field
	public static final String FEEDBACK_TEXT_FIELD = "feedback";
	//i.e. positibe|neutral|negative but in int
	public static final String FEEDBACK_RATE_FIELD = "rate";
	//hidden temp id
	public static final String SESSIONFIELDATTR = "tempID";
	//page attributes
	public static final String OPTIONSATTR = "options";
	public static final String OPTION1 = "update";
	public static final String OPTION2 = "books";
	public static final String OPTION3 = "bids";
	public static final String OPTION4 = "history";
	public static final String OPTION5 = "wanted";
	public static final String OPTION6 = "feedback";
	public static final String OPTION7 = "repost";
	
	public static final String PAGEATTR = "pagenumb";
	public static final String PAGE1 = "page1";
	public static final String PAGE2 = "page2";
	//jsp paths
	public static final String PATHACCO_TOP 
		= "/jsp/account/accountTop.jsp";
	public static final String PATHACCO_BOTTOM
		= "/jsp/account/accountBottom.jsp";
	public static final String PATHACCO_BOOKS
		= "/jsp/account/acco_books.jsp";
	public static final String PATHACCO_BOOKS_EDIT
		= "/jsp/account/acco_books_edit.jsp";
	public static final String PATHACCO_BIDS
		= "/jsp/account/acco_bids.jsp";
	public static final String PATHACCO_HISTORY
		= "/jsp/account/acco_history.jsp";
	public static final String PATHACCO_HISTORY_REPOST
		= "/jsp/account/acco_history_repost.jsp";
	public static final String PATHACCO_UPDATE
		= "/jsp/account/acco_update.jsp";
	public static final String PATHACCO_WANTED
		= "/jsp/account/acco_wanted.jsp";
	
}
