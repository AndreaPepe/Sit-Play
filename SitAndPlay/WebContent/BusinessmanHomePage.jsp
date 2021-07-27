<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    
    <%
    //TODO: remove this forward
    %>
    <jsp:forward page="CreateActivity.jsp"></jsp:forward>
    
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>Home page</title>

<link rel="stylesheet" href="css/basicStyle.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

</head>
<body>
	<header>
		<div class="left_area">
			<h3>Sit<span>&amp;</span>Play</h3>
		</div>
	</header>
	
	<div class="sidebar">
		<a onclick="loadUser()"><span>User</span></a>
		<a onclick="loadActivities()"><span>Activities</span></a>
		<a onclick="loadTournaments()"><span>Tournaments</span></a>
		<a onclick="loadNotifications()"><span>Notifications</span></a>
	</div>
	
	<div id="content" class="content">
	
	</div>
</body>
</html>