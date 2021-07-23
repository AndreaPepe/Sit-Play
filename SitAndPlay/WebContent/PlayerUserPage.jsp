<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang ="en">
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>

<link rel="stylesheet" href="css/topbar.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<script type="text/javascript">
	function loadProfile(){
		$("#innerPage").load("PlayerProfile.jsp");
	}
	
	function loadSeats(){
		$("#innerPage").load("PlayerReservedSeats.jsp");
	}
</script>

</head>
<body onload="$(document).ready(loadProfile)">
	<div class="topnav">
  		<a onclick="loadProfile()">Profile</a>
  		<a onclick="loadSeats()">My Reserved Seats</a>
	</div>
	
	<div id="innerPage" class="innerDiv">
	
	</div>
</body>


</html>