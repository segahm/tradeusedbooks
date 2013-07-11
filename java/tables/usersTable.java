package server;
/*
 *mysql> describe tableusers;
 *+---------------+-------------+---------+-----+---------+-------+
 *| Field         | Type        | Null    | Key | Default | Extra |
 *+---------------+-------------+---------+-----+---------+-------+
 *| id            | varchar(10) |         | MUL |         |       |
 *| username      | varchar(20) |         | MUL |         |       |
 *| password      | varchar(10) |         |     |         |       |
 *| encryptedpass | varchar(28) |         |     |         |       |
 *| email         | varchar(60) |         |     |         |       |
 *| date          | varchar(10) |         |     |         |       |
 *| feedback      | varchar(10) | YES     |     | NULL    |       |
 *| numbmessages  | tinyint(1)  |unsigned |     |    0    |       |
 *7 rows in set (0.00 sec)
 */

public class usersTable extends Table{
	//field names:
	public static final String ID = "id";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String EPASS = "encryptedpass";
	public static final String EMAIL = "email";
	//date: m[m]/d[d]/yyyy
	public static final String DATE = "date";
	//feedback: NULL or int/int/int
	public static final String FEEDBACK = "feedback";
	public static final String MESSAGES = "numbmessages";
	//length parameters:
	public static final int ID_LENGTH = 10;
	public static final int USERNAME_MAX = 20;
	public static final int USERNAME_MIN = 4;
	public static final int PASSWORD_MAX = 10;
	public static final int PASSWORD_MIN = 4;
	public static final int EPASS_LENGTH = 28;
	public static final int EMAIL_MIN = 5;
	public static final int EMAIL_MAX = 60;
	public static final int DATE_MAX = 10;
	public static final int DATE_MIN = 8;
	public static final int FEEDBACK_MAX = 10;
	public static final int FEEDBACK_MIN = 5;
	public static final int DAILY_MESSAGES_MAX = 3;
	private String username;
	private String password;
	private String email;
	private String epass;
	private String id;
	private String date;
	private int[] feedback;
	private int messages;
	public boolean setID(String s) throws FormatException
	{
		if (s == null){
			throw new FormatException(fieldMessages
					.empty("user's ID"));
		}
		if (s.length() != ID_LENGTH){
			throw new FormatException(fieldMessages
					.badLength("user's ID",ID_LENGTH));
		}
		if (!tools.lettersAndDigitsOnly(s,null,"0")){
			throw new FormatException(fieldMessages
					.illegalChars("user's ID","a-z,1-9"));
		}
		id = s;
		return true;
	} //end setID
	public boolean setUsername(String s) throws FormatException
	{
		if (s == null){
			throw new FormatException(fieldMessages
				.empty(USERNAME));
		}
		if (s.trim().length() > USERNAME_MAX 
		|| s.trim().length() < USERNAME_MIN){
			throw new FormatException(fieldMessages
				.outOfBounds("username",USERNAME_MIN,
				USERNAME_MAX));
		}
		if (!tools.lettersAndDigitsOnly(s,null,null)){
				throw new FormatException(fieldMessages
					.illegalChars("username"
					,"a-z,A-Z,0-9"));
		}
		username = s.trim();
		return true;
	} //end setUsername
	public boolean setPassword(String s) 
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
				.illegalChars("password"
				,"a-z,1-9"));
		}
		password = s.trim();
		return true;
	} //end setPassword
	public boolean setEncrypted(String s) throws FormatException
	{
		if (s == null){
			throw new FormatException(fieldMessages
				.empty("encrypted password"));
		}
		if (s.length()!=EPASS_LENGTH){
			throw new FormatException(fieldMessages
				.badLength("encrypted password",EPASS_LENGTH));
		}
		epass = s;
		return true;
	} //end setEncrypted
	public boolean setEmail(String s) throws FormatException
	{
		if (s == null){
			throw new FormatException(fieldMessages
				.empty("email"));
		}
		if (s.trim().length() < EMAIL_MIN
			|| s.trim().length() > EMAIL_MAX){
			throw new FormatException(fieldMessages
			.outOfBounds("email",EMAIL_MIN,EMAIL_MAX));	
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
		return true;
	} //end setEmail
	public boolean setDate(String s) throws FormatException
	{
		if (s == null){
			throw new FormatException(fieldMessages
				.empty("date"));
		}
		if(s.length() > DATE_MAX
			|| s.length() < DATE_MIN){
			throw new FormatException(fieldMessages
				.outOfBounds("date",DATE_MIN,DATE_MAX));
		}
		if (!tools.isGoodDate(s)){
			throw new FormatException(fieldMessages
				.badFormat("date"));
		}
		date = s;
		return true;
	} //end setDate
	public boolean setFeedback(int[] s) throws FormatException
	{
		if (s == null){
			feedback = s;
			return true;
		}
		if(s.length != 3 || ((s[0]+"/"+s[1]+"/"
			+s[2]).length() > FEEDBACK_MAX) 
			|| ((s[0]+"/"+s[1]+"/"+s[2]).length() 
			< FEEDBACK_MIN)){
			throw new FormatException(fieldMessages
				.badFormat("feedback"));
		}
		feedback = s;
		return true;
	} //end setFeedback
	public boolean setFeedback(String s) throws FormatException
	{
		if (s == null){
			feedback = null;
			return true;
		}
		if(s.length() > FEEDBACK_MAX 
			|| s.length() < FEEDBACK_MIN){
			throw new FormatException(fieldMessages
				.badFormat("feedback"));
		}
		String[] temp = s.split("/");
		int[] temp2 = new int[3];
		for (int i=0;i<temp.length;i++){
			temp2[i] = Integer.parseInt(temp[i]);
		}
		feedback = temp2;
		return true;
	} //end setFeedback(String)
	public void setNumberMessages(String number){
		messages = Integer.parseInt(number);
	}
	public String getID(){
		return id;
	}
	public String getUsername(){
		return username;
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
	public int[] getFeedback(){
		return feedback;
	}
	public int getNumberMessages(){
		return messages;
	}
	public String toString(){
		return "usersTable";
	}
}