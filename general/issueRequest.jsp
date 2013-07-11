<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="true" %>
<%@ page import="server.*" %>
<%@ include file="/jsp/allTags.jsp" %>
<%
browserRequest httpRequest = new browserRequest();
out.print(httpRequest.issueRequest(request.getQueryString()).replaceAll("<meta[^>]*>",""));
httpRequest = null;
%>