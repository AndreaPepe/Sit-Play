package main.java.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import main.java.engineering.exceptions.MapboxException;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class MapboxController {

	private static String token = "pk.eyJ1IjoiYW5kcmVhcGVwZTMwIiwiYSI6ImNrcW52aGlzejAxNGUydXFzN2Y3N25lemwifQ._T3qFEHfLLxs4R4Nhy69rg";
	public static String getToken() {
		return token;
	}
	
	public List<JSONObject> getPredictions(String input) throws MapboxException{
		return mabpoxQuery(input, true, 10, "place,address,locality,poi");
	}
	
	private List<JSONObject> mabpoxQuery(String input, Boolean fuzzyMatch, int upperBound, String types) throws MapboxException{
		if (upperBound>10) {
			upperBound = 10;
		}
		if (upperBound < 1) {
			upperBound = 1;
		}
		
		String encodedInput;
		try {
			encodedInput = URLEncoder.encode(input,"UTF-8");
		    HttpClient client = HttpClientBuilder.create().build();
		    String urlFormat = "https://api.mapbox.com/geocoding/v5/mapbox.places/" + encodedInput + ".json" +		    		
		    		"?fuzzyMatch=" + fuzzyMatch +
		    		"&limit=" + upperBound +
		    		"&types=" + types +
		    		"&access_token=" + token;
		    
		    var getRequest = new HttpGet(urlFormat);
		    getRequest.addHeader("accept", "application/json");
		    HttpResponse response = client.execute(getRequest);
		    var json = EntityUtils.toString(response.getEntity(), "UTF-8");
		    return parseJsonString(json);
		    
		} catch (IOException e) {
			throw new MapboxException(e.getMessage());
		} 
	}
	
	private List<JSONObject> parseJsonString(String json) throws MapboxException{
		List<JSONObject> results = new ArrayList<>();
		
		try {
			var parser = new JSONParser();
			var resultObject = parser.parse(json);
			if (resultObject instanceof JSONObject) {
				JSONObject obj = (JSONObject) resultObject;
				JSONArray array = (JSONArray) obj.get("features");
				for (var i = 0; i < array.size(); i++) {
					results.add((JSONObject)array.get(i));
				}							
			}
		} catch (ParseException e) {
			throw new MapboxException("Json error, unable to retrieve places informations");
		}
		
		return results;	
	}
	
	public JSONObject getPlaceByName (String name) throws MapboxException {
		var placeName = "place_name";
		List<String> types = new ArrayList<>();
		types.add("place");
		types.add("address");
		types.add("locality");
		types.add("poi");
		
		List<JSONObject> results = getPredictions(name);
		JSONObject obj = results.get(0);
		for(JSONObject res : results) {
			if (name.startsWith(res.get(placeName).toString()) || name.endsWith(res.get(placeName).toString())) {
				return res;
			}
		}
		
		for(String t : types) {
			results = this.mabpoxQuery(name, false, 10, t);
			for(JSONObject res : results) {
				if (name.startsWith(res.get(placeName).toString()) || name.endsWith(res.get(placeName).toString())) {
					return res;
				}
			}
		}
		return obj;
	}
}
