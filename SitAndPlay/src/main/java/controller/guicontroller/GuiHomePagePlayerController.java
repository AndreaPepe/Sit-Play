package main.java.controller.guicontroller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import main.java.controller.guicontroller.createtable.GuiPlayerCreateTableController;
import main.java.controller.guicontroller.tournaments.GuiJoinTournamentController;
import main.java.controller.guicontroller.userspage.GuiPlayerUserPageController;
import main.java.engineering.exceptions.AlertFactory;
import main.java.engineering.exceptions.WindowNotFoundException;
import main.java.engineering.utils.Session;
import main.java.view.PopupFxFactory;

public class GuiHomePagePlayerController extends GuiBasicController {

	public GuiHomePagePlayerController(Session ssn) {
		super(ssn);
	}

	@FXML
	private ToggleButton btnUser;

	@FXML
	private ToggleButton btnTables;

	@FXML
	private ToggleButton btnTournaments;

	@FXML
	private ToggleButton btnSettings;

	@FXML
	private Pane pnlStatus;

	@FXML
	private Label lblStatus;

	@FXML
	private Label lblMiniStatus;

	@FXML
	private Button btnClose;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setToggleSideMenu();
		btnUser.fire();
	}

	private void setToggleSideMenu() {
		var toggleGroup = new ToggleGroup();
		btnUser.setToggleGroup(toggleGroup);
		btnTables.setToggleGroup(toggleGroup);
		btnTournaments.setToggleGroup(toggleGroup);
		btnSettings.setToggleGroup(toggleGroup);

		// avoid unselected button
		toggleGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
			if (newVal == null) {
				oldVal.setSelected(true);
			}
		});
	}

	@FXML
	public void handleClicks(ActionEvent event) throws IOException {
		if (event.getSource() == btnUser) {
			lblMiniStatus.setText("User");
			lblStatus.setText("User");

			FXMLLoader loader = getInternalPageLoader(1);
			Parent root = loader.load();
			pnPage.getChildren().removeAll();
			pnPage.getChildren().setAll(root);
			
		} else if (event.getSource() == btnTables) {
			lblMiniStatus.setText("Tables");
			lblStatus.setText("Tables");

			FXMLLoader loader = getInternalPageLoader(2);
			Parent root = loader.load();
			pnPage.getChildren().removeAll();
			pnPage.getChildren().setAll(root);
			//TODO: remove this; it's only for testing
			var factory = PopupFxFactory.getInstance();
			try {
				factory.showPopup("Welcome to Sit&Play");
			} catch (WindowNotFoundException e) {
				AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.WARNING).show();
			}

		} else if (event.getSource() == btnTournaments) {
			lblMiniStatus.setText("Tournaments");
			lblStatus.setText("Tournaments");

			FXMLLoader loader = getInternalPageLoader(3);
			Parent root = loader.load();
			pnPage.getChildren().removeAll();
			pnPage.getChildren().setAll(root);

		} else if (event.getSource() == btnSettings) {
			lblMiniStatus.setText("Settings");
			lblStatus.setText("User Settings");

		}

	}

	private FXMLLoader getInternalPageLoader(int numberPage) {
		FXMLLoader loader;
		String page;
		GuiBasicInternalPageController ctrl;
		
		switch (numberPage) {
		case 2:
			page = "/main/java/view/standalone/createtable/CreateTable.fxml";
			ctrl = new GuiPlayerCreateTableController(ssn);
			break;
		case 3:
			page = "/main/java/view/standalone/tournaments/JoinTournament.fxml";
			ctrl = new GuiJoinTournamentController(ssn);
			break;
		default:
			// also page number 1
			page = "/main/java/view/standalone/userspage/PlayerUserPage.fxml";
			ctrl = new GuiPlayerUserPageController(ssn);
			break;
		}
		
		loader = new FXMLLoader(getClass().getResource(page));
		loader.setControllerFactory(c -> ctrl);
		return loader;
	}
}
