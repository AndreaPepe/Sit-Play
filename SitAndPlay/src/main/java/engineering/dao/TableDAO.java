package main.java.engineering.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import main.java.engineering.exceptions.DAOException;
import main.java.engineering.utils.DBConnector;
import main.java.engineering.utils.DatetimeUtil;
import main.java.engineering.utils.query.QueryTable;
import main.java.model.CardGame;
import main.java.model.Place;
import main.java.model.Table;

public class TableDAO {
	
	private TableDAO(){
		
	}
	
	public static Table retrieveTable(String tableName) throws SQLException, DAOException {
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
			var mysqlDatetime = rs.getTimestamp("datetime");
			
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
	
	
	public static void insertTable(String name, Place place, String cardGame, Date datetime, String organizer) throws SQLException, DAOException {
		Statement stmt1 = null;
		Statement stmt2 = null;
		Connection conn = null;
		
		conn = DBConnector.getInstance().getConnection();
		
		try {
			stmt1 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			var mysqlDatetime = DatetimeUtil.fromDateToMysqlTimestamp(datetime);
			QueryTable.insertTable(conn, name, place, cardGame, mysqlDatetime);
			
			
		}catch (SQLIntegrityConstraintViolationException e) {
			throw new DAOException("Table's name already in use");
		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
		}
		stmt2 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		QueryTable.insertOrganizedTable(stmt2, name, organizer);
		
	}
	
	public static void addParticipant(String tableName, String participant) throws DAOException, SQLException {
		Statement stmt = null;
		Connection conn = null;
		
		conn = DBConnector.getInstance().getConnection();
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			QueryTable.addParticipant(stmt, tableName, participant);
		}catch (SQLIntegrityConstraintViolationException e) {
			throw new DAOException("Constraint problem. Probably the table has not been found or you have already joined this table");
		}
	}
	
	
	public static List<Table> retrieveOpenTables() throws SQLException, DAOException {
		Statement stmt = null;
		Connection conn = null;
		List<Table> tables = new ArrayList<>();
		
		conn = DBConnector.getInstance().getConnection();
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = QueryTable.retrieveOpenTables(stmt);
			// if no open table is found, then return an empty list
			if(!rs.first()) {
				return tables;
			}
			rs.first();
			if(!rs.isBeforeFirst())
				rs.previous();
			while(rs.next()) {
				var name = rs.getString("name");
				var place = new Place(rs.getString("address"), rs.getDouble("lat"), rs.getDouble("lng"));
				var cardGame = CardGame.getConstant(rs.getString("cardGame"));
				var datetime = DatetimeUtil.fromMysqlTimestampToDate(rs.getTimestamp("datetime"));
				var organizer = rs.getString("organizer");
				
				var table = new Table(name, place, cardGame, datetime, organizer);
				tables.add(table);				
			}
			
			return tables;
			
		}catch (SQLIntegrityConstraintViolationException e) {
			throw new DAOException("Table's name already in use");
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
	
	
		
}
