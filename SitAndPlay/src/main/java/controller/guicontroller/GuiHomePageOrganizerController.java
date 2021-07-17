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
		FXMLLoader loader;
		if (event.getSource() == btnUser) {
			lblPathStatus.setText("User");
			lblMenuStatus.setText("User");

			loader = getPageLoader(1);
		} else if (event.getSource() == btnTables) {
			lblPathStatus.setText("Tables");
			lblMenuStatus.setText("Tables");

			loader = getPageLoader(2);
		} else if (event.getSource() == btnTournaments) {
			lblPathStatus.setText("Tournaments");
			lblMenuStatus.setText("Tournaments");

			loader = getPageLoader(3);
		} else {
			lblPathStatus.setText("Notifications");
			lblMenuStatus.setText("User's notifications");

			loader = getPageLoader(4);
		}
		Parent pRoot = loader.load();
		pnPage.getChildren().removeAll();
		pnPage.getChildren().setAll(pRoot);
	}

	private FXMLLoader getPageLoader(int pageId) {
		FXMLLoader pageLoader;
		String pag;
		GuiBasicInternalPageController controller;

		switch (pageId) {
		case 2:
			pag = "/main/java/view/standalone/createtable/CreateTable.fxml";
			controller = new GuiPlayerCreateTableController(ssn);
			break;
		case 3:
			pag = "/main/java/view/standalone/tournaments/CreateTournament.fxml";
			controller = new GuiCreateTournamentController(ssn);
			break;
		case 4:
			pag = "/main/java/view/standalone/notification/ViewNotifications.fxml";
			controller = new GuiViewNotificationController(ssn);
			break;
		default:
			// also page number 1
			pag = "/main/java/view/standalone/userspage/PlayerUserPage.fxml";
			controller = new GuiPlayerUserPageController(ssn);
			break;
		}

		pageLoader = new FXMLLoader(getClass().getResource(pag));
		pageLoader.setControllerFactory(c -> controller);
		return pageLoader;
	}

}
