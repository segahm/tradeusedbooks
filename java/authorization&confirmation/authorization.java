package server;
import javax.servlet.http.*;
import javax.servlet.*;
import java.sql.*;
/**
 *serves as an entering authorization point for
 *pages requiring authontication;
 *it be default redirects to the home page unless
 *the forwarder provides its path in query string
 */
public class authorization extends ServletParent 
	implements authInterface,URLInterface,Tables{
	private final String PATHEMAILVERIFICATION
		= "/jsp/mail/emailVerification.jsp";
	/**entering point for the application*/
	public void service(HttpServletRequest request,
                       HttpServletResponse response)
                throws ServletException{
    	try{
			//make sure url is correct
			if (request.getPathInfo()==null
				|| !request.getPathInfo()
				.matches("/("+OPTION1+"|"+OPTION2+"|"
				+OPTION3+"|"+OPTION4+").*")
				){
				response.sendRedirect(response
				.encodeRedirectURL("/"));
			}else{
				HttpSession session = request
					.getSession();
				//url we came from
				String fromURL = request.getQueryString();
				boolean forward = false;
				//do certain actions and then forward to
				//authorization.jsp
				//option1 fowards & redirects
				if (request.getPathInfo()
					.matches("/"+OPTION1+".*")){
					//do something for signin
					request.setAttribute(HEADERTITLE,"Sign In");
					request.setAttribute(OPTIONSATTR,OPTION1);
					forward = signIn(request);
				}
				//option2 always forwards
				if (request.getPathInfo()
					.matches("/"+OPTION2+".*")){
					//do something for register
					request.setAttribute(HEADERTITLE,
						"Registration");
					request.setAttribute(OPTIONSATTR,
						OPTION2);
					forward = register(request,response);
				}
				//option3 always forwards
				if (request.getPathInfo()
					.matches("/"+OPTION3+".*")){
					//do something for forgot
					request.setAttribute(HEADERTITLE,
						"Account Assistance");
					request.setAttribute(OPTIONSATTR,
						OPTION3);
					forward = forgot(request);
				}
				//option4 always redirects
				if (request.getPathInfo()
						.matches("/"+OPTION4+".*")){
					/*invalidate session and redirect
					 *back to where we came from
					 */
					try{
						session.invalidate();
					}catch(IllegalStateException ignore){
					}
				}
				if (forward){
					request.setAttribute(QUERYATTR,fromURL);
					RequestDispatcher rd = getServletContext()
						.getRequestDispatcher(
								PATHAUTHORIZATION);
					rd.forward(request,response);
				}else{
					//send redirect usign encode
					//to allow sessionid passed using 
					//url server rewrite
					if (fromURL != null){
						response.sendRedirect(
							response.encodeRedirectURL(fromURL));
					}else{
						response.sendRedirect(
							response.encodeRedirectURL(
								"/"));
					}
				}
			} //end else
		}catch (Exception e){
			//actual stack trace is expected to be written
			//by exception thrower 
			throw new ServletException(e);
		}                 	
	} //end service
	/**shows either default page with or without errors
	 *or adds user to session and return forward request*/
	private boolean signIn(HttpServletRequest request) throws Exception{
		HttpSession session = request.getSession();
		//username field
		String username = request.getParameter(USERFIELD);
		//password field
		String password = request.getParameter(PASSFIELD);
		/*
		 *how to implement session check:
		 *first generate sessionID and add it to form and session object
		 *then upon authorization check if the two are the same
		 *and if yes then remove the session attribute 
		 *and authenticate the user;
		 *there is a more secure way however,
		 *add sessionID to servletContext with unique id instead of
		 *adding it to session object
		 *(problem with out of memory might arise!)
		 */
		String hiddenSession = request
			.getParameter(SESSIONFIELDATTR);
		//error messages to be displayed
		String userError = null;
		String passError = null;
		boolean defaultPage = false;
		//default if one of the inputs is null
		if (username == null 
			|| password == null 
			|| hiddenSession == null
			|| !hiddenSession.equals(session
				.getAttribute(SESSIONFIELDATTR))){
			defaultPage = true;
		}else{
			if (username.equals("")){
				defaultPage = true;
				userError = "no username entered";
			}
			if (password.equals("")){
				defaultPage = true;
				passError = "no password entered";
			}
			//if no errors, check username and pass
			if (defaultPage == false){
				ConnectionPool conPool 
					= getConnectionPool();
				Connection connection = 
					conPool.getConnection();
				/*
				 *try/catch to make sure we
				 *recyle connection
				 */
				try{
					/*confirming user&pass 
					 *combination and getting
					 *User(encrypted,username,id)
					 *object back
					 */
					User user = confirmUser(username,
						password,connection);
					if (user != null){
						/*creating a new
						 *session with timeout
						 *in seconds
						 */
						session.
							setMaxInactiveInterval(
								StringInterface.MAXINACTIVETIME);
						session.setAttribute(
							StringInterface.USERATTR,
									user);
					}else{
						defaultPage = true;
						//set error message
						passError = "bad password";
					}
				}catch (Exception e){
					throw e;
				}
				finally{
					//finished with database - recycle
					conPool.free(connection);
					connection = null;
				}
			}
		}
		if (defaultPage){
			//create a hidden session id
			hiddenSession = sqlUtils.generate(20);
			//loadding hidden sessionid to session
			//for future security check and
			//for <input type=hidden field
			session.setAttribute(SESSIONFIELDATTR,
				hiddenSession);
			request.setAttribute(SESSIONFIELDATTR,
				hiddenSession);
			//setting error messages(even if null)
			request.setAttribute(ERRORMESSAGE1ATTR,
				userError);
			request.setAttribute(ERRORMESSAGE2ATTR,
				passError);
			return true;//forward request
		}else{
			//getting rid of form security check
			session.removeAttribute(SESSIONFIELDATTR);
			return false;//no forwarding
		}
	} //end signIn
	/**register either displays default registration page
	 *(with or without errors) or displays a notice
	 *of steps that need to be taken*/
	private boolean register(HttpServletRequest request,
		HttpServletResponse response) throws Exception{
		HttpSession session = request.getSession();
		String username = request
			.getParameter(USERFIELD);
		String email = request
			.getParameter(EMAILFIELD);
		String hiddenSession = request
			.getParameter(SESSIONFIELDATTR);
		/*errors to be printed in case of default page
		 *errors should be left null if no errors 
		 */
		String userError = null;
		String emailError = null;
		boolean defaultPage = false;
		//check wherever first time accessing page
		if (username == null 
			|| email == null
			|| hiddenSession == null
			|| !hiddenSession.equals(
			 session.getAttribute(SESSIONFIELDATTR))){
			defaultPage = true;
			//set values to empty for field insertion
			username = "";
			email = "";
		}else{
			//checking for format of data entered
			tempusersTable user = new tempusersTable();
			try{
				user.setUsername(username);
			}catch(FormatException e){
				userError = e.getMessage();
				defaultPage = true;
			}
			try{
				user.setEmail(email);
			}catch(FormatException e){
				emailError = e.getMessage();
				defaultPage = true;
			}
			//if no errors check if username exists
			if (!defaultPage){
				ConnectionPool conPool 
					= getConnectionPool();
				Connection connection 
					= conPool.getConnection();
				try{
					try{
						user.setTempPass(sqlUtils.generate(usersTable.PASSWORD_MAX));
						/*add user to temp table
						 *for further verification
						 */
						addTempUser(user,connection);
						/*
						 *send username,password,email
						 *to included jsp
						 */
						//sending mail
						request.setAttribute(
							mailInterface.USERATTR,
							user.getUsername());
						request.setAttribute(
							mailInterface.PASSATTR,
							user.getTempPass());
						request.setAttribute(
							mailInterface.EMAILATTR,
							user.getEmail());
						RequestDispatcher rd = 
							getServletContext()
							.getRequestDispatcher(
								PATHEMAILVERIFICATION);
						rd.include(request,response);
						//clearing request from mail attr.
						request.removeAttribute(
							mailInterface.USERATTR);
						request.removeAttribute(
							mailInterface.PASSATTR);
						request.removeAttribute(
							mailInterface.EMAILATTR);
					}catch (UserException e){
						defaultPage = true;
						if (e.getField().equals(
							FieldInterface.EMAILFIELD)){
							emailError = e.getMessage();
						}else{
							userError = e.getMessage();
						}
					}
				}catch (Exception e){
					throw e;
				}
				finally{
					//finished with database - recycle
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
			//setting error messages(even if null)
			request.setAttribute(ERRORMESSAGE1ATTR,
				userError);
			request.setAttribute(ERRORMESSAGE2ATTR,
				emailError);
			request.setAttribute(PAGEATTR,
				PAGE1);
			if (username == null){
				username = "";
			}
			if (email == null){
				email = "";
			}
			request.setAttribute(USERFIELD,
				username);
			request.setAttribute(EMAILFIELD,
				email);
		}else{
			//get rid of temp. session attribute
			session.removeAttribute(SESSIONFIELDATTR);
			request.setAttribute(PAGEATTR,
				PAGE2);
		}
		return true;//forward request
	} //end register
	/**provides a way for the user to retrieve either a
	 *username or a password using his email address*/
	private boolean forgot(HttpServletRequest request) 
		throws Exception{
		boolean uRequested = false;//wherever username/password is requested
		String requestedString;//username/password
		String assist_q = request.getQueryString();//p or u
		//only ?u or ?p are allowed
		if (assist_q.matches("u")){
			uRequested = true;
			requestedString = "username";
		}else{
			uRequested = false;
			requestedString = "password";
		}
		String email = request.getParameter(
			authInterface.EMAILFIELD);
		//message == null will NOT show message
		String message = null;
		if (email != null){
			try{
				usersTable user = new usersTable();
				user.setEmail(email);//throws UserFormatException
				ConnectionPool conPool 
					= getConnectionPool();
				Connection connection = conPool.getConnection();
				try{
					if (retrieveUser(user,uRequested,connection)){
						message = "your "+requestedString+" was sent to "+ user.getEmail();
					}else{
						message = user.getEmail()+" wasn't found in our database";
					}
				}catch (Exception e){
					throw e;
				}finally{
					conPool.free(connection);
					connection = null;
				}
			}catch (FormatException e){
				message = e.getMessage();
			}
		}
		request.setAttribute(ERRORMESSAGE1ATTR,message);
		return true;
	} //end forgot
	/**
	 *add tempusersTable(username,password,email) and date to
	 *temporary users table
	 *returns: true/false
	 * or UserException(if bad names are already used)
	 *requires:username,password,email,Connection
	 */
	private static boolean addTempUser(tempusersTable user,
		Connection con) throws UserException, 
		SQLException{
		try{
			Statement st = con.createStatement();
			ResultSet rsTemp = null;
			/*****authentication check begins******/
			//check USERSTABLE for this username
			rsTemp = st.executeQuery("SELECT "
				+usersTable.USERNAME+" FROM "
				+USERSTABLE+" WHERE "
				+usersTable.USERNAME+" = '"
				+user.getUsername()+"'");
			if(rsTemp.next()){
        		throw new UserException("username \""
        		+user.getUsername()+"\" already exists"
        		,FieldInterface.USERFIELD);
        	}
        	//check TEMPUSERSTABLE for this username
        	rsTemp = st.executeQuery("SELECT "+
        	tempusersTable.USERNAME+" FROM "
        	+TEMPUSERSTABLE+" WHERE "
        	+tempusersTable.USERNAME+" = '"
        	+user.getUsername()+"'");
        	if(rsTemp.next()){
        		throw new UserException("username \""
        		+user.getUsername()+"\" already exists"
        		,FieldInterface.USERFIELD);
			}
			//check USERSTABLE for this email
			rsTemp = st.executeQuery("SELECT "
			+usersTable.EMAIL+" FROM "
			+USERSTABLE+" WHERE "+usersTable.EMAIL
			+" = '"+user.getEmail()+"'");
			if(rsTemp.next())
        		throw new UserException("email \""
        		+user.getEmail()
        		+"\" is already used by someone else"
        		,FieldInterface.EMAILFIELD);
			
			//check TEMPUSERSTABLE for this email
			rsTemp = st.executeQuery("SELECT "
			+tempusersTable.EMAIL+" FROM "
			+TEMPUSERSTABLE+" WHERE "
			+tempusersTable.EMAIL+" = '"
			+user.getEmail()+"'");
			if(rsTemp.next())
        		throw new UserException("email \""
        		+user.getEmail()
        		+"\" is already used by someone else"
        		,FieldInterface.EMAILFIELD);
			/*****authentication checks ends*******/
			//if still no exception proceed with adding
			int recordsUpdated;
			
			//date is of the following format: int(1-12)/int(1-31)/int(year)
			DateObject date = new DateObject();
			//adds user using passed username and password parameters and adds a date
			recordsUpdated = st.executeUpdate(
				"INSERT INTO "
				+TEMPUSERSTABLE+" ("
				+tempusersTable.USERNAME
				+","+tempusersTable.PASS+","
				+tempusersTable.EMAIL
				+","+tempusersTable.DATE+") values (\""
				+user.getUsername()+"\",\""
				+user.getTempPass()+"\",\""
				+user.getEmail()+"\",\""+date.getDate()
				+"\")");
			return (recordsUpdated == 1);
		}catch (UserException e){
			throw e;
		}catch (Exception e){
			log.writeException(e.getMessage());			
			throw new SQLException(e.getMessage());
		}	
	} //end addTempUser
	/**
	 *sends username/password of the specified email
	 *returns: true if email found,false if email not found 
	 *requires: email,username requested?,Connection
	 */
	private static boolean retrieveUser(usersTable user,boolean u,
		Connection con) throws SQLException{
		try{
			Statement st = con.createStatement();
			ResultSet rsTemp = null;
			rsTemp = st.executeQuery("SELECT "
				+usersTable.USERNAME+","
				+usersTable.PASSWORD
				+" FROM "+USERSTABLE+" WHERE "
				+usersTable.EMAIL+" = '"
				+user.getEmail()+"'");
			String data = null;
			//check what's requested (username/password)
			String requested;
			String notRequested;
			if (rsTemp.next()){
				if (u){
					data = rsTemp.getString(1);
					requested = "username";
					notRequested = "password";
				}else{
					data = rsTemp.getString(2);
					requested = "password";
					notRequested = "username";
				}
			}else{
				return false;
			}
			String subject = requested+" retrieval service requested";
			//writing message
			String message = "";
			message += "<html><body>";
			message += "<p>You received this email because you recently requested your "+requested+" to be ";
			message += "send to you.</p>";
			message += "<p>your "+requested.toUpperCase()+" is: "+data+"</p>";
			message += "<p>Note:  for security purposes we did not email your "+notRequested+" to you.</p>";
			message += "<p>Again, thank you for using tradeusedbooks.com!</p>";
			message += "<p>tradeusedbooks.com team</p>";
			message += "<p><font size='2'>If you think you received this email by mistake (meaning you never visited <a href='http://www.tradeusedbooks.com'>http://www.tradeusedbooks.com</a> ";
			message += "and never signed up for any of our services) please email us at: ";
			message += "<a href='mailto:support@tradeusedbooks.com?subject=remove mistake'>support@tradeusedbooks.com</a>.</font></p>";
			message += "</body></html>";
			SendMail mail = new SendMail(user.getEmail(),true);
			mail.send(message,subject);
			return true;
		}catch (Exception e){
			log.writeException(e.getMessage());			
			throw new SQLException(e.getMessage());
		}
	} //end retrieveUser
	/**
	 *confirm user
	 *relative to registered users table
	 *returns: User(username,encryptedpass,id)/null
	 *requires:username,password,Connection
	 */
	private static User confirmUser(String u,String p,
		Connection con) throws SQLException{
		try{
			//do not query empy values
			if (u == null || p == null 
			|| u.equals("") || p.equals("")){
				return null;
			}
			Statement st = con.createStatement();
			ResultSet rsTemp = null;
			//select id,encrypted where u = u, p = p
			rsTemp = st.executeQuery("SELECT "
				+usersTable.ID+","+usersTable.EPASS
				+" FROM "+USERSTABLE+" WHERE "
				+usersTable.USERNAME+" = '"+u
				+"' AND "+usersTable.PASSWORD+" = '"+p+"' LIMIT 1");
        	//if found return
        	if (rsTemp.next()){
        		User user = new User();
        		user.setUsername(u); //username
        		user.setEncrypted(rsTemp.getString(
        			usersTable.EPASS)); //encrypedpass
        		user.setID(rsTemp.getString(
        			usersTable.ID)); //id
        		//update last login
        		userUtils.updateDate(user.getID(),con);
        		return user;
        	}else{
        		return null;
        	}
		}catch (Exception e){
			log.writeException(e.getMessage());
			//simplify life by returning SQLException
			throw new SQLException(e.getMessage());
		}
	} //end confirmUser
}
