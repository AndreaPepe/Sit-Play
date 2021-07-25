<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%@page errorPage="ErrorPage.jsp" %>
    
<!DOCTYPE html>
<html lang = "en">
<head>
<meta charset="ISO-8859-1">
<title>Sit&amp;Play - Notifications</title>

<link rel="stylesheet" href="css/basicStyle.css">
<link rel="stylesheet" href="css/notifications.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>
	<header>
		<div class="left_area">
			<h3>Sit<span>&amp;</span>Play</h3>
		</div>
	</header>
	
	
	<div class="container">
		<div class="sidebar">
			<a href="PlayerUserPage.jsp"><span>User</span></a>
			<a href="CreateTable.jsp"><span>Tables</span></a>
			<a onclick="loadTournaments()"><span>Tournaments</span></a>
			<a href="Notifications.jsp"><span>Notifications</span></a>
		</div>
	
		<div id="content" class="content">
			<div class="topnav">
  				<a href="CreateTable.jsp">Create Table</a>
  				<a href="PlayerReserveTable.jsp">Reserve A Seat</a>
  				<a href="PlayerTableDeclareWinner.jsp">Declare Winner</a>
  				<a onclick="loadCreate()">Organized Tables</a>
  				
			</div>
			
			<div id="innerPage" class="innerDiv">
				<label class="title-label">How many notifications do you want to see?</label>
				<div class="radio-group">
					<input type="radio" id ="radio10" name="number" value=10 checked>
					<label for="radio10">Last 10</label>
					<input type="radio" id ="radio20" name="number" value=20>
					<label for="radio20">Last 20</label>
					<input type="radio" id ="radio40" name="number" value=40>
					<label for="radio40">Last 40</label>
				</div>
				
				<div id="scroll" class="scrollPane">
					<div class="notif">
						<label class="msg">This is the content of the notification!</label>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>