package main.java.engineering.bean.tournaments;

import java.util.List;

import main.java.engineering.bean.businessactivity.BusinessActivityBean;
import main.java.engineering.exceptions.BeanCheckException;
import main.java.engineering.exceptions.DateParsingException;
import main.java.engineering.utils.DatetimeUtil;

public class TournamentBean {

	/**
	 * A tournament can be created only if are given at least 3 hours for
	 * reservation of a seat. Reservations are closed 3 hours before the beginning
	 * of the tournament (so, 3 + 3 = 6)
	 */
	private static final int HOURS_OF_MARGIN_FOR_CREATION = 6;

	private String name;
	private String address;
	private double latitude;
	private double longitude;
	private String cardGame;
	private String date;
	private String time;
	private String organizer;
	private List<String> participants;
	private int maxParticipants;
	private float price;
	private float award;
	private String winner;
	private Boolean inRequestForSponsor;
	private BusinessActivityBean sponsor;

	public String getName() {
		return name;
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

	public String getCardGame() {
		return cardGame;
	}

	public String getDate() {
		return date;
	}

	public String getTime() {
		return time;
	}

	public String getOrganizer() {
		return organizer;
	}

	public List<String> getParticipants() {
		return participants;
	}

	public int getMaxParticipants() {
		return maxParticipants;
	}

	public float getPrice() {
		return price;
	}

	public float getAward() {
		return award;
	}

	public String getWinner() {
		return winner;
	}

	public Boolean getInRequestForSponsor() {
		return inRequestForSponsor;
	}

	public void setName(String name) {
		this.name = name;
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

	public void setCardGame(String cardGame) {
		this.cardGame = cardGame;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}

	public void setParticipants(List<String> participants) {
		this.participants = participants;
	}

	public void setMaxParticipants(int maxParticipants) {
		this.maxParticipants = maxParticipants;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public void setAward(float award) {
		this.award = award;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public void setInRequestForSponsor(Boolean inRequestForSponsor) {
		this.inRequestForSponsor = inRequestForSponsor;
	}

	public BusinessActivityBean getSponsor() {
		return sponsor;
	}

	public void setSponsor(BusinessActivityBean sponsor) {
		this.sponsor = sponsor;
	}

	// integrity checking methods
	private Boolean isFutureDatetime() throws DateParsingException {
		return DatetimeUtil.isFutureDatetime(this.date, this.time);
	}

	public Boolean checkRulesForInsert() throws BeanCheckException, DateParsingException {
		if (name == null || name.isBlank()) {
			throw new BeanCheckException("Tournament name is blank");
		}
		if (address == null || address.isBlank()) {
			throw new BeanCheckException("Place has not been selected");
		}
		if (cardGame == null) {
			throw new BeanCheckException("Card game has not been selected");
		}
		if (Boolean.FALSE.equals(isFutureDatetime())) {
			throw new BeanCheckException("Impossible to create a table in the past");
		}
		if (Boolean.FALSE.equals(DatetimeUtil.isValidDateWithMargin(date, time, HOURS_OF_MARGIN_FOR_CREATION))) {
			throw new BeanCheckException(String.format(
					"A tournament must be created within at least %d hours from the start", HOURS_OF_MARGIN_FOR_CREATION));
		}
		if (organizer == null || organizer.isBlank()) {
			throw new BeanCheckException("Something didn't work. Organizer is blank");
		}
		if (maxParticipants < 2) {
			throw new BeanCheckException("Number of max participants has to be at least 2");
		}
		return true;
	}
}
