package server;
import java.sql.*;
import java.util.*;
public abstract class Search {
	//database variables
	protected ResultSet rs;
	protected Statement st;
	protected ResultSet rsCategory;
	protected Statement stCategory;
	//actual sql query executed
	protected String query_executed;
	//all WHERE conditions
	protected String query_conditions;
	//query string requested by user
	protected String query;
	protected int from = 1;//where to start the results
	protected int limit = 0;//limit of results
	protected int results_total = 0;
	//query that was executed
	public String getQuery(){return query;}
	//debug method
	public String toString(){return query_executed;}
	//pulic methods to be accessed from outside
	public int results_from(){return from;}
	public int results_limit(){return limit;}
	public int getTotalResults(){return results_total;}
	public Search(String q, Connection con,
		int from, int limit) throws SQLException{
		query = q;
		this.from = from;
		this.limit = limit;
		try{
			st = con.createStatement();
			stCategory = con.createStatement();
		}catch(SQLException e){
			log.writeException(e.getMessage());
			throw new SQLException(e.getMessage());
		}
	} //end constructor
	//execute needs to be overwritten
	public void execute() throws SQLException{
	} //end next
	//next needs to be overwritten
	public Object[] next() throws SQLException{
		return null;
	} //end next
	//nextCategory needs to be overwritten
	public resultList category() throws SQLException{
		return null;
	} //end nextCategory
	protected String sql_fields(String[] s){
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
	protected String sql_values(String[] s){
		String result = "";
		boolean newElement = true;
		for (int i=0;i<s.length;i++){
			if (!newElement){
				result += ",";
			}else{
				newElement = false;
			}
			if (s[i]!=null){
				result += "\""+s[i]+"\"";
			}else{
				result += s[i];
			}
		}
		return result;
	} //end getValues
	/*format:
	 *formats query string to the accepted chars
	 */
	private String format(String s){
		s = s.trim();
		//replace multiple repeatition of "
		s = s.replaceAll("[\"]+","\"");
		//replace multiple repeatition of space
		s = s.replaceAll("[ ]+"," ");
		//removing special keywords of the format </keyword:text/>
		s = s.replaceAll("</[^>/]*[:][^>/]*/>","");
		String result = "";
		for (int i=0;i<s.length();i++){
			if (Character.isLetterOrDigit(s.charAt(i))
				|| s.charAt(i) == '\"'
				|| Character.isWhitespace(s.charAt(i))){
				result += s.charAt(i);
			}
		}
		return result;
	} //end format
	public String searchAlgorithm(String q){
		q = format(q);
		StringTokenizer token 
			= new StringTokenizer(q," \"",true);
		String result = "";
		boolean quotes = false;
		/*iterate through words and add * to the end of
		 *words if not inside quotes
		 */
		while(token.hasMoreTokens()){
			String word = token.nextToken();
			if (!word.equals("\"") && !word.equals(" ")){
				if (quotes){
					result += word;
				}else{
					result += word;//+"*" to broaden results
				}
			}else{
				if(word.equals("\"")){
					quotes = !quotes;
				}
				result += word;
			}
		}	
		return result;
	} //end searchAlgorithm
	public void close(){
		try{
			st.close();
			rs = null;
			st = null;
		}
		catch (SQLException ignore){
		}
	} //end close
}
