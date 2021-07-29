package main.java.engineering.utils.query;

import java.sql.Connection;
import java.sql.SQLException;

import main.java.engineering.bean.login.BeanUser;

public class QueryLogin {
	private static final String LOGIN_QUERY = "SELECT * FROM Users WHERE username = ? AND password = ?;";
	
	private QueryLogin() {
		
	}
	
	public static String getLoginQuery() throws SQLException {
		return LOGIN_QUERY;
	}
	
	public static void registration(Connection conn, BeanUser user) throws SQLException {
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
		var query = "INSERT INTO Users (username, password, userType) VALUES (?,?,?);";
		var pstmt = conn.prepareStatement(query);
		try {
			pstmt.setString(1, user.getUsername());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, type);
			pstmt.executeUpdate(query);
		} finally {
			pstmt.close();
		}
		
	}
}
