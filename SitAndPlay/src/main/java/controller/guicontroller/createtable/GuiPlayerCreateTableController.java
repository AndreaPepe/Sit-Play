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
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.utils.Session;
import main.java.engineering.utils.map.MapPlace;
import main.java.model.CardGame;
import main.java.view.standalone.SearchMapPlaceTextField;

public class GuiPlayerCreateTableController extends GuiBasicInternalPageController{
	
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
    
    private SearchMapPlaceTextField autocompleteSearch;
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
        

	public GuiPlayerCreateTableController(Session ssn) {
		super(ssn);
		// TODO Auto-generated constructor stub
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
		autocompleteSearch = new SearchMapPlaceTextField(textField);
		apnCreate.getChildren().add(autocompleteSearch.getTextField());
		autocompleteSearch.getLastSelectedItem().addListener((observable,oldValue,newValue)->{
			if(autocompleteSearch.getLastSelectedItem().get()!=null) {
			MapPlace place = autocompleteSearch.getLastSelectedItem().getValue();
			// update values to save data of selected place
			location = place.toString();
			latitude = place.getCoordinates().get(0);
			longitude = place.getCoordinates().get(1);
			var zoom = 9;
			engine.executeScript("updateMap("+latitude+"," + longitude + "," + zoom +");");
			}			
		});
	}
	
	private void loadMapbox() {
	
		URL myUrl=null;
		try {
			myUrl = new File(HTML_MAP).toURI().toURL();
		
		var url= myUrl.toString();
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
    	String organizer = ssn.getLoggedUser().getUsername();
    	if (datePicker.getValue() != null) {
    		String date = datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    		String time = cbHours.getValue() + ":" + cbMinutes.getValue();
        	
        	var placeBean = new PlaceBean(location, latitude, longitude);
        	var tableBean = new TableBean(name, placeBean, cardGame, date, time, organizer);
        	if (Boolean.FALSE.equals(tableBean.checkCreateTable())) {
    			lblError.setText("Impossible to create a table in the past. Select future datetime.");
    			lblError.setVisible(true);
    			return;
    		}
        	
        	var ctrl = new CreateTableController();
        	try {
    			if(ctrl.createTable(tableBean)) {
    				// table created successfully
    				lblSuccessCreate.setText("Table created successfully");
    				lblSuccessCreate.setVisible(true);
    			}
    		} catch (DAOException e) {
    			lblError.setText(e.getMessage());
    			lblError.setVisible(true);
    		}
		}else {
			lblError.setText("Please, select a date");
			lblError.setVisible(true);
		}
    	
    	
    	
    }   
    
    
    private void loadGames() {
		
    	// upload the cardGames for combo box
		cbCardGame.getItems().clear();
		
		List<String> games = new ArrayList<>();
		CardGame[] enumGames = CardGame.values();
		
		for(CardGame cardGame : enumGames) {
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
    	URL myUrl=null;
		try {
			myUrl = new File(MAP_WITH_MARKERS).toURI().toURL();
		
			var url= myUrl.toString();
			allEngine.getLoadWorker().stateProperty().addListener((Observable ,oldState, newState)->{
				if (newState == Worker.State.SUCCEEDED) {
					loadMarkers();
				}
				
			});
			allEngine.load(url);
		} catch (MalformedURLException e1) {
			//TODO: alert
			e1.printStackTrace();
		}
    }
    
    private void loadMarkers() {
    	var ctrl = new ReserveTableSeatController();
		try {
			var beanList = ctrl.retrieveOpenTables();
			if (beanList.isEmpty()) {
				lblJoinResult.setText("No open table has been found");
				lblJoinResult.setVisible(true);
			}else {
				for (TableBean bean : beanList){
					var lat = bean.getLatitude();
					var lng = bean.getLongitude();
					var idname = "\"" + bean.getName() + "\"";
					var address = "\"" + bean.getAddress() + "\"";
					var date = "\"" + bean.getDate() + "\"";
					var time = "\"" + bean.getTime() + "\"";
					var color = "\"" + "#214183" + "\"";

					allEngine.executeScript("addMarker("+lat+"," + lng + "," + null
							+ "," + color + "," + idname + "," + address + "," + date + "," + time							
							+");");
				}
				
			}
		} catch (DAOException e) {
			lblJoinResult.setText(e.getMessage());
			lblJoinResult.setVisible(true);
		}
    }
    
    
    @FXML
    public void joinTable() {
    	String tableName = getSelectedTable();
    	if (tableName == null) {
    		lblJoinResult.setText("No table has been selected");
			lblJoinResult.setVisible(true);
			return;
		}
    	var ctrl = new ReserveTableSeatController();
    	try {
			TableBean bean = ctrl.retrieveTable(tableName);
			String newParticipant = ssn.getLoggedUser().getUsername();
			if (bean.checkBooleanJoinTable(newParticipant)){
				ctrl.joinTable(bean, newParticipant);
				lblJoinResult.setText("Join Successfully");
				lblJoinResult.setVisible(true);
			}
			
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public String getSelectedTable(){
    	var script = "getSelectedTable();";
    	return (String) allEngine.executeScript(script);
    }   
 }