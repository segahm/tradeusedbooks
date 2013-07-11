package server;
import javax.servlet.http.*;
import javax.servlet.*;
import java.sql.*;
/**simply takes book id as a parameter and
 *displays a book
 */
public class BookServlet extends ServletParent 
	implements bookInterface,URLInterface{
	/**entry point to the page*/
	public void service(HttpServletRequest request,
                       HttpServletResponse response)
                throws ServletException{
        ConnectionPool conPool = null;
		Connection connection = null;
        try{
        	//check that book was passed as query string
    		if (request.getQueryString()==null 
    			|| request.getQueryString().length() 
    				!= booksTable.ID_LENGTH){
				response.sendRedirect("/");
			}else{
				//get connection
				conPool = getConnectionPool();
				connection = conPool.getConnection();
				String bookID = request.getQueryString();
				booksTable book = generalUtils.getBook(
					bookID,connection);
				RequestDispatcher rd = null;
				if (book != null){
					/*book found, checking isbn record for
					 *this book
					 */
					isbnTable bookInfo = null;
					if (book.getISBN() != null){
						bookInfo = generalUtils.getBookInfo(
								book.getISBN(),connection);
						//adding isbn info to request
						request.setAttribute(ATTR_ISBNINFO,
							bookInfo);
					}
					//adding book to request
					request.setAttribute(ATTR_BOOKINFO,book);
					/*
					 *getting college/department/
					 *course/teacher names based on ids
					 */
					if (book.getCollegeID()!=0){
						generalUtils utils = new generalUtils(connection);
						collegeTable college = utils.getCollege(book.getCollegeID()+"");
						//adding college short name to request
						request.setAttribute(FULLNAME_COLLEGE,college.getFull());
						request.setAttribute(SHORTNAME_COLLEGE,college.getShort());
						if (book.getDepartmentID()!=0){
							String department = utils.getDepartment(book.getDepartmentID()+"").getName();
							//adding department to request
							request.setAttribute(NAME_DEPARTMENT,department);
							if (book.getCourseID()!=0){
								String course = utils.getCourse(book.getCourseID()+"").getName();
								//adding course to request
								request.setAttribute(NAME_COURSE,course);
							}
						}
						if (book.getTeacherID()!=0){
							String teacher = utils.getTeacher(book.getTeacherID()+"").getName();
							//adding teacher to request
							request.setAttribute(NAME_TEACHER,teacher);
						}
						utils = null;//garbage collect
					}
					//getting seller info based on id
					usersTable seller = userUtils.getUserInfo(book.getSellerID(),connection);
					//adding seller user object to request
					request.setAttribute(ATTR_USERINFO,seller);
					rd = getServletContext()
						.getRequestDispatcher(PATHBOOK);
				}else{
					//book wasn't found--send to error page
					String errorMessage = "we were unable to find the book you specified";
					request.setAttribute(
						StringInterface.ERRORPAGEATTR,
						errorMessage);
					rd = getServletContext()
						.getRequestDispatcher(PATHUSERERROR);
				}
				rd.forward(request,response);
			}
    	}catch(Exception e){
    		throw new ServletException(e);
    	}finally{
    		if (conPool != null && connection != null){
    			conPool.free(connection);
				connection = null;
			}
    	}
    } //end service
}
