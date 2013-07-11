package server;
public class UserException extends Exception{
	private String field = null;
	public UserException(){
		super();
	}
	public UserException(String message){
		super(message);
	}
	public UserException(String message,String f){
		this(message);
		setField(f);
	}
	public void setField(String f){
		field = f;
	}
	public String getField(){
		return field;
	}
}
