package server;
public interface SearchInterface {
	public static final String QUERYSTRING = "querystring";
	//listing parameters that will go afte ?
	public static final String QUERY = "q"; //query
	//h - page (ex: market,bookstores, etc...)
	public static final String HEADER = "h";
	//allowed pages
	public static final String[][] SITE 
		= {{"m","market place"},{"w","wanted"},
			{"b","bookstores"}};
	public static final String COLLEGE = "c";
	//department requires college
	public static final String DEPARTMENT = "d";
	//course requires college & department
	public static final String COURSE = "cr";
	//teacher requires college
	public static final String TEACHER = "t";
	//result page #
	public static final String PAGE = "p";
	//result view
	public static final String RESULTVIEW = "r";
	public static final String RESULTLIMIT = "l";
	//search limits for different sites
	public static final int MARKET_LIMIT = 10;
	public static final int WANTED_LIMIT = 40;
	public static final int BOOKSTORES_LIMIT = 10;
	//search object keyword
	public static final String SEARCHATTR = "searchObject";
	public static final String SELECTEDSITE = "selectedsite";
	public static final String PAGENUMBERATTR = "pagenumberattr";
	public static final String PAGELINEATTR = "pagelineattr";
	public static final String CATEGORYHEADERATTR = "categoryheaderattr";
	public static final String CATEGORYLISTATTR = "categorylistattr";
	//search paths
	public static final String PATHSEARCH
		= "/jsp/search/search.jsp";
}

