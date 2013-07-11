package server;
/*
mysql> describe tablecourses;
+--------------+----------------------+------+-----+---------+----------------+
| Field        | Type                 | Null | Key | Default | Extra          |
+--------------+----------------------+------+-----+---------+----------------+
| id           | smallint(5) unsigned |      | PRI | NULL    | auto_increment |
| name         | varchar(50)          |      |     |         |                |
| departmentID | smallint(5) unsigned |      | MUL | 0       |                |
| collegeID    | smallint(5) unsigned |      | MUL | 0       |                |
+--------------+----------------------+------+-----+---------+----------------+
4 rows in set (0.00 sec)
 */
public class courseTable extends Table{
	//field names:
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String DEPARTMENTID 
		= "departmentID";
	public static final String COLLEGEID = "collegeID";
	//length values:
	public static final int NAME_MAX = 50;
	//variables:
	private int id = 0;
	private String name;
	private int departmentID = 0;
	private int collegeID = 0;
	public void setID(String s)
		throws FormatException{
		if (s == null || s.trim().equals("")){
			id = 0;
		}else{
			try{
				id = Integer.parseInt(s);
			}catch(NumberFormatException e){
				throw new FormatException(
					fieldMessages
					.illegalChars("course id","0-9"));
			}
		}
	} //end setID
	public void setName(String s)
		throws FormatException{
		if (s == null || s.trim().equals("")){
			name = null;
		}else{
			if (!tools.lettersAndDigitsOnly(s,"\\s",null)){
				throw new FormatException(fieldMessages
					.illegalChars("course title","a-z,A-Z"));
			}
			if (s.trim().length()>NAME_MAX){
				throw new FormatException(fieldMessages
					.outOfBounds("course title",0,
					NAME_MAX));
			}
			name = s.trim();
		}	
	} // end setName
	public void setDepartmentID(String s)
		throws FormatException{
		departmentTable test = new departmentTable();
		test.setID(s);
		departmentID = test.getID();
	} //end setDepartmentID
	public void setCollegeID(String s)
		throws FormatException{
		collegeTable test = new collegeTable();
		test.setID(s);
		collegeID = test.getID();
	} //end setCollegeID
	/********** GET METHODS ***********/
	public int getID(){
		return id;
	} // end getID
	public String getName(){
		return name;
	} // end getName
	public int getDepartmentID(){
		return departmentID;
	} //end getDepartmentID
	public int getCollegeID(){
		return collegeID;
	}
	public boolean isComplete(){
		try{
			if (name.length()==0 || collegeID==0 
				|| departmentID==0){
				return false;
			}
			return true;
		}catch(NullPointerException e){
			return false;
		}
	}
	public String toString(){
		return "courseTable";
	}
}