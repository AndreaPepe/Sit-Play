package main.java.controller.guicontroller.userspage;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import main.java.controller.guicontroller.GuiBasicInternalPageController;
import main.java.engineering.utils.Session;

public class GuiPlayerUserPageController extends GuiBasicInternalPageController {


	public GuiPlayerUserPageController(Session ssn) {
		super(ssn);
		// TODO Auto-generated constructor stub
	}

	@FXML
    private AnchorPane apnBackground;

    @FXML
    private ToggleButton btnProfile;

    @FXML
    private ToggleButton btnNotifications;

    @FXML
    private AnchorPane apnProfile;

    @FXML
    private Label lblUsername;

    @FXML
    private AnchorPane apnNotifications;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		var tgGroup = new ToggleGroup();
		btnProfile.setToggleGroup(tgGroup);
		btnNotifications.setToggleGroup(tgGroup);
		// avoid unselected button
		tgGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
			if (newVal == null) {
				oldVal.setSelected(true);
			}
		});
		btnProfile.fire();
	}
	
	@FXML
	public void profilePressed(ActionEvent event) {
		apnNotifications.toBack();
		lblUsername.setText(ssn.getUser().getUsername());
		apnProfile.toFront();
	}
	
	@FXML
	public void notificationsPressed(ActionEvent event) {
		apnProfile.toBack();
		apnNotifications.toFront();
	}
    
    
}
