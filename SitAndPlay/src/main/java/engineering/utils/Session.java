package main.java.engineering.utils;

import main.java.engineering.bean.login.BeanLoggedUser;

public class Session {

	private BeanLoggedUser loggedUser;
	private boolean isWeb;	
	private String homePage;
			
	public Session(boolean isWeb) {
		this.loggedUser = null;
		this.isWeb = isWeb;
	}
	
	public boolean isWeb() {
		return isWeb;
	}
	
	
	public BeanLoggedUser getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(BeanLoggedUser loggedUser) {
		this.loggedUser = loggedUser;
		if (this.loggedUser != null) {
			setHomePage();
		}
	}

	public String getHomePage() {
		return homePage;
	}
	
	// Trying a private method; TODO: check for NPE when calling getHomePage
	public void setHomePage() {
		if (isWeb) {
			switch(loggedUser.getUserType()) {
			case PLAYER: homePage = HomePage.webHomePlayer.page; break;
			case ORGANIZER: homePage = HomePage.webHomeOrganizer.page; break;
			case BUSINESSMAN: homePage = HomePage.webHomeBusinessman.page; break;
			// TODO: maybe add some exception
			default:
			}
		}else {
			switch(loggedUser.getUserType()) {
			case PLAYER: homePage = HomePage.homePlayer.page; break;
			case ORGANIZER: homePage = HomePage.homeOrganizer.page; break;
			case BUSINESSMAN: homePage = HomePage.homeBusinessman.page; break;
			// TODO: maybe add some exception
			default:
			}
		}
	}
	
	
}
