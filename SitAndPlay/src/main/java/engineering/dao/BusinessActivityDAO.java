package main.java.engineering.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import main.java.engineering.exceptions.DAOException;
import main.java.engineering.utils.DBConnector;
import main.java.engineering.utils.DatetimeUtil;
import main.java.engineering.utils.query.QueryBusinessActivity;
import main.java.model.BusinessActivity;
import main.java.model.CardGame;
import main.java.model.ParticipationInfo;
import main.java.model.Place;
import main.java.model.Tournament;

public class BusinessActivityDAO {

	private BusinessActivityDAO() {
		// no instance needed
	}

	public static void insertActivity(String name, InputStream logo, String businessman)
			throws SQLException, DAOException {
		var conn = DBConnector.getInstance().getConnection();

		try {
			QueryBusinessActivity.insertActivity(conn, name, logo, businessman);
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new DAOException(String.format("An activity with the name '%s' already exists", name));
		}

	}

	public static void updateLogo(String activity, InputStream newLogo) throws SQLException {
		var conn = DBConnector.getInstance().getConnection();

		QueryBusinessActivity.updateLogo(conn, activity, newLogo);
	}

	public static List<BusinessActivity> retrieveActivitiesByOwner(String businessman) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		List<BusinessActivity> list = new ArrayList<>();

		conn = DBConnector.getInstance().getConnection();
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = QueryBusinessActivity.retrieveActivities(stmt, businessman);
			if (!rs.first()) {
				return list;
			}
			rs.beforeFirst();

			while (rs.next()) {
				var name = rs.getString("activity");
				var owner = rs.getString("businessman");
				var imageBlob = rs.getBlob("logo");
				InputStream logo;
				if (imageBlob != null) {
					logo = imageBlob.getBinaryStream(1, imageBlob.length());
				} else {
					logo = null;
				}

				var activity = new BusinessActivity(name, logo, owner);
				list.add(activity);
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		return list;
	}
	
	public static List<Tournament> retrieveOpenSponsorizedTournaments(String activityName) throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		List<Tournament> list = new ArrayList<>();
		
		conn = DBConnector.getInstance().getConnection();
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = QueryBusinessActivity.retrieveOpenSponosrizedTournaments(stmt, activityName);
			while (rs.next()) {
				var tournamentName = rs.getString("name");
				var place = new Place(rs.getString("address"), rs.getDouble("lat"), rs.getDouble("lng"));
				var game = CardGame.getConstant(rs.getString("cardGame"));
				var date = DatetimeUtil.fromMysqlTimestampToDate(rs.getTimestamp("datetime"));
				var partInformation = new ParticipationInfo(rs.getInt("maxParticipants"), rs.getFloat("price"),
						rs.getFloat("award"));
				var requested = rs.getBoolean("requestedSponsor");
				var org = rs.getString("organizer");
				BusinessActivity busActivity = null;
				var sponsorName = rs.getString("sponsor");
				if (sponsorName != null) {
					busActivity = new BusinessActivity(sponsorName, rs.getBinaryStream("logo"),
							rs.getString("businessman"));
				}
				var t = new Tournament(tournamentName, place, game, date, org, requested, partInformation);
				t.setSponsor(busActivity);
				list.add(t);	
			}
			rs.close();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return list;
	}
	
	
	public static void deleteBusinessActivity(String name) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		
		conn = DBConnector.getInstance().getConnection();
		try {
			stmt = conn.createStatement();
			QueryBusinessActivity.deleteActivity(stmt, name);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		
	}
}