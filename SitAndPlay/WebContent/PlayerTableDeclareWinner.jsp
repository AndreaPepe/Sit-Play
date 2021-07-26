<%@page import="main.java.controller.applicationcontroller.reserveaseat.table.ReserveTableSeatController"%>
<%@page import="main.java.engineering.utils.Session"%>
<%@page import="main.java.engineering.bean.login.BeanUser"%>
<%@page import="main.java.engineering.bean.createtable.TableBean"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page errorPage="ErrorPage.jsp" %>
    
<%
ReserveTableSeatController c = new ReserveTableSeatController();
BeanUser user = ((Session) session.getAttribute("ssn")).getUser();
List<TableBean> tables = c.retrieveTablesToDeclareWinnerTo(user);
%>
    
    <%
    if(request.getParameter("btnDeclare")!=null){
    	Session ssn = (Session) session.getAttribute("ssn");
    	if(ssn == null){
    		throw new Exception("Session is expired; please go back and log in again");
    	}
    	
    	String table = request.getParameter("table");
    	String winner = request.getParameter("winner");
    	if(table == null || winner == null){
    		throw new Exception("You did not select table or participant; table: " + table + "  participant: " + winner);
    	}
    	ReserveTableSeatController ctrl = new ReserveTableSeatController();
    	List<TableBean> newList = ctrl.retrieveTablesToDeclareWinnerTo(ssn.getUser());
    	
    	TableBean selectedTable = null;
    	for(TableBean tab : newList){
    		if(table.equals(tab.getName())){
    			selectedTable = tab;
    		}
    	}
    	if(selectedTable != null){
    		selectedTable.setWinner(winner);
        	ctrl.declareWinner(selectedTable, ssn.getUser());
        	%>
        	<jsp:forward page="PlayerTableDeclareWinner.jsp"></jsp:forward>
        	<%
    	}else{
    		throw new Exception("Something went wrong! Please reload the page and retry");
    	}
    }
    %>
<!DOCTYPE html>
<html lang= "en">
<head>
<meta charset="ISO-8859-1">
<title>Declare Winner of a Table</title>

<link rel="stylesheet" href="css/basicStyle.css">
<link rel="stylesheet" href="css/declareWinner.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<script>
function changeParticipants() {
	var selTable = document.getElementById("table").value;
	var options = "<option disabled selected>-- Select participant --</option>";
	<%
	for(TableBean bean : tables){
		%>
		if(<%= "\"" + bean.getName() + "\""%> == selTable){
			<% 
			List<String> participants = bean.getParticipants();
			for(String p : participants){
				%>
				options += ("<option>" + <%= "\"" + p + "\""%> + "</option>");
				<%
			}
			%>
		}
		<%
	}
	%>
	
	document.getElementById("winner").innerHTML = options;
}

</script>
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
  				<a href="OrganizedTables.jsp">Organized Tables</a>
  				
			</div>
			
			<div id="innerPage" class="innerDiv">
				<div class="left">
					<label>Here you can declare the winner of a table you have organized</label>
					<label>First select a table and then the participant that will be the winner!</label>
				</div>
				
				<div class="right">
					<form action="PlayerTableDeclareWinner.jsp" name="formWinner">
						<select id="table" name="table" onchange="changeParticipants()">
							<option disabled selected>-- Select Table --</option>
							<%
							for(TableBean table: tables){
								String name = table.getName();
								%>
								<option value=<%= name %>><%= name %></option>
								<%
							}
							%>
						</select>
					
						<select id="winner" name="winner">
							<option disabled selected>-- Select Participant --</option>
						</select>
					
					
						<input type="submit" id="btnDeclare" name="btnDeclare" value="Declare Winner">
					</form>
					
				</div>
			</div>
		</div>
	</div>
	
</body>
</html>