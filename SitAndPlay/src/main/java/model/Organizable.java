package main.java.model;

import java.util.Date;

/**
 * This class is abstract because it is used to represent
 * the concept of an organizable event, but it has to be extended 
 * by a specific class representing a concrete event.
 * @author Andrea Pepe
 *
 */
public abstract class Organizable {
	
	protected String name;
	protected Place place;
	protected Date datetime;
	protected String organizer;
	
	public String getName() {
		return name;
	}
	public Place getPlace() {
		return place;
	}
	public Date getDatetime() {
		return datetime;
	}
	public String getOrganizer() {
		return organizer;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPlace(Place place) {
		this.place = place;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}

}
