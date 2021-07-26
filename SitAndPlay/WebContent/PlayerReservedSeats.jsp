<%@page import="main.java.engineering.utils.MapMarkersUtil"%>
<%@page import="main.java.engineering.bean.createtable.TableBean"%>
<%@page import="main.java.controller.applicationcontroller.reserveaseat.table.ReserveTableSeatController"%>
<%@page import="main.java.engineering.bean.tournaments.TournamentBean"%>
<%@page import="java.util.List"%>
<%@page import="main.java.engineering.utils.Session"%>
<%@page import="main.java.controller.applicationcontroller.reserveaseat.tournament.ReserveTournamentSeatController"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page errorPage="ErrorPage.jsp" %>
    
<%
Session ssn = (Session) session.getAttribute("ssn");
if(ssn == null){
	throw new Exception("Session is expired! Please go back and log in again");
}
ReserveTournamentSeatController tmtCtrl = new ReserveTournamentSeatController();
List<TournamentBean> tournaments = tmtCtrl.retrieveActiveJoinedTournaments(ssn.getUser());

ReserveTableSeatController tabCtrl = new ReserveTableSeatController();
List<TableBean> tables = tabCtrl.retrieveActiveJoinedTables(ssn.getUser());
%>    
    
	<%
	if(request.getParameter("leaveTab") != null){
		ssn = (Session) session.getAttribute("ssn");
		if(ssn == null){
			throw new Exception("Session is expired! Please go back and log in again");
		}
		String selTable = request.getParameter("selTab");
		if(selTable == null || selTable.isBlank()){
			throw new Exception("You did not select a table!");
		}
		for(TableBean bn : tables){
			if(bn.getName().equals(selTable)){
				ReserveTableSeatController ctrl = new ReserveTableSeatController();
				ctrl.removeParticipant(bn, ssn.getUser());
				%>
				<jsp:forward page="PlayerReservedSeats.jsp"/>
				<%
			}
		}
	}
	%>
	
	<%
	if(request.getParameter("leaveTmt") != null){
		ssn = (Session) session.getAttribute("ssn");
		if(ssn == null){
			throw new Exception("Session is expired! Please go back and log in again");
		}
		String selTournament = request.getParameter("selTmt");
		if(selTournament == null || selTournament.isBlank()){
			throw new Exception("You did not select a table!");
		}
		for(TournamentBean tmt : tournaments){
			if(tmt.getName().equals(selTournament)){
				ReserveTournamentSeatController controller = new ReserveTournamentSeatController();
				controller.removeParticipant(tmt, ssn.getUser());
				%>
				<jsp:forward page="PlayerReservedSeats.jsp"/>
				<%
			}
		}
	}
	%>    
    
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>My Reserved Seats</title>


<link rel="stylesheet" href="css/basicStyle.css">
<link rel="stylesheet" href="css/PlayerReservedSeats.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<script type="text/javascript">
function changeTab(){
	var selectedTab = document.getElementById("selTab").value;
	var newHtml = "";
	<%
	for(TableBean t : tables){
		%>
		if(selectedTab == <%="\"" + t.getName() + "\""%>){
			newHtml += "<label class='title'>" + <%="\"" + t.getName() + "\""%> + "</label>";
			newHtml += "<label class='lbl'>" + <%="\"" + t.getAddress() + "\""%> + "</label>";
			newHtml += "<label class='lbl'>" + <%="\"" + t.getCardGame() + "\""%> + "</label>";
			newHtml += "<label class='lbl'>" + <%="\"" + t.getDate() + "\""%> + "</label>";
			newHtml += "<label class='lbl'>" + <%="\"" + t.getTime() + "\""%> + "</label>";
			newHtml += "<label class='lbl'>" + <%="\"" + t.getOrganizer() + "\""%> + "</label>";
		}
		<%
	}
	%>
	document.getElementById("singleTab").innerHTML = newHtml;
}

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
	document.getElementById("singleTourn").innerHTML = newHtml;
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
			<a href="JoinTournament.jsp"><span>Tournaments</span></a>
			<a href="Notifications.jsp"><span>Notifications</span></a>
		</div>
	
		<div id="content" class="content">
			<div class="topnav">
  				<a href="PlayerUserPage.jsp">Profile</a>
  				<a href="PlayerReservedSeats.jsp">My Reserved Seats</a>
			</div>
			
			<div id="innerPage" class="innerDiv">
				<div class="topDiv">
					<div class="formTables">
						<form action="PlayerReservedSeats.jsp" name="tablesForm">
							<select id="selTab" name="selTab" onchange="changeTab()">
								<option disabled selected>-- Select table --</option>
								<%
								for(TableBean tabBean: tables){
									%>
									<option><%= tabBean.getName() %></option>
									<% 
								}
								%>
							</select>
							
							<input type="submit" id="leaveTab" name="leaveTab" value="Leave">
						</form>
					</div>
					
					<div id="singleTab" class="singleTab">
					
					</div>
				</div>
	
				
				
				<div class = "bottomDiv">
					<div class="formTournaments">
						<form action="PlayerReservedSeats.jsp" name="tournamentsForm">
							<select id="selTmt" name="selTmt" onchange="changeTmt()">
								<option disabled selected>-- Select tournament --</option>
								<%
								for(TournamentBean tmtBean: tournaments){
									%>
									<option><%= tmtBean.getName() %></option>
									<% 
								}
								%>
							</select>
							
							<input type="submit" id="leaveTmt" name="leaveTmt" value="Leave">
						</form>
					</div>
					
					<div id="singleTourn" class="singleTourn">
					
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>