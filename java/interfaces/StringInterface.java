package server;
public interface StringInterface
{
	//tradeusedbooks server variables
	
	public static final String DBUSERNAME = "slugt2_WEBMonke";
	public static final String rootPath = "/home/slugt2/";
	public static final String logPath = "/home/slugt2/myLogs/";
	public static final String public_html = "/home/slugt2/public_html/";
	public static final String DBURL = "jdbc:mysql://localhost:3306/slugt2_tradebooks";
	public static final String KEYWORDS = "/home/slugt2/files/keywords.txt";
	public static final String DOMAIN = "www.tradeusedbooks.com";
	
			
	public static final String DRIVER = "com.mysql.jdbc.Driver";	
	public static final String DBPOOL = "dbpool";
	public static final String USERATTR 
		= "user";//used to store session
	public static final String BOOKSTOREUSERATTR 
		= "bookstoreuser";//used to store session
	public static final String ERRORPAGEATTR
		= "stringInterface_error";
	public static final int MAXINACTIVETIME
		= 1200;
	//home server variables
	/*
	public static final String DBUSERNAME = "root";
	public static final String rootPath = "F:/Program Files/resin-3.0.9/webapps/ROOT/";
	public static final String logPath = "F:/Program Files/resin-3.0.9/webapps/ROOT/logs/";
	public static final String public_html = "F:/Program Files/resin-3.0.9/webapps/ROOT/";
	public static final String DBURL = "jdbc:mysql://localhost:3306/tradeusedbooks";
	public static final String KEYWORDS = "F:/Program Files/resin-3.0.9/webapps/ROOT/keywords.txt";
	public static final String DOMAIN = "localhost";
	*/
}