package server;
import java.io.Serializable;
public class BookstoreUser implements Serializable{
	private String epass;
	private String id;
	public void setID(String s){
		id = s;
	} //end setUserID
	public void setEpass(String s){
		epass = s;
	} //end setEpass
	public String getID(){
		return id;
	} //end getID
	public String getEpass(){
		return epass;
	} //end getEpass
}