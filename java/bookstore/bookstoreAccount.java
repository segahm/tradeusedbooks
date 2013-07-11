package server;
import javax.servlet.http.*;
import javax.servlet.*;
import java.sql.*;

public class bookstoreAccount extends ServletParent 
	implements URLInterface,Tables{
	//allowed options
	public static final String OPTION1 = "update";
	public static final String OPTION2 = "books";
	public static final String OPTION3 = "post";
	//attributes
	public static final String HEADERATTR = "headerattr";
	public static final String OPTIONATTR = "optionattr";
	public static final String ERRORMESSAGE = "errormessage";
	public static final String HIDDENID = "id";
	public static final String BOOKSATTR = "bookattr";
	public static final String BOOKIDATTR = "bookidattr";
	public static final String PAGES = "pages";
	//account update field names for user change
	public static final String NEWPASS1_FIELD = "p1";
	public static final String NEWPASS2_FIELD = "p2";
	public static final String OLDPASS_FIELD = "pass";
	public static final String EMAIL_FIELD = "em";
	//account update field names for bookstore change
	public static final String FIRSTNAME_FIELD = "fn";
	public static final String LASTNAME_FIELD = "ln";
	public static final String STORENAME_FIELD = "stn";
	public static final String CONTACT_FIELD = "ci";
	public static final String ADDRESS_FIELD = "ai";
	public static final String MOREINFO_FIELD = "mi";
	public static final String WEBSITE_FIELD = "wb";
	//account post field names
	public static final String TITLE_FIELD = "tf";
	public static final String AUTHOR_FIELD = "af";
	public static final String ISBN_FIELD = "isbn";
	public static final String USEDPRICE_FIELD = "upf";
	public static final String NEWPRICE_FIELD = "npf";
	public static final String CONDITION_FIELD = "cond";
	public static final String COPIES_FIELD = "cop";
	public static final String COMMENT_FIELD = "com";
	//jsp paths:
	private final String PATH_ACCOUNT_TOP = "/jsp/bookstore_account/bookstore_account_top.jsp";
	private final String PATH_ACCOUNT_BOTTOM = "/jsp/bookstore_account/bookstore_account_bottom.jsp";
	private final String PATH_ACCOUNT_UPDATE = "/jsp/bookstore_account/bookstore_account_update.jsp";
	private final String PATH_ACCOUNT_UPDATE_GOOD = "/jsp/bookstore_account/bookstore_account_update_good.jsp";
	private final String PATH_ACCOUNT_POST = "/jsp/bookstore_account/bookstore_account_post.jsp";
	private final String PATH_ACCOUNT_POST_GOOD = "/jsp/bookstore_account/bookstore_account_post_good.jsp";
	private final String PATH_ACCOUNT_BOOKS = "/jsp/bookstore_account/bookstore_account_books.jsp";
	public void service(HttpServletRequest request,
    	HttpServletResponse response) throws ServletException{
    	try{
    		if (request.getPathInfo()==null 
				|| !request.getPathInfo().matches(
					"/("+OPTION1+"|"+OPTION2
					+"|"+OPTION3+").*")){
				response.sendRedirect(response
					.encodeRedirectURL("/"));
			}else if(!dummyAuthentication(request)){
				//if user is not authenticated send to signin
				response.sendRedirect(response.encodeRedirectURL(URLBOOKSTORESIGNIN+"?/bookstores/account"+request.getPathInfo()));
			}else{
				//setting headers
				response.setContentType("text/html");
				response.setHeader("Cache-Control", "no-cache");
				response.setHeader("Expires", "0");
				response.setHeader("Pragma", "no-cache");
				RequestDispatcher rd = getServletContext()
					.getRequestDispatcher(PATH_ACCOUNT_TOP);
				if (request.getPathInfo().matches(
					"/("+OPTION1+").*")){
					//including top of account page
					request.setAttribute(HEADERATTR,"Account Update");
					request.setAttribute(OPTIONATTR,OPTION1);
					rd.include(request,response);
					update(request,response);
				}else if (request.getPathInfo().matches(
					"/("+OPTION2+").*")){
					//including top of account page
					request.setAttribute(HEADERATTR,"Books");
					request.setAttribute(OPTIONATTR,OPTION2);
					rd.include(request,response);
					books(request,response);
				}else  if (request.getPathInfo().matches(
					"/("+OPTION3+").*")){
					//including top of account page
					request.setAttribute(HEADERATTR,"Posting Books");
					request.setAttribute(OPTIONATTR,OPTION3);
					rd.include(request,response);
					post(request,response);
				}
				//including bottom
				rd = getServletContext()
					.getRequestDispatcher(PATH_ACCOUNT_BOTTOM);
				rd.include(request,response);
			}
		}catch(Exception e){
			log.writeError(e.getMessage());
			throw new ServletException(e);
		}
    } //end service
    private void update(HttpServletRequest request,
    	HttpServletResponse response) throws Exception{
    	HttpSession session = request.getSession();
    	//getting authenticated user
    	BookstoreUser user = (BookstoreUser)request.getSession()
    		.getAttribute(StringInterface.BOOKSTOREUSERATTR);
    	ParameterParser parameter = new ParameterParser(request);
    	/*hiddenID - security check that confirms
    	 *that the user came from account page
    	 */
    	String hiddenID = parameter.getString(HIDDENID,null);
    	boolean defaultPage = true;
    	boolean needToGetBookInfo = true;
    	String error = null;
    	ConnectionPool conPool = getConnectionPool();
    	Connection con = conPool.getConnection();
    	try{
    		//two options are possible ?user and ?info
    		if (request.getQueryString()==null 
    			|| hiddenID==null
    			|| !hiddenID.equals((String)session.getAttribute(HIDDENID))){
    			defaultPage = true;
    		}else if(request.getQueryString().equals("user")){
    			defaultPage = false;
    			//updating user info i.e. password,email
    			String newpass1 = parameter.getString(NEWPASS1_FIELD,null);
      			String newpass2 = parameter.getString(NEWPASS2_FIELD,null);
				String oldpass = parameter.getString(OLDPASS_FIELD,null);
    			String email = parameter.getString(EMAIL_FIELD,null);
    			try{
    				bookstoreUsersTable usersTable = new bookstoreUsersTable();
    				boolean specified = false;
    				//if password change requested
    				if(newpass1!=null){
    					if (newpass2==null || !newpass1.equals(newpass2)){
    						throw new FormatException("please verify your new password correctly");
    					}
    					usersTable.setPassword(newpass1);
    					specified = true;
    				}
    				//if email change requested
    				if (email!=null){
    					usersTable.setEmail(email);
    					specified = true;
    				}
    				usersTable.setID(user.getID());
    				//check that at least one field was specified
    				if (!specified){
    					throw new FormatException("no fields were specified");
    				}
    				/*
    				 *check that current password 
    				 *was entered correctly
    				 */
    				if (oldpass == null
    					|| !confirmUserWithEncrypted(user.getID(),userUtils.getEncrypted(oldpass),con)
    					){
    					throw new FormatException("current password was entered incorectly");
    				}
    				if (updateStoreUser(usersTable,con)){
    					//adding new password to the session
    					//if it was changed
    					if (newpass1!=null){
    						user.setEpass(userUtils.getEncrypted(newpass1));
    					}
    					session.setAttribute(StringInterface.BOOKSTOREUSERATTR,user);
    				}else{
    					throw new Exception("failed to update user:"+user.getID());
    				}
    			}catch(FormatException e){
    				error = e.getMessage();
    				defaultPage = true;
    				needToGetBookInfo = true;
    			}
    		}else if(request.getQueryString().equals("info")){
    			//updating general store information
    			defaultPage = false;
    			String firstn = parameter.getString(FIRSTNAME_FIELD,null);
				String lastn = parameter.getString(LASTNAME_FIELD,null);
				String storen = parameter.getString(STORENAME_FIELD,null);
				String contact = parameter.getString(CONTACT_FIELD,null);
				String address = parameter.getString(ADDRESS_FIELD,null);
				String moreinfo = parameter.getString(MOREINFO_FIELD,null);
				String website = parameter.getString(WEBSITE_FIELD,null);
    			try{
    				/*
    				 *check that all required fields
    				 *are NOT empty
    				 */
					if (firstn == null || lastn == null
						|| storen == null || contact == null
						|| address == null){
						throw new FormatException("one of the required fields is empty");
					}
					//check format
					if (!tools.allASCII(contact) 
						|| !tools.allASCII(address)
						|| (website!=null 
						&& !tools.allASCII(website))
						|| (moreinfo!=null 
						&& !tools.allASCII(moreinfo)
						|| !tools.allASCII(firstn)
						|| !tools.allASCII(lastn)
						|| !tools.allASCII(storen))
					){
						throw new FormatException("one of the fields contains illegal characters");
					}
					//if no erros add this info to permament table
					String[] infoTable = new String[]{
						user.getID(),firstn,lastn,storen,
						contact,new DateObject().getDate(),
						address,moreinfo,website
					};
					if (!updateStoreInfo(infoTable,con)){
						throw new Exception("failed to update store's info");
					}
    			}catch(FormatException e){
    				request.setAttribute(FIRSTNAME_FIELD,parameter.getString(FIRSTNAME_FIELD,""));
					request.setAttribute(LASTNAME_FIELD,parameter.getString(LASTNAME_FIELD,""));
					request.setAttribute(STORENAME_FIELD,parameter.getString(STORENAME_FIELD,""));
					request.setAttribute(CONTACT_FIELD,parameter.getString(CONTACT_FIELD,""));
					request.setAttribute(ADDRESS_FIELD,parameter.getString(ADDRESS_FIELD,""));
					request.setAttribute(MOREINFO_FIELD,parameter.getString(MOREINFO_FIELD,""));
					request.setAttribute(WEBSITE_FIELD,parameter.getString(WEBSITE_FIELD,""));
    				error = e.getMessage();
    				defaultPage = true;
    				needToGetBookInfo = false;
    			}
    		}else{
    			
    		}
    		if (defaultPage){
    			if (needToGetBookInfo){
    				//if came here first time - get store info
    				String[] storeInfo = getStoreInfo(user.getID(),con);
    				/*
    				 *if nothing found for this user
    				 *null pointer exception should
    				 *be thrown 
    				 */
    				request.setAttribute(FIRSTNAME_FIELD,storeInfo[bookstoreInfoTable.FIRSTNAME_FIELD]);
					request.setAttribute(LASTNAME_FIELD,storeInfo[bookstoreInfoTable.LASTNAME_FIELD]);
					request.setAttribute(STORENAME_FIELD,storeInfo[bookstoreInfoTable.STORENAME_FIELD]);
					request.setAttribute(CONTACT_FIELD,storeInfo[bookstoreInfoTable.CONTACTINFO_FIELD]);
					request.setAttribute(ADDRESS_FIELD,storeInfo[bookstoreInfoTable.ADDRESS_FIELD]);
					request.setAttribute(MOREINFO_FIELD,storeInfo[bookstoreInfoTable.MOREINFO_FIELD]);
					request.setAttribute(WEBSITE_FIELD,storeInfo[bookstoreInfoTable.WEBSITE_FIELD]);
    			}
    			request.setAttribute(ERRORMESSAGE,error);
    			String email = getBookstoreEmail(user.getID(),user.getEpass(),con);
    			if (email==null){
    				throw new SQLException(
    					"something is wrong - couldn't get user's email");
    			}
    			request.setAttribute(EMAIL_FIELD,email);
    			hiddenID = sqlUtils.generate(10);
    			session.setAttribute(HIDDENID,hiddenID);
    			RequestDispatcher rd = getServletContext()
    				.getRequestDispatcher(PATH_ACCOUNT_UPDATE);
				rd.include(request,response);
    		}else{
    			session.removeAttribute(HIDDENID);
    			//show successfuly updated page
    			RequestDispatcher rd = getServletContext()
    				.getRequestDispatcher(PATH_ACCOUNT_UPDATE_GOOD);
				rd.include(request,response);
    		}
    	}catch(Exception e){
    		throw e;
    	}finally{
    		conPool.free(con);
    		con = null;
    	}
    } //end update
    private void books(HttpServletRequest request,
    	HttpServletResponse response) throws Exception{
    	HttpSession session = request.getSession();
    	BookstoreUser user = (BookstoreUser)session
    				.getAttribute(StringInterface.BOOKSTOREUSERATTR);
    	boolean defaultPage = true;
    	String error = null;//error caused by user entered data
    	ConnectionPool conPool = getConnectionPool();
    	Connection con = conPool.getConnection();
    	try{
    		//determine the action
    		if (request.getPathInfo()
    			.matches(".*/(edit|remove)")
    			&& (request.getQueryString() == null
    				|| request.getQueryString().length()
    					!= bookstoreBooksTable.BOOKID_LENGTH)){
    			defaultPage = false;
    			error = "we were unable to find the book specified";
    		}else if (request.getPathInfo().matches(".*/edit")){
    			defaultPage = false;
    			bookstoreBooksTable book = getBook(request.getQueryString(),con);
    			if (book!=null){
    				//taking care of the security feature
    				String hiddenID = sqlUtils.generate(20);
    				request.setAttribute(HIDDENID,hiddenID);
    				session.setAttribute(HIDDENID,hiddenID);
    				//setting all book fields
    				request.setAttribute(BOOKIDATTR,book.getID());
    				request.setAttribute(TITLE_FIELD,book.getTitle());
					request.setAttribute(AUTHOR_FIELD,book.getAuthor());
					request.setAttribute(ISBN_FIELD,book.getISBN());
					request.setAttribute(COPIES_FIELD,book.getCopies());
					request.setAttribute(USEDPRICE_FIELD,book.getUsedPrice());
					request.setAttribute(NEWPRICE_FIELD,book.getNewPrice());
					request.setAttribute(COMMENT_FIELD,book.getComment());
					request.setAttribute(CONDITION_FIELD,book.getCondition());
    				RequestDispatcher rd = getServletContext()
						.getRequestDispatcher(PATH_ACCOUNT_POST);
					rd.include(request,response);
    			}else{
    				error = "we were unable to find the book specified";
    			}
    		}else if (request.getPathInfo().matches(".*/remove")){
    			if (removeBook(request.getQueryString(),con)){
    				defaultPage = true;
    			}else{
    				defaultPage = false;
    				error = "the specified book wasn't found";
    			}
    		}
    		if (defaultPage){
    			//getting requested page
    			ParameterParser parameter = new ParameterParser(request);
    			int search_page = parameter.getInt("p",1);
    			if (search_page<1){
					search_page = 1;
				}
				//where 10 is the limit
				int searchFrom = (search_page-1)*10;
    			//getting a list of active books
    			Object[] temp = getBooks(user.getID(),searchFrom,con);
				ResultSet rs = (ResultSet)temp[1];
				int totalCount = Integer.parseInt((String)temp[0]);
				request.setAttribute(PAGES,pages(totalCount,search_page));
				request.setAttribute(BOOKSATTR,rs);
    			RequestDispatcher rd = getServletContext()
					.getRequestDispatcher(PATH_ACCOUNT_BOOKS);
				rd.include(request,response);
				rs = null;
    		}else if (error != null){
    			request.setAttribute(StringInterface.ERRORPAGEATTR,error);
    			RequestDispatcher rd = getServletContext()
					.getRequestDispatcher(PATHUSERERROR2);
				rd.include(request,response);
    		}
    	}catch(Exception e){
    		throw e;
    	}finally{
    		conPool.free(con);
    		con = null;
    	}
    } //end books
    private String pages(int totalResults,int page){
		String line = "";
		final int limit = 10; //# of results to return at once
		if (totalResults!=0){
			line += "<p align=center style='font-size:12pt'>Pages: ";
			int maxpages;
			//setting max pages
			maxpages = (int)Math.ceil(totalResults/(double)limit);
			//shows previous page button if current page is bigger than min
			if (page>1){
				line += "<a href='"+URL_BOOKSTORE_ACCOUNT_BOOKS+"?p="+(page-1)
				+"' style='text-decoration: none;'><font face='Times New Roman' color='#0066CC'>&#9668;</font></a>&nbsp;";
			}
			for (int j=1;j<=maxpages;j++){
				if (page==j)
					line += j+"&nbsp"; //current page
				else{
					line += "<a href='"+URL_BOOKSTORE_ACCOUNT_BOOKS+"?p="+j+"'>"+j+"</a>&nbsp;";
				}
			}
			//shows next button if current page is less than max
			if (page<maxpages){ 
				line += "<a href='"+URL_BOOKSTORE_ACCOUNT_BOOKS+"?p="+(page+1)
				+"' style='text-decoration: none;'><font face='Times New Roman' color='#0066CC'>&#9658;</font></a>&nbsp;";
			}
			line += "</p>";
		}
		return line;
	} //end pages
   	private void post(HttpServletRequest request,
    	HttpServletResponse response) throws Exception{
    	boolean defaultPage = false;
    	HttpSession session = request.getSession();
    	String error = null; //badly entered data by user
    	ParameterParser parameter = new ParameterParser(request);
    	String hiddenID = parameter.getString(HIDDENID,null);
    	String title = parameter.getString(TITLE_FIELD,null);
		String author = parameter.getString(AUTHOR_FIELD,null);
		String isbn = parameter.getString(ISBN_FIELD,null);
		String copies = parameter.getString(COPIES_FIELD,null);
		String usedPrice = parameter.getString(USEDPRICE_FIELD,null);
		String newPrice = parameter.getString(NEWPRICE_FIELD,null);
		String comment = parameter.getString(COMMENT_FIELD,null);
		String[] conditions = request.getParameterValues(CONDITION_FIELD);
		if (conditions == null){
			conditions = new String[]{"0"};
		}
    	if (hiddenID == null || !hiddenID.equals(
    			session.getAttribute(HIDDENID))){
    		defaultPage = true;
      	}else{
    		try{
    			bookstoreBooksTable book = new bookstoreBooksTable();
    			//check if bookid was passed
    			boolean update = false;
    			if (request.getQueryString()!=null 
    				&& request.getQueryString().length() 
    				== bookstoreBooksTable.BOOKID_LENGTH){
    				book.setID(request.getQueryString());
    				update = true;
    				request.setAttribute(BOOKIDATTR,request.getQueryString());
    			}else{
    				book.setID(sqlUtils.generate(bookstoreBooksTable.BOOKID_LENGTH));
    			}
    			book.setTitle(title);
    			book.setAuthor(author);
    			if (isbn != null){
    				book.setISBN(isbn);
    			}
    			book.setCopies(copies);
    			if (usedPrice != null){
    				book.setUsedPrice(usedPrice);
    			}
    			if (newPrice != null){
    				book.setNewPrice(newPrice);
    			}
    			//check that at least one price was specified
    			if (usedPrice == null && newPrice == null){
    				throw new FormatException("please specify at least one of the two prices");
    			}
    			if (comment != null){
    				book.setComment(comment);
    			}
    			book.setCondition(conditions);
    			//if still here and no errors add book
    			ConnectionPool conPool = getConnectionPool();
    			Connection con = conPool.getConnection();
    			try{
    				BookstoreUser user = (BookstoreUser)request.getSession()
    					.getAttribute(StringInterface.BOOKSTOREUSERATTR);
    				if (!addBook(book,user.getID(),con,update)){
    					throw new Exception("failed to add book:"+book.getID()+" for "+user.getID());
    				}else if (book.getISBN()!=null){
    					//adding isbn to database
    					storeISBN addisbn = new storeISBN(book.getISBN(),conPool);
    					addisbn.start();
    				}
    			}catch(Exception e){
    				throw e;
    			}finally{
    				conPool.free(con);
    				con = null;
    			}
    		}catch(FormatException e){
    			error = e.getMessage();
    			request.setAttribute(ERRORMESSAGE,error);
    			defaultPage = true;
    		}
    	}
    	RequestDispatcher rd = null;
    	if (defaultPage){
    		request.setAttribute(TITLE_FIELD,title);
			request.setAttribute(AUTHOR_FIELD,author);
			request.setAttribute(ISBN_FIELD,isbn);
			request.setAttribute(COPIES_FIELD,copies);
			request.setAttribute(USEDPRICE_FIELD,usedPrice);
			request.setAttribute(NEWPRICE_FIELD,newPrice);
			request.setAttribute(COMMENT_FIELD,comment);
			request.setAttribute(CONDITION_FIELD,conditions);
    		hiddenID = sqlUtils.generate(10);
    		request.setAttribute(HIDDENID,hiddenID);
    		session.setAttribute(HIDDENID,hiddenID);
    		rd = getServletContext()
    			.getRequestDispatcher(PATH_ACCOUNT_POST);
			rd.include(request,response);
    	}else{
    		request.removeAttribute(HIDDENID);
    		rd = getServletContext()
    			.getRequestDispatcher(PATH_ACCOUNT_POST_GOOD);
			rd.include(request,response);
    	}
    } //end post
    //checks if this user was authenticated
    private boolean dummyAuthentication(
    	HttpServletRequest request){
    	//user authentication is required!
		BookstoreUser user = (BookstoreUser)request.getSession()
    			.getAttribute(StringInterface.BOOKSTOREUSERATTR);
    	if (user == null){
    		return false;
    	}else{
    		return true;
    	}
    } //end dummyAuthentication
    /*************** SQL METHODs ****************/
    private String getBookstoreEmail(String id,
		String epass,Connection con)
		throws SQLException{
		try{
			if (id == null || epass == null){
				return null;
			}
			Statement st = con.createStatement();
			String query = "SELECT "
				+bookstoreUsersTable.EMAIL+" FROM "
				+TABLEBOOKSTOREUSERS
				+" WHERE "
				+bookstoreUsersTable.ID
				+" = '"+id+"' AND "
				+bookstoreUsersTable.EPASS
				+" = '"+epass+"' LIMIT 1";
			ResultSet rs = st.executeQuery(query);
			if (rs.next()){
				return rs.getString(bookstoreUsersTable.EMAIL);
			}else{
				return null;
			}
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end getBookstoreEmail
	private boolean updateStoreUser(
		bookstoreUsersTable usersTable,
		Connection con) throws SQLException{
		try{
			Statement st = con.createStatement();
			String query = "UPDATE "
				+TABLEBOOKSTOREUSERS
				+" SET ";
			boolean comma = false;
			if (usersTable.getEmail()!=null){
				query += bookstoreUsersTable.EMAIL
					+" = '"+usersTable.getEmail()+"'";
				comma = true;
			}
			if (usersTable.getPassword()!=null){
				if (comma){
					query+=",";
				}
				query += 
					bookstoreUsersTable.PASSWORD
					+" = '"+usersTable.getPassword()+"',"
					+bookstoreUsersTable.EPASS
					+" = '"+userUtils.getEncrypted(usersTable.getPassword())+"'";
			}
			query += " WHERE "
				+bookstoreUsersTable.ID
				+" = '"+usersTable.getID()+"' LIMIT 1";
			return st.executeUpdate(query) == 1;
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end updateUser
	private String[] getStoreInfo(
		String storeID,Connection con) throws SQLException{
		try{
			Statement st = con.createStatement();
			String query = "SELECT * FROM "
				+TABLEBOOKSTOREINFO
				+" WHERE "
				+bookstoreInfoTable.BOOKSTOREID
				+" = '"
				+storeID+"' LIMIT 1";
			ResultSet rs = st.executeQuery(query);
			if (rs.next()){
				return new String[]{
					rs.getString(bookstoreInfoTable.BOOKSTOREID),
					rs.getString(bookstoreInfoTable.FIRSTNAME),
					rs.getString(bookstoreInfoTable.LASTNAME),
					rs.getString(bookstoreInfoTable.STORENAME),
					rs.getString(bookstoreInfoTable.CONTACTINFO),
					rs.getString(bookstoreInfoTable.LASTUPDATED),
					rs.getString(bookstoreInfoTable.ADDRESS),
					rs.getString(bookstoreInfoTable.MOREINFO),
					rs.getString(bookstoreInfoTable.WEBSITE)
				};
			}else{
				return null;
			}
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end getStoreInfo
	private boolean confirmUserWithEncrypted(
		String id,String eP,Connection con) 
		throws SQLException{
		try{
			//do not query empy values
			if (id == null || eP == null 
			|| id.equals("") || eP.equals("")){
				return false;
			}
			Statement st = con.createStatement();
			//where username = u and encryptedpass = eP
			String query = "SELECT * FROM "
				+TABLEBOOKSTOREUSERS
				+" WHERE "+bookstoreUsersTable.ID
				+" = '"+id+"' AND "+bookstoreUsersTable.EPASS
				+" = '"+eP+"' LIMIT 1";
			ResultSet rs = st.executeQuery(query);
			return rs.next();
		}catch (SQLException e){
			log.writeException(e.getMessage());
			throw new SQLException(e.getMessage());
		}
	} //end confirmUserWithEncrypted
	private boolean updateStoreInfo(String[] table,
		Connection con) throws SQLException{
		try{
			Statement st = con.createStatement();
			String query = "UPDATE "+TABLEBOOKSTOREINFO
				+" SET ";
			for (int i=0;i<(table.length-1);i++){
				query += bookstoreInfoTable.fieldNames[i];
					if (table[i]!=null)
						query += " = '"+table[i]+"',";
					else
						query += " = "+table[i]+",";
			}
			query += bookstoreInfoTable.fieldNames[table.length-1];
			if (table[table.length-1]!=null)
				query += " = '"+table[table.length-1]+"'";
			else
				query += " = "+table[table.length-1];
			query +=" WHERE "
				+bookstoreInfoTable.BOOKSTOREID
				+" = '"+table[bookstoreInfoTable.BOOKSTOREID_FIELD]+"' LIMIT 1";
			return st.executeUpdate(query)==1;
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end updateStoreInfo
	private boolean addBook(bookstoreBooksTable book,
		String storeID,Connection con,boolean update) 
		throws SQLException{
		try{
			Statement st = con.createStatement();
			String[] fields = new String[]{
				bookstoreBooksTable.BOOKID,
				bookstoreBooksTable.STOREID,
				bookstoreBooksTable.TITLE,
				bookstoreBooksTable.AUTHOR,
				bookstoreBooksTable.ISBN,
				bookstoreBooksTable.PRICENEW,
				bookstoreBooksTable.PRICEUSED,
				bookstoreBooksTable.CONDITIONS,
				bookstoreBooksTable.COMMENT,
				bookstoreBooksTable.COPIES,
				bookstoreBooksTable.DATE
			};
			String condition = "";
			for (int i=0;i<(book.getCondition().length-1);i++){
				condition+=book.getCondition()[i]+"/";
			}
			condition+=book.getCondition()[book.getCondition().length-1];
			String[] values = new String[]{
				book.getID(),storeID,book.getTitle(),
				book.getAuthor(),book.getISBN(),
				book.getNewPrice(),book.getUsedPrice(),
				condition,book.getComment(),
				book.getCopies(),new DateObject().getDate()
			};
			String query = "";
			if (update){
				query += "UPDATE "+TABLEBOOKSTOREBOOKS
					+" SET ";
				for (int i=0;i<(values.length-1);i++){
					query += fields[i]
						+" = "
						+sqlUtils
							.toSQLString(values[i])+",";
				}
				query += fields[values.length-1]
					+" = "
					+sqlUtils.toSQLString(
						values[values.length-1])
					+" WHERE "
					+bookstoreBooksTable.BOOKID
					+" = '"+values[0]+"' LIMIT 1";
			}else{
				query += "INSERT INTO "+TABLEBOOKSTOREBOOKS
					+" ("
						+sqlUtils.sql_fields(fields)
					+") values ("
						+sqlUtils.sql_values(values)
					+")";
			}
			return st.executeUpdate(query) == 1;
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end addBook
	private Object[] getBooks(String storeID,int from,
		Connection con) throws SQLException{
		try{
			Statement st = con.createStatement();
			String countQuery = "SELECT count(*) FROM "+TABLEBOOKSTOREBOOKS
				+" WHERE "+bookstoreBooksTable.STOREID
				+" = '"+storeID+"'";
			ResultSet rs = st.executeQuery(countQuery);
			int count = 0;
			if (rs.next()){
				count = rs.getInt(1);
			}
			String query = "SELECT * FROM "+TABLEBOOKSTOREBOOKS
				+" WHERE "+bookstoreBooksTable.STOREID
				+" = '"+storeID+"' LIMIT "+from+",10";
			rs = st.executeQuery(query);
			return new Object[]{""+count,rs};
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end getBooks
	private boolean removeBook(String bookID,
		Connection con) throws SQLException{
		try{
			Statement st = con.createStatement();
			String query = "DELETE FROM "+TABLEBOOKSTOREBOOKS
				+" WHERE "+bookstoreBooksTable.BOOKID
				+" = '"+bookID+"' LIMIT 1";
			return st.executeUpdate(query)==1;
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end removeBook
	private bookstoreBooksTable getBook(String bookID,
		Connection con) throws SQLException{
		try{
			Statement st = con.createStatement();
			String query = "SELECT * FROM "+TABLEBOOKSTOREBOOKS
				+" WHERE "+bookstoreBooksTable.BOOKID
				+" = '"+bookID+"' LIMIT 1";
			ResultSet rs = st.executeQuery(query);
			if (rs.next()){
				try{
					bookstoreBooksTable book = new bookstoreBooksTable();
					book.setID(rs.getString(bookstoreBooksTable.BOOKID));
					book.setTitle(rs.getString(bookstoreBooksTable.TITLE));
					book.setAuthor(rs.getString(bookstoreBooksTable.AUTHOR));
					book.setISBN(rs.getString(bookstoreBooksTable.ISBN));
					book.setComment(rs.getString(bookstoreBooksTable.COMMENT));
					book.setCopies(rs.getString(bookstoreBooksTable.COPIES));
					book.setDate(rs.getString(bookstoreBooksTable.DATE));
					book.setUsedPrice(rs.getString(bookstoreBooksTable.PRICEUSED));
					book.setNewPrice(rs.getString(bookstoreBooksTable.PRICENEW));
					book.setCondition(rs.getString(bookstoreBooksTable.CONDITIONS).split("/"));
					return book;
				}catch(FormatException ignore){
					throw new SQLException(ignore.getMessage());
				}
			}else{
				return null;
			}
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end getBook
}