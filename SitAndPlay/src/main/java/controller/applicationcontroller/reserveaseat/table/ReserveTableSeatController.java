package main.java.controller.applicationcontroller.reserveaseat.table;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import main.java.engineering.bean.createtable.TableBean;
import main.java.engineering.dao.TableDAO;
import main.java.engineering.exceptions.DAOException;
import main.java.model.Table;

public class ReserveTableSeatController {

	public ArrayList<TableBean> retrieveTablesByCity(String city) throws DAOException{
		ArrayList<TableBean> list = new ArrayList<TableBean>();
		try {
			ArrayList<Table> tables = TableDAO.loadTablesByCity(city);
			Date datetime;
			for (Table table : tables) {
				datetime = buildDatetime(table.getDate(), table.getTime());
				if(datetime.after(getCurrentDateTime())) {
					list.add(translateTableToBean(table));
				}
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		} 
	}
	
	public ArrayList<TableBean> dummyRetrieve(String city){
		ArrayList<TableBean> list = new ArrayList<TableBean>();
		list.add(new TableBean("tablename", city, "poker", "05/08/2025", "12:15", "manu"));
		return list;
	}
	
	
	private TableBean translateTableToBean(Table table) {
		String name = table.getName();
		String city = table.getCity();
		String cardGame = table.getCardGame().toString();
		String date = table.getDate();
		String time = table.getTime();
		String organizer = table.getOrganizer();
		
		TableBean bean = new TableBean(name, city, cardGame, date, time, organizer);
		return bean;
	}
	
	private Date buildDatetime(String date, String time) {
		
		try {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			return format.parse(date + " " + time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private Date getCurrentDateTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		LocalDateTime currentDateTime = LocalDateTime.now();
		String now = formatter.format(currentDateTime);
		try {
			return format.parse(now);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
