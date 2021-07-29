package main.java.controller.applicationcontroller.notifications;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import main.java.engineering.dao.NotificationDAO;
import main.java.engineering.exceptions.WindowNotFoundException;
import main.java.model.Notification;
import main.java.view.PopupFxFactory;

public class NotificationController {

	private static final int SPAMMING_INTERVAL = 5000;
	// thread safe
	private ArrayBlockingQueue<Notification> notificationToPopup;

	private static NotificationController instance = null;

	private NotificationController() {
		notificationToPopup = new ArrayBlockingQueue<>(20, true);
		spamNotifications();
	}

	public static NotificationController getInstance() {
		if (instance == null) {
			instance = new NotificationController();
		}
		return instance;
	}

	private void spamNotifications() {
		
		// runnable task executed periodically
		Runnable periodicTask = (() -> {
			while(true) {
				try {
					var notif = notificationToPopup.take();
					PopupFxFactory.getInstance().showPopup(notif.getContent());
					NotificationDAO.setNotificationAsShown(notif.getId());				
					Thread.sleep(SPAMMING_INTERVAL);
					
				} catch (WindowNotFoundException | SQLException | InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		});

		var spammingThread = new Thread(periodicTask);
		spammingThread.start();
	}

	public synchronized void addNotifications(List<Notification> newNotifs) {
		for (Notification n : newNotifs) {
			if (!notificationToPopup.contains(n)) {
				try {
					notificationToPopup.put(n);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}

		}
	}
}
