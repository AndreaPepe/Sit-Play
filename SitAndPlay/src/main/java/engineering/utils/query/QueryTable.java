package main.java.engineering.utils.query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Locale;

import main.java.model.Place;

public class QueryTable {

	private QueryTable() {

	}

	private static final String SELECT_HEADER = "SELECT DISTINCT name, address, lat, lng, cardGame, datetime, organizer ";

	public static ResultSet retrieveTable(Statement stmt, String name) throws SQLException {
		String query = "SELECT * FROM Tables WHERE name = '" + name + "';";
		return stmt.executeQuery(query);
	}

	public static void insertTable(Connection conn, String name, Place place, String cardGame, Timestamp datetime)
			throws SQLException {
		var query = "INSERT INTO Tables (name, address, lat, lng, cardGame, datetime) VALUES (?,?,?,?,?,?);";

		var pstmt = conn.prepareStatement(query);
		try {
			pstmt.setString(1, name);
			pstmt.setString(2, place.getAddress());
			pstmt.setDouble(3, place.getLatitude());
			pstmt.setDouble(4, place.getLongitude());
			pstmt.setString(5, cardGame);
			pstmt.setTimestamp(6, datetime, Calendar.getInstance(Locale.getDefault()));
			pstmt.executeUpdate();
		} finally {
			pstmt.close();
		}
	}

	public static void insertOrganizedTable(Statement stmt, String table, String organizer) throws SQLException {
		var query = String.format("INSERT INTO OrganizedTables (tablename, organizer) VALUES ('%s', '%s');", table,
				organizer);
		stmt.executeUpdate(query);
	}

	public static void addParticipant(Statement stmt, String table, String participant) throws SQLException {
		var query = String.format("INSERT INTO TablesParticipants(tablename, participant) VALUES('%s', '%s');", table,
				participant);
		stmt.executeUpdate(query);
	}

	public static void removeParticipant(Statement stmt, String table, String participant) throws SQLException {
		var query = String.format("DELETE FROM TablesParticipants WHERE tablename = '%s' AND participant = '%s';",
				table, participant);
		stmt.executeUpdate(query);
	}

	public static void deleteTable(Statement stmt, String table) throws SQLException {
		var query = String.format("DELETE FROM TablesParticipants WHERE tablename = '%s';", table);
		stmt.executeUpdate(query);

		var query2 = String.format("DELETE FROM OrganizedTables WHERE tablename = '%s';", table);
		stmt.executeUpdate(query2);

		var query3 = String.format("DELETE FROM Tables WHERE name = '%s';", table);
		stmt.executeUpdate(query3);
	}

	public static ResultSet retrieveParticipants(Statement stmt, String table) throws SQLException {
		var query = String.format("SELECT participant FROM TablesParticipants WHERE tablename = '%s';", table);
		return stmt.executeQuery(query);
	}

	public static ResultSet retrieveOrganizer(Statement stmt, String table) throws SQLException {
		var query = String.format("SELECT organizer FROM OrganizedTables WHERE tablename = '%s';", table);
		return stmt.executeQuery(query);
	}

	public static ResultSet retrieveOpenTables(Statement stmt) throws SQLException {
		var query = SELECT_HEADER + "FROM tables JOIN organizedtables " + "WHERE name=tablename "
				+ "AND datetime > ADDTIME(current_timestamp(), '01:00:00');";
		return stmt.executeQuery(query);
	}

	public static ResultSet retrieveDeletableTables(Statement stmt, String organizer) throws SQLException {
		var query = SELECT_HEADER + "FROM tables JOIN organizedtables " + "WHERE name=tablename "
				+ "AND datetime > ADDTIME(current_timestamp(), '02:00:00') AND organizer = '" + organizer + "';";
		return stmt.executeQuery(query);
	}

	public static ResultSet retrieveOpenTablesByParticipant(Statement stmt, String participant) throws SQLException {
		var query = String.format(
				SELECT_HEADER + "FROM tables JOIN organizedtables ON name=tablename "
						+ "JOIN TablesParticipants P on name = P.tablename "
						+ "WHERE datetime > ADDTIME(current_timestamp(), '01:00:00') AND P.participant = '%s';",
				participant);
		return stmt.executeQuery(query);
	}

	public static ResultSet getTablesWithNoWinnerByOrganizer(Statement stmt, String organizer) throws SQLException {
		var query = String.format(SELECT_HEADER + "FROM tables JOIN organizedtables ON name=tablename "
				+ "JOIN TablesParticipants P on name = P.tablename "
				+ "WHERE datetime < current_timestamp() AND organizer = '%s' AND winner IS NULL;", organizer);
		return stmt.executeQuery(query);
	}

	public static void updateWinner(Connection conn, String tableName, String winner) throws SQLException {
		var sql = "UPDATE Tables SET winner = ? WHERE name = ?";
		var pstmt = conn.prepareCall(sql);
		try {
			pstmt.setString(1, winner);
			pstmt.setString(2, tableName);

			pstmt.executeUpdate();
		} finally {
			pstmt.close();
		}
	}
	
	public static ResultSet selectNumberOfOrganizedTables(Statement stmt, String org) throws SQLException {
		var query = String.format("SELECT COUNT(*) FROM OrganizedTables WHERE organizer = '%s';", org);
		return stmt.executeQuery(query);
	}
	
	public static ResultSet selectNumberOfTablesJoined(Statement stmt, String participant) throws SQLException {
		var query = String.format("SELECT COUNT(*) FROM TablesParticipants WHERE participant = '%s';", participant);
		return stmt.executeQuery(query);
	}
	
	public static ResultSet selectNumberOfWonTables(Statement stmt, String player) throws SQLException {
		var query = String.format("SELECT COUNT(*) FROM Tables WHERE winner = '%s';", player);
		return stmt.executeQuery(query);
	}
}
