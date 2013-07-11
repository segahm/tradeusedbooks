package server;
import java.sql.*;
import java.util.*;
public class SearchWanted extends Search implements Tables{
	private final String[] fields_select = new String[]{
		TABLEWANTED+"."+wantedTable.AUTHOR,
		TABLEWANTED+"."+wantedTable.ISBN,
		TABLEWANTED+"."+wantedTable.TITLE,
		TABLECOLLEGES+"."+collegeTable.SHORTNAME
	};
	private final String[] fields_orderby = new String[]{
		TABLECOLLEGES+"."+collegeTable.SHORTNAME
	};
	
	public SearchWanted(String q, Connection con,
		int from, int limit) throws SQLException{
		super(q,con,from,limit);
	} //end constructor
	public void execute() throws SQLException{
		try{
			query_conditions = "";
			/*
			 *searching query in isbn,title,author
			 */
			query_conditions += " AND (MATCH ("
				+TABLEWANTED+"."+wantedTable.TITLE+","
				+TABLEWANTED+"."+wantedTable.AUTHOR+","
				+TABLEWANTED+"."+wantedTable.ISBN
				+") AGAINST ('"+searchAlgorithm(query)
				+"' IN BOOLEAN MODE))";
			//finding total results
			String other_executed = "SELECT count(*) FROM "
				+TABLEWANTED+","+TABLECOLLEGES;
			other_executed += " WHERE "
				+TABLECOLLEGES+"."+collegeTable.ID
				+" = "
				+TABLEWANTED+"."+wantedTable.COLLEGE;
			//if not all apply where conditions
			if (!query.matches("</all:all/>")){
				other_executed += query_conditions;
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
			query_executed = "SELECT "+sql_fields(fields_select)
				+" FROM "+TABLEWANTED+","+TABLECOLLEGES;
			query_executed += " WHERE "
				+TABLECOLLEGES+"."+collegeTable.ID
				+" = "
				+TABLEWANTED+"."+wantedTable.COLLEGE;
			//if not all apply where conditions
			if (!query.matches("</all:all/>")){
				query_executed += query_conditions;
			}
			query_executed += " ORDER BY "+sql_fields(fields_orderby)
				+" Limit "+from+","+limit;
			rs = st.executeQuery(query_executed);
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	}
	public Object[] next() throws SQLException{
		try{
			if (rs.next()){
				wantedTable table = new wantedTable();
				table.setAuthor(rs.getString(TABLEWANTED+"."+wantedTable.AUTHOR));
				table.setISBN(rs.getString(TABLEWANTED+"."+wantedTable.ISBN));
				table.setTitle(rs.getString(TABLEWANTED+"."+wantedTable.TITLE));
				return new Object[]{
					table,
					rs.getString(TABLECOLLEGES+"."+collegeTable.SHORTNAME)
				};
			}else{
				return null;
			}
		}catch(Exception e){
			log.writeException(e.getMessage());
			throw new SQLException(e.getMessage());
		}
	} //end next()
}
