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
import javafx.scene.layout.VBox;
import main.java.controller.applicationcontroller.reserveaseat.tournament.ReserveTournamentSeatController;
import main.java.controller.guicontroller.GuiBasicInternalPageController;
import main.java.engineering.bean.tournaments.TournamentBean;
import main.java.engineering.exceptions.AlertFactory;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.utils.Session;
import main.java.view.listview.BeanContainer;
import main.java.view.listview.ListElementFactory;
import main.java.view.listview.ListElementType;

public class GuiPlayerUserPageController extends GuiBasicInternalPageController {

	public GuiPlayerUserPageController(Session ssn) {
		super(ssn);
	}

	@FXML
	private AnchorPane apnBackground;

	@FXML
	private ToggleButton btnProfile;

	@FXML
	private ToggleButton btnSeats;

	@FXML
	private AnchorPane apnProfile;

	@FXML
	private Label lblUsername;

	@FXML
	private AnchorPane apnSeats;

	@FXML
	private VBox vboxTables;

	@FXML
	private VBox vboxTournaments;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		var tgGroup = new ToggleGroup();
		btnProfile.setToggleGroup(tgGroup);
		btnSeats.setToggleGroup(tgGroup);
		// avoid unselected button
		tgGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
			if (newVal == null) {
				oldVal.setSelected(true);
			}
		});
		btnProfile.fire();
	}

	@FXML
	public void profilePressed(ActionEvent event) {
		apnSeats.toBack();
		lblUsername.setText(ssn.getUser().getUsername());
		apnProfile.toFront();
	}

	@FXML
	public void displayReservedSeats(ActionEvent event) {
		apnProfile.toBack();
		apnSeats.toFront();

		loadTables();
		loadTournaments();
	}

	private void loadTournaments() {
		var ctrl = new ReserveTournamentSeatController();
		try {
			var beans = ctrl.retrieveActiveJoinedTournaments(ssn.getUser());
			vboxTournaments.getChildren().clear();
			for (TournamentBean bean : beans) {
				var beanContainer = new BeanContainer(bean, ssn.getUser());
				ListElementFactory.getInstance().createElement(ListElementType.OPEN_TOURNAMENT,
						vboxTournaments, beanContainer);
			}
		} catch (DAOException e) {
			AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
		}

	}

	private void loadTables() {
		// TODO Auto-generated method stub

	}

}
