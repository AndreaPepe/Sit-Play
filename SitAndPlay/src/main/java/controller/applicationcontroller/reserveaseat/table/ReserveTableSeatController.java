package main.java.controller.applicationcontroller.reserveaseat.table;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.engineering.bean.createtable.PlaceBean;
import main.java.engineering.bean.createtable.TableBean;
import main.java.engineering.bean.login.BeanUser;
import main.java.engineering.dao.NotificationDAO;
import main.java.engineering.dao.TableDAO;
import main.java.engineering.exceptions.AlreadyExistingWinnerException;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.DateParsingException;
import main.java.engineering.exceptions.DeleteSeatException;
import main.java.engineering.exceptions.WrongUserTypeException;
import main.java.engineering.utils.CommonStrings;
import main.java.engineering.utils.DatetimeUtil;
import main.java.model.Notification;
import main.java.model.Table;
import main.java.model.UserType;

public class ReserveTableSeatController {

	public List<TableBean> retrieveOpenTables() throws DAOException, DateParsingException {
		List<TableBean> beans = new ArrayList<>();
		try {
			var listOfTables = TableDAO.retrieveOpenTables();
			for (Table table : listOfTables) {
				var bean = buildBean(table);
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

	public void joinTable(TableBean table, BeanUser user) throws DAOException, WrongUserTypeException {
		if (user.getUserType() != UserType.PLAYER) {
			throw new WrongUserTypeException(CommonStrings.getWrongUserErrMsg());
		}
		try {
			TableDAO.addParticipant(table.getName(), user.getUsername());
			var notificationContent = String.format(CommonStrings.getTableReservedNotif(), user.getUsername(),
					table.getName());
			var notif = new Notification(-1, user.getUsername(), table.getOrganizer(), notificationContent, false);
			NotificationDAO.insertNotification(notif);
		} catch (SQLException e) {
			// Change the exception type, so the graphic controller
			// has not to be aware of database concepts and error
			e.printStackTrace();
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
	}

	public void removeParticipant(TableBean table, BeanUser usr)
			throws DateParsingException, DeleteSeatException, DAOException {
		// Table's reservations are closed 1 hour before the beginning of the table game
		if (Boolean.FALSE.equals(DatetimeUtil.isValidDateWithMargin(table.getDate(), table.getTime(), 1))) {
			throw new DeleteSeatException(
					"Reservations for the table have been closed. You can not leave the table now");
		}
		try {
			TableDAO.removeParticipant(table.getName(), usr.getUsername());
			var notificationMessage = String.format(CommonStrings.getTableSeatLeaved(), usr.getUsername(),
					table.getName());
			var not = new Notification(-1, usr.getUsername(), table.getOrganizer(), notificationMessage, false);
			NotificationDAO.insertNotification(not);
		} catch (SQLException e) {
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
	}

	private TableBean buildBean(Table table) {
		var placeBean = new PlaceBean(table.getPlace().getAddress(), table.getPlace().getLatitude(),
				table.getPlace().getLongitude());
		String date = DatetimeUtil.getDate(table.getDatetime());
		String time = DatetimeUtil.getTime(table.getDatetime());
		return new TableBean(table.getName(), placeBean, table.getCardGame().toString(), date, time,
				table.getOrganizer());
	}
	
	public List<TableBean> retrieveActiveJoinedTables(BeanUser user) throws DateParsingException, DAOException{
		List<TableBean> beanList = new ArrayList<>();
		try {
			var tables = TableDAO.getOpenJoinedTablesByParticipant(user.getUsername());
			for(Table table : tables) {
				var bean = buildBean(table);
				beanList.add(bean);
			}
		} catch (SQLException e) {
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		} 
		return beanList;
	}
	
	public List<TableBean> retrieveTablesToDeclareWinnerTo(BeanUser user) throws DateParsingException, DAOException{
		List<TableBean> beans = new ArrayList<>();
		try {
			var tables = TableDAO.getTablesToDeclareWinner(user.getUsername());
			for (Table table: tables) {
				var bean = buildBean(table);
				// we also need the list of participants
				var participants = TableDAO.retrieveParticipants(table.getName());
				bean.setParticipants(participants);
				beans.add(bean);
			}
		} catch (SQLException e) {
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
		return beans;
	}
	
	public void declareWinner(TableBean tableBean, BeanUser organizer) throws DAOException, DateParsingException, AlreadyExistingWinnerException {
		try {
			var tableCheck = TableDAO.retrieveTable(tableBean.getName());
			if (tableCheck.getWinner() != null) {
				throw new AlreadyExistingWinnerException("ERROR! This table already has a winner!");
			}
			TableDAO.addWinner(tableBean.getName(), tableBean.getWinner());
			var msg = String.format(CommonStrings.getTableWinnerString(), tableBean.getName(), organizer.getUsername());
			var notif = new Notification(-1, organizer.getUsername(), tableBean.getWinner(), msg, false);
			NotificationDAO.insertNotification(notif);
		} catch (SQLException e) {
			throw new DAOException(CommonStrings.getDatabaseErrorMsg());
		}
		
	}
}
