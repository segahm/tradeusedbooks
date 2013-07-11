package server;
public class AmazonXML{
	public static final String[] ITEMLOOKUP_ISBN = new String[]{
		"Items","Item","ASIN"};
	public static final String[] ITEMLOOKUP_TITLE = new String[]{
		"Items","Item","ItemAttributes","Title"};
	public static final String[] ITEMLOOKUP_AUTHOR = new String[]{
		"Items","Item","ItemAttributes","Author"};
	public static final String[] ITEMLOOKUP_PUBLISHER = new String[]{
		"Items","Item","ItemAttributes","Publisher"};
	public static final String[] ITEMLOOKUP_BINDING = new String[]{
		"Items","Item","ItemAttributes","Binding"};
	public static final String[] ITEMLOOKUP_LISTPRICE = new String[]{
		"Items","Item","ItemAttributes","ListPrice",
		"FormattedPrice"};
	public static final String[] ITEMLOOKUP_PAGES = new String[]{
		"Items","Item","ItemAttributes","NumberOfPages"};
	public static final String[] ITEMLOOKUP_MIMAGE_URL = new String[]{
		"Items","Item","MediumImage","URL"};
	public static final String[] ITEMLOOKUP_SIMAGE_URL = new String[]{
		"Items","Item","SmallImage","URL"};
	//condition parameters
	public final static String ALL = "All"; 
	public final static String NEW = "New"; 
	public final static String USED = "Used";
	//url parameters
	private String[] commonParameters = {"1NHZMSRA2TMD41DBH902", "tradeusedbooks-20"};
	private String host="webservices.amazon.com";
	private String path="/onca/xml?";
	
	public String construct_ItemLookupURL(String ASIN,
		String cond){
		String operationQuery = "ItemLookup&IdType=ASIN&ItemId="+ASIN+"&Condition="+cond;	
		return "http://"+host+path+"Service=AWSECommerceService&SubscriptionId="+commonParameters[0]+"&AssociateTag="+commonParameters[1]
		+"&Operation="+operationQuery
		+"&ResponseGroup=Images,ItemAttributes,OfferSummary";
	}
	public String construct_ItemSearchURL(String keywordString,
		int page){	
		return "http://"+host+path+"Service=AWSECommerceService"
		+"&SubscriptionId="+commonParameters[0]
		+"&AssociateTag="+commonParameters[1]
		+"&Operation=ItemSearch"
		+"&ItemPage="+page
		+"&Keywords="+keywordString
		+"&ResponseGroup=Images,ItemAttributes,OfferSummary"
		+"&SearchIndex=Books";
	}  
}
