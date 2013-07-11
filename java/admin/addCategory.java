package server;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.io.*;
import java.util.*;
/**
 *gets line after line from the file and adds to the
 *appropriate table
 */
public class addCategory extends ServletParent{
	public static final String FILE_UPLOAD_NAME = "file";
	public static final String DEPARTMENT_TEXT_ID = "departmentID";
	public static final String COLLEGE_TEXT_ID = "collegeID";
	public static final String CATEGORY_RADIO = "category";
	public static final String DEPARTMENT_RADIO_VALUE = "departmentR";
	public static final String COURSE_RADIO_VALUE = "courseR";
	public static final String TEACHER_RADIO_VALUE = "teacherR";
	private final String JSP_FILE = "/jsp/admin/addCategory.jsp";
	public void service(HttpServletRequest request,
		HttpServletResponse response) 
		throws ServletException, IOException{
		try{
			ParameterParser parameter = new ParameterParser(request);
			String file = request.getParameter(FILE_UPLOAD_NAME);
			String collegeID = parameter.getString(COLLEGE_TEXT_ID,null);
			String departmentID = parameter.getString(DEPARTMENT_TEXT_ID,null);
			String categoryRadio = parameter.getString(CATEGORY_RADIO,null);
			boolean defaultPage = false;
			String message = null;
			System.out.println("file:"+file);
			System.out.println("college:"+collegeID);
			System.out.println("category:"+categoryRadio);
			if (file == null || collegeID == null
				|| categoryRadio == null){
				defaultPage = true;
			}else{
				ConnectionPool conPool = getConnectionPool();
				Connection con = conPool.getConnection();
				try{
					message = file;
				}catch(Exception e){
					throw e;
				}finally{
					conPool.free(con);
					con = null;
				}
			}
			if (defaultPage = true){
				RequestDispatcher rd = rd = getServletContext()
					.getRequestDispatcher(JSP_FILE);
				rd.forward(request,response);
			}else{
				PrintWriter out = response.getWriter();
				out.println(message);
				out.close();
			}
		}catch(Exception e){
			throw new ServletException(e);
		}
	}
}