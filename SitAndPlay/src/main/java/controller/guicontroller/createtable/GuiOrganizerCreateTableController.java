package main.java.controller.guicontroller.createtable;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import main.java.controller.applicationcontroller.createtable.CreateTableController;
import main.java.controller.applicationcontroller.reserveaseat.table.ReserveTableSeatController;
import main.java.controller.guicontroller.GuiBasicInternalPageController;
import main.java.engineering.bean.createtable.PlaceBean;
import main.java.engineering.bean.createtable.TableBean;
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

public class GuiOrganizerCreateTableController extends GuiBasicInternalPageController {


	@FXML
	private AnchorPane apnCreateTable;

	@FXML
	private AnchorPane apnMyTables;
	
	@FXML
	private AnchorPane apnOrganizedTables;

	@FXML
	private ToggleButton toggleCreate;

	@FXML
	private ToggleButton toggleDeclareWinner;
	
	@FXML
	private ToggleButton toggleOrganizedTables;

	@FXML
	private TextField tfName;

	@FXML
	private DatePicker dpDate;

	@FXML
	private ComboBox<String> cbCardGame;

	@FXML
	private Button btnCreate;

	@FXML
	private ComboBox<String> cbHours;

	@FXML
	private ComboBox<String> cbMin;

	@FXML
	private WebView webMap;
	private WebEngine webEngine;
	private static final String HTML_MAP = "src/main/java/view/standalone/createtable/mapbox.html";

	private String address;
	private double lat;
	private double lng;

	// second page attributes
	@FXML
	private ComboBox<String> cbTable;

	@FXML
	private ComboBox<String> cbWinner;

	@FXML
	private Button btnDeclareWinner;

	private List<TableBean> winnerBeans;
	
	//third page attributes
	@FXML
	private VBox vboxTables;

	public GuiOrganizerCreateTableController(Session ssn) {
		super(ssn);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// setting up the toggle group in the top bar of the pane
		var toggleGr = new ToggleGroup();
		toggleCreate.setToggleGroup(toggleGr);
		toggleDeclareWinner.setToggleGroup(toggleGr);
		toggleOrganizedTables.setToggleGroup(toggleGr);
		// avoid unselected button
		toggleGr.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
			if (newVal == null) {
				oldVal.setSelected(true);
			}
		});

		// create the web engine for webMap in createTable pane
		webEngine = webMap.getEngine();

		// start from the create table pane
		toggleCreate.fire();
	}

	private void setUpAutocompleteTextField() {
		var tf = new TextField();
		tf.setPrefHeight(25);
		tf.setPrefWidth(280);
		tf.setLayoutX(35);
		tf.setLayoutY(74);
		tf.setPromptText("Select Place");
		var autocompletePlace = new SearchMapPlaceTextField(tf);
		apnCreateTable.getChildren().add(autocompletePlace.getTextField());
		autocompletePlace.getLastSelectedItem().addListener((observable, oldValue, newValue) -> {
			if (autocompletePlace.getLastSelectedItem().get() != null) {

				// target interface of Adapter GOF pattern
				MapPlace place = autocompletePlace.getLastSelectedItem().getValue();
				// update values to save data of selected place
				address = place.toString();
				lat = place.getCoordinates().get(0);
				lng = place.getCoordinates().get(1);
				var zoom = 9;
				webEngine.executeScript("updateMap(" + lat + "," + lng + "," + zoom + ");");
			}
		});
	}

	private void loadMapbox() {

		URL myUrl = null;
		try {
			myUrl = new File(HTML_MAP).toURI().toURL();

			var url = myUrl.toString();
			webEngine.load(url);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
	}

	@FXML
	public void handleToggleCreate(ActionEvent event) {
		apnMyTables.toBack();
		apnOrganizedTables.toBack();
		apnCreateTable.toFront();
		loadMapbox();
		setUpAutocompleteTextField();
		loadCardGames();
		loadTime();
	}


	@FXML
	public void createTable(ActionEvent event) {

		String name = tfName.getText();
		String cardGame = cbCardGame.getValue();
		if (cardGame == null) {
			AlertFactory.getInstance().createAlert("Please select a card game", AlertType.ERROR).show();
			return;
		}
		String organizer = ssn.getUser().getUsername();
		if (dpDate.getValue() != null) {
			String date = dpDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			String time = cbHours.getValue() + ":" + cbMin.getValue();
			if (cbHours.getValue() == null || cbMin.getValue() == null) {
				AlertFactory.getInstance().createAlert("Insert hours and minutes correctly please", AlertType.ERROR)
						.show();
				return;
			}
			var placeBean = new PlaceBean(address, lat, lng);
			var tableBean = new TableBean(name, placeBean, cardGame, date, time, organizer);
			try {
				if (Boolean.FALSE.equals(tableBean.checkCreateTable())) {
					return;
				}
			} catch (BeanCheckException | DateParsingException e1) {
				AlertFactory.getInstance().createAlert(e1.getMessage(), AlertType.ERROR).showAndWait();
				return;
			}

			var ctrl = new CreateTableController();
			try {
				if (ctrl.createTable(tableBean)) {
					// table created successfully
					AlertFactory.getInstance().createAlert("Table created succesfully", AlertType.INFORMATION).show();
				}
			} catch (DAOException | DateParsingException e) {
				AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
			}
		} else {
			AlertFactory.getInstance().createAlert("Please select a date", AlertType.ERROR).show();
		}

	}

	private void loadCardGames() {

		// upload the cardGames for combo box
		cbCardGame.getItems().clear();

		List<String> games = new ArrayList<>();
		CardGame[] enumGames = CardGame.values();

		for (CardGame cg : enumGames) {
			games.add(cg.toString());
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


	// second page controls
	@FXML
	public void handleToggleWinner(ActionEvent event) {
		apnCreateTable.toBack();
		apnOrganizedTables.toBack();
		apnMyTables.toFront();
		loadWinnerTables();
		setUIElements();
	}

	@FXML
	public void declareWinner(ActionEvent event) {
		var conf = AlertFactory.getInstance().createAlert("Are you sure? This is an irreversible operation",
				AlertType.CONFIRMATION);
		var btnYes = new ButtonType("Yes", ButtonData.YES);
		var btnNo = new ButtonType("No", ButtonData.NO);
		conf.getButtonTypes().setAll(btnYes, btnNo);
		conf.showAndWait().ifPresent(type -> {
			if (type == btnYes) {
				var selectedParticipant = cbWinner.getValue();
				var ctrl = new ReserveTableSeatController();
				var tBean = getSelectedWinnerTable();
				if (tBean != null) {
					tBean.setWinner(selectedParticipant);
					try {
						ctrl.declareWinner(tBean, ssn.getUser());
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

	private void loadWinnerTables() {
		var ctrl = new ReserveTableSeatController();
		try {
			winnerBeans = ctrl.retrieveTablesToDeclareWinnerTo(ssn.getUser());
			cbTable.getItems().clear();
			cbWinner.getItems().clear();
			cbTable.setPromptText("Select organized table");
			cbWinner.setPromptText("Select winner");
			for (TableBean b : winnerBeans) {
				cbTable.getItems().add(b.getName());
			}

		} catch (DateParsingException | DAOException e) {
			AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
		}
	}

	private void setUIElements() {
		cbWinner.setDisable(true);
		btnDeclareWinner.setDisable(true);

		cbTable.valueProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null) {
				cbWinner.getItems().clear();
				var bean = getSelectedWinnerTable();
				if (bean != null) {
					bean.getParticipants().forEach(it -> cbWinner.getItems().add(it));
				}
				cbWinner.setDisable(false);
			} else {
				cbWinner.setDisable(true);
				btnDeclareWinner.setDisable(true);
			}
		});

		cbWinner.valueProperty().addListener((obs, oldVal, newVal) -> btnDeclareWinner.setDisable((newVal == null)));

	}

	private TableBean getSelectedWinnerTable() {
		var name = cbTable.getValue();
		if (name == null) {
			AlertFactory.getInstance().createAlert("Select a table first", AlertType.WARNING).show();

		} else {
			for (TableBean b : winnerBeans) {
				if (b.getName().equals(name)) {
					return b;
				}
			}
		}
		return null;
	}
	
	// third page controls
	@FXML
	public void handleToggleOrganized(ActionEvent event) {
		apnCreateTable.toBack();
		apnMyTables.toBack();
		apnOrganizedTables.toFront();
		
		var ctrl = new ReserveTableSeatController();
		try {
			var tBeans = ctrl.retrieveDeletableTables(ssn.getUser());
			vboxTables.getChildren().clear();
			for (TableBean bean : tBeans) {
				ListElementFactory.getInstance().createElement(ListElementType.DELETABLE_TABLE, vboxTables, bean);
			}
		} catch (DateParsingException | DAOException e) {
			AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
		}
	}
}