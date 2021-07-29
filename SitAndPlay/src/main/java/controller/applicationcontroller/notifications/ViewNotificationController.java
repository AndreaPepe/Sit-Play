package main.java.controller.applicationcontroller.notifications;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.engineering.bean.login.BeanUser;
import main.java.engineering.bean.notifications.NotificationBean;
import main.java.engineering.dao.NotificationDAO;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.utils.CommonStrings;
import main.java.model.Notification;

public class ViewNotificationController {

	public List<NotificationBean> retrieveLastNotification(BeanUser user, int quantity) throws DAOException{
		List<NotificationBean> beans = new ArrayList<>();
		try {
			List<Notification> list = NotificationDAO.getLastNotifications(user.getUsername(), quantity);
			for (Notification n : list) {
				var bean = new NotificationBean(n);
				beans.add(bean);
			}
		} catch (SQLException e) {
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
		return beans;
	}
}
