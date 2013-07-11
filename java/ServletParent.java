package server;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletContext;
import java.sql.SQLException;
/**a class that should extended by all servlets that use
 *connection pool*/
public class ServletParent extends HttpServlet {
	/**creates a new connection pool and adds it to
	 *servlet context*/
	protected void newConnectionPool() 
		throws SQLException{
		ConnectionPool connectionpool = ConPoolCreator.newConnectionPool();		
		//adding connection pool to shared object
		ServletContext context = getServletContext();
		context.setAttribute(StringInterface.DBPOOL,connectionpool);
	} //end newConnectionPool
	/**gets connection pool from servlet context and
	 *if null then call newConnectionPool*/
	protected ConnectionPool getConnectionPool() 
		throws SQLException{
		ServletContext context = getServletContext();
		ConnectionPool cPool = (ConnectionPool)(context.getAttribute(StringInterface.DBPOOL));
		if (cPool == null){
			newConnectionPool();
		}
		return (ConnectionPool)(context.getAttribute(StringInterface.DBPOOL));
	} //end getConnectionPool
	/**closes all open connections and removes connection
	 *pool from servlet context*/
	public void destroy(){
		ServletContext context = getServletContext();
		ConnectionPool cPool = (ConnectionPool)(context.getAttribute(StringInterface.DBPOOL));
		if (cPool!=null){
			cPool.closeAllConnections();
			context.removeAttribute(StringInterface.DBPOOL);
		}
	} //end destroy
}
