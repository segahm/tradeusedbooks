package server;
import javax.servlet.http.*;
import javax.servlet.*;
import java.sql.*;
/**
 *feedbackServlet shows feedback of the user including
 *comments and rating
 **/
public class feedbackServlet extends ServletParent 
	implements Tables,URLInterface{
	private final String PATHFEEDBACK
		= "/jsp/feedback/feedback.jsp";
	//userattr will be used to pass feedback owner information
	public final static String USERATTR = "feedback_userattr";
	//resultList object will be passed using listattr
	public final static String LISTATTR = "feedback_listattr";
	/**main entrance to the program*/
	public void service(HttpServletRequest request,
                       HttpServletResponse response)
                throws ServletException{
    	ConnectionPool conPool = null;
    	Connection con = null;
    	try{
    		String ownerID = request.getQueryString();
    		//check url format
    		if (ownerID == null || ownerID.length() 
    			!= usersTable.ID_LENGTH){
    			response.sendRedirect(response
					.encodeRedirectURL("/"));
    		}else{
    			conPool = getConnectionPool();
    			con = conPool.getConnection();
    			//checking if user actually exists
    			usersTable user = userUtils.getUserInfo(ownerID,con);
    			RequestDispatcher rd = null;
    			if (user != null){
    				resultList list = getFeedback(ownerID,con);
    				request.setAttribute(USERATTR,user);
					request.setAttribute(LISTATTR,list);
					rd = getServletContext()
						.getRequestDispatcher(PATHFEEDBACK);
    			}else{
    				String errorMessage = "We were unable to find the specified user in our database!";
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
    		if (conPool != null && con != null){
    			conPool.free(con);
    			con = null;
    		}
    	}
    } //end service
    /**gets feedback owner's (the person who left feedback)
     *username and the feedback left
     *by the owner*/
    private resultList getFeedback(String ownerID,
    	Connection con) throws SQLException{
    	try{
    		Statement st = con.createStatement();
			ResultSet rs = null;
			//get all feedback fields and usersTable.username
			String query = 
				"SELECT "+TABLEFEEDBACK+".*,"
				+USERSTABLE+"."+usersTable.USERNAME
				+" FROM "+TABLEFEEDBACK+","+USERSTABLE
				+" WHERE "+USERSTABLE+"."+usersTable.ID
				+" = "+TABLEFEEDBACK+"."+feedbackTable.TRADERID
				+" AND "+TABLEFEEDBACK+"."+feedbackTable.OWNERID
				+" = '"+ownerID+"' ORDER BY "
				+TABLEFEEDBACK+"."+feedbackTable.DATE;
			rs = st.executeQuery(query);
			return new resultList(rs,resultList.FEEDBACKLIST);
    	}catch(SQLException e){
    		log.writeException(e.getMessage());
    		throw e;
    	}
    } //getFeedback
}