package server;
import java.sql.*;
import java.io.*;
public class storeISBN extends Thread
{
		private String isbn;
		private ConnectionPool conPool;
		private Connection con;
		private Statement st;
		public storeISBN(String isbn,ConnectionPool conPool){
			try{
				this.isbn = isbn;
				this.conPool = conPool;
				con = conPool.getConnection();
				st = con.createStatement();
			}catch (Exception e){
				/*
				 *don't do anything, an exception will
				 *be thrown later in run that will prevent
				 *the program from running
				 */
				log.writeException(e.getMessage());
			}
		}
		public void run(){
			try{
				//add new if none found
				if(generalUtils.getBookInfo(isbn,con) == null)
				{
					isbnTable table = new isbnTable();
					String image_m_url = null;
					String image_s_url = null;
					String response = null;
					try{
						table.setISBN(isbn);
						//creating a request
						String uri = new AmazonXML().construct_ItemLookupURL(table.getISBN(),
						AmazonXML.ALL);
						//sending a request
						response = RestRequest.issueRequest(uri,2,this.getName());
						StringReader reader = new StringReader(response);
						//interpreting xml
						XMLConverter xml = new XMLConverter(reader);
						table.setTitle(xml.getValue(AmazonXML.ITEMLOOKUP_TITLE));
						table.setAuthor(xml.getValue(AmazonXML.ITEMLOOKUP_AUTHOR));
						table.setListPrice(xml.getValue(AmazonXML.ITEMLOOKUP_LISTPRICE));
						table.setPublisher(xml.getValue(AmazonXML.ITEMLOOKUP_PUBLISHER));
						table.setBinding(xml.getValue(AmazonXML.ITEMLOOKUP_BINDING));
						table.setPages(xml.getValue(AmazonXML.ITEMLOOKUP_PAGES));
						image_m_url = xml.getValue(AmazonXML.ITEMLOOKUP_MIMAGE_URL);
						image_s_url = xml.getValue(AmazonXML.ITEMLOOKUP_SIMAGE_URL);				
					}catch (Exception e){
						log.writeException("isbn: "+isbn+"\n"+e.getMessage());
						table = null;
					}
					if (table != null){
						/*
						 *making sure that table contains
						 *something other than isbn
						 */
						table.finalize();
						int itemsfound = 0;
						while (table.hasMoreElements()){
							String[] item = (String[])table.nextElement();
							if (item[1]!=null){
								itemsfound += 1;
							}
						}
						if (!(itemsfound>1)){
							table = null;
						}
					}
					if (table != null)
					{
						//getting medium picture from url
						if (image_m_url != null){
							table.setMimage(image_m_url);
							/*
							synchronized (this)
   							{
								java.net.URL url = new java.net.URL(image_m_url);
								java.net.URLConnection connection = url.openConnection();
								java.io.InputStream input = connection.getInputStream();
								java.awt.image.BufferedImage bi = javax.imageio.ImageIO.read(input); 
   								bi.flush();
   								//output file path/bookImages/[isbn]M.jpg - where [isbn] is a 10 digit/letter word
   								if (bi!=null){
   									File f = new File(StringInterface.public_html+"/bookImages/"+table.getISBN()+"M.jpg"); 
   									javax.imageio.ImageIO.write(bi, "jpeg", f);//file saved
   									//changing url path for image to be put in database
   									table.setMimage("/bookImages/"+table.getISBN()+"M.jpg"); 
								}
								else{
									table.setMimage(null);
								}
							}*/
						}
						//getting small picture from url
						if (image_s_url != null){
							table.setSimage(image_s_url);
							/*
							synchronized (this)
   							{
								java.net.URL url = new java.net.URL(image_s_url);
								java.net.URLConnection connection = url.openConnection();
								java.io.InputStream input = connection.getInputStream();
								java.awt.image.BufferedImage bi = javax.imageio.ImageIO.read(input); 
   								bi.flush();
   								//output file path/bookImages/[isbn]M.jpg - where [isbn] is a 10 digit/letter word
   								if (bi!=null){
   									File f = new File(StringInterface.public_html+"/bookImages/"+table.getISBN()+"S.jpg"); 
   									javax.imageio.ImageIO.write(bi, "jpeg", f);//file saved
   									//changing url path for image to be put in database
   									table.setSimage("/bookImages/"+table.getISBN()+"S.jpg"); 
								}
								else{
									table.setSimage(null);
								}
							}
							*/
						}
						addISBN(table);
					}
				}
			}catch (Exception e){
				log.writeException(e.getMessage());
			}finally{
				//recycle
				conPool.free(con);
				con = null;
				conPool = null;
			}
		}
		private void addISBN(isbnTable table) throws SQLException{
			try{
				String query = "INSERT INTO "+Tables.ISBNSTABLE
				+"("+isbnTable.ISBN+","+isbnTable.TITLE+","
				+isbnTable.AUTHOR+","+isbnTable.LISTPRICE+","
				+isbnTable.PUBLISHER+","+isbnTable.BINDING+","
				+isbnTable.MIMAGE+","+isbnTable.SIMAGE+","
				+isbnTable.PAGES+") values ("
				+sqlUtils.toSQLString(table.getISBN())+","
				+sqlUtils.toSQLString(table.getTitle())+","
				+sqlUtils.toSQLString(table.getAuthor())+","
				+sqlUtils.toSQLString(table.getListPrice())+","
				+sqlUtils.toSQLString(table.getPublisher())+","
				+sqlUtils.toSQLString(table.getBinding())+","
				+sqlUtils.toSQLString(table.getMimage())+","
				+sqlUtils.toSQLString(table.getSimage())+","
				+sqlUtils.toSQLString(table.getPages())+")";
			
				if (st.executeUpdate(query)!=1){
					log.writeException(query+" - faield to update");
				}
			}catch (SQLException e){
				log.writeException(e.getMessage());
				throw e;
			}	
		} //end addISBN
}