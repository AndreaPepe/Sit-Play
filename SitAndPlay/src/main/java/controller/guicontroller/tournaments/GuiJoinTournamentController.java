package main.java.controller.guicontroller.tournaments;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
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
import main.java.controller.applicationcontroller.reserveaseat.table.ReserveTableSeatController;
import main.java.controller.guicontroller.GuiBasicInternalPageController;
import main.java.engineering.bean.createtable.TableBean;
import main.java.engineering.exceptions.AlertFactory;
import main.java.engineering.exceptions.DAOException;
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
    
    private static final String MAP_WITH_MARKERS = "src/main/java/view/standalone/createtable/BigMapWithMarkers.html";
    
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
		
		// TODO: create Tournaments and retrieve them
		var ctrl = new ReserveTableSeatController();
		try {
			var beanList = ctrl.retrieveOpenTables();
			if (beanList.isEmpty()) {
				lblMsg.setText("No open table has been found");
				lblMsg.setVisible(true);
			}else {
				for (TableBean bean : beanList){
					var lat = bean.getLatitude();
					var lng = bean.getLongitude();
					var icon = "\"" + "star" + "\"";
					var idname = "\"" + bean.getName() + "\"";
					var cardGame = "\"" + bean.getCardGame() + "\"";
					var address = "\"" + bean.getAddress() + "\"";
					var date = "\"" + bean.getDate() + "\"";
					var time = "\"" + bean.getTime() + "\"";
					var color = "\"" + "green" + "\"";

					engine.executeScript("addMarker("+lat+"," + lng + "," + icon
							+ "," + color + "," + idname + "," + cardGame + "," + address + "," + date + "," + time							
							+");");
				}
				
			}
		} catch (DAOException e) {
			lblMsg.setText(e.getMessage());
			lblMsg.setVisible(true);
		}
		
	}
	
	@FXML
	public void reserveTournament(ActionEvent event) {
		//TODO
	}
	

}
