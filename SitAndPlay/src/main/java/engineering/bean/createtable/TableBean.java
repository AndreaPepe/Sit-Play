package main.java.engineering.bean.createtable;

import java.util.ArrayList;
import java.util.List;

import main.java.engineering.exceptions.BeanCheckException;
import main.java.engineering.exceptions.DateParsingException;
import main.java.engineering.utils.DatetimeUtil;

public class TableBean {

	// A table can be created only if there is at least 1 hour of time to reserve a
	// seat
	private static final int HOURS_OF_MARGIN = 2;

	private String name;
	private String address;
	private double latitude;
	private double longitude;
	private String cardGame;
	private String date;
	private String time;
	private String organizer;
	private List<String> participants;

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

	private Boolean checkFutureDate() throws DateParsingException {
		return DatetimeUtil.isFutureDatetime(this.getDate(), this.getTime());
	}

	public Boolean checkCreateTable() throws BeanCheckException, DateParsingException {
		if (name == null || name.isBlank()) {
			throw new BeanCheckException("Table name is blank");
		}
		if (address == null || address.isBlank()) {
			throw new BeanCheckException("Place has not been selected");
		}
		if (cardGame == null) {
			throw new BeanCheckException("Card game has not been selected");
		}
		if (Boolean.FALSE.equals(checkFutureDate())) {
			throw new BeanCheckException("Impossible to create a table in the past");
		}
		if (Boolean.FALSE.equals(DatetimeUtil.isValidDateWithMargin(date, time, HOURS_OF_MARGIN))) {
			throw new BeanCheckException(String.format(
					"A table must be created within at least %d hours from the start", HOURS_OF_MARGIN));
		}
		if (organizer == null || organizer.isBlank()) {
			throw new BeanCheckException("Something didn't work. Organizer blank");
		}
		return true;
	}

}
