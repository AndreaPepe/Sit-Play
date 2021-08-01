package main.java.controller.applicationcontroller.notifications;

import java.sql.SQLException;

import main.java.engineering.dao.NotificationDAO;
import main.java.engineering.exceptions.DAOException;
import main.java.model.Notification;

/**
 * This class simulates the sending of 
 * notifications, storing them in the local database.
 * @author Andrea Pepe
 *
 */
public class NotificationSender {
	
	public void sendNotification(String sender, String receiver, String message) throws DAOException, SQLException {
		var notif = new Notification(-1, sender, receiver, message, false);
		NotificationDAO.insertNotification(notif);
	}
	
	public void setNotificationAsRead(Notification notification) throws SQLException {
		NotificationDAO.setNotificationAsShown(notification.getId());
	}
}
