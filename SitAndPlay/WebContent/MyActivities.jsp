<%@page import="main.java.engineering.utils.MapMarkersUtil"%>
<%@page import="main.java.engineering.bean.businessactivity.BusinessActivityBean"%>
<%@page import="java.util.List"%>
<%@page import="main.java.controller.applicationcontroller.business.ManageActivitiesController"%>
<%@page import="main.java.engineering.utils.Session"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page errorPage="ErrorPage.jsp" %>
    
<%
Session ssn = (Session) session.getAttribute("ssn");
if(ssn == null){
	throw new Exception("Session is expired! Please go back and log in again");
}

ManageActivitiesController ctrl = new ManageActivitiesController();
List<BusinessActivityBean> myActivities = ctrl.retrieveActivities(ssn.getUser());
%>    
    <%
    if(request.getParameter("btnDelete") != null){
    	ssn = (Session) session.getAttribute("ssn");
    	if(ssn == null){
    		throw new Exception("Session is expired! Please go back and log in again");
    	}
    	
    	String sel = request.getParameter("activity");
    	if(sel == null || sel.isBlank()){
    		throw new Exception("You did not select an activity");
    	}
    	
    	for(BusinessActivityBean bean : myActivities){
    		if(bean.getName().equals(sel)){
    			ManageActivitiesController contr = new ManageActivitiesController();
    	    	contr.deleteBusinessActivity(bean);
    	    	%>
    	    	<jsp:forward page="MyActivities.jsp"/>
    	    	<%
    			break;
    		}
    	}
    	
    }
    %>
    
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>My Activities</title>

<link rel="stylesheet" href="css/basicStyle.css">
<link rel="stylesheet" href="css/basicStyle.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<script type="text/javascript">
function changeActivity(){
	var selectedAct = document.getElementById("activity").value;
	var newHtml = "";
	<%
	for(BusinessActivityBean act : myActivities){
		%>
		if(selectedAct == <%="\"" + act.getName() + "\""%>){
			newHtml += "<label class='title'>" + <%="\"" + act.getName() + "\""%> + "</label>";
			<% 
			String source;
			if(act.getLogo() != null){
				String encodedImg = MapMarkersUtil.encodeToBase64UTF8(act.getLogo());
				source ="\"" + "data:image/*;base64," + encodedImg + "\"";
			}else{
				source = "\"" + "resources/no_image.png" + "\"";
			}
			%>
			newHtml += ("<img style='width:100px; height:100px;' src=" + <%= source %> +">");
			
			newHtml += "<form action='MyActivities.jsp' method='post' enctype='multipart/form-data'>";
			newHtml += "<input type='file' class='loader' name='fileImg' id='fileImg' accept='image/*'>";
			newHtml += "<img id='preview' alt='Logo' src='resources/upload_img.png'>";
			newHtml += "<input type='submit' id='btnSubmit' name='btnSubmit' value='Create new activity'>"; 
			newHtml += "</form>";
		}
	<%
	}
	%>
	document.getElementById("activityContent").innerHTML = newHtml;
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
			<a href="CreateActivity.jsp"><span>Activities</span></a>
			<a href="JoinTournament.jsp"><span>Tournaments</span></a>
			<a href="Notifications.jsp"><span>Notifications</span></a>
		</div>
	
		<div id="content" class="content">
			<div class="topnav">
  				<a href="CreateActivity.jsp">Add Activity</a>
  				<a href="MyActivities.jsp">My Activities</a>
			</div>
	
		<div id="innerPage" class="innerDiv">
			<div class = "selectDiv">
				<form action="MyActivities.jsp" method="get" name="deleteForm">
					<select id="activity" name="activity" onchange="changeActivity()">
					<option disabled selected>-- Select Activity --</option>
					<%
					for(BusinessActivityBean bean : myActivities){
						%>
						<option><%= bean.getName() %></option>
						<%
					}
					%>
					</select>
					
					<input type = "submit" name="btnDelete" value="Delete">
				</form>
			</div>
			
			<div class = "activityContent" id="activityContent">
			
			</div>
		</div>
	</div>
	</div>
</body>
</html>