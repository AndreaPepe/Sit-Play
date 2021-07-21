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
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import main.java.controller.applicationcontroller.reserveaseat.tournament.ReserveTournamentSeatController;
import main.java.controller.guicontroller.GuiBasicInternalPageController;
import main.java.engineering.bean.tournaments.TournamentBean;
import main.java.engineering.exceptions.AlertFactory;
import main.java.engineering.exceptions.AlreadyExistingWinnerException;
import main.java.engineering.exceptions.BeanCheckException;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.DateParsingException;
import main.java.engineering.utils.Session;
import main.java.engineering.utils.map.MapPlace;
import main.java.model.CardGame;
import main.java.view.listview.ListElementFactory;
import main.java.view.listview.ListElementType;
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
	private ToggleButton toggleDeclareWinner;

	@FXML
	private AnchorPane apHandle;

	@FXML
	private AnchorPane apCreateTournament;

	@FXML
	private AnchorPane apDeclareWinner;

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

	// page declare winner attributes
	@FXML
	private ComboBox<String> cbTournament;

	@FXML
	private ComboBox<String> cbSelectWinner;

	@FXML
	private Button btnWinner;

	private List<TournamentBean> winnerBeans;

	private static final String HTML_MAP = "src/main/java/view/standalone/createtable/mapbox.html";
	private String location;
	private double latitude;
	private double longitude;

	// page handle tournaments attributes
	@FXML
	private VBox vboxTournaments;

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
		apDeclareWinner.toBack();
		apCreateTournament.toFront();

		setMap();
		setAutocompleteTextField();
		setNumericTextFields();
		loadGames();
		loadTime();
	}

	private void setNumericTextFields() {
		
		// make sure the user can insert only integers or floating points
		tfMaxParticipants.textProperty().addListener((obs, oldVal, newVal) -> {
			if (!newVal.matches("\\d{0,4}")) {
				tfMaxParticipants.setText(oldVal);
			}
		});
		
		tfPrice.textProperty().addListener((obd, oldVal, newVal) -> {
			if (!newVal.matches("\\d{0,4}([\\.]\\d{0,2})?")) {
				tfPrice.setText(oldVal);
			}
		});
		
		tfAwards.textProperty().addListener((obd, oldVal, newVal) -> {
			if (!newVal.matches("\\d{0,7}([\\.]\\d{0,2})?")) {
				tfAwards.setText(oldVal);
			}
		});
		
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
				AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
				return;
			}
			var ctrl = new CreateTournamentController();
			try {
				if (Boolean.TRUE.equals(ctrl.createTournament(tournamentBean))) {
					AlertFactory.getInstance().createAlert("Tournament succesfully created", AlertType.INFORMATION)
							.show();
				}
			} catch (DAOException | DateParsingException e) {
				AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
			}
		}
	}

	// second page controllers
	@FXML
	public void handleTournaments(ActionEvent event) {
		apCreateTournament.toBack();
		apDeclareWinner.toBack();
		apHandle.toFront();

		var ctrl = new ReserveTournamentSeatController();
		try {
			var beans = ctrl.getDeletableTournaments(ssn.getUser());
			vboxTournaments.getChildren().clear();
			for (TournamentBean bean : beans) {
				// factory method GOF
				ListElementFactory.getInstance().createElement(ListElementType.DELETABLE_TOURNAMENT, vboxTournaments,
						bean);
			}
		} catch (DateParsingException | DAOException e) {
			AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
		}
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
				AlertFactory.getInstance().createAlert("Insert hours and minutes correctly please", AlertType.ERROR)
						.show();
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

		} else {
			AlertFactory.getInstance().createAlert("You have to select a date!", AlertType.ERROR).show();
		}
		return tournamentBean;
	}

	// declare winner controls
	@FXML
	public void handleDeclareWinner(ActionEvent event) {
		apHandle.toBack();
		apCreateTournament.toBack();
		apDeclareWinner.toFront();

		loadWinnerTournaments();
		setUpUI();
	}

	@FXML
	public void setWinner(ActionEvent event) {
		var confirmationDialog = AlertFactory.getInstance()
				.createAlert("Are you sure? This is an irreversible operation", AlertType.CONFIRMATION);
		var btnYes = new ButtonType("Yes", ButtonData.YES);
		var btnNo = new ButtonType("No", ButtonData.NO);
		confirmationDialog.getButtonTypes().setAll(btnYes, btnNo);
		confirmationDialog.showAndWait().ifPresent(type -> {
			if (type == btnYes) {
				var selectedWinner = cbSelectWinner.getValue();
				var ctrl = new ReserveTournamentSeatController();
				var bean = getSelectedTournamentForWinner();
				if (bean != null) {
					bean.setWinner(selectedWinner);
					try {
						ctrl.setWinner(bean, ssn.getUser());
						AlertFactory.getInstance().createAlert("Winner declared with success", AlertType.INFORMATION)
								.show();
						// reload the page
						toggleDeclareWinner.fire();
					} catch (DAOException | DateParsingException | AlreadyExistingWinnerException e) {
						AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
					}
				}
			}
		});
	}

	private void loadWinnerTournaments() {
		var ctrl = new ReserveTournamentSeatController();
		try {
			winnerBeans = ctrl.retrieveTournamentsToDeclareWinnerTo(ssn.getUser());
			cbTournament.getItems().clear();
			cbTournament.setPromptText("Select tournament");
			cbSelectWinner.getItems().clear();
			cbSelectWinner.setPromptText("Select winner");
			for (TournamentBean bean : winnerBeans) {
				cbTournament.getItems().add(bean.getName());
			}
		} catch (DAOException | DateParsingException e) {
			AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
		}
	}

	private void setUpUI() {
		cbSelectWinner.setDisable(true);
		btnWinner.setDisable(true);

		cbTournament.valueProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null) {
				// load participants
				cbSelectWinner.getItems().clear();
				var selectedTournament = getSelectedTournamentForWinner();
				if (selectedTournament != null) {
					selectedTournament.getParticipants().forEach(it -> cbSelectWinner.getItems().add(it));
				}
				cbSelectWinner.setDisable(false);
			} else {
				cbSelectWinner.setDisable(true);
				btnWinner.setDisable(true);
			}
		});

		cbSelectWinner.valueProperty().addListener((obs, oldVal, newVal) -> btnWinner.setDisable(newVal == null));
	}

	private TournamentBean getSelectedTournamentForWinner() {
		var name = cbTournament.getValue();
		if (name == null) {
			AlertFactory.getInstance().createAlert("Please select a tournament", AlertType.WARNING).show();
		} else {
			for (TournamentBean b : winnerBeans) {
				if (b.getName().equals(name)) {
					return b;
				}
			}
		}
		return null;
	}
}
