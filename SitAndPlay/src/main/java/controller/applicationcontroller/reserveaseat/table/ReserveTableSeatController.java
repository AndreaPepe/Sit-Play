package main.java.controller.applicationcontroller.reserveaseat.table;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.engineering.bean.createtable.PlaceBean;
import main.java.engineering.bean.createtable.TableBean;
import main.java.engineering.dao.TableDAO;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.utils.CommonStrings;
import main.java.engineering.utils.DatetimeUtil;
import main.java.model.Table;

public class ReserveTableSeatController {

	public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm";
	
	public List<TableBean> retrieveOpenTables() throws DAOException{
		List<TableBean> beans = new ArrayList<>();
		try {
			var listOfTables = TableDAO.retrieveOpenTables();
			for (Table table : listOfTables) {
				var placeBean = new PlaceBean(table.getPlace().getAddress(), table.getPlace().getLatitude(), table.getPlace().getLongitude());
				String date = DatetimeUtil.getDate(table.getDatetime());
				String time = DatetimeUtil.getTime(table.getDatetime());
				var bean = new TableBean(table.getName(), placeBean, table.getCardGame().toString(), date, time, table.getOrganizer());
				
				beans.add(bean);
			}
		} catch (SQLException e) {
			// Change the exception type, so the graphic controller
			// has not to be aware of database concepts and error 
			e.printStackTrace();
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
		return beans;
	}
	
	public TableBean retrieveTable(String tableName) throws DAOException {
		Table table;
		try {
			table = TableDAO.retrieveTable(tableName);
		}  catch (SQLException e) {
			// Change the exception type, so the graphic controller
			// has not to be aware of database concepts and error 
			e.printStackTrace();
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
		return translateTableToBean(table);
	}
	
	
	public void joinTable(TableBean table, String participant) throws DAOException {
		try {
			TableDAO.addParticipant(table.getName(), participant);
		}catch (SQLException e) {
			// Change the exception type, so the graphic controller
			// has not to be aware of database concepts and error 
			e.printStackTrace();
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
	}
	
	private TableBean translateTableToBean(Table table) {
		String name = table.getName();
		var placeBean = new PlaceBean(table.getPlace().getAddress(), table.getPlace().getLatitude(), table.getPlace().getLongitude());
		var cardGame = table.getCardGame().toString();
		var datetime = table.getDatetime();
		String organizer = table.getOrganizer();
		
		return new TableBean(name, placeBean, cardGame, DatetimeUtil.getDate(datetime), DatetimeUtil.getTime(datetime), organizer);

	}
	
}
