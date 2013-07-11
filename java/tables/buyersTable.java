package server;
/*
mysql> describe tablebuyers;
+--------+-------------+------+-----+---------+-------+
| Field  | Type        | Null | Key | Default | Extra |
+--------+-------------+------+-----+---------+-------+
| bookID | varchar(20) |      | MUL |         |       |
| buyer  | varchar(10) |      | MUL |         |       |
| date   | varchar(10) |      |     |         |       |
+--------+-------------+------+-----+---------+-------+
3 rows in set (0.00 sec)
*/
public class buyersTable{
	//expiration of each record
	public static final int EXPIRATION = 30;
	public static final String BOOKID = "bookID";
	public static final String BUYERID = "buyer";
	public static final String DATE = "date";
	public static final int BOOKID_LENGTH = booksTable.ID_LENGTH;
	public static final int BUYERID_LENGTH = usersTable.ID_LENGTH;
	private String bookID;
	private String buyer;
	private String date;
	public void setBookID(String s) throws FormatException{
		booksTable temp = new booksTable(s);
		bookID = temp.getID();
	} //end setBookID
	public void setBuyerID(String s) throws FormatException{
		usersTable temp = new usersTable();
		temp.setID(s);
		buyer = temp.getID();
	} //end setBuyerID
	public void setDate(String s){
		date = s;
	} //end setDate
	public String getBookID(){
		return bookID;
	} //end getBookID
	public String getBuyerID(){
		return buyer;
	} //end getBuyerID
	public String getDate(){
		return date;
	} //end getDate
}
