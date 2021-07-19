package main.java.controller.guicontroller.tournaments;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import main.java.controller.applicationcontroller.createtournament.CreateTournamentController;
import main.java.controller.guicontroller.GuiBasicInternalPageController;
import main.java.engineering.bean.tournaments.TournamentBean;
import main.java.engineering.exceptions.AlertFactory;
import main.java.engineering.exceptions.BeanCheckException;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.DateParsingException;
import main.java.engineering.utils.Session;
import main.java.engineering.utils.map.MapPlace;
import main.java.model.CardGame;
import main.java.view.standalone.SearchMapPlaceTextField;

public class GuiCreateTournamentController extends GuiBasicInternalPageController {

	public GuiCreateTournamentController(Session ssn) {
		super(ssn);
	}

	@FXML
	private ToggleButton toggleCreate;

	@FXML
	private ToggleButton toggleHandleTournaments;

	@FXML
	private AnchorPane apHandle;

	@FXML
	private AnchorPane apCreateTournament;

	@FXML
	private WebView webViewCreate;
	private WebEngine engine;

	@FXML
	private TextField tfName;

	@FXML
	private ComboBox<String> cbCardGame;

	@FXML
	private DatePicker dpDatePicker;

	@FXML
	private ComboBox<String> cbHours;

	@FXML
	private ComboBox<String> cbMin;

	@FXML
	private TextField tfMaxParticipants;

	@FXML
	private TextField tfPrice;

	@FXML
	private TextField tfAwards;

	@FXML
	private CheckBox cbSearchSponsor;

	@FXML
	private Button btnCreateTournament;

	@FXML
	private VBox vbox;

	private static final String HTML_MAP = "src/main/java/view/standalone/createtable/mapbox.html";
	private String location;
	private double latitude;
	private double longitude;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		engine = webViewCreate.getEngine();

		var toggleGroup = new ToggleGroup();
		toggleCreate.setToggleGroup(toggleGroup);
		toggleHandleTournaments.setToggleGroup(toggleGroup);
		// avoid unselected button
		toggleGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
			if (newVal == null) {
				oldVal.setSelected(true);
			}
		});
		toggleCreate.fire();
	}

	@FXML
	public void handleToggleCreate(ActionEvent event) {
		apHandle.toBack();
		apCreateTournament.toFront();

		setMap();
		setAutocompleteTextField();
		loadGames();
		loadTime();
	}

	private void setMap() {

		URL myUrl = null;
		try {
			myUrl = new File(HTML_MAP).toURI().toURL();

			var url = myUrl.toString();
			engine.load(url);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
	}

	private void setAutocompleteTextField() {
		var textField = new TextField();
		textField.setPrefSize(250, 25);
		textField.setPromptText("Place");
		textField.setLayoutX(25);
		textField.setLayoutY(50);
		var autocompleteSearch = new SearchMapPlaceTextField(textField);
		apCreateTournament.getChildren().add(autocompleteSearch.getTextField());
		autocompleteSearch.getLastSelectedItem().addListener((observable, oldValue, newValue) -> {
			if (autocompleteSearch.getLastSelectedItem().get() != null) {

				// target interface of Adapter GOF pattern
				MapPlace place = autocompleteSearch.getLastSelectedItem().getValue();
				// update values to save data of selected place
				location = place.toString();
				latitude = place.getCoordinates().get(0);
				longitude = place.getCoordinates().get(1);
				var zoom = 9;
				engine.executeScript("updateMap(" + latitude + "," + longitude + "," + zoom + ");");
			}
		});
	}

	private void loadGames() {

		// upload the cardGames for combo box
		cbCardGame.getItems().clear();

		List<String> games = new ArrayList<>();
		CardGame[] enumGames = CardGame.values();

		for (CardGame cardGame : enumGames) {
			games.add(cardGame.toString());
		}

		cbCardGame.getItems().addAll(games);
	}

	private void loadTime() {

		// upload hours and minutes for time's combo boxes
		cbHours.getItems().clear();
		cbMin.getItems().clear();

		for (var i = 0; i < 60; i++) {
			if (i < 24)
				cbHours.getItems().add(String.format("%02d", i));
			cbMin.getItems().add(String.format("%02d", i));
		}

	}

	@FXML
	public void createTournament(ActionEvent event) {
		
		var tournamentBean = buildBeanFromInput();
		if (tournamentBean != null) {
			try {
				tournamentBean.checkRulesForInsert();
			} catch (BeanCheckException | DateParsingException e) {
				AlertFactory.getInstance()
					.createAlert(e.getMessage(), AlertType.ERROR).show();
				return;
			}
			var ctrl = new CreateTournamentController();
			try {
				if (Boolean.TRUE.equals(ctrl.createTournament(tournamentBean))) {
					AlertFactory.getInstance()
					.createAlert("Tournament succesfully created", AlertType.INFORMATION).show();
				}
			} catch (DAOException | DateParsingException e) {
				AlertFactory.getInstance()
				.createAlert(e.getMessage(), AlertType.ERROR).show();
			}
		}
	}

	// second page controllers
	@FXML
	public void handleTournaments(ActionEvent event) {
		apCreateTournament.toBack();
		apHandle.toFront();

		setMap();
		setAutocompleteTextField();
	}
	
	private TournamentBean buildBeanFromInput() {
		
		TournamentBean tournamentBean = null;
		String name = tfName.getText();
		String cardGame = cbCardGame.getValue();
		String organizer = this.ssn.getUser().getUsername();
		int maxParticipants;
		float price;
		float award;
		if (dpDatePicker.getValue() != null) {
			String date = dpDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			String time = cbHours.getValue() + ":" + cbMin.getValue();
			if (cbHours.getValue() == null || cbMin.getValue() == null) {
				AlertFactory.getInstance()
					.createAlert("Insert hours and minutes correctly please", AlertType.ERROR).show();
				return null;
			}

			// check correct parsing of numbers from text fields
			try {
				maxParticipants = Integer.parseInt(tfMaxParticipants.getText());
			} catch (NumberFormatException e) {
				AlertFactory.getInstance()
						.createAlert("The 'Max participants' field must be an integer number", AlertType.ERROR).show();
				return null;
			}

			try {
				price = Float.parseFloat(tfPrice.getText());
			} catch (NumberFormatException e) {
				AlertFactory.getInstance()
						.createAlert("The 'Price' field must be a floating point number", AlertType.ERROR).show();
				return null;
			}

			try {
				award = Float.parseFloat(tfAwards.getText());
			} catch (NumberFormatException e) {
				AlertFactory.getInstance()
						.createAlert("The 'Award' field must be a floating point number", AlertType.ERROR).show();
				return null;
			}

			Boolean reqSponsor = cbSearchSponsor.isSelected();

			tournamentBean = new TournamentBean();
			tournamentBean.setName(name);
			tournamentBean.setCardGame(cardGame);
			tournamentBean.setAddress(location);
			tournamentBean.setLatitude(latitude);
			tournamentBean.setLongitude(longitude);
			tournamentBean.setDate(date);
			tournamentBean.setTime(time);
			tournamentBean.setOrganizer(organizer);
			tournamentBean.setMaxParticipants(maxParticipants);
			tournamentBean.setPrice(price);
			tournamentBean.setAward(award);
			tournamentBean.setInRequestForSponsor(reqSponsor);
			
			
		}else {
			AlertFactory.getInstance()
			.createAlert("You have to select a date!", AlertType.ERROR).show();
		}
		return tournamentBean;
	}
}
