package main.java.engineering.exceptions;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AlertFactory {

	private static final String WARN_IMG = "src/main/resources/alert/warning.png";
	private static final String ERR_IMG= "src/main/resources/alert/error.png";
	
	private static AlertFactory instance = null;
	
	private AlertFactory() {
		//singleton
	}
	
	public static AlertFactory getInstance() {
		if (instance == null) {
			instance = new AlertFactory();
		}
		return instance;
	}
	
	
	public Alert createAlert(String content, AlertType type){
		Alert alert;
		URL url;
		var headerText = "";
		try {
			switch (type) {
			
			case WARNING: {
				url = new File(WARN_IMG).toURI().toURL();
				headerText = "WARNING";
				alert = new Alert(AlertType.WARNING);
				break;
				}
			
			case INFORMATION: {
				url = new File(WARN_IMG).toURI().toURL();
				headerText = "INFO";
				alert = new Alert(AlertType.INFORMATION);
				break;
				}
			default:{
				url = new File(ERR_IMG).toURI().toURL();
				headerText = "ERROR";
				alert = new Alert(AlertType.ERROR);
				break;
				}
			}
			var img = new Image(url.toString());
			var imgView = new ImageView(img);
			alert.setGraphic(imgView);
		} catch (MalformedURLException e) {
			alert = new Alert(AlertType.NONE);
		}
		
		alert.setTitle("Sit&Play");
		alert.setHeaderText(headerText);
		alert.setContentText(content);
		return alert;
	}
}
