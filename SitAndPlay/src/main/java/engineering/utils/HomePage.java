package main.java.engineering.utils;

public enum HomePage {
	
	//TODO: modify these strings to real pages!!!
	WEB_HOME_PLAYER("HomePagePlayerView.jsp"),
	WEB_HOME_ORGANIZER("HomePageOrganizerView.jsp"),
	WEB_HOME_BUSINESSMAN("HomePageBusinessmanView.jsp"),
	
	HOME_PLAYER("/main/java/view/standalone/HomePagePlayer.fxml"),		
	HOME_ORGANIZER("/main/java/view/standalone/HomePageOrganizer.fxml"),
	HOME_BUSINESSMAN("/main/java/view/standalone/HomePageBusinessman.fxml");
	
	private final String page;
	private HomePage(String page) {
		this.page = page;
	}
	
	@Override
	public String toString() {
		return this.page;
	}
	
}
