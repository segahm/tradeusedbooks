package server;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import java.io.*;
import java.sql.*;

public class CategoryListTag extends BodyTagSupport{
	private String attributeName;
	private String insertID;
	private String insertNAME;
	public void setInsertID(String s){
		insertID = s;
	}
	public void setInsertNAME(String s){
		insertNAME = s;
	}
	public void setAttributeName(String s){
		attributeName = s;
	}
	public int doAfterBody() throws JspException{
		BodyContent body = getBodyContent();
		JspWriter out = body.getEnclosingWriter();
		try{
			/*
			 *getting list from the pageContext using
			 *the given attribute key
			 */
			resultList list = (resultList)
				pageContext.findAttribute(attributeName);
			if (list!=null){
				//finding out which table is stored in the list
				if (list.getOption() 
					== list.COLLEGELIST){
					collegeTable table = (collegeTable)
						list.next();
					String line;
					while (table!=null){
						line = body.getString().replaceAll(insertID,table.getID()+"");
						line = line.replaceAll(insertNAME,tools.lengthFormat(table.getShort(),20));
						out.println(line);
						table = (collegeTable)list.next();
					}
				}else if (list.getOption() 
					== list.DEPARTMENTLIST){
					departmentTable table = (departmentTable)
						list.next();
					String line;
					while (table!=null){
						line = body.getString().replaceAll(insertID,table.getID()+"");
						line = line.replaceAll(insertNAME,tools.lengthFormat(table.getName(),20));
						out.println(line);
						table = (departmentTable)list.next();
					}
				}else if (list.getOption() 
					== list.COURSELIST){
					courseTable table = (courseTable)
						list.next();
					String line;
					while (table!=null){
						line = body.getString().replaceAll(insertID,table.getID()+"");
						line = line.replaceAll(insertNAME,tools.lengthFormat(table.getName(),20));
						out.println(line);
						table = (courseTable)list.next();
					}
				}else if (list.getOption() 
					== list.TEACHERLIST){
					teacherTable table = (teacherTable)
						list.next();
					String line;
					while (table!=null){
						line = body.getString().replaceAll(insertID,table.getID()+"");
						line = line.replaceAll(insertNAME,tools.lengthFormat(table.getName(),20));
						out.println(line);
						table = (teacherTable)list.next();
					}
				}
			}
		}catch (Exception e){
			log.writeException(e.getMessage());
		}
		return(SKIP_BODY);
	}
}