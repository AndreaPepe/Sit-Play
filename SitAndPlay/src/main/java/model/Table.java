package main.java.model;

import java.util.ArrayList;

public class Table {

	private String name;
	private String city;
	private CardGame cardGame;
	private String date;
	private String time;
	private String organizer;
	private ArrayList<String> participants;
	
	public Table(String name, String city, CardGame cardGame, String date, String time, String organizer) {
		this.name = name;
		this.city = city;
		this.cardGame = cardGame;
		this.date = date;
		this.time = time;
		this.organizer = organizer;
		this.participants = new ArrayList<String>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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

	public ArrayList<String> getParticipants() {
		return participants;
	}

	public void setParticipants(ArrayList<String> participants) {
		this.participants = participants;
	}
	
	public void addParticipant(String username) {
		this.participants.add(username);
	}
	
	public void removeParticipant(String username) {
		this.participants.remove(username);
	}
	
}
