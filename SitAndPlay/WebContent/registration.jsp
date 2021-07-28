<%@page import="main.java.model.UserType"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="main.java.engineering.utils.Session" %>
<%@page import="main.java.engineering.bean.login.BeanUser" %>
<%@page import="main.java.controller.applicationcontroller.RegistrationController" %>
<%@page import="main.java.engineering.exceptions.DAOException" %>
<%@page import="main.java.engineering.exceptions.EmptyDataException" %>   

<%@page errorPage="ErrorPage.jsp"%> 
    
<jsp:useBean id="userBean" scope="session" class="main.java.engineering.bean.login.BeanUser"/>
<jsp:setProperty name="userBean" property="*"/>    
    
    <%
    if(request.getParameter("back")!=null){
    	%>
    	<jsp:forward page="login.jsp"/>
    	<%
    }
    %>
    
    <%
    if(request.getParameter("signup")!=null){
    	BeanUser newUser = new BeanUser();
    	newUser.setUsername(request.getParameter("tfUsername"));
    	newUser.setPassword(request.getParameter("pfPassword"));
    	UserType type;
    	switch(request.getParameter("account")){
    	case "Player": type = UserType.PLAYER; break;
    	case "Organizer" : type = UserType.ORGANIZER; break;
    	case "Businessman" : type = UserType.BUSINESSMAN; break;
    	default: type = UserType.PLAYER; break;
    	}
    	newUser.setUserType(type);
    	
    	RegistrationController ctrl = new RegistrationController();
    	ctrl.signIn(newUser);
    }
    %>
    
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>Sit&amp;Play - Registration</title>

<link rel="stylesheet" href="css/registration.css">
<title>Insert title here</title>

<script type="text/javascript">
	function launchAlert(msg){
		alert(msg);
	}
</script>
</head>
<body>
	<div class="topBar">
		<label class="appName">Sit&amp;Play</label>
	</div>
	<div class="lowPageContainer">
		<div class="container">
			<form action="registration.jsp" name="registration" method="post">
				<input id="tfUsername" type="text" name="tfUsername" placeholder="Username"> 
				<input id="pfPassword" type="password" name="pfPassword" placeholder="Password">
				<div class="radioContainer">
					<input id="player" type="radio" name="account" value="Player" checked>
					<label for="player">Player</label>
					<input id="org" type="radio" name="account" value="Organizer">
					<label for="org">Organizer</label>
					<input id="bus" type="radio" name="account" value="Businessman">
					<label for="bus">Businessman</label> 
				</div>
				<div class = "btnContainer">
					<input id="back" type="submit" name="back" value="Back to Login" >
					<input id="signup" type="submit" name="signup" value="Sign Up">
				</div>
			</form>
		</div>
	</div>
</body>
</html>