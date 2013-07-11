package server;
import java.sql.*;
public final class ConPoolCreator {
	/**creates a new connection*/
	public synchronized static ConnectionPool newConnectionPool() throws SQLException
	{
		try{
			//driver, url, username, password, initialCon, maxCon, waitIfBusy
			ConnectionPool connectionpool = 
			new ConnectionPool(StringInterface.DRIVER, StringInterface.DBURL,
							StringInterface.DBUSERNAME, "fignamoya",10,48,true);
			return connectionpool;
		}
		catch (SQLException ex){
			log.writeException(ex.getMessage());
			throw ex;	
		}
	}
}
