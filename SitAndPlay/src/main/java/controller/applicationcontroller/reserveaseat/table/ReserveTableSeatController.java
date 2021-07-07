package main.java.controller.applicationcontroller.reserveaseat.table;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import main.java.engineering.bean.createtable.PlaceBean;
import main.java.engineering.bean.createtable.TableBean;
import main.java.model.Table;

public class ReserveTableSeatController {

	public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm";
	
	public void dummyMethod(Table table) {
		TableBean bean = translateTableToBean(table);
		buildDatetime(bean.getDate(), bean.getTime());
		getCurrentDateTime();
	}
	
	private TableBean translateTableToBean(Table table) {
		String name = table.getName();
		var placeBean = new PlaceBean(table.getPlace().getAddress(), table.getPlace().getLatitude(), table.getPlace().getLongitude());
		var cardGame = table.getCardGame().toString();
		String date = table.getDate();
		String time = table.getTime();
		String organizer = table.getOrganizer();
		
		return new TableBean(name, placeBean, cardGame, date, time, organizer);

	}
	
	private Date buildDatetime(String date, String time) {
		
		try {
			SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
			return format.parse(date + " " + time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private Date getCurrentDateTime() {
		var formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
		var format = new SimpleDateFormat(DATE_FORMAT);
		var currentDateTime = LocalDateTime.now();
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
