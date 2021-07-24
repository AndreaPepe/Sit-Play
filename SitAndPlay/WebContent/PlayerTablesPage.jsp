<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang ="en">
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>

<link rel="stylesheet" href="css/topbar.css">
<script src="javascript/jquery.min.js"></script>

<script type="text/javascript">
	function loadCreate(){
		$("#innerPage").load("CreateTable.jsp");
	}
	
</script>

</head>
<body onload="$(document).ready(loadProfile)">
	<div class="topnav">
  		<a onclick="loadCreate()">Create Table</a>
  		<a onclick="loadCreate()">Reserve A Seat</a>
  		<a onclick="loadCreate()">My Tables</a>
  		<a onclick="loadCreate()">Organized Tables</a>
	</div>
	
	<div id="innerPage" class="innerDiv">
	
	</div>
</body>


</html>