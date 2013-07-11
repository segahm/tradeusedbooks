package server;
import javax.servlet.http.*;
import javax.servlet.*;
import java.sql.*;
/**this is a class that sends a question to seller*/
public class QuestionServlet extends ServletParent 
	implements URLInterface,Tables{
	private final String PATH_QUESTION
		= "/jsp/book/question.jsp";
	private final String PATH_MAIL_QUESTION
		= "/jsp/mail/question.jsp";
	public static final String USER_ATTR
		= "user_attr";
	public static final String BOOK_ATTR
		= "book_param";
	public static final String SELLER_ATTR
		= "seller_param";
	public static final String SUCCESS_SEND
		= "success_send";
	//field names
	public static final String MESSAGE_FIELD
		= "message";
	public static final String BOOKID_FIELD 
		= "bookid";
	public static final String SELLERID_FIELD 
		= "sellerid";
	public static final String HIDDENID_FIELD
		= "questid";
	public void service(HttpServletRequest request,
                       HttpServletResponse response)
                throws ServletException{
		ConnectionPool conPool = null;
		Connection con = null;
		try{
			//establish database connection
			conPool = getConnectionPool();
			con = conPool.getConnection();
			if(!realAuthentication(request,con)){
				String queryString = request.getQueryString();
				if (request.getQueryString() == null){
					queryString = "";
				}
				//if user is not authenticated send to signin
				response.sendRedirect(
					response.encodeRedirectURL(
						URLAUTHSIGNIN+"?"+URLQUESTION+"?"
						+queryString));
			}else{
				//getting visitor info
				User user = (User)request.getSession()
    				.getAttribute(StringInterface.USERATTR);
    			usersTable userInfo = userUtils.getUserInfo(user.getID(),con);
				ParameterParser parameter = new ParameterParser(request);
				String bookID = parameter.getString(BOOKID_FIELD,null);
				String sellerID = parameter.getString(SELLERID_FIELD,null);
				String hiddenID = parameter.getString(HIDDENID_FIELD,null);
				HttpSession session = request.getSession();
				//user entered data
				String message = parameter.getString(MESSAGE_FIELD,null);
				RequestDispatcher rd = null;
				String errorMessage = null;
				Object[] values = null;
				if (bookID == null || sellerID == null
					|| bookID.length() != booksTable.ID_LENGTH
					|| sellerID.length() != usersTable.ID_LENGTH){
					errorMessage = "we were unable to find the book you specified";
				}else if ((values = findBook(bookID,sellerID,con)) 
					== null){
					errorMessage = "we were unable to find the book you specified";
				}else if (userInfo.getNumberMessages()>=usersTable.DAILY_MESSAGES_MAX){
					errorMessage = "you have exceeded the maximum allowed number of messages per day";
				}else if (message != null && hiddenID != null 
					&& hiddenID.equals(
						session.getAttribute(HIDDENID_FIELD))){
					System.out.println("sending");
					//sending message
					request.setAttribute(mailInterface.MESSAGEATTR,message);
					request.setAttribute(mailInterface.USERATTR,((usersTable)values[1]).getUsername());
					request.setAttribute(mailInterface.EMAILATTR,((usersTable)values[1]).getEmail());
					request.setAttribute(mailInterface.BOOKATTR,values[0]);
					request.setAttribute(mailInterface.MOREATTR,userInfo);
					rd = getServletContext()
						.getRequestDispatcher(PATH_MAIL_QUESTION);
					rd.include(request,response);
					//updating message count
					updateMessageCount(userInfo.getID(),con);
					userInfo.setNumberMessages((userInfo.getNumberMessages()+1)+"");
					//letting jsp know that send occured
					request.setAttribute(SUCCESS_SEND,"true");
				}
				if (errorMessage != null){
					request.setAttribute(
						StringInterface.ERRORPAGEATTR,
						errorMessage);
					rd = getServletContext()
						.getRequestDispatcher(PATHUSERERROR);
					rd.forward(request,response);
				}else{
					String genid = sqlUtils.generate(10);
					session.setAttribute(HIDDENID_FIELD,genid);
					request.setAttribute(HIDDENID_FIELD,genid);
					request.setAttribute(USER_ATTR,userInfo);
					request.setAttribute(BOOK_ATTR,values[0]);
					request.setAttribute(SELLER_ATTR,values[1]);
					rd = getServletContext()
						.getRequestDispatcher(PATH_QUESTION);
					rd.forward(request,response);
				}
			}
		}catch(Exception e){
			log.writeError(e.getMessage());
			throw new ServletException(e);
		}finally{
			conPool.free(con);
			con = null;
		}
	} //send service
	protected static Object[] findBook(String bookID,
		String sellerID, Connection con) 
		throws SQLException{
		try{
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM "
				+TABLEBOOKS+","+USERSTABLE+" WHERE "
					+TABLEBOOKS+"."+booksTable.ID
				+" = '"
					+bookID
				+"' AND "
					+USERSTABLE+"."+usersTable.ID
				+" = '"
					+sellerID+"' LIMIT 1");
			//if found
			if (rs.next()){
				booksTable booktable = new booksTable(bookID);
				booktable.setTitle(rs.getString(booksTable.TITLE));
				booktable.setAuthor(rs.getString(booksTable.AUTHOR));
				booktable.setPrice(rs.getString(booksTable.PRICE));
				usersTable usertable = new usersTable();
				usertable.setUsername(
					rs.getString(usersTable.USERNAME));
				usertable.setEmail(
					rs.getString(usersTable.EMAIL));
				usertable.setID(sellerID);
				return new Object[]{
					booktable,usertable
				};
			}else{
				return null; //not found
			}
		}catch(Exception e){
			log.writeException(e.getMessage());
			throw new SQLException(e.getMessage());
		}
	} //end findBook
	private boolean realAuthentication(
    	HttpServletRequest request,Connection con)
    	throws SQLException{
    	//user authentication is required!
		User user = (User)request.getSession()
    			.getAttribute(StringInterface.USERATTR);
    	if (user == null){
    		return false;
    	}
    	boolean authenticated = false;
    	try{
    		authenticated = userUtils.confirmUserWithEncrypted(
    			user.getID(),user.getEncrypted(),con);
    	}catch(Exception e){
    		throw new SQLException(e.getMessage());
    	}
    	return authenticated;
    } //end realAuthentication
    private void updateMessageCount(String userid,
    	Connection con) throws SQLException{
    	try{
    		Statement st = con.createStatement();
			int resultsUpdated = st.executeUpdate("UPDATE "+USERSTABLE
				+" SET "+usersTable.MESSAGES+" = "
				+usersTable.MESSAGES+"+1 WHERE "
				+usersTable.ID+" = '"+userid+"' LIMIT 1");
			if (resultsUpdated!=1){
				throw new SQLException("failed to update message count for "+userid);
			}
    	}catch(SQLException e){
    		log.writeException(e.getMessage());
    		throw e;
    	}
    } //end updateMessageCount
}
