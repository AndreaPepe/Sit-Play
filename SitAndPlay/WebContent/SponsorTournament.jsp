<%@page import="main.java.engineering.utils.MapMarkersUtil"%>
<%@page import="main.java.engineering.bean.businessactivity.BusinessActivityBean"%>
<%@page import="main.java.engineering.bean.tournaments.TournamentBean"%>
<%@page import="java.util.List"%>
<%@page import="main.java.engineering.utils.Session"%>
<%@page import="main.java.controller.applicationcontroller.business.SponsorizeTournamentController"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page errorPage="ErrorPage.jsp" %>
    
    <%
    Session ssn = (Session) session.getAttribute("ssn");
    if(ssn == null){
    	throw new Exception("Session is expired! Please go back and log in again!");
    }
    SponsorizeTournamentController ctrl = new SponsorizeTournamentController();
    List<TournamentBean> tournaments = ctrl.getTournamentsToSponsorize();
    List<BusinessActivityBean> activities = ctrl.getBusinessmanActivities(ssn.getUser());
    %>
    
    
    <%
    if(request.getParameter("btnSponsor") != null){
    	ssn = (Session) session.getAttribute("ssn");
        if(ssn == null){
        	throw new Exception("Session is expired! Please go back and log in again!");
        }
        
        String selectedTmt = request.getParameter("selTmt");
        if(selectedTmt == null || selectedTmt.isBlank()){
        	throw new Exception("You did not select a tournament!");
        }
        String selectedActivity = request.getParameter("selAct");
        if(selectedActivity == null || selectedActivity.isBlank()){
        	throw new Exception("You did not select a business activity to sponsor tournament!");
        }
        
        BusinessActivityBean sponsor = null;
        for(BusinessActivityBean act : activities){
        	if(act.getName().equals(selectedActivity)){
        		sponsor = act;
        	}
        }
        
        TournamentBean tournament = null;
        for(TournamentBean b: tournaments){
        	if(b.getName().equals(selectedTmt)){
        		tournament = b;
        		ctrl = new SponsorizeTournamentController();
                ctrl.confirmSponsorization(tournament, sponsor);
                %>
                <jsp:forward page="SponsorTournament.jsp"/>
                <%
        	}
        }
        
        
    }
    %>
    
    
    
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>Sponsor Tournament</title>

<link rel="stylesheet" href="css/basicStyle.css">
<link rel="stylesheet" href="css/PlayerReserveTable.css">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<script src="https://api.mapbox.com/mapbox.js/v3.2.1/mapbox.js"></script>
<link href="https://api.mapbox.com/mapbox.js/v3.2.1/mapbox.css" rel="stylesheet" />

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
			<a href="BusinessmanUserPage.jsp"><span>User</span></a>
			<a href="CreateActivity.jsp"><span>Activities</span></a>
			<a href="SponsorTournament.jsp"><span>Tournaments</span></a>
			<a href="Notifications.jsp"><span>Notifications</span></a>
		</div>
	
		<div id="content" class="content">
			<div class="topnav">
  				<a href="SponsorTournament.jsp">Sponsor Tournament</a>
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
				
				<form action="SponsorTournament.jsp" name="sponsorTournament">
					<input type="text" readonly="readonly" id="selTmt" name ="selTmt" value="">
					<select id="selAct" name="selAct" class="selAct">
						<option selected disabled>-- Select Activity --</option>
						<%
						for(BusinessActivityBean act : activities){
							%>
							<option><%= act.getName() %></option>
							<%
						}						
						%>
					</select>
					<input type="submit" id="btnSponsor" name="btnSponsor" value="Sponsor Tournament" class="btn">
				</form>
				
			</div>
		</div>
	</div>
	</div>
</body>
</html>