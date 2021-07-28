<%@page import="main.java.engineering.bean.login.BeanUser"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="java.io.InputStream"%>
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
    	if(request.getMethod().equalsIgnoreCase("POST")){
    		ssn = (Session) session.getAttribute("ssn");
    		if(ssn == null){
    			throw new Exception("Session is expired! Please go back and log in again!");
    		}
    		
    		String activityName = null;
    	    String changePressed = null;
    		InputStream logo = null;
    		DiskFileItemFactory factory  = new DiskFileItemFactory();
    		
    		ServletFileUpload upload = new ServletFileUpload(factory);
    		
    		List<FileItem> fileItems = new ArrayList<>();
    		for(Object i : upload.parseRequest(request)){
    			fileItems.add((FileItem) i);
    		}
    		
    		Iterator<FileItem> iterator = fileItems.iterator();
    		
    		while(iterator.hasNext()){
    			FileItem item = iterator.next();
    			
    			if(item.isFormField()){
    				if(item.getFieldName().equals("modAct")){
    					activityName = item.getString();
    				}
    				if(item.getFieldName().equals("change")){
    					changePressed = item.getString();
    				}
    			
    			}else{
    				if(item.getFieldName().equals("changeFile")){
	    				InputStream is = item.getInputStream();
	    				if(is.markSupported()){
	    					is.mark(0);
	    					if(is.read() != -1){
	    						is.reset();
	    						logo = is;
	    					}else{
	    						logo = null;
	    					}
	    				}else{
	    					logo = is;
	    				}
    				}
    			}
    		}
    		
    		if(changePressed != null){
    			if(activityName == null || activityName.isBlank()){
    				throw new Exception("You did not select an activity");
    			}
    			ManageActivitiesController controller = new ManageActivitiesController();
    			BeanUser user = ssn.getUser();
    			BusinessActivityBean activity = new BusinessActivityBean(activityName, logo, user.getUsername());
    			if(activity.checkFields()){
    				controller.modifyLogo(activity);
    				%>
    				<jsp:forward page="MyActivities.jsp"/>
    				<%
    			}
    		}
    		
    	}
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
<link rel="stylesheet" href="css/myactivities.css">
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
			newHtml += ("<img class='logo' src=" + <%= source %> +">");
				
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
			<a href="BusinessmanUserPage.jsp"><span>User</span></a>
			<a href="CreateActivity.jsp"><span>Activities</span></a>
			<a href="SponsorTournament.jsp"><span>Tournaments</span></a>
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
				
				<div class = "activityContent" id="activityContent">
			
				</div>
			</div>
			
			<div class="modifyLogo">
				<form action="MyActivities.jsp" method="post" name="changeLogo" enctype="multipart/form-data">
					<select id="modAct" name="modAct">
						<option selected disabled>-- Select Activity --</option>
						<%
						for(BusinessActivityBean bean : myActivities){
						%>
							<option><%= bean.getName() %></option>
						<%
						}
						%>
					</select>
					
					<input type="file" class="loader" id="changeFile" name="changeFile" accept="image/*">
					<img id="preview" src="resources/upload_img.png" alt="New Logo" class="preview">
					<input type="submit" name="change" id="change" value="Change Logo">
				</form>
			</div>
			
			
		</div>
	</div>
	</div>
	
	
	<script type="text/javascript">
	const input = document.querySelector('.loader');
	
	input.addEventListener('change', changePreview);
	
	function changePreview(){
		var image = document.getElementById('preview');
		const curFiles = input.files;
		if(curFiles.length != 0){
			var file = curFiles[0];
			image.src = URL.createObjectURL(file);
		}else{
			image.src = "resources/upload_img.png";
		}
	}
	
	</script>
</body>
</html>