package main.java.engineering.utils.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryTable {
	
	private QueryTable() {
		
	}
	
	public static ResultSet retrieveTable(Statement stmt, String name) throws SQLException {
		String query = "SELECT * FROM Tables WHERE name = '" + name + "';";
		return stmt.executeQuery(query);
	}
	
	public static void insertTable(Statement stmt1, Statement stmt2, String name, String city, String cardGame, String date, String time, String organizer) throws SQLException {
		String query = String.format(" INSERT INTO Tables (name, city, cardGame, date, time) VALUES ('%s', '%s', '%s', '%s', '%s');", 
				name, city, cardGame, date, time);
		stmt1.executeUpdate(query);
		
		String query2 = String.format("INSERT INTO OrganizedTables (tablename, organizer) VALUES ('%s', '%s');", name, organizer);
		stmt2.executeUpdate(query2);
	}
	
	public static void addParticipant(Statement stmt, String table, String participant) throws SQLException {
		String query = String.format("INSERT INTO TablesParticipants(tablename, participant) VALUES('%s', '%s');", table, participant);
		stmt.executeUpdate(query);
	}
	
	public static void removeParticipant(Statement stmt, String table, String participant) throws SQLException {
		String query = String.format("DELETE FROM TablesParticipants WHERE tablename = '%s' AND participant = '%s';", table, participant);
		stmt.executeUpdate(query);
	}
	
	public static void deleteTable(Statement stmt, String table) throws SQLException {
		String query = String.format("DELETE FROM TablesParticipants WHERE tablename = '%s';", table); 
		stmt.executeUpdate(query);
		
		String query2 = String.format("DELETE FROM OrganizedTables WHERE tablename = '%s';", table); 
		stmt.executeUpdate(query2);
		
		String query3 = String.format("DELETE FROM Tables WHERE name = '%s';", table); 
		stmt.executeUpdate(query3);
	}
	
	public static ResultSet retrieveParticipants(Statement stmt, String table) throws SQLException {
		String query = String.format("SELECT participant FROM TablesParticipants WHERE tablename = '%s';" , table);
		return stmt.executeQuery(query);
	}
	
	public static ResultSet retrieveOrganizer(Statement stmt, String table) throws SQLException{
		String query = String.format("SELECT organizer FROM OrganizedTables WHERE tablename = '%s';" , table);
		return stmt.executeQuery(query);
	}
	
	public static ResultSet retrieveTablesByCity(Statement stmt, String city) throws SQLException{
		String query = String.format("SELECT name FROM Tables WHERE city = '%s';" , city);
		return stmt.executeQuery(query);
	}
}
