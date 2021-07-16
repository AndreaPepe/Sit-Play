package main.java.controller.guicontroller.business;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.commons.io.IOUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import main.java.controller.applicationcontroller.business.SponsorizeTournamentController;
import main.java.controller.applicationcontroller.reserveaseat.tournament.ReserveTournamentSeatController;
import main.java.controller.guicontroller.GuiBasicInternalPageController;
import main.java.engineering.bean.businessactivity.BusinessActivityBean;
import main.java.engineering.bean.tournaments.TournamentBean;
import main.java.engineering.exceptions.AlertFactory;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.utils.Session;

public class GuiSponsorizeTournamentController extends GuiBasicInternalPageController{

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
		URL myUrl=null;
		try {
			myUrl = new File(MAP_RESOURCE).toURI().toURL();
		
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
		
		loadActivities();
    }

	private void loadMarkers() {
		lblMsg.setVisible(false);
		var ctrl = new ReserveTournamentSeatController();
		try {
			sponsorizableTournaments = ctrl.retrieveOpenTournaments();
			if (sponsorizableTournaments.isEmpty()) {
				lblMsg.setText("No open tournament has been found");
				lblMsg.setVisible(true);
			}else {
				for (TournamentBean bean : sponsorizableTournaments){
					var lat = bean.getLatitude();
					var lng = bean.getLongitude();
					var sponsorBean = bean.getSponsor();
					var icon = "\"" + "star" + "\"";
					var color = "\"" + "green" + "\"";
					var idname = "\"" + bean.getName() + "\"";
					var cardGame = "\"" + bean.getCardGame() + "\"";
					var address = "\"" + bean.getAddress() + "\"";
					var date = "\"" + bean.getDate() + "\"";
					var time = "\"" + bean.getTime() + "\"";
					var organizer = "\"" + bean.getOrganizer() + "\"";
					
					var maxParticipants = "\"" + bean.getMaxParticipants() +"\"";
					var price = "\"" + String.format("€ %.2f", bean.getPrice()) + "\"";
					var award = "\"" + String.format("€ %.2f", bean.getAward()) + "\"";
					String sponsor;
					String imgType;
					String logo;
					if(sponsorBean != null) {
						sponsor ="\"" + sponsorBean.getName() + "\"";
						//TODO: only png format for now
						imgType ="\"" + "png" + "\"";
						logo = sponsorBean.getLogo()!=null ? convertToByteArray(sponsorBean.getLogo()) : "\"" + "null" + "\"";
						
					}else {
						sponsor = "\"" + "null" + "\"";
						imgType = "\"" + "null" + "\"";
						logo = "\"" + "null" + "\"";
					}
					
					engine.executeScript("addMarker("+lat+"," + lng + "," + icon
							+ "," + color + "," + idname + "," + cardGame + "," + address + "," + date + "," + time	+ "," + organizer
							+ "," + maxParticipants + "," + price + "," + award + "," + sponsor + "," + imgType + "," + logo 
							+");");
				}
				
			}
		} catch (DAOException e) {
			lblMsg.setText(e.getMessage());
			lblMsg.setVisible(true);
		} catch (IOException e) {
			AlertFactory.getInstance().createAlert("Unable to parse sponsor's logo", AlertType.ERROR).show();
		}
		
	}
	
	private String convertToByteArray(InputStream logo) throws IOException {
		byte[] byteArray = IOUtils.toByteArray(logo);
		return Base64.getEncoder().encodeToString(byteArray);
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
	private void sponsorize (ActionEvent event) {
		
	}
}
