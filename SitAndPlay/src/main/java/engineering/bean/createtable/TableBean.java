package main.java.engineering.bean.createtable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class TableBean {

	private String name;
	private String city;
	private String cardGame;
	private String date;
	private String time;
	private String organizer;
	private ArrayList<String> participants;
	
	
	public TableBean(String name, String city, String cardGame, String date, String time, String organizer) {
		super();
		this.name = name;
		this.city = city;
		this.cardGame = cardGame;
		this.date = date;
		this.time = time;
		this.organizer = organizer;
		this.participants = null;
	}
	
	public TableBean(String name, String city, String cardGame, String date, String time, String organizer,
			ArrayList<String> participants) {
		super();
		this.name = name;
		this.city = city;
		this.cardGame = cardGame;
		this.date = date;
		this.time = time;
		this.organizer = organizer;
		this.participants = participants;
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
	public ArrayList<String> getParticipants() {
		return participants;
	}
	public void setParticipants(ArrayList<String> participants) {
		this.participants = participants;
	}
	
	public Boolean checkDateTime() {
		Date dateToCheck = buildDatetime(this.getDate(), this.getTime());
		Date currentDate = getCurrentDateTime();
		if (dateToCheck.after(currentDate)) {
			return true;
		}else {
			return false;
		}
	}
	
	
	private Date buildDatetime(String date, String time) {
		
		try {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			return format.parse(date + " " + time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private Date getCurrentDateTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		LocalDateTime currentDateTime = LocalDateTime.now();
		String now = formatter.format(currentDateTime);
		try {
			return format.parse(now);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
