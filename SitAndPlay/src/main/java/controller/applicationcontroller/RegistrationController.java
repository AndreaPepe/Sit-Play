package main.java.controller.applicationcontroller;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.engineering.bean.login.BeanUser;
import main.java.engineering.dao.LoginDAO;
import main.java.engineering.exceptions.DAOException;

public class RegistrationController {
	
	public boolean signIn(BeanUser user) throws DAOException {
		try {
			LoginDAO.registration(user);
			return true;
		} catch (SQLException e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "SQLexception catched: " + e.getMessage());
			throw new DAOException(e.getMessage());		
		}
	}
}
