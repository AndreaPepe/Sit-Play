package main.java.controller.guicontroller.login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.java.controller.applicationcontroller.LoginController;
import main.java.controller.guicontroller.GuiBasicController;
import main.java.controller.guicontroller.GuiHomePageBusinessmanController;
import main.java.controller.guicontroller.GuiHomePageOrganizerController;
import main.java.controller.guicontroller.GuiHomePagePlayerController;
import main.java.engineering.bean.login.BeanUser;
import main.java.engineering.exceptions.AlertFactory;
import main.java.engineering.exceptions.BeanCheckException;
import main.java.engineering.exceptions.DAOException;
import main.java.engineering.exceptions.WrongCredentialsExceptions;
import main.java.engineering.utils.Session;

public class GuiLoginController implements Initializable {

	private static final String REGISTRATION_PAGE = "/main/java/view/standalone/login/Registration.fxml";
	private static final String DAO_ERROR_MESSAGE = "Error with database, please retry";
	private static final String GENERIC_ERROR_MESSAGE = "Something went wrong, please retry";

	private static final String START_PAGE_PLAYER = "/main/java/view/standalone/HomePagePlayer.fxml";
	private static final String START_PAGE_ORGANIZER = "/main/java/view/standalone/HomePageOrganizer.fxml";
	private static final String START_PAGE_BUSINESSMAN = "/main/java/view/standalone/HomePageBusinessman.fxml";

	@FXML
	private AnchorPane basePane;

	@FXML
	private Label lblErrorUsername;

	@FXML
	private TextField tfUsername;

	@FXML
	private Label lblErrorPassword;

	@FXML
	private PasswordField pfPassword;

	@FXML
	private Button btnLogin;

	@FXML
	private Hyperlink hyRegistrate;

	@FXML
	private Button btnClose;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Nothing is needed
	}

	@FXML
	private void handleLogin(ActionEvent event) throws IOException {

		resetLabels();

		// create bean to store data
		var beanUser = new BeanUser();
		try {
			beanUser.setUsername(tfUsername.getText());
			beanUser.setPassword(pfPassword.getText());

			// new instance of application controller
			var ctrl = new LoginController();
			

			// create a new desktop Session -> isWeb = false
			var ssn = new Session(false);

			var loggedBean = ctrl.login(beanUser, ssn.isWeb());
			// Registration of the user into Session
			ssn.setUser(loggedBean);
			ssn.setHomePage();

			String startPage;
			GuiBasicController guiCtrl;
			switch (ssn.getUser().getUserType()) {
				case BUSINESSMAN: {
					startPage = START_PAGE_BUSINESSMAN;
					guiCtrl = new GuiHomePageBusinessmanController(ssn);
					break;
				}
				case ORGANIZER: {
					startPage = START_PAGE_ORGANIZER;
					guiCtrl = new GuiHomePageOrganizerController(ssn);
					break;
				}
				default:
					startPage = START_PAGE_PLAYER;
					guiCtrl = new GuiHomePagePlayerController(ssn);
					break;
				}

			var loader = new FXMLLoader(getClass().getResource(startPage));
			loader.setControllerFactory(c -> guiCtrl);
			Parent root = loader.load();
			// create a new scene with different dimensions
			var scene = new Scene(root);
			// retrieve the application Stage to set a new scene of different dimensions
			var appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			appStage.setTitle("Sit&Play");
			appStage.setScene(scene);
			appStage.setX(20);
			appStage.setY(20);
			appStage.show();

		} catch (BeanCheckException e) {
			AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).show();
		} catch (WrongCredentialsExceptions e) {
			lblErrorUsername.setText(e.getMessage());
			lblErrorUsername.setVisible(true);
			AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).showAndWait();
		} catch (DAOException e) {
			lblErrorUsername.setText(DAO_ERROR_MESSAGE);
			lblErrorUsername.setVisible(true);
			AlertFactory.getInstance().createAlert(e.getMessage(), AlertType.ERROR).showAndWait();
		} catch (Exception e) {
			lblErrorUsername.setText(GENERIC_ERROR_MESSAGE);
			lblErrorUsername.setVisible(true);
			AlertFactory.getInstance().createAlert(GENERIC_ERROR_MESSAGE, AlertType.ERROR).showAndWait();
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
					String.format("General Exception catched: %s", e.getMessage()));
		}
	}

	@FXML
	private void handleRegistration(ActionEvent event) throws IOException {
		var loader = new FXMLLoader(getClass().getResource(REGISTRATION_PAGE));
		loader.setControllerFactory(c -> new GuiRegistrationController());
		Parent root = loader.load();
		basePane.getChildren().removeAll();
		basePane.getChildren().setAll(root);
	}

	private void resetLabels() {
		lblErrorUsername.setText("");
		lblErrorUsername.setVisible(false);

		lblErrorPassword.setText("");
		lblErrorPassword.setVisible(false);

	}

}
