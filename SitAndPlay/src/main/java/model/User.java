package main.java.model;

public abstract class User {
	private String username;
	private String password;
	private UserType userType;

	protected User(String username, String password, UserType userType) {
		this.password = password;
		this.username = username;
		this.userType = userType;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}
	
	
	
}
