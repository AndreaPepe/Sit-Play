<%@page import="main.java.engineering.bean.stats.StatsBean"%>
<%@page import="main.java.controller.applicationcontroller.userspage.StatisticsController"%>
<%@page import="main.java.engineering.utils.Session"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page errorPage="ErrorPage.jsp" %>
    
    <%
    Session ssn = (Session) session.getAttribute("ssn");
	if(ssn==null){
		throw new Exception("Session is expired! Please go back and log in again!");
	}
    %>
    
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>Home page</title>

<link rel="stylesheet" href="css/basicStyle.css">
<link rel="stylesheet" href="css/userpage.css">

</head>
<body>
	<header>
		<div class="left_area">
			<h3>Sit<span>&amp;</span>Play</h3>
		</div>
	</header>
	
	<div class="container">
		<div class="sidebar">
			<a href="OrganizerUserPage.jsp"><span>User</span></a>
			<a href="CreateTable.jsp"><span>Tables</span></a>
			<a href="CreateTournament.jsp"><span>Tournaments</span></a>
			<a href="Notifications.jsp"><span>Notifications</span></a>
		</div>
	
		<div id="content" class="content">
			<div class="topnav">
  				<a href="OrganizerUserPage.jsp">Profile</a>
			</div>
			
			<div id="innerPage" class="innerDiv">
			<div class="left_pane">
				<img src="resources/user-icon.png" alt="user image">
				<h2><%= ((Session)session.getAttribute("ssn")).getUser().getUsername() %></h2>
				<br>
				<br>
				<p>Account of type:</p>
				<h3><strong>Organizer</strong></h3>
			</div>
	
			<div class= "right_central">
			<% 
			StatisticsController ctrl = new StatisticsController();
			StatsBean stats = ctrl.getStats(ssn.getUser()); %>
					<label>Organize tables and tournaments, with the opportunity to request a sponsor!</label>
					<p>You have currently organized <%= stats.getOrgTables() %> tables!</p>
					<p>You have currently organized <%= stats.getOrgTournaments() %> tournaments!</p>
		
			</div>
		</div>
	</div>
		</div>
</body>
</html>