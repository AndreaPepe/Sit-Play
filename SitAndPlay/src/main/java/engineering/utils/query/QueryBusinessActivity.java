package main.java.engineering.utils.query;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryBusinessActivity {
	private QueryBusinessActivity() {
		// no instance needed
	}

	public static void insertActivity(Connection conn, String name, InputStream logo, String owner)
			throws SQLException {
		var query = "INSERT INTO BusinessActivity (activity, businessman, logo) VALUES (?,?,?);";
		var pstmt = conn.prepareStatement(query);

		try {
			pstmt.setString(1, name);
			pstmt.setString(2, owner);
			pstmt.setBlob(3, logo);

			pstmt.executeUpdate();
		} finally {
			pstmt.close();
		}
	}

	public static void updateLogo(Connection conn, String activity, InputStream newLogo) throws SQLException {
		var query = "UPDATE BusinessActivity SET logo = ? WHERE activity = ?;";
		var pstmt = conn.prepareStatement(query);
		try {
			pstmt.setBlob(1, newLogo);
			pstmt.setString(2, activity);

			pstmt.executeUpdate();
		} finally {
			pstmt.close();
		}
	}

	public static ResultSet retrieveActivities(Statement stmt, String businessman) throws SQLException {
		var query = String.format("SELECT * FROM BusinessActivity WHERE businessman = '%s';", businessman);
		return stmt.executeQuery(query);
	}

	public static ResultSet retrieveOpenSponsoredTournaments(Statement stmt, String activity) throws SQLException {
		var query = String.format(
				"SELECT * FROM Tournaments WHERE sponsor = '%s' AND datetime > ADDTIME(current_timestamp(), '3:00:00');",
				activity);
		return stmt.executeQuery(query);
	}
	
	public static void deleteActivity(Statement stmt, String name) throws SQLException {
		var query = String.format("DELETE FROM BusinessActivity WHERE activity = '%s';", name);
		stmt.executeUpdate(query);
	}
	
	public static ResultSet retrieveActivityByName(Statement stmt, String name) throws SQLException {
		var query = String.format("SELECT * FROM BusinessActivity WHERE activity = '%s'", name);
		return stmt.executeQuery(query);
	}
}
