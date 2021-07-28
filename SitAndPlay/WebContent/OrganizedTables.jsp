<%@page import="main.java.model.UserType"%>
<%@page import="main.java.engineering.bean.createtable.TableBean"%>
<%@page import="java.util.List"%>
<%@page import="main.java.engineering.utils.Session"%>
<%@page import="main.java.controller.applicationcontroller.reserveaseat.table.ReserveTableSeatController"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page errorPage="ErrorPage.jsp" %>

<%
ReserveTableSeatController ctrl = new ReserveTableSeatController();
Session ssn = (Session) session.getAttribute("ssn");
if(ssn == null){
	throw new Exception("Session is expired! Please go back and log in again.");
}
List<TableBean> beans = ctrl.retrieveDeletableTables(ssn.getUser());
%>    
    
    <%
    if(request.getParameter("btnDel") != null){
    	ssn = (Session) session.getAttribute("ssn");
    	if(ssn == null){
    		throw new Exception("Session is expired! Please go back and log in again.");
    	}
    	
    	String selected = request.getParameter("selTable");
    	if(selected == null || selected.isBlank()){
    		throw new Exception("You did not select a table!");
    	}
    	
    	for(TableBean tb : beans){
    		if(tb.getName().equals(selected)){
    			ReserveTableSeatController controller = new ReserveTableSeatController();
    			controller.deleteTable(tb);
    			%>
    			<jsp:forward page="OrganizedTables.jsp"/>
    			<% 
    		}
    	}
    }
    %>
    
    
<!DOCTYPE html>
<html lang = "en">
<head>
<meta charset="ISO-8859-1">
<title>My Organized Tables</title>

<link rel="stylesheet" href="css/basicStyle.css">
<link rel="stylesheet" href="css/organizedtables.css">
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
			ssn = (Session) session.getAttribute("ssn");
			if(ssn == null){
				throw new Exception("Session is expired! Please go back and log in again.");
			}
			UserType type = ssn.getUser().getUserType();
			if(type == UserType.PLAYER){
			%>
			<a href="PlayerUserPage.jsp"><span>User</span></a>
			<a href="CreateTable.jsp"><span>Tables</span></a>
			<a href="JoinTournament.jsp"><span>Tournaments</span></a>
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
			<div class="topnav">
  				<a href="CreateTable.jsp">Create Table</a>
  				<%if(type == UserType.PLAYER){
  					%>
  					<a href="PlayerReserveTable.jsp">Reserve A Seat</a>
  					<%
  				}
  				%>
  				<a href="PlayerTableDeclareWinner.jsp">Declare Winner</a>
  				<a href="OrganizedTables.jsp">Organized Tables</a>
  				
			</div>
			
			<div id="innerPage" class="innerDiv">
				<div class="formDiv">
					<form action="OrganizedTables.jsp" name="form">
						<select id="selTable" name="selTable" onchange="loadPreview();">
							<option disabled selected>-- Select Table --</option>
						<%
						for(TableBean b : beans){
							%><option><%= b.getName() %></option><%
						}
						%>
						</select>
						
						<input type="submit" id="btnDel" name="btnDel" value ="Delete">	
					</form>
				</div>
			
			<div id="tableDiv" class="tableDiv">
			
			</div>
							
			</div>
		</div>
	</div>
	
	<script>
	function loadPreview(){
		var selectedTable = document.getElementById("selTable").value;
		var newHtml = "";
		<%
		for(TableBean t : beans){
			%>
			if(selectedTable == <%="\"" + t.getName() + "\""%>){
				newHtml += "<label class='title'>" + <%="\"" + t.getName() + "\""%> + "</label>";
				newHtml += "<label>" + <%="\"" + t.getAddress() + "\""%> + "</label>";
				newHtml += "<label>" + <%="\"" + t.getCardGame() + "\""%> + "</label>";
				newHtml += "<label>" + <%="\"" + t.getDate() + "\""%> + "</label>";
				newHtml += "<label>" + <%="\"" + t.getTime() + "\""%> + "</label>";
				console.log(newHtml);
			}
			<%
		}
		%>
		document.getElementById("tableDiv").innerHTML = newHtml;
	}
	</script>
</body>

</html>