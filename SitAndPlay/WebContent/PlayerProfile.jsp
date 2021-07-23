<%@page import="main.java.controller.applicationcontroller.userspage.StatisticsController"%>
<%@page import="main.java.engineering.bean.stats.StatsBean"%>
<%@page import="main.java.engineering.utils.Session"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="main.java.engineering.bean.login.BeanUser"%>
    
    
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>Player Profile</title>

<link rel="stylesheet" href="css/userpage.css">
</head>
<body>
	<div class="left_div">
		<img src="resources/user-icon.png" alt="user image">
		<h2><%= ((Session)session.getAttribute("ssn")).getUser().getUsername() %></h2>
		<br>
		<br>
		<p>Account of type:</p>
		<h3><strong>Player</strong></h3>
	
	</div>
	
	<div class= "right_div">
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
</body>
</html>