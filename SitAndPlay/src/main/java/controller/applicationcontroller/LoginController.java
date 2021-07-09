package main.java.controller.applicationcontroller;

import java.sql.SQLException;
import main.java.engineering.bean.login.BeanLoggedUser;
import main.java.engineering.bean.login.BeanUser;
import main.java.engineering.dao.LoginDAO;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.WrongCredentialsExceptions;
import main.java.model.User;

public class LoginController {

	public BeanLoggedUser login(BeanUser beanUser) throws DAOException, WrongCredentialsExceptions {
				
		var beanLoggedUser = new BeanLoggedUser();
		
		User user;
		try {
			user = LoginDAO.login(beanUser.getUsername(), beanUser.getPassword());
			beanLoggedUser.setUsername(user.getUsername());
			beanLoggedUser.setUserType(user.getUserType());
			
			// TODO: set stuff
			
//			switch(user.getUserType()) {
//			case PLAYER:
//			// doing stuff for each type of user
//			// remember to do casting on variable user
//			case ORGANIZER:
//				
//			case BUSINESSMAN:
//			}
			
			return beanLoggedUser;
		} catch (SQLException e) {
			throw new DAOException(e.getMessage());		
		}
		
		
	}
}