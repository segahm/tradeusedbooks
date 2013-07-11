package server;
import java.util.*;

public class isbnTable extends Table implements Enumeration{
	//static field names
	public static final String ISBN = "isbn";
	public static final String TITLE = "title";
	public static final String AUTHOR = "author";
	public static final String LISTPRICE = "listprice";
	public static final String PUBLISHER = "publisher";
	public static final String BINDING = "binding";
	public static final String MIMAGE = "mimage"; //url
	public static final String SIMAGE = "simage"; //url
	public static final String PAGES = "pages";
	public static final int ISBN_LENGTH1 = 10;
	public static final int ISBN_LENGTH2 = 13;
	public static final int LISTPRICE_MAX = 7;
	private String isbn;
	private String mimage;
	private String simage;
	private String title;
	private String author;
	private String binding;
	private String publisher;
	private String listprice;
	private String pages;
	private Object[] elements = new Object[9];
	private int index; //elements index
	public void setISBN(String s)throws FormatException{
		if (s == null || (s.length()!=ISBN_LENGTH1
			&& s.length()!=ISBN_LENGTH2) 
			|| !tools.lettersAndDigitsOnly(s,null,null)){
			throw new FormatException();
		}
		isbn = s;
	} //end setISBN
	public void setTitle(String s){
		title = s;
	} //end setTitle
	public void setAuthor(String s){
		author = s;
	} //end setAuthor
	public void setMimage(String s){
		mimage = s;
	} //end setMimage
	public void setSimage(String s){
		simage = s;
	} //end setSimage
	public void setBinding(String s){
		binding = s;
	} //end setBinding
	public void setPublisher(String s){
		publisher = s;
	} //end setPublisher
	public void setListPrice(String s){
		listprice = s;
	} //end setListPrice
	public void setPages(String s){
		pages = s;
	} //end setPages
	public String getISBN(){
		return isbn;
	} //end getISBN
	public String getTitle(){
		return title;
	} //end getTitle
	public String getAuthor(){
		return author;
	} //end getAuthor
	public String getMimage(){
		return mimage;
	} //end getMimage
	public String getSimage(){
		return simage;
	} //end getSimage
	public String getPublisher(){
		return publisher;
	} //end getPublisher
	public String getBinding(){
		return binding;
	} //end getBinding
	public String getListPrice(){
		return listprice;
	} //end getListPrice
	public String getPages(){
		return pages;
	} //end getPages
	public void finalize(){
		index = 0;
		elements[0] = new String[]{ISBN,isbn};
		elements[1] = new String[]{TITLE,title};
		elements[2] = new String[]{AUTHOR,author};
		elements[3] = new String[]{LISTPRICE,listprice};
		elements[4] = new String[]{PUBLISHER,publisher};
		elements[5] = new String[]{BINDING,binding};
		elements[6] = new String[]{MIMAGE,mimage};
		elements[7] = new String[]{SIMAGE,simage};
		elements[8] = new String[]{PAGES,pages};
	}
	public boolean hasMoreElements(){
		if (index<elements.length){
			return true;
		}else{
			return false;
		}
	}
	public Object nextElement() throws NoSuchElementException{
		if (index<elements.length){
			index++;
			return elements[index-1];
		}else{
			throw new NoSuchElementException();
		}
	}
	public String toString(){
		return "isbnTable";
	} 
}
