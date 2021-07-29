<%@page import="main.java.engineering.utils.MapMarkersUtil"%>
<%@page import="main.java.engineering.bean.tournaments.TournamentBean"%>
<%@page import="java.util.List"%>
<%@page import="main.java.controller.applicationcontroller.reserveaseat.tournament.ReserveTournamentSeatController"%>
<%@page import="main.java.engineering.utils.Session"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page errorPage="ErrorPage.jsp" %>
    
    
<%
Session ssn = (Session) session.getAttribute("ssn");
if(ssn == null){
	throw new Exception("Session is exprired! Please go back and log in again.");
}
ReserveTournamentSeatController ctrl = new ReserveTournamentSeatController();
List<TournamentBean> tournaments = ctrl.getDeletableTournaments(ssn.getUser());
%>    
    
    <%
    if(request.getParameter("btnDel") != null){
    	ssn = (Session) session.getAttribute("ssn");
    	if(ssn == null){
    		throw new Exception("Session is expired! Please go back and log in again.");
    	}
    	
    	String selected = request.getParameter("selTmt");
    	if(selected == null || selected.isBlank()){
    		throw new Exception("You did not select a tournament!");
    	}
    	
    	for(TournamentBean tb : tournaments){
    		if(tb.getName().equals(selected)){
    			ReserveTournamentSeatController controller = new ReserveTournamentSeatController();
    			controller.deleteTournament(tb);
    			%>
    			<jsp:forward page="HandleTournaments.jsp"/>
    			<% 
    		}
    	}
    }
    %>
    
    
    
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>Handle Tournaments</title>

<link rel="stylesheet" href="css/basicStyle.css">
<link rel="stylesheet" href="css/handleTournaments.css">

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
  				<a href="CreateTournament.jsp">Create Tournament</a>
  				<a href="HandleTournaments.jsp">HandleTournaments</a>
  				<a href="DeclareWinnerTournament.jsp">Declare Winner</a>
			</div>
			
			<div id="innerPage" class="innerDiv">
				<div class="formDiv">
					<form action="HandleTournaments.jsp" name="form" method="get">
						<select id="selTmt" name="selTmt" onchange="changeTmt();">
							<option disabled selected>-- Select Tournament --</option>
						<%
						for(TournamentBean b : tournaments){
							%><option><%= b.getName() %></option><%
						}
						%>
						</select>
						
						<input type="submit" id="btnDel" name="btnDel" value ="Delete" onclick="return confirm('This is an irreversible operation. Are you sure?')">	
					</form>
				</div>
			
				<div id="tmtDiv" class="tmtDiv">
			
				</div>
			</div>
		</div>
	</div>
	
	
	<script>
	function changeTmt(){
		var selectedTmt = document.getElementById("selTmt").value;
		var newHtml = "";
		<%
		for(TournamentBean tourB : tournaments){
			%>
			if(selectedTmt == <%="\"" + tourB.getName() + "\""%>){
				newHtml += "<label class='title'>" + <%="\"" + tourB.getName() + " - " + tourB.getCardGame() + "\""%> + "</label>";
				newHtml += "<label class='lbl'>" + <%="\"" + tourB.getAddress() + "\""%> + "</label>";
				newHtml += "<label class='lbl'>" + <%="\"" + tourB.getDate() +"&nbsp;&nbsp;&nbsp;&nbsp;" + tourB.getTime() +
						"&nbsp;&nbsp;&nbsp;&nbsp;Organized by: " + tourB.getOrganizer() + "\""%> + "</label>";
				<%
				if(tourB.getSponsor() != null){
					%>
					newHtml += "<label class='lbl'>" + <%="\"" + "Sponsored by: " + tourB.getSponsor().getName() + "\""%> + "</label>";
					<%
					String logo;
					if(tourB.getSponsor().getLogo() != null){
						String activityLogo = MapMarkersUtil.encodeToBase64UTF8(tourB.getSponsor().getLogo());
						logo = String.format("<img class='logo' src='data:image/*;base64,%s'>", activityLogo);
					}else{
						logo = "<img class='logo' src='resources/no_image.png'>";
					}
					%>
					
					newHtml += <%="\"" + logo + "\""%> ;
					<%
				}
				%>
				
			}
			<%
		}
		%>
		document.getElementById("tmtDiv").innerHTML = newHtml;
	}
	</script>
	
</body>
</html>