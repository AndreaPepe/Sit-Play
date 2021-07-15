package main.java.engineering.utils;

import main.java.engineering.bean.login.BeanUser;

public class Session {

	private BeanUser user;
	private boolean isWeb;
	private String homePage;

	public Session(boolean isWeb) {
		this.user = null;
		this.isWeb = isWeb;
	}

	public boolean isWeb() {
		return isWeb;
	}

	public BeanUser getUser() {
		return user;
	}

	public void setUser(BeanUser loggedUser) {
		this.user = loggedUser;
		if (this.user != null) {
			setHomePage();
		}
	}

	public String getHomePage() {
		return homePage;
	}

	public void setHomePage() {
		if (isWeb) {
			switch (user.getUserType()) {
			case PLAYER:
				homePage = HomePage.WEB_HOME_PLAYER.toString();
				break;
			case ORGANIZER:
				homePage = HomePage.WEB_HOME_ORGANIZER.toString();
				break;
			case BUSINESSMAN:
				homePage = HomePage.WEB_HOME_BUSINESSMAN.toString();
				break;
			}
		} else {
			switch (user.getUserType()) {
			case PLAYER:
				homePage = HomePage.HOME_PLAYER.toString();
				break;
			case ORGANIZER:
				homePage = HomePage.HOME_ORGANIZER.toString();
				break;
			case BUSINESSMAN:
				homePage = HomePage.HOME_BUSINESSMAN.toString();
				break;
			}
		}
	}

}
