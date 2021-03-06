package main.java.controller.guicontroller.tournaments;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import main.java.controller.applicationcontroller.reserveaseat.tournament.ReserveTournamentSeatController;
import main.java.controller.guicontroller.GuiBasicInternalPageController;
import main.java.engineering.bean.tournaments.TournamentBean;
import main.java.engineering.exceptions.AlertFactory;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.DateParsingException;
import main.java.engineering.exceptions.MaxParticipantsException;
import main.java.engineering.exceptions.WrongUserTypeException;
import main.java.engineering.utils.MapMarkersUtil;
import main.java.engineering.utils.Session;

public class GuiJoinTournamentController extends GuiBasicInternalPageController{

	public GuiJoinTournamentController(Session ssn) {
		super(ssn);
	}


	@FXML
    private ToggleButton btnJoinTournament;

    @FXML
    private WebView webViewTournaments;
    private WebEngine engine;

    @FXML
    private Button btnReserveSeat;

    @FXML
    private Label lblMsg;
    
    private static final String MAP_WITH_MARKERS = "src/main/java/view/standalone/tournaments/OpenTournaments.html";
    private List<TournamentBean> activeTournaments;
    
    @FXML
    public void loadMap(ActionEvent event) {
		// create the web engine for webViewAllTables in second pane
    	engine = webViewTournaments.getEngine();
    	URL myUrl=null;
		try {
			myUrl = new File(MAP_WITH_MARKERS).toURI().toURL();
		
			var url= myUrl.toString();
			engine.getLoadWorker().stateProperty().addListener((observable ,oldState, newState)->{
				if (newState == Worker.State.SUCCEEDED) {
					loadMarkers();
				}
				
			});
			engine.load(url);
		} catch (MalformedURLException e) {
			AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
		}
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		var toggleGroup = new ToggleGroup();
		btnJoinTournament.setToggleGroup(toggleGroup);
		// avoid unselected button
		toggleGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
			if (newVal == null) {
				oldVal.setSelected(true);
			}
		});
		btnJoinTournament.fire();				
	}
	
	
	
	private void loadMarkers() {
		lblMsg.setVisible(false);
		var ctrl = new ReserveTournamentSeatController();
		try {
			activeTournaments = ctrl.retrieveOpenTournaments();
			if (activeTournaments.isEmpty()) {
				lblMsg.setText("No open tournament has been found");
				lblMsg.setVisible(true);
			}else {
				for (TournamentBean bean : activeTournaments){
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

	@FXML
	public void reserveTournament(ActionEvent event) {
		String tournamentName = getSelectedTournament();
		TournamentBean bean = getSelectedBean(tournamentName);
		if (bean == null) {
			AlertFactory.getInstance().createAlert("Something went wrong, please reload the page", AlertType.ERROR).show();
			return;
		}
		
		var ctrl = new ReserveTournamentSeatController();
		try {
			ctrl.joinTournament(bean, ssn.getUser());
			AlertFactory.getInstance().createAlert("Tournament joined successfully", AlertType.INFORMATION).show();
		} catch (DAOException | MaxParticipantsException | WrongUserTypeException e) {
			AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).showAndWait();
		}
		
	}

	private TournamentBean getSelectedBean(String tournamentName) {
		for (TournamentBean bean : activeTournaments) {
			if (bean.getName().equals(tournamentName)) {
				return bean;
			}
		}
		return null;
	}

	private String getSelectedTournament() {
		var script = "getSelectedTournament();";
		return (String) engine.executeScript(script);		
	}
	

}
