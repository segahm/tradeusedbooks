package server;
/*
mysql> describe tablewanted;
+------------+--------------+------+-----+---------+----------------+
| Field      | Type         | Null | Key | Default | Extra          |
+------------+--------------+------+-----+---------+----------------+
| id         | smallint(6)  |      | PRI | NULL    | auto_increment |
| isbn       | varchar(13)  |      | MUL |         |                |
| title      | varchar(100) |      |     |         |                |
| author     | varchar(100) |      |     |         |                |
| userID     | varchar(10)  |      |     |         |                |
| expiration | varchar(10)  |      |     |         |                |
| college    | smallint(5)  |      |     | 0       |                |
+------------+--------------+------+-----+---------+----------------+
7 rows in set (0.00 sec)
*/
public class wantedTable extends Table{
	public static final String ID = "id";
	public static final String ISBN = "isbn";
	public static final String TITLE = "title";
	public static final String AUTHOR = "author";
	public static final String USERID = "userID";
	public static final String EXPIRATION = "expiration";
	public static final String COLLEGE = "college";
	
	private String id;
	private String isbn;
	private String title;
	private String author;
	private String userID;
	private String expiration;
	private String collegeID;
	//these tables will be used to check the format
	booksTable booksTemp = new booksTable();
	usersTable usersTemp = new usersTable();
	public void setID(String s){
		id = s;
	} //end setID
	public void setISBN(String s) throws FormatException{
		if (s == null){
			throw new FormatException("isbn cannot be empty");
		}
		booksTemp.setISBN(s);
		isbn = booksTemp.getISBN();
	} //end setISBN
	public void setTitle(String s) throws FormatException{
		booksTemp.setTitle(s);
		title = booksTemp.getTitle();
	} //end setTitle
	public void setAuthor(String s) throws FormatException{
		booksTemp.setAuthor(s);
		author = booksTemp.getAuthor();
	} //end setAuthor
	public void setUserID(String s) throws FormatException{
		usersTemp.setID(s);
		userID = usersTemp.getID();
	} //end setUserID
	public void setDate(String s) throws FormatException{
		usersTemp.setDate(s);
		expiration = usersTemp.getDate();
	} //end setDate
	public void setCollege(String s) throws FormatException{
		booksTemp.setCollegeID(s);
		collegeID = booksTemp.getCollegeID()+"";
	} //end setCollege
	public String getID(){
		return id;
	} //end setISBN
	public String getISBN(){
		return isbn;
	} //end setISBN
	public String getTitle(){
		return title;
	} //end setTitle
	public String getAuthor(){
		return author;
	} //end setAuthor
	public String getUserID(){
		return userID;
	} //end getUserID
	public String getDate(){
		return expiration;
	} //end getDate
	public String getCollege(){
		return collegeID;
	} //end getCollege
}
