package main.java.controller.guicontroller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import main.java.controller.guicontroller.createtable.GuiPlayerCreateTableController;
import main.java.controller.guicontroller.notifications.GuiViewNotificationController;
import main.java.controller.guicontroller.tournaments.GuiCreateTournamentController;
import main.java.controller.guicontroller.userspage.GuiPlayerUserPageController;
import main.java.engineering.utils.Session;

public class GuiHomePageOrganizerController extends GuiBasicController {

	public GuiHomePageOrganizerController(Session ssn) {
		super(ssn);
	}

	@FXML
	private ToggleButton btnUser;

	@FXML
	private ToggleButton btnTables;

	@FXML
	private ToggleButton btnTournaments;

	@FXML
	private ToggleButton btnNotification;

	@FXML
	private Pane pnlStatus;

	@FXML
	private Label lblMenuStatus;

	@FXML
	private Label lblPathStatus;

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
		btnNotification.setToggleGroup(toggleGroup);

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
			lblPathStatus.setText("User");
			lblMenuStatus.setText("User");

			FXMLLoader loader = getPageLoader(1);
			Parent root = loader.load();
			pnPage.getChildren().removeAll();
			pnPage.getChildren().setAll(root);
		} else if (event.getSource() == btnTables) {
			lblPathStatus.setText("Tables");
			lblMenuStatus.setText("Tables");

			FXMLLoader loader = getPageLoader(2);
			Parent root = loader.load();
			pnPage.getChildren().removeAll();
			pnPage.getChildren().setAll(root);

		} else if (event.getSource() == btnTournaments) {
			lblPathStatus.setText("Tournaments");
			lblMenuStatus.setText("Tournaments");

			FXMLLoader loader = getPageLoader(3);
			Parent root = loader.load();
			pnPage.getChildren().removeAll();
			pnPage.getChildren().setAll(root);

		} else if (event.getSource() == btnNotification) {
			lblPathStatus.setText("Notifications");
			lblMenuStatus.setText("User's notifications");

			FXMLLoader loader = getPageLoader(4);
			Parent root = loader.load();
			pnPage.getChildren().removeAll();
			pnPage.getChildren().setAll(root);
		}

	}

	private FXMLLoader getPageLoader(int numberPage) {
		FXMLLoader loader;
		String page;
		GuiBasicInternalPageController ctrl;
		
		switch (numberPage) {
		case 2:
			page = "/main/java/view/standalone/createtable/CreateTable.fxml";
			ctrl = new GuiPlayerCreateTableController(ssn);
			break;
		case 3:
			page = "/main/java/view/standalone/tournaments/CreateTournament.fxml";
			ctrl = new GuiCreateTournamentController(ssn);
			break;
		case 4:
			page = "/main/java/view/standalone/notification/ViewNotifications.fxml";
			ctrl = new GuiViewNotificationController(ssn);
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
