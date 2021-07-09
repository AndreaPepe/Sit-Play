package main.java.engineering.bean.createtable;

public class PlaceBean {
	private String placeName;
	private double lat;
	private double lng;
	
	public PlaceBean(String address, double lat, double lng) {
		this.placeName = address;
		this.lat = lat;
		this.lng = lng;
	}

	public double getLatitude() {
		return lat;
	}

	public double getLongitude() {
		return lng;
	}

	public void setLatitude(double latitude) {
		this.lat = latitude;
	}

	public void setLongitude(double longitude) {
		this.lng = longitude;
	}

	public String getAddress() {
		return placeName;
	}

	public void setAddress(String address) {
		this.placeName = address;
	}
}
