package server;
import java.sql.*;
import java.security.MessageDigest;
import sun.misc.BASE64Encoder;
/*
 *userUtils represents a set of tools for user
 */
public class userUtils implements StringInterface,Tables{
  	/*
  	 *encrypts password into 28chars
  	 *returns: String/null (null can be ignored)
  	 */
	protected static String getEncrypted(String password){
  		try{
  			//begin encryption,result: 28 string
			MessageDigest md = null;
        	md = MessageDigest.getInstance("SHA");
        	md.update(password.getBytes("UTF-8"));
        	byte raw[] = md.digest();
    		return (new BASE64Encoder()).encode(raw);
    	}catch (Exception ignore){
    		log.writeException(ignore.getMessage());
    		return null;
    	}
  	} //end getEncrypted
  	
	/*
	 *requirements: usersTable.getID(),usersTable.getFeedback()
	 *usersTable.getFeedback() - can be null
	 *returns: void
	 */
	protected static void setFeedback(usersTable user,Connection con)throws Exception{
		try{
			int[] temp = user.getFeedback();
			String feedback;
			//convert to string
			try{
				feedback = temp[0]+"/"+temp[1]
				+"/"+temp[2];
			}catch (Exception i){
				feedback = null;
			}
			Statement st = con.createStatement();
			String field;
			if (feedback == null){
				field = null;
			}else{
				field = "'"+feedback+"'";
			}
			int recordsUpdate = 
				st.executeUpdate("UPDATE "
				+USERSTABLE+" SET "+usersTable.FEEDBACK
				+" = "+field+" WHERE "+usersTable.ID+" = '"
				+user.getID()+"' LIMIT 1");
		}catch (Exception e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end setFeedback
	/*
	 *requirements: userID
	 *returns: usersTable.[username,email,feedback,date]
	 */
	protected static usersTable getUserInfo(String id,
		Connection con) throws Exception{
		try{
			Statement st = con.createStatement();
			ResultSet rs = null;
			rs = st.executeQuery("SELECT " 
			+ usersTable.USERNAME+","+usersTable.FEEDBACK+","
			+usersTable.EMAIL+","+usersTable.DATE+","
			+usersTable.MESSAGES+" FROM "
			+USERSTABLE+" WHERE "+usersTable.ID+" = '"+id+"' LIMIT 1");
			if (rs.next()){
				usersTable u = new usersTable();
				u.setUsername(
					rs.getString(usersTable.USERNAME));
				String temp = rs.getString(
					usersTable.FEEDBACK);
				if (temp == null){
					u.setFeedback(new int[]{0,0,0});
				}else{
					u.setFeedback(
						rs.getString(usersTable.FEEDBACK));
				}
				u.setEmail(
					rs.getString(usersTable.EMAIL));
				u.setDate(
					rs.getString(usersTable.DATE));
				u.setID(id);
				u.setNumberMessages(rs.getString(usersTable.MESSAGES));
				return u;
			}else{
				return null;
			}
		}catch (Exception e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end getUserInfo
	/*
	 *confirms user using id and encrypted pass
	 *relative to registered users table
	 *return true/false
	 *requires:username,encryptedPass,connection
	 */
	protected static boolean confirmUserWithEncrypted(
		String id,String eP,Connection con) throws SQLException
	{
		try
		{
			//do not query empy values
			if (id == null || eP == null 
			|| id.equals("") || eP.equals("")){
				return false;
			}
			Statement st = con.createStatement();
			ResultSet rsTemp = null;
			//where username = u and encryptedpass = eP
			rsTemp = st.executeQuery("SELECT "
				+usersTable.EPASS+" FROM "+USERSTABLE
				+" WHERE "+usersTable.ID
				+" = '"+id+"' AND "+usersTable.EPASS
				+" = '"+eP+"' LIMIT 1");
			return rsTemp.next();
		}
		catch (Exception e)
		{
			log.writeException(e.getMessage());
			throw new SQLException(e.getMessage());
		}
	} //end confirmUserWithEncrypted
	//updateDate is executed dring log in
	protected static boolean updateDate(String id,
		Connection con) throws SQLException
	{
		try
		{
			Statement st = con.createStatement();
			//date is of the folloing format: int(1-12)/int(1-31)/int(year)
			DateObject date = new DateObject();
			//update date setting current date time
			int recordsUpdated = st.executeUpdate("UPDATE "
			+USERSTABLE+" SET "+usersTable.DATE+"='"+date.getDate()
			+"' WHERE "+usersTable.ID+"='"+id+"' LIMIT 1");
			return (recordsUpdated == 1);
		}catch (Exception e){
			log.writeException(e.getMessage());
			throw new SQLException(e.getMessage());
		}
	}
	
}