package server;
import javax.servlet.http.*;
import javax.servlet.*;
import java.sql.*;
/*
 */
public class postB extends ServletParent 
	implements postBInterface,URLInterface{
	private final String PATHBOOKCONFIRMATION
		= "/jsp/mail/bookConfirmation.jsp";
	public void service(HttpServletRequest request,
                       HttpServletResponse response)
                throws ServletException{
    	try{
    		/*
    		 *set no cache in different ways
    		 *this should be repeated in forwarded jsp
    		 *since all headers are cleared when forwarding
    		 */
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Expires", "0");
			response.setHeader("Pragma", "no-cache");
			/*
			 *getting unique form identifier,
			 *it first of all lets us identify each form
			 *uniquely so that one session for the same
			 *user can use different forms simultaneously
			 *it also allows us to forward all invalid
			 *requests to step1
			 */
			String formIdentifier = request
					.getParameter(UNIQUEFORMATTR);
			String pageCameFrom = request
					.getParameter(PAGESTEP);
			HttpSession session = request.getSession();
			//make sure the user is authenticatd
			if(!dummyAuthentication(request)){
				/*redirect to signin with our url
				 *as a parameter
				 */
				response.sendRedirect(response.
					encodeRedirectURL(URLAUTHSIGNIN+"?"
					+URLPBOOKS));
			/*
			 *check wherever unique form identifier was 
			 *already created and put into session
			 *and page we came from is valid
			 */
			}else if (formIdentifier == null
					|| session.getAttribute(
						formIdentifier) == null
					|| pageCameFrom == null
					|| !pageCameFrom.matches("[1-5]")){
				/*
				 *create unique id for the form
				 *and forward the request to step1 jsp
				 */
				postForm p = new postForm();
				session.setAttribute(p.getID(),p);
				//passing form id to jsp
				request.setAttribute(UNIQUEFORMATTR,
					p.getID());
				RequestDispatcher rd 
					= getServletContext()
					.getRequestDispatcher(PATHPBOOKS1);
				rd.forward(request,response);		
			}else{
				//passing unique form id to jsps
				request.setAttribute(UNIQUEFORMATTR,
					formIdentifier);
				/*
				 *process the request based on the page 
				 *we came from
				 */
				 if (pageCameFrom.equals("1")){
				 	step1(request,response);
				 }
				 if (pageCameFrom.equals("2")){
				 	step2(request,response);
				 }
				 if (pageCameFrom.equals("3")){
				 	//forward one step back if asked
				 	if (request.getQueryString()!=null && request.getQueryString().matches("back")){
    						RequestDispatcher rd = getServletContext()
								.getRequestDispatcher(PATHPBOOKS2);
									rd.forward(request,response);
    				}else{
				 		step3(request,response);
				 	}
				 }
				 if (pageCameFrom.equals("4")){
				 	//forward one step back if asked
				 	if (request.getQueryString()!=null && request.getQueryString().matches("back")){
    						RequestDispatcher rd = getServletContext()
								.getRequestDispatcher(PATHPBOOKS3);
									rd.forward(request,response);
    				}else{
				 		step4(request,response);
				 	}
				 }
				 if (pageCameFrom.equals("5")){
				 	//forward one step back if asked
				 	if (request.getQueryString()!=null && request.getQueryString().matches("back")){
    						RequestDispatcher rd = getServletContext()
								.getRequestDispatcher(PATHPBOOKS2);
									rd.forward(request,response);
    				}else{
				 		step5(request,response);
				 	}
				 }
			}
    	}catch(Exception e){
    		log.writeError(e.getMessage());
    		throw new ServletException(e);
    	}
    } //end service
    /*
     *all step[n] methods are specifically called when
     *requests were made from their pages
     */
     /*step1:
      *entry point, enters either isbn or title
      */
    private void step1(HttpServletRequest request,
    	HttpServletResponse response) throws Exception{
    	//fields != null always
    	String field_title = tools.htmlDecode(request.getParameter(TITLEFIELD));
    	String field_isbn = tools.htmlDecode(request.getParameter(ISBNFIELD));
    	/*
    	 *radio button that lets us know wherever to 
    	 *check isbn or title
    	 */
    	String field_radio = request.getParameter("r"); 
    	boolean nextStep = true;
    	String errorMessage = null;
    	postForm formTest = new postForm(); //will only be used to test
    	//for format
    	try{
    		//check if radio button was selected
    		if (field_radio == null){
    			throw new FormatException(
    				"option was not selected");
    		}
    		if (field_radio.equals(TITLEFIELD)){
    			formTest.setTitle(field_title);
    			field_isbn = null; //disregard the other
    		}else if (field_radio.equals(ISBNFIELD)){
    			if (field_isbn == null || field_isbn.length()==0){
    				throw new FormatException("isbn field is empty");
    			}
    			formTest.setISBN(field_isbn);
    			field_title = null;
    		}else{
    			throw new FormatException(
    				"bad option selected");
    		}
    	}catch(FormatException e){
    		nextStep = false;
    		errorMessage = e.getMessage();
    	}
    	if (nextStep){
    		//get book info from our isbn database
    		ConnectionPool conPool 
    			= getConnectionPool();
    		Connection con = conPool.getConnection();
    		isbnTable table = null;
    		try{
    			String query = null;
    			String field = null;
    			//check which option wasn't specified
    			if (formTest.getISBN() != null){
    				query = formTest.getISBN();
    				field = isbnTable.ISBN;
    			}else if (formTest.getTitle() != null){
    				query = formTest.getTitle();
    				field = isbnTable.TITLE;
    			}
    			//get book info using specified field
    			table = postUtils.getBookInfo(query,
    				field,con);
    		}catch (Exception e){
    			throw e;
    		}finally{
    			//recycle
    			conPool.free(con);
    			con = null;
    		}
    		if (table != null){
    			//show to use what we found in our database
    			request.setAttribute(TITLEFIELD,
    				table.getTitle());
    			request.setAttribute(AUTHORFIELD,
    				table.getAuthor());
    			request.setAttribute(ISBNFIELD,
    				table.getISBN());
    			//requests' attributes may be null
    		}else{
    			//simply pass what user entered
    			//O.K. if null
    			request.setAttribute(TITLEFIELD,
    				field_title);
    			request.setAttribute(ISBNFIELD,
    				field_isbn);
    		}
    		//forward request to the next page
    		RequestDispatcher rd 
					= getServletContext()
					.getRequestDispatcher(PATHPBOOKS2);
			rd.forward(request,response);
    	}else{
    		//pass bad title&isbn
    		request.setAttribute(TITLEFIELD,
    			field_title);
    		request.setAttribute(ISBNFIELD,
    			field_isbn);
    		request.setAttribute(ERRORMESSAGE,
    			errorMessage);
    		//forward request back to original page
    		RequestDispatcher rd 
					= getServletContext()
					.getRequestDispatcher(PATHPBOOKS1);
			rd.forward(request,response);
    	}
    } //end step1
    /*step2:
     *enters title,author,isbn
     */
    private void step2(HttpServletRequest request,
    	HttpServletResponse response) throws Exception{
    	HttpSession session = request.getSession();
    	//getting form object from session
    	String formID = (String)request.getAttribute(UNIQUEFORMATTR);
    	postForm form = (postForm)session.getAttribute(formID);
    	//getting fields from submited request
    	String field_title 
    		= tools.htmlDecode(request.getParameter(TITLEFIELD));
    	String field_isbn 
    		= tools.htmlDecode(request.getParameter(ISBNFIELD));
    	String field_author 
    		= tools.htmlDecode(request.getParameter(AUTHORFIELD));
    	boolean nextPage = true;
    	String errorMessage = null;
    	if (field_title == null || field_isbn == null
    		|| field_author == null){
    		nextPage = false;
    	}else{
    		try{
    			form.setTitle(field_title);
    			form.setAuthor(field_author);
    			//isbn can be empty
    			form.setISBN(field_isbn);
    		}catch(FormatException e){
    			errorMessage = e.getMessage();
    			nextPage = false;
    		}
    	}
    	if (nextPage){
    		//add form to session
    		session.setAttribute(form.getID(),form);
    		//forward request to the next page
    		RequestDispatcher rd 
					= getServletContext()
					.getRequestDispatcher(PATHPBOOKS3);
			rd.forward(request,response);
    	}else{
    		//forward back to this page
    		//set error message
    		request.setAttribute(ERRORMESSAGE,
    			errorMessage);
    		//O.K. if attributes are null
    		request.setAttribute(TITLEFIELD,field_title);
    		request.setAttribute(AUTHORFIELD,field_author);
    		request.setAttribute(ISBNFIELD,field_isbn);
    		RequestDispatcher rd 
					= getServletContext()
					.getRequestDispatcher(PATHPBOOKS2);
			rd.forward(request,response);
    	}
    } //end step2
    /*step3:
     *enters comments,price,condition
     */
    private void step3(HttpServletRequest request,
    	HttpServletResponse response) throws Exception{
    	HttpSession session = request.getSession();
    	//getting form object from session
    	String formID = (String)request.getAttribute(UNIQUEFORMATTR);
    	postForm form = (postForm)session.getAttribute(formID);
		//getting parameters from request
		//optional comments
		String comment = tools.htmlDecode(
			request.getParameter(COMMENTSFIELD));
    	//required fields
    	String price = request.getParameter(PRICEFIELD);
    	String condition = request.getParameter(
    		CONDITIONFIELD);
    	boolean nextPage = true;
    	String errorMessage = null;
    	//if first time
    	if (price == null || condition == null
    		|| comment == null){
    		nextPage = false;
    	}else{
    		try{
    			form.setPrice(price);
    			form.setCondition(condition);
    			//comment can be empty
    			form.setComment(comment);
    		}catch(FormatException e){
    			errorMessage = e.getMessage();
    			nextPage = false;
    		}
    	}
    	if (nextPage){
    		//add form to session
    		session.setAttribute(form.getID(),form);
    		//forward request to the next page
    		RequestDispatcher rd 
					= getServletContext()
					.getRequestDispatcher(PATHPBOOKS4);
			rd.forward(request,response);
    	}else{
    		//forward back to this page
    		//set error message
    		request.setAttribute(ERRORMESSAGE,
    			errorMessage);
    		RequestDispatcher rd 
					= getServletContext()
					.getRequestDispatcher(PATHPBOOKS3);
			rd.forward(request,response);
    	}
    } //end step3
    /*step4:
     *selects college,teacher,department,course
     */
    private void step4(HttpServletRequest request,
    	HttpServletResponse response) throws Exception{
    	HttpSession session = request.getSession();
    	//getting form object from session
    	String formID = (String)request.getAttribute(
    		UNIQUEFORMATTR);
    	postForm form = (postForm)session.getAttribute(
    		formID);
    	//getting select values
    	String college_id = request.getParameter(
    		COLLEGEIDFIELD);
    	String department_id = request.getParameter(
    		DEPARTMENTIDFIELD);
    	String course_id = request.getParameter(
    		COURSEIDFIELD);
    	String teacher_id = request.getParameter(
    		TEACHERIDFIELD);
    	//getting input values
    	String college_short = request.getParameter(
    		COLLEGESHORT);
    	String college_full = request.getParameter(
    		COLLEGEFULL);
    	String department_custom = request.getParameter(
    		DEPARTMENTCUSTOM);
    	String course_custom = request.getParameter(
    		COURSECUSTOM);
    	String teacher_custom = request.getParameter(
    		TEACHERCUSTOM);
    	boolean nextPage = false;
    	String errorMessage = null;
    	/*
    	 *check if review was requested
    	 *
    	 */
    	if (college_short != null && college_full != null
    		&& request.getQueryString()!= null
    		&& request.getQueryString().equals("review")){
    		//check for errors and send to preview
    		nextPage = true;//default if no errors
    		try{
    			/*assign all values, null and empty strings
    			 *are allowed, the existence check will
    			 *only be done for college, for everything
    			 *else only format check
    			 */
    			form.setCollegeID(college_id);
    			form.setDepartmentID(department_id);
    			form.setCourseID(course_id);
    			form.setTeacherID(teacher_id);
    			form.setCollegeNames(college_short,
    				college_full);
    			form.setDepartment(department_custom);
    			form.setCourse(course_custom);
    			form.setTeacher(teacher_custom);
    			boolean customCollege = 
    				(form.getCollegeShort()!=null 
    				&& form.getCollegeFull()!=null);
    			
    			if (form.getCollegeID()==0 
    				&& !customCollege){
    				throw new FormatException(
    					"college needs to be specified");
    			}
    			/*
    			 *finalize form - form deletes values that
    			 *are absolete relative to other fields
    			 */
    			form.finalize();
    			
    		}catch(FormatException e){
    			errorMessage = e.getMessage();
    			nextPage = false;
    		}
    	}
    	//forwarding request
    	if (nextPage){
    		request = review(request,form);
    		session.setAttribute(form.getID(),form);
    		RequestDispatcher rd 
				= getServletContext().getRequestDispatcher(PATHPBOOKS5);
			rd.forward(request,response);
    	}else{
    		//forward back to this page
    		//set error message
    		request.setAttribute(ERRORMESSAGE,
    			errorMessage);
    		RequestDispatcher rd 
					= getServletContext()
					.getRequestDispatcher(PATHPBOOKS4);
			rd.forward(request,response);
    	}
    } //end step4
    /*step5:
     *simply show al lthe date for review and post it
     *on submit
     */
    private void step5(HttpServletRequest request,
    	HttpServletResponse response) throws Exception{
    	HttpSession session = request.getSession();
    	User user = (User)session.getAttribute(
    		StringInterface.USERATTR);
    	//getting form object from session
    	String formID = (String)request.getAttribute(
    		UNIQUEFORMATTR);
    	postForm form = (postForm)session.getAttribute(
    		formID);
    	ConnectionPool conPool = getConnectionPool();
    	Connection con = conPool.getConnection();
    	try{
    		form.finalize();
    		postUtils post = new postUtils(con);
    		post.postForm(form,user);
    		if (form.getISBN()!=null){
    			storeISBN addisbn = new storeISBN(form.getISBN(),conPool);
    			addisbn.start();
    		}
    		//remove session form and if
			session.removeAttribute(form.getID());
			request.removeAttribute(UNIQUEFORMATTR);
    		//send paramters to mail jsp
    		request = review(request,form);
    		request.setAttribute(mailInterface.USERATTR,
    			user.getUsername());
    		request.setAttribute(mailInterface.EMAILATTR,
    			userUtils.getUserInfo(user.getID(),con).getEmail());
    		//sending mail
    		RequestDispatcher rd 
					= getServletContext()
					.getRequestDispatcher(PATHBOOKCONFIRMATION);
			rd.include(request,response);
    		rd = getServletContext()
					.getRequestDispatcher(PATHPBOOKS6);
			rd.forward(request,response);
    	}catch(UserException e){
    		response.sendError(response.SC_FORBIDDEN);
    	}catch (Exception e){
    		throw e;
    	}finally{
    		//recycle
    		conPool.free(con);
    		con = null;
    	}
    	
    } //end step5
    private HttpServletRequest review(
    	HttpServletRequest request, postForm form) 
    	throws Exception{
   		String review_college;
		String review_teacher = "not specified";
		String review_course = "not specified";
		String review_department = "not specified";
		String review_isbn = "none";
		String review_comment = "nothing entered";
    	request.setAttribute(postBInterface.TITLEFIELD,
    		form.getTitle());
		request.setAttribute(postBInterface.AUTHORFIELD,
			form.getAuthor());
		if (form.getISBN()!=null){
			review_isbn = form.getISBN();
		}
		if (form.getComment()!=null){
			review_comment = form.getComment();
		}
		if (form.getCollegeID()!=0){
			ConnectionPool conPool = getConnectionPool();
			Connection con = conPool.getConnection();
			try{
				//setting college name
				generalUtils sql = new generalUtils(con);
				review_college = sql.getCollege(
					form.getCollegeID()+"").getFull();
				//setting department name
				if (form.getDepartmentID()!=0){
					review_department = sql
						.getDepartment(form.getDepartmentID()+"")
							.getName();
					//setting course
					if (form.getCourseID()!=0){
						review_course = sql.getCourse(
							form.getCourseID()+"")
								.getName();
					}else if(form.getCourse()!=null){
						review_course = form.getCourse();
					}
				}else{
					if (form.getDepartment() != null){
						review_department = form.getDepartment();
						if (form.getCourse()!=null){
							review_course = form.getCourse();
						}
					}
				}
				//setting teacher
				if (form.getTeacherID()!=0){					review_teacher = sql.getTeacher(
						form.getTeacherID()+"")
						.getName();
				}else if(form.getTeacher()!=null){
					review_teacher = form.getTeacher();
				}
			}catch(Exception e){
				throw e;
			}finally{
				conPool.free(con);
				con = null;
			}
		}else{
			//if customly entered
			review_college = form.getCollegeFull();
			if (form.getDepartment() != null){
				review_department = form.getDepartment();
				if (form.getCourse()!=null){
					review_course = form.getCourse();
				}
			}
			if (form.getTeacher()!=null){
				review_teacher = form.getTeacher();
			}
		}
		request.setAttribute(
			postBInterface.ISBNFIELD,
			review_isbn);
		request.setAttribute(
			postBInterface.COMMENTSFIELD,
			review_comment);
		request.setAttribute(
			postBInterface.CONDITIONFIELD,
			booksTable.CONDITIONS[form.getCondition()]);
		request.setAttribute(
			postBInterface.PRICEFIELD,
			form.getPrice()+"");
		request.setAttribute(
			postBInterface.COLLEGEIDFIELD,
			review_college);
		request.setAttribute(
			postBInterface.DEPARTMENTIDFIELD,
			review_department);
		request.setAttribute(
			postBInterface.TEACHERIDFIELD,
			review_teacher);
		request.setAttribute(
			postBInterface.COURSEIDFIELD,
			review_course);
		return request;
    }
    //return true if user is found in session
    private boolean dummyAuthentication(
    	HttpServletRequest request){
    	//user authentication is required!
		User user = (User)request.getSession()
    			.getAttribute(StringInterface.USERATTR);
    	return (user != null);
    } //end dummyAuthentication
}
