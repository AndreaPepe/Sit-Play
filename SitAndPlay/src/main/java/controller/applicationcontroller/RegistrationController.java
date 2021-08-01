package main.java.controller.applicationcontroller;

import java.sql.SQLException;
import main.java.engineering.bean.login.BeanUser;
import main.java.engineering.dao.LoginDAO;
import main.java.engineering.exceptions.DAOException;

public class RegistrationController {
	
	public boolean signUp(BeanUser user) throws DAOException {
		try {
			LoginDAO.registration(user);
			return true;
		} catch (SQLException e) {
			throw new DAOException(e.getMessage());		
		}
	}
}
