package server;
import java.sql.*;
import java.io.*;
public class everyWeekCron implements Tables{
	public static void main(String[] args) throws Exception{
		Connection con = null;
		try{
			// Load database driver if not already loaded
			Class.forName(StringInterface.DRIVER);
			// Establish network connection to database
			con = DriverManager.getConnection(StringInterface.DBURL,
			StringInterface.DBUSERNAME,"fignamoya");
			log.writeLog("beginning everyweek cron");
			String id;
			String date;
			DateObject today = new DateObject();
			//begin old book clean up
			removeISBNS(con);
			ResultSet rs = getAllOldBooks(con);
			PreparedStatement pstmt = con.prepareStatement(
				"DELETE FROM "
				+TABLEOLDBOOKS
				+" WHERE "
				+oldBooksTable.BOOKID
				+" = ? LIMIT 1");
			while(rs.next()){
				id = rs.getString(oldBooksTable.BOOKID);
				date = rs.getString(oldBooksTable.DATE);
				if (today.expired(date,oldBooksTable.EXPIRATION)){
					pstmt.setString(1,id);
					int resultsUpdated = pstmt.executeUpdate();
					if (resultsUpdated==1){
						log.writeLog(":-) old books removed id:"+id+"\ndate:"+date);
					}else{
						log.writeLog("!!!:-( old books failed to remove id:"+id+"\ndate:"+date);
					}
				}
			}
			log.writeLog("finishing everyweek cron");
		}catch(Exception e){
			log.writeException(e.getMessage());
			throw e;
		}finally{
			con.close();
			con = null;
		}
	} //end main
	private static void removeISBNS(
		Connection con)throws SQLException{
		try{
			Statement st = con.createStatement();
			/*
			 *delete lines for which isbn is not used
			 *in either books table or bookstore books table
			 */
			String query = "DELETE "+ISBNSTABLE
				+" FROM ("+ISBNSTABLE
				+" LEFT JOIN "+TABLEBOOKS
				+" ON "
				+ISBNSTABLE+"."+isbnTable.ISBN
				+" = "
				+TABLEBOOKS+"."+booksTable.ISBN
				+") LEFT JOIN "+TABLEBOOKSTOREBOOKS
				+" ON "
				+ISBNSTABLE+"."+isbnTable.ISBN
				+" = "
				+TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.ISBN 
				+" WHERE "
				+TABLEBOOKS+"."+booksTable.ISBN
				+" IS NULL AND "
				+TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.ISBN
				+" IS NULL";
			int resultsUpdated = st.executeUpdate(query);
			log.writeLog("isbns deleted:"+resultsUpdated);
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end removeISBNS
	private static ResultSet getAllOldBooks(
		Connection con)throws SQLException{
		try{
			Statement st = con.createStatement();
			String query = "SELECT "
				+oldBooksTable.BOOKID+","
				+oldBooksTable.DATE
				+" FROM "+TABLEOLDBOOKS;
			return st.executeQuery(query);
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end getAllOldBooks
}
