package server;
/*
mysql> describe tablebookstoreinfo;
+---------------+--------------+------+-----+---------+-------+
| Field         | Type         | Null | Key | Default | Extra |
+---------------+--------------+------+-----+---------+-------+
| bookstoreid   | varchar(5)   |      | PRI |         |       |
| firstname     | varchar(30)  |      |     |         |       |
| lastname      | varchar(30)  |      |     |         |       |
| bookstorename | varchar(50)  |      |     |         |       |
| contactinfo   | varchar(200) |      |     |         |       |
| lastupdated   | varchar(10)  |      |     |         |       |
| address       | varchar(255) |      |     |         |       |
| moreinfo      | varchar(255) |      |     |         |       |
| website       | varchar(100) |      |     |         |       |
+---------------+--------------+------+-----+---------+-------+
9 rows in set (0.00 sec)
 */
public interface bookstoreInfoTable{
	public static final String BOOKSTOREID = "bookstoreid";
	public static final String FIRSTNAME = "firstname";
	public static final String LASTNAME = "lastname";
	public static final String STORENAME = "bookstorename";
	public static final String CONTACTINFO = "contactinfo";
	public static final String LASTUPDATED = "lastupdated";
	public static final String ADDRESS = "address";
	public static final String MOREINFO = "moreinfo";
	public static final String WEBSITE = "website";
	
	public static final int BOOKSTOREID_LENGTH = 5;
	public static final int FIRSTNAME_MAX = 30;
	public static final int LASTNAME_MAX = 30;
	public static final int STORENAME_MAX = 50;
	public static final int CONTACTINFO_MAX = 200;
	public static final int ADDRESSINFO_MAX = 255;
	public static final int MOREINFO_MAX = 255;
	public static final int WEBSITEINFO_MAX = 100;
	/*
	 *all parameters can be returned using String[]
	 *with the following index standard
	 */
	public static final int BOOKSTOREID_FIELD = 0;
	public static final int FIRSTNAME_FIELD = 1;
	public static final int LASTNAME_FIELD = 2;
	public static final int STORENAME_FIELD = 3;
	public static final int CONTACTINFO_FIELD = 4;
	public static final int LASTUPDATED_FIELD = 5;
	public static final int ADDRESS_FIELD = 6;
	public static final int MOREINFO_FIELD = 7;
	public static final int WEBSITE_FIELD = 8;
	public static final String[] fieldNames = new String[]{
		BOOKSTOREID,FIRSTNAME,LASTNAME,STORENAME,
		CONTACTINFO,LASTUPDATED,ADDRESS,MOREINFO,WEBSITE
	};
}
