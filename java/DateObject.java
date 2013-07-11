package server;
import java.io.*;
import java.util.*;

public class DateObject
{
	private String date;	//book's date
	private Calendar calendar;	//current date
	private Calendar calendarBook;	//temporary date calndar +INTERVAL
	private SimpleTimeZone pdt;
	private Date trialTime;
	public DateObject()
	{
		date = null;
		createCurrentTime();
	}
	public DateObject(String d)
	{
		date = new String(d);	//creates a string m/d/yyyy where m(0,11) d(1,31)
		createCurrentTime();
	}
	private boolean createCurrentTime()
	{
		 // get the supported ids for GMT-08:00 (Pacific Standard Time)
 		String[] ids = TimeZone.getAvailableIDs(-8 * 60 * 60 * 1000);
 		// if no ids were returned, something is wrong. get out.
 		if (ids.length == 0)
 		{
 			return false;
 		}
 		// create a Pacific Standard Time time zone
 		pdt = new SimpleTimeZone(-8 * 60 * 60 * 1000, ids[0]);
 		// set up rules for daylight savings time
 		pdt.setStartRule(Calendar.APRIL, 1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
 		pdt.setEndRule(Calendar.OCTOBER, -1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
 		// create a GregorianCalendar with the Pacific Daylight time zone
 		// and the current date and time
 		calendar = new GregorianCalendar(pdt);
 		trialTime = new Date();
 		calendar.setTime(trialTime);
 		return true;
	}
	//get Date in int/int/int format or (m/d/y)
	public String getDate(){
		return new String((calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.DATE)+"/"+calendar.get(Calendar.YEAR));
	} //end getDate
	//get Time in int:int:int format or (hh:mm:ss)
	public String getTime(){
		return new String(calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND));
	} //end getTime
	/*interval in days, add interval to the given data 
	 *and returns wherever actual data is bigger than 
	 *date+interval
	 */
	public boolean expired(String date,int interval){
		if (date==null){
			return false;
		}
		//books date:
		int m = Integer.parseInt(date.substring(0,date.indexOf("/")))-1;
		int d = Integer.parseInt(date.substring(date.indexOf("/")+1,date.lastIndexOf("/")));
		int y = Integer.parseInt(date.substring(date.lastIndexOf("/")+1,date.length()));
		
		//creating a books date with a same time zone
		calendarBook = new GregorianCalendar(pdt);
 		//calendarBook.setTime(trialTime);
 		//moving books date + 7
 		calendarBook.set(y,m,d+interval);
 		if (calendar.after(calendarBook)){
 			return true;
 		}
 		else{
 			return false;
 		}
	}
	//get Time in int/int/int format or (m/d/y)
	public String getEndDate(String date,int interval){
		if (date==null){
			return null;
		}
		//books date:
		int m = Integer.parseInt(date.substring(0,date.indexOf("/")))-1;
		int d = Integer.parseInt(date.substring(date.indexOf("/")+1,date.lastIndexOf("/")));
		int y = Integer.parseInt(date.substring(date.lastIndexOf("/")+1,date.length()));
		
		//creating a books date with a same time zone
		calendar = new GregorianCalendar(pdt);
 		//calendarBook.setTime(trialTime);
 		//moving books date + interval
 		calendar.set(y,m,d+interval);
		return new String((calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.DATE)+"/"+calendar.get(Calendar.YEAR));
	} //end getEndDate
	public String getDateAfterDays(int days){
		String date = getDate();
		int m = Integer.parseInt(date.substring(0,date.indexOf("/")))-1;
		int d = Integer.parseInt(date.substring(date.indexOf("/")+1,date.lastIndexOf("/")));
		int y = Integer.parseInt(date.substring(date.lastIndexOf("/")+1,date.length()));
		Calendar cal = new GregorianCalendar(pdt);
 		cal.set(y,m,d+days);
 		return new String((cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.DATE)+"/"+cal.get(Calendar.YEAR));
	}
}