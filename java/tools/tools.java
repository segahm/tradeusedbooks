package server;
import java.io.*;
import java.util.*;

public class tools
{	
	public static String htmlEncode(String string){
  		if (string == null){
  			return null;
  		}
  		//replacing " with &#34;
  		string = string.replaceAll("\"","&#34;");
  		string = string.replaceAll(">","&#62;");
  		string = string.replaceAll("<","&#60;");
  		return string;
	}
	public static String htmlDecode(String string){
  		if (string == null){
  			return null;
  		}
  		//replacing &#34; with "
  		string = string.replaceAll("&#34;","\"");
  		string = string.replaceAll("&#62;",">");
  		string = string.replaceAll("&#60;","<");
  		return string;
	}
	
	public static boolean allDigits(String word,
		String exceptions,String restrictions){
		if (!allASCII(word)){
			return false;
		}
		if (exceptions == null){
			exceptions = "";
		}
		if (restrictions == null){
			restrictions = "";
		}
		for (int i=0;i<word.length();i++){
			if (!Character.isDigit(word.charAt(i))
				&& !word.substring(i,i+1).matches(exceptions)
				|| word.substring(i,i+1).matches(restrictions)){
				return false;
			}
		}
		return true;
	} //end allDigits
	public static boolean allLetters(String word,
		String exceptions,String restrictions){
		if (!allASCII(word)){
			return false;
		}
		if (exceptions == null){
			exceptions = "";
		}
		if (restrictions == null){
			restrictions = "";
		}
		for (int i=0;i<word.length();i++){
			if (!Character.isLetter(word.charAt(i))
				&& !word.substring(i,i+1).matches(exceptions)
				|| word.substring(i,i+1).matches(restrictions)){
				return false;
			}
		}
		return true;
	} //end allLetters
	public static boolean lettersAndDigitsOnly(String word,
		String exceptions,String restrictions){
		if (!allASCII(word)){
			return false;
		}
		if (exceptions == null){
			exceptions = "";
		}
		if (restrictions == null){
			restrictions = "";
		}
		for (int i=0;i<word.length();i++){
			if ((!Character.isLetterOrDigit(word.charAt(i))
				&& !word.substring(i,i+1).matches(exceptions))
				|| word.substring(i,i+1).matches(restrictions)){
				return false;
			}
		}
		return true;
	} //end allDigitsAndLetters
	public static boolean allASCII(String word){
		for (int i=0;i<word.length();i++){
			if (!word.substring(i,i+1)
				.matches("[\\x00-\\x7F]")){
				return false;
			}
		}
		return true;
	} //end allowedChars
	public static boolean allLowerCase(String word){
		for (int i=0;i<word.length();i++){
			if (Character.isUpperCase(word.charAt(i))){
				return false;
			}
		}
		return true;
	} //end allLowerCase
	public static boolean allUpperCase(String word){
		for (int i=0;i<word.length();i++){
			if (Character.isLowerCase(word.charAt(i))){
				return false;
			}
		}
		return true;
	} //end allUpperCase
	public static boolean isGoodEmail(String word){
		if (word.indexOf("@") == (-1) 
			|| (word.indexOf(".") == (-1))){
			return false;
		}
		for (int i=0;i<word.length();i++){
			if (!Character.isLetterOrDigit(
				word.charAt(i)) 
				&& !word.substring(i,i+1)
				.matches("[@\\.-_]")){
				return false;
			}
		}
		return true;
	} //end goodEmailFormat
	public static boolean isGoodDate(String word){
		if (word.indexOf("/") == (-1)){
			return false;
		}
		for (int i=0;i<word.length();i++){
			if (!Character.isLetterOrDigit(word.charAt(i)) 
				&& !word.substring(i,i+1).equals("/")){
				return false;
			}
		}
		return true;
	} //end goodDateFormat
	public static String formatISBN(String word){
		if (word == null || (word.length()!=isbnTable.ISBN_LENGTH1 
			&& word.length()!=isbnTable.ISBN_LENGTH2)){
			return null;
		}else if (word.length()==isbnTable.ISBN_LENGTH1){
			return (word.substring(0,3)+"-"
				+word.substring(3,6)+"-"
				+word.substring(6,9)+"-"
				+word.substring(9,10)).toUpperCase();
		}else if (word.length()==isbnTable.ISBN_LENGTH2){
			return (word.substring(0,3)+"-"
				+word.substring(3,6)+"-"
				+word.substring(6,9)+"-"
				+word.substring(9,12)+"-"
				+word.substring(12,13)).toUpperCase();
		}
		return null;
	} //end formatISBN
	public static String lengthFormat(String line,int length){
		if (line == null){
			return null;
		}
		if (line.length()>length){
			line = line.substring(0,length-3)+"...";
		}
		return line;
	}
	public static String toString(String[] s,String between){
		String result = "";
		boolean newElement = true;
		for (int i=0;i<s.length;i++){
			if (!newElement){
				result += between;
			}else{
				newElement = false;
			}
			result += s[i];
		}
		return result;
	} //end toString
	public static String trim(String s){
		if (s == null){
			return null;
		}
		return s.trim();
	}
}