<%@page import="main.java.model.UserType"%>
<%@page import="main.java.engineering.bean.login.BeanUser"%>
<%@page import="main.java.engineering.utils.Session"%>
<%@page import="main.java.engineering.bean.notifications.NotificationBean"%>
<%@page import="java.util.List"%>
<%@page import="main.java.controller.applicationcontroller.notifications.ViewNotificationController"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%@page errorPage="ErrorPage.jsp" %>
    
    
    <%
    ViewNotificationController ctrl = new ViewNotificationController();
    Session ssn = (Session) session.getAttribute("ssn");
    if(ssn==null){
    	throw new Exception("Session is expired! Please go back and log in again.");
    }
    BeanUser user = ssn.getUser();
    List<NotificationBean> notifs = ctrl.retrieveLastNotification(user, 10);
    %>
    
    
    <%
    if(request.getParameter("btnShow")!=null){
    	ssn = (Session) session.getAttribute("ssn");
    	if(ssn==null){
        	throw new Exception("Session is expired! Please go back and log in again.");
        }
    	
    	int numberOfNotifs = Integer.parseInt(request.getParameter("number"));
    	
    	// refresh the list of notifications
    	ctrl = new ViewNotificationController();
    	notifs = ctrl.retrieveLastNotification(ssn.getUser(), numberOfNotifs);
    }
    %>
    
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
			<%
			UserType type = ssn.getUser().getUserType();
			if(type == UserType.PLAYER){
			%>
				<a href="PlayerUserPage.jsp"><span>User</span></a>
				<a href="CreateTable.jsp"><span>Tables</span></a>
				<a href="JoinTournament.jsp"><span>Tournaments</span></a>
				<a href="Notifications.jsp"><span>Notifications</span></a>
			<%
			}else if (type == UserType.BUSINESSMAN){
				%>
				<a href="BusinessmanUserPage.jsp"><span>User</span></a>
				<a href="CreateActivity.jsp"><span>Activities</span></a>
				<a href="SponsorTournament.jsp"><span>Tournaments</span></a>
				<a href="Notifications.jsp"><span>Notifications</span></a>
				<%	
			}else if(type == UserType.ORGANIZER){
				%>
				<a href="OrganizerUserPage.jsp"><span>User</span></a>
				<a href="CreateTable.jsp"><span>Tables</span></a>
				<a href="CreateTournament.jsp"><span>Tournaments</span></a>
				<a href="Notifications.jsp"><span>Notifications</span></a>
				<%
			}
			 %>
			
		</div>
	
		<div id="content" class="content">
			
			<div id="innerPage" class="innerDiv">
				<label class="title-label">How many notifications do you want to see?</label>
				<div class="radio-group">
				<form action="Notifications.jsp" name="myform">
					<input type="radio" id ="radio10" name="number" value=10 checked>
					<label for="radio10">Last 10</label>
					<input type="radio" id ="radio20" name="number" value=20>
					<label for="radio20">Last 20</label>
					<input type="radio" id ="radio40" name="number" value=40>
					<label for="radio40">Last 40</label>
					
					<input type="submit" class="btn" name="btnShow" id="btnShow" value="Show me">
				</form>
					
				</div>
				
				<div id="scroll" class="scrollPane">
				<%
				for(NotificationBean bean : notifs){
				%>
					<div class="notif">
						<label class="msg"><%= bean.getMsg() %></label>
					</div>
				<%
				}
				%>	
				</div>
			</div>
		</div>
	</div>
</body>
</html>