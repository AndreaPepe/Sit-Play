<%@page import="main.java.engineering.bean.login.BeanUser"%>
<%@page import="java.util.Base64"%>
<%@page import="org.apache.commons.io.IOUtils"%>
<%@page import="main.java.engineering.bean.businessactivity.BusinessActivityBean"%>
<%@page import="main.java.engineering.utils.MapMarkersUtil"%>
<%@page import="main.java.engineering.bean.tournaments.TournamentBean"%>
<%@page import="java.util.List"%>
<%@page import="main.java.engineering.utils.Session"%>
<%@page import="main.java.controller.applicationcontroller.reserveaseat.tournament.ReserveTournamentSeatController"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page errorPage="ErrorPage.jsp" %>
    
    <%
    ReserveTournamentSeatController ctrl = new ReserveTournamentSeatController();
    Session ssn = (Session) session.getAttribute("ssn");
    if(ssn == null){
    	throw new Exception("Session is expired! Please go back and log in again.");
    }
    List<TournamentBean> tournaments = ctrl.retrieveOpenTournaments();
    %>
    
    <%
    if(request.getParameter("btnJoin") != null){
    	ssn = (Session) session.getAttribute("ssn");
    	if(ssn == null){
    		throw new Exception ("Session is expired! Go back and log in again.");
    	}
    	
    	String tournamentName = (String) request.getParameter("selTmt");
		if(tournamentName == null || tournamentName.isBlank()){
			throw new Exception("No tournament selected");
		}
		TournamentBean tBean = null;
		for(TournamentBean tb: tournaments){
			if(tb.getName().equals(tournamentName)){
				tBean = tb;
				break;
			}
		}
		if(tBean == null){
			throw new Exception("Something went wrong, please reload the page");
		}
		
		ReserveTournamentSeatController contr = new ReserveTournamentSeatController();
		BeanUser player = ssn.getUser();
		contr.joinTournament(tBean, player);
    }
    %>
    
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>Join Tournament</title>

<link rel="stylesheet" href="css/basicStyle.css">
<link rel="stylesheet" href="css/PlayerReserveTable.css">

<script src="https://api.mapbox.com/mapbox.js/v3.2.1/mapbox.js"></script>
<link href="https://api.mapbox.com/mapbox.js/v3.2.1/mapbox.css" rel="stylesheet" />

<script src="javascript/jquery.min.js"></script>

<style>
			.leaflet-popup-close-button {
				position: absolute;
				top: 0;
				left: 0;
				padding: 4px 0 0 5px;
				text-align: center;
				font-weight: bold;
			}
			
			.centered{
				display:block;
				text-align: center;
			}
			
		</style>
		<script>
		var map;
		var lastSelectedMarker = null;
		var selectedTournament = null;
		
		function addMarker(lat,lng,icon,color,idname, game, address, date, time, organizer, maxParticipants, price, award, sponsor, imgType, logo){
			
			if(map == null){
				loadMap();
			}
			
			if(icon == "null"){
				icon = "marker";
			}
			var marker = L.marker(L.latLng(lat,lng), {
				icon: L.mapbox.marker.icon({
					'marker-size': 'large',
					'marker-symbol': icon,
					'marker-color': color
				})
			}).addTo(map);
			
			let popupHtml = `
			<h3 style="text-align:center">` + idname + `</h3>
			<p> Game: ` + game + `</p>
			<p> Address: ` + address + `</p>
			<p> Date: ` + date + `</p>
			<p> Time: ` + time + `</p>
			<p> Price: ` + price + `</p>
			<h4> Award: ` + award+ `</h4>		
			<p> Organizer: ` + organizer + `</p>
			`;
			
			if(sponsor != "null"){
				popupHtml += `
				<p style="text-align:center;"> Sponsored by ` + sponsor + `</p> 
				`;
				var source =  "data:image/*;base64," + logo;
				if(logo != "null"){
					popupHtml += `<p style="text-align:center;"><img src=` + "\"" + source + "\"" + ` width="80" height="80"></p>`;
				}else{
					// no image
					popupHtml += `<p style="text-align:center;"><img src="resources/no_image.png" width="80" height="80"></p>`;
				}
			}
			
			 
			var popup = L.popup({maxHeight: 220, minWidth: 400}).setContent(popupHtml);
			popup.addEventListener
			marker.bindPopup(popup,{closeOnClick: false})
			
			marker.on('click', function(e){
				marker.setIcon(L.mapbox.marker.icon({
					'marker-size': 'large',
					'marker-symbol': icon,
					'marker-color': "#ff0000"
				}));
				if(lastSelectedMarker != null && lastSelectedMarker != marker){
					lastSelectedMarker.setIcon(L.mapbox.marker.icon({
						'marker-size': 'large',
						'marker-symbol': icon,
						'marker-color': color
					}));
				}				
				lastSelectedMarker = marker;
				selectedTournament = idname;
				
				// refresh the selected tournament in the text box
				document.getElementById("selTmt").value = selectedTournament; 
			});
		}
		
		function getSelectedTournament(){
			return selectedTournament;
		}
		
		
		function loadMap(){
			if(map == null){
				L.mapbox.accessToken = "pk.eyJ1IjoiYW5kcmVhcGVwZTMwIiwiYSI6ImNrcW52aGlzejAxNGUydXFzN2Y3N25lemwifQ._T3qFEHfLLxs4R4Nhy69rg";
				map= L.mapbox.map('map').setView([41.89306,12.48278],5);
			    L.mapbox.styleLayer('mapbox://styles/mapbox/outdoors-v11').addTo(map);
			}
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
  				<a href="JoinTournament.jsp">Join Tournament</a>
			</div>
	
		<div id="innerPage" class="innerDiv">
			<div id="map" class="map">
				<%
				if(tournaments.isEmpty()){
					%>
					<script>
						loadMap();
					</script>
					<%
				}
				for (TournamentBean bean : tournaments){
					double lat = bean.getLatitude();
					double lng = bean.getLongitude();
					BusinessActivityBean sponsorBean = bean.getSponsor();
					String icon = "\"" + "star" + "\"";
					String color = "\"" + "green" + "\"";
					String idname = "\"" + bean.getName() + "\"";
					String cardGame = "\"" + bean.getCardGame() + "\"";
					String address = "\"" + bean.getAddress() + "\"";
					String date = "\"" + bean.getDate() + "\"";
					String time = "\"" + bean.getTime() + "\"";
					String organizer = "\"" + bean.getOrganizer() + "\"";
					
					String maxParticipants = "\"" + bean.getMaxParticipants() +"\"";
					String price = "\"" + String.format("&euro; %.2f", bean.getPrice()) + "\"";
					String award = "\"" + String.format("&euro; %.2f", bean.getAward()) + "\"";
					String sponsor;
					String imgType;
					String logo;
					if(sponsorBean != null) {
						sponsor ="\"" + sponsorBean.getName() + "\"";
						imgType ="\"" + "*" + "\"";
						logo = sponsorBean.getLogo()!=null ? "\"" + MapMarkersUtil.encodeToBase64UTF8(sponsorBean.getLogo()) + "\"" : "\"" + "null" + "\"";
						
						
					}else {
						sponsor = "\"" + "null" + "\"";
						imgType = "\"" + "null" + "\"";
						logo = "\"" + "null" + "\"";
					}
					%>
					<script>
						
						addMarker(<%= lat%> , <%= lng%>, <%= icon%>, <%= color%>, <%= idname %>,
								<%= cardGame%>, <%= address%>, <%= date%>,<%= time%>, <%= organizer%>, 
								<%= maxParticipants%>, <%= price%>, <%= award%>, <%= sponsor%>, <%= imgType%>, <%= logo%>);
					</script>
					<%
				}
				%>
			</div>
			
			<div class="right_col">
				<label> You have currently selected the tournament:</label>
				
				<form action="JoinTournament.jsp" name="reserveTournament">
					<input type="text" readonly="readonly" id="selTmt" name ="selTmt" value="">
					<input type="submit" id="btnJoin" name="btnJoin" value="Reserve A Seat" class="btn">
				</form>
				
			</div>
	</div>
		</div>
	</div>
</body>
</html>