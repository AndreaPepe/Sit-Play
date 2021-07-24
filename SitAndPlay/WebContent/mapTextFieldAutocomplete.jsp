<%@page import="java.util.ArrayList"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="java.util.HashMap"%>
<%@page import="main.java.engineering.utils.map.MapPlace"%>
<%@page import="main.java.engineering.utils.map.MapPlaceAdapter"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="java.util.List"%>
<%@page import="main.java.engineering.exceptions.MapboxException"%>
<%@page import="main.java.controller.MapboxController"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

    <% 
    if(request.getParameter("place") != null){
    	try{
    		String input = (String) request.getParameter("place");
    		MapboxController ctrl = new MapboxController();	
    		List<JSONObject> results = ctrl.getPredictions(input);
    		
    		// use hash map to explicitly declare parameters types
    		HashMap<String,Object> hashMap = new HashMap<>();
    		for(int i = 0; i < results.size(); i++){
    			// build a map place object
    			MapPlace place = new MapPlaceAdapter(results.get(i));
    			ArrayList<Object> arr = new ArrayList<>();
    			arr.add(0, place.toString());
    			arr.add(1, place.getCoordinates().get(0));
    			arr.add(2, place.getCoordinates().get(1));
    			hashMap.put("" + i, arr);
    		}
    		
    		JSONObject ret = new JSONObject(hashMap);
    		// write the object into the response
    		response.setContentType("application/json");
    		response.getWriter().write(ret.toString());
    		
    	}catch(MapboxException e){
    		out.println(e);
    	}
    }
    
    %>