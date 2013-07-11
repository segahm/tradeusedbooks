package server;
public interface URLInterface {
	//URLS:
	//
	public static final String URLFEEDBACK
		= "/feedback";
	//search page
	public static final String URLSEARCH
		= "/query";
	//book page
	public static final String URLBOOK
		= "/book/";
	public static final String URLBUY
		= "/buy";
	public static final String URLQUESTION
		= "/question";
	//account pages
	public static final String URLACCOUNTUPDATE 
		= "/account/update";
	public static final String URLACCOUNTHISTORY 
		= "/account/history";
	public static final String URLACCOUNTBOOKS 
		= "/account/books";
	public static final String URLACCOUNTBIDS
		= "/account/bids";
	public static final String URLACCOUNTWANTED 
		= "/account/wanted";
	public static final String URLACCOUNTFEEDBACK 
		= "/account/feedback";
	public static final String URLACCOUNTREPOST
		= "/account/repost";
	//authorization pages
	public static final String URLAUTHSIGNIN
		= "/auth/signin";
	public static final String URLAUTHREGISTER
		= "/auth/register";
	public static final String URLAUTHSIGNOUT
		= "/auth/signout";
	public static final String URLAUTHFORGOT
		= "/auth/forgot";
	//bookstore authentication pages
	public static final String URLBOOKSTORESIGNIN
		= "/bookstores/auth/signin";
	public static final String URLBOOKSTOREREGISTER
		= "/bookstores/auth/register";
	public static final String URLBOOKSTORESIGNOUT
		= "/bookstores/auth/signout";
	public static final String URLBOOKSTOREFORGOT
		= "/bookstores/auth/forgot";
	public static final String URLBOOKSTOREADMINCONFIRM
		= "/bookstores/auth/adminconfirm";
	//bookstore account pages
	public static final String URL_BOOKSTORE_ACCOUNT_UPDATE
		= "/bookstores/account/update";
	public static final String URL_BOOKSTORE_ACCOUNT_BOOKS
		= "/bookstores/account/books";
	public static final String URL_BOOKSTORE_ACCOUNT_POST
		= "/bookstores/account/post";
	//bookstore book/store page
	public static final String URL_BOOKSTORE_BOOK 
		= "/bookstores/book/";
	//confirm pages
	public static final String URLCONFIRMUSER
		= "/confirm/user";
	public static final String URLPBOOKS
		= "/pbooks";
	public static final String URLPWANTED
		= "/pwanted";
	//general
	public static final String URLTERMS
		= "/doc/terms";
	public static final String URLPRIVACY
		= "/doc/terms#privacy";
	public static final String URLCONDGUID
		= "/doc/condition";
	public static final String URLHELPPAGE
		= "/doc/help";
	//static errror pages
	public static final String URLAUTHORIZATIONERROR
		= "/errorpages/authorization_error.html";
	public static final String URLSERVERERROR
		= "/errorpages/server_error.html";
	//PATHS:
	//paths are relative to application's root directory
	//user error page
	public static final String PATHUSERERROR
		= "/jsp/userError.jsp";
	public static final String PATHUSERERROR2
		= "/jsp/userErrorInclude.jsp";
}
