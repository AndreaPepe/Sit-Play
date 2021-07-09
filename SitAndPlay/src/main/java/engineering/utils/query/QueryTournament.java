package main.java.engineering.utils.query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Locale;
import main.java.engineering.utils.DatetimeUtil;
import main.java.model.Tournament;

public class QueryTournament {
	
	private QueryTournament() {
		
	}
	
	public static ResultSet retrieveTournament(Statement stmt, String name) throws SQLException {
		String query = "SELECT * FROM Tournaments WHERE name = '" + name + "';";
		return stmt.executeQuery(query);
	}
	
	public static ResultSet retrieveOrganizer(Statement stmt, String tournament) throws SQLException {
		var query = String.format("SELECT organizer FROM OrganizedTournaments WHERE tournament = '%s';" , tournament);
		return stmt.executeQuery(query);
	}
	
	public static ResultSet retrieveParticipants(Statement stmt, String tournament) throws SQLException {
		var query = String.format("SELECT organizer FROM TournamentsParticipants WHERE tournament = '%s';" , tournament);
		return stmt.executeQuery(query);
	}
	
	public static void insertTournament(Connection conn, Tournament tournament) throws SQLException {
		var query = "INSERT INTO Tournaments (name, address, lat, lng, cardGame, datetime, price, award, limit, requestedSponsor, winner) " + 
	"VALUES (?,?,?,?,?,?,?,?,?,?,?);";
		
		var pstmt = conn.prepareStatement(query);
		try {
			var place = tournament.getPlace();
			var mysqlDatetime = DatetimeUtil.fromDateToMysqlTimestamp(tournament.getDatetime());
			var participationInfo = tournament.getParticipationInfo();
			
			pstmt.setString(1, tournament.getName());
			pstmt.setString(2, place.getAddress());
			pstmt.setDouble(3, place.getLatitude());
			pstmt.setDouble(4, place.getLongitude());
			pstmt.setString(5, tournament.getCardGame().toString());
			pstmt.setTimestamp(6, mysqlDatetime, Calendar.getInstance(Locale.getDefault()));
			pstmt.setFloat(7, participationInfo.getPrice());
			pstmt.setFloat(8, participationInfo.getAward());
			pstmt.setInt(9, participationInfo.getMaxParticipants());
			pstmt.setBoolean(10, tournament.getInRequestForSponsor());
			pstmt.setString(11, tournament.getWinner());
			
			pstmt.executeUpdate();
		}finally {
			pstmt.close();
		}	
	}
	
	public static void addParticipant(Statement stmt, String tournament, String participant) throws SQLException {
		var query = String.format("INSERT INTO TournamentsParticipants(tournament, participant) VALUES('%s', '%s');", tournament, participant);
		stmt.executeUpdate(query);
	}
	
	public static void removeParticipant(Statement stmt, String tournament, String participant) throws SQLException {
		var query = String.format("DELETE FROM TournamentsParticipants WHERE tournament = '%s' AND participant = '%s';", tournament, participant);
		stmt.executeUpdate(query);
	}
	
	public static void addOrganizer(Statement stmt, String tournament, String organizer) throws SQLException {
		var query = String.format("INSERT INTO OrganizedTournaments(tournament, organizer) VALUES ('%s', '%s');", tournament, organizer);
		stmt.executeUpdate(query);
	}
}
