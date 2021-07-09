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
	
	public void setHomePage() {
		if (isWeb) {
			switch(loggedUser.getUserType()) {
			case PLAYER: homePage = HomePage.WEB_HOME_PLAYER.toString(); break;
			case ORGANIZER: homePage = HomePage.WEB_HOME_ORGANIZER.toString(); break;
			case BUSINESSMAN: homePage = HomePage.WEB_HOME_BUSINESSMAN.toString(); break;
			}
		}else {
			switch(loggedUser.getUserType()) {
			case PLAYER: homePage = HomePage.HOME_PLAYER.toString(); break;
			case ORGANIZER: homePage = HomePage.HOME_ORGANIZER.toString(); break;
			case BUSINESSMAN: homePage = HomePage.HOME_BUSINESSMAN.toString(); break;
			}
		}
	}
	
	
}
