package server;
import java.io.*;

public final class log implements StringInterface
{
	//writeException should be called by the original
	//error thrower directly
	public static synchronized void writeException(String error)
	{
		BufferedWriter exceptionWri = null;
		try
		{
			String methodName;
			String className;
			int lineNumber;
			Throwable t = new Throwable(); 
  			StackTraceElement[] elements = t.getStackTrace(); 
  			if (elements.length <= 0)
  			{
  				methodName = "";
  				className = "";
  				lineNumber=-1;
  			}
  			else
  			{
  				// elements[0] is this method
  				methodName = elements[1].getMethodName();
  				className = elements[1].getClassName();
  				lineNumber = elements[1].getLineNumber();
  			}
  			File f = new File(logPath+"exceptionLog.txt");
  			boolean append = append(f);
			exceptionWri = new BufferedWriter(new FileWriter(f,append));
			exceptionWri.write(new DateObject().getTime()
			+" "+new DateObject().getDate());
			exceptionWri.newLine();
			exceptionWri.write(className+":"+methodName+":");
			exceptionWri.newLine();
			exceptionWri.write("PASSED ERROR:line #:"+lineNumber+":");
			exceptionWri.newLine();
			exceptionWri.write(toString(error));
			exceptionWri.newLine();
			exceptionWri.write("ACTUAL ERROR:");
			exceptionWri.newLine();
			t.printStackTrace(new PrintWriter(exceptionWri));
			exceptionWri.newLine();
			exceptionWri.flush();
			exceptionWri.close();
		}
		catch (Exception e)
		{
			try
			{
				if (exceptionWri!=null)
					exceptionWri.close();
			}
			catch (IOException ignore)
			{
			}
		}
	}
	//writeError can be called by the presentation maker
	//this method SHOULDN't be called by the original
	//error thrower
	public static synchronized void writeError(String error)
	{
		BufferedWriter exceptionWri = null;
		try
		{
			String methodName;
			String className;
			int lineNumber;
			Throwable t = new Throwable(); 
  			StackTraceElement[] elements = t.getStackTrace(); 
  			if (elements.length <= 0)
  			{
  				methodName = "";
  				className = "";
  				lineNumber=-1;
  			}
  			else
  			{
  				// elements[0] is this method
  				methodName = elements[1].getMethodName();
  				className = elements[1].getClassName();
  				lineNumber = elements[1].getLineNumber();
  			}
  			File f = new File(logPath+"errorLog.txt");
  			boolean append = append(f);
			exceptionWri = new BufferedWriter(new FileWriter(f,append));
			exceptionWri.write(new DateObject().getTime()
			+" "+new DateObject().getDate());
			exceptionWri.newLine();
			exceptionWri.write(className+":"+methodName+":");
			exceptionWri.newLine();
			exceptionWri.write("PASSED ERROR:");
			exceptionWri.newLine();
			exceptionWri.write(toString(error));
			exceptionWri.newLine();
			exceptionWri.flush();
			exceptionWri.close();
		}
		catch (Exception e)
		{
			try
			{
				if (exceptionWri!=null)
					exceptionWri.close();
			}
			catch (IOException ignore)
			{
			}
		}
	}
	public static synchronized void writeLog(String log)
	{
		BufferedWriter logWri = null;
		try
		{
			String methodName;
			String className;
			int lineNumber;
			Throwable t = new Throwable(); 
  			StackTraceElement[] elements = t.getStackTrace(); 
  			if (elements.length <= 0)
  			{
  				methodName = "";
  				className = "";
  				lineNumber=-1;
  			}
  			else
  			{
  				// elements[0] is this method
  				methodName = elements[1].getMethodName();
  				className = elements[1].getClassName();
  				lineNumber = elements[1].getLineNumber();
  			}
  			File f = new File(logPath+"log.txt");
  			boolean append = append(f);
			logWri = new BufferedWriter(new FileWriter(f,append));
			logWri.write(new DateObject().getTime()
			+" "+new DateObject().getDate());
			logWri.newLine();
			logWri.write(className+":"+methodName+":");
			logWri.newLine();
			logWri.write(toString(log));
			logWri.newLine();
			//closing output writer
			logWri.flush();
			logWri.close();
			logWri = null;
		}
		catch (Exception e)
		{
			try
			{
				if (logWri!=null)
					logWri.close();
			}
			catch (IOException ignore)
			{
			}
		}
	}
	private static boolean append(File f){
		//if 2 weeks passed or size bigger than roughly
		//2mb, erase the file
  		boolean append;
  		if ((f.lastModified()+1000*60*60*24*14)<
  			System.currentTimeMillis() || f.length()>=2000000){
  			append = false;
  		}else{
  			append = true;
  		}
  		return append;
	}
	private static String toString(String s){
		if (s == null){
			return "null";
		}else{
			return s;
		}
	}
}