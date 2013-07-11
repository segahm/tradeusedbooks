package server;
import javax.servlet.http.*;
import javax.servlet.*;
import java.sql.*;
/*
 *Searchservlet simply accepts search requests and after
 *getting a search object based on the query, it forwards
 *the request to search jsp
 */
public class SearchServlet extends ServletParent 
	implements URLInterface,SearchInterface{
	public void service(HttpServletRequest request,
                       HttpServletResponse response)
                throws ServletException{
   		ConnectionPool conPool = null;
   		Connection connection = null;
   		try{
			ParameterParser parameter = new ParameterParser(request);
			//required parameters
			String search_query = parameter.getString(SearchInterface.QUERY,null);
			String search_site = parameter.getString(SearchInterface.HEADER,null);
			//optional page (default 0 for "from" = 1)
			int search_page = parameter.getInt(SearchInterface.PAGE,0);
			int search_resultView = parameter.getInt(SearchInterface.RESULTVIEW,0);
			int search_limit = parameter.getInt(SearchInterface.RESULTLIMIT,0);
			int searchFrom = 0;//default results_from
			//check that site specified
			if (request.getQueryString()==null || search_site==null
				|| !search_site.matches("["
				+SearchInterface.SITE[0][0]+SearchInterface.SITE[1][0]
				+SearchInterface.SITE[2][0]+"]")){
				response.sendRedirect("/");
			}else if (search_query == null || isEmpty(search_query)){
				response.sendRedirect("/p/"+search_site);
			}else{
				//get connection
				conPool = getConnectionPool();
				connection = conPool.getConnection();
				if (search_page<1){
					search_page = 1;
				}
				request.setAttribute(SearchInterface.QUERYSTRING,
					request.getQueryString());
				request.setAttribute(SearchInterface.RESULTVIEW,
					search_resultView+"");
				//IF MARKET PAGE
				if (search_site.equals(SearchInterface.SITE[0][0])){
					//optional category parameters
					int search_college = parameter.getInt(SearchInterface.COLLEGE,0);
					int search_department = parameter.getInt(SearchInterface.DEPARTMENT,0);
					int search_course = parameter.getInt(SearchInterface.COURSE,0);
					int search_teacher = parameter.getInt(SearchInterface.TEACHER,0);
					//setting default result limit if invalid value
					if (search_limit<1 || search_limit>=100){
						search_limit = SearchInterface.MARKET_LIMIT;
					}
					searchFrom = (search_page-1)*search_limit;
					//making a search
					SearchMarket search = new SearchMarket(
						search_query,connection,
						searchFrom,
						search_limit);
					//setting optional parameters
					search.setParameters(search_college,
						search_department,search_course,
						search_teacher);
					//executing the actual query
					search.execute();
					//sending parameters to jsp
					//site:market 0, wanted 1, bookstores 2
					request.setAttribute(SearchInterface.SELECTEDSITE,"0");
					//search object
					request.setAttribute(SearchInterface.SEARCHATTR,search);
					//page # requested in search
					request.setAttribute(SearchInterface.PAGENUMBERATTR,search_page+"");
					//line of pages #s
					request.setAttribute(SearchInterface.PAGELINEATTR,
						pages(search.getTotalResults(),search_page,search_limit,request.getQueryString()));
					//if something found
					if (search.getTotalResults()>0){
						request.setAttribute(
							SearchInterface.CATEGORYHEADERATTR,
							market_category(
								search_college,
								search_department,
								search_course,
								search_teacher,
								request,
								connection));
						request.setAttribute(
							SearchInterface.CATEGORYLISTATTR,
							search.category());
					}else{
						request.setAttribute(SearchInterface.CATEGORYHEADERATTR,"");
					}
				}
				if (search_site.equals(SearchInterface.SITE[1][0])){
					//setting default result limit if invalid value
					if (search_limit<1 || search_limit>=100){
						search_limit = SearchInterface.WANTED_LIMIT;
					}
					searchFrom = (search_page-1)*search_limit;
					//making a search
					SearchWanted search = new SearchWanted(
						search_query,connection,
						searchFrom,search_limit);
					//executing the actual query
					search.execute();
					//sending parameters to jsp
					//site:market 0, wanted 1, bookstores 2
					request.setAttribute(SearchInterface.SELECTEDSITE,"1");
					//search object
					request.setAttribute(SearchInterface.SEARCHATTR,search);
					//page # requested in search
					request.setAttribute(SearchInterface.PAGENUMBERATTR,search_page+"");
					//line of pages #s
					request.setAttribute(SearchInterface.PAGELINEATTR,
						pages(search.getTotalResults(),search_page,search_limit,request.getQueryString()));
				}
				if (search_site.equals(SearchInterface.SITE[2][0])){
					//setting default result limit if invalid value
					if (search_limit<1 || search_limit>=100){
						search_limit = SearchInterface.BOOKSTORES_LIMIT;
					}
					searchFrom = (search_page-1)*search_limit;
					request.setAttribute(SearchInterface.SELECTEDSITE,"2");
					//making a search
					SearchBookstores search = new SearchBookstores(
						search_query,connection,
						searchFrom,search_limit);
					//executing the actual query
					search.execute();
					//sending parameters to jsp
					//site:market 0, wanted 1, bookstores 2
					request.setAttribute(SearchInterface.SELECTEDSITE,"2");
					//search object
					request.setAttribute(SearchInterface.SEARCHATTR,search);
					//page # requested in search
					request.setAttribute(SearchInterface.PAGENUMBERATTR,search_page+"");
					//line of pages #s
					request.setAttribute(SearchInterface.PAGELINEATTR,
						pages(search.getTotalResults(),search_page,search_limit,request.getQueryString()));
				}
				RequestDispatcher rd = getServletContext()
						.getRequestDispatcher(PATHSEARCH);
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
    }
    private boolean isEmpty(String s){
		s = s.trim();
		String result = "";
		for (int i=0;i<s.length();i++){
			if (Character.isLetterOrDigit(s.charAt(i))){
				result += s.charAt(i);
			}
		}
		return (result.length() == 0);
	} //end format
	private String pages(int totalResults,int page,
		int limit,String queryString){
		String line = "";
		queryString = queryString.replaceAll("&p=[0-9]+","");
		if (totalResults!=0){
			line += "<p align=center style='font-size:12pt'>Pages: ";
			int maxpages;
			//setting max pages
			maxpages = (int)Math.ceil(totalResults/(double)limit);
			//shows previous page button if current page is bigger than min
			if (page>1){
				line += "<a href='"+URLSEARCH+"?"
				+queryString+"&"+PAGE+"="+(page-1)
				+"' style='text-decoration: none;'><font face='Times New Roman' color='#0066CC'>&#9668;</font></a>&nbsp;";
			}
			if (Math.max(1,page-5)!=1){
				line += "<a href='"+URLSEARCH+"?"
				+queryString+"&"+PAGE+"="+(page-6)+"'>...</a>&nbsp;";
			}
			for (int j=Math.max(1,page-5);j<=Math.min(maxpages,page+5);j++){
				if (page==j)
					line += j+"&nbsp"; //current page
				else{
					line += "<a href='"+URLSEARCH+"?"
					+queryString+"&"+PAGE+"="+j+"'>"+j+"</a>&nbsp;";
				}
			}
			if (Math.min(maxpages,page+5)!=maxpages){
				line += "<a href='"+URLSEARCH+"?"
				+queryString+"&"+PAGE+"="+(page+6)+"'>...</a>&nbsp;";
			}
			//shows next button if current page is less than max
			if (page<maxpages){ 
				line += "<a href='"+URLSEARCH+"?"
				+queryString+"&"+PAGE+"="+(page+1)
				+"' style='text-decoration: none;'><font face='Times New Roman' color='#0066CC'>&#9658;</font></a>&nbsp;";
			}
			line += "</p>";
		}
		return line;
	} //end pages
	/*market_category:
	 *assumes that all ids are valid
	 */
	private String market_category(int id_college,
		int id_department, int id_course, int id_teacher,
		HttpServletRequest request,
		Connection con) 
		throws SQLException{
		try{
			generalUtils utils = new generalUtils(con);
			String[][] categories = new String[4][2];
			String line = "";
			String newQueryString = request.getQueryString();
			newQueryString = newQueryString.replaceAll("\\&("
			+SearchInterface.COLLEGE
			+"|"+SearchInterface.DEPARTMENT
			+"|"+SearchInterface.COURSE
			+"|"+SearchInterface.TEACHER+")=[^\\&]*","");
			String emptyQueryString = newQueryString; 
			//college is known
			if (id_college != 0){
				categories[0][0] = id_college+"";
				collegeTable t1 
					= utils.getCollege(id_college+"");
				categories[0][1] = t1.getShort();
			}
			if (id_department != 0){
				categories[1][0] = id_department+"";
				departmentTable t1 
					= utils.getDepartment(id_department+"");
				categories[1][1] = t1.getName();
				//setting college based on department
				if (id_college == 0){
					categories[0][0] = t1.getCollegeID()+"";
					collegeTable t2
						= utils.getCollege(t1.getCollegeID()+"");
					categories[0][1] = t2.getShort();
				}
			}
			if (id_course != 0){
				categories[2][0] = id_course+"";
				courseTable t1 
					= utils.getCourse(id_course+"");
				categories[2][1] = t1.getName();
				//setting college based on course
				if (categories[0][0] == null){
					categories[0][0] = t1.getCollegeID()+"";
					collegeTable t2
						= utils.getCollege(t1.getCollegeID()+"");
					categories[0][1] = t2.getShort();
				}
				if (categories[1][0] == null){
					categories[1][0] = t1.getDepartmentID()+"";
					departmentTable t2
						= utils.getDepartment(t1.getDepartmentID()+"");
					categories[1][1] = t2.getName();
				}
			}
			if (id_teacher != 0){
				categories[3][0] = id_teacher+"";
				teacherTable t1 
					= utils.getTeacher(id_teacher+"");
				categories[3][1] = t1.getName();
				//setting college based on teacher
				if (categories[0][0] == null){
					categories[0][0] = t1.getCollegeID()+"";
					collegeTable t2
						= utils.getCollege(t1.getCollegeID()+"");
					categories[0][1] = t2.getShort();
				}
			}
			//if college category set
			if (categories[0][0] != null
				&& categories[0][1] != null){
				newQueryString += "&"+SearchInterface.COLLEGE+"="+categories[0][0];
				line += "<a class=\"refLinks\" style=\"text-decoration: none;\" href=\"/query?"
				+emptyQueryString+"\">-</a>&nbsp;<a class=\"refLinks\" href=\"/query?"
				+newQueryString+"\">"+tools.lengthFormat(categories[0][1],20)+"</a>";
				//if department category set
				if (categories[1][0] != null
					&& categories[1][1] != null){
					newQueryString += "&"+SearchInterface.DEPARTMENT+"="+categories[1][0];
					line += "&nbsp;&gt;&nbsp;<a class=\"refLinks\" href=\"/query?"+newQueryString+"\">"
					+tools.lengthFormat(categories[1][1],20)+"</a>";
					//if course category set
					if (categories[2][0] != null
						&& categories[2][1] != null){
						newQueryString += "&"+SearchInterface.COURSE+"="+categories[2][0];
						line += "&nbsp;&gt;&nbsp;<a class=\"refLinks\" href=\"/query?"+newQueryString+"\">"
						+tools.lengthFormat(categories[2][1],20)+"</a>";
					}
				}
				//if teacher category set
				if (categories[3][0] != null
					&& categories[3][1] != null){
					newQueryString += "&"+SearchInterface.TEACHER+"="+categories[3][0];
					line += "&nbsp;&gt;&nbsp;<a class=\"refLinks\" href=\"/query?"+newQueryString+"\">"
					+tools.lengthFormat(categories[3][1],20)+"</a>";
				}
			}
			utils = null;
			return line;
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end market_category
}
