package main.java.engineering.bean.login;

import main.java.engineering.exceptions.EmptyDataException;
import main.java.model.UserType;

public class BeanUser {

	private String username;
	private String password;
	private UserType userType;
	
	private EmptyDataException emptyDataExceptionFactory(int errCode) {
		return new EmptyDataException(errCode);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) throws EmptyDataException {
		if (username.isBlank())
			throw emptyDataExceptionFactory(0);
		else 
			this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) throws EmptyDataException {
		if (password.isBlank())
			throw emptyDataExceptionFactory(3);
		else
			this.password = password;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}
		
}
