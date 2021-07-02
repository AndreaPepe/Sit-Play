package main.java.controller.applicationcontroller.createtable;

import java.sql.SQLException;

import main.java.engineering.bean.createtable.TableBean;
import main.java.engineering.dao.TableDAO;
import main.java.engineering.exceptions.DAOException;

public class CreateTableController {

	/**
	 * This method create a new table in persistence.
	 * The method return true if it goes all fine.
	 * 
	 * @param TableBean bean
	 * @return
	 * @throws DAOException 
	 * @throws  
	 */
	public boolean createTable(TableBean bean) throws DAOException {
		String name = bean.getName();
		String city = bean.getCity();
		String cardGame = bean.getCardGame();
		String date = bean.getDate();
		String time = bean.getTime();
		String organizer = bean.getOrganizer();
		
		try {
			TableDAO.insertTable(name, city, cardGame, date, time, organizer);
			return true;
		} catch (SQLException e) {
			// Change the exception type, so the graphic controller
			// has not to be aware of database concepts and error main.java
			e.printStackTrace();
			throw new DAOException("Error occurred in interaction with database");
		}
		
	}
}
