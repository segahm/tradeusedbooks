package server;
/*RULES:
 *if the value needs to be null, do not set null using
 *set methods
 */
public class postForm{
	private String title;
	private String isbn;
	private String author;
	private int condition = 1;
	private int price = 0;
	private String comment;
	private String collegeShort;
	private String collegeFull;
	//college ID is a preffered way to set college
	private int collegeID;
	private String department;
	//department ID is a preffered way to set department
	private int departmentID;
	private String course;
	//course ID is a preffered way to set course
	private int courseID;
	private String teacher;
	//teacher ID is a preffered way to set teacher
	private int teacherID;
	//id that will be used to identify this form
	private String id;
	//test will be used to test format of the data
	booksTable testBooks = new booksTable();
	collegeTable testCollege = new collegeTable();
	courseTable testCourse = new courseTable();
	//teacherTable testTeacher = new teacherTable();
	departmentTable testDepartment = new departmentTable();
	teacherTable testTeacher = new teacherTable();
	public postForm(){
		id = sqlUtils.generate(15);
	}
	public String getID(){
		return id;
	}
	public void setTitle(String s)
		throws FormatException{
		testBooks.setTitle(s);
		title = testBooks.getTitle();
	} //end setTitle
	public void setISBN(String s) 
		throws FormatException{
		testBooks.setISBN(s);
		isbn = testBooks.getISBN();
	} //end setISBN
	public void setAuthor(String s) 
		throws FormatException{
		testBooks.setAuthor(s);
		author = testBooks.getAuthor();
	} //end setAuthor
	public void setPrice(String s)
		throws FormatException{
		testBooks.setPrice(s);
		price = testBooks.getPrice();
	} //end setPrice
	public void setCondition(String s)
		throws FormatException{
		testBooks.setCondition(s);
		condition = testBooks.getCondition();
	} //end setCondition
	public void setComment(String s)
		throws FormatException{
		testBooks.setComment(s);
		comment = testBooks.getComment();
	} //end setComment
	public void setCollegeID(String s)
		throws FormatException{
		testCollege.setID(s);
		collegeID = testCollege.getID();
	} //end setCollegeID
	public void setCollegeNames(String shortN,
		String fullN) throws FormatException{
		testCollege.setShort(shortN);
		testCollege.setFull(fullN);
		/*
		 *throw exception if one of the fields is empty
		 *while the other is not
		 */
		if (shortN!=null && fullN!=null 
			&& (shortN.length()==0 || fullN.length()==0)
			&& (fullN.length() != shortN.length())){
			throw new FormatException(
				"one of the college fields is empty");
		}
		collegeShort = testCollege.getShort();
		collegeFull = testCollege.getFull();
	} //end setCollegeShort
	public void setDepartmentID(String s) 
		throws FormatException{
		testDepartment.setID(s);
		departmentID = testDepartment.getID();
	} //end setDepartmentID
	public void setDepartment(String s)
		throws FormatException{
		testDepartment.setName(s);
		department = testDepartment.getName();
	} //end setDepartment
	public void setCourseID(String s)
		throws FormatException{
		testCourse.setID(s);
		courseID = testCourse.getID();
	} //end setCourseID
	public void setCourse(String s)
		throws FormatException{
		testCourse.setName(s);
		course = testCourse.getName();
	} //end setCourse
	public void setTeacherID(String s)
		throws FormatException{
		testTeacher.setID(s);
		teacherID = testTeacher.getID();
	} //end setCourseID
	public void setTeacher(String s)
		throws FormatException{
		testTeacher.setName(s);
		teacher = testTeacher.getName();
	} //end setTeacher
	public void finalize(){
		//if one is empty but not the other empty both
		if (collegeShort==null 
			|| collegeFull==null
			&& (collegeShort!=null 
			|| collegeFull!=null)){
			collegeFull = null;
			collegeShort = null;
		}
		if (collegeShort!=null && collegeFull!=null){
			collegeID = 0;
		}
		if (department!=null){
			departmentID = 0;
		}
		if (course!=null){
			courseID = 0;
		}
		if (teacher!=null){
			teacherID = 0;
		}
		if (collegeID==0){
			departmentID = 0;
			courseID = 0;
			teacherID = 0;
		}
		if (departmentID==0){
			courseID = 0;
		}
		if (departmentID==0 && department==null){
			course = null;
		}
		if (collegeID==0 && collegeShort==null
			&& collegeFull==null){
			department = null;
			course = null;
			teacher = null;
		}
	}
	/*
	 *get methods:
	 */
	public String getTitle(){
		return title;
	}
	public String getAuthor(){
		return author;
	}
	public String getISBN(){
		return isbn;
	}
	public int getCondition(){
		return condition;
	}
	public int getPrice(){
		return price;
	}
	public String getComment(){
		return comment;
	}
	public int getCollegeID(){
		return collegeID;
	}
	public String getCollegeShort(){
		return collegeShort;
	}
	public String getCollegeFull(){
		return collegeFull;
	}
	public int getDepartmentID(){
		return departmentID;
	}
	public String getDepartment(){
		return department;
	}
	public String getCourse(){
		return course;
	}
	public int getCourseID(){
		return courseID;
	}
	public int getTeacherID(){
		return teacherID;
	}
	public String getTeacher(){
		return teacher;
	}
}
