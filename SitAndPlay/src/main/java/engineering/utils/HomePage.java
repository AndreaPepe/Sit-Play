package main.java.engineering.utils;

public enum HomePage {
	
	//TODO: modify these strings to real pages!!!
	webHomePlayer("HomePagePlayerView.jsp"),
	webHomeOrganizer("HomePageOrganizerView.jsp"),
	webHomeBusinessman("HomePageBusinessmanView.jsp"), 
	//TODO: set different pages; now they're the same page to test login
	homePlayer("/main/java/view/standalone/HomePage.fxml"),		
	homeOrganizer("/main/java/view/standalone/HomePage.fxml"),
	homeBusinessman("/main/java/view/standalone/HomePage.fxml");
	
	public final String page;
	private HomePage(String page) {
		this.page = page;
	}
	
}
