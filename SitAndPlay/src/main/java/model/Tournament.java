package main.java.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tournament extends Organizable{

	private CardGame cardGame;
	private ParticipationInfo participationInfo;
	private List<String> participants;
	private String winner;
	private Boolean inRequestForSponsor;
	private BusinessActivity sponsor;
	
	public Tournament(String name, Place place, CardGame cardGame, Date datetime, String organizer, Boolean sponsorRequested, ParticipationInfo partInfo) {
		this.name = name;
		this.place = place;
		this.cardGame = cardGame;
		this.datetime = datetime;
		this.organizer = organizer;
		this.setParticipationInfo(partInfo);
		this.setInRequestForSponsor(sponsorRequested);
		this.participants = new ArrayList<>();
	}

	public CardGame getCardGame() {
		return cardGame;
	}

	public List<String> getParticipants() {
		return participants;
	}

	public String getWinner() {
		return winner;
	}

	public BusinessActivity getSponsor() {
		return sponsor;
	}

	public void setCardGame(CardGame cardGame) {
		this.cardGame = cardGame;
	}

	public void setParticipants(List<String> participants) {
		this.participants = participants;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public void setSponsor(BusinessActivity sponsor) {
		this.sponsor = sponsor;
	}

	public Boolean getInRequestForSponsor() {
		return inRequestForSponsor;
	}

	public void setInRequestForSponsor(Boolean inRequestForSponsor) {
		this.inRequestForSponsor = inRequestForSponsor;
	}

	public ParticipationInfo getParticipationInfo() {
		return participationInfo;
	}

	public void setParticipationInfo(ParticipationInfo participationInfo) {
		this.participationInfo = participationInfo;
	}

	
	
	
	
	
	
}
