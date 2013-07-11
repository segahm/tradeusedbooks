<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="true" %>
<%@ page import="server.*,java.util.*,java.sql.*" %>
<%-- includes custom tags and stdl --%>
<%@ include file="/jsp/allTags.jsp" %>
<%@ include file="/jsp/conPool.jsp" %>
<%!
//cuts the string to the maximum size of the select options
private String cutOption(String word){
	if (word.length()>45){
		word = word.substring(0,42)+"...";
	}
	return word;
}
private String centerOption(String word){
	String result = "";
	for (int i=0;i<((65-word.length())/2);i++){
		result += "-";
	}
	result +=word;
	for (int i=0;i<((65-word.length())/2);i++){
		result += "-";
	}
	return result;
}
%>
<%
String HEADERTITLE = "Posting a Book";
String error = (String)request.getAttribute(
	postBInterface.ERRORMESSAGE);
String college_short = request.getParameter(
	postBInterface.COLLEGESHORT);
String college_full = request.getParameter(
	postBInterface.COLLEGEFULL);
String department_text = request.getParameter(
	postBInterface.DEPARTMENTCUSTOM);
String teacher_text = request.getParameter(
	postBInterface.TEACHERCUSTOM);
String course_text = request.getParameter(
	postBInterface.COURSECUSTOM);
String unqueFormID = (String)request.getAttribute(
	postBInterface.UNIQUEFORMATTR);
String college_id = request.getParameter(
    	postBInterface.COLLEGEIDFIELD);
String department_id  = request.getParameter(
	postBInterface.DEPARTMENTIDFIELD);
String course_id = request.getParameter(
	postBInterface.COURSEIDFIELD);
String teacher_id = request.getParameter(
	postBInterface.TEACHERIDFIELD);
postForm form = (postForm)session.getAttribute(unqueFormID);
if (form!=null){
	if (college_id == null && form.getCollegeID()!=0){
		college_id = form.getCollegeID()+"";
	}
	if (department_id == null && form.getDepartmentID()!=0){
		department_id = form.getDepartmentID()+"";
	}
	if (course_id == null && form.getCourseID()!=0){
		course_id = form.getCourseID()+"";
	}
	if (teacher_id == null && form.getTeacherID()!=0){
		teacher_id = form.getTeacherID()+"";
	}
	if (college_short == null && form.getCollegeShort()!=null){
		college_short = form.getCollegeShort();
	}
	if (college_full == null && form.getCollegeFull()!=null){
		college_full = form.getCollegeFull();
	}
	if (department_text == null && form.getDepartment()!=null){
		department_text = form.getDepartment();
	}
	if (course_text == null && form.getCourse()!=null){
		course_text = form.getCourse();
	}
	if (teacher_text == null && form.getTeacher()!=null){
		teacher_text = form.getTeacher();
	}
}
if (college_id == null){
	college_id = "0";
}
if (department_id == null){
	department_id = "0";
}
if (course_id == null){
	course_id = "0";
}
if (teacher_id == null){
	teacher_id = "0";
}
if (college_short == null){
	college_short = "";
}
if (college_full == null){
	college_full = "";
}
if (department_text == null){
	department_text = "";
}
if (course_text == null){
	course_text = "";
}
if (teacher_text == null){
	teacher_text = "";
}
college_short = tools.htmlEncode(college_short);
college_full = tools.htmlEncode(college_full);
department_text = tools.htmlEncode(department_text);
teacher_text = tools.htmlEncode(teacher_text);
course_text = tools.htmlEncode(course_text);
ConnectionPool conPool = getConnectionPool();
Connection connection = conPool.getConnection();
try{
%>
<html>
<head>
<meta name="description" content="posting is easy as 1,2,3!">
<meta http-equiv="Cache-Control" content="no-cache" />
<META NAME="ROBOT" CONTENT="all">
<%@ include file="/html/meta.html" %>
<link rel="stylesheet" type="text/css" href="/styles/main.css">
<title>TradeUsedBooks.com - Posting, Step 4</title>
<script language=javascript>
function proc(){
	document.mainForm.scol.disabled = false;
	document.mainForm.sdep.disabled = false;
	document.mainForm.scou.disabled = false;
	document.mainForm.stea.disabled = false;
	<%-- enabling input fields if needed --%>
	if (document.mainForm.idepc.value != ""
		|| document.mainForm.sdep.value != "0"){
		document.mainForm.icouc.disabled = false;
	}
	if ((document.mainForm.icols.value != ""
		&& document.mainForm.icolf.value != "")
		|| document.mainForm.scol.value != "0"){
		document.mainForm.idepc.disabled = false;
		document.mainForm.icouc.disabled = false;
		document.mainForm.iteac.disabled = false;
	}
	<%-- disabling select fields if needed --%>
	if (document.mainForm.icols.value != ""
		|| document.mainForm.icolf.value != ""){
		document.mainForm.scol.disabled = true;
		document.mainForm.sdep.disabled = true;
		document.mainForm.scou.disabled = true;
		document.mainForm.stea.disabled = true;
	}
	if (document.mainForm.idepc.value != ""){
		document.mainForm.sdep.disabled = true;
		document.mainForm.scou.disabled = true;
	}
	if (document.mainForm.icouc.value != ""){
		document.mainForm.scou.disabled = true;
	}
	if (document.mainForm.iteac.value != ""){
		document.mainForm.stea.disabled = true;
	}
	<%-- disabling input fields if needed--%>
	if (document.mainForm.idepc.value == ""
		&& document.mainForm.sdep.value == "0"){
		document.mainForm.icouc.disabled = true;
	}
	if ((document.mainForm.icols.value == ""
		|| document.mainForm.icolf.value == "")
		&& document.mainForm.scol.value == "0"){
		document.mainForm.idepc.disabled = true;
		document.mainForm.icouc.disabled = true;
		document.mainForm.iteac.disabled = true;
	}
}
function submitForm(where){
	var focus = 1;
	if (where == "college"){
		document.mainForm.stea.value = "0";
		document.mainForm.scou.value = "0";
		document.mainForm.sdep.value = "0";
		focus = 1;
	}
	if (where == "department"){
		document.mainForm.scou.value = "0";
		focus = 2;
	}
	if (where == "course"){
		focus = 3;
	}
	if (where == "teacher"){
		focus = 4;
	}
	document.mainForm.action = "<%=response.encodeURL(URLInterface.URLPBOOKS)%>#"+focus;
	document.mainForm.submit();
}
function back(){
	document.mainForm.action = "<%=response.encodeURL(URLInterface.URLPBOOKS)%>?back";
	document.mainForm.submit();
}
function validate(){
	//checking college list if custom college specified
	if (document.mainForm.icolf.value != "" && contains(document.mainForm.scol,document.mainForm.icolf.value)){
		alert("There is already a record of the college you specified! Please select the college name from the list available.");
		return false;
	}
	//checking department list if custom department specified
	if (document.mainForm.idepc.value != "" && contains(document.mainForm.sdep,document.mainForm.idepc.value)){
		alert("We already have the department name you've specified! Please select the department name from the list available.");
		return false;
	}
	//checking course list if custom course specified
	if (document.mainForm.icouc.value != "" && contains(document.mainForm.scou,document.mainForm.icouc.value)){
		alert("We already have the course title you've specified! Please select the course title from the list available.");
		return false;
	}
	//checking teacher list if custom teacher specified
	if (document.mainForm.iteac.value != "" && contains(document.mainForm.stea,document.mainForm.iteac.value)){
		alert("We already have the teacher name you've specified! Please select the teacher name from the list available.");
		return false;
	}
	return true;
}
function contains(list,value){
	var i=0;
	while (list.options[i] != null && list.options[i].text != ""){
		if (trim(list.options[i].text.toLowerCase()) == trim(value.toLowerCase())){
			return true;
		}
		i++;
	}
	return false;
}

function trim(inputString) {
   if (typeof inputString != "string") { return inputString; }
   var retValue = inputString;
   var ch = retValue.substring(0, 1);
   while (ch == " ") { // Check for spaces at the beginning of the string
      retValue = retValue.substring(1, retValue.length);
      ch = retValue.substring(0, 1);
   }
   ch = retValue.substring(retValue.length-1, retValue.length);
   while (ch == " ") { // Check for spaces at the end of the string
      retValue = retValue.substring(0, retValue.length-1);
      ch = retValue.substring(retValue.length-1, retValue.length);
   }
   while (retValue.indexOf("  ") != -1) { // Note that there are two spaces in the string - look for multiple spaces within the string
      retValue = retValue.substring(0, retValue.indexOf("  ")) + retValue.substring(retValue.indexOf("  ")+1, retValue.length); // Again, there are two spaces in each of the strings
   }
   return retValue; // Return the trimmed string back
}
</script>
</head>
<body bgColor=#ffffff>
<table width="100%" border="0" cellspacing="0" cellpadding="0" align=center>
<!-- header begins -->
<%@ include file="/jsp/generalHeader.jsp" %>
<!-- header ends -->
<!--main content begins -->
<tr><td>
<h1>Step IV</h1>
Please select your college and for what department, course, teacher was this book 
used for. <b>Please Note</b>, your book will only appear for the college that 
you select/specify.<br><br>
<%if (error!=null){%>
<img src="/myImages/alert.gif">
<font color='#FF0000'><b>error: </b><%=error%></font>
<%}%>
<%-- from every page form is submited to the same url --%>
<form name="mainForm" method=post 
action="<%=response.encodeURL(URLInterface.URLPBOOKS+"?review")%>" onsubmit="return validate();">
<%-- pagestep parameter let's the prog. know where 
we came from--%>
<input type=hidden 
	name="<%=postBInterface.PAGESTEP%>" value="4">
<%-- this will identify this particular form and
should be passed from page to page using request attr.--%>
<input type=hidden 
	name="<%=postBInterface.UNIQUEFORMATTR%>" 
	value="<%=unqueFormID%>">
<table width="100%">
<tr>
	<td align=left valign=top nowrap><a name="1">Select a College:</a></td>
	<td align=left valign=top>
<%-- college select --%>
<select id="scol" size="7" name="<%=postBInterface.COLLEGEIDFIELD%>" 
onchange="submitForm('college')" align=left>
<%-- college options --%>
<option
<%
if (college_id.equals("0")){
out.print(" selected ");
}
%> 
value="0"><%=centerOption("Select your college")%></option>
<%
resultList college_list = postUtils.getCollegeList(connection);
collegeTable college_table = (collegeTable)college_list.next();
while(college_table!=null){
out.print("<option ");
if (college_id.equals(college_table.getID()+"")){
out.print("selected ");
}
out.println("value=\""
	+college_table.getID()+"\">"+cutOption(college_table.getFull())
	+"</option>");
college_table = (collegeTable)college_list.next();
}
%>
</select>
	</td>
	<td align=left valign=top nowrap><font>
	<b>or</b> specify your own college:</font><br>
	Short name:&nbsp;<input id="icols" type=text 
	name="<%=postBInterface.COLLEGESHORT%>" value="<%=tools.htmlEncode(college_short)%>"
	maxlength="<%=collegeTable.SHORTNAME_MAX%>" size="20" 
	onkeyup="proc()"><br>
	<i>ex: UC Berkeley</i><br>
	Full name:&nbsp;&nbsp;&nbsp;&nbsp;<input id="icolf"
	 type=text name="<%=postBInterface.COLLEGEFULL%>" value="<%=tools.htmlEncode(college_full)%>" 
	 maxlength="<%=collegeTable.FULLNAME_MAX%>" size="20" 
	 onkeyup="proc()"><br>
	 <i>ex: University of California Berkeley </i>
	</td>
</tr>
<tr><td></td><td align=center>\/</td><td></td></tr>
<tr>
	<td align=left valign=top nowrap><a name="2">Select a Department:</a><br><i>optional</i></td>
	<td align=left valign=top>
<%-- department select --%>
<select id="sdep" size="7" name="<%=postBInterface.DEPARTMENTIDFIELD%>" 
onchange="submitForm('department')" align=left>
<option
<%
if (department_id.equals("0")){
out.print(" selected ");
}
%> 
value="0"><%=centerOption("Select the department")%></option>
<%
if (!college_id.equals("0")){
	resultList department_list = 
	postUtils.getDepartmentList(college_id,connection);
	departmentTable department_table 
		= (departmentTable)department_list.next();
	while(department_table!=null){
		out.print("<option ");
		if (department_id.equals(department_table.getID()
			+"")){
			out.print("selected ");
		}
		out.println("value=\""
			+department_table.getID()+"\">"
			+cutOption(department_table.getName())
			+"</option>");
		department_table 
			= (departmentTable)department_list.next();
	}
}
%>
</select></td>
<%-- department input --%>
	<td align=left valign=top nowrap><font>
	<b>or</b> specify your own department:</font><br>
	department name:&nbsp;<input id="idepc" 
	type=text name="<%=postBInterface.DEPARTMENTCUSTOM%>" value="<%=tools.htmlEncode(department_text)%>"
	maxlength="<%=departmentTable.NAME_MAX%>" size="20" onkeyup="proc()">
	</td>
</tr>
<tr><td></td><td align=center>\/</td><td></td></tr>
<tr><td colspan="3"></td></tr>
<tr>
	<td align=left valign=top nowrap><a name="3">Select a Course:</a><br><i>optional</i></td>
	<td align=left valign=top>
<%-- course select --%>
<select id="scou" size="7" 
name="<%=postBInterface.COURSEIDFIELD%>" 
onchange="submitForm('course')" align=left>
<option
<%
if (course_id.equals("0")){
out.print(" selected ");
}
%> 
value="0"><%=centerOption("Select the Course")%></option>
<%
if (!college_id.equals("0") && !department_id.equals("0")){
	resultList course_list = 
	postUtils.getCourseList(college_id,department_id,connection);
	courseTable course_table 
		= (courseTable)course_list.next();
	while(course_table!=null){
		out.print("<option ");
		if (course_id.equals(course_table.getID()
			+"")){
			out.print("selected ");
		}
		out.println("value=\""
			+course_table.getID()+"\">"
			+cutOption(course_table.getName())
			+"</option>");
		course_table 
			= (courseTable)course_list.next();
	}
}
%>
</select></td>
<%-- course input --%>
	<td align=left valign=top nowrap><font>
		<b>or</b> specify your own course:</font><br>
	course title:&nbsp;<input id="icouc" type=text 
	name="<%=postBInterface.COURSECUSTOM%>" value="<%=tools.htmlEncode(course_text)%>"
	maxlength="<%=courseTable.NAME_MAX%>" size="20" onkeyup="proc()">
	</td>
</tr>
<tr><td></td><td align=center>\/</td><td></td></tr>
<tr>
	<td align=left valign=top nowrap><a name="4">Select a Teacher:</a><br><i>optional</i></td>
	<td align=left valign=top>
<%-- teacher select --%>
<select id="stea" size="7" name="<%=postBInterface.TEACHERIDFIELD%>" 
		onchange="submitForm('teacher')" align=left>
<option
<%
if (teacher_id.equals("0")){
out.print(" selected ");
}
%> 
value="0"><%=centerOption("Select the teacher")%></option>
<%
if (!college_id.equals("0")){
	resultList teacher_list = 
	postUtils.getTeacherList(college_id,connection);
	teacherTable teacher_table 
		= (teacherTable)teacher_list.next();
	while(teacher_table!=null){
		out.print("<option ");
		if (teacher_id.equals(teacher_table.getID()
			+"")){
			out.print("selected ");
		}
		out.println("value=\""
			+teacher_table.getID()+"\">"
			+cutOption(teacher_table.getName())
			+"</option>");
		teacher_table 
			= (teacherTable)teacher_list.next();
	}
}
%>
</select>
	</td>
	<td align=left valign=top nowrap><font><b>or</b> specify your own teacher:</font><br>
 	teacher's name:&nbsp;<input id="iteac" 
 	type=text name="<%=postBInterface.TEACHERCUSTOM%>" value="<%=tools.htmlEncode(teacher_text)%>"
	maxlength="<%=teacherTable.NAME_MAX%>" size="20" onkeyup="proc()">
	</td>
</tr>
</table>
<p align=center>
<input type=button onclick="back()" value="<<step back" style="color: #333399;">&nbsp;&nbsp;&nbsp;<input 
type=submit value="proceed to review" style="color: #333399;"></p>
</form>
</td></tr>
<!-- main content ends -->
<!-- footer begins -->
<%@ include file="/html/footer.html" %>
<!-- footer ends -->
</table>
<script>
proc();
</script>
</body>
</html>
<%
}catch (Exception e){
    throw e;
}finally{
    //recycle connection
    conPool.free(connection);
    connection = null;
}
%>