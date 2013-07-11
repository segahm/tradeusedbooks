package server;
import javax.servlet.http.*;
import javax.servlet.*;
import java.sql.*;
import java.io.*;
/**
 *account is a set of pages that allow the user to
 *manage his account and see account history;
 *account page does not check real authentication,
 *however, each action is checked to be legal and
 *thus no illegal action is allowed;
 *account page supprots the following:
 *update,history,books,bids,wanted,feedback
 */
public class account extends ServletParent 
	implements accoInterface,URLInterface,Tables{
	/**entering point for the application*/
	public void service(HttpServletRequest request,
                       HttpServletResponse response)
                throws ServletException{
    	try{
    		//make sure url is correct
			if (request.getPathInfo()==null 
				|| !request.getPathInfo()
				.matches("/("+OPTION1+"|"+OPTION2+"|"
				+OPTION3+"|"+OPTION4+"|"+OPTION5+"|"
				+OPTION6+"|"+OPTION7+").*")){
				response.sendRedirect(response
					.encodeRedirectURL("/"));
			}else if(!dummyAuthentication(request)){
				//if user is not authenticated send to signin
				response.sendRedirect(
					response.encodeRedirectURL(
						URLAUTHSIGNIN+"?/account"
						+request.getPathInfo()+"?"+request.getQueryString()));
			}else if(request.getPathInfo()
					.matches("/"+OPTION7)){
				//REPOST
				response.setContentType("text/html");
				repost(request,response);
			}else{
				//setting headers
				response.setContentType("text/html");
				response.setHeader("Cache-Control", "no-cache");
				response.setHeader("Expires", "0");
				response.setHeader("Pragma", "no-cache");
				ConnectionPool conPool = getConnectionPool();
				//direct requests to their appropriate 
				//functions
				if (request.getPathInfo()
					.matches("/"+OPTION1+".*")){
					//do something option1
					request.setAttribute(HEADERTITLE,
						"Account Update");
					request.setAttribute(OPTIONSATTR,
						OPTION1);
					//including header template jsp
					RequestDispatcher rd = getServletContext()
						.getRequestDispatcher(PATHACCO_TOP);
					rd.include(request,response);
					//proceeding with account update
					accountUpdate(request,response,
						conPool);	
				}
				if (request.getPathInfo()
					.matches("/"+OPTION2+".*")){
					//do something option2
					request.setAttribute(HEADERTITLE,
						"My Books");
					request.setAttribute(OPTIONSATTR,
						OPTION2);
					//including header template jsp
					RequestDispatcher rd = getServletContext()
						.getRequestDispatcher(PATHACCO_TOP);
					rd.include(request,response);
					//proceeding with books
					books(request,response,conPool);	
				}
				if (request.getPathInfo()
					.matches("/"+OPTION3+".*")){
					//do something option3
					request.setAttribute(HEADERTITLE,
						"My Bids");
					request.setAttribute(OPTIONSATTR,
						OPTION3);
					//including header template jsp
					RequestDispatcher rd = getServletContext()
						.getRequestDispatcher(PATHACCO_TOP);
					rd.include(request,response);
					//proceeding with bids
					bids(request,response,conPool);	
				}
				if (request.getPathInfo()
					.matches("/"+OPTION4+".*")){
					//do something option4
					request.setAttribute(HEADERTITLE,
						"My History");
					request.setAttribute(OPTIONSATTR,
						OPTION4);
					//including header template jsp
					RequestDispatcher rd = getServletContext()
						.getRequestDispatcher(PATHACCO_TOP);
					rd.include(request,response);
					//proceeding with history
					history(request,response,conPool);	
				}
				if (request.getPathInfo()
					.matches("/"+OPTION5+".*")){
					//do something option5
					request.setAttribute(HEADERTITLE,
						"Looking for...");
					request.setAttribute(OPTIONSATTR,
						OPTION5);
					//including header template jsp
					RequestDispatcher rd = getServletContext()
						.getRequestDispatcher(PATHACCO_TOP);
					rd.include(request,response);
					//proceeding with wanted
					wanted(request,response,conPool);	
				}
				if (request.getPathInfo()
					.matches("/"+OPTION6+".*")){
					//do something option6
					request.setAttribute(HEADERTITLE,
						"Feedback");
					request.setAttribute(OPTIONSATTR,
						OPTION6);
					//including header template jsp
					RequestDispatcher rd = getServletContext()
						.getRequestDispatcher(PATHACCO_TOP);
					rd.include(request,response);
					//proceeding with feedback
					feedback(request,response,conPool);	
				}
				//including bottom jsp
				RequestDispatcher rd = getServletContext()
					.getRequestDispatcher(PATHACCO_BOTTOM);
				rd.include(request,response);
			}
    	}catch (Exception e){
    		log.writeError(e.getMessage());
    		throw new ServletException(e);
    	}
    }
    /**account update such as password, username, email change*/
    public void accountUpdate(HttpServletRequest request,
    	HttpServletResponse response,ConnectionPool conPool) 
    	throws Exception{
    	User oldUser = (User)request.getSession()
    				.getAttribute(StringInterface.USERATTR);
    	HttpSession session = request.getSession();
    	String new_username =
    		request.getParameter(UPDATE_NEWUSERNAME);
    	String new_email =
    		request.getParameter(UPDATE_NEWEMAIL);
    	String new_pass1 =
    		request.getParameter(UPDATE_NEWPASS1);
    	String new_pass2 =
    		request.getParameter(UPDATE_NEWPASS2);
    	String old_pass =
    		request.getParameter(UPDATE_OLDPASS);
    	String hiddenID =
    		request.getParameter(SESSIONFIELDATTR);
    	boolean defaultPage = false;
    	String errormessage = null;
    	//check if first time
    	if (new_username == null || new_pass1 == null 
    		|| new_pass2 == null || old_pass == null
    		|| hiddenID == null || new_email == null){
    		defaultPage = true;
    	}else{
    		try{
    			boolean processRequest = false;
    			usersTable newUser = new usersTable();
    			newUser.setID(oldUser.getID());
    			/*
    			 *check which fields have been filled
    			 *and throw UserFormatException if one
    			 *of the fields is entered incorrectly
    			 */
    			if (new_username.length() != 0){
    				newUser.setUsername(new_username);
    				processRequest = true;
    			}
    			if (new_email.length() != 0){
    				newUser.setEmail(new_email);
    				processRequest = true;
    			}
    			if (new_pass1.length() != 0){
    				if (new_pass1.equals(new_pass2)){
    					newUser.setPassword(new_pass1);
    					processRequest = true;
    				}else{
    					throw new FormatException("the new password and the verification you entered were not the same");
    				}
    			}
    			if (processRequest){
    				//old_pass is required
    				if (old_pass.length() == 0){
    					throw new FormatException("current password was entered incorectly");
    				}
    				Connection con = conPool.getConnection();
    				try{
    					//check that such user doesn't exist
    					if (newUser.getUsername()!=null 
    						&& userExists(newUser.getUsername(),con)){
    						throw new FormatException("the username you specified is already in use by another user");
    					}
    					boolean updated =
    						changeAccount(
    							newUser,old_pass,con);
    					if (!updated){
    						throw new FormatException("current password was entered incorectly");
    					}else if (new_pass1!=null){
    						/*adding new password to the session
    						 *if it was changed
    						 */
    						oldUser.setEncrypted(userUtils.getEncrypted(new_pass1));
    						session.setAttribute(StringInterface.USERATTR,oldUser);
    					}
    				}catch (Exception e){
    					throw e;
    				}finally{
    					//recycle
    					conPool.free(con);
    					con = null;
    				}
    			}else{
    				errormessage = "no fields were specified";
    				defaultPage = true;
    			}
    		}catch (FormatException e){
    			errormessage = e.getMessage();
    			defaultPage = true;
    		}
    	}
    	if (defaultPage){
    		//passing hidden id
    		hiddenID = sqlUtils.generate(20);
			session.setAttribute(
				SESSIONFIELDATTR,hiddenID);
			//passing error message
			request.setAttribute(
				UPDATE_ERRORMESSAGE,errormessage);
			request.setAttribute(PAGEATTR,PAGE1);
			Connection con = conPool.getConnection();
			try{
				usersTable info = userUtils
    				.getUserInfo(
    			oldUser.getID(),con);
    			request.setAttribute(UPDATE_EMAILATTR,
    				info.getEmail());
    			request.setAttribute(UPDATE_USERATTR,
    				info.getUsername());
			}catch (Exception e){
				throw e;
			}finally{
				conPool.free(con);
				con = null;
			}
    	}else{
    		//remove tempid
    		session.removeAttribute(SESSIONFIELDATTR);
    		session.setAttribute(PAGEATTR,PAGE2);
    	}
		RequestDispatcher rd = getServletContext()
			.getRequestDispatcher(PATHACCO_UPDATE);
		rd.include(request,response);
    } //end accountUpdate
    /**
     *books displays books that the user is selling &
     *the books that are sold but are awaiting feedback
     */
    public void books(HttpServletRequest request,
    	HttpServletResponse response,
    	ConnectionPool conPool) throws Exception{
    	User user = (User)request.getSession()
    				.getAttribute(StringInterface.USERATTR);
    	boolean defaultPage = true;
    	ParameterParser parameter = new ParameterParser(request);
		String bookID = parameter.getString(BOOKS_BOOKID,null);
    	String error = null;//error caused by user entered data
		String sellerID = user.getID();
    	Connection con = conPool.getConnection();
    	try{
    		//determine the action
    		if (bookID!=null && bookID.length() != booksTable.ID_LENGTH){
    			defaultPage = false;
    			error = "we were unable to find the book you specified";
    		}else if (request.getPathInfo().matches(".*/edit")){
    			defaultPage = false;
    			/*
    			 *few things need to be done
    			 *1st get book information
    			 *2nd put it into postForm
    			 *3nd get postForm id and set it into session
    			 *then postForm id and page step 
    			 *need to be insert into html form
    			 */
    			 booksTable book = generalUtils.getBook(bookID,con);
    			 if (book!=null){
    			 	postForm pForm = new postForm();
  					//setting form parameters
  					pForm.setTitle(book.getTitle());
  					pForm.setAuthor(book.getAuthor());
  					pForm.setISBN(book.getISBN());
    			 	pForm.setCondition(book.getCondition()+"");
    			 	pForm.setPrice(book.getPrice()+"");
    			 	pForm.setComment(book.getComment());
    			 	pForm.setCollegeID(book.getCollegeID()+"");
    			 	pForm.setDepartmentID(book.getDepartmentID()+"");
    			 	pForm.setCourseID(book.getCourseID()+"");
    			 	pForm.setTeacherID(book.getTeacherID()+"");
    			 	
    			 	HttpSession session = request.getSession();
				 	session.setAttribute(pForm.getID(),pForm);
				 	//passing form id to jsp
				 	request.setAttribute(postBInterface.UNIQUEFORMATTR,
						pForm.getID());
					//passing book to jsp
					request.setAttribute(BOOKS_BOOKID,
						book);
				 	RequestDispatcher rd = getServletContext()
						.getRequestDispatcher(PATHACCO_BOOKS_EDIT);
				 	rd.include(request,response);
				 }else{
				 	//show bad message
    				error = "we were unable to find the book you specified";
				 }
    		}else if (request.getPathInfo().matches(".*/remove")){    			//if success - show default
    			//check book id length and remove it
    			if (!removeBook(bookID,sellerID,con)){
    				defaultPage = false;
    				//show bad message
    				error = "we were unable to remove the book you specified";
    			}
    		}
    		if (defaultPage){
    			//getting a list of active books
    			ResultSet rs1 = getBooksSelling(sellerID,con);
				/*getting a list of sold books 
				 *that are awaiting user's feedback
				 */
				ResultSet rs2 = getBooksSold(sellerID,con);
				request.setAttribute(BOOKS_SELLINGTABLE_ATTR,rs1);
				request.setAttribute(BOOKS_SOLDTABLE_ATTR,rs2);
    			RequestDispatcher rd = getServletContext()
					.getRequestDispatcher(PATHACCO_BOOKS);
				rd.include(request,response);
				rs1 = null;
				rs2 = null;
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
    /**bids shows books that the user bought but didn't
     *leave feedback for*/
    public void bids(HttpServletRequest request,
    	HttpServletResponse response,
    	ConnectionPool conPool) throws Exception{
    	User user = (User)request.getSession()
    				.getAttribute(StringInterface.USERATTR);
    	Connection con = conPool.getConnection();
    	try{
    		
    		//getting a list of bought books
    		ResultSet rs = getBooksBought(user.getID(),con);
			request.setAttribute(BOOKS_BOUGHTTABLE_ATTR,rs);
    		RequestDispatcher rd = getServletContext()
				.getRequestDispatcher(PATHACCO_BIDS);
			rd.include(request,response);
			rs = null;
    	}catch(Exception e){
    		throw e;
    	}finally{
    		conPool.free(con);
    		con = null;
    	}
    } //end bids
    /**history shows book transactions both sold and bought*/
    public void history(HttpServletRequest request,
    	HttpServletResponse response,
    	ConnectionPool conPool) throws Exception{
    	User user = (User)request.getSession()
    				.getAttribute(StringInterface.USERATTR);
    	boolean defaultPage = true;
    	ParameterParser parameter = new ParameterParser(request);
		String bookID = parameter.getString(BOOKS_BOOKID,null);
    	String error = null;//error caused by user entered data
    	Connection con = conPool.getConnection();
    	try{
    		//determine the action
    		if (bookID!=null && bookID.length() != booksTable.ID_LENGTH){
    			defaultPage = false;
    			error = "we were unable to find the book you specified";
    		}else if (request.getPathInfo().matches(".*/repost")){
    			defaultPage = false;
    			/*
    			 *few things need to be done
    			 *1st get old book information
    			 *2nd put it into postForm
    			 *3nd get postForm id and set it into session
    			 *then postForm id and page step 
    			 *need to be insert into html form
    			 */
    			 oldBooksTable oldbook = generalUtils.getOldBook(bookID,con);
    			 //check security
    			 if (oldbook == null){
    			 	error = "we were unable to find the book you specified";
    			 }else if(!user.getID().equals(oldbook.getSellerID())){
    			 	//this user is not the seller
    			 	error = "you are not authorized to access this page";
    			 }
    			 if (error==null){
    			 	postForm pForm = new postForm();
  					pForm.setTitle(oldbook.getTitle());
  					pForm.setAuthor(oldbook.getAuthor());
  					pForm.setISBN(oldbook.getISBN());
    			 	pForm.setCondition(oldbook.getCondition()+"");
    			 	pForm.setPrice(oldbook.getPrice()+"");
    			 	pForm.setComment(oldbook.getComment());
    			 	pForm.setCollegeID(oldbook.getCollegeID()+"");
    			 	
    			 	HttpSession session = request.getSession();
				 	session.setAttribute(pForm.getID(),pForm);
				 	//passing form id to jsp
				 	request.setAttribute(postBInterface.UNIQUEFORMATTR,
						pForm.getID());
				 	RequestDispatcher rd = getServletContext()
						.getRequestDispatcher(PATHACCO_HISTORY_REPOST);
				 	rd.include(request,response);
				 }
			}
    		if (defaultPage){
    			//getting a list of books in history
    			ResultSet rs = getHistory(user.getID(),con);
				request.setAttribute(BOOKS_HISTORYTABLE_ATTR,rs);
    			RequestDispatcher rd = getServletContext()
					.getRequestDispatcher(PATHACCO_HISTORY);
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
    } //end history
    /**wanted is where user can manage what books he is
     *looking for*/
    public void wanted(HttpServletRequest request,
    	HttpServletResponse response,
    	ConnectionPool conPool) throws Exception{
    	User user = (User)request.getSession()
    				.getAttribute(StringInterface.USERATTR);
    	boolean defaultPage = true;
		String bookID = request.getQueryString();
    	String error = null;//error caused by user entered data
    	Connection con = conPool.getConnection();
    	try{
    		if (request.getPathInfo().matches(".*/remove")
    			&& bookID != null){
    			defaultPage = false;
    			if (!removeWanted(bookID,user.getID(),con)){
    				error = "we were unable to remove the specified posting";
    			}else{
    				defaultPage = true;
    			}
			}
    		if (defaultPage){
    			//getting a list of books in history
    			ResultSet rs = getWanted(user.getID(),con);
				request.setAttribute(BOOKS_WANTEDTABLE_ATTR,rs);
    			RequestDispatcher rd = getServletContext()
					.getRequestDispatcher(PATHACCO_WANTED);
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
    } //end wanted
    /**feedback is the way for a user to leave feedback
     *for another user, for a certain transaction*/
   	public void feedback(HttpServletRequest request,
    	HttpServletResponse response,
    	ConnectionPool conPool) throws Exception{
    	//getting user
    	User user = (User)request.getSession()
    		.getAttribute(StringInterface.USERATTR);
    	//getting parameters
    	ParameterParser parameter = new ParameterParser(request);
    	
    	//getting user entered parameters
    	String feedback_text = parameter.getString(FEEDBACK_TEXT_FIELD,null);
    	int feedback_positiveness = parameter.getInt(FEEDBACK_RATE_FIELD,0);
    	
    	//getting server passed parameters
    	String bookID = parameter.getString(BOOKS_BOOKID,null);
    	//owner -- for whom the feedback is left
    	String feedback_ownerID = parameter.getString(BOOKS_BIDS_USERID,null);
    	//if error != null includes error page
    	String error = null;
    	if (feedback_ownerID == null 
  			|| feedback_ownerID.length() != usersTable.ID_LENGTH 
  			|| bookID == null 
    		|| bookID.length() != booksTable.ID_LENGTH){
    		error = "one of the parameters was entered incorrectly";
    	}else{
    		Connection con = conPool.getConnection();
    		try{
    			boolean defaultPage = true;
    			//error message in regards to text entered
    			String userError = null;
    			PrintWriter out = response.getWriter();
    			if (feedback_text != null){
    				defaultPage = false;
    				//if data entered do something
    				try{
    					feedbackTable feedback = new feedbackTable();
    					feedback.setFeedback(feedback_text);
    					feedback.setPositiveness(feedback_positiveness);
    					feedback.setOwnerID(feedback_ownerID);
    					feedback.setTraderID(user.getID());
    					feedback.setBookID(bookID);
    					//leave feedback
    					boolean success = leaveFeedback(feedback,con);
    					//if no rights to access this page
    					if (!success){
    						error = "you don't have permissions to leave feedback for this item";
    					}else{
    						//print success message
    						out.println();
    						out.println("You have successfully posted your feedback."
								+"<p><a class=refLinks href='"+URLFEEDBACK+"?"+feedback_ownerID+"'>"
								+"click here to see your feedback comments</a></p>");
    						out.flush();
    					}
    				}catch(FormatException e){
    					userError = e.getMessage();
    					defaultPage = true;
    				}
    			}
    			if (defaultPage && error == null){
    				//getting user information
    				usersTable owner = userUtils.getUserInfo(feedback_ownerID,con);
    				if(owner != null){	
    					//printing default
    					out.println();
    					if (userError!=null){
    						//printing friendly bad data message
							out.println("<font color='#FF0000'><b>error: </b>"+userError+"</font><br>");
    					}
    					out.println("leaving feedback for: <a style='color:#333399;' "
    						+"target=_blank href='"+URLFEEDBACK+"?"+feedback_ownerID+"'>"+owner.getUsername()+"</a>");
						out.println("<form method=post action='"+URLACCOUNTFEEDBACK+"?"+BOOKS_BOOKID+"="+bookID+"&"+BOOKS_BIDS_USERID+"="+feedback_ownerID+"'>"
							+"<center>"
								+"<select name='"+FEEDBACK_RATE_FIELD+"'>"
								+"<option value='"+feedbackTable.POSITIVE+"' style='color:#009B00'>positive</option>"
		 						+"<option value='"+feedbackTable.NEUTRAL+"' style='color:#000000'>neutral</option>"
		 						+"<option value='"+feedbackTable.NEGATIVE+"' style='color:#CC3300'>negative</option>"
		 						+"</select>"
								+"<input type=text name='"+FEEDBACK_TEXT_FIELD+"' value='' size=70 maxlength=100><br>"
								+"<input type=submit value='leave feedback' style='color: #333399;'>"
							+"</center>"
							+"</form>");
						out.flush();
					}else{
						error = "the specified user was not found in our database";
					}
					out.close();
    			}
    		}catch(Exception e){
    			throw e;
    		}finally{
    			conPool.free(con);
    			con = null;
    		}
    	}
    	if (error != null){
    		request.setAttribute(StringInterface.ERRORPAGEATTR,error);
    		RequestDispatcher rd = getServletContext()
				.getRequestDispatcher(PATHUSERERROR2);
			rd.include(request,response);
    	}
    } //end feedback
    /**removes the book and forwards the user to post*/
    private void repost(HttpServletRequest request,
    	HttpServletResponse response) throws Exception{
    	ParameterParser parameter = new ParameterParser(request);
		String bookID = parameter.getString(BOOKS_BOOKID,null);
		if (bookID.length() == booksTable.ID_LENGTH){
			User user = (User)request.getSession()
				.getAttribute(StringInterface.USERATTR);
			ConnectionPool conPool = getConnectionPool();
			Connection con = conPool.getConnection();
			try{
				if (!removeBook(bookID,user.getID(),con)){
					throw new Exception("failed to remove the book before editing");
				}
				//else simply forward
			}catch(Exception e){
				throw e;
			}finally{
				conPool.free(con);
				con = null;
			}
    		RequestDispatcher rd = getServletContext()
				.getRequestDispatcher("/servlet/server.postB");
			rd.include(request,response);
		}
    } //end repost
    //return true if user is found in session
    private boolean dummyAuthentication(
    	HttpServletRequest request){
    	//user authentication is required!
		User user = (User)request.getSession()
    			.getAttribute(StringInterface.USERATTR);
    	return (user != null);
    } //end dummyAuthentication
    /******** sql methods ************/
    private ResultSet getBooksSelling(String sellerID,
    	Connection con) throws SQLException{
    	try{
    		Statement st = con.createStatement();
    		String query = "SELECT * FROM "+TABLEBOOKS
    			+" WHERE "+booksTable.SELLERID+" = '"
    			+sellerID+"' ORDER BY "+booksTable.DATE;
    		ResultSet rs = st.executeQuery(query);
    		return rs;
    	}catch(SQLException e){
    		log.writeException(e.getMessage());
    		throw e;
    	}
    } //end getBooksSelling
    private ResultSet getBooksSold(String sellerID,
    	Connection con) throws SQLException{
    	try{
    		Statement st = con.createStatement();
    		String[] fields = new String[]{
    			TABLEOLDBOOKS+"."+oldBooksTable.TITLE,
    			TABLEBUYERS+"."+buyersTable.BOOKID,
    			TABLEBUYERS+"."+buyersTable.BUYERID,
    			USERSTABLE+"."+usersTable.USERNAME,
    			USERSTABLE+"."+usersTable.EMAIL
    		};
    		String query = "SELECT "+sqlUtils.sql_fields(fields)+" FROM "
    				+TABLEOLDBOOKS+","+TABLEBUYERS+","+USERSTABLE
    			+" LEFT JOIN "+TABLEFEEDBACK
    			+" ON "
    				+TABLEOLDBOOKS+"."+oldBooksTable.SELLERID
    				+" = "
    				+TABLEFEEDBACK+"."+feedbackTable.TRADERID
    				+" AND "
    				+TABLEFEEDBACK+"."+feedbackTable.BOOKID
    				+" = "
    				+TABLEOLDBOOKS+"."+oldBooksTable.BOOKID
    			+" WHERE ("
    				+TABLEFEEDBACK+"."+feedbackTable.TRADERID
    			+" IS NULL OR "
    				+TABLEFEEDBACK+"."+feedbackTable.BOOKID
    			+" IS NULL )"
    			+" AND "
    			/*
    			 *selecting all old books that belong 
    			 *to this user
    			 */
    				+TABLEOLDBOOKS+"."+oldBooksTable.SELLERID+" = '"+sellerID+"'"
    			+" AND "
    			/*
    			 *out of previous selection select 
    			 *books that are in buyerstable
    			 */
    				+TABLEOLDBOOKS+"."+oldBooksTable.BOOKID+" = "+TABLEBUYERS+"."+buyersTable.BOOKID
    			+" AND "
    			/*
    			 *and now connect the id of the buyer
    			 *with users table
    			 */
    				+TABLEBUYERS+"."+buyersTable.BUYERID+" = "+USERSTABLE+"."+usersTable.ID
    			+" ORDER BY "+TABLEOLDBOOKS+"."+oldBooksTable.TITLE;
    		ResultSet rs = st.executeQuery(query);
    		return rs;
    	}catch(SQLException e){
    		log.writeException(e.getMessage());
    		throw e;
    	}
    } //end getBooksSold
    private ResultSet getBooksBought(String buyerID,
    	Connection con) throws SQLException{
    	try{
    		Statement st = con.createStatement();
    		String[] fields = new String[]{
    			TABLEOLDBOOKS+"."+oldBooksTable.BOOKID,
    			TABLEOLDBOOKS+"."+oldBooksTable.TITLE,
    			TABLEOLDBOOKS+"."+oldBooksTable.PRICE,
    			USERSTABLE+"."+usersTable.ID,
    			USERSTABLE+"."+usersTable.USERNAME
    		};
    		String query = "SELECT "+sqlUtils.sql_fields(fields)
    			+" FROM "+TABLEBUYERS+","+TABLEOLDBOOKS+","+USERSTABLE
    			+" LEFT JOIN "+TABLEFEEDBACK
    			+" ON "
    				+TABLEFEEDBACK+"."+feedbackTable.TRADERID
    			+" = "
    				+TABLEBUYERS+"."+buyersTable.BUYERID
    			+" AND "
    				+TABLEFEEDBACK+"."+feedbackTable.BOOKID
    			+" = "
    				+TABLEOLDBOOKS+"."+oldBooksTable.BOOKID
    			+" WHERE ("
    				+TABLEFEEDBACK+"."+feedbackTable.TRADERID
    			+" IS NULL OR "
    				+TABLEFEEDBACK+"."+feedbackTable.BOOKID
    			+" IS NULL )"
    			+" AND "
    			//this buyer id
    				+TABLEBUYERS+"."+buyersTable.BUYERID
    			+" = '"
    				+buyerID
    			+"' AND "
    			//book id is the same ib oldbooks and buyers
    				+TABLEBUYERS+"."+buyersTable.BOOKID
    			+" = "
    				+TABLEOLDBOOKS+"."+oldBooksTable.BOOKID
    			+" AND "
    			//seller id of old books is the same as users
    				+USERSTABLE+"."+usersTable.ID
    			+" = "
    				+TABLEOLDBOOKS+"."+oldBooksTable.SELLERID
    			+" ORDER BY "+TABLEOLDBOOKS+"."+oldBooksTable.TITLE;
    		ResultSet rs = st.executeQuery(query);
    		return rs;
    	}catch(SQLException e){
    		log.writeException(e.getMessage());
    		throw e;
    	}
    } //end getBooksBought
    private boolean removeBook(String bookID,
    	String sellerID,
    	Connection con) throws SQLException{
    	try{
    		Statement st = con.createStatement();
    		//delete book where id and sellerid
    		String query = "DELETE FROM "+TABLEBOOKS
    		+" WHERE "+booksTable.ID+" = '"+bookID
    		+"' AND "+booksTable.SELLERID
    		+" = '"+sellerID+"'";
    		int resultsUpdated = st.executeUpdate(query);
    		return resultsUpdated==1;
    	}catch(SQLException e){
    		log.writeException(e.getMessage());
    		throw e;
    	}
    } //end removeBook
    /*********** sql methods *******************/
    /*
	 *requirements:new usersTable object - containing
	 *email, username, new password - (all can be null), 
	 *old password
	 *returns: true/false - updated or not
	 *false - bad authentication/no fields to updat
	 */
	private static boolean changeAccount(
		usersTable newU,
		String password,
		Connection con) throws Exception{
		try{
			Statement st = con.createStatement();
			String fields = "";
			boolean comma = false;
			if (newU.getUsername()!=null){
				fields += usersTable.USERNAME+" = '"+newU.getUsername()+"'";
				comma = true;
			}
			if (newU.getPassword()!=null){
				if (comma)
					fields += ",";
				fields += usersTable.PASSWORD+" = '"+newU.getPassword()+"',";
				fields += usersTable.EPASS+" = '"+userUtils.getEncrypted(newU.getPassword())+"'";
				comma = true;
			}
			if (newU.getEmail()!=null){
				if (comma)
					fields += ",";
				fields += usersTable.EMAIL+" = '"+newU.getEmail()+"'";
			}
			/*
			 *updates uname,pass,ePass,email
			 *if new is null it will not be replaced
			 */
			if (!fields.equals("")){
			String statement = "UPDATE "
				+USERSTABLE+" SET "+fields+" WHERE "+usersTable.ID+" = '"
				+newU.getID()+"' AND "+usersTable.PASSWORD+" = '"
				+password+"'";
				int recordUpdate = st.executeUpdate(statement);
				return (recordUpdate == 1);
			}else{
				return false;
			}
		}catch(Exception e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end changeAccount
	private boolean leaveFeedback(feedbackTable feedback,
		Connection con) throws SQLException{
		try{
			Statement st = con.createStatement();
			/*
			 *1st make sure that no feedback had been left
			 *by this user for this item
			 */
			ResultSet rs = st.executeQuery(
				"SELECT * FROM "
					+TABLEFEEDBACK
				+" WHERE "
					+TABLEFEEDBACK+"."+feedbackTable.BOOKID
				+" = '"
					+feedback.getBookID()+"' LIMIT 2");
			boolean anotherFeedback = false;
			while (rs.next()){
				if (feedback.getTraderID()
					.equals(rs.getString(
						feedbackTable.TRADERID))){
					//feedback was already left
					return false;
				}else{
					anotherFeedback = true;
				}
			}
			/*
			 *2nd make sure that the transaction 
			 *is legal i.e. both users have completed 
			 *a book trade
			 */
			String query = "SELECT * FROM "
				+TABLEBUYERS+","+TABLEOLDBOOKS+","+USERSTABLE
				+" WHERE "
					+TABLEBUYERS+"."+buyersTable.BOOKID
				+" = '"
					+feedback.getBookID()
				+"' AND "
					+TABLEBUYERS+"."+buyersTable.BOOKID
				+" = "
					+TABLEOLDBOOKS+"."+oldBooksTable.BOOKID
				+" AND "
					+USERSTABLE+"."+usersTable.ID
				+" = '"
					+feedback.getOwnerID()
				+"' AND (("
					/*checking wherever this user has
					 *an actual right to leave feedback
					 *for this transaction;
					 *either this guy is a buyer and 
					 *the other is a seller or he is
					 *a seller and the other is buyer
					 */
						+TABLEOLDBOOKS+"."+oldBooksTable.BUYERID
					+" = '"+feedback.getOwnerID()+"' AND "
						+TABLEOLDBOOKS+"."+oldBooksTable.SELLERID
					+" = '"+feedback.getTraderID()
					+"') OR ("
    					+TABLEOLDBOOKS+"."+oldBooksTable.BUYERID
					+" = '"+feedback.getTraderID()+"' AND "
						+TABLEOLDBOOKS+"."+oldBooksTable.SELLERID
					+" = '"+feedback.getOwnerID()+"')) LIMIT 1";
			rs = st.executeQuery(query);
			/*
			 *returning false if permission 
			 *is denied i.e. no record found
			 */
			String feedbackString = null;
			if (!rs.next()){
				return false;
			}else{
				feedbackString = rs.getString(USERSTABLE+"."+usersTable.FEEDBACK);
			}
			//2nd insert data into the table
			
			String[] fields = new String[]{
				TABLEFEEDBACK+"."+feedbackTable.OWNERID,
				TABLEFEEDBACK+"."+feedbackTable.TRADERID,
				TABLEFEEDBACK+"."+feedbackTable.BOOKID,
				TABLEFEEDBACK+"."+feedbackTable.POSITIVENESS,
				TABLEFEEDBACK+"."+feedbackTable.FEEDBACK,
				TABLEFEEDBACK+"."+feedbackTable.DATE
			};
			String[] values = new String[]{
				feedback.getOwnerID(),
				feedback.getTraderID(),
				feedback.getBookID(),
				feedback.getPositiveness()+"",
				feedback.getFeedback(),
				new DateObject().getDate()
			};
			con.setAutoCommit(false);
			//updating feedback table
			query = "INSERT INTO "+TABLEFEEDBACK
				+"("+sqlUtils.sql_fields(fields)
				+") values ("
				+sqlUtils.sql_values(values)+")";
			st.addBatch(query);
			//updating summary feedback for usersTable
			usersTable user = new usersTable();
			user.setFeedback(feedbackString);
			int[] tempfeedback = user.getFeedback();
			if (feedback.getPositiveness()==1){
				tempfeedback[0] = tempfeedback[0]+1;
			}else if (feedback.getPositiveness()==0){
				tempfeedback[1] = tempfeedback[1]+1;
			}else if (feedback.getPositiveness()==-1){
				tempfeedback[2] = tempfeedback[2]+1;
			}
			feedbackString = tempfeedback[0]+"/"+tempfeedback[1]+"/"+tempfeedback[2];
			query = "UPDATE "+USERSTABLE
				+" SET "
					+usersTable.FEEDBACK
				+" = '"
					+feedbackString
				+"' WHERE "
					+usersTable.ID
				+" = '"
					+feedback.getOwnerID()
				+"' LIMIT 1";
			st.addBatch(query);
			/*
			 *deleting a record from buyers table 
			 *if both users left feedback
			 */
			if (anotherFeedback){
				query = "DELETE FROM "+TABLEBUYERS
					+" WHERE "
			 			+TABLEBUYERS+"."+buyersTable.BOOKID
					+" = '"
			 			+feedback.getBookID()+"' LIMIT 1";
				st.addBatch(query);
			}
			int[] updateCounts = st.executeBatch();
			con.setAutoCommit(true);
			if (updateCounts[0] != 1 || updateCounts[1] != 1){
				throw new SQLException("failed to update feedback");
			}
			return true;	
		}catch(Exception e){
			log.writeException(e.getMessage());
			throw new SQLException(e.getMessage());
		}
	} //end leaveFeedback
	private ResultSet getHistory(String userID,
		Connection con) throws SQLException{
		try{
			Statement st = con.createStatement();
			String[] fields = {
				TABLEOLDBOOKS+".*",
				USERSTABLE+"."+usersTable.ID,
				USERSTABLE+"."+usersTable.USERNAME,
				TABLECOLLEGES+"."+collegeTable.FULLNAME
			};
			String query = "SELECT "
				+sqlUtils.sql_fields(fields)
				+" FROM "+TABLEOLDBOOKS+","+USERSTABLE+","+TABLECOLLEGES
				+" WHERE (("
					+TABLEOLDBOOKS+"."+oldBooksTable.SELLERID
				+" = '"
					+userID
				+"' AND "
					+TABLEOLDBOOKS+"."+oldBooksTable.BUYERID
				+" = "
					+USERSTABLE+"."+usersTable.ID
				+") OR ("
					+TABLEOLDBOOKS+"."+oldBooksTable.BUYERID
				+" = '"
					+userID
				+"' AND "
					+TABLEOLDBOOKS+"."+oldBooksTable.SELLERID
				+" = "
					+USERSTABLE+"."+usersTable.ID
				+")) AND "
					+TABLECOLLEGES+"."+collegeTable.ID
				+" = "
					+TABLEOLDBOOKS+"."+oldBooksTable.COLLEGE
				+" ORDER BY "
					+TABLEOLDBOOKS+"."+oldBooksTable.DATE
				+" DESC LIMIT 10";
			ResultSet rs = st.executeQuery(query);
			return rs;
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end getHistory
	private ResultSet getWanted(String userID,
		Connection con) throws SQLException{
		try{
			Statement st = con.createStatement();
			String query = "SELECT "+TABLEWANTED+".*,"
					+TABLECOLLEGES+"."+collegeTable.FULLNAME
				+" FROM "
					+TABLEWANTED+","+TABLECOLLEGES
				+" WHERE "
					+TABLEWANTED+"."+wantedTable.USERID
				+" = '"+userID+"' AND "
					+TABLECOLLEGES+"."+collegeTable.ID
				+" = "
					+TABLEWANTED+"."+wantedTable.COLLEGE;
				return st.executeQuery(query);
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end getWanted
	private boolean removeWanted(String id,String userID,
		Connection con) throws SQLException{
		try{
			Statement st = con.createStatement();
			String query = "DELETE FROM "+TABLEWANTED
				+" WHERE "+wantedTable.ID
				+" = '"
					+id
				+"' AND "
					+wantedTable.USERID
				+" = '"
					+userID
				+"' LIMIT 1";
			int resultsUpdated = st.executeUpdate(query);
			return resultsUpdated == 1;
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	}
	private boolean userExists(String username, 
		Connection con) throws SQLException{
		try{
    		Statement st = con.createStatement();
    		ResultSet rs = st.executeQuery("SELECT "
				+usersTable.USERNAME+" FROM "
				+USERSTABLE+" WHERE "
				+usersTable.USERNAME+" = '"
				+username+"' LIMIT 1");
			if (rs.next()){
				return true;
			}else{
				return false;
			}
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end userExists
}