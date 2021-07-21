package main.java.controller.guicontroller.userspage;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import main.java.controller.applicationcontroller.userspage.StatisticsController;
import main.java.controller.guicontroller.GuiBasicInternalPageController;
import main.java.engineering.exceptions.AlertFactory;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.utils.Session;

public class GuiOrganizerUserPageController extends GuiBasicInternalPageController {

	@FXML
	private ToggleButton toggleProfile;

	@FXML
	private AnchorPane apnOrgPage;

	@FXML
	private Label lblOrgTables;

	@FXML
	private Label lblOrgTournaments;

	@FXML
	private Label lblUName;
	
	private static final String ORG_TABLES_STRING = "Organized Tables : %d";
	private static final String ORG_TOURNAMENTS_STRING = "Organized Tournaments : %d";

	public GuiOrganizerUserPageController(Session ssn) {
		super(ssn);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		var toggleGr = new ToggleGroup();
		toggleProfile.setToggleGroup(toggleGr);
		toggleGr.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal == null) {
				oldVal.setSelected(true);
			}
		});
		
		toggleProfile.fire();
	}
	
	@FXML
	void loadUserPage(ActionEvent event) {
		lblUName.setText(ssn.getUser().getUsername());
		var ctrl = new StatisticsController();
		try {
			var stats = ctrl.getStats(ssn.getUser());
			lblOrgTables.setText(String.format(ORG_TABLES_STRING, stats.getOrgTables()));
			lblOrgTournaments.setText(String.format(ORG_TOURNAMENTS_STRING, stats.getOrgTournaments()));
		} catch (DAOException e) {
			AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
		}
	}

}
