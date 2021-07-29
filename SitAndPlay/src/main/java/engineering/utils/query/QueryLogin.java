package main.java.engineering.utils.query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import main.java.engineering.bean.login.BeanUser;

public class QueryLogin {
	
	private QueryLogin() {
		
	}
	
	public static ResultSet login(Statement stmt, String username, String password) throws SQLException {
		var query = String.format("SELECT * FROM Users WHERE username = '%s' AND password = '%s';", username, password);
		return stmt.executeQuery(query);
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
