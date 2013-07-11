package server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
//can be used only with an interval of 1 second
public class RestRequest
{
	private static long lastRequestTime = 0;
	private static String convertWhiteSpace(String url){
		return url.replaceAll(" ", "%20");	
	}

	/**
	 * Makes the webservice request
	 */
	public static synchronized String issueRequest(String uri,int timeout,String from){
		try
		{
			String urlString = convertWhiteSpace(uri);
			while(busy(timeout,from)){
				//waiting
				try{
					Thread.sleep(500);
				}catch(InterruptedException e){
				}
			}
			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();
			
			ByteArrayOutputStream result = new ByteArrayOutputStream();
			InputStream input = connection.getInputStream();
			byte[] buffer = new byte[1000];
			int amount=0;
			while(amount != -1){
				result.write(buffer, 0, amount);
				amount = input.read(buffer);
			}
			lastRequestTime = new java.util.Date().getTime();
			return result.toString();
		}catch (IOException e){
			log.writeException(e.getMessage());
			return null;
		}catch (SecurityException e){
			log.writeException(e.getMessage());
			return null;
		}
	}
	private static boolean busy(int timeout,String from){
		if (lastRequestTime+(1000*timeout)
			> new java.util.Date().getTime()){
			return true;
		}else{
			return false;
		}
	}
}
