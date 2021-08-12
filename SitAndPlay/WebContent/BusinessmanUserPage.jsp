<%@page import="main.java.engineering.bean.stats.StatsBean"%>
<%@page import="main.java.controller.applicationcontroller.userspage.StatisticsController"%>
<%@page import="main.java.engineering.utils.Session"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page errorPage="ErrorPage.jsp" %>
   
    
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
			<a href="BusinessmanUserPage.jsp"><span>User</span></a>
			<a href="CreateActivity.jsp"><span>Activities</span></a>
			<a href="SponsorTournament.jsp"><span>Tournaments</span></a>
			<a href="Notifications.jsp"><span>Notifications</span></a>
		</div>
	
		<div id="content" class="content">
			<div class="topnav">
  				<a href="BusinessmanUserPage.jsp">Profile</a>
			</div>
			
			<div id="innerPage" class="innerDiv">
			<div class="left_pane">
				<img src="resources/user-icon.png" alt="user image">
				<h2><%= ((Session)session.getAttribute("ssn")).getUser().getUsername() %></h2>
				<br>
				<br>
				<p>Account of type:</p>
				<h3><strong>Businessman</strong></h3>
			</div>
	
			<div class= "right_central">
			<% 
			StatisticsController ctrl = new StatisticsController();
			Session ssn = (Session) session.getAttribute("ssn");
			int sponsoredTournaments = ctrl.getNumberOfSponsorizedTournaments(ssn.getUser()); %>
			
				<label>Create your business activities and sponsor tournaments to promote them!</label>
				<p>You have currently sponsored <%= sponsoredTournaments %> tournaments!</p>
				
				
			</div>
		</div>
	</div>
		</div>
</body>
</html>