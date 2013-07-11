package server;

public class sqlUtils{
	public static String toSQLString(String value){
		if (value == null){
			value = null;
		}else{
			//replace every \ with \\
			value = value.replaceAll("\\\\","\\\\\\\\");
			value = value.replaceAll("[']","\\\\'");
			value = value.replaceAll("[\"]","\\\\\"");
			value = "'"+value+"'";
		}
		return value;
	}
	//generates times letter String consisting of a-z/1-9
	public static String generate(int times){
  		String[] alphabet = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","p","q","r","s","t","u","v","w","x","y","z","1","2","3","4","5","6","7","8","9"};
  		//code follows the following format 10 comination of both letters and integers
  		String result = "";
  		for (int i=0;i<times;i++){
  			result += alphabet[(int)(Math.random()*34)];
  		}
  		return result;
  	} //end generate
  	public static String sql_fields(String[] s){
		String result = "";
		boolean newElement = true;
		for (int i=0;i<s.length;i++){
			if (!newElement){
				result += ",";
			}else{
				newElement = false;
			}
			result += s[i];
		}
		return result;
	} //end getFields
	public static String sql_values(String[] s){
		String result = "";
		boolean newElement = true;
		for (int i=0;i<s.length;i++){
			if (!newElement){
				result += ",";
			}else{
				newElement = false;
			}
			result += toSQLString(s[i]);
		}
		return result;
	} //end getValues
}
