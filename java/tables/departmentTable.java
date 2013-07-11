package server;
/*
+-----------+----------------------+------+-----+---------+----------------+
| Field     | Type                 | Null | Key | Default | Extra          |
+-----------+----------------------+------+-----+---------+----------------+
| id        | smallint(5) unsigned |      | PRI | NULL    | auto_increment |
| name      | varchar(80)          |      |     |         |                |
| collegeID | smallint(5) unsigned |      | MUL | 0       |                |
+-----------+----------------------+------+-----+---------+----------------+
3 rows in set (0.00 sec)
*/
public class departmentTable extends Table{
	//field names:
	//automatically generated id of this department
	public static final String ID = "id";
	//title of this department
	public static final String NAME = "name";
	//college that this department is in
	public static final String COLLEGEID = "collegeID";
	//length values:
	public static final int NAME_MAX = 80;
	//variables
	private int id = 0; //department id
	private String name; //department name
	private int collegeID = 0; //college id that this dep. belongs to
	public void setID(String s)
		throws FormatException{
		if (s == null || s.equals("")){
			id = 0;
		}else{
			try{
				id = Integer.parseInt(s);
			}catch(NumberFormatException e){
				throw new FormatException(fieldMessages
					.illegalChars("department id","0-9"));
			}
		}
	}
	public void setName(String s)
		throws FormatException{
		if (s == null || s.trim().equals("")){
			name = null;
		}else{
			if (!tools.allLetters(s,"\\s",null)){
				throw new FormatException(fieldMessages
					.illegalChars("department name",
					"a-z,A-Z"));
			}
			if (s.trim().length()>NAME_MAX){
				throw new FormatException(fieldMessages
					.outOfBounds("department name",0,
					NAME_MAX));
			}
			name = s.trim();
		}
	}
	public void setCollegeID(String s)
		throws FormatException{
		collegeTable test = new collegeTable();
		test.setID(s);
		collegeID = test.getID();
	}
	/********* GET METHODS ************/
	public int getID(){
		return id;
	}
	public String getName(){
		return name;
	}
	public int getCollegeID(){
		return collegeID;
	}
	public boolean isComplete(){
		try{
			if (name.length() == 0 || collegeID == 0){
				return false;
			}
			return true;
		}catch (NullPointerException e){
			return false;
		}
	}
	public String toString(){
		return "departmentTable";
	}
}
