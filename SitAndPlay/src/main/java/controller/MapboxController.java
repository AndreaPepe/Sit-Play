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

/**
 * The task of this class is to make HTTP requests to
 * mapbox, in order to perform the auto-completion of text fields
 * used to search a place. The responses are in the form of JSONObjects.
 * 
 * @author Andrea Pepe
 *
 */
public class MapboxController {

	private static String token = "pk.eyJ1IjoiYW5kcmVhcGVwZTMwIiwiYSI6ImNrcW52aGlzejAxNGUydXFzN2Y3N25lemwifQ._T3qFEHfLLxs4R4Nhy69rg";
	public static String getToken() {
		return token;
	}
	
	public List<JSONObject> getPlaces(String input) throws MapboxException{
		return mabpoxRequest(input, true, 10, "place,address,locality,poi");
	}
	
	private List<JSONObject> mabpoxRequest(String input, Boolean fuzzyMatch, int upperBound, String types) throws MapboxException{	
		String encodedInput;
		try {
			encodedInput = URLEncoder.encode(input,"UTF-8");
		    HttpClient client = HttpClientBuilder.create().build();
		    String urlAddress = "https://api.mapbox.com/geocoding/v5/mapbox.places/" + encodedInput + ".json" +		    		
		    		"?fuzzyMatch=" + fuzzyMatch +
		    		"&limit=" + upperBound +
		    		"&types=" + types +
		    		"&access_token=" + token;
		    
		    var getRequest = new HttpGet(urlAddress);
		    getRequest.addHeader("accept", "application/json");
		    HttpResponse response = client.execute(getRequest);
		    var jsonString = EntityUtils.toString(response.getEntity(), "UTF-8");
		    return parseJsonString(jsonString);
		    
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
	
}
