package server;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
public class removeEmail extends HttpServlet{
	public void service(HttpServletRequest request,
    	HttpServletResponse response) 
    	throws ServletException{
    	try{
    		response.setContentType("text/html");
    		PrintWriter out = response.getWriter();
    		if (request.getQueryString()!=null && 
    			request.getQueryString().matches(".+@.+")){
    			BufferedWriter writer = new BufferedWriter(new FileWriter(
   					StringInterface.rootPath
   		 			+"removeEmails.txt",true));
    			writer.write(request.getQueryString());
    			writer.newLine();
    			writer.close();
    			out.println("<html><body>your email was removed successfully<br><a href='/'>click here to go to tradeusedbooks.com</a></body></html>");
    		}
    		out.close();
    	}catch(Exception ignore){
    	}
    }
}