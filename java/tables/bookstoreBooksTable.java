package server;
/*
mysql> describe tablebookstorebooks;
+------------+--------------+------+-----+---------+-------+
| Field      | Type         | Null | Key | Default | Extra |
+------------+--------------+------+-----+---------+-------+
| storeid    | varchar(5)   |      |     |         |       |
| bookid     | varchar(10)  |      | PRI |         |       |
| title      | varchar(100) |      | MUL |         |       |
| author     | varchar(100) |      |     |         |       |
| isbn       | varchar(13)  |      |     |         |       |
| newprice   | varchar(5)   | YES  |     | NULL    |       |
| usedprice  | varchar(5)   | YES  |     | NULL    |       |
| conditions | varchar(7)   |      |     |         |       |
| copies     | char(3)      |      |     |         |       |
| comment    | varchar(100) | YES  |     | NULL    |       |
| date       | varchar(10)  |      |     |         |       |
+------------+--------------+------+-----+---------+-------+
11 rows in set (0.00 sec)
*/
public class bookstoreBooksTable extends Table{
	//expiration of each record
	public static final int EXPIRATION = 90;
	//table field names:
	public static final String BOOKID 
		= "bookid"; //uniquely identifies each book
	public static final String STOREID 
		= "storeid"; //uniquely identifies each store
	public static final String TITLE = "title";
	public static final String AUTHOR = "author";
	public static final String ISBN = "isbn";
	public static final String PRICENEW = "newprice";
	public static final String PRICEUSED = "usedprice";
	public static final String CONDITIONS = "conditions";
	public static final String COMMENT = "comment";
	public static final String COPIES = "copies";
	public static final String DATE = "date";
	//field table limitations:
	public static final int CONDITION_ACCEPTABLE = 0;
	public static final int CONDITION_GOOD = 1;
	public static final int CONDITION_LIKENEW = 2;
	public static final int CONDITION_NEW = 3;
	public static final String[] CONDITION_NAMES 
		= new String[]{"acceptable","good","like new","new"};
	public static final int BOOKID_LENGTH = 10;
	public static final int TITLE_MAX = 100;
	public static final int AUTHOR_MAX = 100;
	public static final int ISBN_LENGTH1 = 10;
	public static final int ISBN_LENGTH2 = 13;
	public static final int PRICE_MAX = 5;
	public static final int COMMENT_MAX = 100;
	//storage values
	private String bookid;
	private String title;
	private String isbn;
	private String usedPrice;
	private String newPrice;
	private String author;
	private String[] conditions;
	private String comment;
	private String storeid;
	private String date;
	private String copies;
	public void setID(String s){
		bookid = s;
	} //end setID
	public void setStoreID(String s)
		throws FormatException{
		storeid = s;
	} //end setStoreID
	public void setTitle(String s) 
		throws FormatException{
		if (s == null || s.trim().length()>TITLE_MAX 
			|| s.trim().length() == 0){
			throw new FormatException(
				fieldMessages.outOfBounds("title",0,
				TITLE_MAX));
		}else if (!tools.allASCII(s)){
			throw new FormatException(
				fieldMessages.illegalChars("title",
				"a-z,A-Z"));
		}
		title = s.trim();
	} //end setTitle
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
	public void setISBN(String s)
		throws FormatException{
		if (s == null){
			isbn = null;
		}else if (s.trim().length()!=ISBN_LENGTH1
			&& s.trim().length()!=ISBN_LENGTH2){
			throw new FormatException("isbn must be either "
				+10+" or "+13+" characters long");
		}else if (!tools
			.lettersAndDigitsOnly(s,null,null)){
			throw new FormatException(fieldMessages
				.illegalChars("ISBN","A-Z,0-9"));
		}
		isbn = tools.trim(s);
	} //end setISBN
	public void setUsedPrice(String s)
		throws FormatException{
		if (s != null){
			try{
				Double.parseDouble(s); //check that it is number
				if (s.trim().length()>PRICE_MAX){
					throw new Exception();
				}
				usedPrice = s;
			}catch (Exception e){
				throw new FormatException(
					"bad used price format");
			}
		}else{
			usedPrice = null;
		}
	} //end setUsedPrice
	public void setNewPrice(String s)
		throws FormatException{
		if (s != null){
			try{
				Double.parseDouble(s); //check that it is number
				if (s.trim().length()>PRICE_MAX){
					throw new Exception();
				}
				newPrice = s;
			}catch (Exception e){
				throw new FormatException(
					"bad new price format");
			}
		}else{
			newPrice = null;
		}
	} //end setNewPrice
	public void setCopies(String s)
		throws FormatException{
		if (s == null || s.trim().equals("")){
			throw new FormatException("copies field cannot be empty");
		}
		try{
			Double.parseDouble(s);
		}catch(Exception e){
			throw new FormatException("please enter a digit for the # of copies available");
		}
		copies = s.trim();
	} //end setCopies
	public void setCondition(String[] s)
		throws FormatException{
		if (s==null){
			throw new FormatException("please specify book conditions available");
		}
		conditions = s;
	} //end setCondition
	public void setComment(String s)
		throws FormatException{
		if (s == null){
			comment = null;
		}else if (s.trim().length()>COMMENT_MAX){
			throw new FormatException(fieldMessages
				.outOfBounds("comments",0,COMMENT_MAX));
		}else if (!tools.allASCII(s)){
			throw new FormatException(
				fieldMessages.illegalChars("comments",
				"a-z,A-Z,0-9"));
		}
		comment = tools.trim(s);
	} //end setComment
	public void setDate(String s)
		throws FormatException{
		if (!tools.isGoodDate(s)){
			throw new FormatException("bad date format");
		}
		date = s;
	} //end setDate
	
	/********** GET METHODS************/
	public String getID(){
		return bookid;
	}
	public String getStoreID(){
		return storeid;
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
	public String getNewPrice(){
		return newPrice;
	}
	public String getUsedPrice(){
		return usedPrice;
	}
	public String getCopies(){
		return copies;
	}
	public String[] getCondition(){
		return conditions;
	}
	public String getComment(){
		return comment;
	}
	public String getDate(){
		return date;
	}
	public String toString(){
		return "bookstoreBooksTable";
	}
}
