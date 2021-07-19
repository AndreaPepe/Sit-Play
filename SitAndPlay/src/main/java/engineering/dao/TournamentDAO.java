package main.java.engineering.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.DateParsingException;
import main.java.engineering.utils.DBConnector;
import main.java.engineering.utils.DatetimeUtil;
import main.java.engineering.utils.query.QueryTournament;
import main.java.model.BusinessActivity;
import main.java.model.CardGame;
import main.java.model.ParticipationInfo;
import main.java.model.Place;
import main.java.model.Tournament;

public class TournamentDAO {

	private TournamentDAO() {

	}

	public static Tournament retrieveTournament(String name) throws DAOException, SQLException, DateParsingException {
		Statement stmt = null;
		Connection conn = null;
		Tournament tournament = null;

		conn = DBConnector.getInstance().getConnection();
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = QueryTournament.retrieveTournament(stmt, name);
			if (!rs.first()) {
				// no result
				throw new DAOException("Tournament not found");
			}

			rs.first();

			var tournamentName = rs.getString("name");
			var address = rs.getString("address");
			var lat = rs.getDouble("lat");
			var lng = rs.getDouble("lng");
			var cardGame = CardGame.getConstant(rs.getString("cardGame"));
			var datetime = DatetimeUtil
					.fromMysqlTimestampToDate(rs.getTimestamp("datetime", Calendar.getInstance(Locale.getDefault())));
			if (datetime == null) {
				throw new DAOException("Parsing of datetime from database did not work");
			}
			var price = rs.getFloat("price");
			var award = rs.getFloat("award");
			var maxParticipants = rs.getInt("maxParticipants");
			var requestedSponsor = rs.getBoolean("requestedSponsor");
			var winner = rs.getString("winner");
			var sponsor = rs.getString("sponsor");

			// retrieve the organizer
			rs.close();
			stmt.close();

			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs2 = QueryTournament.retrieveOrganizer(stmt, name);
			if (!rs2.first()) {
				// exists tournament but no one has created it !!!
				throw new DAOException("Something is corrupted: tournament seems to not have an organizer!");
			}
			rs2.first();
			var organizer = rs2.getString("organizer");
			rs2.close();

			var place = new Place(address, lat, lng);
			var participationInfo = new ParticipationInfo(maxParticipants, price, award);
			tournament = new Tournament(tournamentName, place, cardGame, datetime, organizer, requestedSponsor,
					participationInfo);

			if (winner != null) {
				tournament.setWinner(winner);
			}
			if (sponsor != null) {
				var activitySponsor = BusinessActivityDAO.retrieveActivityByName(sponsor);
				if (activitySponsor != null) {
					tournament.setSponsor(activitySponsor);
				}
			}

			// retrieve list of participants
			stmt.close();
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			ResultSet rs3 = QueryTournament.retrieveParticipants(stmt, name);
			ArrayList<String> participants = new ArrayList<>();
			String participant;
			if (rs3.first()) {
				do {
					participant = rs3.getString("participant");
					participants.add(participant);
				} while (rs3.next());
			}

			rs3.close();

			tournament.setParticipants(participants);

		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		return tournament;
	}

	public static void insertTournament(Tournament tournament) throws SQLException, DAOException {
		var conn = DBConnector.getInstance().getConnection();
		Statement stmt = null;
		try {
			QueryTournament.insertTournament(conn, tournament);
			// insert the organizer
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			QueryTournament.addOrganizer(stmt, tournament.getName(), tournament.getOrganizer());
		} catch (SQLIntegrityConstraintViolationException e) {
			e.printStackTrace();
			throw new DAOException("A tournament with this name already exists");
		}

	}

	public static List<Tournament> retrieveOpenTournaments() throws SQLException, DateParsingException {
		Statement stmt = null;
		Connection conn = null;
		List<Tournament> list = new ArrayList<>();

		conn = DBConnector.getInstance().getConnection();
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = QueryTournament.retrieveOpenTournaments(stmt);
			if (!rs.first()) {
				// empty list
				return list;
			}
			if (!rs.isBeforeFirst())
				rs.previous();

			while (rs.next()) {
				var t = buildTournamentFromResultSet(rs);
				list.add(t);
			}
			rs.close();

			return list;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	public static List<String> retrieveParticipants(String tournament) throws SQLException {
		Statement stmt = null;
		Connection conn = null;
		List<String> participants = new ArrayList<>();

		conn = DBConnector.getInstance().getConnection();
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = QueryTournament.retrieveParticipants(stmt, tournament);
			if (!rs.first()) {
				return participants;
			}
			rs.beforeFirst();
			while (rs.next()) {
				participants.add(rs.getString("participant"));
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return participants;
	}

	public static void addParticipant(String tournament, String participant) throws SQLException, DAOException {
		Statement stmt = null;
		Connection conn = null;

		conn = DBConnector.getInstance().getConnection();
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			QueryTournament.addParticipant(stmt, tournament, participant);
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new DAOException(
					"Constraint problem. Probably the tournament has not been found or you have already joined this tournament");
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	public static List<Tournament> retrieveSponsorizableTournaments() throws SQLException, DAOException, DateParsingException {
		Statement stmt = null;
		Connection conn = null;
		List<Tournament> tournaments = new ArrayList<>();

		conn = DBConnector.getInstance().getConnection();
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = QueryTournament.retrieveSponsorizableTournaments(stmt);
			while (rs.next()) {
				var tournament = buildTournamentFromResultSet(rs);
				if (tournament.getSponsor() != null) {
					throw new DAOException("Some tournaments already have a sponsor");
				}
				tournaments.add(tournament);
			}
			rs.close();

			return tournaments;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	// private method to avoid duplications
	private static Tournament buildTournamentFromResultSet(ResultSet rs) throws SQLException, DateParsingException {
		var name = rs.getString("name");
		var place = new Place(rs.getString("address"), rs.getDouble("lat"), rs.getDouble("lng"));
		var cardGame = CardGame.getConstant(rs.getString("cardGame"));
		var datetime = DatetimeUtil
				.fromMysqlTimestampToDate(rs.getTimestamp("datetime", Calendar.getInstance(Locale.getDefault())));
		var pInfo = new ParticipationInfo(rs.getInt("maxParticipants"), rs.getFloat("price"), rs.getFloat("award"));
		var sponsorRequested = rs.getBoolean("requestedSponsor");
		var organizer = rs.getString("organizer");
		BusinessActivity sponsorActivity = null;
		var activity = rs.getString("sponsor");
		if (activity != null) {
			sponsorActivity = new BusinessActivity(activity, rs.getBinaryStream("logo"), rs.getString("businessman"));
		}
		var tournament = new Tournament(name, place, cardGame, datetime, organizer, sponsorRequested, pInfo);
		tournament.setSponsor(sponsorActivity);
		return tournament;
	}

	public static void updateSponsor(String tournamentName, String activityName) throws SQLException {
		var conn = DBConnector.getInstance().getConnection();
		QueryTournament.updateSponsor(conn, tournamentName, activityName);
	}

	public static List<Tournament> retrieveActiveTournamentsByParticipant(String participant) throws SQLException, DateParsingException {
		Statement stmt = null;
		List<Tournament> ret = new ArrayList<>();
		var conn = DBConnector.getInstance().getConnection();

		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = QueryTournament.getActiveTournamentsByParticipant(stmt, participant);
			while (rs.next()) {
				var t = buildTournamentFromResultSet(rs);
				ret.add(t);
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return ret;
	}

	public static void removeParticipant(String tournamentName, String participant) throws SQLException {
		Statement stmt = null;
		var conn = DBConnector.getInstance().getConnection();

		try {
			stmt = conn.createStatement();
			QueryTournament.removeParticipant(stmt, tournamentName, participant);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

}
