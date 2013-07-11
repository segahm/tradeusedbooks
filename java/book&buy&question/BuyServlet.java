package server;
import javax.servlet.http.*;
import javax.servlet.*;
import java.sql.*;
/*
 *BuyServlet requires 3 parameters passed to it
 *using get action
 *parameters are:
 *book id, seller id & and sellerID.hashCode() - security features
 */
public class BuyServlet extends ServletParent 
	implements URLInterface,Tables{
	private final String PATH_BUY_SUCCESS
		= "/jsp/buy/buySuccess.jsp";
	private final String PATH_BUY_CONFIRM
		= "/jsp/buy/buyConfirm.jsp";
	private final String PATHBIDCONFIRMATION
		= "/jsp/mail/bidConfirmation.jsp";
	private final String PATHBOOKUPDATE
		= "/jsp/mail/bookUpdate.jsp";
	public static final String FIELD_MESSAGE
		= "message";
	public static final String ATTR_SELLER
		= "attrseller";
	public static final String ATTR_BUYER
		= "attrbuyer";
	public static final String ATTR_BOOK
		= "attrtitle"; 
	public static final String ATTR_COLLEGE
		= "attrcollege"; 
	public void service(HttpServletRequest request,
                       HttpServletResponse response)
                throws ServletException{
		try{
			ConnectionPool conPool = getConnectionPool();				
			if(!realAuthentication(request,conPool)){
				String queryString = request.getQueryString();
				if (request.getQueryString() == null){
					queryString = "";
				}
				//if user is not authenticated send to signin
				response.sendRedirect(
					response.encodeRedirectURL(
						URLAUTHSIGNIN+"?"+URLBUY+"?"
						+queryString));
			}else{
				response.setHeader("Cache-Control", "no-cache");
				response.setHeader("Expires", "0");
				response.setHeader("Pragma", "no-cache");
				response.setContentType("text/html");
				String errorMessage = processRequest(request,response,conPool);
				if(errorMessage != null){
					request.setAttribute(
						StringInterface.ERRORPAGEATTR,
						errorMessage);
					RequestDispatcher rd = getServletContext()
						.getRequestDispatcher(PATHUSERERROR);
					rd.include(request,response);
				}
			}
		}catch(Exception e){
			throw new ServletException(e);
		}
	}
	private String processRequest(HttpServletRequest request,
		HttpServletResponse response,
		ConnectionPool conPool) 
		throws Exception{
		//getting id parameters
		ParameterParser parameter = new ParameterParser(request);
		String bookID = parameter.getString(bookInterface.FIELD_ID,null);
		String sellerID = parameter.getString(bookInterface.FIELD_SELLERID,null);
		String message = parameter.getString(FIELD_MESSAGE,null);
		int codeID = parameter.getInt(bookInterface.FIELD_HIDDENID,0);
		//get buyer's user object
		User user = (User)request.getSession()
    		.getAttribute(StringInterface.USERATTR);
		//security feauture:
		//if one of ids is missing or incorrect return false
		if (bookID == null || sellerID == null
			|| codeID == 0
			|| bookID.length() != booksTable.ID_LENGTH
			|| sellerID.length() != usersTable.ID_LENGTH
			|| codeID != Math.abs(bookID.hashCode())){
			return "We were unable to find the book you specified! Please make sure that the book id is correct.";
		}
		if (user.getID().equals(sellerID)){
			return "You may not purchase an item from yourself!";
		}
    	//get connection
    	Connection con = conPool.getConnection();
    	try{
			booksTable book = generalUtils.getBook(bookID,con);
			/*security feauture:
			 *check seller id == passed hidden id
			 *book != null
			 */
			if (book == null 
				|| !book.getSellerID().equals(sellerID)){
				return "We were unable to find the book you specified! Please make sure that the book id is correct.";
			}
			usersTable sellerInfo = userUtils.getUserInfo(sellerID,con);
			usersTable buyerInfo = userUtils.getUserInfo(user.getID(),con);
			collegeTable college = getCollege(book.getCollegeID()+"",con);
			//if still here continue
			if (message==null){
				request.setAttribute(ATTR_BOOK,book);
				request.setAttribute(ATTR_SELLER,sellerInfo);
				request.setAttribute(ATTR_BUYER,buyerInfo);
				request.setAttribute(ATTR_COLLEGE,college.getFull());
				RequestDispatcher rd = getServletContext()
						.getRequestDispatcher(PATH_BUY_CONFIRM);
				rd.include(request,response);
				return null;
			}else if (buy(book,user,con)){
				//sending email to buyer
				request.setAttribute(mailInterface.USERATTR,buyerInfo.getUsername());
				request.setAttribute(mailInterface.EMAILATTR,
					buyerInfo.getEmail());
				request.setAttribute(mailInterface.BOOKATTR,book);
				request.setAttribute("book_id",bookID);
				request.setAttribute("seller_id",sellerID);
				
				RequestDispatcher rd = getServletContext()
						.getRequestDispatcher(PATHBIDCONFIRMATION);
				rd.include(request,response);
				//sending email to seller
				request.setAttribute(ATTR_COLLEGE,college.getFull());
				request.setAttribute(mailInterface.USERATTR,
					sellerInfo.getUsername());
				request.setAttribute(mailInterface.EMAILATTR,
					sellerInfo.getEmail());
				request.setAttribute(mailInterface.MESSAGEATTR,
					message);
				request.setAttribute(mailInterface.BOOKATTR,
					book);
				request.setAttribute(mailInterface.MOREATTR,
					buyerInfo);
					
				request.setAttribute("book_id",bookID);
				request.setAttribute("buyer_id",user.getID());
				rd = getServletContext()
						.getRequestDispatcher(PATHBOOKUPDATE);
				rd.include(request,response);
				//showing success message
				rd = getServletContext()
						.getRequestDispatcher(PATH_BUY_SUCCESS);
				rd.include(request,response);
				return null;
			}else{
				throw new Exception("failed to process with buy");
			}
		}catch(Exception e){
			throw e;
		}finally{
			//recycle
			conPool.free(con);
			con = null;
		}
	}
	/**checks database for this user*/
    private boolean realAuthentication(
    	HttpServletRequest request,ConnectionPool conPool)
    	throws SQLException{
    	//user authentication is required!
		User user = (User)request.getSession()
    			.getAttribute(StringInterface.USERATTR);
    	if (user == null){
    		return false;
    	}
    	Connection con = conPool.getConnection();
    	boolean authenticated = false;
    	try{
    		authenticated = userUtils.confirmUserWithEncrypted(
    			user.getID(),user.getEncrypted(),con);
    	}catch(Exception e){
    		throw new SQLException(e.getMessage());
    	}finally{
    		conPool.free(con);
    		con = null;
    	}
    	return authenticated;
    } //end realAuthentication
    /**moves data into certain tables to simulate the 
     *buying process*/
	private boolean buy(booksTable book, User buyer,
		Connection con) throws SQLException{
		try{
			Statement st = con.createStatement();
			/*all parameters are considered to be full--
			 *book is completed and so as buyer object
			 */
			 con.setAutoCommit(false);
			 //1st add purchase to buyers table
			 st.addBatch("INSERT INTO "+TABLEBUYERS
			 	+" ("
			 		+buyersTable.BOOKID+","
			 		+buyersTable.BUYERID+","
			 		+buyersTable.DATE
			 	+") values (\""
			 		+book.getID()
			 	+"\",\""
			 		+buyer.getID()
			 	+"\",\""
			 		+new DateObject().getDate()
			 	+"\")");
			 //2nd add the book to old books
			 String[] oldbooksFields = {
			 	oldBooksTable.SELLERID,oldBooksTable.BUYERID,
			 	oldBooksTable.BOOKID,oldBooksTable.TITLE,
			 	oldBooksTable.AUTHOR,oldBooksTable.ISBN,
			 	oldBooksTable.CONDITION,oldBooksTable.PRICE,
			 	oldBooksTable.COMMENT,oldBooksTable.COLLEGE,
			 	oldBooksTable.DATE
			 };
			 String[] oldbooksValues = {
			 	book.getSellerID(),buyer.getID(),
			 	book.getID(),book.getTitle(),
			 	book.getAuthor(),book.getISBN(),
			 	book.getCondition()+"",book.getPrice()+"",
			 	book.getComment(),book.getCollegeID()+"",
			 	new DateObject().getDate()
			 };
			 st.addBatch("INSERT INTO "+TABLEOLDBOOKS
			 	+" ("+sqlUtils.sql_fields(oldbooksFields)
			 	+") values ("+sqlUtils.sql_values(oldbooksValues)+")");
			 //3rd delete record from booksTable
			 st.addBatch("DELETE FROM "+TABLEBOOKS
				+" WHERE "+booksTable.ID
				+" = '"+book.getID()+"'");
			//execute all 3 steps and set auto commit mode
			int[] updateCounts = st.executeBatch();
			if (updateCounts[0] == 1
				&& updateCounts[1] == 1
				&& updateCounts[2] == 1){
				return true;
			}else{
				return false;
			}
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}finally{
			con.setAutoCommit(true);
		}
	}
	protected collegeTable getCollege(String id,
		Connection con)
		throws SQLException{
		try{
			ResultSet rs = null;
			Statement statement = con.createStatement();
			rs = statement.executeQuery("SELECT * FROM "
				+TABLECOLLEGES+" WHERE "+collegeTable.ID
				+" = "+id+" LIMIT 1");
			//if found
			if (rs.next()){
				collegeTable table = new collegeTable();
				table.setID(id);
				table.setShort(rs.getString(
					collegeTable.SHORTNAME));
				table.setFull(rs.getString(
					collegeTable.FULLNAME));
				return table;
			}else{
				return null; //not found
			}
		}catch(Exception e){
			log.writeException(e.getMessage());
			throw new SQLException(e.getMessage());
		}
	} //end getCollege
}
