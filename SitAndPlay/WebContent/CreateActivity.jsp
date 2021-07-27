<%@page import="main.java.engineering.utils.Session"%>
<%@page import="main.java.model.UserType"%>
<%@page import="main.java.engineering.bean.login.BeanUser"%>
<%@page import="main.java.engineering.bean.businessactivity.BusinessActivityBean"%>
<%@page import="main.java.controller.applicationcontroller.business.CreateBusinessActivityController"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="java.util.List"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%@page errorPage="ErrorPage.jsp" %>
   
    <%
    	if(request.getMethod().equalsIgnoreCase("POST")){
    		Session ssn = (Session) session.getAttribute("ssn");
    		if(ssn == null){
    			throw new Exception("Session is expired! Please go back and log in again!");
    		}
    		
    		String activityName = null;
    	    String submitPressed = null;
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
    				if(item.getFieldName().equals("name")){
    					activityName = item.getString();
    				}
    				if(item.getFieldName().equals("btnSubmit")){
    					submitPressed = item.getString();
    				}
    			
    			}else{
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
    					throw new Exception("Input stream does not support mark");
    				}
    			}
    		}
    		
    		if(submitPressed != null){
    			CreateBusinessActivityController ctrl = new CreateBusinessActivityController();
    			BeanUser user = ssn.getUser();
    			BusinessActivityBean activity = new BusinessActivityBean(activityName, logo, user.getUsername());
    			if(activity.checkFields()){
    				ctrl.createActivity(activity, user);
    			}
    		}
    		
    	}
    %>
    
    
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>Create Activity</title>

<link rel="stylesheet" href="css/basicStyle.css">
<link rel="stylesheet" href="css/createActivity.css">


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
			<div class = "sx">
				<label>Here you can add a new business activity to sponsor tournaments</label>
				<label>Give a name to the new activity and, if you want, upload a representative logo</label>
			</div>
			
			<div class = "dx">
				<form action="CreateActivity.jsp" method="post" enctype="multipart/form-data">
					<input type="text" id="name" name="name" placeholder="Activity's name">
					<input type="file" class="loader" name="fileImg" id="fileImg" accept="image/*">
					<img id="preview" alt="Logo" src="resources/upload_img.png">
					
					<input type="submit" id="btnSubmit" name="btnSubmit" value="Create new activity">
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