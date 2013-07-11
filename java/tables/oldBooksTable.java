package server;
/*
mysql> describe tableoldbooks;
+-----------+----------------------+------+-----+---------+-------+
| Field     | Type                 | Null | Key | Default | Extra |
+-----------+----------------------+------+-----+---------+-------+
| sellerID  | varchar(10)          |      | MUL |         |       |
| buyerID   | varchar(10)          |      |     |         |       |
| id        | varchar(20)          |      |     |         |       |
| title     | varchar(100)         |      |     |         |       |
| author    | varchar(100)         |      |     |         |       |
| isbn      | varchar(13)          | YES  |     | NULL    |       |
| condition | char(1)              |      |     | 0       |       |
| price     | char(3)              |      |     |         |       |
| comment   | varchar(100)         | YES  |     | NULL    |       |
| college   | smallint(5) unsigned |      |     | 0       |       |
| date      | varchar(10)          |      |     |         |       |
+-----------+----------------------+------+-----+---------+-------+
11 rows in set (0.00 sec)
*/
public class oldBooksTable{
	//expiration of each record
	public static final int EXPIRATION = 100;
	//table field names
	public static final String SELLERID = "sellerID";
	public static final String BUYERID = "buyerID";
	public static final String BOOKID = "id";
	public static final String TITLE = "title";
	public static final String AUTHOR = "author";
	public static final String ISBN = "isbn";
	public static final String CONDITION = "condition";
	public static final String PRICE = "price";
	public static final String COMMENT = "comment";
	public static final String COLLEGE = "college";
	public static final String DATE = "date";
	//use booksTable/usersTable length parameters
	private String sellerID;
	private String buyerID;
	private String bookID;
	private String title;
	private String author;
	private String isbn;
	private int condition;
	private int price;
	private String comment;
	private int collegeID;
	private String date;
	//table will be used to test format
	usersTable tempUsers = new usersTable();
	booksTable tempBooks = new booksTable();
	public void setSellerID(String s) 
		throws FormatException{
		tempUsers.setID(s);
		sellerID = tempUsers.getID();
	} //end setSellerID
	public void setBuyerID(String s)
		throws FormatException{
		tempUsers.setID(s);
		buyerID = tempUsers.getID();
	} //end setBuyerID
	public void setBookID(String s)
		throws FormatException{
		tempBooks.setID(s);
		bookID = tempBooks.getID();
	} //end setBookID
	public void setTitle(String s)
		throws FormatException{
		tempBooks.setTitle(s);
		title = tempBooks.getTitle();
	} //end setTitle
	public void setAuthor(String s)
		throws FormatException{
		tempBooks.setAuthor(s);
		author = tempBooks.getAuthor();
	} //end setAuthor
	public void setISBN(String s)
		throws FormatException{
		tempBooks.setISBN(s);
		isbn = tempBooks.getISBN();
	} //end setISBN
	public void setCondition(String s)
		throws FormatException{
		tempBooks.setCondition(s);
		condition = tempBooks.getCondition();
	} //end setCondition
	public void setPrice(String s)
		throws FormatException{
		tempBooks.setPrice(s);
		price = tempBooks.getPrice();
	} //end setPrice
	public void setComment(String s)
		throws FormatException{
		tempBooks.setComment(s);
		comment = tempBooks.getComment();
	} //end setComment
	public void setCollegeID(String s)
		throws FormatException{
		tempBooks.setCollegeID(s);
		collegeID = tempBooks.getCollegeID();
	} //end setCollegeID
	public void setDate(String s)
		throws FormatException{
		tempBooks.setDate(s);
		date = tempBooks.getDate();
	} //end setDate
	public String getSellerID(){
		return sellerID;
	} //end getSellerID
	public String getBuyerID(){
		return buyerID;
	} //end getBuyerID
	public String getBookID(){
		return bookID;
	} //end getBookID
	public String getTitle(){
		return title;
	} //end getTitle
	public String getAuthor(){
		return author;
	} //end getAuthor
	public String getISBN(){
		return isbn;
	} //end getISBN
	public int getCondition(){
		return condition;
	} //end getCondition
	public int getPrice(){
		return price;
	} //end getPrice
	public String getComment(){
		return comment;
	} //end getComment
	public int getCollegeID(){
		return collegeID;
	} //end getCollegeID
	public String getDate(){
		return date;
	} //end getDate
}
