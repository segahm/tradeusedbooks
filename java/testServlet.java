package server;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.io.*;
import java.util.*;

public class testServlet extends ServletParent
{
	public void service(HttpServletRequest request,
		HttpServletResponse response) 
		throws ServletException, IOException{
    	BufferedReader reader = new BufferedReader(
    		new FileReader(StringInterface.rootPath
    		+"emails.txt"));
    	String line = reader.readLine();
    	String emails = "";
    	while (line!=null){
    		emails += line+";";
    		line = reader.readLine();
    	}
    	SendMail mail = new SendMail(emails.split(";"),true);
    	reader = new BufferedReader(
    		new FileReader(StringInterface.rootPath
    		+"mail.html"));
    	line = reader.readLine();
    	String message = "";
    	while (line!=null){
    		message += line;
    		line = reader.readLine();
    	}
    	mail.send(message,"Announcement from TradeUsedBooks.com");
	}
}