package server;
import java.io.*;
import java.util.*;
import java.net.*;
public class browserRequest{
	private StringBuffer buffer;
	public String issueRequest(String urlString){
		try{
			String domain = urlString.replaceAll("/.*$","");
			String relativePath = urlString.replaceAll("(^.*)/[^/]*$","$1");
			URL url = new URL("http://"+urlString.replaceAll(" ", "+"));
			InputStream is = url.openStream();
			BufferedReader reader
				= new BufferedReader(new InputStreamReader(is));
			buffer = new StringBuffer(10000);
			String line = reader.readLine();
			while(line!=null){
				buffer.append(line);
				line = reader.readLine();
			}
			String result = buffer.toString();	
			//if too big
			if (buffer.length()>100000){
				return "too big";
			}
			//working with urls
			//rewriting domain paths
			result = result.replaceAll("(href|src|action)=\"?/([^\"> ]*)\"?","$1=/url/"+domain+"/$2");
			//rewriting absolute paths
			result = result.replaceAll("(href|src|action)=\"?http://([^\"> ]*)\"?","$1=/url/$2");
			//rewriting relative paths
			result = result.replaceAll("(href|src|action)=\"?([^/][^\"> ]*)\"?","$1=/url/"+relativePath+"/$2");		
			result = result.replaceAll("(href|src|action)=([^\"> ]*)","$1=\"http://"+StringInterface.DOMAIN+"$2\"");
			return result;
		}catch(Exception e){
			return "";
		}
	}
}
