package server;
/*
mysql> describe tablebookstoretemp;
+---------------+--------------+------+-----+---------+-------+
| Field         | Type         | Null | Key | Default | Extra |
+---------------+--------------+------+-----+---------+-------+
| bookstoreid   | varchar(5)   |      | PRI |         |       |
| firstname     | varchar(30)  |      |     |         |       |
| lastname      | varchar(30)  |      |     |         |       |
| bookstorename | varchar(100) |      |     |         |       |
| contactinfo   | varchar(200) |      |     |         |       |
| date          | varchar(10)  |      |     |         |       |
| address       | varchar(255) |      |     |         |       |
| moreinfo      | varchar(255) | YES  |     | NULL    |       |
| website       | varchar(100) | YES  |     | NULL    |       |
| email         | varchar(60)  |      |     |         |       |
+---------------+--------------+------+-----+---------+-------+
11 rows in set (0.00 sec)
 */
public interface bookstoreTempTable{
	public static final int EXPIRATION = 10;
	public static final String BOOKSTOREID = "bookstoreid";
	public static final String FIRSTNAME = "firstname";
	public static final String LASTNAME = "lastname";
	public static final String STORENAME = "bookstorename";
	public static final String CONTACTINFO = "contactinfo";
	public static final String ADDRESS = "address";
	public static final String MOREINFO = "moreinfo";
	public static final String WEBSITE = "website";
	public static final String DATE = "date";
	public static final String EMAIL = "email";
}
