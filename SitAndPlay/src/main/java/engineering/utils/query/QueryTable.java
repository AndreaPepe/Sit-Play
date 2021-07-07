package main.java.engineering.utils.query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import main.java.model.Place;

public class QueryTable {
	
	private QueryTable() {
		
	}
	
	public static ResultSet retrieveTable(Statement stmt, String name) throws SQLException {
		String query = "SELECT * FROM Tables WHERE name = '" + name + "';";
		return stmt.executeQuery(query);
	}
	
	public static void insertTable(Connection conn, String name, Place place, String cardGame, String date, String time) throws SQLException {
		var query = "INSERT INTO Tables (name, address, lat, lng, cardGame, date, time) VALUES (?,?,?,?,?,?,?);";
		
		var pstmt = conn.prepareStatement(query);
		try {
		pstmt.setString(1, name);
		pstmt.setString(2, place.getAddress());
		pstmt.setDouble(3, place.getLatitude());
		pstmt.setDouble(4, place.getLongitude());
		pstmt.setString(5, cardGame);
		pstmt.setString(6, date);
		pstmt.setString(7, time);
		pstmt.executeUpdate();
		}finally {
			pstmt.close();
		}
		
	}
	
	public static void insertOrganizedTable(Statement stmt, String table, String organizer) throws SQLException {
		var query = String.format("INSERT INTO OrganizedTables (tablename, organizer) VALUES ('%s', '%s');", table, organizer);
		stmt.executeUpdate(query);
	}
	
	public static void addParticipant(Statement stmt, String table, String participant) throws SQLException {
		var query = String.format("INSERT INTO TablesParticipants(tablename, participant) VALUES('%s', '%s');", table, participant);
		stmt.executeUpdate(query);
	}
	
	public static void removeParticipant(Statement stmt, String table, String participant) throws SQLException {
		var query = String.format("DELETE FROM TablesParticipants WHERE tablename = '%s' AND participant = '%s';", table, participant);
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
		var query = String.format("SELECT participant FROM TablesParticipants WHERE tablename = '%s';" , table);
		return stmt.executeQuery(query);
	}
	
	public static ResultSet retrieveOrganizer(Statement stmt, String table) throws SQLException{
		var query = String.format("SELECT organizer FROM OrganizedTables WHERE tablename = '%s';" , table);
		return stmt.executeQuery(query);
	}
	
	public static ResultSet retrieveTablesByCity(Statement stmt, String city) throws SQLException{
		var query = String.format("SELECT name FROM Tables WHERE city = '%s';" , city);
		return stmt.executeQuery(query);
	}
}
