<%!
//declarations
private void newConnectionPool() throws SQLException
{
	try{
		ConnectionPool connectionpool = ConPoolCreator.newConnectionPool();		
		//adding connection pool to shared object
		ServletContext context = getServletContext();
		context.setAttribute(StringInterface.DBPOOL,connectionpool);
	}
	catch (SQLException ex){
		throw new SQLException(ex.getMessage());	
	}
}
private ConnectionPool getConnectionPool() throws SQLException
{
	ConnectionPool cPool = (ConnectionPool)(getServletContext().getAttribute(StringInterface.DBPOOL));
	if (cPool == null){
		newConnectionPool();
	}
	return (ConnectionPool)(getServletContext().getAttribute(StringInterface.DBPOOL));
}
%>