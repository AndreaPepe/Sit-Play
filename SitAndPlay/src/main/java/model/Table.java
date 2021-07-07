package main.java.model;

import java.util.ArrayList;
import java.util.List;

public class Table {

	private String name;
	private Place place;
	private CardGame cardGame;
	private String date;
	private String time;
	private String organizer;
	private List<String> participants;
	
	public Table(String name, Place place, CardGame cardGame, String date, String time, String organizer) {
		this.name = name;
		this.place = place;
		this.cardGame = cardGame;
		this.date = date;
		this.time = time;
		this.organizer = organizer;
		this.participants = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}
	
	public CardGame getCardGame() {
		return cardGame;
	}

	public void setCardGame(CardGame cardGame) {
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
	
	public void addParticipant(String username) {
		this.participants.add(username);
	}
	
	public void removeParticipant(String username) {
		this.participants.remove(username);
	}
	
}
