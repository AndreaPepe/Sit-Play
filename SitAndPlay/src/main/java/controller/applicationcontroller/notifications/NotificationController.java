package main.java.controller.applicationcontroller.notifications;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import main.java.engineering.dao.NotificationDAO;
import main.java.engineering.exceptions.WindowNotFoundException;
import main.java.model.Notification;
import main.java.view.PopupFxFactory;

public class NotificationController {

	private static final int SPAMMING_INTERVAL = 7000;
	private List<Notification> notificationToPopup;
	private ScheduledExecutorService executor;

	private static NotificationController instance = null;

	private NotificationController() {
		notificationToPopup = new ArrayList<>();
		executor = Executors.newSingleThreadScheduledExecutor();
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
			System.out.println(notificationToPopup.size());
			if (!notificationToPopup.isEmpty()) {
				var notif = notificationToPopup.get(0);
				try {
					PopupFxFactory.getInstance().showPopup(notif.getContent());
					NotificationDAO.setNotificationAsShown(notif.getId());
					notificationToPopup.remove(notif);
				} catch (WindowNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			}

		});
		executor.scheduleAtFixedRate(periodicTask, 0, SPAMMING_INTERVAL, TimeUnit.MILLISECONDS);
	}

	public synchronized void addNotifications(List<Notification> newNotifs) {
		for (Notification n : newNotifs) {
			if (! notificationToPopup.contains(n)) {
				notificationToPopup.add(n);
			}
			
		}
	}
}
