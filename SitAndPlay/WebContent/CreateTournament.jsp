<%@page import="main.java.controller.applicationcontroller.createtournament.CreateTournamentController"%>
<%@page import="main.java.engineering.bean.tournaments.TournamentBean"%>
<%@page import="java.text.ParseException"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="main.java.engineering.utils.map.MapPlaceAdapter"%>
<%@page import="main.java.engineering.exceptions.MapboxException"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="java.util.List"%>
<%@page import="main.java.controller.MapboxController"%>
<%@page import="main.java.engineering.utils.Session"%>
<%@page import="main.java.model.CardGame"%>
<%@page import="main.java.engineering.utils.map.MapPlace"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page errorPage="ErrorPage.jsp" %>
    
	<%
	if(request.getParameter("btnCreate") != null){
		Session ssn = (Session) session.getAttribute("ssn");
		if(ssn == null){
			throw new Exception("Session is expired! Please go back and log in again!");
		}
		
		String name = request.getParameter("tmtname");
		String placeName = request.getParameter("place");
		if(placeName == null || placeName.isBlank()){
			throw new Exception("The selected place is missing");
		}
		MapboxController ctrl = new MapboxController();
		List<JSONObject> predictions = ctrl.getPlaces(placeName);
		if(predictions.isEmpty()){
			throw new MapboxException("Seems to be no places with this name");
		}
		MapPlace mapPlace = null;
		for(JSONObject candidate : predictions){
			MapPlace pl = new MapPlaceAdapter(candidate);
			if(pl.toString().equals(placeName)){
				mapPlace = pl;
			}
		}
		if(mapPlace == null){
			mapPlace = new MapPlaceAdapter(predictions.get(0));
		}
		String cgame = request.getParameter("cardGame");
		try{
			String date = request.getParameter("date");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			Date builtDate = sdf.parse(date);
			String correctDate = new SimpleDateFormat("dd/MM/yyyy").format(builtDate);
			
			String time = request.getParameter("time");
			int maxPart = Integer.parseInt(request.getParameter("part"));
			float price = Float.parseFloat(request.getParameter("price"));
			float award = Float.parseFloat(request.getParameter("award"));
			String reqSponsor = request.getParameter("sponsor");
			boolean sponsorizable;
			if(reqSponsor != null && reqSponsor.equals("true")){
				sponsorizable = true;
			}else{
				sponsorizable = false;
			}
			
			TournamentBean newTournament = new TournamentBean();
			newTournament.setName(name);
			newTournament.setAddress(mapPlace.toString());
			newTournament.setLatitude(mapPlace.getCoordinates().get(0));
			newTournament.setLongitude(mapPlace.getCoordinates().get(1));
			newTournament.setCardGame(cgame);
			newTournament.setDate(correctDate);
			newTournament.setTime(time);
			newTournament.setMaxParticipants(maxPart);
			newTournament.setPrice(price);
			newTournament.setAward(award);
			newTournament.setInRequestForSponsor(sponsorizable);
			newTournament.setOrganizer(ssn.getUser().getUsername());
			
			newTournament.checkRulesForInsert();
			
			CreateTournamentController controller = new CreateTournamentController();
			controller.createTournament(newTournament);
		}catch (ParseException e){
			throw new Exception("Date or time are missing or possibly written in a wrong format");
		}catch(NumberFormatException ex){
			throw new Exception("One of the fields 'Max number of participants', 'Price', 'Award' is blank or written in a wrong format");
		}
	}
	%>

    
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>Create Tournament</title>

<link rel="stylesheet" href="css/createtournament.css">
<link rel="stylesheet" href="css/basicStyle.css">

<script src="https://api.mapbox.com/mapbox.js/v3.2.1/mapbox.js"></script>
<link href="https://api.mapbox.com/mapbox.js/v3.2.1/mapbox.css" rel="stylesheet" />

<script src="javascript/jquery.min.js"></script>
<link href="http://code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css" rel="stylesheet"></link>
<script src="https://code.jquery.com/jquery-1.10.2.js" ></script>  
<script src="https://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>

<script type="text/javascript">
	var list = [];
	var lati = [];
	var longi = [];
	$(function()

	{
		$("#place").autocomplete(
				{
					position : {
						my : "left top",
						at : "left-bottom",
						collision : "none"
					},
					minlength : 1,
					// retreive sources for autocomplete
					source : function(request, response) {

						$.ajax({
							url : "mapTextFieldAutocomplete.jsp",
							method : "get",
							dataType: "json",
							data:{place:request.term},

							success : function(data) {
								console.log(data);
								
								list = [];
								lati = [];
								longi = [];
								for(variable in data){
									list.push(data[variable]['0']);
									lati.push(data[variable]['1']);
									longi.push(data[variable]['2']);
								}
								response(list);
							},
							
							error: function(err){
								console.log("Error: ", err);
							},

							open : function() {
								$(this).removeClass("ui-corner-all").addClass(
										"ui-corner-top");
							},

							close : function() {
								$(this).removeClass("ui-corner-top").addClass(
										"ui-corner-all");
							}

						});
					},
					
					select: function(event, ui){
						var selectedUi = ui.item ? ui.item.value : "empty";
						if(selectedUi != "empty"){
							var key = list.indexOf(selectedUi);
							var lt = parseFloat(lati[key]);
							var lg = parseFloat(longi[key]);
							updateMap(lt,lg, 9);
						}
						
					}
					
				});
	});
	
	
	var map;
	var singleMarker = null;
	function loadMap(){
		map = null;
		L.mapbox.accessToken = "pk.eyJ1IjoiYW5kcmVhcGVwZTMwIiwiYSI6ImNrcW52aGlzejAxNGUydXFzN2Y3N25lemwifQ._T3qFEHfLLxs4R4Nhy69rg";
		map= L.mapbox.map('map').setView([41.89306,12.48278],5);
	    L.mapbox.styleLayer('mapbox://styles/mapbox/outdoors-v11').addTo(map);
	}
	
	function updateMap(lat,lng,zoom){
		map.flyTo(L.latLng(lat, lng), zoom);
		addSingleMarker(lat,lng);
	}
	
	function addSingleMarker(lat,lng){
		if(singleMarker != null){
			map.removeLayer(singleMarker);
		}
		singleMarker=L.marker(L.latLng(lat,lng), {
			icon: L.mapbox.marker.icon({
				'marker-size': 'large',
		        'marker-symbol': "marker",
		        'marker-color': "#214183"
			})
		});
		map.addLayer(singleMarker);
		
		singleMarker.bindPopup("You have selected this place",{closeOnClick: false});
		singleMarker.on('click',function(e){
			singleMarker.openPopup();
		});
	}	
	
	
	function init(){
		$(document).ready(loadMap);
	}
	
	
</script>


</head>
<body onload="init();">
	
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
				<div class="left_div">
				<form action="CreateTournament.jsp" name="create" method="get">
					<input id="tmtname" type="text" name="tmtname" placeholder="Tournament's name"> 
					<input id="place" type="text" name="place" placeholder="Place" class="ui-widget">
					<select id="cardGame" name="cardGame">
						<option disabled selected>-- Card Game --</option>
						<%
						for(CardGame cg: CardGame.values()){
							%>
							<option value= "<%= cg.toString()%>"><%= cg.toString()%></option>
							<%
						}
						%>
						
					</select>
					
					<input id="date" type="date" name="date">
					<input id="time" type="time" name="time">
					<input id="part" type="text" name="part" placeholder="Max number of participants" pattern="[1-9]{1}[0-9]{0,5}" title="Must be an integer (e.g. '5')">
					<input id="price" type="text" name="price" placeholder="Price [&euro;XXX.XX]" pattern="\d+(.\d{1,2})?" title="Must be an integer or a float value with 1 or 2 decimal digits after the point (e.g. '12.4', '123.94')">
					<input id="award" type="text" name="award"  placeholder="Award [&euro;XXX.XX]" pattern="\d+(.\d{1,2})?" title="Must be an integer or a float value with 1 or 2 decimal digits after the point (e.g. '12.4', '123.94')">
					<input id="sponsor" name="sponsor" type="checkbox" value="true">
					<label for="sponsor">Request Sponsor</label>
					
					<input id="btnCreate" name="btnCreate" type="submit" value="Create Tournament">
				</form>
				
				</div>
		
				<div id="map" class="map">
		
				</div>
			</div>
	</div>
		</div>
</body>
</html>