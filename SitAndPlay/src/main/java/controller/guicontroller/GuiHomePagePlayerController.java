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
import javafx.scene.layout.Pane;
import main.java.controller.guicontroller.createtable.GuiPlayerCreateTableController;
import main.java.controller.guicontroller.notifications.GuiViewNotificationController;
import main.java.controller.guicontroller.tournaments.GuiJoinTournamentController;
import main.java.controller.guicontroller.userspage.GuiPlayerUserPageController;
import main.java.engineering.utils.Session;

public class GuiHomePagePlayerController extends GuiBasicController {

	public GuiHomePagePlayerController(Session ssn) {
		super(ssn);
	}

	@FXML
	private ToggleButton btnUser;

	@FXML
	private ToggleButton btnTab;

	@FXML
	private ToggleButton btnTourn;

	@FXML
	private ToggleButton btnNotif;

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
		setMenu();
		btnUser.fire();
	}

	private void setMenu() {
		var toggleGroup = new ToggleGroup();
		btnUser.setToggleGroup(toggleGroup);
		btnTab.setToggleGroup(toggleGroup);
		btnTourn.setToggleGroup(toggleGroup);
		btnNotif.setToggleGroup(toggleGroup);

		// avoid unselected button
		toggleGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
			if (newVal == null) {
				oldVal.setSelected(true);
			}
		});
	}

	@FXML
	public void handleClicks(ActionEvent event) throws IOException {
		FXMLLoader fxmlLoader;
		if (event.getSource() == btnUser) {
			lblMiniStatus.setText("User");
			lblStatus.setText("User");

			fxmlLoader = getInternalPageLoader(1);
		} else if (event.getSource() == btnTab) {
			lblMiniStatus.setText("Tables");
			lblStatus.setText("Tables");

			fxmlLoader = getInternalPageLoader(2);
		} else if (event.getSource() == btnTourn) {
			lblMiniStatus.setText("Tournaments");
			lblStatus.setText("Tournaments");

			fxmlLoader = getInternalPageLoader(3);
		} else {
			lblMiniStatus.setText("Notifications");
			lblStatus.setText("User's Notifications");

			fxmlLoader = getInternalPageLoader(4);
		}

		var parentRoot = fxmlLoader.load();
		pnPage.getChildren().removeAll();
		pnPage.getChildren().setAll((Parent)parentRoot);

	}

	private FXMLLoader getInternalPageLoader(int numberPage) {
		FXMLLoader fxmlLoader;
		String internalPage;
		GuiBasicInternalPageController guiCtrl;

		switch (numberPage) {
		case 2:
			internalPage = "/main/java/view/standalone/createtable/CreateTable.fxml";
			guiCtrl = new GuiPlayerCreateTableController(ssn);
			break;
		case 3:
			internalPage = "/main/java/view/standalone/tournaments/JoinTournament.fxml";
			guiCtrl = new GuiJoinTournamentController(ssn);
			break;
		case 4:
			internalPage = "/main/java/view/standalone/notification/ViewNotifications.fxml";
			guiCtrl = new GuiViewNotificationController(ssn);
			break;
		default:
			// also page number 1
			internalPage = "/main/java/view/standalone/userspage/PlayerUserPage.fxml";
			guiCtrl = new GuiPlayerUserPageController(ssn);
			break;
		}

		fxmlLoader = new FXMLLoader(getClass().getResource(internalPage));
		fxmlLoader.setControllerFactory(c -> guiCtrl);
		return fxmlLoader;
	}
}
