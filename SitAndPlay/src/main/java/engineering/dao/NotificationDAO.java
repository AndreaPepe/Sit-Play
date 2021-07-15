package main.java.engineering.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import main.java.engineering.exceptions.DAOException;
import main.java.engineering.utils.DBConnector;
import main.java.engineering.utils.query.QueryNotification;
import main.java.model.Notification;

public class NotificationDAO {

	private NotificationDAO() {

	}

	public static void insertNotification(Notification notif) throws DAOException, SQLException {
		var conn = DBConnector.getInstance().getConnection();
		try {
			QueryNotification.insertNotification(conn, notif.getSender(), notif.getReceiver(), notif.getContent(),
					notif.getAlreadyPopupped());
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new DAOException("Unable to send notification");
		}
	}

	public static List<Notification> getNewNotifications(String receiver) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		List<Notification> list = new ArrayList<>();

		conn = DBConnector.getInstance().getConnection();
		try {
			stmt = conn.createStatement();
			ResultSet rs = QueryNotification.retrieveNewNotifications(stmt, receiver);
			while (rs.next()) {
				var id = rs.getInt("id");
				var sender = rs.getString("sender");
				var userToNotify = rs.getString("receiver");
				var msg = rs.getString("message");
				var shown = rs.getBoolean("shown");

				var notif = new Notification(id, sender, userToNotify, msg, shown);
				list.add(notif);
			}
			rs.close();

		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return list;
	}

	public static List<Notification> getLastNotifications(String user, int quantity) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		List<Notification> lastNotifications = new ArrayList<>();

		conn = DBConnector.getInstance().getConnection();
		try {
			stmt = conn.createStatement();
			ResultSet rs = QueryNotification.selectNotificationsWithLimit(stmt, user, quantity);
			while (rs.next()) {
				var number = rs.getInt("id");
				var sender = rs.getString("sender");
				var receiver = rs.getString("receiver");
				var message = rs.getString("message");
				var shown = rs.getBoolean("shown");

				var notification = new Notification(number, sender, receiver, message, shown);
				lastNotifications.add(notification);
			}
			rs.close();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return lastNotifications;
	}
	
	public static void setNotificationAsShown(int id) throws SQLException {
		var conn = DBConnector.getInstance().getConnection();
		QueryNotification.updateNotificationStatus(conn, id, true);
	}
}
