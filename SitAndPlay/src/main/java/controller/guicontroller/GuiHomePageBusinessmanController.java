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
import main.java.controller.guicontroller.business.GuiManageActivitiesController;
import main.java.controller.guicontroller.business.GuiSponsorizeTournamentController;
import main.java.controller.guicontroller.notifications.GuiViewNotificationController;
import main.java.controller.guicontroller.userspage.GuiPlayerUserPageController;
import main.java.engineering.utils.Session;

public class GuiHomePageBusinessmanController extends GuiBasicController{

	public GuiHomePageBusinessmanController(Session ssn) {
		super(ssn);
	}

	@FXML
	private Label lblStatus;

	@FXML
	private Label lblMiniStatus;
	
	@FXML
	private ToggleButton btnUser;

	@FXML
	private ToggleButton btnActivities;

	@FXML
	private ToggleButton btnTournaments;

	@FXML
	private ToggleButton btnNotification;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setToggleSideMenu();
		btnUser.fire();
	}

	private void setToggleSideMenu() {
		var toggleGroup = new ToggleGroup();
		btnUser.setToggleGroup(toggleGroup);
		btnActivities.setToggleGroup(toggleGroup);
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
	public void handleMenu(ActionEvent event) throws IOException {
		if (event.getSource() == btnUser) {
			lblMiniStatus.setText("User");
			lblStatus.setText("User");

			FXMLLoader loader = getPageToLoad(1);
			Parent root = loader.load();
			pnPage.getChildren().removeAll();
			pnPage.getChildren().setAll(root);
		} else if (event.getSource() == btnActivities) {
			lblMiniStatus.setText("Tables");
			lblStatus.setText("Tables");

			FXMLLoader loader = getPageToLoad(2);
			Parent root = loader.load();
			pnPage.getChildren().removeAll();
			pnPage.getChildren().setAll(root);

		} else if (event.getSource() == btnTournaments) {
			lblMiniStatus.setText("Tournaments");
			lblStatus.setText("Tournaments");

			FXMLLoader loader = getPageToLoad(3);
			Parent root = loader.load();
			pnPage.getChildren().removeAll();
			pnPage.getChildren().setAll(root);

		} else if (event.getSource() == btnNotification) {
			lblMiniStatus.setText("Notifications");
			lblStatus.setText("User's Notifications");

			FXMLLoader loader = getPageToLoad(4);
			Parent root = loader.load();
			pnPage.getChildren().removeAll();
			pnPage.getChildren().setAll(root);
		}

	}

	private FXMLLoader getPageToLoad(int numberPage) {
		FXMLLoader loader;
		String page;
		GuiBasicInternalPageController ctrl;
		
		switch (numberPage) {
		case 2:
			page = "/main/java/view/standalone/businessactivity/CreateBusinessActivity.fxml";
			ctrl = new GuiManageActivitiesController(ssn);
			break;
		case 3:
			page = "/main/java/view/standalone/tournaments/SendSponsorization.fxml";
			ctrl = new GuiSponsorizeTournamentController(ssn);
			break;
		case 4:
			page = "/main/java/view/standalone/notification/ViewNotifications.fxml";
			ctrl = new GuiViewNotificationController(ssn);
			break;
		default:
			// page number 1
			page = "/main/java/view/standalone/userspage/PlayerUserPage.fxml";
			ctrl = new GuiPlayerUserPageController(ssn);
			break;
		}
		
		loader = new FXMLLoader(getClass().getResource(page));
		loader.setControllerFactory(c -> ctrl);
		return loader;
	}
}
