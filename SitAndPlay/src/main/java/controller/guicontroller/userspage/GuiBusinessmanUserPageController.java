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
import main.java.engineering.exceptions.WrongUserTypeException;
import main.java.engineering.utils.Session;

public class GuiBusinessmanUserPageController extends GuiBasicInternalPageController{

	@FXML
    private ToggleButton btnProfile;

    @FXML
    private AnchorPane apnProfile;

    @FXML
    private Label lblUser;

    @FXML
    private Label lblSponsorizedTournaments;

    private static final String SPONSORIZED_TOURNAMENTS_STRING = "You have currently sponsorized %d tournaments!";
    
    
	public GuiBusinessmanUserPageController(Session ssn) {
		super(ssn);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		var tGroup = new ToggleGroup();
		btnProfile.setToggleGroup(tGroup);
		tGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal == null) {
				oldVal.setSelected(true);
			}
		});
		
		btnProfile.fire();
	}

	@FXML
    void handleProfile(ActionEvent event) {
		apnProfile.toFront();
		lblUser.setText(ssn.getUser().getUsername());
		var tournamentsSponsorized = getNumberOfSponsorizedTournaments();
		lblSponsorizedTournaments.setText(String.format(SPONSORIZED_TOURNAMENTS_STRING, tournamentsSponsorized));
    }
	
	private int getNumberOfSponsorizedTournaments() {
		var ctrl = new StatisticsController();
		try {
			return ctrl.getNumberOfSponsorizedTournaments(ssn.getUser());
		} catch (WrongUserTypeException | DAOException e) {
			AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
		}
		return 0;
	}
}
