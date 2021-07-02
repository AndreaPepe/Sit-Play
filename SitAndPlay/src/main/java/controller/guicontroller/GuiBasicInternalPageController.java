package main.java.controller.guicontroller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import main.java.engineering.utils.Session;

public abstract class GuiBasicInternalPageController implements Initializable{

	protected Session ssn;
	
	@FXML
	protected Pane pnBase;
	
	protected GuiBasicInternalPageController(Session ssn) {
		this.ssn = ssn;
	}
	
	@Override
	public abstract void initialize(URL arg0, ResourceBundle arg1);}
