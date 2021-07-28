<%@page import="main.java.engineering.utils.Session"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page errorPage="ErrorPage.jsp" %>
<%@page import="main.java.engineering.bean.login.BeanUser"%>
<%@page
	import="main.java.controller.applicationcontroller.LoginController"%>
<%@page import="main.java.engineering.exceptions.DAOException"%>
<%@page
	import="main.java.engineering.exceptions.WrongCredentialsExceptions"%>
<%@page import="main.java.engineering.exceptions.EmptyDataException"%>

<jsp:useBean id="userBean" scope="session"
	class="main.java.engineering.bean.login.BeanUser" />
<jsp:setProperty name="userBean" property="*" />


<%
	if (request.getParameter("btnLogin") != null) {
	// create bean to store data
	BeanUser beanUser = new BeanUser();
	
		beanUser.setUsername(request.getParameter("tfUsername"));
		beanUser.setPassword(request.getParameter("pfPassword"));

		// new instance of application controller
		LoginController ctrl = new LoginController();
		BeanUser loggedUser = ctrl.login(beanUser, true);

		// create a new web Session -> isWeb must be true
		// Registration of the user into Session

		Session mySsn = new Session(true);

		mySsn.setUser(loggedUser);
		session.setAttribute("ssn", mySsn);

		String pg = mySsn.getHomePage();
%>
		<jsp:forward page="<%=pg%>" />
<%
	}
%>





<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>Sit&amp;Play - Login</title>

<link rel="stylesheet" href="css/login.css">
</head>
<body>
	<div class="topBar">
		<label class="appName">Sit&amp;Play</label>
	</div>
	<div class="lowPageContainer">
		<div class="container">
			<form action="login.jsp" name="loginform" method="post">
				<input id="tfUsername" type="text" name="tfUsername"
					placeholder="Username"> <input id="pfPassword"
					type="password" name="pfPassword" placeholder="Password">
				<input id="login" type="submit" name="btnLogin" value="Login"
					class="btn">
				 <label class="labelLink">You don't
					have an account? <a id="linkReg" href="registration.jsp">Register</a>
				</label>
			</form>
		</div>
	</div>

</body>
</html>