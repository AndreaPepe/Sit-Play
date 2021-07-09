package main.java.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Table extends Organizable {

	private CardGame cardGame;
	private List<String> participants;
	
	public Table(String name, Place place, CardGame cardGame, Date datetime, String organizer) {
		this.name = name;
		this.place = place;
		this.cardGame = cardGame;
		this.datetime = datetime;
		this.organizer = organizer;
		this.participants = new ArrayList<>();
	}
	
	public CardGame getCardGame() {
		return cardGame;
	}

	public void setCardGame(CardGame cardGame) {
		this.cardGame = cardGame;
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
