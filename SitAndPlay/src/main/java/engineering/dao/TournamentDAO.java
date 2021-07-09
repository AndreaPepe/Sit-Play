package main.java.engineering.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.utils.DBConnector;
import main.java.engineering.utils.DatetimeUtil;
import main.java.engineering.utils.query.QueryTournament;
import main.java.model.CardGame;
import main.java.model.ParticipationInfo;
import main.java.model.Place;
import main.java.model.Tournament;

public class TournamentDAO {

	private TournamentDAO() {
		
	}
	
	public static Tournament retrieveTournament(String name) throws DAOException, SQLException {
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
			var datetime = DatetimeUtil.fromMysqlTimestampToDate(rs.getTimestamp("datetime"));
			if (datetime == null) {
				throw new DAOException("Parsing of datetime from database did not work");
			}
			var price = rs.getFloat("price");
			var award = rs.getFloat("award");
			var limit = rs.getInt("limit");
			var requestedSponsor = rs.getBoolean("requestedSponsor");
			var winner = rs.getString("winner");
			var sponsor = rs.getString("sponsor");
			
			//retrieve the organizer
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
			var participationInfo = new ParticipationInfo(limit, price, award);
			tournament = new Tournament(tournamentName, place, cardGame, datetime, organizer, requestedSponsor, participationInfo);
			
			if (winner != null) {
				tournament.setWinner(winner);
			}
			if (sponsor != null) {
				//TODO: build businessActivity class
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
			
		}finally {
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
			throw new DAOException("A tournament with this name already exists");
		}
		
	}
}
