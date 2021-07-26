<%@page import="main.java.engineering.bean.login.BeanUser"%>
<%@page import="main.java.engineering.utils.Session"%>
<%@page import="main.java.engineering.bean.createtable.TableBean"%>
<%@page import="java.util.List"%>
<%@page import="main.java.controller.applicationcontroller.reserveaseat.table.ReserveTableSeatController"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page errorPage="ErrorPage.jsp" %>

<%
ReserveTableSeatController ctrl = new ReserveTableSeatController();
List<TableBean> beanList = ctrl.retrieveOpenTables();
if(beanList == null || beanList.isEmpty()){
	throw new Exception("No table has been found, retry later!");
}
%>    
    
    <%
    if(request.getParameter("btnReserve") != null){
    	if((Session) session.getAttribute("ssn") == null){
    		throw new Exception("Session is expired. Please go Back to login page");
    	}else{
    		String tablename = (String) request.getParameter("selTable");
    		if(tablename == null || tablename.isBlank()){
    			throw new Exception("No table selected");
    		}
    		TableBean selBean = null;
    		for(TableBean b: beanList){
    			if(b.getName().equals(tablename)){
    				selBean = b;
    				break;
    			}
    		}
    		if(selBean == null){
    			throw new Exception("Something went wrong, please reload the page");
    		}
    		
    		ReserveTableSeatController controller = new ReserveTableSeatController();
    		Session mySession = (Session) session.getAttribute("ssn");
    		BeanUser user = mySession.getUser();
    		controller.joinTable(selBean, user);
    	}
    }
    %>
    
    
    
    
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>Reserve Seat At Table</title>

<link rel="stylesheet" href="css/basicStyle.css">
<link rel="stylesheet" href="css/PlayerReserveTable.css">

<script src="https://api.mapbox.com/mapbox.js/v3.2.1/mapbox.js"></script>
<link href="https://api.mapbox.com/mapbox.js/v3.2.1/mapbox.css" rel="stylesheet" />

<script src="javascript/jquery.min.js"></script>

<style >
			.leaflet-popup-close-button {
				position: absolute;
				top: 0;
				left: 0;
				padding: 4px 0 0 5px;
				text-align: center;
				font-weight: bold;
			}
		</style>
		<script>
		var map = null;
		var selectedTable = null;
		var lastSelectedMarker = null;
		function loadMap(){
			if(map == null){
				L.mapbox.accessToken = "pk.eyJ1IjoiYW5kcmVhcGVwZTMwIiwiYSI6ImNrcW52aGlzejAxNGUydXFzN2Y3N25lemwifQ._T3qFEHfLLxs4R4Nhy69rg";
				map= L.mapbox.map('map').setView([41.89306,12.48278],5);
			    L.mapbox.styleLayer('mapbox://styles/mapbox/outdoors-v11').addTo(map);
			}
		}	
		
		function addMarker(lat,lng,icon,color,idname, game, address, date, time){
			
			L.mapbox.accessToken = "pk.eyJ1IjoiYW5kcmVhcGVwZTMwIiwiYSI6ImNrcW52aGlzejAxNGUydXFzN2Y3N25lemwifQ._T3qFEHfLLxs4R4Nhy69rg";
			if(map == null){
				loadMap();
			}
			if(icon == "null"){
				icon = "marker";
			}
			var marker = L.marker(L.latLng(lat,lng), {
				icon: L.mapbox.marker.icon({
					'marker-size': 'large',
					'marker-symbol': "circle",
					'marker-color': color
				})
			}).addTo(map);
			
			let popupHtml = `
			<h3>` + idname + `</h3>
			<p> Game: ` + game + `</p>
			<p> Address: ` + address + `</p>
			<p> Date: ` + date + `</p>
			<p> Time: ` + time + `</p>
			`;
			
			var popup = L.popup({maxHeight: 220, minWidth: 400}).setContent(popupHtml);
			popup.addEventListener
			marker.bindPopup(popup,{closeOnClick: false})
			
			marker.on('click', function(e){
				// make the selected marker red
				marker.setIcon(L.mapbox.marker.icon({
					'marker-size': 'large',
					'marker-symbol': "circle",
					'marker-color': "#ff0000"
				}));
				if(lastSelectedMarker != null && lastSelectedMarker != marker){
					lastSelectedMarker.setIcon(L.mapbox.marker.icon({
						'marker-size': 'large',
						'marker-symbol': "circle",
						'marker-color': color
					}));
				}				
				lastSelectedMarker = marker;
				selectedTable = idname;
				
				document.getElementById('selTable').value = selectedTable;
			});
		}
		
		function getSelectedTable(){
			return selectedTable;
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
  				<a href="CreateTable.jsp">Create Table</a>
  				<a href="PlayerReserveTable.jsp">Reserve A Seat</a>
  				<a href="PlayerTableDeclareWinner.jsp">My Tables</a>
  				<a href="OrganizedTables.jsp">Organized Tables</a>
			</div>
	
		<div id="innerPage" class="innerDiv">
			<div id="map" class="map">
				<%
				if(beanList.isEmpty()){
					%>
					<script>
						loadMap();
					</script>
					<%
				}
				for (TableBean bean : beanList){
					double lat = bean.getLatitude();
					double lng = bean.getLongitude();
					String idname =  bean.getName();
					String cardGame = bean.getCardGame();
					String address = bean.getAddress();
					String date = bean.getDate();
					String time = bean.getTime();
					String color = "#214183";
					%>
					<script>
						var name = <%= "\"" + idname.toString() +"\"" %>;
						var latid = <%= lat%>;
						var longit = <%= lng%>;
						var cg = <%= "\"" + cardGame.toString() + "\""%>;
						var addr = <%= "\"" + address.toString() + "\""%>;
						var d = <%="\"" + date.toString() +"\""%>;
						var nl = "null";
						var t = <%= "\"" + time.toString() + "\""%>;
						var col = <%="\"" + color.toString() +"\""%>;
						
						addMarker(latid , longit, nl, col, name, cg, addr, d, t);
					</script>
					<%
				}
				%>
			</div>
			
			<div class="right_col">
				<label> You have currently selected the table:</label>
				
				<form action="PlayerReserveTable.jsp" name="reserveTable">
					<input type="text" readonly="readonly" id="selTable" name ="selTable" value="">
					<input type="submit" id="btnReserve" name="btnReserve" value="Reserve A Seat" class="btn">
				</form>
				
			</div>
	</div>
		</div>
	</div>
</body>
</html>