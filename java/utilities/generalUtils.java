package server;
import java.sql.*;
public class generalUtils implements Tables{
	Statement statement;
	protected generalUtils(Connection con) 
		throws SQLException{
		try{
			statement = con.createStatement();
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	}
	/*
	 *gets book info based on isbn
	 *returns: isbnTable
	 */
	protected static isbnTable getBookInfo(String isbn,Connection con) throws SQLException
	{
		try
		{
			isbnTable table = null;
			try{
				table = new isbnTable();
				table.setISBN(isbn);
			}catch (FormatException e){
				return null;
			}
			Statement st = con.createStatement();	
			ResultSet rs = null;
			rs = st.executeQuery("SELECT * FROM "
				+ISBNSTABLE+" WHERE "+isbnTable.ISBN
				+" = '"+table.getISBN()+"' LIMIT 1");
			if(rs.next()){
				table.setTitle(rs.getString(
					isbnTable.TITLE));
				table.setAuthor(rs.getString(
					isbnTable.AUTHOR));
				table.setPublisher(rs.getString(
					isbnTable.PUBLISHER));
				table.setSimage(rs.getString(
					isbnTable.SIMAGE));
				table.setMimage(rs.getString(
					isbnTable.MIMAGE));
				table.setBinding(rs.getString(
					isbnTable.BINDING));
				table.setListPrice(rs.getString(
					isbnTable.LISTPRICE));
				table.setPages(rs.getString(
					isbnTable.PAGES));
				return table;
        	}
        	else
        		return null;
		}catch (Exception e){
			log.writeException(e.getMessage());
			throw new SQLException(e.getMessage());	
		}
	} //end getBookInfo
	protected static booksTable getBook(String id,
		Connection con) throws SQLException{
		try{
			Statement st = con.createStatement();
			ResultSet rs = null;
			rs = st.executeQuery("SELECT * FROM "
				+TABLEBOOKS+" WHERE "+booksTable.ID
				+" = '"+id+"' LIMIT 1");
			//if found
			if (rs.next()){
				booksTable table = new booksTable(id);
				table.setTitle(rs.getString(booksTable.TITLE));
				table.setAuthor(rs.getString(booksTable.AUTHOR));
				table.setISBN(rs.getString(booksTable.ISBN));
				table.setPrice(rs.getString(booksTable.PRICE));
				table.setCondition(rs.getString(booksTable.CONDITION));
				table.setComment(rs.getString(booksTable.COMMENT));
				table.setSellerID(rs.getString(booksTable.SELLERID));
				table.setDate(rs.getString(booksTable.DATE));
				table.setCourseID(rs.getString(booksTable.COURSE));
				table.setTeacherID(rs.getString(booksTable.TEACHER));
				table.setDepartmentID(rs.getString(booksTable.DEPARTMENT));
				table.setCollegeID(rs.getString(booksTable.COLLEGE));
				return table;
			}else{
				return null; //not found
			}
		}catch(Exception e){
			log.writeException(e.getMessage());
			throw new SQLException(e.getMessage());
		}
	} //end getBook
	protected static oldBooksTable getOldBook(String id,
		Connection con) throws SQLException{
		try{
			Statement st = con.createStatement();
			ResultSet rs = null;
			rs = st.executeQuery("SELECT * FROM "
				+TABLEOLDBOOKS+" WHERE "+oldBooksTable.BOOKID
				+" = '"+id+"' LIMIT 1");
			//if found
			if (rs.next()){
				oldBooksTable table = new oldBooksTable();
				table.setBookID(id);
				table.setTitle(rs.getString(oldBooksTable.TITLE));
				table.setAuthor(rs.getString(oldBooksTable.AUTHOR));
				table.setISBN(rs.getString(oldBooksTable.ISBN));
				table.setPrice(rs.getString(oldBooksTable.PRICE));
				table.setCondition(rs.getString(oldBooksTable.CONDITION));
				table.setComment(rs.getString(oldBooksTable.COMMENT));
				table.setSellerID(rs.getString(oldBooksTable.SELLERID));
				table.setBuyerID(rs.getString(oldBooksTable.BUYERID));
				table.setDate(rs.getString(oldBooksTable.DATE));
				table.setCollegeID(rs.getString(oldBooksTable.COLLEGE));
				return table;
			}else{
				return null; //not found
			}
		}catch(Exception e){
			log.writeException(e.getMessage());
			throw new SQLException(e.getMessage());
		}
	} //end getOldBook
	protected collegeTable getCollege(String id)
		throws SQLException{
		try{
			ResultSet rs = null;
			rs = statement.executeQuery("SELECT * FROM "
				+TABLECOLLEGES+" WHERE "+collegeTable.ID
				+" = "+id+" LIMIT 1");
			//if found
			if (rs.next()){
				collegeTable table = new collegeTable();
				table.setID(id);
				table.setShort(rs.getString(
					collegeTable.SHORTNAME));
				table.setFull(rs.getString(
					collegeTable.FULLNAME));
				return table;
			}else{
				return null; //not found
			}
		}catch(Exception e){
			log.writeException(e.getMessage());
			throw new SQLException(e.getMessage());
		}
	} //end getCollege
	protected departmentTable getDepartment(String id)
		throws SQLException{
		try{
			ResultSet rs = null;
			rs = statement.executeQuery("SELECT * FROM "
				+TABLEDEPARTMENTS+" WHERE "
				+departmentTable.ID+" = "+id+" LIMIT 1");
			//if found
			if (rs.next()){
				departmentTable table = new departmentTable();
				table.setID(id);
				table.setName(rs.getString(
					departmentTable.NAME));
				table.setCollegeID(rs.getString(
					departmentTable.COLLEGEID));
				return table;
			}else{
				return null; //not found
			}
		}catch(Exception e){
			log.writeException(e.getMessage());
			throw new SQLException(e.getMessage());
		}
	} //end getDepartment
	protected courseTable getCourse(String id)
		throws SQLException{
		try{
			ResultSet rs = null;
			rs = statement.executeQuery("SELECT * FROM "
				+TABLECOURSES+" WHERE "
				+courseTable.ID+" = "+id+" LIMIT 1");
			//if found
			if (rs.next()){
				courseTable table = new courseTable();
				table.setID(id);
				table.setName(rs.getString(
					courseTable.NAME));
				table.setDepartmentID(rs.getString(
					courseTable.DEPARTMENTID));
				table.setCollegeID(rs.getString(
					courseTable.COLLEGEID));
				return table;
			}else{
				return null; //not found
			}
		}catch(Exception e){
			log.writeException(e.getMessage());
			throw new SQLException(e.getMessage());
		}
	} //end getCourse
	protected teacherTable getTeacher(String id)
		throws SQLException{
		try{
			ResultSet rs = null;
			rs = statement.executeQuery("SELECT * FROM "
				+TABLETEACHERS+" WHERE "
				+teacherTable.ID+" = "+id+" LIMIT 1");
			//if found
			if (rs.next()){
				teacherTable table = new teacherTable();
				table.setID(id);
				table.setName(rs.getString(
					teacherTable.NAME));
				table.setCollegeID(rs.getString(
					teacherTable.COLLEGEID));
				return table;
			}else{
				return null; //not found
			}
		}catch(Exception e){
			log.writeException(e.getMessage());
			throw new SQLException(e.getMessage());
		}
	} //end getTeacher
}
