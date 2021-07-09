package main.java.engineering.bean.createtable;

import java.util.ArrayList;
import java.util.List;

import main.java.engineering.utils.DatetimeUtil;

public class TableBean {

	private String name;
	private String address;
	private double latitude;
	private double longitude;
	private String cardGame;
	private String date;
	private String time;
	private String organizer;
	private ArrayList<String> participants;
	
	public TableBean(String name, PlaceBean place, String cardGame, String date, String time, String organizer) {
		super();
		this.name = name;
		this.address = place.getAddress();
		this.latitude = place.getLatitude();
		this.longitude = place.getLongitude();
		this.cardGame = cardGame;
		this.date = date;
		this.time = time;
		this.organizer = organizer;
		this.participants = null;
	}
	
	public TableBean(String name, PlaceBean place, String cardGame, String date, String time, String organizer,
			List<String> participants) {
		super();
		this.name = name;
		this.address = place.getAddress();
		this.latitude = place.getLatitude();
		this.longitude = place.getLongitude();
		this.cardGame = cardGame;
		this.date = date;
		this.time = time;
		this.organizer = organizer;
		this.participants = new ArrayList<>(participants);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}	
	public String getCardGame() {
		return cardGame;
	}

	public void setCardGame(String cardGame) {
		this.cardGame = cardGame;
	}

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getOrganizer() {
		return organizer;
	}
	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}
	public List<String> getParticipants() {
		return participants;
	}
	public void setParticipants(List<String> participants) {
		this.participants = new ArrayList<>(participants);
	}
	
	public Boolean checkDateTime() {
		return DatetimeUtil.isFutureDatetime(this.getDate(), this.getTime());
	}
	
	
	public Boolean checkCreateTable() {
		if (name == null || name.isBlank()) {
			return false;
		}
		if (address == null || address.isBlank()) {
			return false;
		}
		if (cardGame == null) {
			return false;
		}
		if (Boolean.FALSE.equals(checkDateTime())) {
			return false;
		}
		return (!(organizer==null || organizer.isBlank()));
	}
	
}
