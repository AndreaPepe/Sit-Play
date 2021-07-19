package main.java.controller.guicontroller.business;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import main.java.controller.applicationcontroller.business.SponsorizeTournamentController;
import main.java.controller.guicontroller.GuiBasicInternalPageController;
import main.java.engineering.bean.businessactivity.BusinessActivityBean;
import main.java.engineering.bean.tournaments.TournamentBean;
import main.java.engineering.exceptions.AlertFactory;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.DateParsingException;
import main.java.engineering.utils.MapMarkersUtil;
import main.java.engineering.utils.Session;

public class GuiSponsorizeTournamentController extends GuiBasicInternalPageController {

	@FXML
	private ToggleButton toggleSponsorize;

	@FXML
	private WebView webView;
	private WebEngine engine;
	private static final String MAP_RESOURCE = "src/main/java/view/standalone/tournaments/OpenTournaments.html";

	@FXML
	private Button btnSendSponsorization;

	@FXML
	private Label lblMsg;

	@FXML
	private ComboBox<String> cbSelectActivity;

	private List<TournamentBean> sponsorizableTournaments;

	public GuiSponsorizeTournamentController(Session ssn) {
		super(ssn);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		var toggleGroup = new ToggleGroup();
		toggleSponsorize.setToggleGroup(toggleGroup);
		toggleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal == null) {
				oldVal.setSelected(true);
			}
		});

		toggleSponsorize.fire();
	}

	@FXML
	public void openPage(ActionEvent event) {
		engine = webView.getEngine();
		URL myUrl = null;
		try {
			myUrl = new File(MAP_RESOURCE).toURI().toURL();

			var url = myUrl.toString();
			engine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
				if (newState == Worker.State.SUCCEEDED) {
					loadMarkers();
				}

			});
			engine.load(url);
		} catch (MalformedURLException e) {
			AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
		}

		loadActivities();
	}

	private void loadMarkers() {
		lblMsg.setVisible(false);
		var ctrl = new SponsorizeTournamentController();
		try {
			sponsorizableTournaments = ctrl.getTournamentsToSponsorize();
			if (sponsorizableTournaments.isEmpty()) {
				lblMsg.setText("No sponsorizable tournament has been found");
				lblMsg.setVisible(true);
			} else {
				for (TournamentBean bean : sponsorizableTournaments) {
					var script = MapMarkersUtil.buildLoadTournamentMarkersScript(bean);
					engine.executeScript(script);
				}
			}
		} catch (DAOException | DateParsingException e) {
			AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
		} catch (IOException e) {
			AlertFactory.getInstance().createAlert("Unable to parse sponsor's logo", AlertType.ERROR).show();
		}

	}

	private void loadActivities() {
		ObservableList<String> activities = FXCollections.observableArrayList();
		var ctrl = new SponsorizeTournamentController();
		try {
			var beans = ctrl.getBusinessmanActivities(ssn.getUser());
			for (BusinessActivityBean bean : beans) {
				activities.add(bean.getName());
			}
		} catch (DAOException e) {
			AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
		}
		cbSelectActivity.setItems(activities);
	}

	@FXML
	private void sponsorize(ActionEvent event) {
		var activity = cbSelectActivity.getValue();
		if (activity == null) {
			AlertFactory.getInstance().createAlert(
					"You have to select an activity first. Create one if you don't have any", AlertType.ERROR).show();
			return;
		}

		var tournamentName = (String) engine.executeScript("getSelectedTournament();");
		if (tournamentName == null) {
			AlertFactory.getInstance()
					.createAlert("You have to select one of the tournament on the map", AlertType.ERROR).show();
			return;
		}

		var tournamentBean = getSelectedBean(tournamentName);
		if (tournamentBean == null) {
			AlertFactory.getInstance().createAlert("Something went wrong, please reload the page", AlertType.ERROR)
					.show();
			return;
		}

		var ctrl = new SponsorizeTournamentController();
		var activityBean = new BusinessActivityBean(activity, null, ssn.getUser().getUsername());
		try {
			ctrl.confirmSponsorization(tournamentBean, activityBean);
			AlertFactory.getInstance().createAlert("Tournament succesfully sponsorized", AlertType.INFORMATION).show();
			// refresh tournaments
			toggleSponsorize.fire();
		} catch (DAOException e) {
			AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
		}
	}

	private TournamentBean getSelectedBean(String tournamentName) {
		for (TournamentBean tb : sponsorizableTournaments) {
			if (tb.getName().equals(tournamentName)) {
				return tb;
			}
		}
		return null;
	}
}
