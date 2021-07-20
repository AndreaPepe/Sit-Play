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
		var query = String.format("SELECT organizer FROM OrganizedTournaments WHERE tournament = '%s';", tournament);
		return stmt.executeQuery(query);
	}

	public static ResultSet retrieveParticipants(Statement stmt, String tournament) throws SQLException {
		var query = String.format("SELECT participant FROM TournamentsParticipants WHERE tournament = '%s';",
				tournament);
		return stmt.executeQuery(query);
	}

	public static void insertTournament(Connection conn, Tournament tournament) throws SQLException {
		var query = "INSERT INTO Tournaments (name, address, lat, lng, cardGame, datetime, price, award, maxParticipants, requestedSponsor, winner) VALUES (?,?,?,?,?,?,?,?,?,?,?);";

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
		} finally {
			pstmt.close();
		}
	}

	public static void addParticipant(Statement stmt, String tournament, String participant) throws SQLException {
		var query = String.format("INSERT INTO TournamentsParticipants(tournament, participant) VALUES('%s', '%s');",
				tournament, participant);
		stmt.executeUpdate(query);
	}

	public static void removeParticipant(Statement stmt, String tournament, String participant) throws SQLException {
		var query = String.format("DELETE FROM TournamentsParticipants WHERE tournament = '%s' AND participant = '%s';",
				tournament, participant);
		stmt.executeUpdate(query);
	}

	public static void addOrganizer(Statement stmt, String tournament, String organizer) throws SQLException {
		var query = String.format("INSERT INTO OrganizedTournaments(tournament, organizer) VALUES ('%s', '%s');",
				tournament, organizer);
		stmt.executeUpdate(query);
	}

	// reservation of a seat is allowed only in 3 hours from the beginning of the
	// tournament
	public static ResultSet retrieveOpenTournaments(Statement stmt) throws SQLException {
		var query = "SELECT name, address, lat, lng, cardGame, datetime, price, award, maxParticipants, requestedSponsor, organizer, sponsor, businessman, logo "
				+ "FROM tournaments JOIN OrganizedTournaments ON tournament = name "
				+ "LEFT JOIN businessactivity on activity = sponsor "
				+ "WHERE datetime > ADDTIME(current_timestamp(), '3:00:00');";
		return stmt.executeQuery(query);
	}

	public static ResultSet retrieveSponsorizableTournaments(Statement stmt) throws SQLException {
		var query = "SELECT name, address, lat, lng, cardGame, datetime, price, award, maxParticipants, requestedSponsor, organizer, sponsor "
				+ "FROM tournaments JOIN OrganizedTournaments " + "ON name=tournament "
				+ "WHERE datetime > ADDTIME(current_timestamp(), '3:00:00') AND requestedSponsor = TRUE AND sponsor IS NULL;";
		return stmt.executeQuery(query);
	}

	public static void updateSponsor(Connection conn, String tournament, String activity) throws SQLException {
		var sql = "UPDATE Tournaments SET sponsor = ? WHERE name = ?;";
		var pstmt = conn.prepareStatement(sql);
		try {

			pstmt.setString(1, activity);
			pstmt.setString(2, tournament);

			pstmt.executeUpdate();
		} finally {
			pstmt.close();
		}
	}

	public static ResultSet getActiveTournamentsByParticipant(Statement stmt, String participant) throws SQLException {
		var query = String.format(
				"SELECT DISTINCT name, address, lat, lng, cardGame, datetime, price, award, maxParticipants, requestedSponsor, organizer, sponsor, businessman, logo FROM tournaments JOIN OrganizedTournaments ON name=tournament "
						+ "JOIN TournamentsParticipants P on name= P.tournament "
						+ "LEFT JOIN businessactivity on activity = sponsor "
						+ "WHERE datetime > ADDTIME(current_timestamp(), '3:00:00') " + "AND participant = '%s';",
				participant);
		return stmt.executeQuery(query);
	}

	public static ResultSet getTournamentToDeclareWinnerByOrganizer(Statement stmt, String organizer)
			throws SQLException {
		var query = String.format(
				"SELECT DISTINCT name, address, lat, lng, cardGame, datetime, price, award, maxParticipants, requestedSponsor, organizer, sponsor, businessman, logo "
						+ "FROM tournaments JOIN OrganizedTournaments ON name=tournament "
						+ "LEFT JOIN businessactivity on sponsor = activity "
						+ "WHERE datetime < current_timestamp() AND organizer = '%s' AND winner IS NULL;",
				organizer);
		return stmt.executeQuery(query);
	}

	public static void updateWinner(Connection conn, String tournamentName, String winner) throws SQLException {
		var sql = "UPDATE Tournaments SET winner = ? WHERE name = ?";
		var pstmt = conn.prepareCall(sql);
		try {
			pstmt.setString(1, winner);
			pstmt.setString(2, tournamentName);

			pstmt.executeUpdate();
		} finally {
			pstmt.close();
		}
	}

	public static ResultSet retrieveDeletableTournaments(Statement stmt, String organizer) throws SQLException {
		var query = String.format(
				"SELECT DISTINCT name, address, lat, lng, cardGame, datetime, price, award, maxParticipants, requestedSponsor, organizer, sponsor, businessman, logo "
						+ "FROM tournaments JOIN OrganizedTournaments ON name=tournament "
						+ "LEFT JOIN businessactivity on sponsor = activity "
						+ "WHERE datetime > ADDTIME(current_timestamp(), '6:00:00') " + "AND organizer = '%s';",
				organizer);
		return stmt.executeQuery(query);
	}
	
	public static void deleteTournament(Statement stmt, String tournamentName) throws SQLException {
		var query = String.format("DELETE FROM TournamentsParticipants WHERE tournament = '%s';", tournamentName);
		stmt.executeUpdate(query);

		var query2 = String.format("DELETE FROM OrganizedTournaments WHERE tournament = '%s';", tournamentName);
		stmt.executeUpdate(query2);

		var query3 = String.format("DELETE FROM Tournaments WHERE name = '%s';", tournamentName);
		stmt.executeUpdate(query3);
	}
}
