package server;
import javax.servlet.http.*;
import javax.servlet.*;
import java.sql.*;
/*
 *simply takes book id as a parameter and
 *displays a book
 */
public class bookstoreBook extends ServletParent 
	implements URLInterface,Tables{
	public static final String BOOKATTR = "bookattr";
	public static final String STOREATTR = "storeattr";
	public static final String ISBNATTR = "isbnattr";
	private final String PATH_BOOKSTORE_BOOK 
		= "/jsp/book/bookstore_book.jsp";
	public void service(HttpServletRequest request,
                       HttpServletResponse response)
                throws ServletException{
        ConnectionPool conPool = null;
		Connection connection = null;
        try{
        	//check that book was passed as query string
    		if (request.getQueryString()==null 
    			|| request.getQueryString().length() 
    				!= bookstoreBooksTable.BOOKID_LENGTH){
				response.sendRedirect("/");
			}else{
				//get connection
				conPool = getConnectionPool();
				connection = conPool.getConnection();
				String bookID = request.getQueryString();
				Object[] info = getBookandStoreInfo(bookID,connection);
				RequestDispatcher rd = null;
				if (info != null){
					bookstoreBooksTable bookInfo 
						= (bookstoreBooksTable)info[0];
					String[] storeInfo = (String[])info[1];
					isbnTable isbnInfo = (isbnTable)info[2];
					//adding book info to request
					request.setAttribute(BOOKATTR,bookInfo);
					//adding store info to request
					request.setAttribute(STOREATTR,storeInfo);
					//adding isbn info to request
					request.setAttribute(ISBNATTR,isbnInfo);
					rd = getServletContext()
						.getRequestDispatcher(PATH_BOOKSTORE_BOOK);
				}else{
					//book wasn't found--send to error page
					String errorMessage = "we were unable to find the book you specified";
					request.setAttribute(
						StringInterface.ERRORPAGEATTR,
						errorMessage);
					rd = getServletContext()
						.getRequestDispatcher(PATHUSERERROR);
				}
				rd.forward(request,response);
			}
    	}catch(Exception e){
    		throw new ServletException(e);
    	}finally{
    		if (conPool != null && connection != null){
    			conPool.free(connection);
				connection = null;
			}
    	}
    } //end service
    private Object[] getBookandStoreInfo(String bookid,
    	Connection con) throws SQLException{
    	try{
    		Statement st = con.createStatement();
    		String query = "SELECT * FROM "
    			+TABLEBOOKSTOREBOOKS+","+TABLEBOOKSTOREINFO
    			+" LEFT JOIN "+ISBNSTABLE+" ON "
    			+TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.ISBN
				+" = "
				+ISBNSTABLE+"."+isbnTable.ISBN
    			+" WHERE "
    			+TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.BOOKID
    			+" = '"
    			+bookid+"' AND "
    			+TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.STOREID
    			+" = "
    			+TABLEBOOKSTOREINFO+"."+bookstoreInfoTable.BOOKSTOREID
    			+" LIMIT 1";
    		ResultSet rs = st.executeQuery(query);
    		if (rs.next()){
    			bookstoreBooksTable book = null;
    			String[] storeInfo = null;
    			isbnTable isbnInfo = null;
    			try{
    				//setting book
    				book = new bookstoreBooksTable();
    				book.setID(rs.getString(TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.BOOKID));
					book.setStoreID(rs.getString(TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.STOREID));
					book.setTitle(rs.getString(TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.TITLE));
					book.setAuthor(rs.getString(TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.AUTHOR));
					book.setISBN(rs.getString(TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.ISBN));
					book.setNewPrice(rs.getString(TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.PRICENEW));
					book.setUsedPrice(rs.getString(TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.PRICEUSED));
					book.setCopies(rs.getString(TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.COPIES));
					book.setDate(rs.getString(TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.DATE));
					book.setComment(rs.getString(TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.COMMENT));
					book.setCondition(rs.getString(TABLEBOOKSTOREBOOKS+"."+bookstoreBooksTable.CONDITIONS).split("/"));
					//setting store info
					storeInfo = new String[
						bookstoreInfoTable.fieldNames.length];
					for (int i=0;i<storeInfo.length;i++){
						storeInfo[i] = rs.getString(TABLEBOOKSTOREINFO+"."+bookstoreInfoTable.fieldNames[i]);
					}
					//setting isbn info
					isbnInfo = new isbnTable();
					isbnInfo.setTitle(rs.getString(ISBNSTABLE+"."+isbnTable.TITLE));
					isbnInfo.setAuthor(rs.getString(ISBNSTABLE+"."+isbnTable.AUTHOR));
					isbnInfo.setPublisher(rs.getString(ISBNSTABLE+"."+isbnTable.PUBLISHER));
					isbnInfo.setSimage(rs.getString(ISBNSTABLE+"."+isbnTable.SIMAGE));
					isbnInfo.setMimage(rs.getString(ISBNSTABLE+"."+isbnTable.MIMAGE));
					isbnInfo.setBinding(rs.getString(ISBNSTABLE+"."+isbnTable.BINDING));
					isbnInfo.setListPrice(rs.getString(ISBNSTABLE+"."+isbnTable.LISTPRICE));
					isbnInfo.setPages(rs.getString(ISBNSTABLE+"."+isbnTable.PAGES));
				}catch(FormatException ignore){
					log.writeException("something is wrong-format exception: "+ignore.getMessage());
					throw new SQLException(ignore.getMessage());
				}
				return new Object[]{book,storeInfo,isbnInfo};
    		}else{
    			return null;
    		}
    	}catch(SQLException e){
    		log.writeException(e.getMessage());
    		throw e;
    	}
    } //end getBookandStoreInfo
}
