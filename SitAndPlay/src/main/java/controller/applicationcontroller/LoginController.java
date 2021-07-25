package main.java.controller.applicationcontroller;

import java.sql.SQLException;

import main.java.controller.applicationcontroller.notifications.NotificationPollingThread;
import main.java.engineering.bean.login.BeanUser;
import main.java.engineering.dao.LoginDAO;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.EmptyDataException;
import main.java.engineering.exceptions.WrongCredentialsExceptions;
import main.java.model.User;

public class LoginController {

	public BeanUser login(BeanUser beanUser, boolean isWeb) throws DAOException, WrongCredentialsExceptions, EmptyDataException {
		User user;
		BeanUser newBean;
		try {
			user = LoginDAO.login(beanUser.getUsername(), beanUser.getPassword());
			newBean = new BeanUser();
			newBean.setUsername(user.getUsername());
			newBean.setUserType(user.getUserType());
			
			// launch the polling thread for notifications
			if(!isWeb) {
				var pollingThread = new NotificationPollingThread(user.getUsername());
				pollingThread.start();
			}
		} catch (SQLException e) {
			throw new DAOException(e.getMessage());		
		}
		return newBean;
		
	}
}