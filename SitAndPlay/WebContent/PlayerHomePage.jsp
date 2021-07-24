<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>Home page</title>

<link rel="stylesheet" href="css/homepage.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<script type="text/javascript">
	function loadUser(){
		$("#content").load("PlayerUserPage.jsp");
	}

	function loadTables(){
		$("#content").load("PlayerTablesPage.jsp");
	}
	function loadTournaments(){
		$("#content").load("PlayerUserPage.jsp");
	}
	function loadNotifications(){
		$("#content").load("PlayerUserPage.jsp");
	}
</script>


</head>
<body onload="$(document).ready(loadUser)">
	<header>
		<div class="left_area">
			<h3>Sit<span>&amp;</span>Play</h3>
		</div>
	</header>
	
	<div class="container">
		<div class="sidebar">
			<a onclick="loadUser()"><span>User</span></a>
			<a onclick="loadTables()"><span>Tables</span></a>
			<a onclick="loadTournaments()"><span>Tournaments</span></a>
			<a onclick="loadNotifications()"><span>Notifications</span></a>
		</div>
	
		<div id="content" class="content">
	
		</div>
	</div>
</body>
</html>