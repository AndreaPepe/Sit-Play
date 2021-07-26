<%@page import="java.text.ParseException"%>
<%@page import="main.java.engineering.exceptions.DateParsingException"%>
<%@page import="main.java.engineering.exceptions.MapboxException"%>
<%@page import="main.java.engineering.exceptions.BeanCheckException"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="main.java.engineering.utils.Session"%>
<%@page import="main.java.engineering.bean.createtable.PlaceBean"%>
<%@page import="main.java.engineering.bean.createtable.TableBean"%>
<%@page import="main.java.controller.applicationcontroller.createtable.CreateTableController"%>
<%@page import="java.util.ArrayList"%>
<%@page import="main.java.model.CardGame"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@page import="main.java.engineering.utils.map.MapPlaceAdapter"%>
<%@page import="java.util.List"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="main.java.controller.MapboxController"%>
<%@page import="main.java.engineering.utils.map.MapPlace"%>    

<%@page errorPage="ErrorPage.jsp" %>
    
    
<% MapPlace selectedPlace; 

%>    
    
<%
	if(request.getParameter("btnCreate")!=null){
		String name = request.getParameter("tablename");
		String placeName = request.getParameter("place");
		if(placeName == null || placeName.isBlank()){
			throw new BeanCheckException("The selected place is missing");
		}
		MapboxController ctrl = new MapboxController();
		List<JSONObject> predictions = ctrl.getPredictions(placeName);
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
			Session ssn = (Session) session.getAttribute("ssn");
			CreateTableController controller = new CreateTableController();
			PlaceBean placeBean = new PlaceBean(mapPlace.toString(), mapPlace.getCoordinates().get(0), mapPlace.getCoordinates().get(1));
			TableBean bean = new TableBean(name, placeBean, cgame, correctDate, time, ssn.getUser().getUsername());
			bean.checkCreateTable();
			
			controller.createTable(bean);
		}catch (ParseException e){
			throw new Exception("Date or time are missing or possibly written in a wrong format");
		}
	}
%>    
    
    
    
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>Create table</title>


<link rel="stylesheet" href="css/createtable.css">
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
			<a href="PlayerUserPage.jsp"><span>User</span></a>
			<a href="CreateTable.jsp"><span>Tables</span></a>
			<a href="JoinTournament.jsp"><span>Tournaments</span></a>
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
			<div class="left_div">
			<form action="CreateTable.jsp" name="create">
				<input id="tablename" type="text" name="tablename" placeholder="Table's name"> 
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
				
				<input id="btnCreate" name="btnCreate" type="submit" value="Create Table">
			</form>
			
			</div>
	
			<div id="map" class="map">
	
			</div>
	</div>
		</div>
	</div>
	
</body>
</html>