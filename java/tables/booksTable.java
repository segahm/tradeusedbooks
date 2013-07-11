package server;
/*
mysql> describe tablebooks;
+------------+----------------------+------+-----+---------+-------+
| Field      | Type                 | Null | Key | Default | Extra |
+------------+----------------------+------+-----+---------+-------+
| id         | varchar(20)          |      | PRI |         |       |
| title      | varchar(100)         |      | MUL |         |       |
| author     | varchar(100)         |      | MUL |         |       |
| isbn       | varchar(13)          | YES  | MUL | NULL    |       |
| price      | varchar(5)           |      |     |         |       |
| condition  | tinyint(1)           |      |     | 0       |       |
| comment    | varchar(100)         | YES  |     | NULL    |       |
| sellerID   | varchar(10)          |      |     |         |       |
| date       | varchar(10)          |      |     |         |       |
| course     | smallint(5) unsigned |      | MUL | 0       |       |
| teacher    | smallint(5) unsigned |      | MUL | 0       |       |
| department | smallint(5) unsigned |      | MUL | 0       |       |
| college    | smallint(5) unsigned |      | MUL | 0       |       |
+------------+----------------------+------+-----+---------+-------+
13 rows in set (0.00 sec)
*/
public class booksTable extends Table{
	//expiration of each record
	public static final int EXPIRATION = 180;
	//table field names:
	public static final String ID 
		= "id"; //uniquely identifies each book
	public static final String TITLE = "title";
	public static final String AUTHOR = "author";
	public static final String ISBN = "isbn";
	public static final String PRICE = "price";
	public static final String CONDITION = "condition";
	public static final String COMMENT = "comment";
	public static final String SELLERID = "sellerID";
	public static final String DATE = "date";
	public static final String COURSE = "course";
	public static final String TEACHER = "teacher";
	public static final String DEPARTMENT = "department";
	public static final String COLLEGE = "college";
	//field table limitations:
	public static final int CONDITION_ACCEPTABLE = 1;
	public static final int CONDITION_GOOD = 2;
	public static final int CONDITION_LIKENEW = 3;
	public static final int CONDITION_NEW = 4;
	public static final String[] CONDITIONS 
		= new String[]{"","acceptable","good","like new","new"};
	public static final int ID_LENGTH = 20;
	public static final int TITLE_MAX = 100;
	public static final int AUTHOR_MAX = 100;
	public static final int ISBN_LENGTH1 = 10;
	public static final int ISBN_LENGTH2 = 13;
	public static final int PRICE_MAX = 3;
	public static final int COMMENT_MAX = 100;
	public static final int SELLERID_LENGTH 
		= usersTable.ID_LENGTH;
	//storage values
	private String id;
	private String title;
	private String isbn;
	private int price = 0;
	private String author;
	private int condition = 1;
	private String comment;
	private String sellerID;
	private String date;
	private int courseID = 0;
	private int collegeID = 0;
	private int departmentID = 0;
	private int teacherID = 0;
	public booksTable(){
		setID(sqlUtils.generate(ID_LENGTH));
	} //creates a new id for the book
	public booksTable(String id){
		setID(id);
	}
	public void setID(String s){
		id = s;
	} //end setID
	/*setTitle:
	 *1)check length, not null
	 *2)check all ascii
	 */
	public void setTitle(String s) 
		throws FormatException{
		if (s == null || s.trim().length()>TITLE_MAX 
			|| s.trim().length() == 0){
			throw new FormatException(
				fieldMessages.outOfBounds("title",0,
				TITLE_MAX));
		}
		if (!tools.allASCII(s)){
			throw new FormatException(
				fieldMessages.illegalChars("title",
				"a-z,A-Z"));
		}
		title = s.trim();
	} //end setTitle
	/*setAuthor:
	 *1) check length, not null
	 *2)check all lettets or ","";"
	 */
	public void setAuthor(String s) 
		throws FormatException{
		if (s == null || s.trim().length()>AUTHOR_MAX 
			|| s.trim().length() == 0){
			throw new FormatException(
				fieldMessages.outOfBounds("author",0,
				AUTHOR_MAX));
		}
		if (!tools.allASCII(s)){
			throw new FormatException(
				fieldMessages.illegalChars("author",
				"a-z,A-Z"));
		}
		author = s.trim();
	} //end setAuthor
	/*setISBN:
	 *1)check length
	 *2)check all digits and letters
	 */
	public boolean setISBN(String s)
		throws FormatException{
		if (s == null || s.trim().equals("")){
			isbn = null;
			return true;
		}
		if (s.trim().length()!=ISBN_LENGTH1
			&& s.trim().length()!=ISBN_LENGTH2){
			throw new FormatException("isbn must be either "
				+10+" or "+13+" characters long");
		}
		if (!tools
			.lettersAndDigitsOnly(s,null,null)){
			throw new FormatException(fieldMessages
				.illegalChars("ISBN","A-Z,0-9"));
		}
		isbn = tools.trim(s);
		return true;
	} //end setISBN
	/*setPrice:
	 *1)parse to double
	 */
	public void setPrice(String s)
		throws FormatException{
		try{
			price = Integer.parseInt(s);
		}catch (Exception e){
			throw new FormatException(
				"bad price format");
		}
	} //end setPrice
	/*setCondition:
	 *1) check integer >1 && <4
	 */
	public void setCondition(String s)
		throws FormatException{
		int i;
		try{
			i = Integer.parseInt(s);
		}catch (Exception e){
			throw new FormatException(
				"bad condition format");
		}
		if (i>4 || i<1){
			throw new FormatException("bad condition format");
		}
		condition = i;
	} //end setCondition
	/*setComment:
	 *1)check  length
	 *2)check all ascii
	 */
	public boolean setComment(String s)
		throws FormatException{
		if (s == null || s.trim().equals("")){
			comment = null;
			return true;
		}
		if (s.trim().length()>COMMENT_MAX){
			throw new FormatException(fieldMessages
				.outOfBounds("comments",0,COMMENT_MAX));
		}
		if (!tools.allASCII(s)){
			throw new FormatException(
				fieldMessages.illegalChars("comments",
				"a-z,A-Z,0-9"));
		}
		comment = s;
		return true;
	} //end setComment
	public void setSellerID(String s)
		throws FormatException{
		usersTable test = new usersTable();
		test.setID(s);
		sellerID = test.getID();
	} //end setSellerID
	public void setDate(String s)
		throws FormatException{
		if (!tools.isGoodDate(s)){
			throw new FormatException("bad date format");
		}
		date = s;
	} //end setDate
	public void setCourseID(String s)
		throws FormatException{
		courseTable	test = new courseTable();
		test.setID(s);
		courseID = test.getID();
	}
	public void setTeacherID(String s)
		throws FormatException{
		teacherTable test = new teacherTable();
		test.setID(s);
		teacherID = test.getID();
	}
	public void setCollegeID(String s)
		throws FormatException{
		collegeTable test = new collegeTable();
		test.setID(s);
		collegeID = test.getID();
	}
	public void setDepartmentID(String s)
		throws FormatException{
		departmentTable test = new departmentTable();
		test.setID(s);
		departmentID = test.getID();
	}
	
	/********** GET METHODS************/
	public String getID(){
		return id;
	}
	public String getTitle(){
		return title;
	}
	public String getAuthor(){
		return author;
	}
	public String getISBN(){
		return isbn;
	}
	public int getPrice(){
		return price;
	}
	public int getCondition(){
		return condition;
	}
	public String getComment(){
		return comment;
	}
	public String getSellerID(){
		return sellerID;
	}
	public String getDate(){
		return date;
	}
	public int getCourseID(){
		return courseID;
	}
	public int getCollegeID(){
		return collegeID;
	}
	public int getDepartmentID(){
		return departmentID;
	}
	public int getTeacherID(){
		return teacherID;
	}
	public boolean isComplete(){
		try{
			if (id.length()==0 || title.length()==0
				|| author.length()==0 || collegeID == 0 
				|| sellerID.length()==0){
				return false;
			}
			return true;
		}catch(NullPointerException e){
			return false;
		}
	}
	public String toString(){
		return "booksTable";
	}
}
