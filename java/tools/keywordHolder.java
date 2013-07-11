package server;
import java.io.*;
import java.util.*;
import java.net.*;
public class keywordHolder{
	private LinkedList list;
	public keywordHolder(String file) throws IOException{
		BufferedReader reader = new BufferedReader(
			new FileReader(file));
		list = new LinkedList();
		String line = reader.readLine();
		while (line!=null){
			String[] array = line.split(",");
			for (int i=0;i<array.length;i++){
				list.add(array[i].trim());
			}
			line = reader.readLine();
		}
		
	} //end keywordHolder
	public String getRandomKeyword(){
		try{
			return (String)list.remove((int)(Math.random()*list.size()));
		}catch(IndexOutOfBoundsException e){
			return null;
		}
	} //end getRandom
	public String constructKeywords(int limit){
		String result = getRandomKeyword();
		int i=1;
		String keyword = getRandomKeyword();
		while (i<limit && keyword!=null){
			result += ","+keyword;
			i++;
			keyword = getRandomKeyword();
		}
		return result;
	} //end constructKeywords
	public void printToFile(String file) throws IOException{
		BufferedWriter writer = new BufferedWriter(
			new FileWriter(file));
		writer.write(list.get(0)+"");
		for (int i=1;i<list.size();i++){
			writer.write(","+list.get(i));
		}
		writer.flush();
		writer.close();
	} //end printToFile
}
