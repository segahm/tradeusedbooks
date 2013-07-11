package server;
import java.sql.*;
import java.util.*;
//supports the following special keywords:
/*</storeid:text/>
 *</all:all/>
 */
public class SearchBookstores extends Search implements Tables{
	private final String[] fields_select = new String[]{
		TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.BOOKID,
		TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.STOREID,
		TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.TITLE,
		TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.AUTHOR,
		TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.ISBN,
		TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.PRICENEW,
		TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.PRICEUSED,
		TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.COPIES,
		TABLEBOOKSTOREINFO+"."+bookstoreInfoTable.STORENAME,
		ISBNSTABLE+"."+isbnTable.SIMAGE,
		ISBNSTABLE+"."+isbnTable.TITLE,
	};
	private final String[] fields_orderby = new String[]{
		TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.TITLE,
		TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.AUTHOR,
		TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.PRICEUSED
	};
	public SearchBookstores(String q, Connection con,
		int from, int limit) throws SQLException{
		super(q,con,from,limit);
	} //end constructor
	public void execute() throws SQLException{
		try{
			query_conditions = "";
			boolean keyword = false;
			boolean and = false;
			if (query.matches(".*</storeid:[^/>]+/>.*")){
				String tempQ = query.replaceAll(".*</storeid:","");
				tempQ = tempQ.replaceAll("/>.*","");
				query_conditions += TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.STOREID
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
				query_conditions += "(MATCH ("
					+TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.TITLE+","
					+TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.AUTHOR+","
					+TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.ISBN
					+") AGAINST ('"+searchAlgorithm(query)+"' IN BOOLEAN MODE))";
			}
			//finding total results
			String other_executed = "SELECT count(*) FROM "
				+TABLEBOOKSTOREBOOKS;
			//if not all apply where conditions
			if (!query.matches("</all:all/>")){
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
				+"("+TABLEBOOKSTOREBOOKS+","+TABLEBOOKSTOREINFO+" LEFT JOIN "
				+ISBNSTABLE+" ON "+TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.ISBN
				+" = "+ISBNSTABLE+"."+isbnTable.ISBN+") WHERE ";
			//if not all apply where conditions
			if (!query.matches("</all:all/>")){
				query_executed += query_conditions
					+" AND ";
			}
			query_executed += TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.STOREID
				+" = "
				+TABLEBOOKSTOREINFO+"."+bookstoreInfoTable.BOOKSTOREID;
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
				bookstoreBooksTable table1 = new bookstoreBooksTable();
				table1.setID(rs.getString(TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.BOOKID));
				table1.setStoreID(rs.getString(TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.STOREID));
				table1.setTitle(rs.getString(TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.TITLE));
				table1.setAuthor(rs.getString(TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.AUTHOR));
				table1.setISBN(rs.getString(TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.ISBN));
				table1.setNewPrice(rs.getString(TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.PRICENEW));
				table1.setUsedPrice(rs.getString(TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.PRICEUSED));
				table1.setCopies(rs.getString(TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.COPIES));
				//getting isbn info
				isbnTable table2 = new isbnTable();
				table2.setSimage(rs.getString(ISBNSTABLE+"."+isbnTable.SIMAGE));
				table2.setTitle(rs.getString(ISBNSTABLE+"."+isbnTable.TITLE));
				return new Object[]{
					table1,
					rs.getString(
						TABLEBOOKSTOREINFO+"."
						+bookstoreInfoTable.STORENAME)
					,table2};
			}else{
				return null;
			}
		}catch(Exception e){
			log.writeException(e.getMessage());
			throw new SQLException(e.getMessage());
		}
	} //end next()
}
