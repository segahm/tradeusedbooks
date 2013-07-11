package server;
import java.io.Serializable;
/*
 *User class provides an object oriented scheme to store
 *information about users in one object and carry that
 *object in session for authentication purposes
 */
public class User implements Serializable{
	private String username;
	private String password;
	private String encrypted;
	private String id;
	usersTable test = new usersTable(); //will be used
	//for format test
	public void setID(String s) throws FormatException{
		test.setID(s); //format check
		id = s;
	} //end setUserID
	public void setUsername(String s) throws FormatException{
		test.setUsername(s); //format check
		username = s;
	} //end setUsername
	public void setPassword(String s) throws FormatException{
		if (s != null){
			test.setPassword(s); //format check
		}
		password = s;
	} //end setPassword
	public void setEncrypted(String s) throws FormatException{
		test.setEncrypted(s); //format check
		encrypted = s;
	} //end setEncrypted
	public String getID(){
		return id;
	}
	public String getUsername(){
		return username;
	}
	public String getPassword(){
		return password;
	}
	public String getEncrypted(){
		return encrypted;
	}
}