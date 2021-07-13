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
import main.java.controller.guicontroller.tournaments.GuiCreateTournamentController;
import main.java.controller.guicontroller.tournaments.GuiJoinTournamentController;
import main.java.controller.guicontroller.userspage.GuiPlayerUserPageController;
import main.java.engineering.utils.Session;
import main.java.model.UserType;

public class GuiHomePageController extends GuiBasicController {

	public GuiHomePageController(Session ssn) {
		super(ssn);
		// TODO Auto-generated constructor stub
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
		// TODO: add several content to display
		if (event.getSource() == btnUser) {
			lblMiniStatus.setText("User");
			lblStatus.setText("User");

			FXMLLoader loader = getInternalPageLoader(1, ssn.getUser().getUserType());
			Parent root = loader.load();
			pnPage.getChildren().removeAll();
			pnPage.getChildren().setAll(root);
		} else if (event.getSource() == btnTables) {
			lblMiniStatus.setText("Tables");
			lblStatus.setText("Tables");

			FXMLLoader loader = getInternalPageLoader(2, ssn.getUser().getUserType());
			Parent root = loader.load();
			pnPage.getChildren().removeAll();
			pnPage.getChildren().setAll(root);

		} else if (event.getSource() == btnTournaments) {
			lblMiniStatus.setText("Tournaments");
			lblStatus.setText("Tournaments");

			FXMLLoader loader = getInternalPageLoader(3, ssn.getUser().getUserType());
			Parent root = loader.load();
			pnPage.getChildren().removeAll();
			pnPage.getChildren().setAll(root);

		} else if (event.getSource() == btnSettings) {
			lblMiniStatus.setText("Settings");
			lblStatus.setText("User Settings");

		}

	}

	private FXMLLoader getInternalPageLoader(int numberPage, UserType type) {
		//TODO: fix this better
		FXMLLoader loader;
		String page;
		GuiBasicInternalPageController ctrl;
		switch (type) {
		case PLAYER: {
			switch (numberPage) {
			case 1:
				page = "/main/java/view/standalone/userspage/PlayerUserPage.fxml";
				ctrl = new GuiPlayerUserPageController(ssn);
				break;
			case 2:
				page = "/main/java/view/standalone/createtable/CreateTable.fxml";
				ctrl = new GuiPlayerCreateTableController(ssn);
				break;
			case 3:
				page = "/main/java/view/standalone/tournaments/JoinTournament.fxml";
				ctrl = new GuiJoinTournamentController(ssn);
				break;
			default:
				page = "/main/java/view/standalone/userspage/PlayerUserPage.fxml";
				ctrl = new GuiPlayerUserPageController(ssn);
				break;
			}
			break;
		}
		case ORGANIZER: {
			switch (numberPage) {
			case 1:
				page = "/main/java/view/standalone/userspage/PlayerUserPage.fxml";
				ctrl = new GuiPlayerUserPageController(ssn);
				break;
			case 2:
				page = "/main/java/view/standalone/createtable/CreateTable.fxml";
				ctrl = new GuiPlayerCreateTableController(ssn);
				break;
			case 3:
				page = "/main/java/view/standalone/tournaments/CreateTournament.fxml";
				ctrl = new GuiCreateTournamentController(ssn);
				break;
			default:
				page = "/main/java/view/standalone/userspage/CreateTournament.fxml";
				ctrl = new GuiCreateTournamentController(ssn);
				break;
			}
			break;
		}

		default:
			page = "/main/java/view/standalone/userspage/PlayerUserPage.fxml";
			ctrl = new GuiPlayerUserPageController(ssn);
			break;
		}
		
		loader = new FXMLLoader(getClass().getResource(page));
		loader.setControllerFactory(c -> ctrl);
		return loader;
	}
}
