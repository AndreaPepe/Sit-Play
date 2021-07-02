package main.java.engineering.utils.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import main.java.engineering.bean.login.BeanUser;

public class QueryLogin {
	
	private QueryLogin() {
		
	}
	
	public static ResultSet login(Statement stmt, String username, String password) throws SQLException {
		String query = "SELECT * FROM Users WHERE username = '" + username + "' AND password = '" + password + "';";
		return stmt.executeQuery(query);
	}
	
	public static void registration(Statement stmt, BeanUser user) throws SQLException {
		String type;
		switch(user.getUserType()) {
		case PLAYER:
			type = "Player"; break;
		case ORGANIZER:
			type = "Organizer"; break;
		case BUSINESSMAN:
			type = "Businessman"; break;
		default:
			throw new SQLException("User type unsupported");
		}
		String query = String.format("INSERT INTO Users (username, password, userType) VALUES ('%s', '%s', '%s');",
				user.getUsername(), user.getPassword(), type);
		stmt.executeUpdate(query);
	}
}
