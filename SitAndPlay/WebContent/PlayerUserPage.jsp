<%@page import="main.java.engineering.bean.stats.StatsBean"%>
<%@page import="main.java.controller.applicationcontroller.userspage.StatisticsController"%>
<%@page import="main.java.engineering.utils.Session"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang ="en">
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>

<link rel="stylesheet" href="css/basicStyle.css">
<link rel="stylesheet" href="css/userpage.css">
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
			<a onclick="loadNotifications()"><span>Notifications</span></a>
		</div>
	
		<div id="content" class="content">
			<div class="topnav">
  				<a href="PlayerUserPage.jsp">Profile</a>
  				<a href="PlayerLoadSeats.jsp">My Reserved Seats</a>
			</div>
			
			<div id="innerPage" class="innerDiv">
			<div class="left_pane">
				<img src="resources/user-icon.png" alt="user image">
				<h2><%= ((Session)session.getAttribute("ssn")).getUser().getUsername() %></h2>
				<br>
				<br>
				<p>Account of type:</p>
				<h3><strong>Player</strong></h3>
			</div>
	
			<div class= "right_pane">
			<% 
			StatisticsController ctrl = new StatisticsController();
			Session ssn = (Session) session.getAttribute("ssn");
			StatsBean stats = ctrl.getStats(ssn.getUser()); %>
				<div>
					<p>You have joined <%= stats.getJoinedTables() %> tables</p>
					<p>You have won <%= stats.getWonTables() %> tables</p>
					<p>You have won the <%= String.format("%.2f", stats.getTablesWinningPercentage()) %> % of the joined tables</p>
					<p>You have organized <%= stats.getOrgTables() %> tables </p>
				</div>
				
				<div>
					<p>You have joined <%= stats.getJoinedTournaments() %> tournaments</p>
					<p>You have joined <%= stats.getJoinedTournaments() %> tournaments</p>
					<p>You have won the <%= String.format("%.2f", stats.getTournamentWinningPercentage()) %> % of the joined tournaments</p>
					<p>With tournaments you have won <%= String.format("%.2f", stats.getTotalMoney()) %> &euro;</p>
				</div>
			</div>
		</div>
	</div>
		</div>
	
		

	
	
</body>


</html>