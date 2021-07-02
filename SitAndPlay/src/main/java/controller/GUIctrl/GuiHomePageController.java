package main.java.controller.GUIctrl;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import main.java.controller.GUIctrl.createTable.GuiPlayerCreateTableController;
import main.java.controller.GUIctrl.usersPage.GuiPlayerUserPageController;
import main.java.engineering.utils.Session;

public class GuiHomePageController extends GuiBasicController {
		
	
	public GuiHomePageController(Session ssn) {
		super(ssn);
		// TODO Auto-generated constructor stub
	}

	@FXML
	private AnchorPane basePane;
		
	@FXML
    private Button btnUser;

    @FXML
    private Button btnTables;

    @FXML
    private Button btnTournaments;

    @FXML
    private Button btnSettings;

    @FXML
    private Pane pnlStatus;

    @FXML
    private Label lblStatus;

    @FXML
    private Label lblMiniStatus;

    @FXML
    private Button btnClose;

       
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
//		btnUser.fire();
	}

	@FXML
	public void handleClicks(ActionEvent event) throws IOException {
		//TODO: add several content to display
		if (event.getSource() == btnUser) {
			lblMiniStatus.setText("User");
			lblStatus.setText("User");
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/java/view/standalone/usersPage/PlayerUserPage.fxml"));
			loader.setControllerFactory(c -> new GuiPlayerUserPageController(this.ssn));
			Parent root = loader.load();		
			pnPage.getChildren().removeAll();
			pnPage.getChildren().setAll(root);			
		}else if (event.getSource() == btnTables) {
			lblMiniStatus.setText("Tables");
			lblStatus.setText("Tables");
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/java/view/standalone/createTable/CreateTable.fxml"));
			loader.setControllerFactory(c -> new GuiPlayerCreateTableController(this.ssn));
			Parent root = loader.load();		
			pnPage.getChildren().removeAll();
			pnPage.getChildren().setAll(root);
			
		}else if (event.getSource() == btnTournaments) {
			lblMiniStatus.setText("Tournaments");
			lblStatus.setText("Tournaments");
			
		}else if (event.getSource() == btnSettings) {
			lblMiniStatus.setText("Settings");
			lblStatus.setText("User Settings");
			
		}
		
	}
	
	
	
	
	
}
