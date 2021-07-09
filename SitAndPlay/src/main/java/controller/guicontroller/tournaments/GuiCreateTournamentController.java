package main.java.controller.guicontroller.tournaments;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

public class GuiCreateTournamentController implements Initializable {

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

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfPlaceAutocomplete;

    @FXML
    private ComboBox<?> cbCardGame;

    @FXML
    private DatePicker dpDatePicker;

    @FXML
    private ComboBox<?> cbHours;

    @FXML
    private ComboBox<?> cbMin;

    @FXML
    private TextField tfMaxParticipants;

    @FXML
    private TextField tfPrice;

    @FXML
    private TextField tfAwards;

    @FXML
    private Button btnCreateTournament;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
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
		setMap();
		setAutocompleteTextField();
	}

	private void setMap() {
//		WebEngine engine = webViewCreate.getEngine();
		//TODO: engine.loadMap
	}
	
	private void setAutocompleteTextField() {
		//TODO: setTextField
	}
}
