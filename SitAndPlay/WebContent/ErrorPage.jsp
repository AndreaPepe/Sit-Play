<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%@page isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>Error</title>
</head>
<body>
	<h4>ERROR!</h4>
	<%if(exception != null){
		%><p><%= exception.getMessage() %></p>
		<%
	} %>
	
</body>
</html>