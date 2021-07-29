package main.java.engineering.bean.login;

import main.java.engineering.exceptions.BeanCheckException;
import main.java.model.UserType;

public class BeanUser {

	private String username;
	private String password;
	private UserType userType;
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) throws BeanCheckException {
		if (username.isBlank())
			throw new BeanCheckException("Username is blank");
		else 
			this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) throws BeanCheckException {
		if (password.isBlank())
			throw new BeanCheckException("Password is blank");
		else
			this.password = password;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) throws BeanCheckException {
		if (userType == null) {
			throw new BeanCheckException("User type has not been selected");
		}
		this.userType = userType;
	}
		
}
