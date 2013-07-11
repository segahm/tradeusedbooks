package server;
import java.sql.*;
public class resultList implements Tables{
	private ResultSet rs;
	private int option;
	public static final int COLLEGELIST = 1;
	public static final int DEPARTMENTLIST = 2;
	public static final int COURSELIST = 3;
	public static final int TEACHERLIST = 4;
	public static final int FEEDBACKLIST = 5;
	public resultList(ResultSet r,int opt){
		this.rs = r;
		this.option = opt;
	} //end constructor
	public int getOption(){
		return option;
	}
	/*hasNext:
	 *should never be called
	 */
	public boolean hasNext(){
		return true;
	} //end hasNext
	public Object next(){
		try{
			if (rs.next()){

				if (option == COLLEGELIST){
					return getNextCollege();
				}
				if (option == DEPARTMENTLIST){
					return getNextDepartment();
				}
				if (option == COURSELIST){
					return getNextCourse();
				}
				if (option == TEACHERLIST){
					return getNextTeacher();
				}
				if (option == FEEDBACKLIST){
					return getNextFeedback();
				}
				return null; //default
			}else{
				return null;
			}
		}catch (SQLException e){
			log.writeException(e.getMessage());
			return null;
		}
	} //end next
	private collegeTable getNextCollege() throws SQLException{
		try{
			collegeTable table = new collegeTable();
			table.setID(rs.getString(table.ID));
			table.setShort(rs.getString(table.SHORTNAME));
			table.setFull(rs.getString(table.FULLNAME));
			return table;
		}catch(FormatException ignore){
			log.writeLog(ignore.getMessage());
			return null;
		}catch(SQLException e){
			log.writeException(e.getMessage());
			return null;
		}
	} //end getNextCollege
	private departmentTable getNextDepartment() 
		throws SQLException{
		try{
			departmentTable table = new departmentTable();
			table.setID(rs.getString(table.ID));
			table.setName(rs.getString(table.NAME));
			table.setCollegeID(rs.getString(table.COLLEGEID));
			return table;
		}catch(FormatException ignore){
			log.writeLog(ignore.getMessage());
			return null;
		}catch(SQLException e){
			log.writeException(e.getMessage());
			return null;
		}
	} //end getNextDepartment
	private courseTable getNextCourse() 
		throws SQLException{
		try{
			courseTable table = new courseTable();
			table.setID(rs.getString(table.ID));
			table.setName(rs.getString(table.NAME));
			table.setDepartmentID(rs.getString(table.DEPARTMENTID));
			table.setCollegeID(rs.getString(table.COLLEGEID));
			return table;
		}catch(FormatException ignore){
			log.writeLog(ignore.getMessage());
			return null;
		}catch(SQLException e){
			log.writeException(e.getMessage());
			return null;
		}
	} //end getNextCourse
	private teacherTable getNextTeacher() 
		throws SQLException{
		try{
			teacherTable table = new teacherTable();
			table.setID(rs.getString(table.ID));
			table.setName(rs.getString(table.NAME));
			table.setCollegeID(rs.getString(table.COLLEGEID));
			return table;
		}catch(FormatException ignore){
			log.writeLog(ignore.getMessage());
			return null;
		}catch(SQLException e){
			log.writeException(e.getMessage());
			return null;
		}
	} //end getNextTeacher
	private Object[] getNextFeedback() 
		throws SQLException{
		try{
			feedbackTable table = new feedbackTable();
			table.setOwnerID(rs.getString(TABLEFEEDBACK+"."+feedbackTable.OWNERID));
			table.setTraderID(rs.getString(TABLEFEEDBACK+"."+feedbackTable.TRADERID));
			table.setBookID(rs.getString(TABLEFEEDBACK+"."+feedbackTable.BOOKID));
			table.setPositiveness(rs.getInt(TABLEFEEDBACK+"."+feedbackTable.POSITIVENESS));
			table.setFeedback(rs.getString(TABLEFEEDBACK+"."+feedbackTable.FEEDBACK));
			table.setDate(rs.getString(TABLEFEEDBACK+"."+feedbackTable.DATE));
			return new Object[]{table,rs.getString(
				USERSTABLE+"."+usersTable.USERNAME)};
		}catch(FormatException codeError){
			log.writeLog(codeError.getMessage());
			return null;
		}catch(SQLException e){
			log.writeException(e.getMessage());
			return null;
		}
	} //end getNextFeedback
	
	
}
