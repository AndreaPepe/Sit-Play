package main.java.controller.GUIctrl.usersPage;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import main.java.controller.GUIctrl.GuiBasicInternalPageController;
import main.java.engineering.utils.Session;

public class GuiPlayerUserPageController extends GuiBasicInternalPageController {


	public GuiPlayerUserPageController(Session ssn) {
		super(ssn);
		// TODO Auto-generated constructor stub
	}

	@FXML
    private AnchorPane apnBackground;

    @FXML
    private Button btnProfile;

    @FXML
    private Button btnNotifications;

    @FXML
    private AnchorPane apnProfile;

    @FXML
    private Label lblUsername;

    @FXML
    private AnchorPane apnNotifications;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		btnProfile.fire();
	}
	
	@FXML
	public void profilePressed(ActionEvent event) {
		apnNotifications.toBack();
		lblUsername.setText(ssn.getLoggedUser().getUsername());
		apnProfile.toFront();
	}
	
	@FXML
	public void notificationsPressed(ActionEvent event) {
		apnProfile.toBack();
		apnNotifications.toFront();
	}
    
    
}
