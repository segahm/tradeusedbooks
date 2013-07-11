package server;
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 
//import javax.xml.parsers.FactoryConfigurationError; 
//import javax.xml.parsers.ParserConfigurationException; 
//import org.xml.sax.SAXException; 
//import org.xml.sax.SAXParseException; 
import java.io.*;
import org.xml.sax.InputSource;
import org.w3c.dom.Document;
import org.w3c.dom.DOMException; 
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import java.util.*;

public class XMLConverter{
	private Document document;
	public XMLConverter(Reader is) throws Exception{
		try{
			DocumentBuilderFactory factory 
				= DocumentBuilderFactory.newInstance();
			DocumentBuilder builder 
				= factory.newDocumentBuilder();
			document = builder.parse(new InputSource(is));
		}catch (Exception e){
			log.writeException(e.getMessage());
			throw e;
		}
	} //end constructor
	public String getValue(String[] hierarchy){
		//scanning the hierarhy that we got from linkedlist
		NodeList list = document.getElementsByTagName(
			hierarchy[0].split(":")[0]);
		Node item = null;
		//starting with the second element in the list
		for (int i=0;i<hierarchy.length;i++){
			//iterating through nodes until the right is found
			int match = 0;
			for (int j=0;j<list.getLength();j++){
				item = list.item(j);
				if (item.getNodeName().equals(
						hierarchy[i].split(":")[0])){
					match++;
					if (hierarchy[i].split(":").length==1
						|| hierarchy[i].split(":")[1].equals(match+"")){
						list = item.getChildNodes();
						break;
					}
				}else{
					//check if last element
					if ((list.getLength()-1)==j){
						return null;
					}
				}
			}
		}
		if (item!=null){
			return item.getFirstChild().getNodeValue();
		}else{
			return null;
		}
	} //end getValue
}
