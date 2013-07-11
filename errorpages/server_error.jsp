<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="false" %>
<%@ page isErrorPage="true" %>
<%-- in case error happens on this page --%>
<%@ page errorPage="/errorpages/server_error.html" %>
<%@ page import="server.*" %> 
<%-- some html goes here --%>
<%@ include file="server_error.html" %>
<%
if (exception == null){
	log.writeError("exception == null");
}else if(exception.getMessage()==null){
	log.writeError("exception.getMessage() == null");
}else{
	log.writeError(exception.getMessage());
}
%>