package main.java.controller.applicationcontroller.createtable;

import java.sql.SQLException;
import main.java.engineering.bean.createtable.TableBean;
import main.java.engineering.dao.TableDAO;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.DateParsingException;
import main.java.engineering.utils.CommonStrings;
import main.java.engineering.utils.DatetimeUtil;
import main.java.model.Place;

public class CreateTableController {

	/**
	 * This method create a new table in persistence.
	 * The method return true if it goes all fine.
	 * 
	 * @param TableBean bean
	 * @return
	 * @throws DAOException   
	 * @throws DateParsingException 
	 */
	public boolean createTable(TableBean bean) throws DAOException, DateParsingException {
		String name = bean.getName();
		var place = new Place(bean.getAddress(), bean.getLatitude(), bean.getLongitude());
		String cardGame = bean.getCardGame();
		var datetime = DatetimeUtil.stringToDate(bean.getDate(), bean.getTime());
		String organizer = bean.getOrganizer();
		
		try {
			TableDAO.insertTable(name, place, cardGame, datetime, organizer);
			return true;
		} catch (SQLException e) {
			// Change the exception type, so the graphic controller
			// has not to be aware of database concepts and error 
			e.printStackTrace();
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
		
	}
}
