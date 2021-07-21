package main.java.controller.applicationcontroller.notifications;

import java.sql.SQLException;

import main.java.engineering.dao.NotificationDAO;

public class NotificationPollingThread extends Thread {

	private String username;
	private Boolean isAlive;
	private Boolean isFirst;
	private static final int POLLING_INTERVAL = 5000;

	public NotificationPollingThread(String user) {
		this.username = user;
		this.isAlive = true;
		this.isFirst = true;
	}
	
	public void shutdown() {
		this.isAlive = false;
	}
	
	
	@Override
	public void run() {
		var controller = NotificationController.getInstance();
		while(Boolean.TRUE.equals(isAlive)) {
			try {
				if (Boolean.TRUE.equals(isFirst)) {
					isFirst = false;
					Thread.sleep(1500);
				}
				var newNotifications = NotificationDAO.getNewNotifications(username);
				controller.addNotifications(newNotifications);
				
				// if a lot of notifications have been retrieved, give the time to process them
				Thread.sleep(POLLING_INTERVAL * newNotifications.size() + 1000l);
			} catch (SQLException | InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}

}
