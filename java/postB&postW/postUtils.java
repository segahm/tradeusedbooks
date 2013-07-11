package server;
import java.sql.*;
import java.io.*;
public class postUtils implements Tables{
	Statement statement;
	Connection connection; //preferably not to use this
	protected postUtils(Connection con) throws SQLException{
		try{
			connection = con;
			statement = connection.createStatement();
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	}
	/*
	 *although this method supports a qeury request for
	 *any field, it is advisable not to use it
	 *with fields other than isbn/title/author
	 *returns: isbnTable
	 */
	protected static isbnTable getBookInfo(String query,
		String field,Connection con) throws SQLException
	{
		try
		{
			if (query == null || field == null){
				return null;
			}
			Statement st = con.createStatement();	
			ResultSet rs = null;
			rs = st.executeQuery("SELECT * FROM "
				+ISBNSTABLE+" WHERE "+field
				+" = "+sqlUtils.toSQLString(query)+" OR "+field+" LIKE "
				+sqlUtils.toSQLString("%"+query+"%"));
			if(rs.next())
			{
				isbnTable table = new isbnTable();
				table.setISBN(rs.getString(
					isbnTable.ISBN));
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
		}
		catch (Exception e)
		{
			log.writeException(e.getMessage());
			throw new SQLException(e.getMessage());	
		}
	} //end getBookInfo
	public void postForm(postForm form,User user)
		throws Exception,UserException{
		try{
			//first confirm user authentication
			if (!userUtils.confirmUserWithEncrypted(
				user.getID(),user.getEncrypted(),connection)){
				throw new UserException("bad authentication, can't proceed");
			}
			form.finalize();
			booksTable books = new booksTable();
			//all the required fields are asumed to != null
			if (form.getCollegeID() != 0){
				books.setCollegeID(form.getCollegeID()+"");
			}else{
				collegeTable temp = new collegeTable();
				temp.setShort(form.getCollegeShort());
				temp.setFull(form.getCollegeFull());
				form.setCollegeID(addCollege(temp)+"");
				books.setCollegeID(form.getCollegeID()+"");
			}
			if (form.getDepartmentID() != 0){
				books.setDepartmentID(form.getDepartmentID()+"");
			}else if (form.getDepartment()!=null){
				departmentTable temp = new departmentTable();
				temp.setName(form.getDepartment());
				temp.setCollegeID(form.getCollegeID()+"");
				form.setDepartmentID(addDepartment(temp)+"");
				books.setDepartmentID(form.getDepartmentID()+"");
			}
			if (form.getDepartmentID() != 0){
				if (form.getCourseID() != 0){
					books.setCourseID(form.getCourseID()+"");
				}else if (form.getCourse()!=null){
					courseTable temp = new courseTable();
					temp.setName(form.getCourse());
					temp.setCollegeID(form.getCollegeID()+"");
					temp.setDepartmentID(form.getDepartmentID()+"");
					form.setCourseID(addCourse(temp)+"");
					books.setCourseID(form.getCourseID()+"");
				}
			}
			if (form.getTeacherID() != 0){
				books.setTeacherID(form.getTeacherID()+"");
			}else if (form.getTeacher()!=null){
				teacherTable temp = new teacherTable();
				temp.setCollegeID(form.getCollegeID()+"");
				temp.setName(form.getTeacher());
				form.setTeacherID(addTeacher(temp)+"");
				books.setTeacherID(form.getTeacherID()+"");
			}
			books.setTitle(form.getTitle());
			books.setAuthor(form.getAuthor());
			books.setCondition(form.getCondition()+"");
			books.setPrice(form.getPrice()+"");
			books.setSellerID(user.getID());
			if (form.getISBN()!=null){
				books.setISBN(form.getISBN());
			}
			if (form.getComment()!=null){
				books.setComment(form.getComment());
			}
			addBook(books);
		}catch(UserException e){
			throw e;
		}catch (Exception e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end postBook
	public boolean addBook(booksTable table)
		throws SQLException{
		String query = null;
		try{
			if (table==null || !table.isComplete()){
				throw new SQLException(
					"booksTable is empty or not complete");
			}
			int resultsUpdated;
			//adding required fields and values
			String[] fields = new String[]{
				booksTable.ID,booksTable.TITLE,
				booksTable.AUTHOR,
				booksTable.CONDITION,
				booksTable.PRICE,booksTable.SELLERID,
				booksTable.COLLEGE,booksTable.DATE,
				};
			String[] values = new String[]{
				table.getID(),
				table.getTitle(),
				table.getAuthor(),
				table.getCondition()+"",
				table.getPrice()+"",
				table.getSellerID(),
				table.getCollegeID()+"",
				new DateObject().getDate()
				};
			//optional parameters
			String optFields = "";
			String optValues = "";
			if (table.getISBN() != null){
				optFields += ","+booksTable.ISBN;
				optValues += ",'"+table.getISBN()+"'";
			}
			if (table.getComment() != null){
				optFields += ","+booksTable.COMMENT;
				optValues += ","+sqlUtils.toSQLString(table.getComment());
			}
			if (table.getDepartmentID() != 0){
				optFields += ","+booksTable.DEPARTMENT;
				optValues += ",'"+table.getDepartmentID()+"'";
			}
			if (table.getCourseID() != 0){
				optFields += ","+booksTable.COURSE;
				optValues += ",'"+table.getCourseID()+"'";
			}
			if (table.getTeacherID() != 0){
				optFields += ","+booksTable.TEACHER;
				optValues += ",'"+table.getTeacherID()+"'";
			}
			query = "INSERT INTO "+TABLEBOOKS
				+" ("
				+sqlUtils.sql_fields(fields)+optFields
				+") values ("
				+sqlUtils.sql_values(values)+optValues
				+")";
			resultsUpdated = statement.executeUpdate(query);
			if (resultsUpdated != 1){
				throw new SQLException(
					query+" failed to update");
			}
			return true;
		}catch(SQLException e){
			log.writeException(query+"\n"+e.getMessage());
			throw e;
		}
	} //end addBook
	public int addCollege(collegeTable table)
		throws SQLException{
		try{
			if (table == null || !table.isComplete()){
				throw new SQLException("table is not complete");
			}
			int resultsUpdated;
			String query = "INSERT INTO "+TABLECOLLEGES
				+" ("+collegeTable.SHORTNAME+","
				+collegeTable.FULLNAME+") values (\""+
				table.getShort()+"\",\""+table.getFull()+"\")";
			resultsUpdated = statement.executeUpdate(
				query,new String[]{collegeTable.ID});
			if (resultsUpdated != 1){
				throw new SQLException(query+" failed to update");
			}
			ResultSet rs = statement.getGeneratedKeys();
			if (rs.next()){
				return rs.getInt(1);
			}else{
				throw new SQLException("key wasn't returned");
			}
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end addCollege
	public int addDepartment(departmentTable table)
		throws SQLException{
		try{
			if (table == null || !table.isComplete()){
				throw new SQLException("table is not complete");
			}
			int resultsUpdated;
			String query = "INSERT INTO "+TABLEDEPARTMENTS
				+" ("+departmentTable.NAME+","
				+departmentTable.COLLEGEID+") values (\""+
				table.getName()+"\",\""+table.getCollegeID()+"\")";
			resultsUpdated = statement.executeUpdate(query,
				new String[]{departmentTable.ID});
			if (resultsUpdated != 1){
				throw new SQLException(query+" failed to update");
			}
			ResultSet rs = statement.getGeneratedKeys();
			if (rs.next()){
				return rs.getInt(1);
			}else{
				throw new SQLException("key wasn't returned");
			}
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end addDepartment
	public int addCourse(courseTable table)
		throws SQLException{
		try{
			if (table == null || !table.isComplete()){
				throw new SQLException("table is not complete");
			}
			int resultsUpdated;
			String query = "INSERT INTO "+TABLECOURSES
				+" ("+courseTable.COLLEGEID+","
				+courseTable.DEPARTMENTID+","
				+courseTable.NAME+") values (\""+
				table.getCollegeID()+"\",\""+
				table.getDepartmentID()+"\",\""+
				table.getName()+"\")";
			resultsUpdated = statement.executeUpdate(query,
				new String[]{courseTable.ID});
			if (resultsUpdated != 1){
				throw new SQLException(query+" failed to update");
			}
			ResultSet rs = statement.getGeneratedKeys();
			if (rs.next()){
				return rs.getInt(1);
			}else{
				throw new SQLException("key wasn't returned");
			}
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end addCourse
	public int addTeacher(teacherTable table)
		throws SQLException{
		try{
			if (table == null || !table.isComplete()){
				throw new SQLException("table is not complete");
			}
			int resultsUpdated;
			String query = "INSERT INTO "+TABLETEACHERS
				+" ("+teacherTable.NAME+","
				+teacherTable.COLLEGEID+") values (\""+
				table.getName()+"\",\""+table.getCollegeID()+"\")";
			resultsUpdated = statement.executeUpdate(query,
				new String[]{teacherTable.ID});
			if (resultsUpdated != 1){
				throw new SQLException(query+" failed to update");
			}
			ResultSet rs = statement.getGeneratedKeys();
			if (rs.next()){
				return rs.getInt(1);
			}else{
				throw new SQLException("key wasn't returned");
			}
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end addTeacher
	/*executeQuery
	 *simply returns Resultset object
	 */
	protected static ResultSet executeQuery(String query,
		Connection con)throws SQLException{
		try
		{
			Statement st = con.createStatement();	
			ResultSet rs = null;
			rs = st.executeQuery(query);
			return rs;
		}catch (Exception e){
			log.writeException(e.getMessage());
			throw new SQLException(e.getMessage());	
		}
	} //end executeQuery
	public static resultList getCollegeList(Connection con)
		throws SQLException{
		String query = "SELECT * FROM "+TABLECOLLEGES
			+" ORDER BY "+collegeTable.FULLNAME;
		ResultSet rs = executeQuery(query,con);
		return new resultList(rs,
			resultList.COLLEGELIST);
	} //end getCollegeList
	public static resultList getDepartmentList(String colID,
		Connection con)throws SQLException{
		String query = "SELECT * FROM "+TABLEDEPARTMENTS
			+" WHERE "+departmentTable.COLLEGEID+" = "+colID
			+" ORDER by "+departmentTable.NAME;
		ResultSet rs = executeQuery(query,con);
		return new resultList(rs,
			resultList.DEPARTMENTLIST);
	} //end getDepartmentList
	public static resultList getCourseList(String colID,
		String depID,Connection con)throws SQLException{
		String query = "SELECT * FROM "+TABLECOURSES
			+" WHERE "+courseTable.COLLEGEID+" = "+colID
			+" AND "+courseTable.DEPARTMENTID+" = "+depID
			+" ORDER by "+courseTable.NAME;
		ResultSet rs = executeQuery(query,con);
		return new resultList(rs,
			resultList.COURSELIST);
	} //end getCourseList
	public static resultList getTeacherList(String colID,
		Connection con)throws SQLException{
		String query = "SELECT * FROM "+TABLETEACHERS
			+" WHERE "+teacherTable.COLLEGEID+" = "+colID
			+" ORDER by "+teacherTable.NAME;
		ResultSet rs = executeQuery(query,con);
		return new resultList(rs,
			resultList.TEACHERLIST);
	} //end getTeacherList
}
