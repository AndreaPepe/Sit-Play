package main.java.engineering.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.DateParsingException;
import main.java.engineering.utils.DBConnector;
import main.java.engineering.utils.DatetimeUtil;
import main.java.engineering.utils.query.QueryTable;
import main.java.model.CardGame;
import main.java.model.Place;
import main.java.model.Table;

public class TableDAO {

	private TableDAO() {

	}

	public static Table retrieveTable(String tableName) throws SQLException, DAOException, DateParsingException {
		Statement stmt = null;
		Connection conn = null;
		Table table = null;

		conn = DBConnector.getInstance().getConnection();
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			ResultSet rs = QueryTable.retrieveTable(stmt, tableName);
			if (!rs.first()) {
				throw new DAOException("Table not found");
			}
			rs.first();

			var name = rs.getString("name");
			var address = rs.getString("address");
			var lat = rs.getDouble("lat");
			var lng = rs.getDouble("lng");
			var cardGame = CardGame.getConstant(rs.getString("cardGame"));
			var mysqlDatetime = rs.getTimestamp("datetime", Calendar.getInstance(Locale.getDefault()));
			var winner = rs.getString("winner");

			rs.close();
			stmt.close();

			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			ResultSet rs2 = QueryTable.retrieveOrganizer(stmt, tableName);
			if (!rs2.first()) {
				throw new DAOException("Something went wrong. Impossible to find the organizer");
			}
			rs2.first();

			var organizer = rs2.getString("organizer");

			var place = new Place(address, lat, lng);
			var datetime = DatetimeUtil.fromMysqlTimestampToDate(mysqlDatetime);
			if (datetime == null) {
				throw new DAOException("Parsing of datetime from database did not work");
			}
			table = new Table(name, place, cardGame, datetime, organizer);
			table.setWinner(winner);
			rs2.close();
			stmt.close();

			// load the participants and add them to the table instance
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs3 = QueryTable.retrieveParticipants(stmt, tableName);
			ArrayList<String> participants = new ArrayList<>();
			String participant;
			if (rs3.first()) {
				do {
					participant = rs3.getString("participant");
					participants.add(participant);
				} while (rs3.next());
			}

			rs3.close();

			table.setParticipants(participants);

		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return table;
	}

	public static void insertTable(String name, Place place, String cardGame, Date datetime, String organizer)
			throws SQLException, DAOException {
		Statement stmt1 = null;
		Connection conn = null;

		conn = DBConnector.getInstance().getConnection();

		try {
			stmt1 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			var mysqlDatetime = DatetimeUtil.fromDateToMysqlTimestamp(datetime);
			QueryTable.insertTable(conn, name, place, cardGame, mysqlDatetime);

		} catch (SQLIntegrityConstraintViolationException e) {
			throw new DAOException("Table's name already in use");
		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
		}
		QueryTable.insertOrganizedTable(conn, name, organizer);

	}

	public static void addParticipant(String tableName, String participant) throws DAOException, SQLException {
		Statement stmt = null;
		Connection conn = null;

		conn = DBConnector.getInstance().getConnection();
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			QueryTable.addParticipant(stmt, tableName, participant);
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new DAOException(
					"Constraint problem. Probably you have already joined this table or the table has not been found");
		}
	}

	public static List<Table> retrieveOpenTables() throws SQLException, DateParsingException {
		Statement stmt = null;
		Connection conn = null;
		List<Table> tables = new ArrayList<>();

		conn = DBConnector.getInstance().getConnection();
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = QueryTable.retrieveOpenTables(stmt);
			// if no open table is found, then return an empty list
			if (!rs.first()) {
				return tables;
			}
			if (!rs.isBeforeFirst())
				rs.previous();
			while (rs.next()) {
				var table = buildTableFromResultSet(rs);
				tables.add(table);
			}
			rs.close();
			return tables;

		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
	
	public static List<Table> retrieveDeletableTables(String organizer) throws SQLException, DateParsingException {
		Statement stmt = null;
		Connection conn = null;
		List<Table> list = new ArrayList<>();

		conn = DBConnector.getInstance().getConnection();
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = QueryTable.retrieveDeletableTables(stmt, organizer);
			while (rs.next()) {
				var table = buildTableFromResultSet(rs);
				list.add(table);
			}
			rs.close();
			return list;

		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
	
	public static void removeParticipant(String tableName, String participant) throws SQLException {
		Statement stmt = null;
		var conn = DBConnector.getInstance().getConnection();
		
		try {
			stmt = conn.createStatement();
			QueryTable.removeParticipant(stmt, tableName, participant);
			
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
	
	public static List<Table> getOpenJoinedTablesByParticipant(String username) throws SQLException, DateParsingException{
		Statement stmt = null;
		List<Table> list = new ArrayList<>();
		
		var conn = DBConnector.getInstance().getConnection();
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = QueryTable.retrieveOpenTablesByParticipant(stmt, username);
			while (rs.next()) {
				var tab = buildTableFromResultSet(rs);
				list.add(tab);
			}
			rs.close();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return list;
	}

	
	private static Table buildTableFromResultSet(ResultSet rs) throws SQLException, DateParsingException {
		var name = rs.getString("name");
		var place = new Place(rs.getString("address"), rs.getDouble("lat"), rs.getDouble("lng"));
		var cardGame = CardGame.getConstant(rs.getString("cardGame"));
		var datetime = DatetimeUtil.fromMysqlTimestampToDate(
				rs.getTimestamp("datetime", Calendar.getInstance(Locale.getDefault())));
		var organizer = rs.getString("organizer");

		return new Table(name, place, cardGame, datetime, organizer);
	}
	
	public static List<Table> getTablesToDeclareWinner(String organizer) throws SQLException, DateParsingException{
		Statement stmt = null;
		List<Table> tables = new ArrayList<>();
		var conn = DBConnector.getInstance().getConnection();
		
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = QueryTable.getTablesWithNoWinnerByOrganizer(stmt, organizer);
			while(rs.next()) {
				var table = buildTableFromResultSet(rs);
				tables.add(table);
			}
			rs.close();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return tables;
	}
	
	public static void addWinner(String tableName, String winnerUsername) throws SQLException {
		var conn = DBConnector.getInstance().getConnection();
		QueryTable.updateWinner(conn, tableName, winnerUsername);
	}
	
	public static List<String> retrieveParticipants(String tablename) throws SQLException{
		Statement stmt = null;
		List<String> participants = new ArrayList<>();
		var conn = DBConnector.getInstance().getConnection();
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = QueryTable.retrieveParticipants(stmt, tablename);
			while(rs.next()) {
				participants.add(rs.getString("participant"));
			}
			rs.close();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return participants;
	}
	
	public static void deleteTable(String tableName) throws SQLException {
		Statement stmt = null;
		var conn = DBConnector.getInstance().getConnection();
		try {
			stmt = conn.createStatement();
			QueryTable.deleteTable(stmt, tableName);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
	
	
	public static int getNumberOfOrganizedTables(String orgUsername) throws SQLException {
		Statement stmt = null;
		var ret = 0;
		var conn = DBConnector.getInstance().getConnection();
		
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = QueryTable.selectNumberOfOrganizedTables(stmt, orgUsername);
			if (!rs.first()) {
				return ret;
			}
			ret = rs.getInt(1);
			rs.close();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return ret;
	}
	
	public static int getNumberOfJoinedTables(String player) throws SQLException {
		Statement stmt = null;
		var ret = 0;
		var conn = DBConnector.getInstance().getConnection();
		
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = QueryTable.selectNumberOfTablesJoined(stmt, player);
			if (!rs.first()) {
				return ret;
			}
			ret = rs.getInt(1);
			rs.close();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return ret;
	}
	
	public static int getNumberOfWonTables(String playerName) throws SQLException {
		Statement stmt = null;
		var ret = 0;
		var conn = DBConnector.getInstance().getConnection();
		
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = QueryTable.selectNumberOfWonTables(stmt, playerName);
			if (!rs.first()) {
				return ret;
			}
			ret = rs.getInt(1);
			rs.close();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return ret;
	}
}
