package main.java.engineering.bean.createtable;

public class PlaceBean {
	private String address;
	private double latitude;
	private double longitude;
	
	public PlaceBean(String address, double lat, double lng) {
		this.setAddress(address);
		this.latitude = lat;
		this.longitude = lng;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
