package server;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.sql.*;
/**
 *confirmation is a class that checks temporary tables
 *for entered data and thus confirms email
 *it then adds data to regular tables and deletes data
 *from old tables
 *supports urls:
 * /user.*
 */
public class confirmation extends ServletParent 
	implements confInterface,URLInterface,Tables{
	private final String PATHMAILNEWUSER
		= "/jsp/mail/newUser.jsp";
	public void service(HttpServletRequest request,
                       HttpServletResponse response)
                throws ServletException{
    	try{
    		//set no cache in different ways
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Expires", "0");
			response.setHeader("Pragma", "no-cache");
			//make sure url is correct
			if (request.getPathInfo()==null 
				|| !request.getPathInfo().matches("/"+OPTION1+".*")){
				response.sendRedirect(response
				.encodeRedirectURL("/"));
			}else{
				ConnectionPool conPool 
					= getConnectionPool();
				//do certain actions and then forward to
				//matching jsp
				//option1 fowards & redirects
				if (request.getPathInfo()
					.matches("/"+OPTION1+".*")){
					//do something for signin
					request.setAttribute(HEADERTITLE,
						"Registration Confirmation");
					user(request,response,conPool);
				}
			}
    	}catch (Exception e){
    		throw new ServletException(e);
    	}
    } //end service
   	/**user
	 *returns: void
	 *process the requests
	 */
	private void user(HttpServletRequest request,
		HttpServletResponse response,
		ConnectionPool conPool) throws Exception{
		HttpSession session = request.getSession();
		//getting input fields
		String username = request.getParameter(USERFIELD);
		String password = request.getParameter(PASSFIELD);
		//generated session id from input field
		String hiddenSession = request.getParameter(SESSIONFIELDATTR);
		//agree to terms field
		String agreeChecked = request.getParameter(AGREEFIELD);
		boolean defaultPage = false;
		User user = null;	//user object returned
		String email = null; //email of the new user
		String user_error = null;
		String pass_error = null;
		String generalError = null;
		if (username == null || password == null
			|| hiddenSession == null 
			|| !hiddenSession.equals(session.getAttribute(SESSIONFIELDATTR))){
			defaultPage = true;
		}else{
			/*
			 *if not default and agree is null redirect to terms
		 	*/
			if (agreeChecked == null){
				generalError = "you must agree to terms of service";
				defaultPage = true;
			}
				user = new User();
			try{
				user.setUsername(username);
			}catch(FormatException e){
				user_error = e.getMessage();
				defaultPage = true;
			}
			try{
				user.setPassword(password);
			}catch(FormatException e){
				pass_error = e.getMessage();
				defaultPage = true;
			}
			//if no errors check database
			if (!defaultPage){
				Connection connection 
					= conPool.getConnection();
				try{
					try{
						Object[] userAndEmail = 
							addUser(user,connection);
						if (userAndEmail == null){
							defaultPage = true;
						}else{
							user = (User)userAndEmail[0];
							email = (String)userAndEmail[1];
						}
					}catch (UserException e){
						//bad user/pass combination
						defaultPage = true;
						generalError = e.getMessage();
					}
				}catch (Exception e){
					throw e;
				}finally{
					//done with database - recycle
					conPool.free(connection);
					connection = null;
				}
			}
		}	
    	if (defaultPage){
			//create a hidden session id
			hiddenSession = sqlUtils.generate(20);
			session.setAttribute(SESSIONFIELDATTR,
				hiddenSession);
			request.setAttribute(ERRORMESSAGEATTR,generalError);
			request.setAttribute(ERRORMESSAGE1ATTR,
				user_error);
			request.setAttribute(ERRORMESSAGE2ATTR,
				pass_error);
			//forward the request to jsp
			RequestDispatcher rd = 
				getServletContext()
				.getRequestDispatcher(
					PATHCONFIRMUSER);
			rd.forward(request,response);
		}else if (agreeChecked != null){
			//remove hidden attribute
			session.removeAttribute(SESSIONFIELDATTR);
			session.setMaxInactiveInterval(600);
			/*
			 *attach username,email with request object
			 *and send them to included jsp;
			 *sending mail
			 */
			request.setAttribute(
				mailInterface.USERATTR,
				user.getUsername());
			request.setAttribute(
				mailInterface.EMAILATTR,
				email);
			RequestDispatcher rd = 
				getServletContext()
				.getRequestDispatcher(
					PATHMAILNEWUSER);
			rd.include(request,response);
			//clearing request from mail attr.
			request.removeAttribute(
				mailInterface.USERATTR);
			request.removeAttribute(
				mailInterface.EMAILATTR);
			//adding to session a new user
			session.
				setMaxInactiveInterval(
					StringInterface.MAXINACTIVETIME);
			session.setAttribute(
				StringInterface.USERATTR,user);
			//redirecting to account update page
			response.sendRedirect(
				response.encodeRedirectURL(
					URLACCOUNTUPDATE));
		}
	} //end user
	/*
	 *1) checks if username and password match
	 *2) gets email and userID and adds both to User
	 *3) inserts all that data into registered users table
	 *4) delete all that data from old table 
	 *returns: Object[]{User(username,id,encrypted),
	 *email}
	 *requires: username,password,email
	 */
	protected static Object[] addUser(User user,
		Connection con) throws UserException, SQLException
	{
		try
		{
			Statement st = con.createStatement();
			ResultSet rsTemp = null;
			//getting email from temp table
			rsTemp = st.executeQuery("SELECT "
			+tempusersTable.EMAIL+" FROM "
			+TEMPUSERSTABLE+" WHERE "
			+tempusersTable.USERNAME+" = '"
			+user.getUsername()+"' AND "
			+tempusersTable.PASS+" = '"
			+user.getPassword()+"'");
			String email = null;
			if (rsTemp.next()){
				//adding email & id to the returned object
				email = rsTemp.getString(
					tempusersTable.EMAIL);
				user.setID(sqlUtils.generate(usersTable.ID_LENGTH));
				user.setEncrypted(userUtils
					.getEncrypted(user.getPassword()));
			}else{
				throw new UserException(
				"bad username/password combination");
			}
			con.setAutoCommit(false);
			//date is of the folloing format: int(1-12)/int(1-31)/int(year)
			DateObject date = new DateObject();
			//adding batch commands
			//first add user to USERSTABLE
			st.addBatch("INSERT INTO "+USERSTABLE
				+" ("+usersTable.ID+","
				+usersTable.USERNAME
				+","+usersTable.PASSWORD+","
				+usersTable.EMAIL
				+","+usersTable.EPASS+","
				+usersTable.DATE+") values (\""
				+user.getID()+"\",\""
				+user.getUsername()+"\",\""
				+user.getPassword()+"\",\""
				+email+"\",\""
				+user.getEncrypted()+"\",\""
				+date.getDate()+"\")");
			//remove user from TEMPUSERSTABLE
			st.addBatch("DELETE FROM "+TEMPUSERSTABLE
				+" WHERE "+tempusersTable.USERNAME
				+" = '"+user.getUsername()+"'");
			int[] updateCounts = st.executeBatch();
			con.setAutoCommit(true);
			if (updateCounts[0] == 1 
				&& updateCounts[1] == 1){
				//bad idea to carry password in session
				user.setPassword(null);
				return new Object[]{user,email};
			}else{
				return null;
			}
		}catch (UserException e){
			throw e;
		}catch (Exception e){
			log.writeException(e.getMessage());			
			throw new SQLException(e.getMessage());
		}
	} //end addUser
}