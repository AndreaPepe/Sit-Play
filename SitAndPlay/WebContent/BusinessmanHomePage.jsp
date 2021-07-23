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

	function loadActivities(){
		$("#content").load("PlayerUserPage.jsp");
	}
	function loadTournaments(){
		$("#content").load("PlayerUserPage.jsp");
	}
	function loadNotifications(){
		$("#content").load("PlayerUserPage.jsp");
	}
</script>


</head>
<body onload="loadUser()">
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