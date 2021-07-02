package main.java.engineering.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;

import main.java.engineering.exceptions.DAOException;
import main.java.engineering.utils.DBConnector;
import main.java.engineering.utils.query.QueryTable;
import main.java.model.CardGame;
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
			
			String name = rs.getString("name");
			String city = rs.getString("city");
			CardGame cardGame = CardGame.getConstant(rs.getString("cardGame"));
			String date = rs.getString("date");
			String time = rs.getString("time");
			
			rs.close();
			stmt.close();
			
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			ResultSet rs2 = QueryTable.retrieveOrganizer(stmt, tableName);
			if (!rs2.first()) {
				throw new DAOException("Something went wrong. Impossible to find the organizer");
			}
			rs2.first();
			
			String organizer = rs2.getString("organizer");
			
			table = new Table(name, city, cardGame, date, time, organizer);
			rs2.close();
			stmt.close();
			
			// load the participants and add them to the table instance			
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs3 = QueryTable.retrieveParticipants(stmt, tableName);
			ArrayList<String> participants = new ArrayList<String>();
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
	
	
	
	public static ArrayList<Table> loadTablesByCity(String city) throws SQLException, DAOException{
		Statement stmt = null;
		Connection conn = null;
		ArrayList<Table> tables = new ArrayList<Table>();
		
		conn = DBConnector.getInstance().getConnection();
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			ResultSet rs = QueryTable.retrieveTablesByCity(stmt, city);
			String tableName = new String();
			Table table;
			if (rs.first()) {
				do {
					tableName = rs.getString("name");
					table = retrieveTable(tableName);
					tables.add(table);
				} while (rs.next());
			}
						
			rs.close();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return tables;
	}
	
	
	public static void insertTable(String name, String city, String cardGame, String date, String time, String organizer) throws SQLException, DAOException {
		Statement stmt1 = null;
		Statement stmt2 = null;
		Connection conn = null;
		
		conn = DBConnector.getInstance().getConnection();
		
		try {
			stmt1 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stmt2 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			QueryTable.insertTable(stmt1, stmt2, name, city, cardGame, date, time, organizer);
			
		}catch (SQLIntegrityConstraintViolationException e) {
			throw new DAOException("Table's name already in use");
		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (stmt2 != null) {
				stmt2.close();
			}
		}
	}
	
	
		
}
