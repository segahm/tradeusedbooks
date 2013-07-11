package server;
import java.sql.*;
public class everydayCron implements Tables{
	public static void main(String[] args) throws Exception{
		Connection con = null;
		try{
			// Load database driver if not already loaded
			Class.forName(StringInterface.DRIVER);
			// Establish network connection to database
			con = DriverManager.getConnection(StringInterface.DBURL,
			StringInterface.DBUSERNAME,"fignamoya");
			log.writeLog("beginning everyday cron");
			
			/*clean up daily message limit by setting 
			 *messages to 0*/
			Statement st = con.createStatement();
			st.executeUpdate("UPDATE "+USERSTABLE
				+" SET "+usersTable.MESSAGES+" = 0");
			//some commom variables
			String id;
			String date;
			SendMail mail;
			DateObject today = new DateObject();
			//begin temp users clean up
			ResultSet rs = getAllTempUsers(con);
			PreparedStatement pstmt = con.prepareStatement(
				"DELETE FROM "
				+TEMPUSERSTABLE
				+" WHERE "
				+tempusersTable.USERNAME
				+" = ? LIMIT 1");
			while(rs.next()){
				id = rs.getString(tempusersTable.USERNAME);
				date = rs.getString(tempusersTable.DATE);
				if (today.expired(date,tempusersTable.EXPIRATION)){
					pstmt.setString(1,id);
					int resultsUpdated = pstmt.executeUpdate();
					if (resultsUpdated==1){
						log.writeLog(":-) tempuser removed id:"+id+"\ndate:"+date);
					}else{
						log.writeLog("!!!:-( tempuser failed to remove id:"+id+"\ndate:"+date);
					}
				}
			}
			//begin temp bookstores clean up
			rs = getAllTempBookstores(con);
			pstmt = con.prepareStatement(
				"DELETE FROM "
				+TABLEBOOKSTORETEMP
				+" WHERE "
				+bookstoreTempTable.BOOKSTOREID
				+" = ? LIMIT 1");
			while(rs.next()){
				id = rs.getString(bookstoreTempTable.BOOKSTOREID);
				date = rs.getString(bookstoreTempTable.DATE);
				if (today.expired(date,bookstoreTempTable.EXPIRATION)){
					pstmt.setString(1,id);
					int resultsUpdated = pstmt.executeUpdate();
					if (resultsUpdated==1){
						log.writeLog(":-) temp bookstore removed id:"+id+"\ndate:"+date);
					}else{
						log.writeLog("!!!:-( temp bookstore failed to remove id:"+id+"\ndate:"+date);
					}
				}
			}
			//begin book clean up
			rs = getAllBooks(con);
			pstmt = con.prepareStatement(
				"DELETE FROM "
				+TABLEBOOKS
				+" WHERE "
				+booksTable.ID
				+" = ? LIMIT 1");
			while(rs.next()){
				id = rs.getString(TABLEBOOKS+"."+booksTable.ID);
				String title = rs.getString(TABLEBOOKS+"."+booksTable.TITLE);
				date = rs.getString(TABLEBOOKS+"."+booksTable.DATE);
				String email = rs.getString(USERSTABLE+"."+usersTable.EMAIL);
				String username = rs.getString(USERSTABLE+"."+usersTable.USERNAME);
				if (today.expired(date,booksTable.EXPIRATION)){
					pstmt.setString(1,id);
					int resultsUpdated = pstmt.executeUpdate();
					if (resultsUpdated==1){
						log.writeLog(":-) books removed id:"+id+"\ndate:"+date);
						//sending mail
						mail = new SendMail(email,false);
						String message = "Dear "+username+",\n"
							+"This message was sent to you because your book posting (\""+title+"\") has expired. "
							+"Please note that you can repost this exact title by going to your account history page and clicking on repost.\n"
							+"good luck next time,\n"
							+"tradeusedbooks.com team\n\n"
							+"If you think you received this email by mistake (meaning you never visited http://www.tradeusedbooks.com "
							+"and never signed up for any of our services) please email us at "
							+"support@tradeusedbooks.com.";
						mail.send(message,"your book posting has been removed");
					}else{
						log.writeLog("!!!:-( books failed to remove id:"+id+"\ndate:"+date);
					}
				}
			}
			//begin bookstore book clean up
			rs = getAllBookstoreBooks(con);
			pstmt = con.prepareStatement(
				"DELETE FROM "
				+TABLEBOOKSTOREBOOKS
				+" WHERE "
				+bookstoreBooksTable.BOOKID
				+" = ? LIMIT 1");
			while(rs.next()){
				id = rs.getString(bookstoreBooksTable.BOOKID);
				date = rs.getString(bookstoreBooksTable.DATE);
				if (today.expired(date,bookstoreBooksTable.EXPIRATION)){
					pstmt.setString(1,id);
					int resultsUpdated = pstmt.executeUpdate();
					if (resultsUpdated==1){
						log.writeLog(":-) bookstore books removed id:"+id+"\ndate:"+date);
					}else{
						log.writeLog("!!!:-( bookstore books failed to remove id:"+id+"\ndate:"+date);
					}
				}
			}
			/*****begin buyers clean up****/
			rs = getAllBuyers(con);
			pstmt = con.prepareStatement(
				"DELETE FROM "
				+TABLEBUYERS
				+" WHERE "
				+buyersTable.BOOKID
				+" = ? LIMIT 1");
			while(rs.next()){
				id = rs.getString(buyersTable.BOOKID);
				date = rs.getString(buyersTable.DATE);
				if (today.expired(date,buyersTable.EXPIRATION)){
					pstmt.setString(1,id);
					int resultsUpdated = pstmt.executeUpdate();
					if (resultsUpdated==1){
						log.writeLog(":-) buyers removed id:"+id+"\ndate:"+date);
					}else{
						log.writeLog("!!!:-( buyers failed to remove id:"+id+"\ndate:"+date);
					}
				}
			}
			/*****begin working with wanted*****/
			//get all wanted lines that have found a match
			rs = getFoundWanted(con);
			pstmt = con.prepareStatement(
				"DELETE FROM "
				+TABLEWANTED
				+" WHERE "
				+wantedTable.ID
				+" = ? LIMIT 1");
			while(rs.next()){
				String username = rs.getString(USERSTABLE+"."+usersTable.USERNAME);
				String email = rs.getString(USERSTABLE+"."+usersTable.EMAIL);
				id = rs.getString(TABLEWANTED+"."+wantedTable.ID);
				String bookID = rs.getString(TABLEBOOKS+"."+booksTable.ID);
				String wantedTitle = rs.getString(TABLEWANTED+"."+wantedTable.TITLE);
				//removing wanted request
				pstmt.setString(1,id);
				int resultsUpdated = pstmt.executeUpdate();
				if (resultsUpdated==1){
					log.writeLog(":-) wanted found match removed id:"+id+"\nsending mail to "+email);
					//sending mail
					mail = new SendMail(email,false);
					String message = ""
						+"Dear "+username+",\n"
						+"This message was sent to you because you requested us to notify "
						+"you when the title matching "+wantedTitle+" becomes available. "
						+"Good news, the title is available and can be found at "
						+"http://www.tradeusedbooks.com"+URLInterface.URLBOOK+bookID+" (copy this url to your browser).\n"
						+"Again, thank you for using tradeusedbooks.com\n"
						+"tradeusedbooks.com team\n\n"
						+"If you think you received this email by mistake (meaning you never visited http://www.tradeusedbooks.com "
						+"and never signed up for any of our services) please email us at: "
						+"support@tradeusedbooks.com.";
					mail.send(message,"we have found a match for your request");
				}else{
					log.writeLog("!!!:-( wanted found match failed to remove id:"+id);
				}
			}
			//begin all wanted clean up
			rs = getAllWanted(con);
			pstmt = con.prepareStatement(
				"DELETE FROM "
				+TABLEWANTED
				+" WHERE "
				+wantedTable.ID
				+" = ? LIMIT 1");
			while(rs.next()){
				id = rs.getString(wantedTable.ID);
				//check if todays d
				date = rs.getString(wantedTable.EXPIRATION);
				if (today.expired(date,0)){
					pstmt.setString(1,id);
					int resultsUpdated = pstmt.executeUpdate();
					if (resultsUpdated==1){
						log.writeLog(":-) wanted removed id:"+id+"\nexp. date:"+date);
					}else{
						log.writeLog("!!!:-( wanted failed to remove id:"+id+"\nexp. date:"+date);
					}
				}
			}
			log.writeLog("finishing everyday cron");
		}catch(Exception e){
			log.writeException(e.getMessage());
			throw e;
		}finally{
			con.close();
			con = null;
		}
	} //end main
	private static ResultSet getAllWanted(
		Connection con) throws SQLException{
		try{
			Statement st = con.createStatement();
			String query = "SELECT "
				+wantedTable.ID+","
				+wantedTable.EXPIRATION
				+" FROM "+TABLEWANTED;
			return st.executeQuery(query);
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end removeExpiredWanted
	private static ResultSet getFoundWanted(
		Connection con) throws SQLException{
		try{
			Statement st = con.createStatement();
			String[] fields = new String[]{
				USERSTABLE+"."+usersTable.USERNAME,
				USERSTABLE+"."+usersTable.EMAIL,
				TABLEWANTED+"."+wantedTable.ID,
				TABLEBOOKS+"."+booksTable.ID,
				TABLEWANTED+"."+wantedTable.TITLE
			};
			String query = "SELECT "
				+sqlUtils.sql_fields(fields)
				+" FROM "+TABLEWANTED+","+TABLEBOOKS+","
				+USERSTABLE
				+" WHERE "
				+TABLEWANTED+"."+wantedTable.USERID
				+" = "
				+USERSTABLE+"."+usersTable.ID
				+" AND (("
				+TABLEWANTED+"."+wantedTable.ISBN
				+" = "
				+TABLEBOOKS+"."+booksTable.ISBN
				+") OR ("
				+TABLEWANTED+"."+wantedTable.TITLE
				+" = "
				+TABLEBOOKS+"."+booksTable.TITLE
				+" AND "
				+TABLEWANTED+"."+wantedTable.AUTHOR
				+" = "
				+TABLEBOOKS+"."+booksTable.AUTHOR
				+"))";
			return st.executeQuery(query);
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end getAllWanted
	private static ResultSet getAllTempUsers(
		Connection con)throws SQLException{
		try{
			Statement st = con.createStatement();
			String query = "SELECT "
				+tempusersTable.USERNAME+","
				+tempusersTable.DATE
				+" FROM "+TEMPUSERSTABLE;
			return st.executeQuery(query);
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end getAllTempUsers
	private static ResultSet getAllTempBookstores(
		Connection con)throws SQLException{
		try{
			Statement st = con.createStatement();
			String query = "SELECT "
				+bookstoreTempTable.BOOKSTOREID+","
				+bookstoreTempTable.DATE
				+" FROM "+TABLEBOOKSTORETEMP;
			return st.executeQuery(query);
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end getAllTempBookstores
	private static ResultSet getAllBooks(
		Connection con)throws SQLException{
		try{
			Statement st = con.createStatement();
			String query = "SELECT "
				+TABLEBOOKS+"."+booksTable.ID+","
				+TABLEBOOKS+"."+booksTable.DATE+","
				+USERSTABLE+"."+usersTable.EMAIL+","
				+USERSTABLE+"."+usersTable.USERNAME+","
				+TABLEBOOKS+"."+booksTable.TITLE
				+" FROM "+TABLEBOOKS+","+USERSTABLE
				+" WHERE "
				+TABLEBOOKS+"."+booksTable.SELLERID
				+" = "
				+USERSTABLE+"."+usersTable.ID;
			return st.executeQuery(query);
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end getAllBooks
	private static ResultSet getAllBookstoreBooks(
		Connection con)throws SQLException{
		try{
			Statement st = con.createStatement();
			String query = "SELECT "
				+bookstoreBooksTable.BOOKID+","
				+bookstoreBooksTable.DATE
				+" FROM "+TABLEBOOKSTOREBOOKS;
			return st.executeQuery(query);
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end getAllBookstoreBooks
	private static ResultSet getAllBuyers(
		Connection con) throws SQLException{
		try{
			Statement st = con.createStatement();
			String query = "SELECT "
				+buyersTable.BOOKID+","
				+buyersTable.DATE
				+" FROM "+TABLEBUYERS;
			return st.executeQuery(query);
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end getAllBuyers
	/*
	private static ResultSet getAllEmptyFeedback(
		Connection con) throws Exception{
		try{
			Statement st = con.createStatement();
			String query = "SELECT "
				+USERSTABLE+"."+usersTable.USERNAME+","
				+USERSTABLE+"."+usersTable.EMAIL+","
				+"userfor."+usersTable.USERNAME+","
				+TABLEOLDBOOKS+"."+oldBooksTable.TITLE
				+" FROM "
				+USERSTABLE+","+TABLEOLDBOOKS+","
				+USERSTABLE+" AS userfor,"
				+" ("+TABLEBUYERS
				+" LEFT JOIN "+TABLEFEEDBACK
				+" ON "
				+TABLEBUYERS+"."+buyersTable.BOOKID
				+" = "
				+TABLEFEEDBACK+"."+feedbackTable.BOOKID
				+" AND "
				+TABLEBUYERS+"."+buyersTable.BUYERID
				+" = "
				+TABLEFEEDBACK+"."+feedbackTable.TRADERID
				+")"
				+" HAVING "
					+TABLEFEEDBACK+"."+feedbackTable.BOOKID
					+" IS NULL AND "
					+USERSTABLE+"."+usersTable.ID
				+" = "
					+TABLEBUYERS+"."+buyersTable.BUYERID
				+" AND "
					+TABLEBUYERS+"."+buyersTable.BOOKID
				+" = "
					+TABLEOLDBOOKS+"."+oldBooksTable.BOOKID
				+" AND "
					+TABLEOLDBOOKS+"."+oldBooksTable.SELLERID
				+" = "
					+"userfor."+usersTable.ID;
			return st.executeQuery(query);
		}catch (Exception e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end getAllEmptyFeedback
	*/
}
