package server;
/*
mysql> describe tablefeedback;
+--------------+--------------+------+-----+---------+-------+
| Field        | Type         | Null | Key | Default | Extra |
+--------------+--------------+------+-----+---------+-------+
| ownerID      | varchar(10)  |      | MUL |         |       |
| traderID     | varchar(10)  |      |     |         |       |
| bookID       | varchar(20)  |      |     |         |       |
| positiveness | tinyint(1)   |      |     | 0       |       |
| feedback     | varchar(100) |      |     |         |       |
| date         | varchar(10)  |      |     |         |       |
+--------------+--------------+------+-----+---------+-------+
6 rows in set (0.00 sec)
 */
public class feedbackTable{
	public static final int POSITIVE = 1;
	public static final int NEUTRAL = 0;
	public static final int NEGATIVE = -1;
	public static final String OWNERID = "ownerID";
	public static final String TRADERID = "traderID";
	public static final String BOOKID = "bookID";
	public static final String POSITIVENESS 
		= "positiveness";
	public static final String FEEDBACK = "feedback";
	public static final String DATE = "date";
	public static final int FEEDBACK_MAX = 100;
	private String ownerID;
	private String traderID;
	private String bookID;
	private int positiveness;
	private String feedback;
	private String date;
	usersTable tempUsers = new usersTable();
	public void setOwnerID(String s)
		throws FormatException{
		tempUsers.setID(s);
		ownerID = tempUsers.getID();
	} //end setOwnerID
	public void setTraderID(String s)
		throws FormatException{
		tempUsers.setID(s);
		traderID = tempUsers.getID();
	} //end setTraderID
	public void setBookID(String s)
		throws FormatException{
		booksTable tempBooks = new booksTable();
		tempBooks.setID(s);
		bookID = tempBooks.getID();
	} //end setBookID
	public void setPositiveness(int s)
		throws FormatException{
		if (positiveness != 1 && positiveness != 0
			&& positiveness != -1){
			throw new FormatException("bad feedback rate");
		}
		positiveness = s;
	} //end setPositiveness
	public void setFeedback(String s)
		throws FormatException{
		if (s == null || s.trim().equals("")){
			feedback = null;
		}else if (s.trim().length()>FEEDBACK_MAX){
			throw new FormatException(fieldMessages
				.outOfBounds("feedback",0,FEEDBACK_MAX));
		}else if (!tools.allASCII(s)){
			throw new FormatException(fieldMessages
				.illegalChars("feedback",
				"english characters/symbols"));
		}else{
			feedback = s.trim();
		}
	} //end setFeedback
	public void setDate(String s)
		throws FormatException{
		date = s;
	} //end setDate
	public String getOwnerID(){
		return ownerID;
	} //end getOwnerID
	public String getTraderID(){
		return traderID;
	} //end getTraderID
	public String getBookID(){
		return bookID;
	} //end getBookID
	public int getPositiveness(){
		return positiveness;
	} //end getPositiveness
	public String getFeedback(){
		return feedback;
	} //end getFeedback
	public String getDate(){
		return date;
	} //end getDate
}
