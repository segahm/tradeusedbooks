package server;
import java.sql.*;
import java.util.*;
//supports the following special keywords:
/*</userid:text/>
 *</all:all/>
 */
public class SearchMarket extends Search implements Tables{
	private final String[] fields_select = new String[]{
		TABLEBOOKS+"."+booksTable.ID,
		TABLEBOOKS+"."+booksTable.ISBN,
		TABLEBOOKS+"."+booksTable.TITLE,
		TABLEBOOKS+"."+booksTable.AUTHOR,
		TABLEBOOKS+"."+booksTable.PRICE,
		ISBNSTABLE+"."+isbnTable.SIMAGE,
		ISBNSTABLE+"."+isbnTable.TITLE,
		ISBNSTABLE+"."+isbnTable.PUBLISHER,
		ISBNSTABLE+"."+isbnTable.PAGES
	};
	private final String[] fields_orderby = new String[]{
		TABLEBOOKS+"."+booksTable.TITLE,
		TABLEBOOKS+"."+booksTable.AUTHOR,
		TABLEBOOKS+"."+booksTable.PRICE
	};
	//parameters that the search is based on
	private String college;//optional
	private String department;//optional
	private String course;//optional
	private String teacher;//optional
	public SearchMarket(String q, Connection con,
		int from, int limit) throws SQLException{
		super(q,con,from,limit);
	} //end constructor
	public void execute() throws SQLException{
		try{
			query_conditions = "";
			boolean and = false;
			//adding additional paramaters to condition
			if (college != null){
				and = true;
				query_conditions += "("+booksTable.COLLEGE+" = "+college+")";
			}
			if (department != null){
				if (and){
					query_conditions += " AND ";
				}
				and = true;
				query_conditions += "("+booksTable.DEPARTMENT+" = "+department+")";	
			}
			if (course != null){
				if (and){
					query_conditions += " AND ";
				}
				and = true;
				query_conditions += "("+booksTable.COURSE+" = "+course+")";
			}
			if (teacher != null){
				if (and){
					query_conditions += " AND ";
				}
				and = true;
				query_conditions += "("+booksTable.TEACHER+" = "+teacher+")";
			}
			boolean keyword = false;
			if (query.matches(".*</userid:[^/>]+/>.*")){
				if (and){
					query_conditions += " AND ";
				}
				and = true;
				String tempQ = query.replaceAll(".*</userid:","");
				tempQ = tempQ.replaceAll("/>.*","");
				query_conditions += TABLEBOOKS+"."+booksTable.SELLERID
					+ " = '"+tempQ+"'";
				keyword = true;
				and = true;
			}
			if (query.matches("</all:all/>")){
				keyword = true;
			}
			//apply match search only if keyword is 
			//not specified or query is not empty
			if (keyword == false || searchAlgorithm(query).length()!=0){
				/*
				 *searching query in isbn,title,author
				 */
				if (and){
					query_conditions += " AND ";
				}
				and = true;
				query_conditions += "(MATCH ("
					+TABLEBOOKS+"."+booksTable.TITLE+","
					+TABLEBOOKS+"."+booksTable.AUTHOR+","
					+TABLEBOOKS+"."+booksTable.ISBN
					+") AGAINST ('"+searchAlgorithm(query)+"' IN BOOLEAN MODE))";
			}
			//finding total results
			String other_executed = "SELECT count(*) FROM "
				+TABLEBOOKS;
			//if not all apply where conditions
			if (query_conditions.length()!=0){
				other_executed += " WHERE "+query_conditions;
			}
			rs = st.executeQuery(other_executed);
			if (rs.next()){
				results_total = rs.getInt(1);
			}else{
				throw new SQLException("other wasn't executed");
			}
			//finished with other, begin query executing
			if (from>results_total){
				from = 0;
			}
			query_executed = "SELECT "
				+sql_fields(fields_select)+" FROM "
				+"("+TABLEBOOKS+" LEFT JOIN "
				+ISBNSTABLE+" ON "+TABLEBOOKS+"."+booksTable.ISBN
				+" = "+ISBNSTABLE+"."+isbnTable.ISBN+")";
			//if not all apply where conditions
			if (query_conditions.length()!=0){
				query_executed += " WHERE "+query_conditions;
			}
			query_executed += " ORDER BY "+sql_fields(fields_orderby)
				+" Limit "+from+","+limit;
			rs = st.executeQuery(query_executed);
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	}
	public void setParameters(int col,int dep,
		int cour,int teac){
		if (col != 0){
			college = col+"";
		}
		if (dep != 0){
			department = dep+"";
		}
		if (cour != 0){
			course = cour+"";
		}
		if (teac != 0){
			teacher = teac+"";
		}
	}
	public Object[] next() throws SQLException{
		try{
			if (rs.next()){
				booksTable table 
					= new booksTable(
					rs.getString(TABLEBOOKS+"."+booksTable.ID));
				table.setISBN(
					rs.getString(TABLEBOOKS+"."+booksTable.ISBN));
				table.setTitle(
					rs.getString(TABLEBOOKS+"."+booksTable.TITLE));
				table.setAuthor(
					rs.getString(TABLEBOOKS+"."+booksTable.AUTHOR));
				table.setPrice(
					rs.getString(TABLEBOOKS+"."+booksTable.PRICE));
				isbnTable table2 = new isbnTable();
				table2.setSimage(rs.getString(ISBNSTABLE+"."+isbnTable.SIMAGE));
				table2.setTitle(rs.getString(ISBNSTABLE+"."+isbnTable.TITLE));
				table2.setPublisher(rs.getString(ISBNSTABLE+"."+isbnTable.PUBLISHER));
				table2.setPages(rs.getString(ISBNSTABLE+"."+isbnTable.PAGES));
				return new Object[]{table,table2};
			}else{
				return null;
			}
		}catch(Exception e){
			log.writeException(e.getMessage());
			throw new SQLException(e.getMessage());
		}
	} //end next()
	public resultList category() throws SQLException{
		try{
			String category_executed = "";
			if (college == null){
				category_executed = "SELECT DISTINCT "
					+TABLECOLLEGES+".*"
					+" FROM "+TABLEBOOKS+","+TABLECOLLEGES
					+" WHERE ";
					if (query_conditions.length()!=0){
						category_executed += query_conditions+" AND ";
					}
					category_executed += TABLEBOOKS+"."+booksTable.COLLEGE+" = "
					+TABLECOLLEGES+"."+collegeTable.ID
					+" ORDER BY "
					+TABLECOLLEGES+"."+collegeTable.SHORTNAME;
				rsCategory = stCategory.executeQuery(category_executed);
				return new resultList(rsCategory,resultList.COLLEGELIST);
			}else if (department == null){
				category_executed = "SELECT DISTINCT "
					+TABLEDEPARTMENTS+".*"
					+" FROM "+TABLEBOOKS+","+TABLEDEPARTMENTS
					+" WHERE ";
					if (query_conditions.length()!=0){
						category_executed += query_conditions+" AND ";
					}
					category_executed += TABLEBOOKS+"."+booksTable.DEPARTMENT+" = "
					+TABLEDEPARTMENTS+"."+departmentTable.ID
					+" ORDER BY "
					+TABLEDEPARTMENTS+"."+departmentTable.NAME;
				rsCategory = stCategory.executeQuery(category_executed);
				return new resultList(rsCategory,resultList.DEPARTMENTLIST);
			}else if (course == null){
				category_executed = "SELECT DISTINCT "
					+TABLECOURSES+".*"
					+" FROM "+TABLEBOOKS+","+TABLECOURSES
					+" WHERE ";
					if (query_conditions.length()!=0){
						category_executed += query_conditions+" AND ";
					}
					category_executed += TABLEBOOKS+"."+booksTable.COURSE+" = "
					+TABLECOURSES+"."+courseTable.ID
					+" ORDER BY "
					+TABLECOURSES+"."+courseTable.NAME;
				rsCategory = stCategory.executeQuery(category_executed);
				return new resultList(rsCategory,resultList.COURSELIST);
			}else if (teacher == null){
				category_executed = "SELECT DISTINCT "
					+TABLETEACHERS+".*"
					+" FROM "+TABLEBOOKS+","+TABLETEACHERS
					+" WHERE ";
					if (query_conditions.length()!=0){
						category_executed += query_conditions+" AND ";
					}
					category_executed += TABLEBOOKS+"."+booksTable.TEACHER+" = "
					+TABLETEACHERS+"."+teacherTable.ID
					+" ORDER BY "
					+TABLETEACHERS+"."+teacherTable.NAME;
				rsCategory = stCategory.executeQuery(category_executed);
				return new resultList(rsCategory,resultList.TEACHERLIST);
			}
			return null;
		}catch(Exception e){
			log.writeException(e.getMessage());
			throw new SQLException(e.getMessage());
		}
	}
}
