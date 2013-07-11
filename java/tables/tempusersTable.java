package server;
/*
 *mysql> describe tabletempusers;
 *+----------+-------------+------+-----+---------+-------+
 *| Field    | Type        | Null | Key | Default | Extra |
 *+----------+-------------+------+-----+---------+-------+
 *| username | varchar(20) |      | MUL |         |       |
 *| tempPass | varchar(10) |      |     |         |       |
 *| email    | varchar(60) |      |     |         |       |
 *| date     | varchar(10) |      |     |         |       |
 *+----------+-------------+------+-----+---------+-------+
 *4 rows in set (0.00 sec)
 */
public class tempusersTable extends Table{
	//expiration of each record
	public static final int EXPIRATION = 3;
	//begin TEMPUSERSTABLE fields
	public static final String USERNAME = "username";
	public static final String PASS = "tempPass";
	public static final String EMAIL = "email";
	public static final String DATE = "date";
	
	private String username;
	private String tempPass;
	private String email;
	private String date;
	usersTable test = new usersTable();
	public void setUsername(String s) 
		throws FormatException{
		test.setUsername(s);
		username = test.getUsername();
	} //end setUsername
	public void setTempPass(String s)
		throws FormatException{
		test.setPassword(s);
		tempPass = test.getPassword();
	} //end setPassword
	public void setEmail(String s) 
		throws FormatException{
		test.setEmail(s);
		email = test.getEmail();
	} //end setEmail
	public void setDate(String s) 
		throws FormatException{
		test.setDate(s);
		date = test.getDate();
	} //end setDate
	public String getUsername(){
		return username;
	}
	public String getTempPass(){
		return tempPass;
	}
	public String getEmail(){
		return email;
	}
	public String getDate(){
		return date;
	}
	public String toString(){
		return "tempusersTable";
	}
}