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

	public void login(BeanUser beanUser) throws DAOException, WrongCredentialsExceptions, EmptyDataException {
		User user;
		try {
			user = LoginDAO.login(beanUser.getUsername(), beanUser.getPassword());
			beanUser.setUsername(user.getUsername());
			beanUser.setUserType(user.getUserType());
			
			// launch the polling thread for notifications
			var pollingThread = new NotificationPollingThread(user.getUsername());
			pollingThread.start();
			
			
			// TODO: set stuff
			
//			switch(user.getUserType()) {
//			case PLAYER:
//			// doing stuff for each type of user
//			// remember to do casting on variable user
//			case ORGANIZER:
//				
//			case BUSINESSMAN:
//			}
			
		} catch (SQLException e) {
			throw new DAOException(e.getMessage());		
		}
		
		
	}
}