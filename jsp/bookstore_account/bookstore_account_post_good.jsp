<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="true" %>
<%@ page import="server.*" %>
<h1>Book Posting</h1>
You have successfully posted this book<p>
<a href="<%=response.encodeURL(URLInterface.URL_BOOKSTORE_ACCOUNT_POST)%>" 
class="refLinks">click here to post another book</a>