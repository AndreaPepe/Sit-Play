package main.java.controller.applicationcontroller.notifications;

import java.sql.SQLException;

import main.java.engineering.dao.NotificationDAO;

public class NotificationPollingThread extends Thread {

	private String username;
	private Boolean isAlive;
	private static final int POLLING_INTERVAL = 7000;

	public NotificationPollingThread(String user) {
		this.username = user;
		this.isAlive = true;
	}
	
	public void shutdown() {
		this.isAlive = false;
	}
	
	
	@Override
	public void run() {
		var controller = NotificationController.getInstance();
		while(Boolean.TRUE.equals(isAlive)) {
			try {
				var newNotifications = NotificationDAO.getNewNotifications(username);
				controller.addNotifications(newNotifications);
				Thread.sleep(POLLING_INTERVAL);
			} catch (SQLException | InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}

}
