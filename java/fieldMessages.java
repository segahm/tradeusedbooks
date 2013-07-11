package server;

public class fieldMessages{
	public static String empty(String s){
		return s+" must not be empty";
	}
	public static String badLength(String s,int length){
		return s+" must be "+length
		+" characters long";
	}
	public static String outOfBounds(String s,int min,
		int max){
		return s+" must be within "+min+" and "+max
		+" characters long";
	}
	public static String illegalChars(String s,
		String chars){
		return s+" field contains characters other than "
			+chars;
	}
	public static String notLowerCase(String s){
		return s+" field needs to be all lowercase";
	}
	public static String mustContain(String s,
		String chars){
		return s+" must contain "+chars
		+" characters/symbols";
	}
	public static String badFormat(String s){
		return "bad "+s+" format";
	}
}
