package main.java.controller.guicontroller.createtable;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import main.java.controller.applicationcontroller.createtable.CreateTableController;
import main.java.controller.applicationcontroller.reserveaseat.table.ReserveTableSeatController;
import main.java.controller.guicontroller.GuiBasicInternalPageController;
import main.java.engineering.bean.createtable.PlaceBean;
import main.java.engineering.bean.createtable.TableBean;
import main.java.engineering.exceptions.AlertFactory;
import main.java.engineering.exceptions.BeanCheckException;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.WrongUserTypeException;
import main.java.engineering.utils.Session;
import main.java.engineering.utils.map.MapPlace;
import main.java.model.CardGame;
import main.java.view.standalone.SearchMapPlaceTextField;

public class GuiPlayerCreateTableController extends GuiBasicInternalPageController {

	@FXML
	private AnchorPane apnReserve;

	@FXML
	private AnchorPane apnCreate;

	@FXML
	private ToggleButton btnCreateMenu;

	@FXML
	private ToggleButton btnReserveMenu;

	@FXML
	private TextField tfTableName;

	@FXML
	private DatePicker datePicker;

	@FXML
	private ComboBox<String> cbCardGame;

	@FXML
	private Button btnCreateTable;

	@FXML
	private ComboBox<String> cbHours;

	@FXML
	private ComboBox<String> cbMinutes;

	@FXML
	private Label lblError;

	@FXML
	private Label lblSuccessCreate;

	@FXML
	private WebView webMap;
	private WebEngine engine;
	private static final String HTML_MAP = "src/main/java/view/standalone/createtable/mapbox.html";

	private String location;
	private double latitude;
	private double longitude;

	// second pane attributes
	@FXML
	private Label lblJoinResult;

	@FXML
	private Button btnJoin;

	@FXML
	private WebView webViewAllTables;
	private WebEngine allEngine;
	private static final String MAP_WITH_MARKERS = "src/main/java/view/standalone/createtable/BigMapWithMarkers.html";

	private List<TableBean> beanList;

	public GuiPlayerCreateTableController(Session ssn) {
		super(ssn);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// setting up the toggle group in the top bar of the pane
		var topBarToggleGroup = new ToggleGroup();
		btnCreateMenu.setToggleGroup(topBarToggleGroup);
		btnReserveMenu.setToggleGroup(topBarToggleGroup);
		// avoid unselected button
		topBarToggleGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
			if (newVal == null) {
				oldVal.setSelected(true);
			}
		});

		// create the web engine for webMap in createTable pane
		engine = webMap.getEngine();

		// start from the create table pane
		btnCreateMenu.fire();
		lblError.setVisible(false);
		lblSuccessCreate.setVisible(false);
	}

	private void setUpAutocompleteTextField() {
		var textField = new TextField();
		textField.setPrefHeight(25);
		textField.setPrefWidth(200);
		textField.setLayoutX(115);
		textField.setLayoutY(74);
		var autocompleteSearch = new SearchMapPlaceTextField(textField);
		apnCreate.getChildren().add(autocompleteSearch.getTextField());
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

	private void loadMapbox() {

		URL myUrl = null;
		try {
			myUrl = new File(HTML_MAP).toURI().toURL();

			var url = myUrl.toString();
			engine.load(url);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
	}

	@FXML
	public void handleCreateMenu(ActionEvent event) {
		apnReserve.toBack();
		apnCreate.toFront();
		loadMapbox();
		setUpAutocompleteTextField();
		loadGames();
		loadTime();
		lblError.setVisible(false);
		lblSuccessCreate.setVisible(false);
	}

	@FXML
	public void handleReserveMenu(ActionEvent event) {

		loadSecondPage();
		apnCreate.toBack();
		apnReserve.toFront();
	}

	@FXML
	public void handleCreateTable(ActionEvent event) {

		lblError.setVisible(false);
		lblSuccessCreate.setVisible(false);

		String name = tfTableName.getText();
		String cardGame = cbCardGame.getValue();
		if (cardGame == null) {
			AlertFactory.getInstance().createAlert("Please select a card game", AlertType.ERROR).show();
			return;
		}
		String organizer = ssn.getUser().getUsername();
		if (datePicker.getValue() != null) {
			String date = datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			String time = cbHours.getValue() + ":" + cbMinutes.getValue();
			if (cbHours.getValue() == null || cbMinutes.getValue() == null) {
				AlertFactory.getInstance().createAlert("Insert hours and minutes correctly please", AlertType.ERROR)
						.show();
				return;
			}
			var placeBean = new PlaceBean(location, latitude, longitude);
			var tableBean = new TableBean(name, placeBean, cardGame, date, time, organizer);
			try {
				if (Boolean.FALSE.equals(tableBean.checkCreateTable())) {
					return;
				}
			} catch (BeanCheckException e1) {
				AlertFactory.getInstance().createAlert(e1.getMessage(), AlertType.ERROR).showAndWait();
			}

			var ctrl = new CreateTableController();
			try {
				if (ctrl.createTable(tableBean)) {
					// table created successfully
					AlertFactory.getInstance().createAlert("Table created succesfully", AlertType.INFORMATION).show();
				}
			} catch (DAOException e) {
				AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
			}
		} else {
			AlertFactory.getInstance().createAlert("Please select a date", AlertType.ERROR).show();
		}

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
		cbMinutes.getItems().clear();

		for (var i = 0; i < 60; i++) {
			if (i < 24)
				cbHours.getItems().add(String.format("%02d", i));
			cbMinutes.getItems().add(String.format("%02d", i));
		}

	}

	/**
	 * Event controller of the second page "Reserve Seat"
	 */
	private void loadSecondPage() {
		// create the web engine for webViewAllTables in second pane
		allEngine = webViewAllTables.getEngine();
		URL myUrl = null;
		try {
			myUrl = new File(MAP_WITH_MARKERS).toURI().toURL();

			var url = myUrl.toString();
			allEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
				if (newState == Worker.State.SUCCEEDED) {
					loadMarkers();
				}

			});
			allEngine.load(url);
		} catch (MalformedURLException e) {
			AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
		}
	}

	private void loadMarkers() {
		var ctrl = new ReserveTableSeatController();
		try {
			beanList = ctrl.retrieveOpenTables();
			if (beanList.isEmpty()) {
				AlertFactory.getInstance().createAlert("No open table has been found; retry later", AlertType.WARNING).show();
			} else {
				for (TableBean bean : beanList) {
					var lat = bean.getLatitude();
					var lng = bean.getLongitude();
					var idname = "\"" + bean.getName() + "\"";
					var cardGame = "\"" + bean.getCardGame() + "\"";
					var address = "\"" + bean.getAddress() + "\"";
					var date = "\"" + bean.getDate() + "\"";
					var time = "\"" + bean.getTime() + "\"";
					var color = "\"" + "#214183" + "\"";

					allEngine.executeScript("addMarker(" + lat + "," + lng + "," + null + "," + color + "," + idname
							+ "," + cardGame + "," + address + "," + date + "," + time + ");");
				}

			}
		} catch (DAOException e) {
			AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
		}
	}

	@FXML
	public void joinTable() {
		String tableName = getSelectedTable();
		if (tableName == null) {
			AlertFactory.getInstance()
					.createAlert("No table has been selected. First select one on the map.", AlertType.WARNING).show();
		}

		try {
			TableBean bean = findSelectedTable(tableName);
			if (bean == null) {
				AlertFactory.getInstance().createAlert("Something went wrong, please reload the page", AlertType.ERROR)
						.show();
				return;
			}
			var ctrl = new ReserveTableSeatController();
			ctrl.joinTable(bean, ssn.getUser());
			AlertFactory.getInstance().createAlert("Table joined successfully", AlertType.INFORMATION).show();

		} catch (DAOException | WrongUserTypeException e) {
			AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
		}
	}

	private TableBean findSelectedTable(String tableName) {
		for (TableBean tb : beanList) {
			if (tb.getName().equals(tableName)) {
				return tb;
			}
		}
		return null;
	}

	public String getSelectedTable() {
		var script = "getSelectedTable();";
		return (String) allEngine.executeScript(script);
	}
}