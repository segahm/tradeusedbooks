package server;
import javax.servlet.http.*;
import javax.servlet.*;
import java.sql.*;
/*
 */
public class postW extends ServletParent 
	implements URLInterface,Tables{
	public static final String HIDDENID_FIELD = "postW_hiddenID";
	public static final String ISBN_FIELD = "isbn";
	public static final String TITLE_FIELD = "t";
	public static final String AUTHOR_FIELD = "a";
	//college fields
	public static final String COLLEGESELECT_FIELD = "col";
	public static final String COLLEGEINPUTSHORT_FIELD 
		= "colShort";
	public static final String COLLEGEINPUTFULL_FIELD 
		= "colFul";
	//EXPERATION_FIELD - number of weeks will be assigned to this field
	public static final String EXPIRATION_FIELD = "exp";
	public static final String ERRORMESSAGE_ATTR = "errormessage";
	public static final String PAGE_ATTR = "pagenumber";
	public static final String TABLE_ATTR = "tableattr";
	private final String PATH_POSTW = "/jsp/post/postw.jsp";
	public void service(HttpServletRequest request,
		HttpServletResponse response) throws ServletException{
        try{
        	ConnectionPool conPool = getConnectionPool();
        	HttpSession session = request.getSession();
        	if(!realAuthentication(request,conPool)){
				/*redirect to signin with our url
				 *as a parameter
				 */
				response.sendRedirect(response.
					encodeRedirectURL(URLAUTHSIGNIN+"?"
					+URLPWANTED));
			}else{
				//getting form parameters
				ParameterParser parameter = new ParameterParser(request);
				String hiddenID = parameter.getString(
					HIDDENID_FIELD,null);
				String isbn = parameter.getString(
					ISBN_FIELD,null);
				String title = parameter.getString(
					TITLE_FIELD,null);
				String author = parameter.getString(
					AUTHOR_FIELD,null);
				int expDate = parameter.getInt(
					EXPIRATION_FIELD,2);//2 weeks default
				//college parameters
				int collegeID = parameter.getInt(
					COLLEGESELECT_FIELD,0);
				String collegeShort = parameter.getString(
					COLLEGEINPUTSHORT_FIELD,null);
				String collegeFull = parameter.getString(
					COLLEGEINPUTFULL_FIELD,null);
				boolean defaultPage = true;
				String error = null;
				if(hiddenID == null ||
					!hiddenID.equals(session.getAttribute(HIDDENID_FIELD))){
					//show default page
					hiddenID = sqlUtils.generate(20);
					session.setAttribute(HIDDENID_FIELD,hiddenID);
				}else{
					defaultPage = false;
					User user = (User)request.getSession().getAttribute(StringInterface.USERATTR);
					//do form processing
					try{
						wantedTable table = new wantedTable();
						table.setISBN(isbn);
						table.setTitle(title);
						table.setAuthor(author);
						table.setDate(new DateObject().getDateAfterDays(expDate*7));
						table.setUserID(user.getID());
						Connection con = conPool.getConnection();
						try{
							collegeTable colTable = new collegeTable();
							if (collegeShort!=null 
								&& collegeFull!=null){
								//adding to colleges
								colTable.setShort(collegeShort);
								colTable.setFull(collegeFull);
								postUtils utils = new postUtils(con);
								collegeID = utils.addCollege(colTable);
							}
							if (collegeID == 0){
								throw new FormatException("college was not specified");
							}
							table.setCollege(collegeID+"");
							//if no errors add to DB
							if (!addToWanted(table,con)){
								//failed to update the table
								throw new Exception("failed to update wanted table");
							}
						}catch(Exception e){
							throw e;
						}finally{
							conPool.free(con);
							con = null;
						}
					}catch(FormatException e){
						defaultPage = true;
						error = e.getMessage();
					}
				}
				if (defaultPage){
					request.setAttribute(ERRORMESSAGE_ATTR,error);
					request.setAttribute(PAGE_ATTR,"1");
					//set field parameters in case of an error they will be used
					request.setAttribute(ISBN_FIELD,isbn);
					request.setAttribute(TITLE_FIELD,title);
					request.setAttribute(AUTHOR_FIELD,author);
					//setting college parameters
					request.setAttribute(COLLEGESELECT_FIELD,collegeID+"");
					request.setAttribute(COLLEGEINPUTSHORT_FIELD,collegeShort);
					request.setAttribute(COLLEGEINPUTFULL_FIELD,collegeFull);
				}else{
					session.removeAttribute(HIDDENID_FIELD);
					request.setAttribute(PAGE_ATTR,"2");
				}
				//forward request
    			RequestDispatcher rd = getServletContext()
					.getRequestDispatcher(PATH_POSTW);
				rd.forward(request,response);
			}
        }catch(Exception e){
        	log.writeError(e.getMessage());
        	throw new ServletException(e);
        }
    }
    //checks database for this user
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
    /****** sql methods *********/
    private boolean addToWanted(wantedTable table,
    	Connection con) throws SQLException{
    	try{
    		Statement st = con.createStatement();
    		String[] fields = new String[]{
    			wantedTable.ISBN,
    			wantedTable.TITLE,
    			wantedTable.AUTHOR,
    			wantedTable.USERID,
    			wantedTable.EXPIRATION,
    			wantedTable.COLLEGE
    		};
    		String[] values = new String[]{
    			table.getISBN(),table.getTitle(),
    			table.getAuthor(),table.getUserID(),
    			table.getDate(),table.getCollege()
    		};
    		String query = "INSERT INTO "+TABLEWANTED
			 	+" ("
			 		+sqlUtils.sql_fields(fields)
			 	+") values ("
			 		+sqlUtils.sql_values(values)
			 	+")";
			int resultsUpdated = st.executeUpdate(query);
    		return (resultsUpdated == 1);
    	}catch(SQLException e){
    		log.writeException(e.getMessage());
    		throw e;
    	}
    }
}