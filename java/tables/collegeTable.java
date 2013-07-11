package server;
public class collegeTable extends Table{
	//field names:
	public static final String ID = "id";
	public static final String SHORTNAME = "shortname";
	public static final String FULLNAME = "longname";
	//field's limitations
	public static final int SHORTNAME_MAX = 20;
	public static final int FULLNAME_MAX = 60;
	//variables:
	private int id = 0;
	private String shortName;
	private String fullName;
	public void setID(String s)
		throws FormatException{
		if (s == null || s.trim().equals("")){
			id = 0;
		}else{
			try{
				id = Integer.parseInt(s);
			}catch(NumberFormatException e){
				throw new FormatException(fieldMessages
					.illegalChars("college id","0-9"));
			}
		}
	} //end setID
	public void setShort(String s)
		throws FormatException{
		if (s == null || s.trim().equals("")){
			shortName = null;
		}else{
			if (!tools.allLetters(s,"\\s",null)){
				throw new FormatException(
					"college's short name may only include letters");
			}
			if (s.trim().length()>SHORTNAME_MAX){
				throw new FormatException(fieldMessages
					.outOfBounds("college's short name",0,
					SHORTNAME_MAX));
			}
			shortName = s.trim();
		}
	} //end setShort
	public void setFull(String s)
		throws FormatException{
		if (s == null || s.trim().equals("")){
			fullName = null;
		}else{
			if (!tools.allLetters(s,"\\s",null)){
				throw new FormatException(
					"college's full name may only include letters");
			}
			if (s.trim().length()>FULLNAME_MAX){
				throw new FormatException(fieldMessages
					.outOfBounds("college's full name",0,
					FULLNAME_MAX));
			}
			fullName = s.trim();
		}
	} //end setFull
	/***** GET METHOD **********/
	public int getID(){
		return id;
	}
	public String getShort(){
		return shortName;
	}
	public String getFull(){
		return fullName;
	}
	public boolean isComplete(){
		try{
			if (shortName.length() == 0 
				|| fullName.length() == 0){
				return false;	
			}
			return true;
		}catch (NullPointerException e){
			return false;
		}
	}
	public String toString(){
		return "collegeTable";
	}
}
