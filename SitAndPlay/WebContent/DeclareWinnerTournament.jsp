<%@page import="java.util.List"%>
<%@page import="main.java.controller.applicationcontroller.reserveaseat.tournament.ReserveTournamentSeatController"%>
<%@page import="main.java.engineering.utils.Session"%>
<%@page import="main.java.engineering.bean.tournaments.TournamentBean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page errorPage="ErrorPage.jsp" %>
    
<%
Session ssn = (Session) session.getAttribute("ssn");
if(ssn == null){
	throw new Exception("Session is expired! Please go back and log in again");
}

ReserveTournamentSeatController ctrl = new ReserveTournamentSeatController();
List<TournamentBean> tournaments = ctrl.retrieveTournamentsToDeclareWinnerTo(ssn.getUser());
%>
    <%
    if(request.getParameter("btnDeclare")!=null){
    	ssn = (Session) session.getAttribute("ssn");
    	if(ssn == null){
    		throw new Exception("Session is expired; please go back and log in again");
    	}
    	
    	String tournamentName = request.getParameter("tmt");
    	String winner = request.getParameter("winner");
    	if(tournamentName == null || winner == null){
    		throw new Exception("You did not select tournament or participant; tournament: " + tournamentName + "  participant: " + winner);
    	}
		ReserveTournamentSeatController controller = new ReserveTournamentSeatController();
    	List<TournamentBean> newList = controller.retrieveTournamentsToDeclareWinnerTo(ssn.getUser());
    	
    	TournamentBean selectedTmt = null;
    	for(TournamentBean t : newList){
    		if(tournamentName.equals(t.getName())){
    			selectedTmt = t;
    		}
    	}
    	if(selectedTmt != null){
    		selectedTmt.setWinner(winner);
			controller.setWinner(selectedTmt, ssn.getUser());
    		%>
        	<jsp:forward page="DeclareWinnerTournament.jsp"/>
        	<%
    	}
    }
    %>
    
    
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>Declare Winner of a Tournament</title>

<link rel="stylesheet" href="css/basicStyle.css">

<link rel="stylesheet" href="css/basicStyle.css">
<link rel="stylesheet" href="css/declareWinner.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<script>
function changePartips() {
	var selTmt = document.getElementById("tmt").value;
	var options = "<option disabled selected>-- Select participant --</option>";
	<%
	for(TournamentBean bean : tournaments){
		%>
		if(<%= "\"" + bean.getName() + "\""%> == selTmt){
			<% 
			List<String> participants = bean.getParticipants();
			for(String part : participants){
				%>
				console.log(<%= "\"" + part + "\"" %>);
				options += ("<option>" + <%= "\"" + part + "\""%> + "</option>");
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
  				<a href="CreateTournament.jsp">Create Tournament</a>
  				<a href="HandleTournaments.jsp">HandleTournaments</a>
  				<a href="DeclareWinnerTournament.jsp">Declare Winner</a>
			</div>
			
			<div id="innerPage" class="innerDiv">
				<div class="left">
					<label>Here you can declare the winner of a tournament you have organized</label>
					<label>First select a tournament and then the participant that will be the winner!</label>
				</div>
				
				<div class="right">
					<form action="DeclareWinnerTournament.jsp" name="formWinner">
						<select id="tmt" name="tmt" onchange="changePartips()">
							<option disabled selected>-- Select Tournament --</option>
							<%
							for(TournamentBean tournament: tournaments){
								String name = tournament.getName();
								%>
								<option><%= name %></option>
								<%
							}
							%>
						</select>
					
						<select id="winner" name="winner">
							<option disabled selected>-- Select Participant --</option>
						</select>
					
					
						<input type="submit" id="btnDeclare" name="btnDeclare" value="Declare Winner" onclick="return confirm('This is an irreversible operation. Are you sure?')">
					</form>
					
				</div>
			</div>
	</div>
		</div>
<body>

</body>
</html>