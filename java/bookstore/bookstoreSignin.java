package server;
import javax.servlet.http.*;
import javax.servlet.*;
import java.sql.*;

public class bookstoreSignin extends ServletParent 
	implements URLInterface,Tables{
	//jsp paths:
	private final String SIGNIN_PATH = "/jsp/bookstore_auth/bookstore_signin.jsp";
	private final String REGISTRATION_PATH = "/jsp/bookstore_auth/bookstore_register.jsp";
	private final String FORGOT_PATH = "/jsp/bookstore_auth/bookstore_forgot.jsp";
	private final String ADMINCONFIRM_PATH = "/jsp/bookstore_auth/bookstore_adminconfirm.jsp";
	private final String MAILNEWBOOKSTORE_PATH = "/jsp/mail/newbookstore.jsp";
	//page is used by some jsps to determine which page to show
	public static final String PAGE = "page";
	//options supported in url path
	public static final String OPTION1 = "signin";
	public static final String OPTION2 = "register"; 
	public static final String OPTION3 = "forgot";
	public static final String OPTION4 = "signout";
	public static final String OPTION5 = "adminconfirm";
	//some attributes
	public static final String QUERYSTRING_ATTR = "querystring";
	public static final String ERRORMESSAGE_ATTR = "errormessage";
	//form parameters - can be used for different pages
	public static final String AGREEFIELD = "agree";
	public static final String EMAIL_FIELD = "user";
	public static final String PASSWORD_FIELD = "pass";
	public static final String HIDDENID_FIELD = "id";
	public static final String CONTACT_FIELD = "contactinfo";
	public static final String ADDRESS_FIELD = "address";
	public static final String WEBSITE_FIELD = "webpage";
	public static final String MOREINFO_FIELD = "moreinfo";
	public static final String FIRSTNAME_FIELD = "fname";
	public static final String LASTNAME_FIELD = "lname";
	public static final String BOOKSTORE_FIELD = "bookstore";
	public void service(HttpServletRequest request,
                       HttpServletResponse response)
                throws ServletException{
    	try{
			//make sure url is correct
			if (request.getPathInfo()==null
				|| !request.getPathInfo()
				.matches("/("+OPTION1+"|"+OPTION2+"|"
				+OPTION3+"|"+OPTION4+"|"+OPTION5+").*")
				){
				response.sendRedirect(response
				.encodeRedirectURL("/"));
			}else{
				if (request.getPathInfo()
					.matches("/"+OPTION1+".*")){
					signIn(request,response);
				}
				if (request.getPathInfo()
					.matches("/"+OPTION2+".*")){
					register(request,response);
				}
				if (request.getPathInfo()
					.matches("/"+OPTION3+".*")){
					forgot(request,response);
				}
				if (request.getPathInfo()
						.matches("/"+OPTION4+".*")){
					/*invalidate session and redirect
					 *back to where we came from
					 */
					try{
						request.getSession().invalidate();
					}catch(IllegalStateException ignore){
					}
					if (request.getQueryString() != null){
						response.sendRedirect(
							request.getQueryString());
					}else{
						response.sendRedirect("/");
					}
				}
				if (request.getPathInfo()
					.matches("/"+OPTION5+".*")){
					/*
					 *administrative function
					 *retrieves a bookstore based on
					 *given bookstore id
					 *and allowes to edit the data
					 */
					adminconfirm(request,response);
				}
			} //end else
		}catch (Exception e){
			//actual stack trace is expected to be written
			//by exception thrower 
			throw new ServletException(e);
		}                 	
	} //end service
	/*
	 *signin
	 */
	private void signIn(HttpServletRequest request,
		HttpServletResponse response) throws Exception{
		HttpSession session = request.getSession();
		ParameterParser parameter = new ParameterParser(request);
		//username field - where username is an email
		String username = parameter.getString(EMAIL_FIELD,null);
		//password field
		String password = parameter.getString(PASSWORD_FIELD,null);
		//hiddenid - is a security check
		String hiddenID = parameter.getString(HIDDENID_FIELD,null);
		//error message to be displayed
		String error = null;
		boolean defaultPage = false;
		//default if one of the inputs is null
		if (username == null 
			|| password == null 
			|| hiddenID == null
			|| !hiddenID.equals(session
				.getAttribute(HIDDENID_FIELD))){
			defaultPage = true;
		}else{
			ConnectionPool conPool = getConnectionPool();
			Connection con = conPool.getConnection();
			/*
			 *try/catch to make sure we
			 *recyle connection
			 */
			try{
				BookstoreUser user = confirmUser(username,password,con);
				if (user != null){
					/*creating a new
					 *session with timeout
					 *in seconds
					 */
					session.setMaxInactiveInterval(
						StringInterface.MAXINACTIVETIME);
					session.setAttribute(
						StringInterface.BOOKSTOREUSERATTR,
						user);
				}else{
					defaultPage = true;
					error = "bad email/password combination";
				}
			}catch (Exception e){
				throw e;
			}finally{
				//finished with database - recycle
				conPool.free(con);
				con = null;
			}
		}
		if (defaultPage){
			//create a hidden session id
			hiddenID = sqlUtils.generate(20);
			//adding hiddenID to session for future check
			session.setAttribute(HIDDENID_FIELD,hiddenID);
			request.setAttribute(HIDDENID_FIELD,hiddenID);
			//setting error messages(even if null)
			request.setAttribute(ERRORMESSAGE_ATTR,error);
			request.setAttribute(QUERYSTRING_ATTR,request.getQueryString());
			RequestDispatcher rd = getServletContext()
				.getRequestDispatcher(SIGNIN_PATH);
			rd.forward(request,response);
		}else{
			//getting rid of form security check
			session.removeAttribute(HIDDENID_FIELD);
			if (request.getQueryString()!=null){
				//redirect to where we came from
				response.sendRedirect(
					response.encodeRedirectURL(
						request.getQueryString()));
			}else{
				response.sendRedirect(
					response.encodeRedirectURL("/"));
			}
		}
	} //end signIn
	/*
	 *register
	 */
	private void register(HttpServletRequest request,
		HttpServletResponse response) throws Exception{
		HttpSession session = request.getSession();
		//get parameters
		ParameterParser parameter = new ParameterParser(request);
		String email = parameter.getString(EMAIL_FIELD,null);
		String hiddenID = parameter.getString(HIDDENID_FIELD,null);
		String contact = parameter.getString(CONTACT_FIELD,null);
		String address = parameter.getString(ADDRESS_FIELD,null);
		String website = parameter.getString(WEBSITE_FIELD,null);
		String moreinfo = parameter.getString(MOREINFO_FIELD,null);
		String firstname = parameter.getString(FIRSTNAME_FIELD,null);
		String lastname = parameter.getString(LASTNAME_FIELD,null);
		String bookstore = parameter.getString(BOOKSTORE_FIELD,null);
		String agreeChecked = parameter.getString(AGREEFIELD,null);
		//error message about bad fields
		String error = null;
		boolean defaultPage = false;
		if (email == null || hiddenID == null
			|| contact == null || address == null
			|| !hiddenID.equals(session.getAttribute(HIDDENID_FIELD))
			|| firstname == null || lastname == null
			|| bookstore == null){
			defaultPage = true;
		}else if(agreeChecked==null){
			response.sendRedirect(response
					.encodeRedirectURL(URLTERMS));
		}else{
			try{
				//check format
				if (!tools.isGoodEmail(email)){
					throw new FormatException("bad email format");
				}
				if (!tools.allASCII(contact) 
					|| !tools.allASCII(address)
					|| (website!=null 
					&& !tools.allASCII(website))
					|| (moreinfo!=null 
					&& !tools.allASCII(moreinfo)
					|| !tools.allASCII(firstname)
					|| !tools.allASCII(lastname)
					|| !tools.allASCII(bookstore))
					){
					throw new FormatException("one of the fields contains characters other than english");
				}
			}catch(FormatException e){
				defaultPage = true;
				error = e.getMessage();
			}
		}
		if (defaultPage){
			//create a hidden session id
			hiddenID = sqlUtils.generate(20);
			//adding hiddenID to session for future check
			session.setAttribute(HIDDENID_FIELD,hiddenID);
			request.setAttribute(HIDDENID_FIELD,hiddenID);
			//setting parameters
			request.setAttribute(ERRORMESSAGE_ATTR,error);
			request.setAttribute(QUERYSTRING_ATTR,request.getQueryString());
			request.setAttribute(EMAIL_FIELD,email);
			request.setAttribute(CONTACT_FIELD,contact);
			request.setAttribute(ADDRESS_FIELD,address);
			request.setAttribute(WEBSITE_FIELD,website);
			request.setAttribute(MOREINFO_FIELD,moreinfo);
			request.setAttribute(FIRSTNAME_FIELD,firstname);
			request.setAttribute(LASTNAME_FIELD,lastname);
			request.setAttribute(BOOKSTORE_FIELD,bookstore);
			//forwarding request to default jsp
			request.setAttribute(PAGE,"1");
			RequestDispatcher rd = getServletContext()
				.getRequestDispatcher(REGISTRATION_PATH);
			rd.forward(request,response);
		}else if(agreeChecked!=null){
			//getting rid of form security check
			session.removeAttribute(HIDDENID_FIELD);
			//adding book to temp table
			ConnectionPool conPool = getConnectionPool();
			Connection con = conPool.getConnection();
			try{
				/*
				 *adding the book to the temporary 
				 *bookstore table
				 */
				Statement st = con.createStatement();
				String[] fields = new String[]{
					bookstoreTempTable.BOOKSTOREID,
					bookstoreTempTable.FIRSTNAME,
					bookstoreTempTable.LASTNAME,
					bookstoreTempTable.STORENAME,
					bookstoreTempTable.CONTACTINFO,
					bookstoreTempTable.DATE,
					bookstoreTempTable.ADDRESS,
					bookstoreTempTable.MOREINFO,
					bookstoreTempTable.WEBSITE,
					bookstoreTempTable.EMAIL
				};
				String bookstoreID = sqlUtils.generate(bookstoreInfoTable.BOOKSTOREID_LENGTH);
				String[] values = new String[]{
					bookstoreID,
					firstname,lastname,bookstore,contact,
					new DateObject().getDate(),address,
					moreinfo,website,email
				};
				String query = "INSERT INTO "+TABLEBOOKSTORETEMP
					+" ("
					+sqlUtils.sql_fields(fields)
					+") values ("
					+sqlUtils.sql_values(values)
					+")";
				if (st.executeUpdate(query)!=1){
					throw new SQLException("failed to insert temp bookstore");
				}
				//sending mail to admin
				SendMail mail = new SendMail("support@tradeusedbooks.com",true);
				String message = "confirm this bookstore "
				+"- <a href='http://tradeusedbooks.com"+URLBOOKSTOREADMINCONFIRM+"?"+bookstoreID+"'>http://tradeusedbooks.com"+URLBOOKSTOREADMINCONFIRM+"?"+bookstoreID+"</a>";
				mail.send(message,"administrtive - confirm this bookstore");
			}catch(Exception e){
				throw e;
			}finally{
				conPool.free(con);
				con = null;
			}
			//forwarding request to jsp
			request.setAttribute(PAGE,"2");
			RequestDispatcher rd = getServletContext()
				.getRequestDispatcher(REGISTRATION_PATH);
			rd.forward(request,response);
		}
	} //end register
	/*
	 *forgot
	 */
	private void forgot(HttpServletRequest request,
		HttpServletResponse response) throws Exception{
		ParameterParser parameter = new ParameterParser(request);

		String email = parameter.getString(EMAIL_FIELD,null);
		//message == null will NOT show message
		String message = null;
		bookstoreUsersTable user = null;
		if (email != null){
			user = new bookstoreUsersTable();
			try{
				user.setEmail(email);
			}catch(FormatException e){
				message = e.getMessage();
			}
		}
		if (email != null && user.getEmail() != null){
			ConnectionPool conPool = getConnectionPool();
			Connection con = conPool.getConnection();
			try{
				if (retrieveUser(email,con)){
					message = "your password was sent to "+ user.getEmail();
				}else{
					message = user.getEmail()+" wasn't found in our database";
				}
			}catch (Exception e){
				throw e;
			}finally{
				conPool.free(con);
				con = null;
			}
		}
		request.setAttribute(QUERYSTRING_ATTR,request.getQueryString());
		request.setAttribute(ERRORMESSAGE_ATTR,message);
		RequestDispatcher rd = getServletContext()
			.getRequestDispatcher(FORGOT_PATH);
		rd.forward(request,response);
	}
	private void adminconfirm(HttpServletRequest request,
		HttpServletResponse response) throws Exception{
		//set no cache
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Expires", "0");
		response.setHeader("Pragma", "no-cache");
		
		RequestDispatcher rd = null;
		//get all parameters
		String bookstoreID = request.getQueryString();
		ParameterParser parameter = new ParameterParser(request);
		String email = parameter.getString(EMAIL_FIELD,null);
		String contact = parameter.getString(CONTACT_FIELD,null);
		String address = parameter.getString(ADDRESS_FIELD,null);
		String website = parameter.getString(WEBSITE_FIELD,null);
		String moreinfo = parameter.getString(MOREINFO_FIELD,null);
		String firstname = parameter.getString(FIRSTNAME_FIELD,null);
		String lastname = parameter.getString(LASTNAME_FIELD,null);
		String bookstore = parameter.getString(BOOKSTORE_FIELD,null);
		String error = null;
		//eliminating some of the bad requests
		if (bookstoreID!=null 
			&& bookstoreID.length() 
			== 
			bookstoreInfoTable.BOOKSTOREID_LENGTH){
			
			ConnectionPool conPool = getConnectionPool();
			Connection con = conPool.getConnection();
			try{
				Statement st = con.createStatement();
				//if first time accessing this page
				if (email == null
					|| contact == null || address == null
					|| firstname == null || lastname == null
					|| bookstore == null){
					//show default page
					request.setAttribute(PAGE,"1");
					//get the bookstore info based on its id
					ResultSet rs = st.executeQuery(
						"SELECT * FROM "
						+TABLEBOOKSTORETEMP
						+" WHERE "
						+bookstoreTempTable.BOOKSTOREID
						+" = '"
						+bookstoreID+"' LIMIT 1");
					if (rs.next()){
						//if the bookstore was found
						request.setAttribute(QUERYSTRING_ATTR,request.getQueryString());
						request.setAttribute(EMAIL_FIELD,
							rs.getString(bookstoreTempTable.EMAIL));
						request.setAttribute(CONTACT_FIELD,
							rs.getString(bookstoreTempTable.CONTACTINFO));
						request.setAttribute(ADDRESS_FIELD,
							rs.getString(bookstoreTempTable.ADDRESS));
						request.setAttribute(WEBSITE_FIELD,
							rs.getString(bookstoreTempTable.WEBSITE));
						request.setAttribute(MOREINFO_FIELD,
							rs.getString(bookstoreTempTable.MOREINFO));
						request.setAttribute(FIRSTNAME_FIELD,
							rs.getString(bookstoreTempTable.FIRSTNAME));
						request.setAttribute(LASTNAME_FIELD,
							rs.getString(bookstoreTempTable.LASTNAME));
						request.setAttribute(BOOKSTORE_FIELD,
							rs.getString(bookstoreTempTable.STORENAME));
					}else{
						throw new SQLException("bookstore wasn't found");
					}
				}else{
					request.setAttribute(PAGE,"2");
					//1st delete this record from temp table
					String query =
						"DELETE FROM "
						+TABLEBOOKSTORETEMP
						+" WHERE "
						+bookstoreTempTable.BOOKSTOREID
						+" = '"+bookstoreID+"' LIMIT 1";
					if (st.executeUpdate(query) != 1){
						throw new SQLException("bookstore couldn't be deleted - wasn't found");
					}
					//2nd adding the book to the info bookstore table
					String[] fields = new String[]{
						bookstoreInfoTable.BOOKSTOREID,
						bookstoreInfoTable.FIRSTNAME,
						bookstoreInfoTable.LASTNAME,
						bookstoreInfoTable.STORENAME,
						bookstoreInfoTable.CONTACTINFO,
						bookstoreInfoTable.LASTUPDATED,
						bookstoreInfoTable.ADDRESS,
						bookstoreInfoTable.MOREINFO,
						bookstoreInfoTable.WEBSITE
					};
					String[] values = new String[]{
						bookstoreID,firstname,lastname,bookstore,
						contact,new DateObject().getDate(),address,
						moreinfo,website
					};
					query = "INSERT INTO "+TABLEBOOKSTOREINFO
						+" ("
						+sqlUtils.sql_fields(fields)
						+") values ("
						+sqlUtils.sql_values(values)
						+")";
					if (st.executeUpdate(query)!=1){
						throw new SQLException("failed to insert to table bookstore");
					}
					//3rd adding the book to the bookstore user table
					fields = new String[]{
						bookstoreUsersTable.EMAIL,
						bookstoreUsersTable.ID,
						bookstoreUsersTable.PASSWORD,
						bookstoreUsersTable.EPASS,
						bookstoreUsersTable.DATE
					};
					String password = sqlUtils.generate(bookstoreUsersTable.PASSWORD_MAX);
					values = new String[]{
						email,bookstoreID,password,
						userUtils.getEncrypted(password),
						new DateObject().getDate()
					};
					query = "INSERT INTO "+TABLEBOOKSTOREUSERS
						+" ("
						+sqlUtils.sql_fields(fields)
						+") values ("
						+sqlUtils.sql_values(values)
						+")";
					if (st.executeUpdate(query)!=1){
						throw new SQLException("failed to insert to users table bookstore");
					}
					request.setAttribute(mailInterface.USERATTR,firstname);
					request.setAttribute(mailInterface.PASSATTR,password);
					request.setAttribute(mailInterface.EMAILATTR,email);					
					//sending mail to the bookstore owner
					rd = getServletContext()
						.getRequestDispatcher(MAILNEWBOOKSTORE_PATH);
					rd.include(request,response);
				}				
				rd = getServletContext()
					.getRequestDispatcher(ADMINCONFIRM_PATH);
				rd.include(request,response);
			}catch(Exception e){
				throw e;
			}finally{
				conPool.free(con);
				con = null;
			}
		}

	} //end adminConfirm
	/*
	 *sends password to the specified email
	 *returns: true if email found,false if email not found 
	 */
	private boolean retrieveUser(String email,
		Connection con) throws SQLException
	{
		try
		{
			Statement st = con.createStatement();
			String query = "SELECT "
				+TABLEBOOKSTOREINFO+"."+bookstoreInfoTable.FIRSTNAME+","
				+TABLEBOOKSTOREUSERS+"."+bookstoreUsersTable.PASSWORD
				+" FROM "
					+TABLEBOOKSTOREUSERS+","
					+TABLEBOOKSTOREINFO
				+" WHERE "
					+TABLEBOOKSTOREUSERS+"."+bookstoreUsersTable.EMAIL
				+" = '"+email+"'"
				+" AND "
				+TABLEBOOKSTOREUSERS+"."+bookstoreUsersTable.ID
				+" = "
				+TABLEBOOKSTOREINFO+"."+bookstoreInfoTable.BOOKSTOREID
				+" LIMIT 1";
				ResultSet rs = st.executeQuery(query);
			//if nothing found
			if (!rs.next()){
				return false;
			}
			String password = rs.getString(TABLEBOOKSTOREUSERS+"."+bookstoreUsersTable.PASSWORD);
			String firstname = rs.getString(TABLEBOOKSTOREINFO+"."+bookstoreInfoTable.FIRSTNAME);
			String subject = "password retrieval service requested";
			//writing message
			String message = "<html><body>"
				+"<p>Dear "+firstname+",</p>"
				+"<p>You received this email because you recently requested your password to be "
				+"send to you.</p>"
				+"<p>your PASSWORD is: "+password+"</p>"
				+"<p>Again, thank you for using tradeusedbooks.com!</p>"
				+"<p>tradeusedbooks.com team</p>"
				+"<p><font size='2'>If you think you received this email by mistake (meaning you never visited <a href='http://www.tradeusedbooks.com'>http://www.tradeusedbooks.com</a> "
				+"and never signed up for any of our services) please email us at: "
				+"<a href='mailto:support@tradeusedbooks.com?subject=remove mistake'>support@tradeusedbooks.com</a>.</font></p>"
				+"</body></html>";
			SendMail mail = new SendMail(email,true);
			mail.send(message,subject);
			return true;
		}
		catch (Exception e)
		{
			log.writeException(e.getMessage());			
			throw new SQLException(e.getMessage());
		}
	} //end retrieveUser
	/*
	 *confirm user
	 */
	private BookstoreUser confirmUser(String u,String p,
		Connection con) throws SQLException
	{
		try
		{
			//do not query empy values
			if (u == null || p == null 
			|| u.equals("") || p.equals("")){
				return null;
			}
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT "
				+bookstoreUsersTable.ID+","+bookstoreUsersTable.EPASS
				+" FROM "+TABLEBOOKSTOREUSERS+" WHERE "
				+bookstoreUsersTable.EMAIL+" = '"+u
				+"' AND "+bookstoreUsersTable.PASSWORD+" = '"+p+"' LIMIT 1");
        	//if found return
        	if (rs.next()){
        		BookstoreUser user = new BookstoreUser();
        		user.setID(rs.getString(bookstoreUsersTable.ID));
        		user.setEpass(rs.getString(bookstoreUsersTable.EPASS));
        		//update last login
        		updateDate(user.getID(),con);
        		return user;
        	}else{
        		return null;
        	}
		}
		catch (SQLException e)
		{
			log.writeException(e.getMessage());
			throw new SQLException(e.getMessage());
		}
	} //end confirmUser
	private void updateDate(String userID,
		Connection con) throws SQLException{
		try{
			Statement st = con.createStatement();
			DateObject date = new DateObject();
			//update date setting current date time
			int recordsUpdated = st.executeUpdate("UPDATE "
				+TABLEBOOKSTOREUSERS
				+" SET "
				+bookstoreUsersTable.DATE
				+" = '"+date.getDate()
				+"' WHERE "
				+bookstoreUsersTable.ID
				+" = '"+userID+"' LIMIT 1");
			if (recordsUpdated != 1)
				throw new SQLException("failed to update date for"+userID);
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end updateDate
}
