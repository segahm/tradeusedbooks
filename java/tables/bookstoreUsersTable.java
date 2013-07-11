package server;
/*
 mysql> describe tablebookstoreusers;
+----------+-------------+------+-----+---------+-------+
| Field    | Type        | Null | Key | Default | Extra |
+----------+-------------+------+-----+---------+-------+
| id       | varchar(5)  |      | PRI |         |       |
| email    | varchar(60) |      |     |         |       |
| password | varchar(10) |      |     |         |       |
| epass    | varchar(28) |      |     |         |       |
| date     | varchar(10) |      |     |         |       |
+----------+-------------+------+-----+---------+-------+
5 rows in set (0.04 sec)
 */
public class bookstoreUsersTable extends Table{
	public static final String ID = "id";
	public static final String EMAIL = "email";
	public static final String PASSWORD = "password";
	public static final String EPASS = "epass";
	public static final String DATE = "date";
	public static final int EMAIL_MAX = 60;
	public static final int PASSWORD_MIN = 6;
	public static final int PASSWORD_MAX = 10;
	private String id;
	private String email;
	private String password;
	private String epass;
	private String date;
	public void setID(String s) throws FormatException{
		id = s;
	} //end setID
	public void setPassword(String s) 
		throws FormatException{
		if (s == null){
			throw new FormatException(fieldMessages
					.empty("password"));
		}
		if (s.trim().length() > PASSWORD_MAX 
		|| s.trim().length() < PASSWORD_MIN){
				throw new FormatException(fieldMessages
					.outOfBounds("password",PASSWORD_MIN,
					PASSWORD_MAX));
		}
		if (!tools.allLowerCase(s)){
			throw new FormatException(fieldMessages
				.notLowerCase("password"));
		}
		if (!tools.lettersAndDigitsOnly(s,null,null)){
			throw new FormatException(fieldMessages
				.illegalChars("password","a-z,1-9"));
		}
		password = s.trim();
	} //end setPassword
	public void setEncrypted(String s) throws FormatException{
		epass = s;
	} //end setEncrypted
	public void setEmail(String s) throws FormatException{
		if (s == null){
			throw new FormatException(fieldMessages
				.empty("email"));
		}
		if (s.trim().length() > EMAIL_MAX){
			throw new FormatException(fieldMessages
			.outOfBounds("email",0,EMAIL_MAX));	
		}
		if (!tools.isGoodEmail(s)){
			throw new FormatException(fieldMessages
				.mustContain("email","'@', '.'"));
		}
		if (!tools.allASCII(s)){
			throw new FormatException(fieldMessages
			.illegalChars("email","english "
			+"letters/digits/symbols"));
		}
		email = s.trim();
	} //end setEmail
	public void setDate(String s) throws FormatException{
		date = s;
	} //end setDate
	public String getID(){
		return id;
	}
	public String getPassword(){
		return password;
	}
	public String getEncrypted(){
		return epass;
	}
	public String getEmail(){
		return email;
	}
	public String getDate(){
		return date;
	}
	public String toString(){
		return "bookstoreUsersTable";
	}	
}
