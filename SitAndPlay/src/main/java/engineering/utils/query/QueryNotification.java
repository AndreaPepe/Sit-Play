package main.java.engineering.utils.query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryNotification {
	private QueryNotification() {

	}

	public static void insertNotification(Connection conn, String sender, String receiver, String message,
			Boolean shown) throws SQLException {
		var sql = "INSERT INTO Notifications (sender, receiver, message, shown) VALUES(?,?,?,?);";
		var pstmt = conn.prepareStatement(sql);
		try {
			pstmt.setString(1, sender);
			pstmt.setString(2, receiver);
			pstmt.setString(3, message);
			pstmt.setBoolean(4, shown);

			pstmt.executeUpdate();
		} finally {
			pstmt.close();
		}
	}

	public static ResultSet retrieveNewNotifications(Statement stmt, String username) throws SQLException {
		var query = String.format(
				"SELECT * FROM Notifications WHERE receiver = '%s' AND shown = FALSE ORDER BY id DESC;", username);
		return stmt.executeQuery(query);
	}

	public static ResultSet selectNotificationsWithLimit(Statement stmt, String username, int limit) throws SQLException {
		var query = String.format("SELECT * FROM Notifications WHERE receiver = '%s' LIMIT %d ORDER BY id DESC;",
				username, limit);
		return stmt.executeQuery(query);
	}
	
	public static void updateNotificationStatus(Connection conn, int id, Boolean status) throws SQLException {
		var sql = "UPDATE Notifications SET shown = ? WHERE id = ?;";
		var pstmt = conn.prepareStatement(sql);
		try {
			pstmt.setBoolean(1, status);
			pstmt.setInt(2, id);
			
			pstmt.executeUpdate();
		} finally {
			pstmt.close();
		}
		
	}
}
