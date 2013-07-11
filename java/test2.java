package server;
import java.io.*;
import java.util.*;
import java.sql.*;
import java.net.*;
public class test2 implements StringInterface {
	public static void main(String[] args) throws Exception{
		BufferedReader reader = new BufferedReader(
				new FileReader(StringInterface.rootPath+"emails.txt"));
		test(reader);
		System.out.println("main-aftertest:"+reader.readLine());
		
	}
	private static void test(BufferedReader reader) throws IOException{
		System.out.println("test:1"+reader.readLine());
		System.out.println("test:2"+reader.readLine());
		System.out.println("test:3"+reader.readLine());
	}
}
