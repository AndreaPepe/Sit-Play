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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import main.java.controller.applicationcontroller.createtable.CreateTableController;
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
    private TableView<TableBean> tableView;

    @FXML
    private TableColumn<TableBean, String> tcTable;
    
    @FXML
    private TableColumn<TableBean, String> tcCardGame;

    @FXML
    private TableColumn<TableBean, String> tcCity;

    @FXML
    private TableColumn<TableBean, String> tcDate;

    @FXML
    private TableColumn<TableBean, String> tcTime;

    @FXML
    private TableColumn<TableBean, String> tcOrganizer;

    @FXML
    private TableColumn<TableBean, Void> tcAction;

    @FXML
    private ComboBox<String> cbSelectCity;

    @FXML
    private Button btnSearch;
    
    @FXML
    private Label lblErrorSearch;
    
    @FXML
    private WebView webMap;
    private WebEngine engine;    
    private static final String HTML_MAP = "src/main/java/view/standalone/createtable/mapbox.html";
    
    private SearchMapPlaceTextField autocompleteSearch;
    private String location;
    private double latitude;
    private double longitude;
    

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
    
    
    @FXML
    public void handleSearch(ActionEvent event) {
    	
    	lblErrorSearch.setVisible(false);
    	if (cbSelectCity.getValue() == null) {
			lblErrorSearch.setText("You have to select a city first!");
			lblErrorSearch.setVisible(true);
			return;
		}
    	
//    	ObservableList<TableBean> list = FXCollections.observableArrayList();
//    	ReserveTableSeatController ctrl = new ReserveTableSeatController();
//		ArrayList<TableBean> beans = ctrl.dummyRetrieve(cbSelectCity.getValue());
//		for(TableBean bean : beans) {
//			list.add(bean);
//		}
//		initTable(list);
//		addButtonToTable();
    	
    	
    	// THIS MUST BE DELETED IN FUTURE; A DIFFERENT FEATURE IS COMING IN TOWN!!!
    	
//    	try {
//    		ObservableList<TableBean> list = FXCollections.observableArrayList();
//        	ReserveTableSeatController ctrl = new ReserveTableSeatController();
//			ArrayList<TableBean> beans = ctrl.retrieveTablesByCity(cbSelectCity.getValue());
//			for(TableBean bean : beans) {
//				list.add(bean);
//			}
//			initTable(list);
//			addButtonToTable();
//		} 
//    	catch (DAOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	
    }
    
//    private void addButtonToTable() {
//
//        Callback<TableColumn<TableBean, Void>, TableCell<TableBean, Void>> cellFactory = new Callback<TableColumn<TableBean, Void>, TableCell<TableBean, Void>>() {
//            @Override
//            public TableCell<TableBean, Void> call(final TableColumn<TableBean, Void> param) {
//                final TableCell<TableBean, Void> cell = new TableCell<TableBean, Void>() {
//
//                    private final Button btn = new Button("Join");
//
//                    {
//                        btn.setOnAction((ActionEvent event) -> {
//                            TableBean data = getTableView().getItems().get(getIndex());
//                            // TODO: create a real controller to join a table
//                            System.out.println("selectedData: " + data);
//                        });
//                    }
//
//                    @Override
//                    public void updateItem(Void item, boolean empty) {
//                        super.updateItem(item, empty);
//                        if (empty) {
//                            setGraphic(null);
//                        } else {
//                            setGraphic(btn);
//                        }
//                    }
//                };
//                return cell;
//            }
//        };
//        
//        tcAction.setCellFactory(cellFactory);
//    }
//    
//    private void initTable(ObservableList<TableBean> list) {
//    	
//    	tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//    
//    	tcTable.setCellValueFactory(new PropertyValueFactory<TableBean, String>("name"));
//    	tcCardGame.setCellValueFactory(new PropertyValueFactory<TableBean, String>("cardGame"));
//    	tcCity.setCellValueFactory(new PropertyValueFactory<TableBean, String>("city"));
//    	tcDate.setCellValueFactory(new PropertyValueFactory<TableBean, String>("date"));
//    	tcTime.setCellValueFactory(new PropertyValueFactory<TableBean, String>("time"));
//    	tcOrganizer.setCellValueFactory(new PropertyValueFactory<TableBean, String>("organizer"));
//    	
//    	tableView.setItems(list);
//    }
}
