package main.java.engineering.utils.map;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MapPlaceAdapter implements MapPlace {

	private String placeName;
	private String type;
	private String country;
	private String city="???";
	private double lng;
	private double lat;
	private String postCode;
	private String category;
	private String icon;
	
	
	public MapPlaceAdapter(JSONObject place) {
		this.buildFromJson(place);
	}

	@Override
	public List<Double> getCoordinates() {
		List<Double> coordinates = new ArrayList<>();
		coordinates.add(this.lat);
		coordinates.add(this.lng);
		return coordinates;
	}

	@Override
	public String getPlaceName() {
		return this.placeName;
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public String getCountry() {
		return this.country;
	}

	@Override
	public String getCity() {
		return this.city;
	}

	@Override
	public String getCategory() {
		return this.category;
	}

	@Override
	public String getIcon() {
		return this.icon;
	}

	@Override
	public String getPostCode() {
		return this.postCode;
	}
	
	@Override
	public String toString() {
		return this.placeName;
	}

	private void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	private void setType(String type) {
		this.type = type;
	}

	private void setCountry(String country) {
		this.country = country;
	}

	private void setCity(String city) {
		this.city = city;
	}

	private void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	private void setCategory(String category) {
		this.category = category;
	}

	private void setIcon(String icon) {
		this.icon = icon;
	}
	
	private void setCoordinates(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
	}
	
	/**
	 * This method provides conversion from a Mapbox JSON object,
	 * returned from an HTTP request, to an internal representation 
	 * of a place.
	 * 
	 * @param place
	 */
	private void buildFromJson(JSONObject place) {
		this.setPlaceName(place.get("place_name").toString());
		JSONArray types = (JSONArray) place.get("place_type");
		String jtype = (String) types.get(0);
		this.setType(jtype);
		
		JSONArray coordinates = (JSONArray)place.get("center");
		// mapbox returns coordinates in lng-lat order; switch their order in our representation
		double lat1;
		double lng0;
		if (coordinates.get(1) instanceof Long) {
			var long1 = (Long)coordinates.get(1);
			lat1 = long1.doubleValue();
		}else {
			lat1 = (Double)coordinates.get(1);
		}
		if (coordinates.get(0) instanceof Long) {
			var long0 = (Long)coordinates.get(0);
			lng0 = long0.doubleValue();
		}else {
			lng0 = (Double)coordinates.get(0);
		}
		
		this.setCoordinates(lat1, lng0);
		
		JSONArray context = (JSONArray)place.get("context");
		analyzeContext(context, jtype);
		if (jtype.compareTo("poi") == 0) {
			JSONObject jcategory = (JSONObject)place.get("properties");
			if (jcategory.get("maki") != null) {
				this.setIcon(jcategory.get("maki").toString());				
			}
			if (jcategory.get("category") != null) {
				this.setCategory(jcategory.get("category").toString());
			}
		}
	}
	
	
	private void analyzeContext(JSONArray context, String type) {
		if (context != null) {
			for (var i = 0; i < context.size(); i++) {
				JSONObject first = (JSONObject)context.get(i);
				var id = first.get("id").toString();
				
				// "poi" stays for "points of interest"
				if (type.compareTo("poi") == 0 && id.startsWith("postcode")) {
					this.setPostCode(first.get("text").toString());
				}
				if (id.startsWith("region")) {
					this.setCity(first.get("text").toString());
				}
				else if (id.startsWith("country")) {
					this.setCountry(first.get("text").toString());
				}
			}
		}
	}

}
 