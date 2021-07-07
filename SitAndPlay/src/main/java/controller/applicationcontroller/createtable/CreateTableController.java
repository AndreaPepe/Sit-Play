package main.java.controller.applicationcontroller.createtable;

import java.sql.SQLException;

import main.java.engineering.bean.createtable.TableBean;
import main.java.engineering.dao.TableDAO;
import main.java.engineering.exceptions.DAOException;
import main.java.model.Place;

public class CreateTableController {

	/**
	 * This method create a new table in persistence.
	 * The method return true if it goes all fine.
	 * 
	 * @param TableBean bean
	 * @return
	 * @throws DAOException   
	 */
	public boolean createTable(TableBean bean) throws DAOException {
		String name = bean.getName();
		Place place = new Place(bean.getAddress(), bean.getLatitude(), bean.getLongitude());
		String cardGame = bean.getCardGame();
		String date = bean.getDate();
		String time = bean.getTime();
		String organizer = bean.getOrganizer();
		
		try {
			TableDAO.insertTable(name, place, cardGame, date, time, organizer);
			return true;
		} catch (SQLException e) {
			// Change the exception type, so the graphic controller
			// has not to be aware of database concepts and error 
			e.printStackTrace();
			throw new DAOException("Error occurred in interaction with database");
		}
		
	}
}
